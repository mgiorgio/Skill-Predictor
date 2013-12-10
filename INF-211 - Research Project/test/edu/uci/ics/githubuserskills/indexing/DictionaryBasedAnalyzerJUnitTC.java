package edu.uci.ics.githubuserskills.indexing;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.junit.Assert;
import org.junit.Test;

import edu.uci.ics.githubuserskills.profile.DictionaryBasedAnalyzer;
import edu.uci.ics.githubuserskills.profile.SkillTermsDictionary;

public class DictionaryBasedAnalyzerJUnitTC {

	public DictionaryBasedAnalyzerJUnitTC() {
	}

	@Test
	public void testWhiteSpaceSeparator() throws IOException {
		List<String> words = Arrays.asList("this", "is", "just", "a", "text");
		testTokenization(words, words, words);
	}

	@Test
	public void testCamelCaseSeparator() throws IOException {
		List<String> words = Arrays.asList("ThisIsJustText");
		List<String> expectedTerms = Arrays.asList("this", "is", "just", "text");
		testTokenization(words, expectedTerms, expectedTerms);
	}

	private void testTokenization(List<String> inputWords, Collection<String> dictionary, List<String> expectedTerms) throws IOException {
		final String input = StringUtils.join(Arrays.asList(inputWords), " ");

		DictionaryBasedAnalyzer analyzer = new DictionaryBasedAnalyzer(new SkillTermsDictionary("test", new HashSet<String>(dictionary)));

		List<String> terms = extractTermsAsStrings(input, analyzer);

		Assert.assertArrayEquals(terms.toArray(new String[terms.size()]), expectedTerms.toArray(new String[expectedTerms.size()]));
	}

	private List<String> extractTermsAsStrings(final String input, DictionaryBasedAnalyzer analyzer) throws IOException {
		List<String> terms = new ArrayList<String>();

		TokenStream ts = analyzer.tokenStream("myfield", new StringReader(input));
		CharTermAttribute termAtt = ts.addAttribute(CharTermAttribute.class);

		try {
			ts.reset(); // Resets this stream to the beginning. (Required)
			while (ts.incrementToken()) {
				String term = termAtt.toString();
				terms.add(term);
			}
			ts.end(); // Perform end-of-stream operations, e.g. set the final
						// offset.
		} finally {
			ts.close(); // Release resources associated with this stream.
			analyzer.close();
		}

		return terms;
	}
}
