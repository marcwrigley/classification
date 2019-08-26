/**
 * 
 */
package main.java.classification;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 
 * Class for storing data to be used to train a Classifier.
 * 
 * Sample data is stored in a 2D ArrayList dataArray, with corresponding class labels
 * stored in a ArrayList labelArray.
 * Where dataArary.get(i).get(j) stores the jth feature value of sample i, and
 * labelArray.get(i) stores the class of sample i.
 * 
 * @author Marc Wrigley
 *
 */
public class ClassifierData<T extends Comparable<T>>{



	// Sample data
	private ArrayList<ArrayList<T>> dataArray;

	// Class labels
	private ArrayList<Integer> labelArray;

	// Number of features each sample contains
	private int numFeatures;

	// Number of samples in the dataset
	private int numSamples;

	// The Class type stored in dataArray
	private final Class<T> clazz;

	// Returns the number of features in the dataset
	public int numFeatures() {
		return numFeatures;
	}

	// Returns the number samples in the dataset
	public int numSamples() {
		return numSamples;
	}


	/**
	 * 
	 * Returns the number of samples of the given class label
	 * 
	 * @param classLabel
	 */
	public int classCount(Integer classLabel) {
		int classCount=0;

		for (int i=0; i<numSamples; i++) {
			if (labelArray.get(i).equals(classLabel)) {
				classCount++;
			}
		}
		return classCount;
	}

	/**
	 * Recompute the number of samples and features in the dataset.
	 * To be used after a feature has been removed from the dataset.
	 */
	public void updateDimensions() {

		// check that dataArray isn't empty
		if (dataArray == null || dataArray.size() == 0) {
			throw new IndexOutOfBoundsException("dataArray is null or empty");
		}

		numSamples = dataArray.size();
		numFeatures = dataArray.get(0).size();
	}

	/**
	 * Calculates the mean value for a given feature
	 * Currently only works where T=Double
	 * TODO fix so that it works for T = Float or Double
	 * 
	 * @param featureNum
	 */
	@SuppressWarnings("unchecked")
	public T meanFeatureValue(int featureNum) {
		Double runningTotal = 0.0;

		ArrayList<T> featureData = getFeatureData(featureNum);

		for (int i=0; i<featureData.size(); i++) {
			runningTotal += (Double)featureData.get(i);
		}
		Double mean = ((Double)runningTotal) / featureData.size();

		return (T) mean;
	}

	/**
	 * Returns the attribute value for a given sampleNum and featureNum
	 * 
	 * @param sampleNum
	 * @param featureNum
	 */
	public T attribute(int sampleNum, int featureNum){

		// if given sampleNum and featureNum are within range, return attribute value
		if ((sampleNum >= 0 && sampleNum < numSamples) && (featureNum >= 0 && featureNum < numFeatures)) {
			return dataArray.get(sampleNum).get(featureNum);
		}
		else {
			return null;
		}
	}

	/**
	 * Returns the data for a given sampleNum
	 * 
	 * @param sampleNum
	 */
	public ArrayList<T> sample(int sampleNum){

		// if given sampleNum is within range, return data sample
		if (sampleNum >= 0 && sampleNum < numSamples) {
			return dataArray.get(sampleNum);
		}
		else {
			return null;
		}

	}

	/**
	 * Returns the class label of the given sampleNum
	 * 
	 * @param sampleNum
	 */
	public Integer classLabel(int sampleNum) {

		// if given sampleNum is within range, return class label of sampleNum
		if (sampleNum >= 0 && sampleNum < numSamples) {
			return labelArray.get(sampleNum);
		}
		else {
			return null;
		}
	}

	/**
	 * Returns a list of feature values for a given featureNum
	 * 
	 * @param featureNum
	 */
	public ArrayList<T> getFeatureData(int featureNum){

		// if featureNum is within range, return list of feature values
		if (featureNum >= 0 && featureNum < numFeatures) {
			ArrayList<T> featureArray = new ArrayList<T>();

			for (int i=0; i<numSamples; i++) {
				featureArray.add(dataArray.get(i).get(featureNum));
			}
			return featureArray;
		}
		else {
			return null;
		}
	}

	/**
	 * returns labelArray
	 * 
	 */
	public ArrayList<Integer> labelData(){
		return labelArray;
	}

	/**
	 * returns dataArray
	 * 
	 */
	public ArrayList<ArrayList<T>> getDataArray(){
		return dataArray;
	}


	/**
	 * Checks that all samples in a given inputArray have the same number of features.
	 * 
	 * @param inputArray
	 */
	private boolean arrayDimensionsCorrect(T[][] inputArray) {


		if (inputArray == null) {
			throw new IndexOutOfBoundsException("inputArray is null");
		}

		int numOfFeatures = inputArray[0].length;

		for (int i=0; i<inputArray.length; i++) {
			if (inputArray[i].length != numOfFeatures) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Checks that all samples in a given inputArray have the same number of features.
	 * 
	 * @param inputArray
	 */
	private boolean arrayDimensionsCorrect(ArrayList<ArrayList<T>> inputArray) {

		//Check inputArray isn't null or empty
		if (inputArray == null || inputArray.size() == 0) {
			throw new IndexOutOfBoundsException("inputArray is null or empty");
		}
		else if(inputArray.get(0).size() == 0) {
			throw new IndexOutOfBoundsException("inputArray has no features");
		}


		int numOfFeatures = inputArray.get(0).size();

		for (int i=0; i<inputArray.size(); i++) {
			if (inputArray.get(i).size() != numOfFeatures) {
				return false;
			}
		}

		return true;
	}


	/**
	 * Prints the stored data
	 */
	public void printData() {

		if (dataArray != null && labelArray != null) {

			for (int i=0; i<dataArray.size(); i++) {
				System.out.print(""+ i + ": ");
				for (int j=0; j<dataArray.get(i).size(); j++) {
					System.out.print("" + dataArray.get(i).get(j) + ", ");
				}
				System.out.println("  Label: " + labelArray.get(i));
			}
		}
		else
		{
			throw new IndexOutOfBoundsException("dataArray or labelArray is null");
		}
	}


	/**
	 * Read data from given input file,
	 * Data should be in a .csv or similar text file in the following format:
	 * 
	 * 
	 * columnHeading1, columnHeading2, columnHeading3, classLabel
	 * 1.0, 2.0, 3.0, 0
	 * 1.0, 2.0, 3.0, 1
	 * .
	 * .
	 * .
	 * 1.0, 2.0, 3.0, 0
	 * 
	 * where each attribute is separated by a ","
	 * Attributes should be real numbers, labels should be integers
	 * 
	 * 
	 * 
	 * @param inputFilePath
	 */
	private void readDataFromFile(String inputFilePath) {


		// Initialise arrays
		dataArray = new ArrayList<ArrayList<T>>();
		labelArray = new ArrayList<Integer>();

		int currentLineNum = -1;

		// Current sample row read from the input file
		String row;

		BufferedReader csvReader;

		// Try opening the given file
		try {

			csvReader = new BufferedReader(new FileReader(inputFilePath));

			// Read each line from the file
			while ((row = csvReader.readLine()) != null) {

				// Ignore the first line in the file (the first row should contain column names rather than sample data).
				if (currentLineNum >= 0) {

					// Feature data should be separated by a ","
					String[] data = row.split(",");

					ArrayList<T> currentRowList = new ArrayList<T>();

					// Parse class label from row data
					labelArray.add(Integer.parseInt(data[data.length-1]));

					// For each attribute value in row data, try to parse value as type T
					for (int i=0; i<data.length-1; i++) {
						try {

							// Argument array
							Class<?>[] cArg = new Class[1];
							// Class name is stored as a String
							cArg[0] = String.class;

							T instance;
							// Calls T.constructer() with current attribute value
							instance = clazz.getDeclaredConstructor(cArg).newInstance(data[i]);

							// Add parsed value to current row list
							currentRowList.add(instance);

						} catch (Exception e) {
							csvReader.close();
							throw new IllegalArgumentException("Not a valid number: " + data[i] + " at index " + i, e);
						}

					}


					// Add current line data to inputData array.
					dataArray.add(currentRowList);
				}

				// Increment the current line number
				currentLineNum++;
			}


			// Close the file
			csvReader.close();

			// Check if dataArray is empty
			if (dataArray == null || dataArray.get(0).size() == 0) {
				throw new IndexOutOfBoundsException("dataArray is null or empty");
			}

			// Check if all samples in dataArray have the same number of features
			if (!arrayDimensionsCorrect(dataArray)) {
				throw new IllegalArgumentException("Given samples have different number of features");
			}


		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}



	/**
	 * Default constructor
	 */
	public ClassifierData()	{
		numSamples = 0;
		numFeatures = 0;

		clazz = null;
	}

	/**
	 * Copy Constructor, copies the dataArray and labelArray from the
	 * given classifierData into new arrays
	 * 
	 * @param classifierData
	 */
	public ClassifierData(ClassifierData<T> classifierData) {

		clazz = null;

		ArrayList<ArrayList<T>> newDataArray = new ArrayList<ArrayList<T>>();
		ArrayList<Integer> newLabelArray = new ArrayList<Integer>();

		for (int i=0; i<classifierData.numSamples(); i++) {

			ArrayList<T> currentSample = new ArrayList<T>();

			for (int j=0; j<classifierData.sample(i).size(); j++) {
				currentSample.add(classifierData.attribute(i, j));
			}
			newDataArray.add(currentSample);
			newLabelArray.add(classifierData.classLabel(i));
		}

		dataArray = newDataArray;
		labelArray = newLabelArray;

		// Check if dataArray is empty
		if (dataArray.size() == 0 || dataArray.get(0).size() == 0) {
			throw new IndexOutOfBoundsException("dataArray is null or empty");
		}
		numSamples = dataArray.size();
		numFeatures = dataArray.get(0).size();

	}


	/**
	 * Constructor that converts given arrays to ArrayLists
	 * 
	 * @param inputData
	 * @param inputLabels
	 */
	public ClassifierData(T[][] inputData, Integer[] inputLabels) {


		// Check inputArray isn't null or empty
		if (inputData == null || inputData.length == 0)	{
			throw new IndexOutOfBoundsException("inputData is null or empty");
		}

		// Check that all samples have the same number of features
		if (!arrayDimensionsCorrect(inputData)) {
			throw new IllegalArgumentException("Given samples have different number of features");
		}


		// Check that inputLabels isn't null and is the same size as inputData
		if (inputLabels == null ) {
			throw new IllegalArgumentException("inputLabels is empty");
		}
		if (inputLabels.length != inputData.length) {
			throw new IllegalArgumentException("inputLabels is of different size to inputData");
		}


		dataArray = new ArrayList<ArrayList<T>>();
		labelArray = new ArrayList<Integer>();

		// Add inputData and inputLabels to dataArray and labelArray
		for (int i=0; i<inputData.length; i++) {
			ArrayList<T> currentRow = new ArrayList<T>();
			for (int j=0; j<inputData[0].length; j++) {
				currentRow.add(inputData[i][j]);
			}
			labelArray.add(inputLabels[i]);
			dataArray.add(currentRow);
		}

		// Check if dataArray is empty
		if (dataArray.size() == 0 || dataArray.get(0).size() == 0) {
			throw new IndexOutOfBoundsException("dataArray is null or empty");
		}

		numSamples = dataArray.size();
		numFeatures = dataArray.get(0).size();

		clazz = null;
	}

	/**
	 * Constructor - sets dataArray and labelArray using the given input arrays
	 * 
	 * @param inputData
	 * @param inputLabels
	 */
	public ClassifierData(ArrayList<ArrayList<T>> inputData, ArrayList<Integer> inputLabels) {


		// Check that input data isn't null or empty, and has the correct dimension
		if (inputData == null || inputData.size() == 0)	{
			throw new IndexOutOfBoundsException("inputData is null or empty");
		}
		else if (!arrayDimensionsCorrect(inputData)) {
			throw new IllegalArgumentException("Given samples have different number of features");
		}


		// Check that inputLabels isn't null and is the same size as inputData
		if (inputLabels == null) {
			throw new IllegalArgumentException("inputLabels is empty");
		}
		if (inputLabels.size() != inputData.size()) {
			throw new IllegalArgumentException("inputLabels is of different size to inputData");
		}

		dataArray = inputData;
		labelArray = inputLabels;

		// Check if dataArray is empty
		if (dataArray.size() == 0 || dataArray.get(0).size() == 0) {
			throw new IndexOutOfBoundsException("dataArray is null or empty");
		}

		numSamples = dataArray.size();
		numFeatures = dataArray.get(0).size();


		clazz = null;
	}


	/**
	 * Constructor - reads data from given inputFilePath
	 * 
	 * @param inputFilePath
	 * @param clazz
	 */
	public ClassifierData(String inputFilePath, Class<T> clazz) {

		this.clazz = clazz;

		// Read data from give inputFilePath
		readDataFromFile(inputFilePath);


		// Check if dataArray is empty
		if (dataArray.size() == 0 || dataArray.get(0).size() == 0) {
			throw new IndexOutOfBoundsException("dataArray is null or empty");
		}


		numSamples = dataArray.size();
		numFeatures = dataArray.get(0).size();

	}





}
