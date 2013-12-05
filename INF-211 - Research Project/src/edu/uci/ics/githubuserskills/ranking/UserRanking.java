package edu.uci.ics.githubuserskills.ranking;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class UserRanking {

	private String author;

	private List<TermFrequencyPair> terms;

	public UserRanking() {
		this.terms = new LinkedList<TermFrequencyPair>();
	}

	public void addTermFrequencyPair(TermFrequencyPair frequencyPair) {
		this.terms.add(frequencyPair);
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public List<TermFrequencyPair> getTerms() {
		return new ArrayList<TermFrequencyPair>(this.terms);
	}

	public void setTerms(List<TermFrequencyPair> terms) {
		this.terms = terms;
	}

}
