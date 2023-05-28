package ModelPackage;

import java.io.Serializable;
import java.util.ArrayList;

import baseScrabble.Tile;

public class Player implements Serializable {

	private static final long serialVersionUID = 1L;
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
	
}
