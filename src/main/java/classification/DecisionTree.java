/**
 * 
 */
package main.java.classification;

import java.util.ArrayList;

/**
 * Decision Tree Classifier
 * 
 * Builds a classifier given some ClassifierData
 * 
 * @author Marc Wrigley
 *
 */
public class DecisionTree<T extends Comparable<T>> extends Classifier<T> {

	// root node of the decision tree
	TreeNode<T> rootNode;

	// depth of the tree
	int treeDepth;

	/**
	 * Predicts a class label for the given data sample
	 */
	@Override
	public Integer classify(ArrayList<T> sample) {

		// use rootNode to classify given data sample
		return rootNode.classifySample(sample);

	}

	/**
	 * Calculates the depth of the tree
	 */
	private void calculateTreeDepth() {
		treeDepth = rootNode.maxChildDepth();
	}

	/**
	 * Returns the depth of the tree
	 */
	public int TreeDepth() {
		return treeDepth;
	}

	/**
	 * Prints the tree and all it's nodes
	 */
	public void PrintTree() {

		rootNode.printNode();

		System.out.println("treeDepth: " + treeDepth);

	}

	/**
	 * Constructor - Builds a DecisionTree from the given inputData
	 * 
	 * @param inputData
	 */
	public DecisionTree(ClassifierData<T> inputData){

		// depth of the tree if data is split equally at each node = log2(numSamples), used if numFeatures is large
		int maxDepth1 = (int) Math.round(TreeNode.log2(inputData.numSamples()));


		// maxDepth must be <= inpuData.numFeatures, can't split data more than n times, where n=numFeatures
		int maxDepth2 = inputData.numFeatures()+1;


		// build root node using given inputData, and minimum of the two maxDepths
		rootNode = new TreeNode<T>(inputData, Math.min(maxDepth1, maxDepth2), 1, "0");

		calculateTreeDepth();


	}

	/**
	 * Constructor - Builds a DecisionTree from the given inputData,
	 * where maxDepth = maximum depth of constructed tree
	 * 
	 * @param inputData
	 * @param maxDepth
	 */
	public DecisionTree(ClassifierData<T> inputData, int maxDepth){

		// build root node using give inputData and maxDepth
		rootNode = new TreeNode<T>(inputData, maxDepth, 1, "0");

		calculateTreeDepth();
	}




}
