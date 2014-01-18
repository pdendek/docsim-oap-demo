package pl.edu.icm.coansys.webdemo.docsim.auxiliary;

import pl.edu.icm.coansys.webdemo.docsim.auxiliary.Output.MainPaper;
import pl.edu.icm.coansys.webdemo.docsim.auxiliary.Output.OtherPaper;
import pl.edu.icm.coansys.webdemo.docsim.auxiliary.Output.OutputObject;

public class AuxiliaryDTO {
		public float sim = -1;
		public String doi;
		public String year;
		public String title;
		public String[] authors;
		public AuxiliaryDTO[] sims;
		
		public OtherPaper toOtherPaper(){
			OtherPaper op = new OtherPaper();
			op.sim = sim;
			op.doi = doi;
			op.year = year;
			op.title = title;
			op.authors = authors;
			return op;
		}
		
		public Output toOutput(){
			MainPaper mp = new MainPaper();
			mp.doi = doi;
			mp.year = year;
			mp.title = title;
			mp.authors = authors;
			
			OtherPaper[] op = new OtherPaper[sims.length];
			int idx = 0;
			for(AuxiliaryDTO adto : sims){
				op[idx] = adto.toOtherPaper();
				idx++;
			}
			
			OutputObject oo = new OutputObject();
			oo.setGivenArticleDetails(mp);
			oo.setSimilarResults(op);
			Output o = new Output();
			o.setOutputObject(oo);
			return o;
		}
}
