package edu.uci.ics.githubuserskills.profile;

public class UserRankingEntry {

	private String term;

	private long frequency;

	private double weight;

	public UserRankingEntry() {
		this(null, 0L);
	}

	public UserRankingEntry(String term, long frequency) {
		this.setFrequency(frequency);
		this.setTerm(term);
	}

	public double getWeight() {
		return this.weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public long getFrequency() {
		return frequency;
	}

	public void setFrequency(long frequency) {
		this.frequency = frequency;
	}

	@Override
	public String toString() {
		return this.getTerm() + ":" + this.getFrequency() + " (" + this.weight + ")";
	}
}
