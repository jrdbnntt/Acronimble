/**
 * The card that contains an image to display and a word to guess.
 * TODO: make this an activity/view that manages itself
 * 
 * @author Jared
 *
 */

package com.jrdbnnt.acronimble;

public class Card {
	//Bitmap img; -- add later, also to constuctor
	private String word;			//word that must be formed
	private int currentLetter;		//current letter needed
	
	Card (String word) {
		this.word = word.toUpperCase();
		this.currentLetter = 0;
	}
	
	/**
	 * Compares first letter of word to needed, returns t/f
	 * 
	 * @author Jared
	 * 
	 */
	public boolean isLetter(String w) {
		if(this.word.charAt(currentLetter) == w.charAt(0)) {
			this.currentLetter++;
			return true;
		}
		return false;
	}
	
	/**
	 * Checks to see if word has been formed
	 * @author Jared
	 */
	public boolean isFormed() {
		return (this.word.length() == this.currentLetter);
	}
	
	/**
	 * Returns the current state of formed string, with each character spaced out with blanks as '_'s
	 * @author Jared
	 */
	public String getFormed() {
		String formed = "";
		
		//created formed string
		for(int i = 0; i < this.word.length(); i++) {
			if(this.currentLetter > i)
				formed += this.word.charAt(i);
			else
				formed += "_";
			
			//add space if more chars to come
			if((i+1) < this.word.length())
				formed += " ";
		}
		
		return formed;
	}
	
	/**
	 * Accessor for card's word
	 * @author Jared
	 */
	public String getWord() {
		return this.word;
	}
}
