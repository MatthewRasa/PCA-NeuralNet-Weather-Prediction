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
package com.stormie.NeuralNet;

import java.io.BufferedReader;
import java.io.FileReader;
/* Setting Imports */
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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

	/**
	 * Main method.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		int lineNum = 0;

		try {
			FileReader inF = new FileReader("API_KEY.txt");
			BufferedReader in = new BufferedReader(inF);
			String inLine = "";

			while ((inLine = in.readLine()) != null) {
				try {
					if (lineNum == 1) {
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

			System.out.println(pastWeather);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("FINISHED HTTP REQUESTS");

		/*
		 * List<Double> input = new ArrayList<Double>(); Random r = new
		 * Random(); for (int i = 0; i < num; i++) { input.add(r.nextDouble());
		 * } NeuralNet nn = new NeuralNet(num, num, input);
		 * System.out.println(nn.feedForward());
		 */
	}

}
