package modelPackage;

import java.io.Serializable;
import java.util.ArrayList;
import org.bson.Document;

import baseScrabble.Tile;

public class Player implements Serializable {

	private static final long serialVersionUID = 1L;
	private static int currSerialNum=1;
	final int playerId;
	String name;
	public int numOfPoints;
	public ArrayList<Tile> myTiles; //up to 7 tiles according to game logic.
	
	public Player(String Name) {
		//Unique serial number for each player created:
		playerId=currSerialNum;
		currSerialNum++;
		this.name=Name;
		numOfPoints=0;
		myTiles=new ArrayList<Tile>();
	}

	public Player(String Name,int id) {
		//Unique serial number for each player created:
		playerId=id;
		currSerialNum++;
		this.name=Name;
		numOfPoints=0;
		myTiles=new ArrayList<Tile>();
	}
	
	public int getplayerId() {
		return playerId;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getNumOfPoints() {
		return numOfPoints;
	}
	public void setNumOfPoints(int numOfPoints) {
		this.numOfPoints = numOfPoints;
	}
	public ArrayList<Tile> getMyTiles() {
		return myTiles;
	}
	public void setMyTiles(ArrayList<Tile> myTiles) {
		this.myTiles = myTiles;
	}
	public void addTile(Tile e) {
		myTiles.add(e);
	}
	@Override
	public String toString() {
		String s1;
		s1="{playerId:"+playerId+",name:"+name+",numOfPoints:"+numOfPoints+"}";
		for(int i=0;i<this.myTiles.size();i++) {
		}
		return s1;
		
	}
	
	public boolean equals(Player other) {
		boolean res =true;
		if(this.playerId!=other.playerId) {
			return false;
		}
		if(!this.name.equals(other.name)) {
			return false;
		}
		if(this.myTiles.size()!=other.myTiles.size()) {
			return false;
		}
		for(int i=0;i<this.myTiles.size();i++) {
			if(this.myTiles.get(i).letter!=other.myTiles.get(i).letter) {
				return false;
			}
		}
		if(this.numOfPoints!=other.numOfPoints) {
			return false;
		}
		
		return res;
	}

	public Document toDocument() {
		Document document= new Document();
		document.append("name", name);
		document.append("playerId", this.playerId);
		document.append("numOfPoints",(Integer)this.numOfPoints );
		document.append("myTiles",embedded_myTilesDoc() );
		return document;

	}
	//creating an document that has multiple documents in it to include all of the tile objects:
	public Document embedded_myTilesDoc() {
		Document document= new Document();
		for(int i=0;i<this.myTiles.size();i++ ) {
			document.append(""+i, this.myTiles.get(i).toDocument());
		}
		return document;
	}

	//convert mongodb doc to a Player object:
	public static Player fromDocument(Document document_player) {
		Player p1;
		//getting the simple objects:
		int id=(int)document_player.getInteger("playerId");
		String name=document_player.getString("name");
		int numOfPoints=(int)document_player.getInteger("numOfPoints");
		ArrayList<Tile> listOfTileDoc;//new ArrayList<Document>();
		//The following line does not work need to fix:
		//listOfTileDoc=new ArrayList<Document>( document_player.getList("myTiles", Document.class));
		listOfTileDoc=getListOfTilesFromDoc(document_player.get("myTiles", Document.class));
		//now we get the tile from each document:
		//ArrayList<Tile> tiles=new ArrayList<Tile> ();
		//for(int i=0; i<listOfTileDoc.size();i++) {

		//	Tile t=Tile.fromDocument(listOfTileDoc.get(i) ) ;
		//	tiles.add(t);
		//}
		p1=new Player(name,id);
		p1.myTiles=listOfTileDoc;
		//p1.playerId=id;
		p1.numOfPoints=numOfPoints;
		return p1;

	}

	public static ArrayList<Tile> getListOfTilesFromDoc(Document listOfTileDoc){
		if(listOfTileDoc==null)
			return null;
		ArrayList<Tile> listOfTiles=new ArrayList<>();
		int maxNumOfTiles=GameState.numOfTilesForPlayer;
		boolean done=false;
		//reading all of the tile documents in the list
		for(int i=0;i<maxNumOfTiles &&!done;i++) {
			Document currTileDoc =listOfTileDoc.get(""+i,Document.class);
			if(currTileDoc!=null) {
				listOfTiles.add(Tile.fromDocument(currTileDoc));
			}else {
				done=true;//finished reading all tile documents
			}
		}
		return listOfTiles;
	}
}
