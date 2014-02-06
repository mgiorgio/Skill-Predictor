/**
 * 
 */
package edu.uci.ics.githubuserskills.profile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.uci.ics.githubuserskills.controller.UserRankingCreationException;
import edu.uci.ics.githubuserskills.model.RawSkillData;

/**
 * @author matias
 * 
 */
public class UserRankingCollector extends UserProfileCreatorDecorator {

	private Map<String, UserProfile> userProfiles;

	public UserRankingCollector(RawSkillDataProcessor innerCreator) {
		super(innerCreator);
		this.userProfiles = new HashMap<String, UserProfile>();
	}

	@Override
	public UserProfile rank(String author, List<RawSkillData> rawSkillDataObjects) throws UserRankingCreationException {
		UserProfile profile = super.rank(author, rawSkillDataObjects);

		this.addProfile(profile);

		return profile;
	}

	private void addProfile(UserProfile profile) {
		this.userProfiles.put(profile.getUser(), profile);
	}

	public Map<String, UserProfile> getUserProfiles() {
		return new HashMap<String, UserProfile>(this.userProfiles);
	}

}
