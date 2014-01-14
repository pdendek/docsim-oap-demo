/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.edu.icm.coansys.webdemo.controller;

import com.google.gson.Gson;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import pl.edu.icm.coansys.citations.data.MatchableEntity;
import pl.edu.icm.coansys.webdemo.data.json.Citation;
import pl.edu.icm.coansys.webdemo.data.json.ExtractedMetadata;
import pl.edu.icm.coansys.webdemo.data.json.MatchedDocument;
import pl.edu.icm.coansys.webdemo.data.json.MatchingRequest;
import pl.edu.icm.coansys.webdemo.data.json.MatchingResult;
import pl.edu.icm.coansys.webdemo.data.json.ResultEntry;
import pl.edu.icm.coansys.webdemo.service.ParsingService;

/**
 *
 * @author matfed
 */
@org.springframework.stereotype.Controller
public class CoansysController {
    Logger logger = LoggerFactory.getLogger(CoansysController.class);
    
    @Autowired
    private ParsingService parsingService;

    public void setParsingService(ParsingService parsingService) {
        this.parsingService = parsingService;
    }
    
    @RequestMapping(value = "/citations.do", method = RequestMethod.POST)
    public ResponseEntity<String> extractSync(@RequestBody String query,
            Model model) {
        try {
            logger.debug("the query: " + query);
            MatchingRequest req = MatchingRequest.fromJson(query);
            
            List<ResultEntry> results = new ArrayList<ResultEntry>();
            
            for (Citation cit : req.getCitations()) {
                String citationText = cit.getCitationText();
                logger.info(citationText);
                MatchableEntity parsed = parsingService.parseCitation(citationText);
                results.add(
                        new ResultEntry(citationText, 
                        new ExtractedMetadata(parsed.author(), parsed.year(), parsed.title(), parsed.source(), parsed.pages()), 
                        Collections.<MatchedDocument>emptyList()));
            }
            
            String response = new Gson().toJson(new MatchingResult(results));
            
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.setContentType(MediaType.APPLICATION_JSON);

            return new ResponseEntity<String>(response, responseHeaders, HttpStatus.OK);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(CoansysController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<String>("Exception: " + ex.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
}
