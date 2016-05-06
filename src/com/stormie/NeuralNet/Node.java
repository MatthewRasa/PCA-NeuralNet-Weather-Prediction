/**
 * @author Kevin Bohinski   <bohinsk1@tcnj.edu>
 * @author Matthew Steuerer <steuerm1@tcnj.edu>
 * @author Matthew Rasa       <rasam1@tcnj.edu>
 * @author Nicholas Amuso    <amuson1@tcnj.edu>
 * 
 * @version 1.0
 * @since 2016-5-5
 *
 * Course:        CSC 470 - 02 (Topics: Machine Learning)
 * Instructor:    Dr. Elangovan
 * Project Name:  Project 3
 * Description:   Stormie
 *
 * Filename:      Node.java
 * Description:   Node class for the NeuralNet implementation.
 * Last Modified: 2016-5-5
 */

/* Setting Package */
package com.stormie.NeuralNet;

/* Setting Imports */
import java.util.ArrayList;
import java.util.List;

public class Node {
	private Double output;
	private Double threshold;
	private List<Double> weights;

	/**
	 * Constructor: Creates a new Node object.
	 */
	public Node(int nodesPerLayer) {
		weights = new ArrayList<Double>();
		threshold = Math.random();
		output = 0.0;
		while (nodesPerLayer > 0) {
			weights.add((-1 + (2 * Math.random())));
			nodesPerLayer--;
		}
	}

	/**
	 * Getter method for the node's output.
	 * 
	 * @return {Double} : The node's output.
	 */
	public Double getOutput() {
		return output;
	}

	/**
	 * Setter method for the node's output.
	 * 
	 * @param output
	 *            : The node's output.
	 */
	public void setOutput(Double output) {
		this.output = output;
	}

	/**
	 * Getter method for the node's weights.
	 * 
	 * @return {List<Double>} : The node's weights.
	 */
	public List<Double> getWeights() {
		return weights;
	}

	/**
	 * Setter method for the node's weights.
	 * 
	 * @param weights
	 *            : The node's weights.
	 */
	public void setWeights(List<Double> weights) {
		this.weights = weights;
	}

	/**
	 * Getter method for the node's activation threshold.
	 * 
	 * @return {Double} : The node's activation threshold.
	 */
	public Double getThreshold() {
		return threshold;
	}

	/**
	 * Setter method for the node's threshold.
	 * 
	 * @param threshold
	 *            : The node's threshold.
	 */
	public void setThreshold(Double threshold) {
		this.threshold = threshold;
	}

	/**
	 * To string.
	 * 
	 * @return {String} : The node's string representation.
	 */
	@Override
	public String toString() {
		return "Node [output=" + output + ", weights=" + weights + ", threshold=" + threshold + "]";
	}
}