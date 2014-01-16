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
import pl.edu.icm.coansys.webdemo.data.json.DocumentMetadata;
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
    
    @RequestMapping(value = "/citation_matching.do", method = RequestMethod.POST)
    public ResponseEntity<String> citationMatching(@RequestBody String query,
            Model model) {
        try {
            logger.debug("the query: " + query);
            MatchingRequest req = MatchingRequest.fromJson(query);
            
            List<ResultEntry> results = new ArrayList<ResultEntry>();
            
            for (Citation cit : req.getCitations()) {
                String citationText = cit.getCitationText();
                logger.info(citationText);
                MatchableEntity parsed = parsingService.parseCitation(citationText);
                List<MatchedDocument> matched = new ArrayList<MatchedDocument>();
                matched.add(new MatchedDocument(0.95, new DocumentMetadata("doc1", "10.1000/182", "Nicolaus Copernicus", "1543", "De revolutionibus orbium coelestium", null, null)));
                matched.add(new MatchedDocument(0.6, new DocumentMetadata("doc123", null, null, null, null, null, null)));
                
                results.add(new ResultEntry(
                        citationText, 
                        new ExtractedMetadata(parsed.author(), parsed.year(), parsed.title(), parsed.source(), parsed.pages()), 
                        matched));
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
    @RequestMapping(value = "/document_similarity.do", method = RequestMethod.POST)
    public ResponseEntity<String> documentSimilarity(@RequestBody String query,
            Model model) {
            logger.debug("the query: " + query);
            
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.setContentType(MediaType.APPLICATION_JSON);

            String response = "{\r\n" +
"    \"outputObject\": {\r\n" +
"        \"givenArticleDetails\": {\r\n" +
"            \"doi\": \"10.3791/3308\",\r\n" +
"            \"linkAddress\": \"http://www.lubie-kraby.pl/krolestwo-za-kraby,0\",\r\n" +
"            \"year\": \"2012\",\r\n" +
"            \"title\": \"Implantation of a Carotid Cuff for Triggering Shear-stress Induced Atherosclerosis in Mice\",\r\n" +
"            \"authors\": [\r\n" +
"                \"Michael T. Kuhlmann\",\r\n" +
"                \"Simon Cuhlmann\",\r\n" +
"                \"Irmgard Hoppe\",\r\n" +
"                \"Rob Krams\",\r\n" +
"                \"Paul C. Evans\",\r\n" +
"                \"Gustav J. Strijkers\",\r\n" +
"                \"Klaas Nicolay\",\r\n" +
"                \"Sven Hermann\",\r\n" +
"                \"Michael SchĂ¤fers\"\r\n" +
"            ],\r\n" +
"            \"abstract\": \"It is widely accepted that alterations in vascular shear stress trigger the expression of inflammatory genes in endothelial cells...\"\r\n" +
"        },\r\n" +
"        \"similarResults\": [\r\n" +
"            {\r\n" +
"                \"similarity\": \"0.9\",\r\n" +
"                \"doi\": \"10.3791/9\",\r\n" +
"                \"linkAddress\": \"http://www.lubie-kraby.pl/9\",\r\n" +
"                \"year\": \"2012\",\r\n" +
"                \"title\": \"Implantation\",\r\n" +
"                \"authors\": [\r\n" +
"                    \"Michael T. Kuhlmann\"\r\n" +
"                ],\r\n" +
"                \"abstract\": \"It is\"\r\n" +
"            },\r\n" +
"            {\r\n" +
"                \"similarity\": \"0.8\",\r\n" +
"                \"doi\": \"10.3791/8\",\r\n" +
"                \"linkAddress\": \"http://www.lubie-kraby.pl/8\",\r\n" +
"                \"year\": \"2012\",\r\n" +
"                \"title\": \"of\",\r\n" +
"                \"authors\": [\r\n" +
"                    \"Simon Cuhlmann\"\r\n" +
"                ],\r\n" +
"                \"abstract\": \"widely accepted\"\r\n" +
"            },\r\n" +
"            {\r\n" +
"                \"similarity\": \"0.7\",\r\n" +
"                \"doi\": \"10.3791/7\",\r\n" +
"                \"linkAddress\": \"http://www.lubie-kraby.pl/7\",\r\n" +
"                \"year\": \"2012\",\r\n" +
"                \"title\": \"a\",\r\n" +
"                \"authors\": [\r\n" +
"                    \"Irmgard Hoppe\"\r\n" +
"                ],\r\n" +
"                \"abstract\": \"that alterations\"\r\n" +
"            },\r\n" +
"            {\r\n" +
"                \"similarity\": \"0.6\",\r\n" +
"                \"doi\": \"10.3791/6\",\r\n" +
"                \"linkAddress\": \"http://www.lubie-kraby.pl/6\",\r\n" +
"                \"year\": \"2012\",\r\n" +
"                \"title\": \"Carotid Cuff\",\r\n" +
"                \"authors\": [\r\n" +
"                    \"Rob Krams\"\r\n" +
"                ],\r\n" +
"                \"abstract\": \"in vascular\"\r\n" +
"            },\r\n" +
"            {\r\n" +
"                \"similarity\": \"0.5\",\r\n" +
"                \"doi\": \"10.3791/5\",\r\n" +
"                \"linkAddress\": \"http://www.lubie-kraby.pl/5\",\r\n" +
"                \"year\": \"2012\",\r\n" +
"                \"title\": \"for Triggering\",\r\n" +
"                \"authors\": [\r\n" +
"                    \"Paul C. Evans\"\r\n" +
"                ],\r\n" +
"                \"abstract\": \"shear stress\"\r\n" +
"            }\r\n" +
"        ]\r\n" +
"    }\r\n" +
"}\r\n";
            
            return new ResponseEntity<String>(response, responseHeaders, HttpStatus.OK);
    }
}
