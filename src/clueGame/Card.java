package clueGame;

public class Card {

	private String cardName;
	private CardType cardType;
	
	public Card(String currentCard, CardType cardEnum) {
		this.cardName = currentCard;
		this.cardType = cardEnum;
	}

	public String getCardName() {
		return cardName;
	}

	public CardType getCardType() {
		return cardType;
	}
	
	/*
	@Override
	public boolean equals() {
		
	}
	*/
}
