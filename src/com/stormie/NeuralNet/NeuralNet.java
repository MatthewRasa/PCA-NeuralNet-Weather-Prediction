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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NeuralNet {
	private List<Layer> layers;
	private List<Double> output;
	private Map<Integer, List<Double>> expectedOutput;
	private Double learningRate;
	private Double momentum;
	private int epoch;

	/**
	 * Constructor : Creates a new NeuralNet object based off params.
	 * 
	 * @param numLayers        : Number of layers (inclusive of hidden, input, and output)
	 * @param numNodesPerLayer : Number of nodes per layer
	 * @param input            : Input doubles for the input layer of nodes
	 * @param expectedOutput   : Expected output for back propagation training.
	 */
	public NeuralNet(int numLayers, int numNodesPerLayer, List<Double> input,
			Map<Integer, List<Double>> expectedOutput) {
		momentum = 0.01;
		learningRate = 0.2;
		epoch = 0;
		layers = new ArrayList<Layer>();
		this.expectedOutput = expectedOutput;
		while (numLayers > 0) {
			layers.add(new Layer(numNodesPerLayer));
			numLayers--;
		}
		addData(input);
	}
	
	public void addData(List<Double> input) {
		layers.get(0).setInputFromLastLayer(input);
	}

	/**
	 * Feeds information through the network, letting the nodes fire.
	 * @return {List<Double>} : The output
	 */
	public List<Double> feedForward() {
		/* For each node in each layer, feed forward, and if on the output layer, stop the process */
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

	/**
	 * Updates weights of the nodes via back propagation.
	 * Calculates distance from expected output (referred to as error) and adjusts accordingly.
	 * 
	 * @param sampleNum : Which training data to use to calculate distance from.
	 */
	public void updateWeights(int sampleNum) {
		int lastLayer = layers.size() - 1;

		/* Calculate signal error in output layer */
		for (Node n : layers.get(lastLayer).getNodes()) {
			int i = layers.get(lastLayer).getNodes().indexOf(n);
			Double sigErr = ((expectedOutput.get(sampleNum % expectedOutput.size()).get(i)
					- layers.get(lastLayer).getNodes().get(i).getOutput())
					* layers.get(lastLayer).getNodes().get(i).getOutput()
					* (1 - layers.get(lastLayer).getNodes().get(i).getOutput()));
			n.setSigErr(sigErr);
		}

		/* 
		 * Calculates signal errors for all nodes in hidden layers.
		 * Back propagates the signal errors.
		 */
		for (int i = (lastLayer - 1); i > 0; i--) {
			for (int j = 0; j < layers.get(i).getNodes().size(); j++) {
				Double sum = 0.0;
				for (int k = 0; k < layers.get(i + 1).getNodes().size(); k++) {
					sum += layers.get(i + 1).getNodes().get(k).getWeights().get(j)
							* layers.get(i + 1).getNodes().get(k).getSigErr();
				}
				layers.get(i).getNodes().get(j).setSigErr(sum * layers.get(i).getNodes().get(j).getOutput()
						* (1 - layers.get(i).getNodes().get(j).getOutput()));
			}
		}

		/* Updates weights */
		for (int i = (layers.size() - 1); i > 0; i--) {
			for (Node n : layers.get(i).getNodes()) {
				/* Calculate weight error */
				n.setThresholdErr(learningRate * n.getSigErr() + momentum * n.getThresholdErr());
				n.setThreshold(n.getThreshold() + n.getThresholdErr());

				/* updates weights */
				for (int j = 0; j < n.getWeights().size(); j++) {
					/* Calculate distance between nodes */
					n.getWeightsErr().set(j,
							learningRate * n.getSigErr() * layers.get(i - 1).getNodes().get(j).getOutput()
									+ momentum * n.getWeightsErr().get(j));

					/* Update based on distance */
					n.getWeights().set(j, n.getWeights().get(j) + n.getWeightsErr().get(j));
				}
			}
		}

		/* Reduces learning rate over time to prevent over or under fitting */
		epoch++;
		learningRate = learningRate / ((1 + epoch) / (epoch));
	}

}
