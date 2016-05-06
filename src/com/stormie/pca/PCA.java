package com.stormie.pca;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;
import java.util.Arrays;

public class PCA {

	public static int[] runPCA(Matrix x) {
		// Find principal components, sort in descending order
		double[] prinComps = new double[x.getRows()];
		for (int r = 0; r < prinComps.length; r++)
			prinComps[r] = variance(x.getRowAt(r));
		prinComps = sortDescending(prinComps);

		// Compute the covariance matrix
		Matrix c = covarianceMatrix(x);

		// Calculate eigenvectors
		Array2DRowRealMatrix eigMat = new Array2DRowRealMatrix(c.getData());
		EigenDecomposition eigDec = new EigenDecomposition(eigMat);
		int eigCount = eigDec.getRealEigenvalues().length;
		double[] eigVals = new double[eigCount];
		double[][] eigVecs = new double[eigCount][c.getRows()];
		for (int i = 0; i < eigVecs.length; i++) {
			eigVals[i] = eigDec.getRealEigenvalue(i);
			eigVecs[i] = eigDec.getEigenvector(i).toArray();
		}
		
		// Dimensionality reduction
		int[] rm = rmDims(eigVals, prinComps);
		double[][] reducedData = new double[x.getRows() - rm.length][x.getCols()];
		for (int i = 0, ri = 0, rmi = 0; i < x.getRows(); i++) {
			if (rmi < rm.length && i == rm[rmi])
				rmi++;
			else if (ri < reducedData.length)
				reducedData[ri++] = x.getRowAt(i);
		}
		x = new Matrix(reducedData);
		return rm;
	}

	private static int[] rmDims(double[] eigVals, double[] prinComps) {
		double[] sortVals = eigVals.clone();
		int[] rm = new int[eigVals.length - propOfVar(prinComps)];

		Arrays.sort(sortVals);
		for (int i = 0; i < rm.length; i++) {
			for (int j = 0; j < eigVals.length; j++) {
				if (sortVals[i] == eigVals[j]) {
					rm[i] = j;
					eigVals[j] = Integer.MIN_VALUE;
					break;
				}
			}
		}
		return rm;
	}

	private static Matrix covarianceMatrix(Matrix x) {
//		System.out.println("X: \n" + x);
		Matrix xClone = x.clone(), 
			xTrans = x.clone();
		xTrans.transpose();
//		System.out.println("Transpose: \n" + xTrans);
		xClone.multiplyRight(xTrans);
//		System.out.println("Product: \n" + xClone);
		xClone.multiplyScalar(1.0 / x.getCols());
//		System.out.println("Covariance: \n" + xClone);
		return xClone;
	}

	private static int propOfVar(double[] prinComps) {
		double den = Vector.sum(prinComps);
		for (int k = 1; k <= prinComps.length; k++) {
			if (Vector.sumTo(prinComps, k) / den >= 0.9)
				return k;
		}
		return prinComps.length;
	}

	private static double variance(double[] x) {
		double sum = 0, diff, xAve = Vector.ave(x);
		for (int i = 0; i < x.length; i++)
			sum += (diff = x[i] - xAve) * diff;
		return sum / (x.length - 1);
	}

	private static double[] sortDescending(double[] vector) {
		double[] rev = new double[vector.length];
		Arrays.sort(vector);
		for (int i = 0; i < rev.length; i++)
			rev[i] = vector[rev.length - 1 - i];
		return rev;
	}	

}
