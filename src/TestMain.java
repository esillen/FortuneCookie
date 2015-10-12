   import java.util.Random;

import simplenlg.framework.*;
import simplenlg.lexicon.*;
import simplenlg.realiser.english.*;
import simplenlg.phrasespec.*;
import simplenlg.features.*;
    
public class TestMain {
	
	


	public static void main(String [] args){
		
        //train();
        addDivide(); //OBSERVE THAT THIS DOES NOT UPDATE THE FEATURESETS
    }
	
public static void train(){
		
		SentenceBuilder sentenceBuilder = new SentenceBuilder();
        for (int i=0;i<10;i++){
        	System.out.println(i);
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
	
	public static void addDivide(){
		
        String inpath1 = "/training/TrainingRun1/";
        String inpath2 = "/Yanbei/";
        String outpath = "/training/TrainingRun1/";
        
        WordMatrix2d nouns_nouns = LinkIntepreter.readMatrixFile(inpath1+"nouns_nouns.txt");
        WordMatrix2d nouns_adjectives = LinkIntepreter.readMatrixFile(inpath1+"nouns_adjectives.txt");
        WordMatrix2d verbs_adverbs = LinkIntepreter.readMatrixFile(inpath1+"verbs_adverbs.txt");
        WordMatrix2d nouns_verbs = LinkIntepreter.readMatrixFile(inpath1+"nouns_verbs.txt");
        WordMatrix2d nouns_pronouns = LinkIntepreter.readMatrixFile(inpath1+"nouns_pronouns.txt");
	    
        WordMatrix2d nouns_nouns2 = LinkIntepreter.readMatrixFile(inpath2+"nouns_nouns.txt");
        WordMatrix2d nouns_adjectives2 = LinkIntepreter.readMatrixFile(inpath2+"nouns_adjectives.txt");
        WordMatrix2d verbs_adverbs2 = LinkIntepreter.readMatrixFile(inpath2+"verbs_adverbs.txt");
        WordMatrix2d nouns_verbs2 = LinkIntepreter.readMatrixFile(inpath2+"nouns_verbs.txt");
        WordMatrix2d nouns_pronouns2 = LinkIntepreter.readMatrixFile(inpath2+"nouns_pronouns.txt");
        
        nouns_nouns.add(nouns_nouns2);
        nouns_adjectives.add(nouns_adjectives2);
        verbs_adverbs.add(verbs_adverbs2);
        nouns_verbs.add(nouns_verbs2);
        nouns_pronouns.add(nouns_pronouns2);
        
        nouns_nouns.divide(2.0);
        nouns_adjectives.divide(2.0);
        verbs_adverbs.divide(2.0);
        nouns_verbs.divide(2.0);
        nouns_pronouns.divide(2.0);
        
        //OUTPUTS (inputs are in SentenceBuilder)
        LinkIntepreter.writeMatrixToFile(nouns_nouns, outpath+"nouns_nouns.txt");
        LinkIntepreter.writeMatrixToFile(nouns_verbs, outpath+"nouns_verbs.txt");
        LinkIntepreter.writeMatrixToFile(nouns_adjectives, outpath+"nouns_adjectives.txt");
        LinkIntepreter.writeMatrixToFile(nouns_pronouns, outpath+"nouns_pronouns.txt");
        LinkIntepreter.writeMatrixToFile(verbs_adverbs, outpath+"verbs_adverbs.txt");
        System.out.println("done");
	}
	
	
	

	
}
