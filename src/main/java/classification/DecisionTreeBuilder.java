/**
 * 
 */
package main.java.classification;

/**
 * Class to build and test a Decision Tree classifier using given inputData and testData
 * 
 * @author Marc Wrigley
 *
 */
public class DecisionTreeBuilder<T extends Comparable<T>> extends ClassifierBuilder<DecisionTree<T>, T>{


	/**
	 * Tests the accuracy of the classifier on the given testData
	 * 
	 * @param testData
	 */
	@Override
	public double test(ClassifierData<T> testData) {

		int numCorrect = 0;

		// for each sample in testData
		for (int i=0; i<testData.numSamples(); i++) {

			// use classifier to predict class
			Integer predictedClass = classifier.classify(testData.sample(i));

			// check if predicted class matches true class label
			if (predictedClass.equals(testData.classLabel(i))) {
				numCorrect++;
			}
		}


		// calculate classifier accuracy
		return (double) numCorrect / testData.numSamples();

	}


	/**
	 * Builds a Decision Tree classifier from given inputData
	 * 
	 * @param inputData
	 */
	@Override
	public void build(ClassifierData<T> inputData) {

		// During building features are removed from inputData when splitting, so first copy inputData
		ClassifierData<T> newInputData = new ClassifierData<T>(inputData);

		// build classifier using copied data
		classifier = new DecisionTree<T>(newInputData);


	}

	/**
	 * Prints the built classifier
	 */
	@Override
	public void printClassifier() {
		classifier.PrintTree();
	}

}
