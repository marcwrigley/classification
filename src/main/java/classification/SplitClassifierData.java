/**
 * 
 */
package main.java.classification;

import java.util.ArrayList;

/**
 * 
 * Class for storing ClassifierData that has been split in two
 * during the building of a two class Classifier.
 * 
 * @author Marc Wrigley
 *
 */
public class SplitClassifierData<T extends Comparable<T>> {

	// the left part of the data after a split
	private ClassifierData<T> leftData;

	// the right part of the data after a split
	private ClassifierData<T> rightData;

	/**
	 * Is the current split valid, i.e. if either leftData or rightData is empty then
	 * splitting the data is redundant.
	 */	
	private boolean validSplit = true;

	/**
	 * returns the left part of the data
	 * 
	 */
	public ClassifierData<T> leftData(){
		return leftData;
	}

	/**
	 * returns the right part of the data
	 * 
	 */
	public ClassifierData<T> rightData(){
		return rightData;
	}

	/**
	 * returns whether the current split is valid, i.e. not redundant
	 */
	public boolean validSplit() {
		return validSplit;
	}

	/**
	 * Removes all attribute values of a given featureNum from both left and right datasets
	 * 
	 * @param featureNum
	 */
	private void removeFeature(int featureNum) {

		// for every data sample in left and right data, remove feature value at given featureNum
		for (int i =0; i<leftData.numSamples(); i++) {
			leftData.sample(i).remove(featureNum);
		}

		for (int i =0; i<rightData.numSamples(); i++) {
			rightData.sample(i).remove(featureNum);
		}

		// update dimensions of left and right data
		leftData.updateDimensions();
		rightData.updateDimensions();


	}

	/**
	 * Splits the given inputData into leftData and rightData,
	 * data is split using the given splittingFeature index and splittingValue
	 * 
	 * @param inputData
	 * @param splittingFeature
	 * @param splittingValue
	 */
	private void splitData(ClassifierData<T> inputData, int splittingFeature, T splittingValue) {

		// if given inputData isn't null or empty
		if (inputData != null && inputData.numSamples()>0) {

			// initialise left and right data and label arrays
			ArrayList<ArrayList<T>> leftDataArray = new ArrayList<ArrayList<T>>();
			ArrayList<Integer> leftLabelArray = new ArrayList<Integer>(); 

			ArrayList<ArrayList<T>> rightDataArray = new ArrayList<ArrayList<T>>();
			ArrayList<Integer> rightLabelArray = new ArrayList<Integer>(); 

			// add each inputData sample to either left or right array, depending on given splittingFeature and splittingValue
			for (int i =0; i<inputData.numSamples(); i++) {
				if (inputData.attribute(i, splittingFeature).compareTo(splittingValue) <= 0) {

					leftDataArray.add(inputData.sample(i));
					leftLabelArray.add(inputData.classLabel(i));
				}
				else {

					rightDataArray.add(inputData.sample(i));
					rightLabelArray.add(inputData.classLabel(i));
				}
			}

			// if both left and right data aren't empty then the split is valid
			if (leftDataArray.size()>0 && rightDataArray.size()>0) {			

				// set left and right data
				leftData = new ClassifierData<T>(leftDataArray, leftLabelArray);
				rightData = new ClassifierData<T>(rightDataArray, rightLabelArray);
			}
			// else splitting inputData is redundant
			else {
				validSplit = false;
			}
		}
		else {
			throw new IndexOutOfBoundsException("inputData is null or empty");
		}
	}


	/**
	 * Constructor - calls splitData with the given parameters
	 * Doesn't remove the given splittingFeature after the split
	 * 
	 * @param inputData
	 * @param splittingFeature
	 * @param splittingValue
	 */
	public SplitClassifierData(ClassifierData<T> inputData, int splittingFeature, T splittingValue) {

		splitData(inputData, splittingFeature, splittingValue);

	}

	/**
	 * Constructor - calls splitData with the given parameters
	 * Removes the given splittingFeature from the split data if removeFeature==true
	 * 
	 * @param inputData
	 * @param splittingFeature
	 * @param splittingValue
	 * @param removeFeature
	 */
	public SplitClassifierData(ClassifierData<T> inputData, int splittingFeature, T splittingValue, boolean removeFeature) {


		ClassifierData<T> newInputData = new ClassifierData<T>(inputData);

		splitData(newInputData, splittingFeature, splittingValue);

		if (removeFeature) {
			removeFeature(splittingFeature);
		}


	}

	/**
	 * Constructor - splits the given data in two based on given sample number
	 * 
	 * Can be used to split data into training and testing data for a classifier
	 * 
	 * @param inputData
	 * @param sampleNumber
	 */
	public SplitClassifierData(ClassifierData<T> inputData, int sampleNumber) {

		// if given inputData isn't null or empty
		if (inputData != null && inputData.numSamples()> sampleNumber) {

			// initialise left and right data and label arrays
			ArrayList<ArrayList<T>> leftDataArray = new ArrayList<ArrayList<T>>();
			ArrayList<Integer> leftLabelArray = new ArrayList<Integer>(); 

			ArrayList<ArrayList<T>> rightDataArray = new ArrayList<ArrayList<T>>();
			ArrayList<Integer> rightLabelArray = new ArrayList<Integer>(); 

			// add all data samples <= given sampleNumber to left array
			for (int i =0; i<=sampleNumber; i++) {

				leftDataArray.add(inputData.sample(i));
				leftLabelArray.add(inputData.classLabel(i));
			}

			// add all data samples > given sampleNumber to right array
			for (int i =sampleNumber+1; i<inputData.numSamples(); i++) {

				rightDataArray.add(inputData.sample(i));
				rightLabelArray.add(inputData.classLabel(i));
			}			

			// set left and right data
			leftData = new ClassifierData<T>(leftDataArray, leftLabelArray);
			rightData = new ClassifierData<T>(rightDataArray, rightLabelArray);
		}
		else {
			throw new IndexOutOfBoundsException("inputData is null or empty");
		}



	}


}
