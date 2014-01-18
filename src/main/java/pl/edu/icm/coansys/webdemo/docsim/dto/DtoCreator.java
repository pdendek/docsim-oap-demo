/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.edu.icm.coansys.webdemo.docsim.dto;

import java.sql.SQLException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.icm.coansys.webdemo.service.DocumentSimarityService;
import pl.edu.icm.coansys.webdemo.service.DocumentSimarityService.SecondDocument;

/**
 * 
 * @author pdendek
 */
public class DtoCreator {

	static Logger logger = LoggerFactory.getLogger(DtoCreator.class);

	public AuxiliaryDto getAuxiliaryDTO(String doi,
			DocumentSimarityService coll) throws SQLException {
		return getAuxiliaryDTO(doi, coll, 3);
	}
	
	private static AuxiliaryDto getAuxiliaryDTO(String doi,
			DocumentSimarityService documentSimarityService, int fillType) throws SQLException {
		
		AuxiliaryDto retPaper = new AuxiliaryDto();
		
		if((fillType & 1) == 1 ){
			String[] doiYearTitle = documentSimarityService.getDocument(doi);
			retPaper.doi = doiYearTitle[0];
			retPaper.year = doiYearTitle[1];
			retPaper.title = doiYearTitle[2];
			ArrayList<String> list = documentSimarityService.getAuthors(doi);
			retPaper.authors = list.toArray(new String[list.size()]);
		}
		
		if((fillType & 2) == 2 ){
			ArrayList<SecondDocument> sims = documentSimarityService.getSims(doi, 20);
			ArrayList<AuxiliaryDto> secDocs = new ArrayList<AuxiliaryDto>();
			for(SecondDocument sd : sims){
				if(sd == null) break;
				String doib = sd.doib;
				AuxiliaryDto secD = getAuxiliaryDTO(doib, documentSimarityService, 1);
				secD.sim = sd.sim;
				secDocs.add(secD);
			}
			retPaper.sims = secDocs.toArray(new AuxiliaryDto[secDocs.size()]);
		}
		return retPaper;
	}
}
