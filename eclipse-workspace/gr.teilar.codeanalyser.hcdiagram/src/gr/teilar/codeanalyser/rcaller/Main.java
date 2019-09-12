package gr.teilar.codeanalyser.rcaller;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.github.rcaller.rstuff.RCaller;
import com.github.rcaller.rstuff.RCode;

public class Main {

	public static void main(String[] args) {
		try {
			RCaller caller = RCaller.create();
			RCode code = RCode.create();

			String path = System.getProperty("user.home") + "\\output.txt";
			path = path.replace("\\", "\\\\");
			
			code.addString("path", path);

			code.addRCode("input <- read.csv(path, sep = ' ', header = TRUE)");
			code.addRCode("dist_mat <- dist(as.matrix(input))");
			code.addRCode("hclust_ave <- hclust(dist_mat, method = 'average')");
			
			File file = code.startPlot();
			code.addRCode("plot(hclust_ave,labels=colnames(input))");
			code.endPlot();

			caller.setRCode(code);
	      
			caller.runOnly();
			code.showPlot(file);
		} catch (Exception e) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, e.getMessage());
	    }
	}

}
