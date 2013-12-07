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

/**
 * @author shriti Class to fetch User object and Author object from database
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
	public DBCollection getTable(MongoDBHelper helper) throws UnknownHostException {

		MongoClient DbClient = helper.getConnection("localhost", 27017);
		DB database = helper.getDatabase(databaseName, DbClient);
		DBCollection table = helper.getTable(tableName, database);
		return table;

	}

	public DBObject getAuthorDBObject(String login) throws UnknownHostException {

		MongoDBHelper helper = new MongoDBHelper();
		BasicDBObject searchQuery = new BasicDBObject("login", login);
		BasicDBObject excludeFields = new BasicDBObject("_id", 0).append("name", 0).append("company", 0).append("blog", 0).append("location", 0).append("email", 0).append("hireable", 0)
				.append("bio", 0).append("public_repos", 0).append("followers", 0).append("following", 0).append("created_at", 0).append("updated_at", 0).append("public_gists", 0);
		DBCollection table = getTable(helper);
		DBCursor cursor = helper.findData(searchQuery, table, excludeFields);
		if (cursor.size() > 1) {
			// TODO log a warning message that more than one entry found for a
			// unique login
		}
		// return the first entry as the author
		DBObject obj = cursor.next();
		return obj;

	}

	public BasicDBList getAllLogins() throws UnknownHostException {
		// TODO: extract all logins
		MongoDBHelper helper = new MongoDBHelper();
		DBCollection table = getTable(helper);
		BasicDBList list = (BasicDBList) table.distinct("login");
		return list;
	}

	public List<String> getLoginList() throws UnknownHostException {
		List<String> loginList = new ArrayList<String>();
		BasicDBList list = getAllLogins();
		/*
		 * Iterator it = list.iterator(); while(it.hasNext()) { Object obj =
		 * it.next(); loginList.add(obj.toString()); }
		 */
		for (int i = 0; i < list.size(); i++) {
			loginList.add(list.get(i).toString());
		}
		return (loginList);
	}
}
