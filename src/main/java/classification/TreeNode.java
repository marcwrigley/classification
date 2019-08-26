/**
 * 
 */
package main.java.classification;


import java.util.ArrayList;

/**
 * A Node of a Decision Tree Classifier
 * 
 * @author Marc Wrigley
 *
 */
public class TreeNode<T extends Comparable<T>> {

	// Is this node a leaf node
	private boolean isLeaf;

	// The associated class label if this node is a leaf
	private int classLabel;

	// During building can the given data be split, if false then this node becomes a leaf node.
	private boolean validSplit = false;

	/**
	 * ID of the current node, where the ID of the root = "0",
	 * leftChildID = currentID + "0", rightChildID = currentID + "1"
	 * 
	 * e.g.:
	 * ID of root->left = "00", ID of root->right = "01"
	 * ID of root->left->left = "000", ID of root->left->right = "001"
	 * 
	 */
	String nodeID;

	// Left child of the current node
	TreeNode<T> leftNode;

	// Right child of the current node
	TreeNode<T> rightNode;

	// index of the best feature to split on at this node
	int splittingFeature;

	// maximum depth of the Decision Tree, used during building
	int maxDepth;

	// depth of the current node
	int nodeDepth;

	// threshold value of splittingFeature, used to split the data
	T splittingValue;

	/**
	 * Returns log base 2 of x
	 * 
	 * @param x
	 */
	public static double log2(double x)
	{
		return (Math.log(x) / Math.log(2));

	}


	/**
	 * Calculates the entropy of the given ClassifierData,
	 * where entropy(currentNode) = - P(class0) log2(p(class0)) + P(class1) log2(p(class1))
	 * 
	 * @param currentNodeData
	 */
	private double entropy(ClassifierData<T> currentNodeData) {


		// Number of data samples of each class
		int classCount0 = currentNodeData.classCount(0);
		int classCount1 = currentNodeData.classCount(1);

		// If data samples aren't all of the same class
		if (classCount0 > 0  && classCount0 < currentNodeData.numSamples())
		{


			// Probability of each class
			double probC0 = (double) classCount0 / currentNodeData.numSamples();
			double probC1 = (double) classCount1 / currentNodeData.numSamples();


			double entropy = -((probC0 * log2(probC0)) + (probC1 * log2(probC1)));


			return entropy;
		}
		// entropy = 1 if all data samples are of the same class
		else {
			return 1.0;
		}
	}



	/**
	 * Calculates the best split given the currentNodeData,
	 * sets splittingFeature and splittingValue
	 * 
	 * Uses Information Gain to calculate the best split,
	 * where InformationGain(currentSplit) = Entropy(currentNode) - weightedAverage*Entropy(Children)
	 * 
	 * @param currentNodeData
	 */
	public void calculateSplit(ClassifierData<T> currentNodeData) {

		// Current best parameters to split on		
		double bestInfoGain = -1.0;
		int bestSplittingFeature = -1;
		T bestSplittingValue = null;

		// for each feature
		for (int currentFeature=0; currentFeature<currentNodeData.numFeatures(); currentFeature++) {

			// calculate value to split on
			T currentSplittingValue = currentNodeData.meanFeatureValue(currentFeature);

			// split data on current feature
			SplitClassifierData<T> splitData = new SplitClassifierData<T>(currentNodeData, currentFeature, currentSplittingValue);	

			// check that the split is valid and not redundant
			if (splitData.validSplit()){
				validSplit = true;

				// Calculate InformationGain(currentSplit) = Entropy(currentNode) - weightedAverage*Entropy(Children)

				double entropyParent = entropy(currentNodeData);
				double entropyLeftChild = entropy(splitData.leftData());
				double entropyRightChild = entropy(splitData.rightData());


				int numSamplesLeft = splitData.leftData().numSamples();
				int numSamplesRight = splitData.rightData().numSamples();

				double currentInfoGain = entropyParent - 
						((((double) numSamplesLeft / currentNodeData.numSamples()) * entropyLeftChild)
								+ (((double) numSamplesRight / currentNodeData.numSamples()) * entropyRightChild));


				// If currentInfoGain > bestInfoGain, update splitting parameters
				if (currentInfoGain > bestInfoGain) {
					bestInfoGain = currentInfoGain;
					bestSplittingFeature = currentFeature;
					bestSplittingValue = currentSplittingValue;
				}
			}
		}


		// set splittingFeature
		splittingFeature = bestSplittingFeature;
		// set splittingValue
		splittingValue = bestSplittingValue;


	}

	/**
	 * Returns a predicted class for the given sampleData.
	 * 
	 */
	public Integer classifySample(ArrayList<T> sampleData) {

		// if the current node is a leaf node, return classLabel as classifier prediction
		if (isLeaf) {
			return classLabel;
		}
		// else split the given sample and pass to left or right child to classify
		else if (sampleData.get(splittingFeature).compareTo(splittingValue) <= 0) {

			sampleData.remove(splittingFeature);

			return leftNode.classifySample(sampleData);
		}
		else {

			sampleData.remove(splittingFeature);

			return rightNode.classifySample(sampleData);
		}

	}

	/**
	 * Calculates the most likely class given a list of class labels
	 * 
	 * @param classLabels
	 */
	private static Integer predictClass(ArrayList<Integer> classLabels) {

		// Initialise label counts
		int[] labelCount = new int[2];
		labelCount[0] = 0;
		labelCount[1] = 0;

		for (int i=0; i<classLabels.size(); i++) {
			labelCount[classLabels.get(i)]++;
		}

		if (labelCount[0] > labelCount[1]) {
			return 0;
		}
		else {
			return 1;
		}
	}

	/**
	 * Returns the maximum depth of node's children
	 * 
	 */
	public int maxChildDepth() {

		// if the current node is a leaf node, return the depth of the current node
		if (isLeaf) {
			return nodeDepth;
		}
		// else get depth of child nodes, and return maximum depth
		else {
			return Math.max(leftNode.maxChildDepth(), rightNode.maxChildDepth());
		}
	}

	/**
	 * Prints the node and it's children
	 *  
	 */
	public void printNode() {

		// print isLeaf and ID
		System.out.print("[TreeNode] ID: " + nodeID + ", nodeDepth: " + nodeDepth + ", isLeaf: " + isLeaf);

		// if node is a leaf node then print classLabel
		if (isLeaf) {
			System.out.println(", classLabel: " + classLabel);
		}
		// else node isn't a leaf so print splittingFeature and Value, then print child nodes
		else {
			System.out.println(", splittingFeature: " + splittingFeature + ", splittingValue: " + splittingValue);
			leftNode.printNode();
			rightNode.printNode();
		}
	}



	/**
	 * Builds the current node and its children given input ClassifierData
	 * 
	 * TODO Check if the InformationGain of the best split > splitThresholdValue, if not don't split and set as leaf.
	 * 
	 * @param currentNodeData
	 */
	public void buildNode(ClassifierData<T> currentNodeData){


		// if there is only one data sample in currentNodeData, make currentNode a leaf node
		if (currentNodeData.numSamples() == 1) {			
			isLeaf = true;
			classLabel = currentNodeData.classLabel(0);
		}

		// if the maximum tree depth has been reached, make currentNode a leaf node 
		else if (nodeDepth == maxDepth) {
			isLeaf = true;
			ArrayList<Integer> labels = currentNodeData.labelData();

			classLabel = predictClass(labels);

		}
		// if data samples are all the same class, make currentNode a leaf node
		else if(currentNodeData.classCount(0) == 0 || currentNodeData.classCount(1) == 0) {
			isLeaf = true;
			classLabel = currentNodeData.classLabel(0);
		}
		// else need to split the data and build node's children
		else {

			// set ID of child nodes
			String leftID = nodeID+"0";
			String rightID = nodeID+"1";

			// calculate best split
			calculateSplit(currentNodeData);

			// if the calculated split is valid and not redundant
			if (validSplit) {

				// split data using best splittingFeature and splittingValue and remove feature that data is split on
				SplitClassifierData<T> splitData = new SplitClassifierData<T>(currentNodeData, splittingFeature, splittingValue, true);

				// build child nodes using split data
				leftNode = new TreeNode<T>(splitData.leftData(), maxDepth, nodeDepth+1, leftID);
				rightNode = new TreeNode<T>(splitData.rightData(), maxDepth, nodeDepth+1, rightID);
			}
			// don't split, make currentNode a leaf node
			else {
				isLeaf = true;
				ArrayList<Integer> labels = currentNodeData.labelData();

				classLabel = predictClass(labels);
			}
		}



	}

	/**
	 * Constructor, sets given parameters and then calls buildNode.
	 * 
	 * @param currentNodeData
	 * @param maxDepth
	 * @param currentDepth
	 * @param nodeID
	 */
	public TreeNode(ClassifierData<T> currentNodeData, int maxDepth, int currentDepth, String nodeID){

		// check that currentNodeData isn't null or empty
		if (currentNodeData == null || currentNodeData.numSamples()==0) {
			throw new IndexOutOfBoundsException("currentNodeData is null or empty");
		}

		isLeaf = false;
		classLabel = -1;
		this.maxDepth = maxDepth;
		nodeDepth = currentDepth;
		this.nodeID = nodeID;

		// build current node using given currentNodeData
		buildNode(currentNodeData);


	}



}
