package edu.uci.ics.githubuserskills.indexing;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashSet;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.junit.Test;

public class DirectTokenizationTest {

	public DirectTokenizationTest() {
	}

	@Test
	public void testWhiteSpaceSeparator() throws IOException {
		final String input = "This is just a text";

		DictionaryBasedAnalyzer analyzer = new DictionaryBasedAnalyzer();

		analyzer.setDictionary(new HashSet<>(Arrays.asList("just")));

		TokenStream ts = analyzer.tokenStream("myfield", new StringReader(input));
		CharTermAttribute termAtt = ts.addAttribute(CharTermAttribute.class);

		try {
			ts.reset(); // Resets this stream to the beginning. (Required)
			while (ts.incrementToken()) {
				System.out.println("token: " + termAtt);
			}
			ts.end(); // Perform end-of-stream operations, e.g. set the final
						// offset.
		} finally {
			ts.close(); // Release resources associated with this stream.
			analyzer.close();
		}
	}

}
