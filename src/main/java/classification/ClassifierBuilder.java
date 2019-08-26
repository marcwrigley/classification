/**
 * 
 */
package main.java.classification;

/**
 * Abstract ClassifierBuilder
 * Subclasses are used to build classifiers that extend Classifier.
 * 
 * @author Marc Wrigley
 *
 */
public abstract class ClassifierBuilder<T extends Classifier<?>, U extends Comparable<U>> {

	// Built classifier
	T classifier;

	/**
	 * Builds a classifier from given inputData
	 * 
	 * @param inputData
	 */
	public abstract void build(ClassifierData<U> inputData);

	/**
	 * Tests the accuracy of the classifier on the given testData
	 * 
	 * @param testData
	 */
	public abstract double test(ClassifierData<U> testData);

	/**
	 * Prints the built classifier
	 */
	public abstract void printClassifier();


}
