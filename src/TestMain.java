   import java.util.Random;

import simplenlg.framework.*;
import simplenlg.lexicon.*;
import simplenlg.realiser.english.*;
import simplenlg.phrasespec.*;
import simplenlg.features.*;
    
public class TestMain {
	
	


	public static void main(String [] args){
		TestMain m = new TestMain();
       // m.osv("cat","mouse","finds");
       
        SentenceBuilder sentenceBuilder = new SentenceBuilder();
        for (int i=0;i<10;i++){
        	sentenceBuilder.createFortuneCookie();
        }
        LinkIntepreter.writeMatrixToFile(sentenceBuilder.nouns_nouns, "schmarf.txt");
        LinkIntepreter.writeFeaturesToFile(sentenceBuilder.featureSet, "hurrrrdurrrr.txt");
    }

	
}
