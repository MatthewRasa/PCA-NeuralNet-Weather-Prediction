package com.stormie;

import com.stormie.pca.*;
import java.util.Random;

public class Driver {

	public static void main(String[] args) {
		int labels = 100, cols = 10;
		double[][] xData = new double[labels][cols];
		Random rand = new Random();

		System.out.println("Using " + labels + " labels...");
		for (int i = 0; i < labels; i++) {
			for (int j = 0; j < cols; j++)
				xData[i][j] = rand.nextInt(100);
		}
		Matrix x = new Matrix(xData);
		System.out.println("Initial Dimensions: \n" + x);	
		int[] rm = PCA.runPCA(x);
		System.out.print("Reduced Dimensions: \n" + x + "Removed dimensions: ");
		for (int i = 0; i < rm.length; i++)
			System.out.print(rm[i] + " ");
		System.out.println();
	}

}
