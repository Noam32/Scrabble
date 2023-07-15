package modelPackage;

import baseScrabble.Tile;
import baseScrabble.Word;
import server.MyServer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
	//PrintWriter outToServer;
	private ObjectOutputStream outToServer;
	//BufferedReader inFromServer;
	private ObjectInputStream inFromServer;
	private ObjectStream myObjectStream;//ObjectStream:class for sending (Serializable)objects through TCP/IP:
	private boolean hasGameStarted=false;


	public ModelGuest(String name){
		//this.gamestate=new GameState();
		this.name=name;
		initConnectiontoServer();
	}
	//use this method when resuming a save game
	@Override
	public void resumeGame(GameState game) {
		this.gamestate=game;
		//TODO wait for player to connect:
		//init local server ...
	}


	//*******************************
	//Get data method :
	//*******************************

	@Override //Unfinished ????????????untested!
	public GameState getGameState() {
		String []command=GuestClientHandler.createCommandStrings("getGameState");
		sendAllString(command);//sending request to host
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
		String [] command=GuestClientHandler.createCommandStrings("getNumOfPointsForPlayer","Integer",strPlayerId);
		sendAllString(command);//sending request to host
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
		sendAllString(command);//sending request to host
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
		String [] command=GuestClientHandler.createCommandStrings("getTilesForPlayer","Integer",strPlayerId);
		sendAllString(command);//sending request to host
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
	public ArrayList<Tile> getTilesForPlayer(String name) {
		String [] command=GuestClientHandler.createCommandStrings("getTilesForPlayer","String",name);
		sendAllString(command);//sending request to host
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
		sendAllString(command);//sending request to host
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
		sendAllString(command);//sending request to host
		String inputString = getMessageFromHost();//reading response message from host:
		String [] splitString= inputString.split(":");
		String res=splitString[1];
		int intRes=Integer.parseInt(res);
		return intRes;
	}

	@Override
	public boolean wasLastPlacementSuccessful() {
		String []command=GuestClientHandler.createCommandStrings("wasLastPlacementSuccessful");
		sendAllString(command);//sending request to host
		String inputString = getMessageFromHost();//reading response message from host:
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
		String [] command=GuestClientHandler.createCommandStrings("addAplayer","String",name);
		sendAllString(command);//sending request to host
		getMessageFromHost();//reading response message from host:
	}

	@Override
	public void initGame() {
		String []command=GuestClientHandler.createCommandStrings("initGame");
		sendAllString(command);//sending request to host
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
		String [] command=GuestClientHandler.createCommandStrings("givePlayerOneTile","String",strPlayerId);
		sendAllString(command);//sending request to host
		//getMessageFromHost();//reading response message from host:
		
	}

	@Override
	public void placeWordOnBoard(Word w) throws Exception {
		// TODO Auto-generated method stub
		//Special communication : here we only send the method name and after that we send the object!
		String [] command=GuestClientHandler.createCommandStrings("placeWordOnBoard","Word","Object");
		sendAllString(command);
		getMessageFromHost();//reading response message from host - will ask us to send the Word object
		//send word object:
		myObjectStream.writeObjectOut(w);
		//wait for response:
		String ack_or_error_msg=getMessageFromHost();//reading response message from host(eeither an ACK or exception string)
		System.out.println("modelguest.placeWordOnBoard() ack_or_error_msg = "+ack_or_error_msg);
		//change data:
		setChanged();
		this.notifyObservers();

	}

	@Override
	public void endPlayerTurn() {
		String []command=GuestClientHandler.createCommandStrings("endPlayerTurn");
		sendAllString(command);//sending request to host
		getMessageFromHost();//reading response message from host:
		setChanged();
		this.notifyObservers();
	}

	@Override
	public void skipPlayerTurn() {
		String []command=GuestClientHandler.createCommandStrings("skipPlayerTurn");
		sendAllString(command);//sending request to host
		getMessageFromHost();//reading response message from host:
		setChanged();
		this.notifyObservers();
	}

	public void initConnectiontoServer() {
		try {
			int port=ModelHost.Host_PortFor_Communicating_With_Guests;
		this.client=new Socket("localhost",port);
		//initializing object sending/receiving object:
		myObjectStream=new ObjectStream(client);//passing the socket to the Serializable object sender
		//initializing string senders / receivers objects:
		//this.outToServer=new PrintWriter(client.getOutputStream(),true);
		//this.inFromServer=new BufferedReader(new InputStreamReader(client.getInputStream()));
		
		//Found a solution here :https://stackoverflow.com/questions/14217245/socket-objectoutpustream-objectinputstream
		//have both side start their streams in reverse order (Guest:out then in vs Host(guestclienthandler): in and then out)
		//Initialize the "myObjectStream"
		System.out.println("initConnectiontoServer started");
		myObjectStream.initOutputStreams();
		myObjectStream.oos.flush();
		myObjectStream.initInputStream();
		System.out.println("initConnectiontoServer :myObjectStream initialized");
		System.out.println("initConnectiontoServer :myObjectStream initialization - done!");
		Thread.sleep(1000);
		//Now we can start to communicate: 
		String inputString=myObjectStream.readString();//reading welcome  message from host:
		System.out.println("ModelGuest says :message received from server:"+inputString);
		myObjectStream.sendString(this.name); // sending the name to the host
		inputString=myObjectStream.readString();;//reading waiting for players message from host:
		System.out.println("ModelGuest says :message received from server:"+inputString);
		inputString=myObjectStream.readString();//reading good luck message for players from host:
		System.out.println("ModelGuest says :message received from server:"+inputString);
		this.hasGameStarted=true;
		}
		catch (IOException e) {
			errorInLast_communication=true;
			//throw new RuntimeException(e);
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	public void sendAllString(String []command){
		for(int i=0;i< command.length;i++){
			try {
				myObjectStream.sendString(command[i]);
			} catch (IOException e) {
			};//sending every part of the String []command
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
			inputString = myObjectStream.readString();//reading response message from host:
		} catch (IOException | ClassNotFoundException e) {
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


	public boolean hasGameStarted() {
		return this.hasGameStarted;
	}

}