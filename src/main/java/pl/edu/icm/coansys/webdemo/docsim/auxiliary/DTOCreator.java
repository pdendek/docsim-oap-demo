/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.edu.icm.coansys.webdemo.docsim.auxiliary;

import java.sql.SQLException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.icm.coansys.webdemo.docsim.auxiliary.DocSimInfoCollector.SecondDocument;

/**
 * 
 * @author pdendek
 */
public class DTOCreator {

	static Logger logger = LoggerFactory.getLogger(DTOCreator.class);

	public AuxiliaryDTO getAuxiliaryDTO(String doi,
			DocSimInfoCollector coll) throws SQLException {
		return getAuxiliaryDTO(doi, coll, 3);
	}
	
	private static AuxiliaryDTO getAuxiliaryDTO(String doi,
			DocSimInfoCollector coll, int fillType) throws SQLException {
		
		AuxiliaryDTO retPaper = new AuxiliaryDTO();
		
		if((fillType & 1) == 1 ){
			String[] doiYearTitle = coll.getDocument(doi);
			retPaper.doi = doiYearTitle[0];
			retPaper.year = doiYearTitle[1];
			retPaper.title = doiYearTitle[2];
			ArrayList<String> list = coll.getAuthors(doi);
			retPaper.authors = list.toArray(new String[list.size()]);
		}
		
		if((fillType & 2) == 2 ){
			ArrayList<SecondDocument> sims = coll.getSims(doi, 20);
			ArrayList<AuxiliaryDTO> secDocs = new ArrayList<AuxiliaryDTO>();
			for(SecondDocument sd : sims){
				if(sd == null) break;
				String doib = sd.doib;
				AuxiliaryDTO secD = getAuxiliaryDTO(doib, coll, 1);
				secD.sim = sd.sim;
				secDocs.add(secD);
			}
			retPaper.sims = secDocs.toArray(new AuxiliaryDTO[secDocs.size()]);
		}
		return retPaper;
	}
}
