import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

public class LinkIntepreter {
	
	public static WordMatrix2d readMatrixFile(String filename){
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
		in.close();
		return new WordMatrix2d(parts1,parts2,matrix);
	}
	
	public static FeatureSet readFeatureFile(String filename){
		FeatureSet featureSet = new FeatureSet();
		Scanner in = null;
		try {
			in = new Scanner(new FileReader(System.getProperty("user.dir")+"/"+filename));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		in.useDelimiter("\n");
		//Tense
		String line = in.next();
		String[] items = line.split(" ");
		featureSet.tenseProb[0] = Double.parseDouble(items[1]);
		featureSet.tenseProb[1] = Double.parseDouble(items[2]);
		featureSet.tenseProb[2] = Double.parseDouble(items[3]);
		
		//has_object
		line = in.next();
		items = line.split(" ");
		featureSet.has_objectProb[0] = Double.parseDouble(items[1]);
		featureSet.has_objectProb[1] = Double.parseDouble(items[2]);
		
		//has_adverb
		line = in.next();
		items = line.split(" ");
		featureSet.has_adverbProb[0] = Double.parseDouble(items[1]);
		featureSet.has_adverbProb[1] = Double.parseDouble(items[2]);
		
		//object_has_adjective
		line = in.next();
		items = line.split(" ");
		featureSet.object_has_adjectiveProb[0] = Double.parseDouble(items[1]);
		featureSet.object_has_adjectiveProb[1] = Double.parseDouble(items[2]);
		
		//object_has_adjective
		line = in.next();
		items = line.split(" ");
		featureSet.object_is_pluralProb[0] = Double.parseDouble(items[1]);
		featureSet.object_is_pluralProb[1] = Double.parseDouble(items[2]);
		
		//subject_has_adjective
		line = in.next();
		items = line.split(" ");
		featureSet.subject_has_adjectiveProb[0] = Double.parseDouble(items[1]);
		featureSet.subject_has_adjectiveProb[1] = Double.parseDouble(items[2]);
		
		//subject_has_adjective
		line = in.next();
		items = line.split(" ");
		featureSet.subject_is_pronounProb[0] = Double.parseDouble(items[1]);
		featureSet.subject_is_pronounProb[1] = Double.parseDouble(items[2]);
		
		//subject_has_adjective
		line = in.next();
		items = line.split(" ");
		featureSet.subject_pronoun_is_possessiveProb[0] = Double.parseDouble(items[1]);
		featureSet.subject_pronoun_is_possessiveProb[1] = Double.parseDouble(items[2]);
		
		//subject_has_adjective
		line = in.next();
		items = line.split(" ");
		featureSet.subject_is_definiteProb[0] = Double.parseDouble(items[1]);
		featureSet.subject_is_definiteProb[1] = Double.parseDouble(items[2]);
				
		//subject_has_adjective
		line = in.next();
		items = line.split(" ");
		featureSet.object_is_pronounProb[0] = Double.parseDouble(items[1]);
		featureSet.object_is_pronounProb[1] = Double.parseDouble(items[2]);
		
		//subject_has_adjective
		line = in.next();
		items = line.split(" ");
		featureSet.object_pronoun_is_possessiveProb[0] = Double.parseDouble(items[1]);
		featureSet.object_pronoun_is_possessiveProb[1] = Double.parseDouble(items[2]);
		
		//subject_has_adjective
		line = in.next();
		items = line.split(" ");
		featureSet.object_is_definiteProb[0] = Double.parseDouble(items[1]);
		featureSet.object_is_definiteProb[1] = Double.parseDouble(items[2]);

		in.close();
		
		return featureSet;
	}
	
	public static void writeToFile(WordMatrix2d wm,String filename){
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(System.getProperty("user.dir")+"/"+filename));
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
		StringBuilder str1 = new StringBuilder();
		for (int i=0;i<wm.words1.length-1;i++){
			str1.append(wm.words1[i]);
			str1.append(" ");
		}
		str1.append(wm.words1[wm.words1.length-1]);
		str1.append("\n");
		
		StringBuilder str2 = new StringBuilder();
		for (int i=0;i<wm.words2.length-1;i++){
			str2.append(wm.words2[i]);
			str2.append(" ");
		}
		str2.append(wm.words2[wm.words2.length-1]);
		str2.append("\n");
		
		StringBuilder str3 = new StringBuilder();
		
		for (int i=0;i<wm.matrix.length;i++){
			for (int j=0;j<wm.matrix[0].length-1;j++){
				str3.append(wm.matrix[i][j]);
				str3.append(" ");
			}
			str3.append(wm.matrix[i][wm.matrix[0].length-1]);
			str3.append("\n");
		}
		System.out.println(str3.toString());
		try {
			writer.write(str1.toString());
			writer.write(str2.toString());
			writer.write(str3.toString());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
}
