package edu.uci.ics.githubuserskills.dataAccess;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

/**
 * @author shriti Utility to operate over the database
 */
public class MongoDBHelper {

	private static Map<ServerAddress, MongoClient> cachedConnections = new HashMap<ServerAddress, MongoClient>();

	public static MongoClient getConnection(ServerAddress address) throws UnknownHostException {
		// MongoClient mongo = new MongoClient("localhost", 27017);

		MongoClient mongoClient = cachedConnections.get(address);

		if (mongoClient == null) {
			// MongoClient mongo = new MongoClient("localhost", 27017);
			mongoClient = new MongoClient(address);
			cachedConnections.put(address, mongoClient);
		}
		return mongoClient;
	}

	public static DB getDatabase(String databaseName, MongoClient mongo) {
		DB db = mongo.getDB(databaseName);
		return db;
	}

	public static Set<String> getAllTables(DB db) {
		return (db.getCollectionNames());
	}

	public static DBCollection getTable(String tableName, DB db) {
		return (db.getCollection(tableName));
	}

	public static DBCursor findData(BasicDBObject searchQuery, DBCollection table, BasicDBObject fields) {
		if (fields == null)
			return (table.find(searchQuery));
		else
			return (table.find(searchQuery, fields));

	}
}
