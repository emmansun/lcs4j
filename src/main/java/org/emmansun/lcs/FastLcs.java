package org.emmansun.lcs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public class FastLcs<T> extends AbstractLcs<T> {
	private static final int DIRECTION_LEFT = 0;
	private static final int DIRECTION_TOP = 1;

	public FastLcs(List<T> model, List<T> sample) {
		super(model, sample);
	}

	public Collection<LcsPair> findAllLcs() {
		Stack<Element> store = new Stack<Element>();
		Stack<Element> print = new Stack<Element>();
		int topLcsLen = 0;
		Collection<LcsPair> results = new HashSet<LcsPair>();

		computeGraph();
		if (lengthMatrix[rows - 1][columns - 1] == 0) {
			results.add(EMPTY_LCSPAIR);
			return results;
		}

		Element storeTop, element, virtualNode = new Element(
				lengthMatrix[rows - 1][columns - 1] + 1, rows, columns);
		store.push(virtualNode);

		while (!store.isEmpty()) {
			storeTop = store.pop();
			if (storeTop.row <= 1 || storeTop.column <= 1) {
				if (storeTop.row > 0 && storeTop.column > 0) {
					print.push(storeTop);
				}
				results.add(createLcsPair(print));
				if (!store.isEmpty()) {
					element = store.peek();
					topLcsLen = element.getLcsLen();
					while (!print.isEmpty()) {
						element = print.peek();
						if (element.getLcsLen() <= topLcsLen) {
							print.pop();
						} else {
							break;
						}
					}
				}
			} else {
				print.push(storeTop);
				Point leftPoint = new Point();
				SearchElement(leftPoint, storeTop.row - 1, storeTop.column - 1,
						DIRECTION_LEFT);
				Point topPoint = new Point();
				SearchElement(topPoint, storeTop.row - 1, storeTop.column - 1,
						DIRECTION_TOP);
				if (leftPoint.getRow() == topPoint.getRow()
						&& leftPoint.getColumn() == topPoint.getColumn()) {
					store.push(new Element(
							lengthMatrix[leftPoint.getRow()][leftPoint
									.getColumn()], leftPoint.getRow(),
							leftPoint.getColumn()));
				} else {
					addAllJumpElements(store, leftPoint, topPoint);
				}
			}
		}
		return results;
	}

	private void addAllJumpElements(Stack<Element> store, Point leftPoint,
			Point topPoint) {
		int currentLcsLen = 0;
		for (int i = topPoint.getRow(); i >= leftPoint.getRow(); i--) {
			for (int j = leftPoint.getColumn(); j >= topPoint.getColumn(); j--) {
				// Emman, check lcs len to avoid mix different lcs len cases.
				if (pathDirectionMatrix[i][j] <= 1
						&& lengthMatrix[i][j] >= currentLcsLen) {
					Element e = new Element(lengthMatrix[i][j], i, j);
					store.push(e);
					currentLcsLen = lengthMatrix[i][j];
				}
			}
		}
	}

	private void updateToMostTop(Point point) {
		int k = point.getColumn() - 1;
		int pointCol = point.getColumn();
		while (true) {
			if (lengthMatrix[point.getRow()][k] == lengthMatrix[point.getRow()][point
					.getColumn()]) {
				if (pathDirectionMatrix[point.getRow()][k] == 1) {
					pointCol = k;
				}
				k--;
			} else {
				break;
			}
		}
		point.setColumn(pointCol);
	}

	private void updateToMostLeft(Point point) {
		int k = point.getRow() - 1;
		int pointRow = point.getRow();
		while (true) {
			if (lengthMatrix[k][point.getColumn()] == lengthMatrix[point
					.getRow()][point.getColumn()]) {
				if (pathDirectionMatrix[k][point.getColumn()] == 1) {
					pointRow = k;
				}
				k--;
			} else {
				break;
			}
		}
		point.setRow(pointRow);
	}

	private void SearchElement(Point point, int row, int column, int type) {
		switch (pathDirectionMatrix[row][column]) {
		case 1:
			point.setColumn(column);
			point.setRow(row);
			// Emman, if no below process, we can NOT get complete candidate set
			if (row > 0 && column > 0) {
				if (type == 0) {
					updateToMostLeft(point);
				} else {
					updateToMostTop(point);
				}
			}
			break;
		case 2:
			SearchElement(point, row - 1, column, type);
			break;
		case 3:
			SearchElement(point, row, column - 1, type);
			break;
		case 4:
			if (type == 0) {
				SearchElement(point, row - 1, column, type);
			} else {
				SearchElement(point, row, column - 1, type);
			}
			break;
		}
	}

	private LcsPair createLcsPair(Stack<Element> print) {
		Iterator<Element> iterator = print.iterator();
		List<Integer> modelList = new ArrayList<Integer>();
		List<Integer> sampleList = new ArrayList<Integer>();
		while (iterator.hasNext()) {
			Element element = iterator.next();
			if (element.getRow() > 0 && element.getRow() < this.rows) {
				modelList.add(element.getRow() - 1);
			}
			if (element.getColumn() > 0 && element.getColumn() < this.columns) {
				sampleList.add(element.getColumn() - 1);
			}
		}
		Collections.reverse(modelList);
		Collections.reverse(sampleList);
		LcsPair lcsPair = new LcsPair(modelList, sampleList);
		return lcsPair;
	}

	private static class Element {
		private int row;
		private int column;
		private int lcsLen;

		public Element(int lcsLen, int row, int column) {
			super();
			this.row = row;
			this.column = column;
			this.lcsLen = lcsLen;
		}

		public int getRow() {
			return row;
		}

		public int getColumn() {
			return column;
		}

		public int getLcsLen() {
			return lcsLen;
		}
	}
}
