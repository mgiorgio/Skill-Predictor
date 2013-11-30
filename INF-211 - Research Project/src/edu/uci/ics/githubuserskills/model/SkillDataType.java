package edu.uci.ics.githubuserskills.model;

public enum SkillDataType {

	COMMIT("Commit"), COMMIT_MESSAGE("Commit message"), PULL_REQUEST_MESSAGE("Pull Request message"), ISSUE_MESSAGE("Issue message");

	private String name;

	private SkillDataType(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

}
