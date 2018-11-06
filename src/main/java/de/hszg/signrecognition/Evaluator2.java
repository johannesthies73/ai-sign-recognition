package de.hszg.signrecognition;

import de.hszg.signrecognition.demo.DummyLearner;
import de.hszg.signrecognition.imageprocessing.entity.featurevector.FeatureVector;
import de.hszg.signrecognition.imageprocessing.entity.Sign;
import de.hszg.signrecognition.learner.Learner;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;





public class Evaluator2 {
	/** the percentage (between 0 und 100) of vectors from the data to be used for the test
	*/
	private static int testRate = 40; 

	
	public Evaluator2(String filename) {
		List<FeatureVector> vectors = readData(filename);
		
		Learner learner = new DummyLearner();
		
		
		// TODO: folgendes muss zur Evaluierung mehrfach ausgefxhrt werden
		// Verschiedene Teilmengen finden und Verschiedene Reihenfolgen festlegen,
		// wie oft, das hngt vom gewnschten Vertrauensintervall ab
		do{
			vectors = mixData(vectors);
			List<List<FeatureVector>> sets = extractTrainingData(vectors);
			learner.learn(sets.get(0));
			Vector<Integer> result = evaluate(sets.get(1),learner);
			evalResult(result);
		}while(false); //TODO: eine andere Abbruchbedingung verwenden
		
	}
	/**
	 * Evaluate the reulst from the test for output or furthjer considerations
	 * @param result a Vector containing 3 values: a) right classification ba used signrecognition,
	 * b) lerner could not decide or c) signrecognition found wrong concept
	 */
	private void evalResult(Vector<Integer> result) {
		// TODO hier muss mehr Auswertung passieren, insbes: Vertrauensintervalle etc
		System.out.println("Learning result: \n correct: "+result.get(0)+"\n unknown: "+result.get(1)+"\n wrong: "+result.get(2));
	}
	/** evaluate the signrecognition with a given test set.
	 * 
	 * @param list: The set of test examples containing the correct concept
	 * @param learner: The signrecognition to be tests
	 * 
	 * @return a vector containing the test results: success, unknown, false
	 */
private Vector<Integer> evaluate(List<FeatureVector> list, Learner learner) {
		int success=0;int unknown=0;int fault =0;
		for(FeatureVector fv : list){
//			Sign c = learner.classify(fv);
//			if(c.equals(Sign.UNKNOWN)) unknown++;
//			else if(c.equals(fv.getGuess())) success++;
//			else fault++;
				
		}
		Vector<Integer> res = new Vector<>();
		res.add(0,success);
		res.add(1,unknown);
		res.add(2,fault);
		return res;
	}
/**
 * 
 * @param vectors a list of vectors
 * @return list containing the same vectors as parameter but 
 * (usually) in different order
 */
	private List<FeatureVector> mixData(List<FeatureVector> vectors) {
		// TODO: die Reihenfolge der Elemente zufxllig verxndern
		return vectors;
	}

	/**
	 * Split the set of festure vectors in a set of traing data and a set of test data.
	 * For representative results it is essential to mix the order of vectors 
	 * before splitting the set
	 * 
	 * @param vectors :a List fo Feature Vectors we can use for the test
	 * @return a List containt two Lists: first the training data, second the test data they are disjunct subsets of vector 
	 *
	 */
	private List<List<FeatureVector>> extractTrainingData(
			List<FeatureVector> vectors) {
		List<List<FeatureVector>> result = new LinkedList<>();
		List<FeatureVector> trainingData = new LinkedList<>();
		List<FeatureVector> testData = new LinkedList<>();
		
		int cut = testRate/100 * vectors.size();
		trainingData.addAll(vectors.subList(0,cut));
		testData.addAll(vectors.subList(cut+1, vectors.size()));
		
		result.add(trainingData);
		result.add(testData);
		return result;
	}

	/** read data from file
	 * 
	 * @param filename the file with this name should contain a serialized List<FeatureVector> containt all the data
	 * @return all the data
	 */
	private List<FeatureVector> readData(String filename) {
		List<FeatureVector> vectors = null;
		try{
			ObjectInputStream in = new ObjectInputStream(new BufferedInputStream( new FileInputStream(filename)));
			vectors = (List<FeatureVector>) in.readObject();
			in.close();
		}catch(Throwable t){
			System.out.println("Could not read Data from file: "+filename);
			System.exit(1);
		}
		return vectors;
	}

	/** run the program with training set provided in file with 
	 * name given in first parameter
	 * @param args 1. filename of Serialiszed List<FeatureVector>
	 */
	public static void main(String[] args){
		String filename=null;
		if(args.length==0){
			System.out.println("No data file provided, using dummy data: DummyData.dat");
			filename = "DummyData.dat";
		}
		else 
			filename = args[0];
		new Evaluator2(filename);
		
	}
}
