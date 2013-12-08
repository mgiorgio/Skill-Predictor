package edu.uci.ics.githubuserskills.dataAccess;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.githubuserskills.lucene.LuceneUtils;


public class TagDictionaryParser {

	public final List<String> tagList = new ArrayList<String>();
	public final List<String> phraseList = new ArrayList<String>();
	public final String relativePathToDictionary = "/data/fixeddictionary.txt";
	
	private static final Logger log = LoggerFactory.getLogger(TagDictionaryParser.class);
	
	public int populateTagsAndPhrases() throws FileNotFoundException{
		int numberOfLines=0;
		log.info("populating phrase list and tag list");
		log.info("Resource Path: " + getClass().getResource("../").getPath());
		String filePath=null;
		//filePath = new File(".").getCanonicalPath() + relativePathToDictionary;
		filePath = LuceneUtils.getFixedDictionaryPath();
		File f = new File(filePath);
		if(!f.exists())
		{
			log.error("Dictionary file does not exist. Returning without parsing.");
			return numberOfLines;
		}
		Scanner scanner = new Scanner(f);
		if(!scanner.hasNext())
		{
			log.error("Dictionary file may be empty. Returning without parsing.");
			return(numberOfLines);
		}
		while(scanner.hasNextLine())
		{
			String content = scanner.nextLine();
			if(content.contains("-"))
			{
				String modifiedContent = content.replace("-", " ");
				phraseList.add(modifiedContent);
			}
			else
				tagList.add(content);
			numberOfLines++;
		}
		log.info("Completed parsing. Total tags parsed are " + numberOfLines);
		return numberOfLines;
	}
	
	/*public static void main(String args[])
	{
		try {
			TagDictionaryParser dp = new TagDictionaryParser();
			int num = dp.populateTagsAndPhrases();
			System.out.println(num);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	
	
}
