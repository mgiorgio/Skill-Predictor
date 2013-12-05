package edu.uci.ics.githubuserskills.model;

public enum SkillDataType {

	COMMIT_PATCH("Commit patch"), COMMIT_MESSAGE("Commit message"), COMMIT_COMMENT("Commit comment"), PULL_REQUEST_COMMENT("Pull Request comment"), ISSUE_COMMENT("Issue comment");

	private String name;

	private SkillDataType(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

}
