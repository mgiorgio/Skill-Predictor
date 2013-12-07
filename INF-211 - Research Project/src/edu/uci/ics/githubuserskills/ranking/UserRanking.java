package edu.uci.ics.githubuserskills.ranking;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.githubuserskills.lucene.LuceneUtils;

/**
 * @author Matias
 * 
 */
public class UserRanking {

	private static final Logger console = LoggerFactory.getLogger("console");

	private String author;

	private List<UserRankingEntry> terms;

	public UserRanking() {
		this.terms = new LinkedList<UserRankingEntry>();
	}

	public void addEntry(UserRankingEntry frequencyPair) {
		this.terms.add(frequencyPair);
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	protected void sort() {
		Collections.sort(this.terms, new Comparator<UserRankingEntry>() {

			@Override
			public int compare(UserRankingEntry o1, UserRankingEntry o2) {
				if (o1.getFrequency() > o2.getFrequency()) {
					return -1;
				} else if (o1.getFrequency() < o2.getFrequency()) {
					return 1;
				} else {
					return 0;
				}
			}
		});
	}

	public List<UserRankingEntry> getTerms() {
		this.sort();
		return new ArrayList<UserRankingEntry>(this.terms);
	}

	public void setTerms(List<UserRankingEntry> terms) {
		this.terms = terms;
	}

	public void exportTextFile() throws IOException {
		this.sort();

		String fileName = LuceneUtils.USERDATA + File.separator + author + ".txt";
		File exportFile = new File(fileName);

		FileUtils.touch(exportFile);

		FileWriter writer = new FileWriter(exportFile);
		for (UserRankingEntry termFreq : this.terms) {
			writer.write(String.format("%s: %s\n", termFreq.getTerm(), termFreq.getFrequency()));
		}
		writer.close();
		console.info("User Ranking for [{}] created in [{}].", author, fileName);
	}
}