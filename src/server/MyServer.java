package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class MyServer {
	
	    int port;
	    boolean stop;
	    ClientHandler ch;//Reference to interface ClientHandler
	    int maxThreads;//maximum number of Threads The server is allow to open
	    ServerSocket server;
	    ExecutorService threadPool;

	    public MyServer(int port, ClientHandler ch, int maxThreads) {
	        this.port = port;
	        this.ch = ch;
	        this.maxThreads = maxThreads;
	        this.threadPool = Executors.newFixedThreadPool(maxThreads);
	    }

	    public void start() {
	        stop = false;
	        threadPool.execute(() -> startserver());
	    }
	    
	    
	    public void startserver() {
	        try {
	            server = new ServerSocket(port);//server socket creation
	            server.setSoTimeout(6*10000);//6*100000ms=60sec - waiting 60 seconds for client to connect
	            while (!stop) {
	                try {
	                    Socket client = server.accept();
	                    this.handleClient(client);
	                    //threadPool.execute(() -> handleClient(client));
	                	}
	                catch (SocketTimeoutException e) {
	                    // ignore and continue waiting for connections
	                	//System.err.println("time out exception in MyServer.startserver()");
	                }
	                catch(Exception e) {
	                	e.printStackTrace();
	                }
	            }
	        }
	        catch (IOException e) {
	            e.printStackTrace();
	        } 
	        finally {
	            close();
	        }
	    }

	    private void handleClient(Socket client) throws Exception {
	        try {
	        	System.out.println("**My server:handleClient:trying to run ch.handleClient**");
	            ch.handleClient(client.getInputStream(), client.getOutputStream());
	            //System.out.println("**My server:HERE!!!!**");
	        } catch (IOException e) {
	            System.out.println("**My server:handleClient:IO exception tring to run handleClient**");
	        	e.printStackTrace();
	        }
	        //Here we are closing the interaction with the client after 1 query/challenge as instructed.
	        finally {
	            try {
	                ch.close();
	                client.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    }

	    public void stop() {
	        System.out.println("Server was stopped .The client handler is of type :"+ch.getClass().getSimpleName());
	    	stop = true;
	    }

		// This method shuts down the thread pool and closes the server socket.
	    public void close() {
	        stop();//ensure that the server stops listening for new connections before shutting down the thread pool 
	        System.out.println("Server is closed .The client handler was of type :"+ch.getClass().getSimpleName());
	        try {
	            threadPool.shutdown();
	            if(server.isClosed())
	            	server.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	}


