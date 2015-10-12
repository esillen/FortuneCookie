   import java.util.Random;

import simplenlg.framework.*;
import simplenlg.lexicon.*;
import simplenlg.realiser.english.*;
import simplenlg.phrasespec.*;
import simplenlg.features.*;
    
public class TestMain {
	
	


	public static void main(String [] args){
		
        SentenceBuilder sentenceBuilder = new SentenceBuilder();
        for (int i=0;i<0;i++){
        	sentenceBuilder.createFortuneCookie();
        }
        String path = "/training/Erik/";
        //OUTPUTS (inputs are in SentenceBuilder)
        LinkIntepreter.writeMatrixToFile(sentenceBuilder.nouns_nouns, path+"nouns_nouns.txt");
        LinkIntepreter.writeMatrixToFile(sentenceBuilder.nouns_verbs, path+"nouns_verbs.txt");
        LinkIntepreter.writeMatrixToFile(sentenceBuilder.nouns_adjectives, path+"nouns_adjectives.txt");
        LinkIntepreter.writeMatrixToFile(sentenceBuilder.nouns_pronouns, path+"nouns_pronouns.txt");
        LinkIntepreter.writeMatrixToFile(sentenceBuilder.verbs_adverbs, path+"verbs_adverbs.txt");
        LinkIntepreter.writeFeaturesToFile(sentenceBuilder.featureSet, path+"features.txt");
        SentenceBuilder.user_input.close();
        System.out.println("done");
    }

	
}
