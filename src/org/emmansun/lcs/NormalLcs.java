package org.emmansun.lcs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NormalLcs<T> extends AbstractLcs<T> {
	private static final Point ZERO_VERTEX = new Point(0, 0);
	private Point[][][] lcsEdges;
	private Point[][][] equivalentEdges;
	private List<Point> commonVertices = new ArrayList<Point>();
	private Integer[] commonVerticeRows;
	private Integer[] commonVerticeCols;
	private Set<Integer> commonRowSet = new HashSet<Integer>();
	private Set<Integer> commonColSet = new HashSet<Integer>();

	public NormalLcs(List<T> model, List<T> sample) {
		super(model, sample);
		lcsEdges = new Point[rows][columns][2];
		equivalentEdges = new Point[rows][columns][2];

	}

	@Override
	protected void whenDiagonalEquals(int row, int column) {
		int top = lengthMatrix[row][column - 1];
		int left = lengthMatrix[row - 1][column];

		commonRowSet.add(row);
		commonColSet.add(column);

		lcsEdges[row][column][0] = new Point(row - 1, column - 1);
		commonVertices.add(new Point(row, column));

		if (lengthMatrix[row][column] == top) {
			equivalentEdges[row][column][0] = new Point(row, column - 1);
		}
		if (lengthMatrix[row][column] == left) {
			equivalentEdges[row][column][1] = new Point(row - 1, column);
		}

	}

	@Override
	protected void whenLeftGreaterThanTop(int row, int column) {
		lcsEdges[row][column][0] = new Point(row - 1, column);
	}

	@Override
	protected void whenTopGreaterThanLeft(int row, int column) {
		lcsEdges[row][column][0] = new Point(row, column - 1);
	}

	@Override
	protected void whenTopEqualsLeft(int row, int column) {
		lcsEdges[row][column][0] = new Point(row, column - 1);
		lcsEdges[row][column][1] = new Point(row - 1, column);
	}

	private boolean hasEdges(Point vertex) {
		return (lcsEdges[vertex.getRow()][vertex.getColumn()][0] != null || lcsEdges[vertex
				.getRow()][vertex.getColumn()][1] != null);
	}

	private Set<LcsPair> createCandidatesWithEmptyPair() {
		Set<LcsPair> candidates = new HashSet<LcsPair>();
		candidates.add(new LcsPair(new ArrayList<Integer>(),
				new ArrayList<Integer>()));
		return candidates;
	}

	@Override
	public Collection<LcsPair> findAllLcs() {
		computeGraph();
		Set<LcsPair> results = new HashSet<LcsPair>();
		if (this.commonVertices.isEmpty()) {
			results.add(EMPTY_LCSPAIR);
			return results;
		}
		this.commonVerticeRows = this.commonRowSet.toArray(new Integer[0]);
		this.commonVerticeCols = this.commonColSet.toArray(new Integer[0]);
		this.commonRowSet = null;
		this.commonColSet = null;
		Arrays.sort(this.commonVerticeRows);
		Arrays.sort(this.commonVerticeCols);

		return findAllLcs(findNextVertex(ZERO_VERTEX, new Point(rows - 1,
				columns - 1)));
	}

	private Collection<LcsPair> findAllLcs(Point vertex) {
		if (vertex == null || !hasEdges(vertex)) {
			return createCandidatesWithEmptyPair();
		}
		// traverse all normal LCS edges to build LCS
		Collection<LcsPair> candidates = traverseEdges(lcsEdges[vertex.getRow()][vertex
				.getColumn()]);
		if (pathDirectionMatrix[vertex.getRow()][vertex.getColumn()] == 1) {
			for (LcsPair candidate : candidates) {
				candidate.getModelIndexes().add(vertex.getRow() - 1);
				candidate.getSampleIndexes().add(vertex.getColumn() - 1);
			}
			// traverse all equivalent edges to get all candidates that are as
			// good as the current LCS
			candidates
					.addAll(traverseEdges(equivalentEdges[vertex.getRow()][vertex
							.getColumn()]));
		}
		return candidates;
	}

	private Collection<LcsPair> traverseEdges(Point[] points) {
		Set<LcsPair> candidates = new HashSet<LcsPair>();
		for (Point edge : points) {
			if (edge != null) {
				Point nextVertex = findNextVertex(ZERO_VERTEX, edge);
				candidates.addAll(findAllLcs(nextVertex));
			}
		}
		return candidates;
	}

	private int findEndIndex(Integer[] arrays, int start, int end) {
		int i = Arrays.binarySearch(arrays, end);
		if (i < 0) {
			i = -(i + 1) - 1;
		}
		if (i < 0) {
			return -1;
		} else if (arrays[i] >= start) {
			return arrays[i];
		}
		return -1;
	}

	private int findNextVertexRowIndex(Point startPoint, Point endPoint) {
		return findEndIndex(commonVerticeRows, startPoint.getRow(),
				endPoint.getRow());
	}

	private int findNextVertexColIndex(Point startPoint, Point endPoint) {
		return findEndIndex(commonVerticeCols, startPoint.getColumn(),
				endPoint.getColumn());
	}

	private Point findNextVertex(Point startVertex, Point endVertex) {
		Point nextPoint = new Point(-1, -1);
		if (pathDirectionMatrix[endVertex.getRow()][endVertex.getColumn()] == 1) {
			return endVertex;
		}
		if (endVertex.getRow() > endVertex.getColumn()) {
			nextPoint.setColumn(findNextVertexColIndex(startVertex, endVertex));
			if (nextPoint.getColumn() < 0) {
				return null;
			}
			nextPoint.setRow(findNextVertexRowIndex(startVertex, endVertex));
		} else {
			nextPoint.setRow(findNextVertexRowIndex(startVertex, endVertex));
			if (nextPoint.getRow() < 0) {
				return null;
			}
			nextPoint.setColumn(findNextVertexColIndex(startVertex, endVertex));
		}
		if (nextPoint.getRow() < 0 || nextPoint.getColumn() < 0) {
			return null;
		} else {
			return nextPoint;
		}

	}

}
