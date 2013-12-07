package edu.uci.ics.githubuserskills.ranking;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.githubuserskills.controller.UserRankingCreationException;
import edu.uci.ics.githubuserskills.lucene.LuceneUtils;
import edu.uci.ics.githubuserskills.model.RawSkillData;

/**
 * @author Matias
 * 
 */
public class DirectLuceneBasedUserRankingCreator implements UserRankingCreator {

	private static final Logger log = LoggerFactory.getLogger(DirectLuceneBasedUserRankingCreator.class);

	private UserRankingBuilder freqsHolder;

	private DictionaryBasedAnalyzer analyzer;

	public DirectLuceneBasedUserRankingCreator() {
	}

	@Override
	public UserRanking rank(String author, List<RawSkillData> rawSkillDataObjects) throws UserRankingCreationException {
		UserRankingBuilder userRankingBuilder = new UserRankingBuilder();
		userRankingBuilder.setAuthor(author);

		try {
			for (RawSkillData rawSkillData : rawSkillDataObjects) {

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
			}
		} catch (IOException e) {
			throw new UserRankingCreationException(e);
		}

		return userRankingBuilder.build();
	}

	public UserRankingBuilder getFreqsHolder() {
		return this.freqsHolder;
	}

	@Override
	public void initialize() throws UserRankingCreationException {
		log.info("Initializing Dictionary Based Analyzer...");
		try {
			Set<String> fixedDictionary = LuceneUtils.loadFixedDictionary();
			analyzer = new DictionaryBasedAnalyzer(fixedDictionary);
			analyzer.setDictionary(fixedDictionary);
			log.info("Dictionary Based Analyzer initialized successfully.");
		} catch (IOException e1) {
			throw new UserRankingCreationException(e1);
		}

	}

	@Override
	public void close() throws UserRankingCreationException {
		analyzer.close();
	}
}
