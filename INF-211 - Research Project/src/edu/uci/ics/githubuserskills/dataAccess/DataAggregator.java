package edu.uci.ics.githubuserskills.dataAccess;

import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.BasicDBList;

import edu.uci.ics.githubuserskills.model.RawSkillData;
import edu.uci.ics.githubuserskills.model.SkillDataType;
import edu.uci.ics.githubuserskills.model.db.Comments;
import edu.uci.ics.githubuserskills.model.db.Commit;
import edu.uci.ics.githubuserskills.model.db.PatchedFile;

/**
 * @author shriti Class to bring together all data access objects to produce
 *         basic unit of information for Lucene as <RawSkillData>.
 * 
 */
public class DataAggregator {

	private static final Logger log = LoggerFactory.getLogger(DataAggregator.class);

	/**
	 * @param login
	 * @return ArrayList<RawSkillData>
	 * @throws UnknownHostException
	 */
	public List<RawSkillData> getAuthorData(String login) throws UnknownHostException {
		log.info("About to query DB for [{}]...", login);
		List<RawSkillData> rawDataList = new LinkedList<RawSkillData>();

		ContentDAO contentDAO = new ContentDAO();

		// get commit data
		log.debug("Querying commits for [{}]...", login);
		ArrayList<Commit> authorCommits = (ArrayList<Commit>) contentDAO.getCommits(login);
		if (authorCommits.size() != 0) {
			log.debug("About to process [{}] commits for [{}]...", authorCommits.size(), login);
			Iterator<Commit> it = authorCommits.iterator();
			while (it.hasNext()) {
				Commit commit = it.next();

				long commitTime = Timestamp.valueOf(commit.getTime().substring(0, 10) + " 00:00:00").getTime();

				// Process commit message.
				RawSkillData commitMessageData = new RawSkillData();
				commitMessageData.setAuthor(login);
				commitMessageData.setType(SkillDataType.COMMIT_MESSAGE);
				commitMessageData.setTimestamp(commitTime);
				commitMessageData.setContents(commit.getCommit_message());

				rawDataList.add(commitMessageData);

				// Process patches.
				for (PatchedFile eachPatchedFile : commit.getPatches()) {

					RawSkillData commitPatchData = new RawSkillData();
					commitPatchData.setAuthor(login);
					commitPatchData.setType(SkillDataType.COMMIT_PATCH);
					commitPatchData.setTimestamp(commitTime);
					commitPatchData.setContents(eachPatchedFile.getPatch());

					rawDataList.add(commitPatchData);
				}
			}
		} else {
			log.debug("No commits were found for [{}]", login);
		}
		log.debug("Commits for [{}] have been queried.", login);

		// get issue comments
		log.debug("Querying issue comments for [{}]...", login);
		ArrayList<Comments> authorIssueComments = (ArrayList<Comments>) contentDAO.getIssueComments(login);
		if (authorIssueComments.size() != 0) {
			log.debug("About to process [{}] issue comments for [{}]...", authorIssueComments.size(), login);
			Iterator<Comments> it = authorIssueComments.iterator();
			while (it.hasNext()) {
				Comments comment = it.next();
				RawSkillData authorComment = new RawSkillData();
				authorComment.setAuthor(login);
				authorComment.setType(SkillDataType.ISSUE_COMMENT);
				authorComment.setTimestamp(Timestamp.valueOf(comment.getTime().substring(0, 10) + " 00:00:00").getTime());
				authorComment.setContents(comment.getComment());
				rawDataList.add(authorComment);
			}
		} else {
			log.debug("No issue comments were found for [{}]", login);
		}
		log.debug("Issue comments for [{}] have been queried.", login);

		// get pull request comments
		log.debug("Querying pull requests for [{}]...", login);
		ArrayList<Comments> authorPullRequestComments = (ArrayList<Comments>) contentDAO.getPullRequestComments(login);
		if (authorPullRequestComments.size() != 0) {
			log.debug("About to process [{}] pull request comments for [{}]...", authorPullRequestComments.size(), login);
			Iterator<Comments> it = authorPullRequestComments.iterator();
			while (it.hasNext()) {
				Comments comment = it.next();
				RawSkillData authorPullRequestComment = new RawSkillData();
				authorPullRequestComment.setAuthor(login);
				authorPullRequestComment.setType(SkillDataType.PULL_REQUEST_COMMENT);
				authorPullRequestComment.setTimestamp(Timestamp.valueOf(comment.getTime().substring(0, 10) + " 00:00:00").getTime());
				authorPullRequestComment.setContents(comment.getComment());
				rawDataList.add(authorPullRequestComment);
			}
		} else {
			log.debug("No pull requests were found for [{}]", login);
		}
		log.debug("Pull requests for [{}] have been queried.", login);

		// get commit comments
		log.debug("Querying commit comments for [{}]...", login);
		ArrayList<Comments> authorCommitComments = (ArrayList<Comments>) contentDAO.getCommitComments(login);
		if (authorCommitComments.size() != 0) {
			log.debug("About to process [{}] commit comments for [{}]...", authorCommitComments.size(), login);
			Iterator<Comments> it = authorCommitComments.iterator();
			while (it.hasNext()) {
				Comments comment = it.next();
				RawSkillData authorCommitComment = new RawSkillData();
				authorCommitComment.setAuthor(login);
				authorCommitComment.setType(SkillDataType.COMMIT_COMMENT);
				authorCommitComment.setTimestamp(Timestamp.valueOf(comment.getTime().substring(0, 10) + " 00:00:00").getTime());
				authorCommitComment.setContents(comment.getComment());
				rawDataList.add(authorCommitComment);
			}
		} else {
			log.debug("No commit comments were found for [{}]", login);
		}

		log.debug("Commit comments for [{}] have been queried.", login);

		log.info("DB query for [{}] finished.", login);
		return rawDataList;

	}

	/**
	 * @param login
	 * @return HashMap<String, ArrayList<RawSkillData>>
	 * @throws UnknownHostException
	 */
	public List<RawSkillData> getAllLogins() throws UnknownHostException {
		List<RawSkillData> rawDataList = new LinkedList<RawSkillData>();
		AuthorAndUserDAO dao = new AuthorAndUserDAO();

		log.debug("Querying all authors...");
		BasicDBList logins = dao.getAllLogins();
		Iterator<Object> it = logins.iterator();
		while (it.hasNext()) {
			Object s = it.next();
			rawDataList.addAll(getAuthorData(s.toString()));
		}
		log.debug("All authors ({}) have been retrieved.", rawDataList.size());

		return rawDataList;
	}
}
