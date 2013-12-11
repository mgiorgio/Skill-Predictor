package edu.uci.ics.githubuserskills.dataAccess;

import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.GregorianCalendar;
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

	private Calendar cachedCalendar = GregorianCalendar.getInstance();

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
		List<Commit> authorCommits = (List<Commit>) contentDAO.getCommits(login);
		if (!authorCommits.isEmpty()) {
			log.debug("About to process [{}] commits for [{}]...", authorCommits.size(), login);
			Iterator<Commit> it = authorCommits.iterator();
			while (it.hasNext()) {
				Commit commit = it.next();

				long commitTime = parseTime(commit.getTime());

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
		List<Comments> authorIssueComments = (List<Comments>) contentDAO.getIssueComments(login);
		if (authorIssueComments.size() != 0) {
			log.debug("About to process [{}] issue comments for [{}]...", authorIssueComments.size(), login);
			Iterator<Comments> it = authorIssueComments.iterator();
			while (it.hasNext()) {
				Comments comment = it.next();
				RawSkillData authorComment = new RawSkillData();
				authorComment.setAuthor(login);
				authorComment.setType(SkillDataType.ISSUE_COMMENT);
				authorComment.setTimestamp(this.parseTime(comment.getTime()));
				authorComment.setContents(comment.getComment());
				rawDataList.add(authorComment);
			}
		} else {
			log.debug("No issue comments were found for [{}]", login);
		}
		log.debug("Issue comments for [{}] have been queried.", login);

		// get pull request comments
		log.debug("Querying pull requests for [{}]...", login);
		List<Comments> authorPullRequestComments = (List<Comments>) contentDAO.getPullRequestComments(login);
		if (authorPullRequestComments.size() != 0) {
			log.debug("About to process [{}] pull request comments for [{}]...", authorPullRequestComments.size(), login);
			Iterator<Comments> it = authorPullRequestComments.iterator();
			while (it.hasNext()) {
				Comments comment = it.next();
				RawSkillData authorPullRequestComment = new RawSkillData();
				authorPullRequestComment.setAuthor(login);
				authorPullRequestComment.setType(SkillDataType.PULL_REQUEST_COMMENT);
				authorPullRequestComment.setTimestamp(this.parseTime(comment.getTime()));
				authorPullRequestComment.setContents(comment.getComment());
				rawDataList.add(authorPullRequestComment);
			}
		} else {
			log.debug("No pull requests were found for [{}]", login);
		}
		log.debug("Pull requests for [{}] have been queried.", login);

		// get commit comments
		log.debug("Querying commit comments for [{}]...", login);
		List<Comments> authorCommitComments = (List<Comments>) contentDAO.getCommitComments(login);
		if (authorCommitComments.size() != 0) {
			log.debug("About to process [{}] commit comments for [{}]...", authorCommitComments.size(), login);
			Iterator<Comments> it = authorCommitComments.iterator();
			while (it.hasNext()) {
				Comments comment = it.next();
				RawSkillData authorCommitComment = new RawSkillData();
				authorCommitComment.setAuthor(login);
				authorCommitComment.setType(SkillDataType.COMMIT_COMMENT);
				authorCommitComment.setTimestamp(this.parseTime(comment.getTime()));
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

	private long parseTime(String time) {
		cachedCalendar.set(Integer.parseInt(time.substring(0, 4)), Integer.parseInt(time.substring(5, 7)) - 1, Integer.parseInt(time.substring(8, 10)));
		cachedCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time.substring(11, 13)));
		cachedCalendar.set(Calendar.MINUTE, Integer.parseInt(time.substring(14, 16)));
		cachedCalendar.set(Calendar.SECOND, Integer.parseInt(time.substring(17, 19)));
		return cachedCalendar.getTimeInMillis();
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
