package mongodbCommunicationPackage;


import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;

import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.codecs.pojo.PojoCodecProvider.Builder;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.MongoClientSettings;
import baseScrabble.Tile.Bag;
import modelPackage.*;
import baseScrabble.Tile;

public class Test_com_with_mongodb {
	// Connection settings
	public static String connectionString = "mongodb+srv://turbh:0525685974@atlascluster.ji8h963.mongodb.net/?retryWrites=true&w=majority";
	public static String databaseName = "myDataBase";
			// Create a MongoDB client
	public static MongoClient  mongoClient = MongoClients.create(connectionString);
			// Get a reference to the database
	//public static MongoDatabase  database = mongoClient.getDatabase(databaseName);
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		insertGameStateObjToMongoDB();
		GameState game = MongoDbMethods.getGameSaveFromMongo("placeHoldergameSaveName");
		//System.out.println(game.toString());
		ModelGuestTest.printGameState(game);
		game.addAPlayer("newPlayerTest");
		MongoDbMethods.sendGameStateToMongo(game, "test_game_delete_after");
	}

	
	public static void TestGettingGameStateFromMongo() {
		// Get a reference to the database
				testConnection( mongoClient);
		        String collectionName="GameSavesDB";
		        MongoDatabase database = mongoClient.getDatabase(databaseName);
				MongoCollection<Document> game_collection = database.getCollection(collectionName);
				
		        //Now we need to test retrieving gamesaves from the db:
				//accessing the db to get document with the matching gameSaveName :
				String gameSaveName_to_load="placeHoldergameSaveName";
				Document gameDoc = getAllGameStateFields(game_collection,gameSaveName_to_load);
				System.out.println("document is:");
				System.out.println(gameDoc.toString());
				GameState game=GameState.readGameStatefromDocument(gameDoc);
				System.out.println("gamstate read from mongod server is:");
				ModelGuestTest.printGameState(game);
				System.out.println("players are");
				Player p1=game.listOfPlayers.get(0);
				Player p2=game.listOfPlayers.get(1);
				System.out.println(p1);
				System.out.println(p1.myTiles);
				System.out.println(p2	);
				System.out.println(p2.myTiles);
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
		
		if(cursor.hasNext())
			 d=cursor.next();
		return d;
	}
	
	
	
	
	
	
	public static void insertGameStateObjToMongoDB() {
		System.out.println("test connection to mongo db= "+testConnection(mongoClient));
		GameState game= new GameState();
		Bag b1=game.bag;
		//testDocuments:
		System.out.println("Bag to doc:"+b1.toDocument().toString());
		System.out.println("ConnectedBoard to doc "+game.gameBoard.toDocument().toString());
		
		//putgame state into a known state : 
		game.addAPlayer("yossi");
		game.addAPlayer("dani");
		Player p1=game.listOfPlayers.get(0);
		p1.myTiles.add(b1.getTile('A'));
		p1.numOfPoints=50;
		Player p2=game.listOfPlayers.get(1);
		p2.myTiles.add(b1.getTile('B'));
		p2.numOfPoints=160;
		
		System.out.println("Player p1 to doc:"+game.listOfPlayers.get(0).toDocument().toString());
		System.out.println("Player p2 to doc:"+game.listOfPlayers.get(1).toDocument().toString());
		
		game.gameBoard.tiles[7][7]=b1.getTile('Y');
		System.out.println(game.gameBoard.toString());
		game.gameBoard.tiles[0][0]=b1.getTile('A');
		System.out.println("ConnectedBoard to doc "+game.gameBoard.toDocument().toString());
		//
		Document document =game.toDocument();
		
		System.out.println("-------------------------");
		System.out.println("the final document:"+ document);
		System.out.println("---------------------------");
        // Get a reference to the database
        String collectionName="GameSavesDB";
        MongoDatabase database = mongoClient.getDatabase(databaseName);
		//now we can access the d base?
        MongoCollection<Document> game_collection = database.getCollection(collectionName);
        game_collection.insertOne(document);
	}
	
	
	
	
	 
	
	public static boolean testConnection(MongoClient mongoClient) {
        try {
            // Attempt to retrieve the database names
            String s=mongoClient.listDatabaseNames().first();
            System.out.println("first data base is "+s);
            return true;
        } catch (Exception e) {
        	e.printStackTrace();
            return false;
        }
    }
	
	
	
	
}


/*Leftover code:
 // Create the projection to include all fields
        //Document projection = new Document("$project", new Document());
        // Retrieve the document with nested objects
       // Document gameDoc = gameCollection
       //         .find(Filters.eq("gameSaveName", gameSaveName))
       //         .projection(Projections.i)
       //         .first();
        * 
        * 		int i=0;
		//while (cursor.hasNext()) {
		//    System.out.println(cursor.next());
		//    System.out.println("i="+i);
		//}
 */














