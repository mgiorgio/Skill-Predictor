package edu.uci.ics.githubuserskills.indexing;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import edu.uci.ics.githubuserskills.lucene.LuceneUtils;
import edu.uci.ics.githubuserskills.model.RawSkillData;
import edu.uci.ics.githubuserskills.ranking.TermFrequencyPair;
import edu.uci.ics.githubuserskills.ranking.UserRanking;

public class DirectIndexer implements RawSkillDataIndexer {

	public DirectIndexer() {
	}

	@Override
	public void index(List<RawSkillData> rawSkillDataObjects) throws IndexingException {
		/*
		 * Assume all the rawSkillDataObjects belong to the same author.
		 */
		FreqCalculator freqCalculator = new FreqCalculator();
		DictionaryBasedAnalyzer analyzer;
		try {
			analyzer = new DictionaryBasedAnalyzer(LuceneUtils.loadFixedDictionary());
		} catch (IOException e1) {
			throw new IndexingException(e1);
		}
		analyzer.setDictionary(new HashSet<>(Arrays.asList("just")));
		for (RawSkillData rawSkillData : rawSkillDataObjects) {

			try {
				final String input = rawSkillData.getContents();

				TokenStream ts;
				ts = analyzer.tokenStream(LuceneUtils.Globals.CONTENTS_FIELD, input);

				CharTermAttribute termAtt = ts.addAttribute(CharTermAttribute.class);

				try {
					ts.reset(); // Resets this stream to the beginning.
								// (Required)
					while (ts.incrementToken()) {
						freqCalculator.increment(termAtt.toString());
					}
					ts.end(); // Perform end-of-stream operations, e.g. set the
								// final
								// offset.
				} finally {
					ts.close(); // Release resources associated with this
								// stream.
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				analyzer.close();
			}
		}
		UserRanking userRanking = this.createUserRanking(freqCalculator);
		try {
			userRanking.exportTextFile();
		} catch (IOException e) {
			throw new IndexingException(e);
		}
	}

	private UserRanking createUserRanking(FreqCalculator freqCalculator) {
		UserRanking ranking = new UserRanking();

		Map<String, Long> freqs = freqCalculator.freqs();

		for (Entry<String, Long> freq : freqs.entrySet()) {
			ranking.addEntry(new TermFrequencyPair(freq.getKey(), freq.getValue()));
		}

		return ranking;
	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub

	}

	@Override
	public void close() throws IndexingException {
		// TODO Auto-generated method stub

	}
}
