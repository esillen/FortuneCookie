import java.util.Arrays;
import java.util.Random;


public class WordMatrix2d {

	public String [] words1;
	public String [] words2;
	public double [][] matrix;
	
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
		Random randomGenerator = new Random();
		return words1[randomGenerator.nextInt(words1.length)];
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
			return "uSuckiSucki";
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
		return "arraySuckiSucki";
	}
	
}
