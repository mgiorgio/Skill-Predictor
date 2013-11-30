package edu.uci.ics.githubuserskills;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

public class DemoMain {

	public DemoMain() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws IOException, ParseException {
		Directory indexDirectory = new RAMDirectory();

		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_46);
		IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_46, analyzer);

		iwc.setOpenMode(OpenMode.CREATE);
		IndexWriter writer;
		try {
			writer = new IndexWriter(indexDirectory, iwc);
			indexDocs(writer);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		search(indexDirectory);
	}

	private static void search(Directory indexDirectory) throws IOException, ParseException {
		String field = "message";
		int repeat = 0;
		IndexReader reader = DirectoryReader.open(indexDirectory);
		IndexSearcher searcher = new IndexSearcher(reader);
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_46);

		BufferedReader in = null;
		in = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
		QueryParser parser = new QueryParser(Version.LUCENE_46, field, analyzer);
		boolean raw = true;
		String queryString = null;
		int hitsPerPage = 10;
		while (true) {
			if (queryString == null) { // prompt the user
				System.out.println("Enter query: ");
			}

			String line = queryString != null ? queryString : in.readLine();

			if (line == null || line.length() == -1) {
				break;
			}

			line = line.trim();
			if (line.length() == 0) {
				break;
			}

			Query query = parser.parse(line);
			System.out.println("Searching for: " + query.toString(field));

			if (repeat > 0) { // repeat & time as benchmark
				Date start = new Date();
				for (int i = 0; i < repeat; i++) {
					searcher.search(query, null, 100);
				}
				Date end = new Date();
				System.out.println("Time: " + (end.getTime() - start.getTime()) + "ms");
			}

			doPagingSearch(in, searcher, query, hitsPerPage, raw, queryString == null);

			if (queryString != null) {
				break;
			}
		}
		reader.close();
	}

	private static void indexDocs(IndexWriter writer) {
		String name = "Matthias Vallentin";
		long time = System.currentTimeMillis();
		String message = "Fix conditional variance of LS estimate.\n\nIn 18.1, the equation of the conditional variance of beta hat lacked a square\noperation in the denominator.\n\nMoreover, at the end of 18.1, the statement about the estimator under the\nassumption or Normality now includes a more precise formulation.\n\nContributed by Travis Walter.";

		Document doc = new Document();

		doc.add(new LongField("time", time, Store.NO));
		doc.add(new StringField("name", name, Store.NO));
		doc.add(new TextField("message", message, Store.YES));

		try {
			writer.addDocument(doc);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * This demonstrates a typical paging search scenario, where the search
	 * engine presents pages of size n to the user. The user can then go to the
	 * next page if interested in the next hits.
	 * 
	 * When the query is executed for the first time, then only enough results
	 * are collected to fill 5 result pages. If the user wants to page beyond
	 * this limit, then the query is executed another time and all hits are
	 * collected.
	 * 
	 */
	public static void doPagingSearch(BufferedReader in, IndexSearcher searcher, Query query, int hitsPerPage, boolean raw, boolean interactive) throws IOException {

		// Collect enough docs to show 5 pages
		TopDocs results = searcher.search(query, 5 * hitsPerPage);
		ScoreDoc[] hits = results.scoreDocs;

		int numTotalHits = results.totalHits;
		System.out.println(numTotalHits + " total matching documents");

		int start = 0;
		int end = Math.min(numTotalHits, hitsPerPage);

		while (true) {
			if (end > hits.length) {
				System.out.println("Only results 1 - " + hits.length + " of " + numTotalHits + " total matching documents collected.");
				System.out.println("Collect more (y/n) ?");
				String line = in.readLine();
				if (line.length() == 0 || line.charAt(0) == 'n') {
					break;
				}

				hits = searcher.search(query, numTotalHits).scoreDocs;
			}

			end = Math.min(hits.length, start + hitsPerPage);

			for (int i = start; i < end; i++) {
				if (raw) { // output raw format
					System.out.println("doc=" + hits[i].doc + " score=" + hits[i].score);
					continue;
				}

				Document doc = searcher.doc(hits[i].doc);
				String path = doc.get("path");
				if (path != null) {
					System.out.println((i + 1) + ". " + path);
					String title = doc.get("title");
					if (title != null) {
						System.out.println("   Title: " + doc.get("title"));
					}
				} else {
					System.out.println((i + 1) + ". " + "No path for this document");
				}

			}

			if (!interactive || end == 0) {
				break;
			}

			if (numTotalHits >= end) {
				boolean quit = false;
				while (true) {
					System.out.print("Press ");
					if (start - hitsPerPage >= 0) {
						System.out.print("(p)revious page, ");
					}
					if (start + hitsPerPage < numTotalHits) {
						System.out.print("(n)ext page, ");
					}
					System.out.println("(q)uit or enter number to jump to a page.");

					String line = in.readLine();
					if (line.length() == 0 || line.charAt(0) == 'q') {
						quit = true;
						break;
					}
					if (line.charAt(0) == 'p') {
						start = Math.max(0, start - hitsPerPage);
						break;
					} else if (line.charAt(0) == 'n') {
						if (start + hitsPerPage < numTotalHits) {
							start += hitsPerPage;
						}
						break;
					} else {
						int page = Integer.parseInt(line);
						if ((page - 1) * hitsPerPage < numTotalHits) {
							start = (page - 1) * hitsPerPage;
							break;
						} else {
							System.out.println("No such page");
						}
					}
				}
				if (quit)
					break;
				end = Math.min(numTotalHits, start + hitsPerPage);
			}
		}
	}
}
