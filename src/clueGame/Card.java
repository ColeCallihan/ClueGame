/*
 * @author Cole Callihan, Carter Pasqualini
 * 
 * Card class to create cards with their names and card types
 */ 

package clueGame;

public class Card{

	//instance variables for the name and card type
	private String cardName;
	private CardType cardType;
	
	/*
	 * Constructor to set the card name and type
	 */
	public Card(String currentCard, CardType cardEnum) {
		this.cardName = currentCard;
		this.cardType = cardEnum;
	}

	/*
	 * Returns the name of the card
	 */
	public String getCardName() {
		return cardName;
	}

	/*
	 * Returns the type of the card
	 */
	public CardType getCardType() {
		return cardType;
	}
	
	/*
	 * If the cards share the same name and type, they are equal
	 */
	public boolean equals(Card compareCard) {
		if(cardName.equals(compareCard.getCardName()) && cardType == compareCard.getCardType()){
			return true;
		}
		else {
			return false;
		}
	}
}
