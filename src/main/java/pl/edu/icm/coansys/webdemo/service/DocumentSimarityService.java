package pl.edu.icm.coansys.webdemo.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import pl.edu.icm.coansys.commons.java.StackTraceExtractor;
import pl.edu.icm.coansys.webdemo.docsim.constants.DocumentSimilarityConstants;

@Component
public class DocumentSimarityService {

	/**
	 * 
	 * @author pdendek
	 * 
	 *         The class contains information about seconds document doi and its
	 *         similarity to the first one.
	 */
	public static class SecondDocument {
		public String doib;
		public float sim;

		public SecondDocument(String doib, float sim) {
			this.doib = doib;
			this.sim = sim;
		}

		final static Comparator<SecondDocument> asc = new Comparator<SecondDocument>() {
			@Override
			public int compare(SecondDocument o1, SecondDocument o2) {
				return (o1.sim - o2.sim > 0 ? 1 : -1);
			}
		};

		final static Comparator<SecondDocument> desc = new Comparator<SecondDocument>() {
			@Override
			public int compare(SecondDocument o1, SecondDocument o2) {
				return -(o1.sim - o2.sim > 0 ? 1 : -1);
			}
		};
	}

	static Logger logger = LoggerFactory
			.getLogger(DocumentSimarityService.class);

	@Value("${docsim.postgres.dbhost}")
	String dbhost = null;
	@Value("${docsim.postgres.dbname}")
	String dbname = null;
	@Value("${docsim.postgres.dbuser}")
	String dbuser = null;
	@Value("${docsim.postgres.dbpasswd}")
	String dbpasswd = null;

	Connection con = null;

	public String getDbhost() {
		return dbhost;
	}

	public void setDbhost(String dbhost) {
		this.dbhost = dbhost;
	}

	public String getDbname() {
		return dbname;
	}

	public void setDbname(String dbname) {
		this.dbname = dbname;
	}

	public String getDbuser() {
		return dbuser;
	}

	public void setDbuser(String dbuser) {
		this.dbuser = dbuser;
	}

	public String getDbpasswd() {
		return dbpasswd;
	}

	public void setDbpasswd(String dbpasswd) {
		this.dbpasswd = dbpasswd;
	}

	public Connection getCon() {
		return con;
	}

	public void setCon(Connection con) {
		this.con = con;
	}

	public DocumentSimarityService(String dbhost, String dbname, 
			String dbuser, String dbpasswd) {
		this.dbhost = dbhost;
		this.dbname = dbname;
		this.dbuser = dbuser;
		this.dbpasswd = dbpasswd;
	}

	public DocumentSimarityService() {
		this.dbhost = "localhost";
		this.dbname = "docsimtestdb";
		this.dbuser = "postgres";
		this.dbpasswd = "postgres";
	}

	@PostConstruct
	public DocumentSimarityService init() throws SQLException, ClassNotFoundException {
		getConnection();
		return this;
	}

	@PreDestroy
	public void tearDown() {
		try {
			if (con != null) {
				con.close();
			}

		} catch (SQLException ex) {
			logger.error(StackTraceExtractor.getStackTrace(ex));
		}
	}
	
	synchronized public void getConnection() throws SQLException, ClassNotFoundException {
		if (con == null) {
                        Class.forName("org.postgresql.Driver");
			String url = "jdbc:postgresql://" + dbhost + "/" + dbname;
			con = DriverManager.getConnection(url, dbuser, dbpasswd);
		}
	}

/**
	 * 
	 * @param doi 
	 * @param maxNum number of desired similar elements
	 * @return returns an array of objects of class {@link SecondDocument), sorted in descending order  
	 * @throws SQLException
	 */
	public ArrayList<SecondDocument> getSims(String doi, int maxNum)
			throws SQLException {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = con.prepareStatement(DocumentSimilarityConstants.QUERY_DOCUMENT_SIMS);
			st.setString(1, doi);
			st.setInt(2, maxNum);
			rs = st.executeQuery();

			ArrayList<SecondDocument> ret = new ArrayList<SecondDocument>();
			String doib = null;
			Float sim = null;

			while (rs.next()) {
				doib = rs.getString("doib");
				sim = rs.getFloat("sim");
				ret.add(new SecondDocument(doib, sim));
			}
			rs.close();
			st.close();
			return ret;
		} catch (SQLException ex) {
			logger.error(StackTraceExtractor.getStackTrace(ex));
			return null;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
			} catch (SQLException ex) {
				logger.error(StackTraceExtractor.getStackTrace(ex));
			}
		}
	}

	/**
	 * 
	 * @param doi
	 * @return returns a String array of a name of authors
	 * @throws SQLException
	 */
	public ArrayList<String> getAuthors(String doi) throws SQLException {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = con.prepareStatement(DocumentSimilarityConstants.QUERY_DOCUMENT_AUTHORS);
			st.setString(1, doi);
			rs = st.executeQuery();

			ArrayList<String> names = new ArrayList<String>();
			while (rs.next()) {
				names.add(rs.getString("name"));
			}
			return names;
		} catch (SQLException ex) {
			logger.error(StackTraceExtractor.getStackTrace(ex));
			return null;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}

			} catch (SQLException ex) {
				logger.error(StackTraceExtractor.getStackTrace(ex));
			}
		}
	}

	/**
	 * 
	 * @param doi
	 * @return returns a String array containing document doi, year and title
	 * @throws SQLException
	 */
	public String[] getDocument(String doi) throws SQLException {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = con.prepareStatement(DocumentSimilarityConstants.QUERY_DOCUMENT_MAIN_DATA);
			st.setString(1, doi);
			rs = st.executeQuery();

			String year = null, title = null;

			if (rs.next()) {
				year = rs.getString("year");
				title = rs.getString("title");
			}
			rs.close();
			st.close();

			return new String[] { doi, year, title };
		} catch (SQLException ex) {
			logger.error(StackTraceExtractor.getStackTrace(ex));
			return null;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
			} catch (SQLException ex) {
				logger.error(StackTraceExtractor.getStackTrace(ex));
			}
		}
	}
}
