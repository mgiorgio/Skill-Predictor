/**
 * 
 */
package edu.uci.ics.githubuserskills.profile;

import java.util.HashMap;
import java.util.Map;

import edu.uci.ics.githubuserskills.ranking.stats.UserProfileStats;

/**
 * @author matias
 * 
 */
public class UserProfile {

	private Map<String, UserDomainRanking> domainRankings;

	private String user;

	public UserProfile(String user) {
		this.user = user;
		this.domainRankings = new HashMap<String, UserDomainRanking>();
	}

	public void prepareForStats() {
		for (UserDomainRanking eachUserDomainRanking : domainRankings.values()) {
			eachUserDomainRanking.prepareForStats();
		}
	}

	public UserProfileStats getStats() {
		UserProfileStats profileStats = new UserProfileStats();

		for (UserDomainRanking eachRanking : this.domainRankings.values()) {
			profileStats.addDomainStats(eachRanking.getDomainStats());
		}

		return profileStats;
	}

	public void addUserDomainRanking(UserDomainRanking domainRanking) {
		this.domainRankings.put(domainRanking.getDomain(), domainRanking);
	}

	public Map<String, UserDomainRanking> getDomainRankings() {
		return new HashMap<String, UserDomainRanking>(this.domainRankings);
	}

	public UserDomainRanking getDomainRanking(String domain) {
		return this.domainRankings.get(domain);
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

}
