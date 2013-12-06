/**
 * 
 */
package edu.uci.ics.githubuserskills.indexing;

import java.io.Reader;
import java.util.HashSet;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.miscellaneous.KeepWordFilter;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.util.Version;

/**
 * @author Matias
 * 
 */
public class DictionaryBasedAnalyzer extends Analyzer {

	private Set<String> dictionary;

	/**
	 * 
	 */
	public DictionaryBasedAnalyzer() {
		this(new HashSet<String>());
	}

	public DictionaryBasedAnalyzer(Set<String> dictionary) {
		this.setDictionary(dictionary);
	}

	public void setDictionary(Set<String> dictionary) {
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
		WhitespaceTokenizer source = new WhitespaceTokenizer(Version.LUCENE_46, reader);

		TokenStream result = new KeepWordFilter(Version.LUCENE_46, source, this.adaptDictionaryToLucene());

		TokenStreamComponents components = new TokenStreamComponents(source, result);

		return components;
	}

	private CharArraySet adaptDictionaryToLucene() {
		// TODO Cache this object.
		CharArraySet arraySet = new CharArraySet(Version.LUCENE_46, dictionary, true);

		return arraySet;
	}

}
