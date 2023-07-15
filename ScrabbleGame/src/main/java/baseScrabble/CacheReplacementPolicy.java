package baseScrabble;

public interface CacheReplacementPolicy{
	void add(String word);
	String remove(); 
}
