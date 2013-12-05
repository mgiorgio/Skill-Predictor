package edu.uci.ics.githubuserskills.lucene;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class LuceneFSDirectoryFactory implements LuceneDirectoryFactory {

	public LuceneFSDirectoryFactory() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Directory openDirectory(String author) throws IOException {
		return FSDirectory.open(new File(LuceneUtils.getFileDirectoryForUser(author)));
	}

}
