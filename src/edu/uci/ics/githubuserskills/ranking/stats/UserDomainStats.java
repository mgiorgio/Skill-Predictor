package edu.uci.ics.githubuserskills.ranking.stats;

/**
 * @author matias
 * 
 */
public class UserDomainStats {

	/**
	 * The domain name.
	 */
	private String domain;

	/**
	 * Profile metric for intra-domain analysis.
	 */
	private double score;

	/**
	 * Metric for inter-domain analysis. It reflects the weight (w) of the
	 * profile (p) associated to this domain (d) for a user (u).
	 */
	private double profileWeight;

	public UserDomainStats(String domain) {
		this.domain = domain;
	}

	public double getProfileWeight() {
		return profileWeight;
	}

	public void setProfileWeight(double profileWeight) {
		this.profileWeight = profileWeight;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}
}