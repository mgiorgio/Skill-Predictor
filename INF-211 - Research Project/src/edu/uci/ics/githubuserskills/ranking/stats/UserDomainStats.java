package edu.uci.ics.githubuserskills.ranking.stats;

public class UserDomainStats {

	private String domain;

	private double expertise;

	public UserDomainStats(String domain) {
		this.domain = domain;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public double getExpertise() {
		return expertise;
	}

	public void setExpertise(double expertise) {
		this.expertise = expertise;
	}

}
