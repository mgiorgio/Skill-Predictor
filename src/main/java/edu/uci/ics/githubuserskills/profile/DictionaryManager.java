/**
 * 
 */
package edu.uci.ics.githubuserskills.profile;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.githubuserskills.config.ConfigurationUtils;
import edu.uci.ics.githubuserskills.model.SkillProfile;

/**
 * @author matias
 * 
 */
public class DictionaryManager {

	private static final Logger console = LoggerFactory.getLogger("console");

	private static Map<String, SkillTermsDictionary> cachedDictionaries;

	private static boolean allCached;

	static {
		cachedDictionaries = new HashMap<String, SkillTermsDictionary>();
		allCached = false;
	}

	private static void loadDictionary(String name, String file) throws IOException {
		if (!cachedDictionaries.containsKey(name)) {
			console.info("Reading {} dictionary from {}...", name, file);
			List<String> skillDictionary = FileUtils.readLines(new File(file));
			cachedDictionaries.put(name, new SkillTermsDictionary(name, new HashSet<String>(skillDictionary)));
			console.info("Done.");
		}
	}

	private static SkillTermsDictionary getDictionary(String name, String file) throws IOException {
		loadDictionary(name, file);
		return cachedDictionaries.get(name);
	}

	private static String getDictionaryPath(String name) {
		List<SkillProfile> profiles = ConfigurationUtils.getConfiguredProfiles();

		for (SkillProfile skillProfile : profiles) {
			if (skillProfile.getName().equals(name)) {
				return skillProfile.getDictionaryFile();
			}
		}

		return null;
	}

	public static Collection<SkillTermsDictionary> getDictionaries() throws IOException {
		if (!allCached) {

			Collection<SkillTermsDictionary> dictionaries = new LinkedList<SkillTermsDictionary>();
			List<SkillProfile> configuredProfiles = ConfigurationUtils.getConfiguredProfiles();

			for (SkillProfile skillProfile : configuredProfiles) {
				SkillTermsDictionary dictionary = getDictionary(skillProfile.getName(), skillProfile.getDictionaryFile());

				dictionaries.add(dictionary);
			}

			allCached = true;

			return dictionaries;
		} else {
			return cachedDictionaries.values();
		}
	}

	public static SkillTermsDictionary loadTokenizationDictionary() throws IOException {
		console.info("Loading tokenization dictionary...");
		Set<String> allWords = new HashSet<String>();

		Collection<SkillTermsDictionary> dictionaries = getDictionaries();

		for (SkillTermsDictionary eachDictionary : dictionaries) {
			allWords.addAll(eachDictionary.getDictionary());
		}

		console.info("Tokenization dictionary has been successfully loaded.");
		return new SkillTermsDictionary("All", allWords);
	}
}
