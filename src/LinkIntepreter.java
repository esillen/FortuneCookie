import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;


public class LinkIntepreter {
	
	public static WordMatrix2d readFile(String filename){
		
		Scanner in = null;
		try {
			in = new Scanner(new FileReader(System.getProperty("user.dir")+"/"+filename));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		in.useDelimiter("\n");
		String wordsLine1 = in.next();
		String wordsLine2 = in.next();
		String[] parts1 = wordsLine1.split(" ");
		String[] parts2 = wordsLine2.split(" ");
		double matrix [][] = new double[parts1.length][parts2.length];
		int j = 0;
		for (int i=0;i<parts1.length;i++){
			j = 0;
			String numbersLine = in.next();
			String[] numbersArray = numbersLine.split(" ");
			for(String number:numbersArray){
				matrix[i][j] = Double.parseDouble(number);
				j+=1;
			}
		}
		return new WordMatrix2d(parts1,parts2,matrix);
	}
}
