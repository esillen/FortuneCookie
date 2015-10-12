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
	private double featureIncrement = 0.01;
	
	
	
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
	
	
	public double[] trainFeature(double [] toTrain, int feature_to_increase){;
		double sum = 0;
		toTrain[feature_to_increase] += featureIncrement;
		
		for (int i=0;i<toTrain.length;i++){
			sum+=toTrain[i];
		}
		
		for (int i=0;i<toTrain.length;i++){
			toTrain[i]=toTrain[i]/sum;
		}
		
	return toTrain;
	}
	
	public void addFeatureSet(FeatureSet other){
		tenseProb = addDivideArrays(tenseProb,other.tenseProb);
		has_objectProb = addDivideArrays(has_objectProb,other.has_objectProb);
		subject_has_adjectiveProb = addDivideArrays(subject_has_adjectiveProb,other.subject_has_adjectiveProb);
		object_has_adjectiveProb = addDivideArrays(object_has_adjectiveProb,other.object_has_adjectiveProb);
		object_is_pluralProb = addDivideArrays(object_is_pluralProb,other.object_is_pluralProb);
		has_adverbProb = addDivideArrays(has_adverbProb,other.has_adverbProb);
		subject_is_pronounProb = addDivideArrays(subject_is_pronounProb,other.subject_is_pronounProb);
		subject_pronoun_is_possessiveProb = addDivideArrays(subject_pronoun_is_possessiveProb,other.subject_pronoun_is_possessiveProb);
		object_is_pronounProb = addDivideArrays(object_is_pronounProb,other.object_is_pronounProb);
		object_pronoun_is_possessiveProb = addDivideArrays(object_pronoun_is_possessiveProb,other.object_pronoun_is_possessiveProb);
		object_is_definiteProb = addDivideArrays(object_is_definiteProb,other.object_is_definiteProb);
		subject_is_definiteProb = addDivideArrays(subject_is_definiteProb,other.subject_is_definiteProb);
	}
	
	private double[] addDivideArrays(double[] a1,double[] a2){
		for(int i=0;i<a1.length;i++){
			a1[i] = (a1[i]+a2[i])/2.0;
		}
		return a1;
	}
	
}
