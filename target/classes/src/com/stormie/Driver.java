package com.stormie;

import com.stormie.pca.*;

public class Driver {

	public static void main(String[] args) {
		String[] labels = new String[] {
			"Label 0", "Label 1", "Label 2", "Label 3"
		};
		Matrix x = new Matrix(new double[][] {
			{5, 10, 4, 6},
			{3, 5, 1, 9},
			{3, 4, 1, 9},
			{7, 2, 3, 9}
		});
		System.out.println("Initial Dimensions: ");
		for (String str : labels)
			System.out.println("\t" + str);
		System.out.println("Initial Data: \n" + x);
		int[] rm = PCA.runPCA(x);
		for (int i = 0; i < rm.length; i++)
			System.out.println(rm[i] + " ");
	}

}
