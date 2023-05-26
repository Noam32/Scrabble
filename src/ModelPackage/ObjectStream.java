package ModelPackage;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

//class responsible for serializing objects:
	 public class ObjectStream{
		 //output;
		 OutputStream os ;
		 ObjectOutputStream oos;
		 //input:
		 InputStream  input_stream;
		 ObjectInputStream objectInputStream;
		 //socket to init objects:
		 Socket mySocket;
		 
		 
		 
		 public ObjectStream(Socket s) {
			 this.mySocket=s;
		 }
		 protected ObjectStream() {
			 
		 }
		 
		 
		 //Output
		 public void initOutputStreams() throws IOException {
			  os = mySocket.getOutputStream();
		     oos = new ObjectOutputStream(os);
		     System.out.println("initOutputStreams() created - probably the client");
		 }
		 //Output
		 protected void initOutputStreams(OutputStream outputStream) throws IOException {
			 System.out.println("initOutputStreams called"); 
			 os = outputStream;
		     oos = new ObjectOutputStream(os);
		     System.out.println("ObjectOutputStream created-- probably the server");
		 }
		 public void writeObjectOut(Object obj) throws IOException {
			 oos.writeObject(obj);
		 }
		 
		 public void sendString(String str) throws IOException {
			 oos.writeObject(str);
		 }
		
		 
		 public void closeOutputStreams() throws IOException{
				oos.close(); //do not use ! will close socket!
				os.close();
		 }
		 
		 //Input:
		 
		 public void initInputStream() throws IOException { 
			 input_stream=mySocket.getInputStream(); 
			  objectInputStream = new ObjectInputStream(input_stream);
			  System.out.println("initInputStream() -has been create (probably client)");
		 }
		 
		 protected void initInputStream(InputStream inputStream) throws IOException {
			 System.out.println("initInputStream was called -probably server"); 
			 input_stream=inputStream;
			  System.out.println("initInputStream was called ");
			  objectInputStream = new ObjectInputStream(input_stream);
			  System.out.println("initInputStream finished ");
		 }
		 
		 public Object readObject() throws ClassNotFoundException, IOException {
			 Object obj=objectInputStream.readObject();
			 return obj;
		 }
		 public String readString() throws ClassNotFoundException,IOException{
			 return (String)this.readObject();
		 }
		 
		 public void closeInputStream() throws IOException{
			 //input_stream.close(); //do not invoke -will close the Server!
			 objectInputStream.close();
		 }
		 
		 
		 
		 
	 }