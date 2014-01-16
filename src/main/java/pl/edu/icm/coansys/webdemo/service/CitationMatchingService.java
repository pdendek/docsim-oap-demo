/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.icm.coansys.webdemo.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import pl.edu.icm.cermine.bibref.CRFBibReferenceParser;
import pl.edu.icm.cermine.exception.AnalysisException;
import pl.edu.icm.coansys.citations.data.MatchableEntity;
import pl.edu.icm.coansys.webdemo.data.json.DocumentMetadata;
import pl.edu.icm.coansys.webdemo.data.json.ExtractedMetadata;
import pl.edu.icm.coansys.webdemo.data.json.MatchedDocument;
import pl.edu.icm.coansys.webdemo.data.json.ResultEntry;

/**
 *
 * @author matfed
 */
@Component
public class CitationMatchingService {

    public ResultEntry matchCitation(String citationText) throws AnalysisException {
        CRFBibReferenceParser parser = new CRFBibReferenceParser(
                this.getClass().getResourceAsStream("/pl/edu/icm/cermine/bibref/acrf-small.ser.gz"));

        MatchableEntity parsed = MatchableEntity.fromUnparsedReference(parser, "0", citationText);
        List<MatchedDocument> matched = new ArrayList<MatchedDocument>();
        matched.add(new MatchedDocument(0.95, new DocumentMetadata("doc1", "10.1000/182", "Nicolaus Copernicus", "1543", "De revolutionibus orbium coelestium", null, null)));
        matched.add(new MatchedDocument(0.6, new DocumentMetadata("doc123", null, null, null, null, null, null)));

        return new ResultEntry(
                citationText,
                new ExtractedMetadata(parsed.author(), parsed.year(), parsed.title(), parsed.source(), parsed.pages()),
                matched);
    }
}
