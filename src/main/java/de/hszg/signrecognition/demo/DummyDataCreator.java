package de.hszg.signrecognition.demo;

import de.hszg.signrecognition.imageprocessing.entity.featurevector.FeatureVector;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;

public class DummyDataCreator {

	private static final String filename = "DummyData.dat";
	DummyDataCreator(){
		FeatureVector[] f = new FeatureVector[6];
		/*f[0] = new DummyFeatureVector(4,2,1, Sign.Stop);
		f[1] = new DummyFeatureVector(1,2,3, Sign.Stop);
		f[2] = new DummyFeatureVector(4,5,6, Sign.Vorfahrt);
		f[3] = new DummyFeatureVector(1,5,3, Sign.RechtsAbbiegen);
		f[4] = new DummyFeatureVector(3,2,5, Sign.Stop);
		f[5] = new DummyFeatureVector(5,2,1, Sign.LinksAbbiegen);*/
		
		List<FeatureVector> res = new LinkedList<>();
		for(FeatureVector fv : f) res.add(fv);
		try{
			ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(filename)));
			out.writeObject(res);
			out.close();
		}catch(Throwable t){
			System.out.println("DummyDataCreator: Could not create DummyData.dat");
			t.printStackTrace();
		}
	}
	public static void main(String[] args) {
		new DummyDataCreator();
	}

}
