import java.util.Arrays;
import java.util.Random;

import simplenlg.features.Tense;


public class FeatureSet {
	
	private static Random randgen = new Random();
	public double [] tenseProb,has_objectProb,subject_has_adjectiveProb,object_has_adjectiveProb,object_is_pluralProb,has_adverbProb;
	public double [] subject_is_pronounProb, subject_pronoun_is_possessiveProb, object_is_pronounProb, object_pronoun_is_possessiveProb;
	public double[] subject_is_definiteProb,object_is_definiteProb;
	public boolean has_object,subject_has_adjective,object_has_adjective,object_is_plural,has_adverb;
	public boolean subject_is_pronoun,subject_pronoun_is_possessive, object_is_pronoun, object_pronoun_is_possessive;
	public boolean subject_is_definite,object_is_definite;
	public Tense tense;
	
	
	
	public FeatureSet(){
		tenseProb = new double[]{0,0,0};
		has_objectProb = new double[]{0,0};
		subject_has_adjectiveProb = new double[]{0,0};
		object_has_adjectiveProb = new double[]{0,0};
		object_is_pluralProb = new double[]{0,0};
		has_adverbProb = new double[]{0,0};
		subject_is_pronounProb = new double[]{0,0};
		subject_pronoun_is_possessiveProb = new double[]{0,0};
		object_is_pronounProb = new double[]{0,0};
		object_pronoun_is_possessiveProb = new double[]{0,0};
		object_is_definiteProb = new double[]{0,0};
		subject_is_definiteProb = new double[]{0,0};
		
	}
	
	public void setTense(){
		double randvalue = randgen.nextDouble();
		double cumsum = 0;
		for (int i=0;i < tenseProb.length;i++){
			cumsum+=tenseProb[i];
			if (cumsum>randvalue){
				if(i==0) tense = Tense.PAST;
				if(i==1) tense = Tense.PRESENT;
				if(i==2) tense = Tense.FUTURE;
				return;
			}
		}
		System.err.println("derp error in array");
	}
	
	public boolean decideYesNo(double[] featureProb){
		double randvalue = randgen.nextDouble();
		double cumsum = 0;
		for (int i=0;i < featureProb.length;i++){
			cumsum+=featureProb[i];
			if (cumsum>randvalue){
				if(i==0) return true;
				if(i==1) return false;
				}
		}
		System.err.println("derp error in yes/no array");
		return true;
	}
	
	public void recalcFeatures(){
		setTense();
		has_object = decideYesNo(has_objectProb);
		subject_has_adjective = decideYesNo(subject_has_adjectiveProb);
		object_has_adjective = decideYesNo(object_has_adjectiveProb);
		object_is_plural = decideYesNo(object_is_pluralProb);
		has_adverb = decideYesNo(has_adverbProb);
		subject_is_pronoun = decideYesNo(subject_is_pronounProb);
		subject_pronoun_is_possessive = decideYesNo(subject_pronoun_is_possessiveProb);
		object_is_pronoun = decideYesNo(object_is_pronounProb);
		object_pronoun_is_possessive = decideYesNo(object_pronoun_is_possessiveProb);
		object_is_definite = decideYesNo(object_is_definiteProb);
		subject_is_definite = decideYesNo(subject_is_definiteProb);
	}
	
	public void print(){
		System.out.println(Arrays.toString(tenseProb));
		System.out.println(Arrays.toString(has_objectProb));
		System.out.println(Arrays.toString(subject_has_adjectiveProb));
		System.out.println(Arrays.toString(object_has_adjectiveProb));
		System.out.println(Arrays.toString(object_is_pluralProb));
		System.out.println(Arrays.toString(has_adverbProb));
	}
	public double[] trainFeature(double [] toTrain){
		double increment = 0.05;
		double sum=toTrain[0]+toTrain[1] + increment;
		toTrain[0] = (toTrain[0]+increment)/sum;
		toTrain[1] = toTrain[1]/sum;
	
	return toTrain;
		
	}
}
