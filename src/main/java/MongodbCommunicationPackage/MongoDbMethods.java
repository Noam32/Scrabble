package MongodbCommunicationPackage;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import ModelPackage.GameState;


public class MongoDbMethods {
	//SETTINGS FOR THE MONGODB SERVER:
	// Connection settings
	public static String connectionString = "mongodb://localhost:27017";
	public static String databaseName = "myDataBase";
	// Create a MongoDB client
	public static MongoClient  mongoClient = MongoClients.create(connectionString);
	public static String collectionName="GameSavesDB";
    public static MongoDatabase database = mongoClient.getDatabase(databaseName);
	public static MongoCollection<Document> game_collection = database.getCollection(collectionName);
	
	//Static methods: 
	//send a gameState object to the server - name should be unique:
	public static void sendGameStateToMongo(GameState game,String nameOfFile) {
		game.gameSaveName=nameOfFile;
		Document document =game.toDocument();
		game_collection.insertOne(document);
	}
	//send a gameState object to the server - name should be unique:
	public static GameState getGameSaveFromMongo(String nameOfFile) {
		GameState game;
		String gameSaveName_to_load=nameOfFile;
		Document gameDoc = getAllGameStateFields(game_collection,gameSaveName_to_load);
		//parsing the BSON to a gameState java object:
		game=GameState.readGameStatefromDocument(gameDoc);
		return game;
	}
	
	
	//https://www.mongodb.com/docs/manual/tutorial/project-fields-from-query-results/
	//using projection to get all of the inner/nested documents:
	//https://www.baeldung.com/mongodb-query-documents-id
	public static Document getAllGameStateFields(MongoCollection<Document> gameCollection,String gameSaveName) {
		Document d=null;
		//Bson filter = 	Filters.eq("_id", new ObjectId("648a5de9c987872a75332952"));
		Bson filter = Filters.eq("gameSaveName", gameSaveName);
		FindIterable<Document> documents = gameCollection.find(filter);

		//Document gameDoc=gameCollection.find()
		MongoCursor<Document> cursor = documents.iterator();
		//System.out.println(cursor.next());
		//get the first result:
		if(cursor.hasNext())
			d=cursor.next();
		return d;
	}
	
	
	
	
}
