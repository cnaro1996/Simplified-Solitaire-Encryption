package solitaire;

import java.io.IOException;
import java.util.Scanner;
import java.util.Random;

/**
 * This class implements a simplified version of Bruce Schneier's Solitaire Encryption algorithm.
 * 
 * @author RU NB CS112
 */
public class Solitaire {
	
	/**
	 * Circular linked list that is the deck of cards for encryption
	 */
	CardNode deckRear;
	
	/**
	 * Makes a shuffled deck of cards for encryption. The deck is stored in a circular
	 * linked list, whose last node is pointed to by the field deckRear
	 */
	public void makeDeck() {
		// start with an array of 1..28 for easy shuffling
		int[] cardValues = new int[28];
		// assign values from 1 to 28
		for (int i=0; i < cardValues.length; i++) {
			cardValues[i] = i+1;
		}
		
		// shuffle the cards
		Random randgen = new Random();
 	        for (int i = 0; i < cardValues.length; i++) {
	            int other = randgen.nextInt(28);
	            int temp = cardValues[i];
	            cardValues[i] = cardValues[other];
	            cardValues[other] = temp;
	        }
	     
	    // create a circular linked list from this deck and make deckRear point to its last node
	    CardNode cn = new CardNode();
	    cn.cardValue = cardValues[0];
	    cn.next = cn;
	    deckRear = cn;
	    for (int i=1; i < cardValues.length; i++) {
	    	cn = new CardNode();
	    	cn.cardValue = cardValues[i];
	    	cn.next = deckRear.next;
	    	deckRear.next = cn;
	    	deckRear = cn;
	    }
	}
	
	/**
	 * Makes a circular linked list deck out of values read from scanner.
	 */
	public void makeDeck(Scanner scanner) 
	throws IOException {
		CardNode cn = null;
		if (scanner.hasNextInt()) {
			cn = new CardNode();
		    cn.cardValue = scanner.nextInt();
		    cn.next = cn;
		    deckRear = cn;
		}
		while (scanner.hasNextInt()) {
			cn = new CardNode();
	    	cn.cardValue = scanner.nextInt();
	    	cn.next = deckRear.next;
	    	deckRear.next = cn;
	    	deckRear = cn;
		}
	    //CODE BETWEEN THESE LINES ARE USED FOR TESTING PURPOSES--------------------------------!
		
		printList(deckRear);
	    System.out.println("^Original Deck");
	    
	    jokerA();
	    printList(deckRear);
	    System.out.println("	^JokerA");
	    
	    jokerB();
	    printList(deckRear);
	    System.out.println("			^JokerB");
	    
	    countCut();
	    printList(deckRear);
	    System.out.println("					^CountCut");
	    
	    tripleCut();
	    printList(deckRear);
	    System.out.println("							^TripleCut");
	    
	    //CODE BETWEEN THESE LINES ARE USED FOR TESTING PURPOSES--------------------------------!
	}
	
	/**
	 * Implements Step 1 - Joker A - on the deck.
	 */
	void jokerA() {
		//find jokerA
		CardNode newNode = deckRear;
		while(newNode.cardValue != 27){
			newNode = newNode.next;
		}
		//Set joker A's card-value to the next card's value & assign next card's value to 27
		newNode.cardValue = newNode.next.cardValue;
		newNode.next.cardValue = 27;
	}
	
	/**
	 * Implements Step 2 - Joker B - on the deck.
	 */
	void jokerB() {
		//find JokerB
		CardNode JokerB = deckRear;
		while(JokerB.cardValue != 28){
			JokerB = JokerB.next;
		}
		//Swap the values of JokerB & the next card & update JokerB's pointer to point to it's new node.
		JokerB.cardValue = JokerB.next.cardValue;
		JokerB.next.cardValue = 28;
		JokerB = JokerB.next;
		//Repeat once more to swap the next two cards.
		JokerB.cardValue = JokerB.next.cardValue;
		JokerB.next.cardValue = 28;
	}
	
	/**
	 * Implements Step 3 - Triple Cut - on the deck.
	 */
	void tripleCut() { //<-------------------------------------------------------------NEEDS DEBUGGING!!!
		CardNode newNode = deckRear;
		//Case 1: No cards before first joker.
		//^^^^^MUST BE DEBUGGED. CURRENTLY SWAPS VALUES, MUST MOVE JOKER 2 TO END OF LIST INSTEAD.
		if(newNode.next.cardValue == 27 || newNode.next.cardValue == 28){
			//Find second joker.
			newNode = newNode.next.next;
			while(true){
				if(newNode.cardValue != 28){
					if(newNode.cardValue != 27){
						newNode = newNode.next;
					}else break;
				}else break;
			}	
			//newNode is now joker 2, swap values of joker 2 and rear (this code accounts for if the last card is also a joker).
			int j = newNode.cardValue; //joker2 card value
			newNode.cardValue = deckRear.cardValue;
			deckRear.cardValue = j;
			return;
		}
		//Case 2: No cards after second joker. (If rear is a joker, then there are no cards after joker 2.)
		//^^^^^MUST BE DEBUGGED. CURRENTLY SWAPS VALUES, MUST MOVE JOKER 1 TO FRONT OF LIST INSTEAD.
		if(newNode.cardValue == 27 || newNode.cardValue == 28){
			//Find joker 1.
			newNode = newNode.next; //Skipping past rear (or joker 2).
			while(true){
				if(newNode.cardValue != 28){
					if(newNode.cardValue != 27){
						newNode = newNode.next;
					}else break;
				}else break;
			}
			//Joker 1 found. Swap values of Joker 1 and first card.
			int j = newNode.cardValue; //Joker 1's card value.
			newNode.cardValue = deckRear.next.cardValue;
			deckRear.next.cardValue = j;
			return;
		}
		//Case 3: All other scenarios.
		//Find Joker 1 + joker 1's previous node.
		CardNode J1prev = null;
		while(true){
			if(newNode.cardValue != 28){
				if(newNode.cardValue != 27){
					J1prev = newNode;
					newNode = newNode.next;
				}else break;
			}else break;
		}
		CardNode Joker1 = newNode;
		//Joker 1 & J1 prev node found.
		//Find Joker2
		newNode = newNode.next;
		while(true){
			if(newNode.cardValue != 28){
				if(newNode.cardValue != 27){
					newNode = newNode.next;
				}else break;
			}else break;
		}
		CardNode Joker2 = newNode;
		CardNode Joker2next = Joker2.next;
		CardNode front = deckRear.next;
		//Now rearrange list.
		//Case 3.a: The card before Joker1 is the front.
		if(J1prev.equals(front)){
			deckRear.next = null;
			Joker2.next = front;
			deckRear.next = Joker1;
			front.next = Joker2next;
			deckRear = front;
			return;
		}
		//Case 3.b: The card after Joker2 is the rear.
		if(Joker2next.equals(deckRear)){
			deckRear.next = null;
			Joker2.next = front;
			deckRear.next = Joker1;
			J1prev.next = deckRear;
			deckRear = J1prev;
			return;
		}
		//Case 3.c: The card before Joker1 is not the front &&
		// the card after Joker2 is not the rear
		deckRear.next = null;
		Joker2.next = front;
		deckRear.next = Joker1;
		J1prev.next = Joker2next;
		deckRear = J1prev;
	}
	
	/**
	 * Implements Step 4 - Count Cut - on the deck.
	 */
	void countCut() {
		//Cases 1 & 2.
		if(deckRear.cardValue ==  28 || deckRear.cardValue ==  27){
			return;
		}
		//Pointer declarations
		int val = deckRear.cardValue; //store value of rear card
		CardNode front = deckRear.next; //front pointer
		CardNode end = deckRear.next; //the last node of the set of numbers to be moved.
		//find rear's previous node.
		CardNode temp = deckRear.next;
		for(int i = 0; i < 26; i++){ //loop 26 times to get second to last node
			temp = temp.next;
		}
		CardNode rearDeckPrev = temp;
		//Case 3.
		if(val == 1){
			deckRear.next = front.next;
			front.next = deckRear;
			rearDeckPrev.next = front;
			return;
		}
		//Case 4.
		//Set end pointer.
		for(int i = 0; i < (val-1); i++){
			end = end.next;
		}
		//Move linked list.
		CardNode endnext = end.next;
		end.next = deckRear;
		deckRear.next = endnext;
		rearDeckPrev.next = front;
		return;
	}
	
	/**
	 * Gets a key. Calls the four steps - Joker A, Joker B, Triple Cut, Count Cut, then
	 * counts down based on the value of the first card and extracts the next card value 
	 * as key. But if that value is 27 or 28, repeats the whole process (Joker A through Count Cut)
	 * on the latest (current) deck, until a value less than or equal to 26 is found, which is then returned.
	 * 
	 * @return Key between 1 and 26
	 */
	int getKey() {
		final boolean notdone = true;
		CardNode key = null; //change back to = null
		while(notdone){
			jokerA();
			jokerB();
			tripleCut();
			countCut();
			if(deckRear.next.cardValue == 28 || deckRear.next.cardValue == 27){
				return deckRear.cardValue;
			}
			int val = deckRear.next.cardValue;
			CardNode temp = deckRear.next;
			//Counting down (val) cards from the first
			for(int i = 0; i < (val-1); i++){
				temp = temp.next;
			}
			if(temp.next.cardValue == 27 || temp.next.cardValue == 28){
				continue;
			}
			key = temp.next;
			break;
		}
		return key.cardValue;
	}
	
	/**
	 * Utility method that prints a circular linked list, given its rear pointer
	 * 
	 * @param rear Rear pointer
	 */
	private static void printList(CardNode rear) {
		if (rear == null) { 
			return;
		}
		System.out.print(rear.next.cardValue);
		CardNode ptr = rear.next;
		do {
			ptr = ptr.next;
			System.out.print("," + ptr.cardValue);
		} while (ptr != rear);
		System.out.println("\n");
	}

	/**
	 * Encrypts a message, ignores all characters except upper case letters
	 * 
	 * @param message Message to be encrypted
	 * @return Encrypted message, a sequence of upper case letters only
	 */
	public String encrypt(String message) {	
		String oldMessage = "";				
		String newMessage = "";				
		//Remove non-alphabetic characters from string.
		for(int i = 0; i <= message.length()-1; i++){
			if(Character.isLetter(message.charAt(i))){
				oldMessage = oldMessage + Character.toString(Character.toUpperCase(message.charAt(i)));
			}
		}
		//Add a key to each letter of the string, if sum is greater than
		//26, subtract 26 from sum. convert number into a letter. Add
		//letter to new string to form encryption sequence.
		for(int i = 0; i <= oldMessage.length()-1; i++){
			int position = oldMessage.charAt(i) - 'A' + 1; //alphabetic position, -'A'+1 used for casting.
			position = position + getKey();
			if(position > 26){
				position = position - 26;
			}
			char letter = (char)(position - 1 + 'A');
			newMessage = newMessage + Character.toString(letter);
		}
	    return newMessage;
	}
	
	/**
	 * Decrypts a message, which consists of upper case letters only
	 * 
	 * @param message Message to be decrypted
	 * @return Decrypted message, a sequence of upper case letters only
	 */
	public String decrypt(String message) {	
		String newMessage = "";
		for(int i = 0; i <= message.length()-1; i++){
			int position = message.charAt(i) - 'A' + 1; //alphabetic position, -'A'+1 used for casting.
			int key = getKey();
			if(position <= key){
				position = position + 26;
			}
			position = position - key;
			
			char letter = (char)(position - 1 + 'A');
			newMessage = newMessage + Character.toString(letter);
		}
	    return newMessage;
	}
}
