package server;

//import java.util.Dictionary;
import java.util.HashMap;

import baseScrabble.Dictionary;

/**
A class that manages a collection of dictionaries and provides methods to query and challenge words in those dictionaries.
Class Name: DictionaryManager
Input: None
Output: none
Functionality:
The DictionaryManager class manages a collection of dictionaries represented by a HashMap.
The query method checks if a given word is present in any of the dictionaries.
The challenge method checks if a given word is not present in any of the dictionaries.
The getSize method returns the size of the dictionary collection.
The addToMap method adds a dictionary to the collection if it does not already exist.
The DictionaryManager class is implemented as a Singleton to ensure that only one instance of the class exists.
*/
public class DictionaryManager {
	HashMap<String, Dictionary> dict=new HashMap<String, Dictionary>();
	private static DictionaryManager instance = null;
	
	/**
	Function Name: constructor
	Input: None
	Output: none
	Functionality:
	Initializes a HashMap to store the dictionaries.
	The constructor is private to prevent the creation of new instances of the class, ensuring that only one instance exists.
	Initializes an instance of the class using the Singleton design pattern.
	*/
	DictionaryManager() {
		//instance=new DictionaryManager();
		dict = new HashMap<String, Dictionary>();
	}
	
	/**
	Function Name: query
	Input: An array of Strings representing the names of the dictionaries to
	       query and  in the last place a  String representing the word to query.
	Output: A Boolean value indicating whether the word is present in any of the dictionaries.
	Functionality:
	Adds the dictionaries to the HashMap if they are not already present.
	Iterates through the array of dictionary names and retrieves each dictionary from the HashMap.
	Calls the query method of each dictionary with the given
	word and sets a Boolean value to true if the word is present in any of the dictionaries.
	Returns the Boolean value.
	*/
	public boolean query(String... args) {
		addToMap(args);
		Boolean b=false;
		String word=args[args.length-1];
		for (int i=0;i<args.length-1;i++) {
			Dictionary d = dict.get(args[i]);
			if(d.query(word))
				b=true;
		}
		return b;
	}

	/**
	Function Name: challenge
	Input: An array of Strings representing the names of the dictionaries to challenge 
	and in the last place a String representing the word to challenge.
	Output: A Boolean value indicating whether the word is not present in any of the dictionaries.
	Functionality:
	Adds the dictionaries to the HashMap if they are not already present.
	Iterates through the array of dictionary names and retrieves each dictionary from the HashMap.
	Calls the challenge method of each dictionary with the given word and sets a Boolean value to true if the word is not present in any of the dictionaries.
	Returns the Boolean value.
	*/
	public boolean challenge(String... args) {
		addToMap(args);
		Boolean b=false;
		String word=args[args.length-1];
		for (int i=0;i<args.length-1;i++) {
			Dictionary d = dict.get(args[i]);
			if(d.challenge(word))
				b=true;
		}
		return b;
	}

	/**
	Function Name: getSize
	Input: None
	Output: An integer value representing the number of dictionaries in the collection.
	Functionality:
	Returns the size of the HashMap containing the dictionaries.
	*/
	public int getSize() {
		// TODO Auto-generated method stub
		return dict.size();
	}
	
	/**
	Function Name: get
	Input: None
	Output: An instance of the DictionaryManager class
	Functionality:
	Implements the Singleton design pattern to ensure that only one instance of the class exists.
	Returns the instance of the class.
	*/
	public static DictionaryManager get() {
		if (instance==null) {
			instance = new DictionaryManager();
		}
		return instance;
	}
	
	/**
	Function Name: addToMap
	Input: An array of Strings representing the names of the dictionaries to add to the collection.
	Output: None
	Functionality:
	Iterates through the array of dictionary names and adds each 
	dictionary to the HashMap if it does not already exist in the collection.
	*/
	private void addToMap(String... args) {
		//only adding until the length-1 string 
		//because the last string isn't a book name but a word to search! 
		for(int i=0;i<args.length-1;i++) {
			if(!dict.containsKey(args[i])) {
				Dictionary d = new Dictionary(args[i]);
				dict.put(args[i], d);
			}
		}
	}

}
