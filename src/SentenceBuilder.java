import java.util.Random;
import java.util.Scanner;

import simplenlg.features.Feature;
import simplenlg.features.NumberAgreement;
import simplenlg.features.Tense;
import simplenlg.framework.LexicalCategory;
import simplenlg.framework.NLGElement;
import simplenlg.framework.NLGFactory;
import simplenlg.framework.PhraseElement;
import simplenlg.lexicon.Lexicon;
import simplenlg.phrasespec.NPPhraseSpec;
import simplenlg.phrasespec.SPhraseSpec;
import simplenlg.realiser.english.Realiser;

//TODO:
//Differentiate between plural, indirect or direct objects

public class SentenceBuilder {

	static Lexicon lexicon = Lexicon.getDefaultLexicon(); 
    static NLGFactory nlgFactory = new NLGFactory(lexicon);
    static Realiser realiser = new Realiser(lexicon);
    static Random randgen = new Random();
	
    public WordMatrix2d nouns_nouns;
    public WordMatrix2d nouns_verbs;
    public WordMatrix2d nouns_adjectives;
    public WordMatrix2d verbs_adverbs;
    public FeatureSet featureSet;
    
	public SentenceBuilder(){
		  nouns_nouns = LinkIntepreter.readMatrixFile("nouns_nouns.txt");
	      nouns_adjectives = LinkIntepreter.readMatrixFile("nouns_adjectives.txt");
	      verbs_adverbs = LinkIntepreter.readMatrixFile("verbs_adverbs.txt");
	      featureSet = LinkIntepreter.readFeatureFile("fortune_cookies_features.txt");
	      nouns_verbs = LinkIntepreter.readMatrixFile("nouns_verbs.txt");
	}
	/*
	 * This creates a sentence using random distributions of "what word comes next"
	 * Also using sentence features. Some features depend on other features.
	 * 
	 * -has_object: determines if the sentence contains an object
	 * 	-object_is_pronoun: is the object a pronoun?
	 * 		-object_pronoun_is_possessive
	 * 	-object_is_plural: if not pronoun
	 * 	-object_is_definite: "the" or "a" if not plural, if not pronoun
	 * 	-object_has_adjective: if not pronoun
	 * 
	 * -subject_is_pronoun
	 * 		-subject_pronoun_is_possessive
	 * -subject_has_adjective: if not pronoun
	 * -subject_is_definite: "the" or "a" if not pronoun
	 * 
	 * -tense: past, present, future
	 * -has_adverb
	 */
    public void createFortuneCookie(){
    	featureSet.recalcFeatures(); //Run this to recalc all features
    	String subj = nouns_nouns.randomWord();
    	String verb = nouns_verbs.nextWord(subj);
    	String obj = nouns_nouns.nextWord(subj);
    	String adverb = verbs_adverbs.nextWord(verb);
    	String object_adjective = nouns_adjectives.nextWord(obj);
    	String subjective_adjective = nouns_adjectives.nextWord(subj);

        SPhraseSpec p = nlgFactory.createClause();
        
        //SUBJECTIVE
        
        if(featureSet.subject_is_pronoun){
    		//TODO: replace you with something taken from distribution
    		NLGElement pronoun = nlgFactory.createWord("you",LexicalCategory.PRONOUN);
    		if(featureSet.subject_pronoun_is_possessive){
    			pronoun.setFeature(Feature.POSSESSIVE, true);
    			//TODO: replace randomWord with word from distribution
    			PhraseElement subj_possessive_phrase = nlgFactory.createNounPhrase(pronoun,nouns_nouns.randomWord());
        		p.setSubject(subj_possessive_phrase); //Does this even work???
    		}
    		else{
    			p.setSubject(pronoun);
    		}
    	}
        else {
        	NPPhraseSpec subject = nlgFactory.createNounPhrase(subj);
        	if(featureSet.subject_has_adjective){
        		subject.addModifier(subjective_adjective);
        	}
        	if(featureSet.subject_is_definite){
        		subject.setSpecifier("the");
        	}
        	else{
        		subject.setSpecifier("a");
        	}
        	p.setSubject(subject);
        }
        
        //VERB
        p.setVerb(verb);
        if(featureSet.has_adverb){
        	p.addComplement(adverb);
        }
        
        //OBJECT
        if (featureSet.has_object){
        	if(featureSet.object_is_pronoun){
        		//TODO: replace you with something taken from distribution
        		NLGElement pronoun = nlgFactory.createWord("you",LexicalCategory.PRONOUN);
        		if(featureSet.object_pronoun_is_possessive){
        			pronoun.setFeature(Feature.POSSESSIVE, true);
        			//TODO: replace randomWord with word from distribution
        			PhraseElement obj_posessive_phrase = nlgFactory.createNounPhrase(pronoun,nouns_nouns.randomWord());
            		p.setObject(obj_posessive_phrase); //Does this even work???
        		}
        		else{
        			p.setObject(pronoun);
        		}
        	}
        	else{
		        NPPhraseSpec object = nlgFactory.createNounPhrase(obj);
		        if(featureSet.object_is_plural){
		        	object.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		        }
		        else{
		        	if(featureSet.object_is_definite){
		        		object.setSpecifier("the");
		        	}
		        	else{
		        		object.setSpecifier("a");
	        		}
		        }
		        if(featureSet.object_has_adjective){
		        	object.addModifier(object_adjective);
		        }
		        
		        p.setObject(object);
        	} 
        }
        //p.setIndirectObject(object);
        
        //TENSE
        p.setFeature(Feature.TENSE, featureSet.tense);
        
        String output = realiser.realiseSentence(p);
        System.out.println(output);
		
        //Retrain model
        //TODO: train for features as well as words
        Scanner user_input = new Scanner(System.in);
        System.out.println("Is this a good sentence? (y/n)");
        String answer = user_input.next();
        if (answer.equals("y")){
        	//LOTS OF UNNESSECARY CODE, I KNOW. BUT IT IS EASIER TO ADD/REMOVE THINGS THIS WAY
        	
        	//SUBJECT
        	if(featureSet.subject_is_pronoun){
        		featureSet.trainFeature(featureSet.subject_is_pronounProb,0);
        	}
        	else featureSet.trainFeature(featureSet.subject_is_pronounProb,1);
        	
        	if(!featureSet.subject_is_pronoun && featureSet.subject_has_adjective){ 
	        	nouns_adjectives.trainWordCouple(subj,subjective_adjective);
	        	featureSet.trainFeature(featureSet.subject_has_adjectiveProb,0);
        	}
        	else featureSet.trainFeature(featureSet.subject_has_adjectiveProb,1);
        	
        	
        	
        	
        	//OBJECT
        	if(featureSet.has_object){ 
        		nouns_nouns.trainWordCouple(subj,obj);
        		featureSet.trainFeature(featureSet.has_objectProb,0);
        		
        		if(featureSet.object_is_pronoun){
	        		if(featureSet.object_has_adjective) {
	            		nouns_adjectives.trainWordCouple(obj,object_adjective);
	            	}
        		}
        	}
        	else featureSet.trainFeature(featureSet.has_objectProb,1);
        	
        	//ADVERB
        	if(featureSet.has_adverb) {
        		verbs_adverbs.trainWordCouple(verb,adverb);
        		featureSet.trainFeature(featureSet.has_adverbProb,0);
        	}
        	else featureSet.trainFeature(featureSet.has_adverbProb,1);
        	
        	
        	//TENSE
        	if(featureSet.tense==Tense.PAST) featureSet.trainFeature(featureSet.tenseProb,0);
        	else if(featureSet.tense==Tense.PRESENT) featureSet.trainFeature(featureSet.tenseProb,1);
        	else if(featureSet.tense==Tense.FUTURE) featureSet.trainFeature(featureSet.tenseProb,2);
        }
	}
}
