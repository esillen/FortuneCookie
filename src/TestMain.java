   import simplenlg.framework.*;
    import simplenlg.lexicon.*;
    import simplenlg.realiser.english.*;
    import simplenlg.phrasespec.*;
    import simplenlg.features.*;
    
public class TestMain {


	public static void main(String [] args){
		Lexicon lexicon = Lexicon.getDefaultLexicon();                         // default simplenlg lexicon
        NLGFactory nlgFactory = new NLGFactory(lexicon);             // factory based on lexicon
        Realiser realiser = new Realiser(lexicon);
        
        SPhraseSpec p = nlgFactory.createClause();
        NPPhraseSpec subject = nlgFactory.createNounPhrase("measure");
        subject.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
        subject.addModifier("protective");
        p.setSubject(subject);
        
        p.setVerb("prevent");
        
        NPPhraseSpec object = nlgFactory.createNounPhrase("disaster");
        object.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
        object.addModifier("costly");
        
        p.setObject(object);
        p.setFeature(Feature.TENSE, Tense.FUTURE);
        
        String output = realiser.realiseSentence(p);
        System.out.println(output);
        
	}

}
