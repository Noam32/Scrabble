package modelPackage;

//import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
//import java.io.InputStreamReader;
import java.io.OutputStream;
//import java.io.OutputStreamWriter;
//import java.io.PrintWriter;
import java.util.ArrayList;

import baseScrabble.Tile;
import baseScrabble.Word;
import server.ClientHandler;




//This is a class for the MOdelHost to use when getting requests from the guests.
//We will parse the request -and get the method name and variables we will call needed  method
//and then convert the method output to a string and send it back to the client/we will perform the action and 
//send confirmation to the clients. 

public class GuestClientHandler implements ClientHandler {
	 //private PrintWriter out=null;
	 private ModelHost theHost;
	 private int numOfPlayers;
	 private ObjectStream myObjectStream;//ObjectStream:class for sending ((Serializable))objects through TCP/IP:
	 
	 public static final Exception exception=new Exception("Error parsing Command");
	 
	 
	 public GuestClientHandler() {
		 myObjectStream=new ObjectStream();
	 }
	 
	 public GuestClientHandler(ModelHost theHost,int numOfPlayers) {
		 this.theHost=theHost;
		 this.numOfPlayers=numOfPlayers;
		 myObjectStream=new ObjectStream();
	 }
	 
	 
	 //method that creates all strings for the command to send to the local-host-server.
	 public static String[] createCommandStrings(String ...Strings) {
		 if(Strings ==null) {
			 return null;
		 }
		 //if we received any variables we will add them as well:
		 int len=1+((Strings.length-1)/2); // the are two string for each variable "int","value" ...
		 //first line is method name :
		 String[] command =new String[len];
		 command[0]="MethodName:"+Strings[0];
		 //Followed by the variables:
		 for(int i=1;i<len;i=i+2) {
			 String type=Strings[i];
			 String value=Strings[i+1];
			 String str="var"+i+":"+type+":"+value;
			 command[i]=str;
		 }
		 return command;
	 }
	 
	 
	 /*
	 Format for asking the local host server to execute a method for the client:
	"MethodName:<name>
	 var_1:<type><value>
	 ...
	 var_n:<type>:<value>"
	 line=var5:double:7.9992
	 
	 String []splittedStr=line.split(":");
	 String type=splittedStr[1];
	 String value=splittedStr[2];
	 if(type==double)
	 	double input=Double.parseDouble(value);
	 	return 
	 
	 */
	 public static String getMethodName(String inputString){
		 String res;
		 String [] splitString= inputString.split(":");
		 res=splitString[1];
		 return res;
	 }
	 
	 private  Word getWordFromClient()   {
		 Word wordFromGuest;
		 Object obj=null;
		 try {
			myObjectStream.sendString("Please send the Word object");
		} catch (IOException e) {e.printStackTrace();}
		 try {
			 obj=myObjectStream.readObject();
		} catch (ClassNotFoundException | IOException e) {e.printStackTrace();}
		 
		 wordFromGuest=(Word)obj;//if this fails an exception will be thrown
		 return wordFromGuest;
	 }
	 
	@Override
	public void handleClient(InputStream inFromclient, OutputStream outToClient) throws IOException {
        //initializing the object sender object:
		System.out.println("GuestClientHandler handleClient has started");
		if(myObjectStream==null) {//
			System.err.println("***problem!!!myObjectStream=null. was it not initialized");
		}
		myObjectStream.initInputStream(inFromclient);
		System.out.println("GuestClientHandler initInputStream is done");
		myObjectStream.initOutputStreams(outToClient);
		myObjectStream.os.flush();
		System.out.println("GuestClientHandler initOutputStreams is done (also flushed)");
		//try-with-resources statement -ensures that the buffer will be closed.
    	try  {
    		//out =new PrintWriter(new OutputStreamWriter(outToClient), true);
    		//out=new PrintWriter(outToClient,true);
    		//Firstly we need to get the name of the the player:
    		String nameOfPlayer=ConnectingNewPlayer();
    		//now we 'busy wait' until the number of connected players is equal to the defined numOfPlayers:
    		while(this.numOfPlayers!=theHost.getGameState().listOfPlayers.size()) {
    			try {Thread.sleep(500);} //wait for 0.5 seconds then check again
    			catch (InterruptedException e) {e.printStackTrace();}
    		}
    		String Start_Of_Game_String="player "+nameOfPlayer+" is in the game -good luck!";
    		myObjectStream.sendString(Start_Of_Game_String);
    		//Now we initialize the game:
    		theHost.initGame();
    		//Now the player can send commands:

        	while(!theHost.hasGameEnded) {
        		String inputString;//reader.readLine();//reading the first line-that has the method name:
        		inputString=myObjectStream.readString();
        		System.out.println("Server says:I received the command :"+inputString);
        		String methodName=getMethodName(inputString);
	        	try {
	        		Object output=executeCommand(methodName);
	        		//Send output back to the client (i.e. to the model of the guest):
	        		sendOutputToClient(output); //@TODO
	        		
	        	}
	        	catch(Exception e){
	        		e.printStackTrace();
	        	}
        	}
        //out.println("Game has ended.Goodbye!");
        String goodbyeMsg="Game has ended.Goodbye!";
        myObjectStream.sendString(goodbyeMsg);
        //everything will be closed by the server - do not close here!
        	 	
    	}
    	catch(IOException e){
    		e.printStackTrace();
    	} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	
	//@TODO
	//Sends string or Object :
	//if  it is a string the format is :<Type>:<Value> 
	public void sendOutputToClient(Object output) throws IOException{
		String output_str;
		//if output is null this means that there were no exceptions and there is no value to return (void method) 
		if(output==null) {
			String Action_ACK="command completed successfully:no return values";
			myObjectStream.sendString(Action_ACK);
		}
		if(output instanceof GameState ) {//problematic! maybe try to serialize?
			//send game state over tcIP
			myObjectStream.writeObjectOut(output);
		}
		if(output instanceof Integer ) {
			//send string Integer over tcIP
			Integer num=(Integer) output;
			output_str="Integer:"+num.toString();
			myObjectStream.sendString(output_str);
			
		}
		if(output instanceof ArrayList) {//problematic! maybe try to serialize?
			//send arrayList<tile> over tcp/Ip  //Player
			myObjectStream.writeObjectOut(output);
		}
		if(output instanceof Player) {//problematic! maybe try to serialize?
		//send Player over TCP / IP 
			myObjectStream.writeObjectOut(output);
		}
		if(output instanceof Boolean) {
			//send Boolean over TCP / IP
			Boolean b1=(Boolean)output;
			//send String to the client:
			output_str ="Boolean:"+b1.toString();
			myObjectStream.sendString(output_str);
			
			}

	}
	
	
	
	//Connects the new player to the game .returns the name of the player
	public String ConnectingNewPlayer( ) throws IOException {
		String welcomeMsg="Welcome to the game:please send your name";
		myObjectStream.sendString(welcomeMsg);
		//out.println("Welcome to the game:please send your name");
		//waiting for client to send us the name.
		//String inputString=reader.readLine();//reading name line:
		String inputString=null;
		try {
			inputString = myObjectStream.readString();
			System.out.println("GuestClientHandler.ConnectingNewPlayer says:inputString is "+inputString);
			//now we add a player to the game (using host model methods):
			theHost.addAplayer(inputString);
			//out.println(inputString+" is connected.waiting for players to connect");
			String connected_ACK=inputString+" is connected.waiting for players to connect";
			myObjectStream.sendString(connected_ACK);
			return inputString;
		}
		catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
	
	
	
	
	
	//Method to decode the command received through the tcp/ip connection from the guest.
	//Will parse the string and return the output if there is any. (if the method returns void - null will be returned)
	//Also, if there is a parsing error because of bad input/connection problem we will throw an exception.
	private Object executeCommand(String methodName) throws Exception {
		final int  indexOfTypeString=1;
		final int  indexOfValueString=2;
		//------------------------------------
		Object output=null;
		//switch case for all methods:
		String[] methodsWithInputs= {"getNumOfPointsForPlayer", "getTilesForPlayer","addAplayer","givePlayerOneTile","placeWordOnBoard"};
		String []methodsWhitoutInputs= {"getGameState","WhoseTurnIsIt","WhoseTurnIsIt_Id","wasLastPlacementSuccessful","endPlayerTurn", "skipPlayerTurn"};
		boolean isWithInputs=contatinsStrArr(methodsWithInputs,methodName);
		if(isWithInputs)
			output=executeCommand_methodsWithInputs(methodName);
		else
			output=executeCommand_methodsWithNoInputs(methodName);
			
		return output;
	}
	
	
	//helper method that takes care of methods 
	private Object executeCommand_methodsWithInputs(String methodName) throws Exception{
		final int  indexOfTypeString=1;
		final int  indexOfValueString=2;
		//------------------------------------
		String output=null;
		String firstVarLine;
		String []splitString;
		String typeString;
		String valueString;
		//All methods currently have just one variable - therefore we will only read one line and parse it: 
		//firstVarLine=reader.readLine();
		firstVarLine=myObjectStream.readString();
		splitString=firstVarLine.split(":");
		typeString=splitString[indexOfTypeString];
		valueString=splitString[indexOfValueString];
		System.out.println("(executeCommand)server says:firstVarLine is:"+firstVarLine);
		//switch case for all methods:
		switch(methodName) {
		case "getNumOfPointsForPlayer":
			  //this method has two options - so we will check both options:
			  if(typeString.equals("int")||typeString.equals("Integer")) {
				int input=Integer.parseInt(valueString);
				return (Integer)theHost.getNumOfPointsForPlayer(input);
			  }else if(typeString.equals("String")) {
				  System.out.println("(executeCommand)server says:typeString==\"String\"");
				  return (Integer)theHost.getNumOfPointsForPlayer(valueString);
			  }else {
				  throw exception;
			  }
		//////////////////////////////////////
		case "getTilesForPlayer":
			ArrayList<Tile> tilesList;
			 if(typeString.equals("int")||typeString.equals("Integer")) {
					int val=Integer.parseInt(valueString);
					return (ArrayList<Tile>)theHost.getTilesForPlayer(val);
			}
			 else if(typeString.equals("String")) {
					return (ArrayList<Tile>)theHost.getTilesForPlayer(valueString);
			}else {
					 throw exception;
				  }
		case "addAplayer":
			theHost.addAplayer(valueString);
			return null;
			
		case "givePlayerOneTile": 
			int input=Integer.parseInt(valueString);
			theHost.givePlayerOneTile(input);
			return null;
		
		case "placeWordOnBoard"://Challenging !
			if(typeString.equals("Word")||typeString.equals("word")) {
				//we need to ask the guest to send us the object:
				Word wordFromGuest=getWordFromClient();
				theHost.placeWordOnBoard(wordFromGuest);
			}
		
		}
		return output;
	}
	
	
	//Helper method to excecute 
	private Object executeCommand_methodsWithNoInputs(String methodName)throws Exception {
		String output=null;
		String []methodsWhitoutInputs= {"getGameState","WhoseTurnIsIt","WhoseTurnIsIt_Id","wasLastPlacementSuccessful","endPlayerTurn", "skipPlayerTurn"};
		switch(methodName) {
		  	case "getGameState":
				  return theHost.getGameState();
		  	case "WhoseTurnIsIt":
				  return theHost.WhoseTurnIsIt();
			case "WhoseTurnIsIt_Id":
				return (Integer)theHost.WhoseTurnIsIt_Id();
			case "wasLastPlacementSuccessful":
				return theHost.wasLastPlacementSuccessful();
			case "endPlayerTurn":
				 theHost.endPlayerTurn();
				 return null;
			case "skipPlayerTurn":
				 theHost.skipPlayerTurn();
				 return null;
		  	default:
		  		throw exception; // notifying that the parsing of the command failed. 
		  		
		}
		//return output;
	}
	
	@Override
	public void close() {
		// TODO Auto-generated method stub
		System.out.println("****guest client handler :closed was called****");
		if(myObjectStream!=null) {
			try {
			myObjectStream.closeInputStream();
			myObjectStream.closeOutputStreams();
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
			

	}
	
	
	private static boolean contatinsStrArr(String[] arr,String str) {
		for(String s:arr) {
			if(s.equals(str))
				return true;
		}
		return false;
	}
	
	

}
