package com.stormie.pca;

public class Vector {

	public static double multiply(double[] v0, double[] v1) {
		if (v0.length != v1.length)
			throw new RuntimeException("Vector lengths must be equal.");
		double sum = 0;
		for (int i = 0; i < v0.length; i++)
			sum += v0[i] * v1[i];
		return sum;
	}

	public static String print(double[] vector) {
		String out = "[ ";
		for (int i = 0; i < vector.length; i++)
			out += vector[i] + " ";
		return out + "]";
	}

	public static double sumTo(double[] vector, int end) {
		double sum = 0;
		for (int i = 0; i < end; i++)
			sum += vector[i];
		return sum;
	}

	public static double sum(double[] vector) {
		return sumTo(vector, vector.length);
	}

	public static double ave(double[] vector) {
		return sum(vector) / vector.length;
	}

}
