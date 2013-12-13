package edu.uci.ics.githubuserskills.dataAccess;

import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

import edu.uci.ics.githubuserskills.model.db.Comments;
import edu.uci.ics.githubuserskills.model.db.Commit;
import edu.uci.ics.githubuserskills.model.db.PatchedFile;

/**
 * @author shriti Class to seek information from database and return model
 *         objects or their collections
 */
public class ContentDAO {

	private MongoClient getClient() throws UnknownHostException {
		return MongoDBHelper.getConnection(new ServerAddress("localhost", 27017));
	}

	/**
	 * @param helper
	 * @return DBCollection table
	 * @throws UnknownHostException
	 */
	public DBCollection getTable(String tableName) throws UnknownHostException {
		DB database = MongoDBHelper.getDatabase("msr14", this.getClient());
		DBCollection table = MongoDBHelper.getTable(tableName, database);
		return table;

	}

	// given the author get commit data
	// commit data will have commit message, author, html url, raw_url,
	// content_url, patch, filename, stats
	public List<Commit> getCommits(String login) throws UnknownHostException {
		// return a list of commits object, commits made by the author
		List<Commit> commitList = new LinkedList<Commit>();
		// DBCollection commitTable = helper.getTable("commits", database);
		DBCollection commitTable = getTable("commits");

		// TODO Implement projections to reduce required bandwidth.
		BasicDBObject commitSearchQuery = new BasicDBObject("author.login", login);
		BasicDBObject projection = createProjection("commit", "author", "files");
		DBCursor commitCursor = MongoDBHelper.findData(commitSearchQuery, commitTable, projection);

		try {
			while (commitCursor.hasNext()) {
				DBObject commitObject = commitCursor.next();
				Commit commit = new Commit();
				if (commitObject.get("author") != null)
					commit.setAuthor(login);
				else
					continue;
				Object commitMessageDBObject = ((DBObject) commitObject.get("commit")).get("message");
				if (commitMessageDBObject != null)
					commit.setCommit_message(commitMessageDBObject.toString());
				else
					commit.setCommit_message(null);

				BasicDBList fileList = (BasicDBList) commitObject.get("files");

				if (fileList != null) {
					processPatches(commit, fileList);
				}

				Object dateDBObject = ((DBObject) ((DBObject) commitObject.get("commit")).get("author")).get("date");
				if (dateDBObject != null)
					commit.setTime(dateDBObject.toString());
				else
					commit.setTime(null);

				commitList.add(commit);
			}
		} finally {
			commitCursor.close();
		}

		return commitList;
	}

	private BasicDBObject createProjection(String... fields) {
		BasicDBObject projection = new BasicDBObject();
		projection.put("_id", 0);

		for (int i = 0; i < fields.length; i++) {
			projection.put(fields[i], 1);
		}

		return projection;
	}

	private void processPatches(Commit commit, BasicDBList fileList) {
		Collection<PatchedFile> patches = new LinkedList<PatchedFile>();
		for (Iterator iterator = fileList.iterator(); iterator.hasNext();) {
			BasicDBObject fileObject = (BasicDBObject) iterator.next();
			PatchedFile patchedFile = this.processCommitPatch(fileObject);

			patches.add(patchedFile);
		}
		commit.setPatches(patches);
	}

	private PatchedFile processCommitPatch(BasicDBObject fileObject) {
		PatchedFile patchedFile = new PatchedFile();
		patchedFile.setFilename(fileObject.getString("filename"));

		if (fileObject.get("changes") != null) {
			patchedFile.setChanges(Integer.parseInt(fileObject.getString("changes")));
		} else {
			patchedFile.setChanges(0);
		}

		if (fileObject.get("additions") != null) {
			patchedFile.setAdditions(Integer.parseInt(fileObject.getString("additions")));
		} else {
			patchedFile.setAdditions(0);
		}

		if (fileObject.get("deletions") != null) {
			patchedFile.setDeletions(Integer.parseInt(fileObject.getString("deletions")));
		} else {
			patchedFile.setDeletions(0);
		}

		String patchContents = fileObject.getString("patch");
		if (patchContents != null) {
			patchedFile.setPatch(patchContents);
		} else {
			patchedFile.setPatch("");
		}

		return patchedFile;
	}

	/**
	 * @param login
	 * @return List<Comments>
	 * @throws UnknownHostException
	 */
	public List<Comments> getIssueComments(String login) throws UnknownHostException {
		List<Comments> issueCommentList = new LinkedList<Comments>();
		DBCollection issueCommentsTable = getTable("issue_comments");
		BasicDBObject commitSearchQuery = new BasicDBObject("user.login", login);

		BasicDBObject projection = createProjection("user", "body", "created_at");
		DBCursor cursor = MongoDBHelper.findData(commitSearchQuery, issueCommentsTable, projection);

		try {
			while (cursor.hasNext()) {
				Comments comment = new Comments();
				comment.setType("Issue Comment");
				DBObject obj = cursor.next();
				// could directly set it to author. comparison not needed
				if (obj.get("user") != null)
					comment.setAuthor(login);
				else
					continue;

				Object bodyDBObject = obj.get("body");
				if (bodyDBObject != null)
					comment.setComment(bodyDBObject.toString());
				else
					comment.setComment(null);

				Object timeDBObject = obj.get("created_at");
				if (timeDBObject != null)
					comment.setTime(timeDBObject.toString());
				else
					comment.setTime(null);

				issueCommentList.add(comment);
			}
		} finally {
			cursor.close();
		}

		return issueCommentList;
	}

	/**
	 * @param login
	 * @return List<Comments>
	 * @throws UnknownHostException
	 */
	public List<Comments> getPullRequestComments(String login) throws UnknownHostException {
		List<Comments> PullRequestCommentList = new LinkedList<Comments>();
		DBCollection pullRequestCommentsTable = getTable("pull_request_comments");
		BasicDBObject commitSearchQuery = new BasicDBObject("user.login", login);

		BasicDBObject projection = createProjection("user", "body", "created_at");
		DBCursor cursor = MongoDBHelper.findData(commitSearchQuery, pullRequestCommentsTable, projection);

		try {
			while (cursor.hasNext()) {
				Comments comment = new Comments();
				comment.setType("Pull Request Comment");
				DBObject obj = cursor.next();
				// could directly set it to author. comparison not needed
				if (obj.get("user") != null)
					comment.setAuthor(login);
				else
					continue;
				Object bodyDBObject = obj.get("body");
				if (bodyDBObject != null)
					comment.setComment(bodyDBObject.toString());
				else
					comment.setComment(null);
				Object timeDBObject = obj.get("created_at");
				if (timeDBObject != null)
					comment.setTime(timeDBObject.toString());
				else
					comment.setTime(null);

				PullRequestCommentList.add(comment);
			}
		} finally {
			cursor.close();
		}

		return PullRequestCommentList;
	}

	/**
	 * @param login
	 * @return List<Comments>
	 * @throws UnknownHostException
	 */
	public List<Comments> getCommitComments(String login) throws UnknownHostException {
		List<Comments> CommitCommentList = new LinkedList<Comments>();
		DBCollection commitCommentsTable = getTable("commit_comments");
		// authorDBObject.put("site_admin", false);
		// BasicDBObject commitSearchQuery = new BasicDBObject("user",
		// authorDBObject);
		BasicDBObject commitSearchQuery = new BasicDBObject("user", login);

		BasicDBObject projection = createProjection("user", "body", "created_at");
		DBCursor cursor = MongoDBHelper.findData(commitSearchQuery, commitCommentsTable, projection);

		try {
			while (cursor.hasNext()) {
				Comments comment = new Comments();
				comment.setType("Commit Comment");
				DBObject obj = cursor.next();
				// could directly set it to author. comparison not needed
				if (obj.get("user") != null)
					comment.setAuthor(login);
				else
					continue;
				Object bodyDBObject = obj.get("body");
				if (bodyDBObject != null)
					comment.setComment(bodyDBObject.toString());
				else
					comment.setComment(null);
				Object timeDBObject = obj.get("created_at");
				if (timeDBObject != null)
					comment.setTime(timeDBObject.toString());
				else
					comment.setTime(null);

				CommitCommentList.add(comment);
			}
		} finally {
			cursor.close();
		}

		return CommitCommentList;
	}
}