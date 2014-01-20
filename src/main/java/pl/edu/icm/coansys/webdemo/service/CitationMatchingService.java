/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.icm.coansys.webdemo.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;


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
import scala.Function1;
/**
 *
 * @author matfed
 */
@Component
public class CitationMatchingService {
    private static final String SOLR_URL = "http://localhost:8983/solr/citation-matching/";
    private SolrServer solrServer = new HttpSolrServer(SOLR_URL);
    
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
            if (score > 0.0) {
                matched.add(new MatchedDocument(score, doc));
            }
        }

        return new ResultEntry(citationText, ExtractedMetadata.fromMatchableEntity(parsed), matched);
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
                
        String queryStr = "author_idxd:(" + authorStr + ") AND year_idxd:(" + yearStr + ")";
        SolrQuery query = new SolrQuery(queryStr);        
        QueryResponse rsp = solrServer.query(query);
        return rsp.getBeans(DocumentMetadata.class);
//        List<DocumentMetadata> result = new ArrayList<DocumentMetadata>();
//        
//        result.add(new DocumentMetadata("doc1", "10.1000/182", "Nicolaus Copernicus", "1543", "De revolutionibus orbium coelestium", null, null));
//        result.add(new DocumentMetadata("doc123", null, null, null, null, null, null));
//        
//        return result;
    }
}
