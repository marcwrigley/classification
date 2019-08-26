/**
 * 
 */
package main.java.classification;

import java.util.ArrayList;

/**
 * Abstract Classifier class
 * 
 * @author Marc Wrigley
 *
 */
public abstract class Classifier<T extends Comparable<T>> {

	/**
	 * Abstract
	 * returns a predicted class label of the given data sample
	 * 
	 * @param sample
	 */
	public abstract Integer classify(ArrayList<T> sample);
}
