/**
 * Similar to WordChecker, but checks against a list of valid cards
 * 
 * @author Jared
 */

package com.jrdbnnt.acronimble;

import java.util.ArrayList;
import java.util.Random;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.format.Time;

public class CardChecker {
	private static ArrayList<String> cards;
	private Boolean hasLoaded;
	private Boolean isLoading;
	private AssetManager am;
	private Random rand;
	private final int NUM_WORDS = 100;		//appox, is actually lower

	private static final CardChecker sInstance = new CardChecker();

	private CardChecker() {
		init();
	}
	private void init() {
		this.cards = new ArrayList<String>(this.NUM_WORDS);
		this.hasLoaded = false;
		this.isLoading = false;
		
		Time t = new Time();
		t.setToNow();
		this.rand = new Random(t.toMillis(false));
		
	}
	
	/**
	 * Used to access the static instance and its contents.
	 * 
	 * @author Jared
	 */
	public static CardChecker getInstance() {
		return sInstance;
	}

	/**
	 * Loads card list in background. 
	 * Needs to be called from an activity once.
	 * 
	 * @param a context (use 'this' inside an activity)
	 * @author Jared
	 */
	public void load(Context c) {
		if(!hasLoaded && !isLoading) {
			this.am = c.getAssets();

			try {
				final Scanner inFile = new Scanner(am.open("localCards.txt"));

				//populate card list
				Thread populate = new Thread() {
					@Override
					public void run() {
						while(inFile.hasNext()) {				
							cards.add(inFile.nextLine());
						}
						isLoading = false;
						hasLoaded = true;
						inFile.close();
					}
				};
				this.isLoading = true;
				populate.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}	
	
	/**
	 * Used to check on loading thread.
	 * 
	 * @author Jared
	 * @return whether or not load is finished
	 */
	public boolean isReady() {
		return hasLoaded;
	}
	
	/**
	 * Pulls a random card from list;
	 * @param usedCards (will not be chosen)
	 * @return (String) cardName
	 */
	public String pullCard(ArrayList<String> usedCards) {
		int i;
		boolean good;
		
		//reset usedcards if they have all been used
		//if(usedCards.size() == this.cards.size())
		//	usedCards.clear();
		
		//TODO: fix the fact that it crashes (gets stuck in the for loop i think) when all cards have been used
		
		do {
			good = true;
			
			//get random index
			i = this.rand.nextInt(this.cards.size() - 1);

			//check if that card has been used
			for(int j = 0; j < usedCards.size() && good; j++) {
				good = !(this.cards.get(i).equals(usedCards.get(j)));
			}
		} while (!good);
		
		return this.cards.get(i);
	}



	public void addCard(String cardName) {
		//TODO: make this add cards
	}
	
	

}
