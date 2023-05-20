package ModelPackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

import baseScrabble.Tile;
import server.ClientHandler;




//This is a class for the MOdelHost to use when getting requests from the guests.
//We will parse the request -and get the method name and variables we will call needed  method
//and then convert the method output to a string and send it back to the client/we will perform the action and 
//send confirmation to the clients. 

public class GuestClientHandler implements ClientHandler {
	 private PrintWriter out=null;
	 private ModelHost theHost;
	 private int numOfPlayers;
	 
	 public static final Exception exception=new Exception("Error parsing Command");
	 
	 
	 public GuestClientHandler() {
		 
	 }
	 
	 public GuestClientHandler(ModelHost theHost,int numOfPlayers) {
		 this.theHost=theHost;
		 this.numOfPlayers=numOfPlayers;
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
	 
	 
	@Override
	public void handleClient(InputStream inFromclient, OutputStream outToClient) {
        //try-with-resources statement -ensures that the buffer will be closed.
    	try (BufferedReader reader = new BufferedReader(new InputStreamReader(inFromclient))) {
    		out=new PrintWriter(outToClient,true);
    		//Firstly we need to get the name of the the player:
    		String nameOfPlayer=ConnectingNewPlayer(out, reader);
    		//now we 'busy wait' until the number of connected players is equal to the defined numOfPlayers:
    		while(this.numOfPlayers!=theHost.getGameState().listOfPlayers.size()) {
    			try {Thread.sleep(500);} //wait for 0.5 seconds then check again
    			catch (InterruptedException e) {e.printStackTrace();}
    		}
    		
    		out.println("player "+nameOfPlayer+" is in the game -good luck!");
    		//Now the player can send commands:
        	
        	while(!theHost.hasGameEnded) {
        		String inputString=reader.readLine();//reading the first line-that has the method name:
        		String methodName=getMethodName(inputString);
	        	try {
	        		Object output=executeCommand(methodName,reader);
	        		//Send output back to the client (i.e. to the model of the guest):
	        		sendOutputToClient(output); //@TODO
	        		
	        	}
	        	catch(Exception e){
	        		
	        	}
        	}
        out.println("Game has ended.Goodbye!");
        //everything will be closed by the server - do not close here!
        	 	
    	}
    	catch(IOException e){
    		
    	}
	}
	
	
	//@TODO
	//Sends string or byte array :
	//if  it is a string the format is :<Type>:<Value> 
	public void sendOutputToClient(Object output){
		//
		if(output instanceof GameState ) {//problematic! maybe try to serialize?
			//send game state over tcIP
		}
		if(output instanceof Integer ) {
			//send string Integer over tcIP
			Integer num=(Integer) output;
			out.println("Integer:"+num.toString());
			
		}
		if(output instanceof ArrayList) {//problematic! maybe try to serialize?
			//send arrayList<tile> over tcp/Ip  //Player
		}
		if(output instanceof Player) {//problematic! maybe try to serialize?
		//send Player over TCP / IP 
		}
		if(output instanceof Boolean) {
			//send Boolean over TCP / IP
			Boolean b1=(Boolean)output;
			//send String to the client:
			out.println("Boolean:"+b1.toString());
			
			}

	}
	
	
	
	//Connects the new player to the game .returns the name of the player
	public String ConnectingNewPlayer(PrintWriter out , BufferedReader reader) throws IOException {
		out.println("Welcome to the game:please send your name");
		//waiting for client to send us the name.
		String inputString=reader.readLine();//reading name line:
		//now we add a player to the game (using host model methods):
		theHost.addAplayer(inputString);
		out.println(inputString+" is connected.waiting for players to connect");
		return inputString;
	}
	
	
	
	
	
	//Method to decode the command received through the tcp/ip connection from the guest.
	//Will parse the string and return the output if there is any. (if the method returns void - null will be returned)
	//Also, if there is a parsing error because of bad input/connection problem we will throw an exception.
	private Object executeCommand(String methodName,BufferedReader reader) throws Exception {
		final int  indexOfTypeString=1;
		final int  indexOfValueString=2;
		//------------------------------------
		Object output=null;
		//switch case for all methods:
		String[] methodsWithInputs= {"getNumOfPointsForPlayer", "getTilesForPlayer","addAplayer","givePlayerOneTile","placeWordOnBoard"};
		String []methodsWhitoutInputs= {"getGameState","WhoseTurnIsIt","WhoseTurnIsIt_Id","wasLastPlacementSuccessful","endPlayerTurn", "skipPlayerTurn"};
		boolean isWithInputs=contatinsStrArr(methodsWithInputs,methodName);
		if(isWithInputs)
			output=executeCommand_methodsWithInputs(methodName,reader);
		else
			output=executeCommand_methodsWithNoInputs(methodName,reader);
			
		return output;
	}
	
	
	//helper method that takes care of methods 
	private Object executeCommand_methodsWithInputs(String methodName,BufferedReader reader) throws Exception{
		final int  indexOfTypeString=1;
		final int  indexOfValueString=2;
		//------------------------------------
		String output=null;
		String firstVarLine;
		String []splitString;
		String typeString;
		String valueString;
		//All methods currently have just one variable - therefore we will only read one line and parse it: 
		firstVarLine=reader.readLine();
		splitString=firstVarLine.split(":");
		typeString=splitString[indexOfTypeString];
		valueString=splitString[indexOfValueString];
		//switch case for all methods:
		switch(methodName) {
		case "getNumOfPointsForPlayer":
			  //this method has two options - so we will check both options:
			  if(typeString=="int") {
				int input=Integer.parseInt(valueString);
				return (Integer)theHost.getNumOfPointsForPlayer(input);
			  }else if(typeString=="String") {
				  return (Integer)theHost.getNumOfPointsForPlayer(valueString);
			  }else {
				  throw exception;
			  }
		//////////////////////////////////////
		case "getTilesForPlayer":
			ArrayList<Tile> tilesList;
			 if(typeString=="int") {
					int val=Integer.parseInt(valueString);
					return (ArrayList<Tile>)theHost.getTilesForPlayer(val);
			}
			 else if(typeString=="String") {
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
			
		
		}
		return output;
	}
	
	
	//Helper method to excecute 
	private Object executeCommand_methodsWithNoInputs(String methodName,BufferedReader reader)throws Exception {
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
		if(out!=null)
			out.close();

	}
	
	
	private static boolean contatinsStrArr(String[] arr,String str) {
		for(String s:arr) {
			if(s.equals(str))
				return true;
		}
		return false;
	}
	

}
