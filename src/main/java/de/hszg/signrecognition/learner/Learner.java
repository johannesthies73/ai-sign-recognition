package de.hszg.signrecognition.learner;
import de.hszg.signrecognition.imageprocessing.entity.featurevector.FeatureVector;
import de.hszg.signrecognition.imageprocessing.entity.Sign;

import java.util.List;

public interface Learner {
	/** The training method, that chances the internal state of the signrecognition such that
	 * it will classify examples of a similar set (i.e. the testSet better.
	 * 
	 * @param trainingSet contains feature vectors and corresponding concepts 
	 * to provide experience to learn from  
	 */
	void learn(List<FeatureVector> trainingSet);
	
	/**
	 * find the concept of the example from the internal knowledge of the lerner
	 * this method must not consider example.getGuess() at all!!
	 * 
	 * @param example: is a feature vector
	 * @return the concept of the examplke as learned by this before
	 */
	Sign classify(FeatureVector example);
}
