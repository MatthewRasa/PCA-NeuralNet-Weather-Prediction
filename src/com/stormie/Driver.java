package com.stormie;

import com.stormie.pca.*;

public class Driver {

	public static void main(String[] args) {
		Matrix x = new Matrix(new double[][] {
			{5, 10, 4, 6},
			{3, 5, 1, 9},
			{0, 4, 2, 8},
			{7, 2, 3, 9}
		});
		System.out.println("Initial: \n" + x);
		PCA.runPCA(x);
	}

}
