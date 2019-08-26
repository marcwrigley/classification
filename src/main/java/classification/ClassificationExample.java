package main.java.classification;

/**
 * Example use of Decision Tree classifier.
 * 
 * A Decision Tree classifier is trained and tested on data from a 
 * given input file.
 * 
 * @author Marc Wrigley
 *
 */

public class ClassificationExample {

	public static void main(String[] args) {

		// The input data file
		String treeInputPath = "diabetes.csv";

		// Read input file into ClassifierData class
		ClassifierData<Double> inputData = new ClassifierData<Double>(treeInputPath, Double.class);


		// Split inputData in half, leftData = training data, rightData = testing data
		int splitSampleNum = inputData.numSamples() / 2;
		SplitClassifierData<Double> splitData = new SplitClassifierData<Double>(inputData, splitSampleNum);


		// Build a Decision Tree using the training data		
		DecisionTreeBuilder<Double> treeBuilder = new DecisionTreeBuilder<Double>();
		treeBuilder.build(splitData.leftData());

		treeBuilder.printClassifier();


		// Measure classifier accuracy on training and testing data		
		double testAccuracyTrain = treeBuilder.test(splitData.leftData());
		double testAccuracyTest = treeBuilder.test(splitData.rightData());


		System.out.println("Training Data Accuracy: " + testAccuracyTrain);
		System.out.println("Testing Data Accuracy: " + testAccuracyTest);


	}

}
