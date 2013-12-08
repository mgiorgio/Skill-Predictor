package edu.uci.ics.githubuserskills.model;

public class SkillProfile {

	private String name;

	private String dictionaryFile;

	public SkillProfile(String name, String dictionaryFile) {
		this.name = name;
		this.dictionaryFile = dictionaryFile;
	}

	public String getName() {
		return this.name;
	}

	public String getDictionaryFile() {
		return this.dictionaryFile;
	}
}
