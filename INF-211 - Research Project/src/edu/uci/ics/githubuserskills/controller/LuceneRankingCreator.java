package edu.uci.ics.githubuserskills.controller;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.BytesRef;

import edu.uci.ics.githubuserskills.lucene.LuceneDirectoryFactory;
import edu.uci.ics.githubuserskills.lucene.LuceneFSDirectoryFactory;
import edu.uci.ics.githubuserskills.lucene.LuceneUtils;
import edu.uci.ics.githubuserskills.ranking.UserRankingEntry;
import edu.uci.ics.githubuserskills.ranking.UserRanking;

public class LuceneRankingCreator {

	private LuceneDirectoryFactory directoryFactory;

	public LuceneRankingCreator() {
		this.setDirectoryFactory(new LuceneFSDirectoryFactory());
	}

	public void setDirectoryFactory(LuceneDirectoryFactory directoryFactory) {
		this.directoryFactory = directoryFactory;
	}

	private LuceneDirectoryFactory getDirectoryFactory() {
		return directoryFactory;
	}

	public List<UserRanking> rankings() throws UserRankingCreationException {
		List<String> authors = this.getAuthors();
		List<UserRanking> rankings = new LinkedList<UserRanking>();

		for (String author : authors) {
			rankings.add(this.ranking(author));
		}

		return rankings;
	}

	private List<String> getAuthors() {
		File[] subDirectories = new File(LuceneUtils.getExecutionDirectory()).listFiles(new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				return pathname.isDirectory();
			}
		});

		List<String> authors = new LinkedList<String>();

		for (int i = 0; i < subDirectories.length; i++) {
			authors.add(subDirectories[i].getName());
		}

		return authors;
	}

	public UserRanking ranking(String author) throws UserRankingCreationException {
		IndexReader indexReader;
		try {
			indexReader = this.openIndexReaderForAuthor(author);
		} catch (IOException e1) {
			throw new UserRankingCreationException(e1);
		}

		Map<String, Long> freqMap = new HashMap<String, Long>();

		int numDocs = indexReader.numDocs();

		try {
			for (int i = 0; i < numDocs; i++) {
				TermsEnum termsEnum = indexReader.getTermVector(i, "contents").iterator(null);

				BytesRef text = null;
				while ((text = termsEnum.next()) != null) {
					String term = text.toString();
					long freq = termsEnum.totalTermFreq();

					if (!freqMap.containsKey(term)) {
						freqMap.put(term, 0L);
					}
					freqMap.put(term, freqMap.get(term) + freq);
				}
			}
		} catch (IOException e) {
			throw new UserRankingCreationException(e);
		}

		return createUserRanking(freqMap);
	}

	private UserRanking createUserRanking(Map<String, Long> freqMap) {
		UserRanking ranking = new UserRanking();
		for (Entry<String, Long> termFreq : freqMap.entrySet()) {
			UserRankingEntry pair = new UserRankingEntry(termFreq.getKey(), termFreq.getValue());
			ranking.addEntry(pair);
		}

		return ranking;
	}

	private IndexReader openIndexReaderForAuthor(String author) throws IOException {
		Directory dir = this.openDirectoryForAuthor(author);
		IndexReader reader = DirectoryReader.open(dir);

		return reader;
	}

	private Directory openDirectoryForAuthor(String author) throws IOException {
		return this.getDirectoryFactory().openDirectory(author);
	}
}
