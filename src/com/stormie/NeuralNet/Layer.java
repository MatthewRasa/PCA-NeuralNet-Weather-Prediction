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
 * Filename:      Layer.java
 * Description:   Layer class for the NeuralNet implementation.
 * Last Modified: 2016-5-5
 */

/* Setting Package */
package com.stormie.NeuralNet;

/* Setting Imports */
import java.util.ArrayList;
import java.util.List;

public class Layer {
	private List<Node> nodes;
	private List<Double> inputFromLastLayer;

	/**
	 * Constructor: Creates a new Layer object.
	 * 
	 * @param numNodes
	 *            : Number of desired nodes in the layer.
	 */
	public Layer(int numNodes) {
		nodes = new ArrayList<Node>();
		inputFromLastLayer = new ArrayList<Double>();
		while (numNodes > 0) {
			nodes.add(new Node(numNodes));
			numNodes--;
		}
	}

	/**
	 * Obtains the feed forward sigmoid function value for each node.
	 */
	public void feedForward() {
		for (Node n : nodes) {
			Double feedForwardVal = n.getThreshold();

			for (Double weight : n.getWeights()) {
				feedForwardVal += weight * inputFromLastLayer.get(n.getWeights().indexOf(weight));
			}

			n.setOutput(1 / (Math.exp(-feedForwardVal) + 1));
		}
	}

	/**
	 * Runs the calculations to figure out the output.
	 * 
	 * @return {List<Double>} : The output from the current layer of nodes.
	 */
	public List<Double> getOutput() {
		List<Double> output = new ArrayList<Double>();
		for (Node n : nodes) {
			output.add(nodes.indexOf(n), n.getOutput());
		}
		return output;
	}

	/**
	 * Getter method for the layer's nodes.
	 * 
	 * @return {List<Node>} : The nodes in the layer.
	 */
	public List<Node> getNodes() {
		return nodes;
	}

	/**
	 * Setter method for the layer's nodes.
	 * 
	 * @param nodes
	 *            : This layer's nodes.
	 */
	public void setNodes(List<Node> nodes) {
		this.nodes = nodes;
	}

	/**
	 * Getter method for the feeding layer's output.
	 * 
	 * @return {List<Node>} : The input for this layer.
	 */
	public List<Double> getInputFromLastLayer() {
		return inputFromLastLayer;
	}

	/**
	 * Setter method for the feeding layer's output.
	 * 
	 * @param inputFromLastLayer
	 *            : The input for this layer.
	 */
	public void setInputFromLastLayer(List<Double> inputFromLastLayer) {
		this.inputFromLastLayer = inputFromLastLayer;
	}

	/**
	 * To string.
	 * 
	 * @return {String} : The layer's string representation.
	 */
	@Override
	public String toString() {
		return "Layer [nodes=" + nodes + ", inputFromLastLayer=" + inputFromLastLayer + "]";
	}
}