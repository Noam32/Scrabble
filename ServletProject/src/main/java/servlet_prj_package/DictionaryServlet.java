package servlet_prj_package;

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
/**
 * Servlet implementation class DictionaryServlet
 */
public class DictionaryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	//SETUP:
		public static int portOfDictionaryTCP_IP_Server=8000;   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DictionaryServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 * Expected URI:http://localhost:<port>/servlet_prj/DictionaryServlet?challenge=<string_word>
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
				String challngeString = request.getParameter("challenge");
				System.out.println("param is "+challngeString);
				//response.getWriter().append("Served at: ").append(request.getContextPath());
				PrintWriter pw=response.getWriter();
				
				if(challngeString==null) {
					pw.println("invalid URI :no challenge field in URI.Expected URI:http://localhost:<port>/servlet_prj/DictionaryServlet?challenge=<string_word> ");
					return;
				}
				
				Boolean b1=false;
				try {
					b1 = runClientToDictionaryServer(portOfDictionaryTCP_IP_Server,'C',challngeString);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("Error communicating with dictionay -please check if the dictionary server is running");
					e.printStackTrace();
					pw.println("communication_error with dictionary server - please make sure that the server is up!");
				}
				//response.setContentType("text/html");
				//pw.println("<h1>hi hello world!<h1>");
				pw.println(b1.toString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	

	//This method creates a client that will communicate with the Dictionary 'remote' server.
	//Q_or_C = 'Q' for query and 'C' for challenge
	//throws exception if connection to the DictionaryServer failed !
	public static Boolean runClientToDictionaryServer(int port,char Q_or_C ,String stringTosearch) throws Exception{
		String bookNames="mobydick.txt";
		try {
			Socket server=new Socket("localhost",port);
			PrintWriter out=new PrintWriter(server.getOutputStream());
			Scanner in=new Scanner(server.getInputStream());
			//template is : "Q,bookNames1,bookName2,...,stringTosearch"
			String stringToSend=Q_or_C+","+bookNames+","+stringTosearch;
			System.out.println("runClientToDictionaryServer:sending \""+ stringToSend+"\"");
			//We send 2 string - one is all upper case - and one is all lower case 
			//- if one of the challenges returns true-we return true:
			out.println(stringToSend);//here we are sending the query/challenge string to the Client handler server
			out.flush();
			//System.out.println("in.hasNext()= "  +in.hasNext());
			String res=in.next();//here we are receiving the query/challenge *result* string from the Client handler server
			Boolean boolRes=Boolean.parseBoolean(res);
			//closing :
			in.close();
			out.close();
			server.close();
			///returning :
			return boolRes;
		} catch (IOException e) {
			System.out.println("your code ran into an IOException ");
			e.printStackTrace();
			System.out.println("is port available = " +isTcpPortAvailable(port) );
			throw e;//query failed
		}
	}


	private static boolean isTcpPortAvailable(int port) {
		try (ServerSocket serverSocket = new ServerSocket()) {
			// setReuseAddress(false) is required only on macOS,
			// otherwise the code will not work correctly on that platform
			serverSocket.setReuseAddress(false);
			serverSocket.bind(new InetSocketAddress(InetAddress.getByName("localhost"), port), 1);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	
	
	
	
	
	
	
	
	
}
