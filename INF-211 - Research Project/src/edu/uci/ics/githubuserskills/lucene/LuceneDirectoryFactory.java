package edu.uci.ics.githubuserskills.lucene;

import java.io.IOException;

import org.apache.lucene.store.Directory;

public interface LuceneDirectoryFactory {

	public Directory openDirectory(String author) throws IOException;

}
