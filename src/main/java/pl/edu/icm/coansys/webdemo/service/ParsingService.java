/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.edu.icm.coansys.webdemo.service;

import org.springframework.stereotype.Component;

import pl.edu.icm.cermine.bibref.BibReferenceParser;
import pl.edu.icm.cermine.bibref.CRFBibReferenceParser;
import pl.edu.icm.cermine.exception.AnalysisException;
import pl.edu.icm.coansys.citations.data.MatchableEntity;

/**
 *
 * @author matfed
 */
@Component
public class ParsingService {
    BibReferenceParser parser;

    public ParsingService() throws AnalysisException {
        this.parser = new CRFBibReferenceParser(
                this.getClass().getResourceAsStream("/pl/edu/icm/cermine/bibref/acrf-small.ser.gz"));
    }
    
    public MatchableEntity parseCitation(String citationText) {
        return MatchableEntity.fromUnparsedReference(parser, "0", citationText);
    }
}
