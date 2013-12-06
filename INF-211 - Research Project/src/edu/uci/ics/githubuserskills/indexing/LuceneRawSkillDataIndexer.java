package edu.uci.ics.githubuserskills.indexing;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

import edu.uci.ics.githubuserskills.lucene.LuceneDirectoryFactory;
import edu.uci.ics.githubuserskills.lucene.LuceneFSDirectoryFactory;
import edu.uci.ics.githubuserskills.lucene.LuceneUtils;
import edu.uci.ics.githubuserskills.model.RawSkillData;

public class LuceneRawSkillDataIndexer implements RawSkillDataIndexer {

	private Map<String, IndexWriter> indexWriterForUser;
	private Map<String, Directory> openDirectories;

	private LuceneDirectoryFactory directoryFactory;

	private boolean append;

	private Set<String> fixedDictionary;

	public LuceneRawSkillDataIndexer() {
		this(false);
	}

	public LuceneRawSkillDataIndexer(boolean append) {
		this.setDirectoryFactory(new LuceneFSDirectoryFactory());
		this.indexWriterForUser = new HashMap<String, IndexWriter>();
		this.openDirectories = new HashMap<String, Directory>();
		this.append = append;
	}

	public void setFixedDictionary(Set<String> dictionary) {
		this.fixedDictionary = dictionary;
	}

	private boolean hasFixedDirectory() {
		return this.fixedDictionary != null;
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

	private IndexWriterConfig createIndexWriterConfig() throws IOException {
		IndexWriterConfig config = new IndexWriterConfig(LuceneUtils.LUCENE_VERSION, this.createAnalyzer());

		if (this.append) {
			config.setOpenMode(OpenMode.CREATE_OR_APPEND);
		} else {
			config.setOpenMode(OpenMode.CREATE);
		}

		return config;
	}

	private Analyzer createAnalyzer() throws IOException {
		if (this.hasFixedDirectory()) {
			return new DictionaryBasedAnalyzer(LuceneUtils.loadFixedDictionary());
		} else {
			return new StandardAnalyzer(LuceneUtils.LUCENE_VERSION);
		}
	}

	@Override
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
		doc.add(new TextField(LuceneUtils.Globals.CONTENTS_FIELD, new StringReader(rawSkillData.getContents())));

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