package com.stormie.pca;

public class Matrix {

	private double[][] mData;

	public Matrix(double[][] data) {
		mData = data;
	}

	public Matrix clone() {
		double[][] clone = new double[getRows()][getCols()];
		for (int r = 0; r < getRows(); r++) {
			for (int c = 0; c < getCols(); c++)
				clone[r][c] = mData[r][c];
		}
		return new Matrix(clone);
	}
	
	public double[] getColAt(int index) {
		double[] col = new double[mData.length];
		for (int r = 0; r < mData.length; r++)
			col[r] = mData[r][index];
		return col;
	}

	public int getCols() {
		return mData[0].length;
	}
	
	public double[][] getData() {
		return mData;
	}

	public double getElemAt(int row, int col) {
		return mData[row][col];
	}

	public double[] getRowAt(int index) {
		return mData[index];
	}

	public int getRows() {
		return mData.length;
	}

	public void multiplyRight(Matrix x) {
		if (getRows() != x.getCols())
			throw new RuntimeException("Row count of left matrix must equal column count of right matrix.");
		double[][] data = new double[getRows()][x.getCols()];
		for (int r = 0; r < getRows(); r++) {
			double[] row = getRowAt(r);
			for (int c = 0; c < x.getCols(); c++)
				data[r][c] = Vector.multiply(row, x.getColAt(c));
		}
		mData = data;
	}

	public void multiplyScalar(double x) {
		for (int r = 0; r < getRows(); r++) {
			for (int c = 0; c < getCols(); c++)
				mData[r][c] *= x;
		}
	}

	public String toString() {
		String out = "";
		for (int r = 0; r < mData.length; r++) {
			out += "[ ";
			for (int c = 0; c < mData[0].length - 1; c++)
				out += mData[r][c] + "\t";
			out += mData[r][mData[0].length - 1] + " ]\n";
		}
		return out;
	}

	public void transpose() {
		double[][] data = new double[getCols()][getRows()];
		for (int r = 0; r < getRows(); r++) {
			for (int c = 0; c < getCols(); c++)
				data[c][r] = mData[r][c];
		}
		mData = data;
	}
	
}
