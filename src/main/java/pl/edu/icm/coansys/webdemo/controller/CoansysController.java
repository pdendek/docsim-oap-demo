/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.edu.icm.coansys.webdemo.controller;

import java.sql.SQLException;
import java.util.ArrayList;
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

import pl.edu.icm.coansys.commons.java.StackTraceExtractor;
import pl.edu.icm.coansys.webdemo.data.json.Citation;
import pl.edu.icm.coansys.webdemo.data.json.MatchingRequest;
import pl.edu.icm.coansys.webdemo.data.json.MatchingResult;
import pl.edu.icm.coansys.webdemo.data.json.ResultEntry;
import pl.edu.icm.coansys.webdemo.docsim.auxiliary.AuxiliaryDTO;
import pl.edu.icm.coansys.webdemo.docsim.auxiliary.DTOCreator;
import pl.edu.icm.coansys.webdemo.docsim.auxiliary.DocSimInfoCollector;
import pl.edu.icm.coansys.webdemo.docsim.auxiliary.Input;
import pl.edu.icm.coansys.webdemo.docsim.auxiliary.Output;
import pl.edu.icm.coansys.webdemo.service.CitationMatchingService;

import com.google.gson.Gson;

/**
 * 
 * @author matfed, pdendek
 */
@org.springframework.stereotype.Controller
public class CoansysController {

    Logger logger = LoggerFactory.getLogger(CoansysController.class);
    
    @Autowired
    private CitationMatchingService citationMatchingService;

    public void setCitationMatchingService(CitationMatchingService citationMatchingService) {
        this.citationMatchingService = citationMatchingService;
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
                
                results.add(citationMatchingService.matchCitation(citationText));
            }
            
            String response = new Gson().toJson(new MatchingResult(results));
            
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.setContentType(MediaType.APPLICATION_JSON);
            responseHeaders.add("Access-Control-Allow-Origin", "*");

            return new ResponseEntity<String>(response, responseHeaders, HttpStatus.OK);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(CoansysController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<String>("Exception: " + ex.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	public static void main(String[] args) throws SQLException {
		/* input parsing */
		String in = "{ \"inputObject\" : { \"doi\": \"10.1208/s12248-007-9000-9\" }}";
		Input injson = new Gson().fromJson(in, Input.class);
		String doi = injson.getInputObject().getDoi();
		
		/* initialize connection with db */
		DocSimInfoCollector coll = new DocSimInfoCollector().init();

		/* communicate with db and construct the result */
		DTOCreator dtoCreator = new DTOCreator();
		AuxiliaryDTO auxDto = dtoCreator.getAuxiliaryDTO(doi, coll);
		Output o = auxDto.toOutput();
		String response = new Gson().toJson(o, Output.class);
		
		System.out.println("the response: " + response);

		/* shutdown connection with db */
		coll.tearDown();

		/* return results */
		System.out.println(response);
	}

	@RequestMapping(value = "/document_similarity.do", method = RequestMethod.POST)
	public ResponseEntity<String> documentSimilarity(@RequestBody String query,
			Model model) {

		String response = null;
		logger.debug("the query: " + query);

		/* input parsing */
		Input input = new Gson().fromJson(query, Input.class);
		String doi = input.getInputObject().getDoi();

		DocSimInfoCollector coll;
		try {
			/* initialize connection with db */
			coll = new DocSimInfoCollector().init();
			
			/* communicate with db and construct the result */
			DTOCreator dtoCreator = new DTOCreator();
			AuxiliaryDTO auxDto = dtoCreator.getAuxiliaryDTO(doi, coll);
			Output o = auxDto.toOutput();
			response = new Gson().toJson(o, Output.class);
			logger.debug("the response: " + response);

			/* shutdown connection with db */
			coll.tearDown();
		} catch (SQLException e) {
			String error = StackTraceExtractor.getStackTrace(e);
			logger.error(error);
			response = "{ \"error\" : \"" + e.getMessage() + "\"}";
		}
		
		/* respond with the constructed result */
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType.APPLICATION_JSON);
		return new ResponseEntity<String>(response, responseHeaders,
				HttpStatus.OK);
	}


}
