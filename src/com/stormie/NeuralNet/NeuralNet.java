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
 * Filename:      NeuralNet.java
 * Description:   NeuralNet class for the NeuralNet implementation.
 * Last Modified: 2016-5-5
 */

/* Setting Package */
package com.stormie.NeuralNet;

/* Setting Imports */
import java.util.ArrayList;
/* Setting Imports */
import java.util.List;

public class NeuralNet {
	private List<Layer> layers;
	private List<Double> output;
	private Double learningRate;
	private Double momentum;

	public NeuralNet(int numLayers, int numNodesPerLayer, List<Double> input) {
		momentum = 0.01;
		layers = new ArrayList<Layer>();
		while (numLayers > 0) {
			layers.add(new Layer(numNodesPerLayer));
			numLayers--;
		}
		layers.get(0).setInputFromLastLayer(input);
	}

	public List<Double> feedForward() {
		for (Layer l : layers) {
			l.feedForward();
			if (layers.indexOf(l) == layers.size() - 1) {
				output = l.getOutput();
			} else {
				layers.get(layers.indexOf(l) + 1).setInputFromLastLayer(l.getOutput());
			}
		}
		
		return output;
	}
	
	
}
