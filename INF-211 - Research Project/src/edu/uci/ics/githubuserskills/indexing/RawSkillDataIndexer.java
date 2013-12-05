package edu.uci.ics.githubuserskills.indexing;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;

import edu.uci.ics.githubuserskills.lucene.LuceneDirectoryFactory;
import edu.uci.ics.githubuserskills.lucene.LuceneFSDirectoryFactory;
import edu.uci.ics.githubuserskills.lucene.LuceneUtils;
import edu.uci.ics.githubuserskills.model.RawSkillData;

public class RawSkillDataIndexer {

	private Map<String, IndexWriter> indexWriterForUser;
	private Map<String, Directory> openDirectories;

	private LuceneDirectoryFactory directoryFactory;

	private boolean append;

	public RawSkillDataIndexer() {
		this(false);
	}

	public RawSkillDataIndexer(boolean append) {
		this.setDirectoryFactory(new LuceneFSDirectoryFactory());
		this.indexWriterForUser = new HashMap<String, IndexWriter>();
		this.openDirectories = new HashMap<String, Directory>();
		this.append = append;
	}

	private LuceneDirectoryFactory getDirectoryFactory() {
		return directoryFactory;
	}

	public void setDirectoryFactory(LuceneDirectoryFactory directoryFactory) {
		this.directoryFactory = directoryFactory;
	}

	public void initialize() {
	}

	protected IndexWriter createIndexWriterForUser(String user) throws IOException {
		Directory directory = this.openDirectoryForUser(user);
		openDirectories.put(user, directory);
		return new IndexWriter(directory, this.createIndexWriterConfig());
	}

	private Directory openDirectoryForUser(String user) throws IOException {
		return this.getDirectoryFactory().openDirectory(user);
	}

	private IndexWriterConfig createIndexWriterConfig() {
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_46, this.createAnalyzer());

		if (this.append) {
			config.setOpenMode(OpenMode.CREATE_OR_APPEND);
		} else {
			config.setOpenMode(OpenMode.CREATE);
		}

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

		doc.add(new StringField(LuceneUtils.Globals.AUTHOR_FIELD, rawSkillData.getAuthor(), Store.YES));
		doc.add(new StringField(LuceneUtils.Globals.TYPE_FIELD, rawSkillData.getType().getName(), Store.NO));
		doc.add(new LongField(LuceneUtils.Globals.TIME_FIELD, rawSkillData.getTimestamp(), Store.YES));
		doc.add(new TextField(LuceneUtils.Globals.CONTENTS_FIELD, rawSkillData.getContents()));

		return doc;
	}

	public void close() throws IndexingException {
		try {
			for (Directory dir : this.openDirectories.values()) {
				dir.close();
			}
		} catch (IOException e) {
			throw new IndexingException(e);
		}
	}
}