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
//Differentiate between indirect or direct objects

public class SentenceBuilder {

	static Lexicon lexicon = Lexicon.getDefaultLexicon(); 
    static NLGFactory nlgFactory = new NLGFactory(lexicon);
    static Realiser realiser = new Realiser(lexicon);
    static Random randgen = new Random();
    
    public static Scanner user_input = new Scanner(System.in); //Remember to close this input!!
	
    public WordMatrix2d nouns_nouns;
    public WordMatrix2d nouns_verbs;
    public WordMatrix2d nouns_adjectives;
    public WordMatrix2d verbs_adverbs;
    public WordMatrix2d nouns_pronouns;
    public FeatureSet featureSet;
    
	public SentenceBuilder(){
		
		//INPUTS
		String path = "/training/Erik/";

	   	nouns_nouns = LinkIntepreter.readMatrixFile(path+"nouns_nouns.txt");
	    nouns_adjectives = LinkIntepreter.readMatrixFile(path+"nouns_adjectives.txt");
	    verbs_adverbs = LinkIntepreter.readMatrixFile(path+"verbs_adverbs.txt");
	    featureSet = LinkIntepreter.readFeatureFile(path+"features.txt");
	    nouns_verbs = LinkIntepreter.readMatrixFile(path+"nouns_verbs.txt");
	    nouns_pronouns = LinkIntepreter.readMatrixFile(path+"nouns_pronouns.txt");
	}
	/*
	 * This creates a sentence using random distributions of "what word comes next"
	 * Also using sentence features. Some features depend on other features.
	 * 
	 * -subject_is_pronoun
	 * 		-subject_pronoun_is_possessive
	 * -subject_has_adjective: if not pronoun
	 * -subject_is_definite: "the" or "a" if not pronoun
	 * 
	 * 
	 * 
	 * -has_object: determines if the sentence contains an object
	 * 	-object_is_pronoun: is the object a pronoun?
	 * 		-object_pronoun_is_possessive
	 * 	-object_is_plural: if not pronoun
	 * 	-object_is_definite: "the" or "a" if not plural, if not pronoun
	 * 	-object_has_adjective: if not pronoun
	 * 
	 * 
	 * -tense: past, present, future
	 * -has_adverb
	 */
    public void createFortuneCookie(){
    	featureSet.recalcFeatures(); //Run this to recalc all features
    	String subj = nouns_nouns.randomWord();
    	String subject_proverb = nouns_pronouns.nextWord(subj);
    	String verb = nouns_verbs.nextWord(subj);
    	String obj = nouns_nouns.nextWord(subj);
    	String adverb = verbs_adverbs.nextWord(verb);
    	String object_adjective = nouns_adjectives.nextWord(obj);
    	String subjective_adjective = nouns_adjectives.nextWord(subj);
    	String object_pronoun = nouns_pronouns.nextWord(subj);

        SPhraseSpec p = nlgFactory.createClause();
        
        //SUBJECTIVE
        
        if(featureSet.subject_is_pronoun){
    		//TODO: replace you with something taken from distribution
    		NLGElement pronoun = nlgFactory.createWord(subject_proverb,LexicalCategory.PRONOUN);
    		if(featureSet.subject_pronoun_is_possessive){
    			pronoun.setFeature(Feature.POSSESSIVE, true);
    			//TODO: replace randomWord with word from distribution
    			PhraseElement subj_possessive_phrase = nlgFactory.createNounPhrase(pronoun,subj);
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
        		NLGElement pronoun = nlgFactory.createWord(object_pronoun,LexicalCategory.PRONOUN);
        		if(featureSet.object_pronoun_is_possessive){
        			pronoun.setFeature(Feature.POSSESSIVE, true);
        			//TODO: replace randomWord with word from distribution
        			PhraseElement obj_posessive_phrase = nlgFactory.createNounPhrase(pronoun,obj);
            		p.setObject(obj_posessive_phrase); 
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
        
        System.out.println("Is this a good sentence? (y/n)");
        String answer = user_input.next();
        if (answer.equals("y")){        	
////////////////////////////////////////////////SUBJECT//////////////////////////////////////////////
        	//pronoun
        	if(featureSet.subject_is_pronoun){
        		featureSet.trainFeature(featureSet.subject_is_pronounProb,0);
        	}
        	
        	//possessive
        	if(featureSet.subject_is_pronoun && featureSet.subject_pronoun_is_possessive){
        		featureSet.trainFeature(featureSet.subject_pronoun_is_possessiveProb, 0);
        	}
        	
        	//adjective
        	if(!featureSet.subject_is_pronoun && featureSet.subject_has_adjective){ 
	        	nouns_adjectives.trainWordCouple(subj,subjective_adjective,true);
	        	featureSet.trainFeature(featureSet.subject_has_adjectiveProb,0);
        	}
        	
        	//definite
        	if(!featureSet.subject_is_pronoun && featureSet.subject_is_definite){
        		featureSet.trainFeature(featureSet.subject_is_definiteProb, 0);
        	}
        	

////////////////////////////////////////////////////OBJECT////////////////////////////////////////////////
        	//has_object
        	if(featureSet.has_object){ 
        		nouns_nouns.trainWordCouple(subj,obj,true);
        		featureSet.trainFeature(featureSet.has_objectProb,0);
        	}
        	
        	//object_is_pronoun
        	if(featureSet.has_object && featureSet.object_is_pronoun){
        		featureSet.trainFeature(featureSet.object_is_pronounProb,0);
        	}
        	
        	//object_pronoun_is_possessive
        	if(featureSet.has_object && featureSet.object_is_pronoun && featureSet.object_pronoun_is_possessive){
        		featureSet.trainFeature(featureSet.object_pronoun_is_possessiveProb,0);
        	}
        	
        	//object_has_adjective
        	if(featureSet.has_object && !featureSet.object_is_pronoun && featureSet.object_has_adjective){
        		nouns_adjectives.trainWordCouple(obj,object_adjective,true);
        		featureSet.trainFeature(featureSet.object_has_adjectiveProb,0);
        	}
        	
        	//object_is_plural
        	if(featureSet.has_object && !featureSet.object_is_pronoun && featureSet.object_is_plural){
        		featureSet.trainFeature(featureSet.object_is_pluralProb,0);
        	}
        	
        	//object_is_definite
        	if(featureSet.has_object && !featureSet.object_is_pronoun && !featureSet.object_is_plural && featureSet.object_is_definite){
        		featureSet.trainFeature(featureSet.object_is_definiteProb,0);
        	}
        	

////////////////////////////////////////////////ADVERB//////////////////////////////////////////////
        	if(featureSet.has_adverb) {
        		verbs_adverbs.trainWordCouple(verb,adverb,true);
        		featureSet.trainFeature(featureSet.has_adverbProb,0);
        	}
        	
        	
        	
////////////////////////////////////////////////TENSE//////////////////////////////////////////////
        	if(featureSet.tense==Tense.PAST) featureSet.trainFeature(featureSet.tenseProb,0);
        	else if(featureSet.tense==Tense.PRESENT) featureSet.trainFeature(featureSet.tenseProb,1);
        	else if(featureSet.tense==Tense.FUTURE) featureSet.trainFeature(featureSet.tenseProb,2);
        }
        if (answer.equals("n")){        	
		////////////////////////////////////////////////SUBJECT//////////////////////////////////////////////
		//pronoun
		if(featureSet.subject_is_pronoun){
			featureSet.trainFeature(featureSet.subject_is_pronounProb,1);
		}
		//possessive
		if(featureSet.subject_is_pronoun && featureSet.subject_pronoun_is_possessive){
			featureSet.trainFeature(featureSet.subject_pronoun_is_possessiveProb, 1);
		}
		
		//adjective
		if(!featureSet.subject_is_pronoun && featureSet.subject_has_adjective){ 
		nouns_adjectives.trainWordCouple(subj,subjective_adjective,false);
			featureSet.trainFeature(featureSet.subject_has_adjectiveProb,1);
		}
		
		//definite
		if(!featureSet.subject_is_pronoun && featureSet.subject_is_definite){
			featureSet.trainFeature(featureSet.subject_is_definiteProb, 1);
		}
		
		////////////////////////////////////////////////////OBJECT////////////////////////////////////////////////
		//has_object
		if(featureSet.has_object){ 
			nouns_nouns.trainWordCouple(subj,obj,false);
			featureSet.trainFeature(featureSet.has_objectProb,1);
		}
		//object_is_pronoun
		if(featureSet.has_object && featureSet.object_is_pronoun){
			featureSet.trainFeature(featureSet.object_is_pronounProb,1);
		}
		//object_pronoun_is_possessive
		if(featureSet.has_object && featureSet.object_is_pronoun && featureSet.object_pronoun_is_possessive){
			featureSet.trainFeature(featureSet.object_pronoun_is_possessiveProb,1);
		}
		//object_has_adjective
		if(featureSet.has_object && !featureSet.object_is_pronoun && featureSet.object_has_adjective){
			nouns_adjectives.trainWordCouple(obj,object_adjective,false);
			featureSet.trainFeature(featureSet.object_has_adjectiveProb,1);
		}
		
		//object_is_plural
		if(featureSet.has_object && !featureSet.object_is_pronoun && featureSet.object_is_plural){
			featureSet.trainFeature(featureSet.object_is_pluralProb,1);
		}
		
		//object_is_definite
		if(featureSet.has_object && !featureSet.object_is_pronoun && !featureSet.object_is_plural && featureSet.object_is_definite){
			featureSet.trainFeature(featureSet.object_is_definiteProb,1);
		}
		
		
		////////////////////////////////////////////////ADVERB//////////////////////////////////////////////
		if(featureSet.has_adverb) {
			verbs_adverbs.trainWordCouple(verb,adverb,false);
			featureSet.trainFeature(featureSet.has_adverbProb,1);
		}

        }
	}
}
