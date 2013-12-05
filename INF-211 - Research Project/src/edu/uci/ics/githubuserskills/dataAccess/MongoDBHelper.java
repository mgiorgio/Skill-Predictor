package edu.uci.ics.githubuserskills.dataAccess;

import java.net.UnknownHostException;
import java.util.Set;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

/**
 * @author shriti Utility to operate over the database
 */
public class MongoDBHelper {

	public MongoClient getConnection(String Server, int Port) throws UnknownHostException {
		MongoClient mongo = new MongoClient("localhost", 27017);
		return mongo;
	}

	public DB getDatabase(String databaseName, MongoClient mongo) {
		DB db = mongo.getDB(databaseName);
		return db;
	}

	public Set<String> getAllTables(DB db) {
		return (db.getCollectionNames());
	}

	public DBCollection getTable(String tableName, DB db) {
		return (db.getCollection(tableName));
	}

	public DBCursor findData(BasicDBObject searchQuery, DBCollection table, BasicDBObject fields) {
		// if(fields == null)
		// return(table.find(searchQuery));
		// else
		// return(table.find(searchQuery, fields));
		if (fields == null)
			return (table.find(searchQuery).limit(100));
		else
			return (table.find(searchQuery, fields).limit(100));

	}
}
