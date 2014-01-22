/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.edu.icm.coansys.webdemo.controller;

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
import pl.edu.icm.coansys.webdemo.docsim.dto.AuxiliaryDto;
import pl.edu.icm.coansys.webdemo.docsim.dto.DtoCreator;
import pl.edu.icm.coansys.webdemo.docsim.json.Input;
import pl.edu.icm.coansys.webdemo.docsim.json.Output;
import pl.edu.icm.coansys.webdemo.service.CitationMatchingService;
import pl.edu.icm.coansys.webdemo.service.DocumentSimarityService;

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
	@Autowired
	private DocumentSimarityService documentSimarityService;

	public void setCitationMatchingService(
			CitationMatchingService citationMatchingService) {
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

			return new ResponseEntity<String>(response, responseHeaders,
					HttpStatus.OK);
		} catch (Exception ex) {
			java.util.logging.Logger.getLogger(
					CoansysController.class.getName()).log(Level.SEVERE, null,
					ex);
			return new ResponseEntity<String>("Exception: " + ex.getMessage(),
					null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

    @RequestMapping(value = "/document_similarity.do", method = RequestMethod.POST)
    public ResponseEntity<String> documentSimilarity(@RequestBody String query,
            Model model) {
            logger.debug("the query: " + query);
            
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.setContentType(MediaType.APPLICATION_JSON);
            responseHeaders.add("Access-Control-Allow-Origin", "*");
            String response = "{\"outputObject\":{\"givenArticleDetails\":{\"doi\":\"10.3791/3308\",\"year\":\"2012\",\"title\":\"ImplantationofaCarotidCuffforTriggeringShear-stressInducedAtherosclerosisinMice\",\"authors\":[\"MichaelT.Kuhlmann\",\"SimonCuhlmann\",\"IrmgardHoppe\",\"RobKrams\",\"PaulC.Evans\",\"GustavJ.Strijkers\",\"KlaasNicolay\",\"SvenHermann\",\"MichaelSchäfers\"]},\"similarResults\":[{\"similarity\":\"0.9\",\"doi\":\"10.3791/9\",\"year\":\"2012\",\"title\":\"Implantation\",\"authors\":[\"MichaelT.Kuhlmann\"]},{\"similarity\":\"0.8\",\"doi\":\"10.3791/8\",\"year\":\"2012\",\"title\":\"of\",\"authors\":[\"SimonCuhlmann\"]},{\"similarity\":\"0.7\",\"doi\":\"10.3791/7\",\"year\":\"2012\",\"title\":\"a\",\"authors\":[\"IrmgardHoppe\"]},{\"similarity\":\"0.6\",\"doi\":\"10.3791/6\",\"year\":\"2012\",\"title\":\"CarotidCuff\",\"authors\":[\"RobKrams\"]},{\"similarity\":\"0.5\",\"doi\":\"10.3791/5\",\"year\":\"2012\",\"title\":\"forTriggering\",\"authors\":[\"PaulC.Evans\"]}]}}";
            return new ResponseEntity<String>(response, responseHeaders, HttpStatus.OK);
    }
}
