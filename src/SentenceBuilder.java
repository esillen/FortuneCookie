import java.util.Random;
import java.util.Scanner;

import simplenlg.features.Feature;
import simplenlg.features.NumberAgreement;
import simplenlg.features.Tense;
import simplenlg.framework.NLGFactory;
import simplenlg.lexicon.Lexicon;
import simplenlg.phrasespec.NPPhraseSpec;
import simplenlg.phrasespec.SPhraseSpec;
import simplenlg.realiser.english.Realiser;

//TODO:
//Differentiate between plural, indirect or direct objects

public class SentenceBuilder {

	static Lexicon lexicon = Lexicon.getDefaultLexicon();                         // default simplenlg lexicon
    static NLGFactory nlgFactory = new NLGFactory(lexicon);             // factory based on lexicon
    static Realiser realiser = new Realiser(lexicon);
    static Random randgen = new Random();
	
    public WordMatrix2d nouns_nouns;
    public WordMatrix2d nouns_verbs;
    public WordMatrix2d nouns_adjectives;
    public WordMatrix2d verbs_adverbs;
    
    
	public SentenceBuilder(){
		  nouns_nouns = LinkIntepreter.readFile("nouns_nouns.txt");
	      nouns_verbs = LinkIntepreter.readFile("nouns_verbs.txt");
	      nouns_adjectives = LinkIntepreter.readFile("nouns_adjectives.txt");
	      verbs_adverbs = LinkIntepreter.readFile("verbs_adverbs.txt");
	}
    
	
    public void createRandomSentence(){
    	String subj = nouns_nouns.randomWord();
    	String adj1 = nouns_adjectives.nextWord(subj);
    	String obj = nouns_nouns.nextWord(subj);
    	String adj2 = nouns_adjectives.nextWord(subj);
    	String verb = nouns_verbs.nextWord(subj);
    	String adverb = verbs_adverbs.nextWord(verb);

        SPhraseSpec p = nlgFactory.createClause();
        NPPhraseSpec subject = nlgFactory.createNounPhrase(subj);
        //subject.addModifier(adj1);
        
        p.setSubject(subject);
        
        p.setVerb(verb);
        //p.addComplement(adverb);
        
        NPPhraseSpec object = nlgFactory.createNounPhrase(obj);
        //object.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
        //object.setFeature(Feature.NUMBER,NumberAgreement.PLURAL);
        object.addModifier(adj2);
        
       
        if(randgen.nextDouble()>0.5){
        	p.setObject(object);
        }
        else{
        	p.setIndirectObject(object);
        }
        
        if(randgen.nextDouble()>0.5){
        	p.setFeature(Feature.TENSE, Tense.PAST);
        }
        
        String output = realiser.realiseSentence(p);
        System.out.println(output);
		
        Scanner user_input = new Scanner(System.in);
        String answer = user_input.next();
        if (answer.equals("y")){
        	nouns_nouns.trainWordCouple(subj,obj);
        }
        
        
	}
}
