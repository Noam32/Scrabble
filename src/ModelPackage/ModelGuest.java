package ModelPackage;

import baseScrabble.Tile;
import baseScrabble.Word;
import server.MyServer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;


// This class is the model for the remote client - who doesn't have the game state - therefore it has to send requests
//(currently TcpId to the host model!!  

public class ModelGuest extends Observable implements Model {
	private static boolean errorInLast_communication=false;
	private  GameState gamestate;
	String name;
	public Socket client;// the guest player
	PrintWriter outToServer;
	BufferedReader inFromServer;
	private ObjectStream myObjectStream;//ObjectStream:class for sending (Serializable)objects through TCP/IP:


	public ModelGuest(String name){
		//this.gamestate=new GameState();
		this.name=name;
		initConnectiontoServer();
	}


	//*******************************
	//Get data method :
	//*******************************

	@Override //Unfinished ????????????untested!
	public GameState getGameState() {
		String []command=GuestClientHandler.createCommandStrings("getGameState");
		sendAllString(command,outToServer);//sending request to host
		//getMessageFromHost();//reading response message from host:
		//reading object sent from host:
		Object objectSentFromHost=null;
		try {
			objectSentFromHost = getObjectOver_TCP_IP();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		//now we try to covert the object to "GameState" object: (if fails -an exception will be thrown)
		GameState state=(GameState)objectSentFromHost;
		return state;
	}

	@Override
	public int getNumOfPointsForPlayer(int playerId) {
		String strPlayerId = ""+playerId;
		String [] command=GuestClientHandler.createCommandStrings("getNumOfPointsForPlayer","String",strPlayerId);
		sendAllString(command,outToServer);//sending request to host
		String inputString = getMessageFromHost();//reading response message from host:
		String [] splitString= inputString.split(":");
		String res=splitString[1];
		int intRes=Integer.parseInt(res);
		return intRes;
	}

	@Override
	public int getNumOfPointsForPlayer(String name) {
		String [] command=GuestClientHandler.createCommandStrings("getNumOfPointsForPlayer","String",name);
		System.out.println("message sent to server:");
		GuestClientHandlerTest.printStrings(command);
		sendAllString(command,outToServer);//sending request to host
		String inputString = getMessageFromHost();//reading response message from host:
		System.out.println("message got from server:"+inputString);
		String [] splitString= inputString.split(":");
		String res=splitString[1];
		int intRes=Integer.parseInt(res);
		return intRes;
	}

	@Override//Unfinished???untested!
	public ArrayList<Tile> getTilesForPlayer(int playerId) {
		String strPlayerId = ""+playerId;
		String [] command=GuestClientHandler.createCommandStrings("getTilesForPlayer","String","strPlayerId");
		sendAllString(command,outToServer);//sending request to host
		//getMessageFromHost();//reading respone message from host:
		Object objectSentFromHost=null;
		try {
			objectSentFromHost = getObjectOver_TCP_IP();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		//now we try to covert the object to "ArrayList<Tile> " object: (if fails -an exception will be thrown)
		ArrayList<Tile> list=(ArrayList<Tile>)objectSentFromHost;
		return list;
	}

	@Override//Unfinished???untested!
	public ArrayList<Tile> getTilesForPlayer(String playerId) {
		String [] command=GuestClientHandler.createCommandStrings("getTilesForPlayer","String","1");
		sendAllString(command,outToServer);//sending request to host
		//reading object sent from host:
		Object objectSentFromHost=null;
		try {
			objectSentFromHost = getObjectOver_TCP_IP();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		//now we try to covert the object to "ArrayList<Tile> " object: (if fails -an exception will be thrown)
		ArrayList<Tile> list=(ArrayList<Tile>)objectSentFromHost;
		return list;
	}

	@Override//Unfinished????untested!
	public Player WhoseTurnIsIt() {
		String []command=GuestClientHandler.createCommandStrings("WhoseTurnIsIt");
		sendAllString(command,outToServer);//sending request to host
		//getMessageFromHost();//reading response message from host:
		Object objectSentFromHost=null;
		try {
			objectSentFromHost = getObjectOver_TCP_IP();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		//now we try to covert the object to class "Player " object: (if fails -an exception will be thrown)
		Player p1=(Player)objectSentFromHost;
		return p1;
	}

	@Override
	public int WhoseTurnIsIt_Id() {
		String []command=GuestClientHandler.createCommandStrings("WhoseTurnIsIt_Id");
		sendAllString(command,outToServer);//sending request to host
		String inputString = getMessageFromHost();//reading response message from host:
		String [] splitString= inputString.split(":");
		String res=splitString[1];
		int intRes=Integer.parseInt(res);
		return intRes;
	}

	@Override
	public boolean wasLastPlacementSuccessful() {
		String []command=GuestClientHandler.createCommandStrings("wasLastPlacementSuccessful");
		sendAllString(command,outToServer);//sending request to host
		String inputString = getMessageFromHost();//reading respone message from host:
		String [] splitString= inputString.split(":");
		String res=splitString[1];
		Boolean boolRes=Boolean.parseBoolean(res);
		return boolRes;
	}


	//*******************************
	//Change data method :
	//*******************************

	@Override
	public void addAplayer(String name) {
		String [] command=GuestClientHandler.createCommandStrings("addAplayer","String","player1");
		sendAllString(command,outToServer);//sending request to host
		getMessageFromHost();//reading response message from host:
	}

	@Override
	public void initGame() {
		String []command=GuestClientHandler.createCommandStrings("initGame");
		sendAllString(command,outToServer);//sending request to host
		getMessageFromHost();//reading response message from host:
		/*try {
			String inputString = inFromServer.readLine();//reading approval message from host that everything was good and finished:
		} catch (IOException e) {
			throw new RuntimeException(e);
		}*/
	}

	@Override
	public void givePlayerOneTile(int playerId) {
		String strPlayerId = ""+playerId;
		String [] command=GuestClientHandler.createCommandStrings("givePlayerOneTile","String","strPlayerId");
		sendAllString(command,outToServer);//sending request to host
		//getMessageFromHost();//reading response message from host:
		
	}

	@Override
	public void placeWordOnBoard(Word w) {
		// TODO Auto-generated method stub
		

	}

	@Override
	public void endPlayerTurn() {
		String []command=GuestClientHandler.createCommandStrings("endPlayerTurn");
		sendAllString(command,outToServer);//sending request to host
		getMessageFromHost();//reading response message from host:
	}

	@Override
	public void skipPlayerTurn() {
		String []command=GuestClientHandler.createCommandStrings("skipPlayerTurn");
		sendAllString(command,outToServer);//sending request to host
		getMessageFromHost();//reading response message from host:
	}

	public void initConnectiontoServer() {
		try {
		this.client=new Socket("localhost",8080);
		//initializing object sending/receiving object:
		myObjectStream=new ObjectStream(client);//passing the socket to the Serializable object sender
		//initializing string senders / receivers objects:
		this.outToServer=new PrintWriter(client.getOutputStream(),true);
		this.inFromServer=new BufferedReader(new InputStreamReader(client.getInputStream()));
		String inputString=inFromServer.readLine();//reading welcome from host:
		System.out.println("message received from server:"+inputString);
		outToServer.println(this.name);
		inputString=inFromServer.readLine();//reading waiting for players message from host:
		System.out.println("message received from server:"+inputString);
		inputString=inFromServer.readLine();//reading good luck message for players from host:
		System.out.println("message received from server:"+inputString);
		}
		catch (IOException e) {
			errorInLast_communication=true;
			throw new RuntimeException(e);
		}
	}


	public void sendAllString(String []command, PrintWriter outToServer){
		for(int i=0;i< command.length;i++){
			outToServer.println(command[i]);//sending every part of the String []command
		}
	}
	
	private Object getObjectOver_TCP_IP() throws ClassNotFoundException, IOException {
		Object obj =myObjectStream.readObject();
		return obj;
	}

	@Override
	public boolean wasThereAnErrorAtLastCommunication() {
		return errorInLast_communication;
	}
	
	public String getMessageFromHost() {
		String inputString;
		try {
			inputString = inFromServer.readLine();//reading response message from host:
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		if(inputString==null) {
			System.out.println("ModelGuest says:error communicating with host.errorInLast_communication=true");
			errorInLast_communication=true;
		}else{
			errorInLast_communication=false;
		}
		return inputString;
	}


}