/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.edu.icm.coansys.webdemo.controller;

import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author matfed
 */
@org.springframework.stereotype.Controller
public class CoansysController {
    Logger logger = LoggerFactory.getLogger(CoansysController.class);
    
    @RequestMapping(value = "/citations.do")
    public ResponseEntity<String> extractSync(@RequestParam("q") String query,
            Model model) {
        try {
            logger.debug("the query: " + query);
            
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.setContentType(MediaType.APPLICATION_JSON);
            String response = "{\n" +
"  \"results\":[\n" +
"    {\n" +
"      \"extracted-metadata\":{\n" +
"        \"author\":\"Copernicus, N.\",\n" +
"        \"year\":\"1543\",\n" +
"        \"title\":\"De revolutionibus orbium coelestium\"\n" +
"      },\n" +
"      \"matched-documents\":[\n" +
"        {\n" +
"          \"metadata\":{\n" +
"            \"id\":\"doc1\",\n" +
"            \"author\":\"Nicolaus Copernicus\",\n" +
"            \"year\":\"1543\",\n" +
"            \"title\":\"De revolutionibus orbium coelestium\"\n" +
"          },\n" +
"          \"score\":0.95\n" +
"        },\n" +
"        {\n" +
"          \"metadata\":{\n" +
"            \"id\":\"doc123\"\n" +
"          },\n" +
"          \"score\":0.6\n" +
"        }\n" +
"      ]\n" +
"    },\n" +
"    {\n" +
"      \"extracted-metadata\":{\n" +
"        \"author\":\"Newton, I.\",\n" +
"        \"year\":\"1687\",\n" +
"        \"title\":\"Philosophiae naturalis principia mathematica\"\n" +
"      },\n" +
"      \"matched-documents\":[\n" +
"\n" +
"      ]\n" +
"    }\n" +
"  ]\n" +
"}";
            return new ResponseEntity<String>(response, responseHeaders, HttpStatus.OK);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(CoansysController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<String>("Exception: " + ex.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
}
