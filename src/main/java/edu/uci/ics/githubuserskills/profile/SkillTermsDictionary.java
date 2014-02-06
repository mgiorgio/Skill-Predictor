package edu.uci.ics.githubuserskills.profile;

import java.util.Set;

/**
 * @author matias
 *
 */
public class SkillTermsDictionary {

	private String name;

	private Set<String> dictionary;

	public SkillTermsDictionary(String name, Set<String> dictionary) {
		this.name = name;
		this.dictionary = dictionary;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<String> getDictionary() {
		return dictionary;
	}

	public void setDictionary(Set<String> dictionary) {
		this.dictionary = dictionary;
	}
}
