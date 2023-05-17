package ModelPackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

import server.ClientHandler;




//This is a class for the MOdelHost to use when getting requests from the guests.
//We will parse the request -and get the method name and variables we will call needed  method
//and then convert the method output to a string and send it back to the client/we will perform the action and 
//send confirmation to the clients. 

public class GuestClientHandler implements ClientHandler {
	 private PrintWriter out=null;
	 private ModelHost theHost;
	 
	 public static final Exception exception=new Exception("Error parsing Command");
	 
	 
	 public GuestClientHandler() {
		 
	 }
	 
	 public GuestClientHandler(ModelHost theHost) {
		 this.theHost=theHost;
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
        	String inputString=reader.readLine();//reading the first line
        	String methodName=getMethodName(inputString);
        	try {
        		Object output=executeCommand(methodName,reader);
        	}
        	catch(Exception e){
        		
        	}

        	
        	
    	}
    	catch(IOException e){
    		
    	}
	}
	
	
	//private get
	
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
	
	
	
	private Object executeCommand_methodsWithInputs(String methodName,BufferedReader reader) throws Exception{
		final int  indexOfTypeString=1;
		final int  indexOfValueString=2;
		//------------------------------------
		String output=null;
		String firstVarLine;
		String []splitString;
		String typeString;
		String valueString;
		//switch case for all methods:
		switch(methodName) {
		case "getNumOfPointsForPlayer":
			  //read next line:
			  firstVarLine=reader.readLine();
			  splitString=firstVarLine.split(":");
			  typeString=splitString[indexOfTypeString];
			  valueString=splitString[indexOfValueString];
			  //this method has two options - so i am 
			  if(typeString=="int") {
				int val=Integer.parseInt(valueString);
				return (Integer)theHost.getNumOfPointsForPlayer(val);
			  }else if(typeString=="String") {
				  return (Integer)theHost.getNumOfPointsForPlayer(valueString);
			  }else {
				  throw exception;
			  }
		
		}
		return output;
	}
	
	

	private Object executeCommand_methodsWithNoInputs(String methodName,BufferedReader reader)throws Exception {
		String output=null;
		switch(methodName) {
		  	case "getGameState":
			  return theHost.getGameState();
		  	case "a":
		  		
		  		
		  		
		  	case "b":
		  		
		  	default:
		  		
		}
		return output;
	}
	
	
	@Override
	public void close() {
		// TODO Auto-generated method stub

	}
	
	
	private static boolean contatinsStrArr(String[] arr,String str) {
		for(String s:arr) {
			if(s.equals(str))
				return true;
		}
		return false;
	}
	

}
