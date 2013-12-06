package edu.uci.ics.githubuserskills.ranking;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import edu.uci.ics.githubuserskills.controller.UserRankingCreationException;
import edu.uci.ics.githubuserskills.lucene.LuceneUtils;
import edu.uci.ics.githubuserskills.model.RawSkillData;

public class DirectLuceneBasedUserRankingCreator implements UserRankingCreator {

	private UserRankingBuilder freqsHolder;

	public DirectLuceneBasedUserRankingCreator() {
	}

	@Override
	public UserRanking rank(String author, List<RawSkillData> rawSkillDataObjects) throws UserRankingCreationException {
		UserRankingBuilder userRankingBuilder = new UserRankingBuilder();
		DictionaryBasedAnalyzer analyzer;
		Set<String> fixedDictionary;

		try {
			fixedDictionary = LuceneUtils.loadFixedDictionary();
		} catch (IOException e1) {
			throw new UserRankingCreationException(e1);
		}

		analyzer = new DictionaryBasedAnalyzer(fixedDictionary);
		analyzer.setDictionary(fixedDictionary);

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
						userRankingBuilder.increment(termAtt.toString());
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

		return userRankingBuilder.build();
	}

	public UserRankingBuilder getFreqsHolder() {
		return this.freqsHolder;
	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub

	}

	@Override
	public void close() throws UserRankingCreationException {
		// TODO Auto-generated method stub

	}
}
