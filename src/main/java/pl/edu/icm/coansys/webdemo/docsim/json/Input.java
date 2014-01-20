package pl.edu.icm.coansys.webdemo.docsim.json;

public class Input {
	
	public InputObject inputObject;
	
	public InputObject getInputObject() {
		return inputObject;
	}

	public void setInputObject(InputObject inputObject) {
		this.inputObject = inputObject;
	}



	public static class InputObject{
		public String doi;

		public InputObject(){
			
		}
		
		public String getDoi() {
			return doi;
		}

		public void setDoi(String doi) {
			this.doi = doi;
		}
		
		public String toString(){
			return doi;
		}
	}
	
	public String toString(){
		return "{ inputObject : { doi : "
	+ inputObject.doi+" }}";
	}
}
