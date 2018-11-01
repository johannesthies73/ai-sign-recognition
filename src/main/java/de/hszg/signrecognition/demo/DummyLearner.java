package de.hszg.signrecognition.demo;

import de.hszg.signrecognition.demo.entity.Cal2Node;
import de.hszg.signrecognition.imageprocessing.entity.featurevector.FeatureVector;
import de.hszg.signrecognition.learner.Learner;
import de.hszg.signrecognition.imageprocessing.entity.Sign;

import java.util.List;


/** a dummy class to implement a signrecognition that will not do any
 * learning and will the think, everything corresponds to 
 * the same concept. 
 * 
 * @author gr
 *
 */
public class DummyLearner implements Learner {

	private Cal2Node searchTree = new Cal2Node();

	@Override
	public void learn(List<FeatureVector> trainingSet) {
		for (FeatureVector featureVector : trainingSet) {
			searchTree.update(featureVector);
		}
	}

	@Override
	public Sign classify(FeatureVector example) {
		// weil wir nichts gelernt haben glauben wir, alles sind Stopschilder
		return Sign.STOP;
	}

}
