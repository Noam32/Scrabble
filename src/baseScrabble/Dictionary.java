package baseScrabble;

import java.io.File;
import java.util.Scanner;

public class Dictionary {
	
	CacheManager exists,notExists;
	BloomFilter bf;
	private String[] fileNames;
	ParIOSearcher searcher;
	
	public Dictionary(String...fileNames) {
		this.fileNames=fileNames;
		exists=new CacheManager(400, new LRU());
		notExists=new CacheManager(100, new LFU());
		bf = new BloomFilter(256, "MD5","SHA1");
		System.out.println("Dictionary constructor says :filenames are "+fileNames[0]);
		for(String fn : fileNames) {
			try {
				Scanner s=new Scanner(new File(fn));
				while(s.hasNext())
					bf.add(s.next());
				s.close();
				System.out.println("Dictionary constructor says:file "+fileNames[0]+" read and closed succefully! ");
			}catch(Exception e) {
				System.out.println("Dictionary constructor says-no file opened yet.Give a valid book name!");
			}
		}		
		searcher=new ParIOSearcher();
	}
	
	public boolean query(String word) {
		if(exists.query(word))
			return true;
		if(notExists.query(word))
			return false;
		
		boolean doesExist = bf.contains(word);
		//System.out.println("Dictionary.query("+word+") says :doesExist= "+doesExist);
		if(doesExist)
			exists.add(word);
		else
			notExists.add(word);
		
		return doesExist;
	}
	
	public boolean challenge(String word) {
		boolean doesExist = searcher.search(word, fileNames);
		//System.out.println("*Dictionary.challenge*("+word+") says :doesExist= "+doesExist);
		if(doesExist)
			exists.add(word);
		else
			notExists.add(word);
		
		return doesExist;
	}
	
	public void close() {
		searcher.stop();
	}

}
