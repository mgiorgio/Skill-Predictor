package edu.uci.ics.githubuserskills.indexing;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import edu.uci.ics.githubuserskills.model.RawSkillData;

public class RawSkillDataIndexer {

	private String uniqueExecutionToken;

	private Map<String, IndexWriter> indexWriterForUser;

	public RawSkillDataIndexer() {
		this.indexWriterForUser = new HashMap<String, IndexWriter>();
	}

	public void initialize() {
		this.initializeUniqueExecutionToken();
	}

	private void initializeUniqueExecutionToken() {
		this.setUniqueExecutionToken(String.valueOf(System.currentTimeMillis()));
	}

	protected IndexWriter createIndexWriterForUser(String user) throws IOException {
		Directory createDirectoryForUser = this.createDirectoryForUser(user);
		return new IndexWriter(createDirectoryForUser, this.createIndexWriterConfig());
	}

	protected Directory createDirectoryForUser(String user) throws IOException {
		return FSDirectory.open(this.getFileDirectoryForUser(user));
	}

	private File getFileDirectoryForUser(String user) {
		return new File("data" + File.separator + this.getUniqueExecutionToken() + File.separator + user);
	}

	private String getUniqueExecutionToken() {
		return uniqueExecutionToken;
	}

	private void setUniqueExecutionToken(String uniqueExecutionToken) {
		this.uniqueExecutionToken = uniqueExecutionToken;
	}

	private IndexWriterConfig createIndexWriterConfig() {
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_46, this.createAnalyzer());

		config.setOpenMode(OpenMode.CREATE);

		return config;
	}

	private Analyzer createAnalyzer() {
		return new StandardAnalyzer(Version.LUCENE_46);
	}

	public void index(List<RawSkillData> rawSkillDataObjects) throws IndexingException {
		for (RawSkillData rawSkillData : rawSkillDataObjects) {
			this.index(rawSkillData);
		}
	}

	protected void index(RawSkillData rawSkillData) throws IndexingException {
		Document doc = this.createDocument(rawSkillData);

		try {
			this.getIndexWriterForUser(rawSkillData.getAuthor()).addDocument(doc);
		} catch (IOException e) {
			throw new IndexingException(e);
		}
	}

	protected IndexWriter getIndexWriterForUser(String author) throws IOException {
		IndexWriter indexWriter = this.indexWriterForUser.get(author);
		if (indexWriter == null) {
			indexWriter = this.createIndexWriterForUser(author);
			this.indexWriterForUser.put(author, indexWriter);
		}
		return indexWriter;
	}

	private Document createDocument(RawSkillData rawSkillData) {
		Document doc = new Document();

		return doc;
	}

}