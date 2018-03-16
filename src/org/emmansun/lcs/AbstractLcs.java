package org.emmansun.lcs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class AbstractLcs<T> {
	protected static List<Integer> EMPTY_INDEXLIST = new ArrayList<Integer>();
	protected static LcsPair EMPTY_LCSPAIR = new LcsPair(EMPTY_INDEXLIST,
			EMPTY_INDEXLIST);
	protected List<T> model;
	protected List<T> sample;
	protected int[][] lengthMatrix;
	protected int[][] pathDirectionMatrix;
	protected int rows;
	protected int columns;

	public AbstractLcs(List<T> model, List<T> sample) {
		this.model = model;
		this.sample = sample;
		this.rows = model.size() + 1;
		this.columns = sample.size() + 1;
		this.lengthMatrix = new int[rows][columns];
		this.pathDirectionMatrix = new int[rows][columns];
	}

	protected void whenDiagonalEquals(int row, int column) {
	}
	
	protected void whenLeftGreaterThanTop(int row, int column) {
	}
	
	protected void whenTopGreaterThanLeft(int row, int column) {
	}
	
	protected void whenTopEqualsLeft(int row, int column) {
	}
	
	protected void computeGraph() {
		// DP
		for (int i = 1; i < rows; i++) {
			for (int j = 1; j < columns; j++) {
				if (i <= model.size() && j <= sample.size()
						&& model.get(i - 1).equals(sample.get(j - 1))) {
					lengthMatrix[i][j] = lengthMatrix[i - 1][j - 1] + 1;
					pathDirectionMatrix[i][j] = 1;
					whenDiagonalEquals(i, j);
				} else if (lengthMatrix[i - 1][j] > lengthMatrix[i][j - 1]) {
					lengthMatrix[i][j] = lengthMatrix[i - 1][j];
					pathDirectionMatrix[i][j] = 2;
					whenLeftGreaterThanTop(i, j);
				} else if (lengthMatrix[i - 1][j] < lengthMatrix[i][j - 1]) {
					lengthMatrix[i][j] = lengthMatrix[i][j - 1];
					pathDirectionMatrix[i][j] = 3;
					whenTopGreaterThanLeft(i, j);
				} else {
					lengthMatrix[i][j] = lengthMatrix[i][j - 1];
					pathDirectionMatrix[i][j] = 4;
					whenTopEqualsLeft(i, j);
				}
			}
		}
	}
	
	public int getLcsLength() {
		return this.lengthMatrix[rows-1][columns-1];
	}

	public abstract Collection<LcsPair> findAllLcs();

	static class Point implements Comparable<Point> {
		private int row;
		private int column;

		public Point() {
			super();
		}
		
		public Point(int row, int column) {
			super();
			this.row = row;
			this.column = column;
		}

		public int getRow() {
			return row;
		}

		public void setRow(int row) {
			this.row = row;
		}

		public int getColumn() {
			return column;
		}

		public void setColumn(int column) {
			this.column = column;
		}

		private long getDiagonalLength() {
			return (row * row) + (column * column);
		}

		@Override
		public int compareTo(Point other) {
			return (int) (this.getDiagonalLength() - other.getDiagonalLength());
		}

		@Override
		public String toString() {
			return "(" + row + ", " + column + ")";
		}

		@Override
		public int hashCode() {
			return toString().hashCode();
		}

	}
	
	
}