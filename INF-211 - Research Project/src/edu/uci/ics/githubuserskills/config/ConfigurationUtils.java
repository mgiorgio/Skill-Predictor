package edu.uci.ics.githubuserskills.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;

import edu.uci.ics.githubuserskills.lucene.Utils;
import edu.uci.ics.githubuserskills.model.SkillProfile;

public class ConfigurationUtils {

	private static XMLConfiguration config;

	private static XMLConfiguration getConfig() {
		if (config == null) {

			try {
				config = new XMLConfiguration(ConfigurationUtils.getConfigurationPath());

			} catch (ConfigurationException e) {
				throw new RuntimeException(e.getMessage());
			}
		}
		return config;
	}

	public static List<SkillProfile> getConfiguredProfiles() {
		List<SkillProfile> profiles = new ArrayList<SkillProfile>();

		List<HierarchicalConfiguration> profilesConfig = getConfig().configurationsAt("profiles.profile");

		for (HierarchicalConfiguration eachProfileConfig : profilesConfig) {

			String name = eachProfileConfig.getString("name");
			String dictionaryFile = eachProfileConfig.getString("dictionary");

			SkillProfile profile = new SkillProfile(name, dictionaryFile);

			profiles.add(profile);
		}

		return profiles;
	}

	public static String getConfigurationPath() {
		return Utils.CONFIG + File.separator + Utils.CONFIG_FILENAME;
	}
}
