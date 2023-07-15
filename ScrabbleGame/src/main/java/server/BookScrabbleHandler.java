package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;


public class BookScrabbleHandler implements ClientHandler {
    private final DictionaryManager dictionaryManager;
    private PrintWriter out=null;
    
    
    //constructor
    public BookScrabbleHandler() {
		this.dictionaryManager = new DictionaryManager();
    	
    }
    
    
    //constructor 
    public BookScrabbleHandler(DictionaryManager dictionaryManager) {
        this.dictionaryManager = dictionaryManager;
    }

    @Override
    //This method handles client's request by using methods from DictionaryManager class.
    //It receives stream objects for input and output -reads the input string then..
    //calls DictionaryManager methods accordingly and then we send the output string to the user .
    public void handleClient(InputStream inFromClient, OutputStream outToClient) {
        //try-with-resources statement -ensures that the buffer will be closed.
    	try (BufferedReader reader = new BufferedReader(new InputStreamReader(inFromClient))) {
        	out=new PrintWriter(outToClient,true);
        	String inputString=reader.readLine();//reading the whole request
        	char firstChar=inputString.charAt(0);//reading the first char 'Q' or 'C'.
        	inputString=inputString.substring(1);//removing the first char from the string.
        	String [] strings=inputString.split(",");//splitting strings with delimiter ','.
        	//now we query/challenge according to the first character :
        	boolean result;
        	if(firstChar=='Q') {
        		result = dictionaryManager.query(strings);
        	}
        	else if(firstChar=='C') {
        		result = dictionaryManager.challenge(strings);
        	}
        	else {
        		//notifying the client that the request is invalid: 
        		out.println("Illegal query type use 'C' or 'Q'");//
        		return;
        	}
        	//now we send the result to the client:
        	String outputString;
        	if(result==true)
        		outputString="true";
        	else
        		outputString="false";
        	out.println(outputString);
        	
        } catch (IOException e) {
            System.err.println("Error handling client: " + e.getMessage());
        }
    }

    @Override
    public void close() {
        // Do nothing-maybe close the stream or socket or printeWriter??
    	out.close();
    	
    }


    
//  

    
    
    
}











