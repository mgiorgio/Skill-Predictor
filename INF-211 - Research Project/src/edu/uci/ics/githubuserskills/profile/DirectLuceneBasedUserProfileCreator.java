package edu.uci.ics.githubuserskills.profile;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.githubuserskills.controller.UserRankingCreationException;
import edu.uci.ics.githubuserskills.lucene.Utils;
import edu.uci.ics.githubuserskills.model.RawSkillData;

/**
 * @author Matias
 * 
 */
public class DirectLuceneBasedUserProfileCreator implements RawSkillDataProcessor {

	private static final Logger log = LoggerFactory.getLogger(DirectLuceneBasedUserProfileCreator.class);

	private DictionaryBasedAnalyzer analyzer;

	private Collection<SkillTermsDictionary> dictionaries;

	public DirectLuceneBasedUserProfileCreator(Collection<SkillTermsDictionary> dictionaries) {
		this.dictionaries = dictionaries;
	}

	@Override
	public UserProfile rank(String author, List<RawSkillData> rawSkillDataObjects) throws UserRankingCreationException {
		UserProfileBuilder userProfileBuilder = new UserProfileBuilder(this.dictionaries);

		userProfileBuilder.setAuthor(author);

		try {
			for (RawSkillData rawSkillData : rawSkillDataObjects) {

				final String input = rawSkillData.getContents();

				TokenStream ts;
				ts = analyzer.tokenStream(Utils.Globals.CONTENTS_FIELD, input);

				CharTermAttribute termAtt = ts.addAttribute(CharTermAttribute.class);

				try {
					ts.reset(); // Resets this stream to the beginning.
								// (Required)
					while (ts.incrementToken()) {
						userProfileBuilder.increment(termAtt.toString());
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

		return userProfileBuilder.buildProfile();
	}

	@Override
	public void initialize() throws UserRankingCreationException {
		log.info("Initializing Dictionary Based Analyzer...");
		try {
			SkillTermsDictionary tokenizationDictionary = DictionaryManager.loadTokenizationDictionary();
			analyzer = new DictionaryBasedAnalyzer(tokenizationDictionary);
			analyzer.setDictionary(tokenizationDictionary);
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
