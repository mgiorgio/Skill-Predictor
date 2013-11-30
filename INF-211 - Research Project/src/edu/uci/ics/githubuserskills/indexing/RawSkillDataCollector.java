package edu.uci.ics.githubuserskills.indexing;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import edu.uci.ics.githubuserskills.model.RawSkillData;

public class RawSkillDataCollector {

	private IndexWriter indexWriter;

	public RawSkillDataCollector() {
	}

	public void initialize() throws IndexingException {
		try {
			this.setIndexWriter(new IndexWriter(this.createDirectory(), this.createIndexWriterConfig()));
		} catch (IOException e) {
			throw new IndexingException(e);
		}
	}

	protected Directory createDirectory() {
		// TODO Change to Disk.
		return new RAMDirectory();
	}

	private IndexWriterConfig createIndexWriterConfig() {
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_46, this.createAnalyzer());

		config.setOpenMode(OpenMode.CREATE);

		return config;
	}

	private Analyzer createAnalyzer() {
		return new StandardAnalyzer(Version.LUCENE_46);
	}

	protected IndexWriter getIndexWriter() {
		return indexWriter;
	}

	protected void setIndexWriter(IndexWriter indexWriter) {
		this.indexWriter = indexWriter;
	}

	public void index(RawSkillData rawSkillData) throws IndexingException {
		Document doc = this.createDocument(rawSkillData);

		try {
			this.getIndexWriter().addDocument(doc);
		} catch (IOException e) {
			throw new IndexingException(e);
		}
	}

	private Document createDocument(RawSkillData rawSkillData) {
		Document doc = new Document();

		return doc;
	}

}