package ModelPackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

import server.ClientHandler;

//This c

public class GuestClientHandler implements ClientHandler {
	 private PrintWriter out=null;
	 
	 public GuestClientHandler() {
		 
	 }
	 
	@Override
	public void handleClient(InputStream inFromclient, OutputStream outToClient) {
        //try-with-resources statement -ensures that the buffer will be closed.
    	try (BufferedReader reader = new BufferedReader(new InputStreamReader(inFromclient))) {
        	out=new PrintWriter(outToClient,true);
        	String inputString=reader.readLine();//reading the whole request
    	}
    	catch(IOException e){
    		
    	}
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

}
