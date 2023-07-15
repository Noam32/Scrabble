package mypackage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoClient;
import com.mongodb.MongoException;
import org.bson.Document;

public class MyPlayer implements Serializable {
	private static final long serialVersionUID = 1L;
	public static int objectCounter=0;
	String name;
	int id;
	ArrayList<Integer> numbers;
	
	
	
	public MyPlayer(){
		this.numbers=new ArrayList<Integer>();
		Random rand =new Random();
		numbers.add(9);
		numbers.add(11);
		this.numbers.add((Integer)rand.nextInt());
		this.numbers.add((Integer)rand.nextInt());
		this.numbers.add((Integer)rand.nextInt());
		this.numbers.add((Integer)rand.nextInt());
	}
	
	@Override
	public String toString() {
		return "MyPlayer [name=" + name + ", id=" + id +",numbers="+numbers.toString()+ "]";
		
	}

	public Document toDocument( ) {
		//Take all data members and append to Document;
		Document document = new Document("serialVersionUID", serialVersionUID);
		document.append("name",name);
		document.append("id",id);
		document.append("numbers", numbers);
		return document;
	}
	
	public static MyPlayer fromDocument(Document d) {
		MyPlayer p=new MyPlayer();
		p.name=d.getString("name");
		p.id=d.getInteger("id");
		p.numbers= (ArrayList<Integer>) d.getList("numbers",Integer.class);
		return p;
		
	}
	
	
	//public static intListFromDoc()
	
	
	public MyPlayer(String name) {
		this.numbers=new ArrayList<Integer>();
		Random rand =new Random();
		numbers.add(9);
		numbers.add(11);
		this.numbers.add((Integer)rand.nextInt());
		this.numbers.add((Integer)rand.nextInt());
		this.numbers.add((Integer)rand.nextInt());
		this.numbers.add((Integer)rand.nextInt());
		this.name=name;
		objectCounter++;
		this.id=objectCounter;
	}
	
	
	
	
	
	
	
}
