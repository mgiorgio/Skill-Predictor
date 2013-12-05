package edu.uci.ics.githubuserskills.dataAccess;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import edu.uci.ics.githubuserskills.model.Author;
import edu.uci.ics.githubuserskills.model.Comments;
import edu.uci.ics.githubuserskills.model.Commit;



/**
 * @author shriti
 * Class to seek information from database and return model objects or their collections
 */
public class ContentDAO{

	/**
	 * @param helper
	 * @return DBCollection table
	 * @throws UnknownHostException
	 */
	public DBCollection getTable(MongoDBHelper helper, String tableName) throws UnknownHostException
	{

		MongoClient DbClient = helper.getConnection("localhost", 27017);
		DB database = helper.getDatabase("msr14", DbClient);
		DBCollection table = helper.getTable(tableName, database);
		return table;

	}


	//given the author get commit data
	//commit data will have commit message, author, html url, raw_url, content_url, patch, filename, stats
	public List<Commit> getCommits(String login) throws UnknownHostException
	{
		//return a list of commits object, commits made by the author
		List<Commit> commitList = new ArrayList<Commit>();
		//DBCollection commitTable = helper.getTable("commits", database);
		MongoDBHelper helper = new MongoDBHelper();
		DBCollection commitTable = getTable(helper, "commits");
		AuthorAndUserDAO dao = new AuthorAndUserDAO();
		DBObject authorDBObject = dao.getAuthorDBObject(login);
		Author author = dao.getAuthorbyLogin(login);

		BasicDBObject commitSearchQuery = new BasicDBObject("author", authorDBObject);
		DBCursor commitCursor = helper.findData(commitSearchQuery, commitTable, null);

		while(commitCursor.hasNext())
		{
			DBObject commitObject = commitCursor.next();
			Commit c =new Commit();
			if(commitObject.get("author")!=null)
				c.setAuthor(author);
			else
				continue;
			if(((DBObject) commitObject.get("commit")).get("message")!=null)
				c.setCommit_message(((DBObject) commitObject.get("commit")).get("message").toString());
			else
				c.setCommit_message(null);
			if(commitObject.get("html_url")!=null)
				c.setHtml_url(commitObject.get("html_url").toString());
			else
				c.setHtml_url(null);
			if(commitObject.get("comments_url")!=null)
				c.setComments_url(commitObject.get("comments_url").toString());
			else
				c.setComments_url(null);

			BasicDBList fileList = (BasicDBList) commitObject.get("files");
			BasicDBObject fileObject = (BasicDBObject) fileList.get(0);
			if(fileObject.get("filename")!=null)
				c.setFilename(fileObject.getString("filename"));
			else
				c.setFilename(null);
			if(fileObject.get("changes")!=null)
				c.setChanges(Integer.parseInt(fileObject.getString("changes")));
			else
				c.setChanges(0);
			if(fileObject.get("additions")!=null)
				c.setAdditions(Integer.parseInt(fileObject.getString("additions")));
			else
				c.setAdditions(0);
			if(fileObject.get("deletions")!=null)
				c.setDeletions(Integer.parseInt(fileObject.getString("deletions")));
			else
				c.setDeletions(0);
			if(fileObject.get("raw_url")!=null)
				c.setRaw_url(fileObject.getString("raw_url"));
			else
				c.setRaw_url(null);
			if(fileObject.get("contents_url")!=null)
				c.setContents_url(fileObject.getString("contents_url"));
			else
				c.setContents_url(null);
			if(fileObject.get("patch")!=null)
				c.setPatch(fileObject.getString("patch"));
			else
				c.setPatch(null);

			commitList.add(c);	
		}

		return commitList;		
	}

	/**
	 * @param login
	 * @return List<Comments>
	 * @throws UnknownHostException
	 */
	public List<Comments> getIssueComments(String login) throws UnknownHostException
	{
		MongoDBHelper helper = new MongoDBHelper();


		List<Comments> issueCommentList = new ArrayList<Comments>();
		DBCollection issueCommentsTable = getTable(helper, "issue_comments");
		AuthorAndUserDAO dao = new AuthorAndUserDAO();
		DBObject authorDBObject = dao.getAuthorDBObject(login);
		Author author = dao.getAuthorbyLogin(login);
		BasicDBObject commitSearchQuery = new BasicDBObject("user", authorDBObject);
		DBCursor cursor = helper.findData(commitSearchQuery, issueCommentsTable, null);

		while(cursor.hasNext())
		{
			Comments comment = new Comments();
			comment.setType("Issue Comment");
			DBObject obj = cursor.next();
			//could directly set it to author. comparison not needed
			if(obj.get("user")!=null)
				comment.setAuthor(author);
			else
				continue;
			if(obj.get("body")!=null)
				comment.setComment(obj.get("body").toString());
			else
				comment.setComment(null);
			if(obj.get("created_at")!=null)
				comment.setTime(obj.get("created_at").toString());
			else
				comment.setTime(null);

			issueCommentList.add(comment);			
		}

		return issueCommentList;
	}

	/**
	 * @param login
	 * @return List<Comments>
	 * @throws UnknownHostException
	 */
	public List<Comments> getPullRequestComments(String login) throws UnknownHostException
	{
		MongoDBHelper helper = new MongoDBHelper();

		List<Comments> PullRequestCommentList = new ArrayList<Comments>();
		DBCollection pullRequestCommentsTable = getTable(helper, "pull_request_comments");
		AuthorAndUserDAO dao = new AuthorAndUserDAO();
		DBObject authorDBObject = dao.getAuthorDBObject(login);
		Author author = dao.getAuthorbyLogin(login);
		BasicDBObject commitSearchQuery = new BasicDBObject("user", authorDBObject);
		DBCursor cursor = helper.findData(commitSearchQuery, pullRequestCommentsTable, null);

		while(cursor.hasNext())
		{
			Comments comment = new Comments();
			comment.setType("Pull Request Comment");
			DBObject obj = cursor.next();
			//could directly set it to author. comparison not needed
			if(obj.get("user")!=null)
				comment.setAuthor(author);
			else
				continue;
			if(obj.get("body")!=null)
				comment.setComment(obj.get("body").toString());
			else
				comment.setComment(null);
			if(obj.get("created_at")!=null)
				comment.setTime(obj.get("created_at").toString());
			else
				comment.setTime(null);

			PullRequestCommentList.add(comment);			
		}

		return PullRequestCommentList;
	}

	/**
	 * @param login
	 * @return List<Comments>
	 * @throws UnknownHostException
	 */
	public List<Comments> getCommitComments(String login) throws UnknownHostException
	{
		MongoDBHelper helper = new MongoDBHelper();

		List<Comments> CommitCommentList = new ArrayList<Comments>();
		DBCollection commitCommentsTable = getTable(helper, "commit_comments");
		AuthorAndUserDAO dao = new AuthorAndUserDAO();
		DBObject authorDBObject = dao.getAuthorDBObject(login);
		authorDBObject.put("site_admin",false);
		Author author = dao.getAuthorbyLogin(login);
		BasicDBObject commitSearchQuery = new BasicDBObject("user", authorDBObject);
		DBCursor cursor = helper.findData(commitSearchQuery, commitCommentsTable, null);

		while(cursor.hasNext())
		{
			Comments comment = new Comments();
			comment.setType("Commit Comment");
			DBObject obj = cursor.next();
			//could directly set it to author. comparison not needed
			if(obj.get("user")!=null)
				comment.setAuthor(author);
			else
				continue;
			if(obj.get("body")!=null)
				comment.setComment(obj.get("body").toString());
			else
				comment.setComment(null);
			if(obj.get("created_at")!=null)
				comment.setTime(obj.get("created_at").toString());
			else
				comment.setTime(null);

			CommitCommentList.add(comment);			
		}

		return CommitCommentList;
	}
}