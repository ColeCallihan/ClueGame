/*
 * @author Cole Callihan, Carter Pasqualini
 * 
 * CardType class that is an enumerator for the types of cards
 */ 

package clueGame;

public enum CardType {
	PERSON("PERSON"), WEAPON("WEAPON"), ROOM("ROOM");
	
	//String equivalent value of the enumerator
	private String value;
	
	CardType(String value) {
		this.value = value;
	}
	
	/*
	 * Returns the string equivalent value of the enumerator
	 */
	public String value() {
		return value;
	}
}
