package pl.edu.icm.coansys.webdemo.docsim.auxiliary;

public class Output {
	
	public OutputObject outputObject;
	
	public OutputObject getOutputObject() {
		return outputObject;
	}

	public void setOutputObject(OutputObject outputObject) {
		this.outputObject = outputObject;
	}

	public static class OutputObject {
		public MainPaper givenArticleDetails;
		public OtherPaper[] similarResults;
		
		public MainPaper getGivenArticleDetails() {
			return givenArticleDetails;
		}

		public void setGivenArticleDetails(MainPaper givenArticleDetails) {
			this.givenArticleDetails = givenArticleDetails;
		}
		
		public OtherPaper[] getSimilarResults() {
			return similarResults;
		}

		public void setSimilarResults(OtherPaper[] similarResults) {
			this.similarResults = similarResults;
		}

		public OutputObject(){}
	}
	
	public static abstract class Paper {
		public String doi;
		public String year;
		public String title;
		public String[] authors;
	}
	
	public static class MainPaper extends Paper{
		public MainPaper(){}
	}
	
	public static class OtherPaper extends Paper{
		public float sim;
		public OtherPaper(){}
	}
}
