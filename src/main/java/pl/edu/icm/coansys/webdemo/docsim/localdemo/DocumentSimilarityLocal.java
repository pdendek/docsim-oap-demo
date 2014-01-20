/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.edu.icm.coansys.webdemo.docsim.localdemo;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.icm.coansys.webdemo.docsim.dto.AuxiliaryDto;
import pl.edu.icm.coansys.webdemo.docsim.dto.DtoCreator;
import pl.edu.icm.coansys.webdemo.docsim.json.Input;
import pl.edu.icm.coansys.webdemo.docsim.json.Output;
import pl.edu.icm.coansys.webdemo.service.DocumentSimarityService;

import com.google.gson.Gson;

/**
 * 
 * @author pdendek
 */
public class DocumentSimilarityLocal {

	Logger logger = LoggerFactory.getLogger(DocumentSimilarityLocal.class);

	public static void main(String[] args) throws SQLException {
		/* input parsing */
		String in = "{ \"inputObject\" : { \"doi\": \"10.1208/s12248-007-9000-9\" }}";
		Input injson = new Gson().fromJson(in, Input.class);
		String doi = injson.getInputObject().getDoi();

		/* initialize connection with db */
		DocumentSimarityService coll = new DocumentSimarityService().init();

		/* communicate with db and construct the result */
		DtoCreator dtoCreator = new DtoCreator();
		AuxiliaryDto auxDto = dtoCreator.getAuxiliaryDTO(doi, coll);
		Output o = auxDto.toOutput();
		String response = new Gson().toJson(o, Output.class);

		System.out.println("the response: " + response);

		/* shutdown connection with db */
		coll.tearDown();

		/* return results */
		System.out.println(response);
	}
}
