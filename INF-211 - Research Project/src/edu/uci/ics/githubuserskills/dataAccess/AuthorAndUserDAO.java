package edu.uci.ics.githubuserskills.dataAccess;


import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import edu.uci.ics.githubuserskills.model.db.Author;
import edu.uci.ics.githubuserskills.model.db.User;



/**
 * @author shriti
 * Class to fetch User object and Author object from database
 * 
 */

public class AuthorAndUserDAO {
	public static final String tableName = "users";
	public static final String databaseName = "msr14";

	/**
	 * @param helper
	 * @return DBCollection table
	 * @throws UnknownHostException
	 */
	public DBCollection getTable(MongoDBHelper helper) throws UnknownHostException
	{

		MongoClient DbClient = helper.getConnection("localhost", 27017);
		DB database = helper.getDatabase(databaseName, DbClient);
		DBCollection table = helper.getTable(tableName, database);
		return table;

	}
	/**
	 * @param obj, author
	 * @return
	 */
	public void setCommonAttributes(DBObject obj, Author author)
	{
		//TODO: Deal with null values
		author.setLogin(obj.get("login").toString());
		author.setId(Integer.parseInt(obj.get("id").toString()));
		author.setAvatar_url(obj.get("avatar_url").toString());
		author.setGravatar_id(obj.get("gravatar_id").toString());
		author.setUrl(obj.get("url").toString());
		author.setHtml_url(obj.get("html_url").toString());
		author.setFollowers_url(obj.get("followers_url").toString());
		author.setFollowing_url(obj.get("following_url").toString());
		author.setGists_url(obj.get("gists_url").toString());
		author.setStarred_url(obj.get("starred_url").toString());
		author.setSubscriptions_url(obj.get("subscriptions_url").toString());
		author.setOrganizations_url(obj.get("organizations_url").toString());
		author.setRepos_url(obj.get("repos_url").toString());
		author.setEvents_url(obj.get("events_url").toString());
		author.setReceived_events_url(obj.get("received_events_url").toString());
		author.setType(obj.get("type").toString());
	}
	/**
	 * @param login
	 * @return Author
	 * @throws UnknownHostException
	 */
	public Author getAuthorbyLogin(String login) throws UnknownHostException
	{
		MongoDBHelper helper = new MongoDBHelper();
		Author author = new Author();
		BasicDBObject searchQuery = new BasicDBObject("login", login);
		BasicDBObject excludeFields = new BasicDBObject("_id", 0).append("name", 0).append("company", 0).append("blog", 0).append("location", 0).append("email", 0)
				.append("hireable", 0).append("bio", 0).append("public_repos", 0).append("followers", 0).append("following", 0)
				.append("created_at", 0).append("updated_at", 0).append("public_gists", 0);
		DBCollection table = getTable(helper);
		DBCursor cursor = helper.findData(searchQuery, table, excludeFields);
		if(cursor.size()>1)
		{
			// TODO log a warning message that more than one entry found for a unique login
		}
		//return the first entry as the author
		DBObject obj= cursor.next();
		setCommonAttributes(obj, author);
		return author;

	}
	/**
	 * @param login
	 * @return User
	 * @throws UnknownHostException
	 */
	public User getUserByLogin(String login) throws UnknownHostException
	{
		MongoDBHelper helper = new MongoDBHelper();
		User user = new User();
		BasicDBObject searchQuery = new BasicDBObject("login", login);
		DBCollection table = getTable(helper);
		DBCursor cursor = helper.findData(searchQuery, table, null);
		if(cursor.size()>1)
		{
			// TODO log a warning message that more than one entry found for a unique login
		}
		//return the first entry as the author
		DBObject obj= cursor.next();
		setCommonAttributes(obj, user);
		//TODO: Deal with null values. If obj.get(attrib) returns a null, it needs to be checked.
		user.setName(obj.get("name").toString());
		user.setCompany(obj.get("company").toString());
		user.setBlog(obj.get("blog").toString());
		user.setLocation(obj.get("location").toString());
		user.setEmail(obj.get("email").toString());
		user.setHireable(obj.get("hireable").toString());
		user.setBio(obj.get("bio").toString());
		user.setPublic_repos(Integer.parseInt(obj.get("public_repos").toString()));
		user.setFollowers(Integer.parseInt(obj.get("followers").toString()));
		user.setFollowing(Integer.parseInt(obj.get("following").toString()));
		user.setCreated_at(obj.get("created_at").toString());
		user.setUpdated_at(obj.get("updated_at").toString());
		user.setPublic_gists(Integer.parseInt(obj.get("public_gists").toString()));

		return user;
	}

	public DBObject getAuthorDBObject(String login) throws UnknownHostException{

		MongoDBHelper helper = new MongoDBHelper();
		BasicDBObject searchQuery = new BasicDBObject("login", login);
		BasicDBObject excludeFields = new BasicDBObject("_id", 0).append("name", 0).append("company", 0).append("blog", 0).append("location", 0).append("email", 0)
				.append("hireable", 0).append("bio", 0).append("public_repos", 0).append("followers", 0).append("following", 0)
				.append("created_at", 0).append("updated_at", 0).append("public_gists", 0);
		DBCollection table = getTable(helper);
		DBCursor cursor = helper.findData(searchQuery, table, excludeFields);
		if(cursor.size()>1)
		{
			// TODO log a warning message that more than one entry found for a unique login
		}
		//return the first entry as the author
		DBObject obj= cursor.next();
		return obj;

	}

	public BasicDBList getAllLogins() throws UnknownHostException
	{
		//TODO: extract all logins
		MongoDBHelper helper = new MongoDBHelper();
		DBCollection table = getTable(helper);
		BasicDBList list = (BasicDBList) table.distinct("login");
		return list;
	}
	
	public List<String> getLoginList() throws UnknownHostException
	{
		List<String> loginList = new ArrayList<String>();
		BasicDBList list = getAllLogins();
		Iterator it = list.iterator();
		while(it.hasNext())
		{
			Object obj = it.next();
			loginList.add(obj.toString());	
		}
		return(loginList);
	}
}
