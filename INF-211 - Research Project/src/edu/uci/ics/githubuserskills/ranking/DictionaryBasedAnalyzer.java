/**
 * 
 */
package edu.uci.ics.githubuserskills.ranking;

import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.miscellaneous.KeepWordFilter;
import org.apache.lucene.analysis.miscellaneous.WordDelimiterFilter;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.util.Version;

import edu.uci.ics.githubuserskills.lucene.Utils;

/**
 * @author Matias
 * 
 */
public class DictionaryBasedAnalyzer extends Analyzer {

	private SkillTermsDictionary dictionary;

	public DictionaryBasedAnalyzer(SkillTermsDictionary dictionary) {
		this.setDictionary(dictionary);
	}

	public void setDictionary(SkillTermsDictionary dictionary) {
		this.dictionary = dictionary;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.lucene.analysis.Analyzer#createComponents(java.lang.String,
	 * java.io.Reader)
	 */
	@Override
	protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
		WhitespaceTokenizer source = new WhitespaceTokenizer(Utils.LUCENE_VERSION, reader);

		TokenStream result = null;

		int configurationFlags = WordDelimiterFilter.GENERATE_WORD_PARTS | WordDelimiterFilter.SPLIT_ON_CASE_CHANGE;

		result = new WordDelimiterFilter(source, configurationFlags, null);

		result = new KeepWordFilter(Utils.LUCENE_VERSION, result, this.adaptDictionaryToLucene());

		result = new LowerCaseFilter(Utils.LUCENE_VERSION, result);

		TokenStreamComponents components = new TokenStreamComponents(source, result);

		return components;
	}

	private CharArraySet adaptDictionaryToLucene() {
		// TODO Cache this object.
		CharArraySet arraySet = new CharArraySet(Version.LUCENE_46, dictionary.getDictionary(), true);

		return arraySet;
	}

}
