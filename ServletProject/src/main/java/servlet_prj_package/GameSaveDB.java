package servlet_prj_package;
/*
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
*/
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

////
import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

///
//Servlet that will take care of 
public class GameSaveDB extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static String []legalActions={"getSaveDocument","getScoreBoard","getAllGameSaveNames" };
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GameSaveDB() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    
    //The expected http URI is :http://localhost:<portNumber>/servlet_prj/GameSaveDB?action=<actionStr>&name=<stringnameOfGameSave>
    //for getAllGameSaveNames:http://localhost:8082/servlet_prj/GameSaveDB?action=getAllGameSaveNames&name=all
    //actions : {getSaveDocument,getScoreBoard,getAllGameSaveNames }
    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
    	String action =request.getParameter("action");
    	String nameOfGameSave = request.getParameter("name");
    	PrintWriter pw=response.getWriter();
    	if(action==null ||nameOfGameSave==null ) {
    		if(action==null) {
    			pw.println("invalid get URI : action field is not in URI . URI format is:http://localhost:<portNumber>/servlet_prj/GameSaveDB?action=<actionStr>&name=<stringnameOfGameSave>");
    			return;
    		}
    		if(!isActionStringLegal(action)) {
    			pw.println("invalid action string -please try again:actions supported are {getSaveDocument,getScoreBoard,getAllGameSaveNames}");
    			return;
    		}
    		if(nameOfGameSave==null )
    		pw.println("invalid get URI : name field is required  but not in URI . URI format is:http://localhost:<portNumber>/servlet_prj/GameSaveDB?action=<actionStr>&name=<stringnameOfGameSave> \name field can be left empty when using getAllGameSaveNames" );
    		return ;
    	}
		System.out.println("action param is :"+action+".");
		if(nameOfGameSave!=null)
			System.out.println(" name param is: "+nameOfGameSave+".");
    	
    	switch(action) {
    	case "getSaveDocument":
    		GetGameSaveAction(request,response);
    		break;
    	case "getScoreBoard":
    		GetScoreBoard(request,response);
    		break;
    	case "getAllGameSaveNames":
    		getAllGameSaveNames(request, response);
    		break;
    		
    	default:
    		pw.println("invalid action string -please try again:actions supported are {getSaveDocument,getScoreBoard,getAllGameSaveNames}");
    	}
    		
    		
    	
		//pw.println("");
		System.out.println("");
	}
    //checks if a string is in the legalActions array:
    private boolean isActionStringLegal(String action) {
    	for(int i=0;i<legalActions.length;i++) {
    		if(legalActions[i].equals(action))
    			return true;
    	}
    	return false;
    }
    
    
    protected void GetScoreBoard(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
    	String nameOfGameSave = request.getParameter("name");
    	PrintWriter pw=response.getWriter();
    	String str_scoreBoard=MongoMethodsForServlet.getScoreBoardOfGame(nameOfGameSave);
    	if(str_scoreBoard==null) {
    		pw.println("Game save not found: "+nameOfGameSave);
    	}else {
    		
    		pw.println(str_scoreBoard.toString());
    	}
    	
    }
    
    protected void GetGameSaveAction(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
    	String nameOfGameSave = request.getParameter("name");
    	PrintWriter pw=response.getWriter();
    	Document gameSaveDoc=MongoMethodsForServlet.getGameSaveFromServer(nameOfGameSave);
    	if(gameSaveDoc==null) {
    		pw.println("Game save not found : "+nameOfGameSave);
    	}else {
    		pw.println(gameSaveDoc.toJson());
    	}
    }
    protected void getAllGameSaveNames(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
    	PrintWriter pw=response.getWriter();
    	String strAllNames=MongoMethodsForServlet.getAllNamesOfGameSaves();
    	System.out.println("getAllGameSaveNames: res is:");
    	System.out.println(strAllNames);
    	pw.println(strAllNames);
    }
    
    
    
    
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
    	System.out.println("GameSaveDB servlet recieved a post request:"+request.getQueryString());
    	PrintWriter pw=response.getWriter();
    	pw.println("you sent a post reqest");
	}

}


/*
 *  <dependency>
    <groupId>javax.servlet</groupId>
    <artifactId>javax.servlet-api</artifactId>
    <version>3.1.0</version>
    <scope>provided</scope>
</dependency>
 */


