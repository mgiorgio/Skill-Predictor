package edu.uci.ics.githubuserskills.lucene;

import java.io.IOException;

import org.apache.lucene.store.Directory;

/**
 * @author matias
 *
 */
public interface LuceneDirectoryFactory {

	public Directory openDirectory(String author) throws IOException;

}
