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
		 }
		 //Output
		 protected void initOutputStreams(OutputStream outputStream) throws IOException {
			  os = outputStream;
		     oos = new ObjectOutputStream(os);
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
		 }
		 
		 protected void initInputStream(InputStream inputStream) throws IOException {
			  input_stream=inputStream;
			  objectInputStream = new ObjectInputStream(input_stream);
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