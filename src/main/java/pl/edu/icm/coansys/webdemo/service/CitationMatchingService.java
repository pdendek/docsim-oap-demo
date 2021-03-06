/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.icm.coansys.webdemo.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;


import org.springframework.stereotype.Component;

import pl.edu.icm.cermine.bibref.CRFBibReferenceParser;
import pl.edu.icm.cermine.exception.AnalysisException;
import pl.edu.icm.coansys.citations.data.MatchableEntity;
import pl.edu.icm.coansys.citations.data.SimilarityMeasurer;
import pl.edu.icm.coansys.webdemo.data.json.DocumentMetadata;
import pl.edu.icm.coansys.webdemo.data.json.ExtractedMetadata;
import pl.edu.icm.coansys.webdemo.data.json.MatchedDocument;
import pl.edu.icm.coansys.webdemo.data.json.ResultEntry;
import scala.collection.JavaConverters;

import static pl.edu.icm.coansys.citations.util.misc.digitsNormaliseTokenise;
import static pl.edu.icm.coansys.citations.util.misc.approximateYear;
import static pl.edu.icm.coansys.citations.util.misc.lettersNormaliseTokenise;
/**
 *
 * @author matfed
 */
@Component
public class CitationMatchingService {
    private static class MatchedDocumentComparator implements Comparator<MatchedDocument> {
        @Override
        public int compare(MatchedDocument o1, MatchedDocument o2) {
            return -Double.compare(o1.getScore(), o2.getScore());
        }
    }
    
    @Value("${citations.similarity.threshold}")
    private double similarityThreshold;
    @Value("${citations.max.matched.documents}")
    private int maxMatchedDocs;
    private SolrServer solrServer;

    @Autowired
    public void setSolrServer(SolrServer solrServer) {
        this.solrServer = solrServer;
    }
    
    public ResultEntry matchCitation(String citationText) throws AnalysisException, SolrServerException {
        CRFBibReferenceParser parser = new CRFBibReferenceParser(
                this.getClass().getResourceAsStream("/pl/edu/icm/cermine/bibref/acrf-small.ser.gz"));

        MatchableEntity parsed = MatchableEntity.fromUnparsedReference(parser, "0", citationText);
        List<DocumentMetadata> candidates = getHeuristicallyMatching(parsed);
        List<MatchedDocument> matched = new ArrayList<MatchedDocument>();
        SimilarityMeasurer measurer = new SimilarityMeasurer(SimilarityMeasurer.advancedFvBuilder());
        for (DocumentMetadata doc : candidates) {
            MatchableEntity entity = doc.toMatchableEntity();
            double score = measurer.similarity(parsed, entity);
            if (score >= similarityThreshold) {
                matched.add(new MatchedDocument(score, doc));
            }
        }
        
        Collections.sort(matched, new MatchedDocumentComparator());

        return new ResultEntry(citationText, 
                               ExtractedMetadata.fromMatchableEntity(parsed), 
                               matched.subList(0, Math.min(maxMatchedDocs, matched.size())));
    }
    
    public List<DocumentMetadata> getHeuristicallyMatching(MatchableEntity entity) throws SolrServerException {
        String text = entity.rawText().get();
        
        Set<String> years = new HashSet<String>();
        for(String year : JavaConverters.asJavaListConverter(digitsNormaliseTokenise(text)).asJava()) {
            for (String approx : JavaConverters.asJavaListConverter(approximateYear(year)).asJava()) {
                if (approx.length() == 4) {
                    years.add(approx);
                }
            }
        }
        
        String yearStr = StringUtils.join(years, " ");
        
        String authorStr = StringUtils.join(JavaConverters.asJavaSetConverter(lettersNormaliseTokenise(text).toSet()).asJava(), " ");
        if (StringUtils.isBlank(yearStr) || StringUtils.isBlank(authorStr)) {
            return Collections.<DocumentMetadata>emptyList();
        }
        String queryStr = "author_idxd:(" + authorStr + ") AND year_idxd:(" + yearStr + ")";
        SolrQuery query = new SolrQuery(queryStr);        
        QueryResponse rsp = solrServer.query(query);
        return rsp.getBeans(DocumentMetadata.class);
    }
}
