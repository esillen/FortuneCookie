import java.util.Arrays;
import java.util.Random;


public class WordMatrix2d {

	public String [] words1;
	public String [] words2;
	public double [][] matrix;
	private static Random randgen = new Random();
	
	public WordMatrix2d(String [] w1,String [] w2,double [][] startData){
		words1 = w1;
		words2 = w2;
		matrix = startData;
	}
	
	public void print(){
		System.out.println(Arrays.toString(words1));
		System.out.println(Arrays.toString(words2));
		System.out.println(Arrays.deepToString(matrix));
	}
	
	public String randomWord(){
		return words1[randgen.nextInt(words1.length)];
	}
	
	public String nextWord(String startWord){
		int wordAt = -1;
		for(int i=0;i<words1.length;i++){
			if (words1[i].equals(startWord)){
				wordAt = i;
				break;
			}
		}
		
		if (wordAt == -1){
			return "wordNotFound";
		}
		Random randomGenerator = new Random();
		double randFloat = randomGenerator.nextDouble();
		double accumFloat = 0;
		for(int i = 0;i < matrix[wordAt].length;i++){
			accumFloat+=matrix[wordAt][i];
			if (accumFloat>randFloat){
				return words2[i];
			}
		}
		return "arrayError";
	}
	
	public void trainWordCouple(String w1,String w2){
		int xpos = -1,ypos = -1;
		for(int i=0;i<words1.length;i++){
			if(words1[i].equals(w1)){
				ypos=i;
				break;
			}
		}
		for(int i=0;i<words2.length;i++){
			if(words2[i].equals(w2)){
				xpos=i;
				break;
			}
		}
		if (xpos==-1 || ypos == -1){
			System.err.print("EPIC FAIL :(");
			return;
		}
		matrix[ypos][xpos] += 1.0/(double)matrix[0].length;
		perturbate();
		normalize();
		
	}
	
	private void perturbate() {
		for(int i=0;i<matrix.length;i++){
			for(int j=0;j<matrix[0].length;j++){
				matrix[i][j]+=randgen.nextGaussian()*0.1/matrix[0].length;
			}
		}
	}

	public void normalize(){
		for(int i=0;i<matrix.length;i++){
			double rowsum = 0;
			for(int j=0;j<matrix[0].length;j++){
				rowsum+=matrix[i][j];
			}
			for(int j=0;j<matrix[0].length;j++){
				matrix[i][j] = matrix[i][j]/rowsum;
			}
		}
	}
	
}
