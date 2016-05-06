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
 * Filename:      Driver.java
 * Description:   Class to pull data from the Forecast.io API to train and test the neural net.
 * Last Modified: 2016-5-5
 */

/* Setting Package */
package com.stormie;

import com.stormie.NeuralNet.NeuralNet;
import com.stormie.pca.*;

import java.io.BufferedReader;
import java.io.FileReader;
/* Setting Imports */
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.*;

public class Driver {

	static OkHttpClient client = new OkHttpClient();
	static List<String> pastWeather = new ArrayList<String>();
	static String API_KEY;
	static String TCNJ_LATLON = "40.2688352,-74.7830987";

	/**
	 * @param month
	 * @param year
	 * @return
	 */
	public static int getMaxDaysInMonth(int month, int year) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.MONTH, month + 1);
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.DATE, cal.get(Calendar.DATE) - 1);

		return cal.get(Calendar.DATE);
	}
	
	public static void parseJSON(JSONObject json, ArrayList<String> names, ArrayList<Double> data, String label) {
		for (String key: JSONObject.getNames(json)) {
			try {
				JSONArray children = json.getJSONArray(key);
				for (int i = 0; i < children.length(); i++)
					parseJSON(children.getJSONObject(i), names, data, label + key + "-");
			} catch (Exception e) {
				try {
					JSONObject child = json.getJSONObject(key);
					parseJSON(child, names, data, label + key + "-");
				} catch (Exception e0) {
					try {
						data.add(json.getDouble(key));
						names.add(label + key);
					} catch (Exception e1) {
						
					}
				}
			} 
		}
	}

	public static double[][] parseWeatherData(ArrayList<String> rLabels) {
		ArrayList<ArrayList<Double>> totals = new ArrayList<ArrayList<Double>>();
		double[][] rTotals;
		
		for (String entry: pastWeather) {
			try {
				ArrayList<String> names = new ArrayList<String>();
				ArrayList<Double> data = new ArrayList<Double>();
				parseJSON(new JSONObject(entry), names, data, "");
				if (names.size() > rLabels.size()) {
					rLabels.clear();
					rLabels.addAll(names);	
				}
				totals.add(data);
			} catch (Exception e) {
			}
		}
		rTotals = new double[rLabels.size()][totals.size()];
		for (int c = 0; c < totals.size(); c++) {
			for (int r = 0; r < totals.get(c).size(); r++)	
				rTotals[r][c] = totals.get(c).get(r);
		}
		return rTotals;
	}

	/**
	 * Abstract method that uses the OkHttp library to run a HTTP GET request.
	 * 
	 * @param url
	 *            : The url to request the data from.
	 * @return : The data in json.
	 * @throws IOException
	 */
	public static String run(String url) throws IOException {
		Request request = new Request.Builder().url(url).build();
		Response response = client.newCall(request).execute();
		return response.body().string();
	}
	
	public static void readKey() {
		int lineNum = 0;
		try {
			FileReader inF = new FileReader("API_KEY.txt");
			BufferedReader in = new BufferedReader(inF);
			String inLine = "";

			while ((inLine = in.readLine()) != null) {
				try {
					if (lineNum++ < 1) {
						API_KEY = inLine;
					} else {
						IOException e = new IOException();
						throw (e);
					} /* if block */

				} catch (IOException e) {
					System.err.println("Unexpected File.");
				} /* try catch */
			} /* while */

			in.close();

		} catch (IOException e) {
			System.err.println("Trouble reading file.");
		}
	}
	
	public static void pullWeatherData() {
		System.out.println("STARTING HTTP REQUESTS");
		try {
			for (int i = 6; i > 0; i--) {
				pastWeather.add(run("https://api.forecast.io/forecast/" + API_KEY + "/" + TCNJ_LATLON + ",2016-05-" + i
						+ "T12:00:00-0400"));
			}
			for (int i = getMaxDaysInMonth(Calendar.APRIL, 2016); i > 0; i--) {
				pastWeather.add(run("https://api.forecast.io/forecast/" + API_KEY + "/" + TCNJ_LATLON + ",2016-04-" + i
						+ "T12:00:00-0400"));
			}
			for (int i = getMaxDaysInMonth(Calendar.MARCH, 2016); i > 0; i--) {
				pastWeather.add(run("https://api.forecast.io/forecast/" + API_KEY + "/" + TCNJ_LATLON + ",2016-03-" + i
						+ "T12:00:00-0400"));
			}
			for (int i = getMaxDaysInMonth(Calendar.FEBRUARY, 2016); i > 0; i--) {
				pastWeather.add(run("https://api.forecast.io/forecast/" + API_KEY + "/" + TCNJ_LATLON + ",2016-02-" + i
						+ "T12:00:00-0400"));
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("FINISHED HTTP REQUESTS");
	}

	/**
	 * Main method.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		readKey();
		pullWeatherData();
		
		// Parse data
		ArrayList<String> labels = new ArrayList<String>();
		double[][] totals = parseWeatherData(labels);
		
		// Perform dimension reduction using PCA
		Matrix pcaMatrix = new Matrix(totals);
		int[] removedDims = PCA.runPCA(pcaMatrix);	
		ArrayList<ArrayList<Double>> reduced = new ArrayList<ArrayList<Double>>();
		for (int c = 0; c < pcaMatrix.getCols(); c++) {
			ArrayList<Double> col = new ArrayList<Double>();
			for (int r = 0, rmi = 0; r < pcaMatrix.getRows(); r++) {
				if (r == removedDims[rmi])
					rmi++;
				else
					col.add(pcaMatrix.getElemAt(r, c));
			}
			reduced.add(col);
		}
		System.out.println("\n[PCA] Removed " + removedDims.length + " out of " + pcaMatrix.getRows()
			+ " dimensions (" + (int)(((double) removedDims.length / pcaMatrix.getRows()) * 100) + "% removed).");

		// Separate training and testing data
		HashMap<Integer, List<Double>> trainingData = new HashMap<Integer, List<Double>>();
		int trainSize = 3 * reduced.size() / 4, trainId = 0; // 75% training data
		for (int i = 0; i < trainSize; i++)
			trainingData.put(i, reduced.remove(0));
		
		// Instantiate neural net
		NeuralNet nn = new NeuralNet(5, trainingData.get(0).size(), trainingData.get(0), trainingData);
		
		// Train neural net
		for (int i = 1; i < trainingData.size(); i++) {
			nn.feedForward();
			nn.addData(trainingData.get(i));
			nn.updateWeights(trainId++);
		}
		
		// Test neural net
		for (ArrayList<Double> sample: reduced) {
			nn.feedForward();
			nn.addData(sample);
		}
		
		/*
		 * List<Double> input = new ArrayList<Double>(); Random r = new
		 * Random(); for (int i = 0; i < num; i++) { input.add(r.nextDouble());
		 * } NeuralNet nn = new NeuralNet(num, num, input);
		 * System.out.println(nn.feedForward());
		 */
	}

}
