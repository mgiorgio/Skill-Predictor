package edu.uci.ics.githubuserskills.ranking.stats;

import java.util.HashMap;
import java.util.Map;

public class UserProfileStats {

	private Map<String, UserDomainStats> domainStats;

	public UserProfileStats() {
		this.domainStats = new HashMap<String, UserDomainStats>();
	}

	public UserDomainStats getDomainIndex(String domain) {
		return domainStats.get(domain);
	}

	public void addDomainStats(UserDomainStats domainStats) {
		this.domainStats.put(domainStats.getDomain(), domainStats);
	}

	public Map<String, UserDomainStats> getDomainStats() {
		return new HashMap<String, UserDomainStats>(this.domainStats);
	}

}
