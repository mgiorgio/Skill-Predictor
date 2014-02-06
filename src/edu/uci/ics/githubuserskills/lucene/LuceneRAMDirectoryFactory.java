package edu.uci.ics.githubuserskills.lucene;

import java.io.IOException;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

/**
 * @author matias
 *
 */
public class LuceneRAMDirectoryFactory implements LuceneDirectoryFactory {

	public LuceneRAMDirectoryFactory() {
	}

	public Directory openDirectory(String author) throws IOException {
		return new RAMDirectory();
	}

}
