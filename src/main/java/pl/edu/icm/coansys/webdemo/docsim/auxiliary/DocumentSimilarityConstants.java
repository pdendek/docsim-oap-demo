package pl.edu.icm.coansys.webdemo.docsim.auxiliary;


public class DocumentSimilarityConstants {

	public final static String QUERY_DOCUMENT_MAIN_DATA = 
			"SELECT * FROM DOCUMENTS WHERE DOI=?";
	public final static String QUERY_DOCUMENT_AUTHORS = 
			"SELECT name FROM  AUTHORS WHERE DOI=?";
	public final static String QUERY_DOCUMENT_SIMS = 
			"SELECT doib,sim FROM SIMS WHERE doia=? ORDER BY sim DESC LIMIT ?";
	
	
	public final static int DOCUMENT_INFO = 1;
	public final static int SIMILAR_DOCS = 2; 
}
