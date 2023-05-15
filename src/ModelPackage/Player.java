package ModelPackage;

import java.util.ArrayList;

import baseScrabble.Tile;

public class Player {
	private static int currSerialNum=1;
	final int playerId;
	String name;
	int numOfPoints;
	ArrayList<Tile> myTiles; //up to 7 tiles according to game logic.
	
	public Player(String Name) {
		//Unique serial number for each player created:
		playerId=currSerialNum;
		currSerialNum++;
		this.name=Name;
		numOfPoints=0;
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
	
	
	
}
