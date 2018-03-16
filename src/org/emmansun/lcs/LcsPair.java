package org.emmansun.lcs;

import java.util.List;

public class LcsPair {
	private List<Integer> modelIndexes;
	private List<Integer> sampleIndexes;

	public LcsPair(List<Integer> modelIndexes, List<Integer> sampleIndexes) {
		super();
		this.modelIndexes = modelIndexes;
		this.sampleIndexes = sampleIndexes;
	}

	public List<Integer> getModelIndexes() {
		return modelIndexes;
	}

	public List<Integer> getSampleIndexes() {
		return sampleIndexes;
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append(modelIndexes.toString()).append("=").append(sampleIndexes.toString());
		return buffer.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((modelIndexes == null) ? 0 : modelIndexes.hashCode());
		result = prime * result
				+ ((sampleIndexes == null) ? 0 : sampleIndexes.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LcsPair other = (LcsPair) obj;
		if (modelIndexes == null) {
			if (other.modelIndexes != null)
				return false;
		} else if (!modelIndexes.equals(other.modelIndexes))
			return false;
		if (sampleIndexes == null) {
			if (other.sampleIndexes != null)
				return false;
		} else if (!sampleIndexes.equals(other.sampleIndexes))
			return false;
		return true;
	}


	
}
