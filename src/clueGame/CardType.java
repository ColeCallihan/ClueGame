package clueGame;

public enum CardType {
	PERSON("PERSON"), WEAPON("WEAPON"), ROOM("ROOM");
	
	private String value;
	
	CardType(String value) {
		this.value = value;
	}
	
	public String value() {
		return value;
	}
}
