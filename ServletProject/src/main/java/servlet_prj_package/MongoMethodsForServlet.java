package servlet_prj_package;

import java.util.HashMap;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public abstract class MongoMethodsForServlet {
	//SETTINGS FOR THE MONGODB SERVER:
		// Connection settings
		public static String connectionString = "mongodb://localhost:27017";
		public static String databaseName = "myDataBase";
		// Create a MongoDB client
		public static MongoClient  mongoClient = MongoClients.create(connectionString);
		//public static MongoClient mongoClient = MongoClients.create();//// ("mongodb://localhost:27017");
		public static String collectionName="GameSavesDB";
	    public static MongoDatabase database = mongoClient.getDatabase(databaseName);
		public static MongoCollection<Document> game_collection = database.getCollection(collectionName);
		
		
		
		public static void main(String[]args) {
			//Document document=getGameSaveFromServer("JohnGame123");
			//System.out.println(document.toJson());
		}
		
		
		//returns the document from the mongoDbServer with the matching name .(assumption :name has to be unique!)
		protected static Document getGameSaveFromServer(String nameOfGameSave) {	
	    	
			Document document=null;
			try {
	    	document = getAllGameStateFields(game_collection,nameOfGameSave);
	    	//System.out.println("getGameSaveFromServer:"+document.toJson());
			}catch(Exception e) {
				e.printStackTrace();
			}
	    	return document;
	    } 
		
		//https://www.mongodb.com/docs/manual/tutorial/project-fields-from-query-results/
		//using projection to get all of the inner/nested documents:
		//https://www.baeldung.com/mongodb-query-documents-id
		private static Document getAllGameStateFields(MongoCollection<Document> gameCollection,String gameSaveName) {
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
		
		
		//returns a string representation of the getScoreBoardOfGame for the corresponding nameOfGameSave in the database 
		protected static String getScoreBoardOfGame(String nameOfGameSave) {
			//String output="";
			Document game_document =getGameSaveFromServer(nameOfGameSave);
			if(game_document==null) { //if the name is not found in the database
				return null;
			}
			Document listOfPlayersDocument=game_document.get("listOfPlayers",Document.class);
			if(listOfPlayersDocument==null) { //if the hash map of players is not found
				return null;
			}
			HashMap<String,Integer> hash_player_numOfPoints=getHashmapOfNameToPoints(listOfPlayersDocument);
			return (hash_player_numOfPoints.toString());
		}

	
		//This method builds the HashMap object from its corresponding document
		private static HashMap<String,Integer> getHashmapOfNameToPoints(Document listofPlayersDocument){
			HashMap<String,Integer> hash_name_to_points=new HashMap<>();
			int maxNumOfplayers=4;
			boolean endLoopFlag=false;
			//iterating over the indices and getting the inner documents each representing a player:
			for(int i=0;i<maxNumOfplayers&& !endLoopFlag;i++) {
			Document currPlayerDocument =listofPlayersDocument.get(""+i, Document.class);
			if(currPlayerDocument==null) {
				endLoopFlag=true;
			}
			else {
				Integer points=currPlayerDocument.getInteger("numOfPoints");
				String name =currPlayerDocument.getString("name");
				hash_name_to_points.put(name,points);
			}
		}
		return hash_name_to_points;
	    	
	    }
		
		//this method will return a string with all of the names of gamesaves in the database collection:
		public static String getAllNamesOfGameSaves() {
			// Find all documents and retrieve the "gameSaveName" field
	        FindIterable<Document> documents = game_collection.find(); //getting all documents :
	        MongoCursor<Document> cursor = documents.iterator();
	        String output="";
	        //System.out.println(documents.toString()+"  "+cursor.toString());
	        //List<String> gameSaveNames = new ArrayList<>();
	        while (cursor.hasNext()) {
	            Document document = cursor.next();
	            String gameSaveName = document.getString("gameSaveName");
	            //System.out.println("gameSaveName: "+gameSaveName);
	            output+=gameSaveName+"\n";
	        }
	        return output;
		}
		
		
}

/*
 *  <!-- https://mvnrepository.com/artifact/org.mongodb/mongo-java-driver -->
        <dependency>
            <groupId>org.mongodb</groupId>
            <artifactId>mongo-java-driver</artifactId>
            <version>3.6.3</version>
        </dependency>
 * 
 */

