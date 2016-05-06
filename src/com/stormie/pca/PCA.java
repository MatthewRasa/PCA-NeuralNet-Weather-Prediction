package com.stormie.pca;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;
import java.util.Arrays;

public class PCA {

	public static void runPCA(Matrix x) {
		// Find principal components, sort in descending order
		double[] prinComps = new double[x.getRows()];
		for (int r = 0; r < prinComps.length; r++)
			prinComps[r] = variance(x.getRowAt(r));
		prinComps = sortDescending(prinComps);

		System.out.println("Principal components: " + Vector.print(prinComps));

		// Compute the covariance matrix
		Matrix c = covarianceMatrix(x);
		System.out.println("Covariance matrix: \n" + c);

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
		
		Matrix p = new Matrix(eigVecs);
		System.out.println("Eigenvalues: " + Vector.print(eigVals));
		System.out.println("Eigenvectors: \n" + p);
		
		System.out.println("PoV: " + propOfVar(prinComps));
	}

	private static Matrix covarianceMatrix(Matrix x) {
		Matrix xClone = x.clone(), 
			xTrans = x.clone();
		xTrans.transpose();
		xClone.multiplyRight(xTrans);
		xClone.multiplyScalar(1.0 / x.getCols());
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

	public class EigenPair {

		private double[] mEigVector;
		private double mEigValue;
	
		public EigenPair(double eigValue, double[] eigVector) {
			mEigVector = eigVector;
			mEigValue = eigValue;
		}

	}

}
