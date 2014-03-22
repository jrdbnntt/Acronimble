/**
 * Checks a file with a list of valid words to see if they are valid
 * @author Jared
 */
package com.jrdbnnt.acronimble;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.io.IOException;
import java.util.Scanner;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.format.Time;
import android.util.Log;

public class Dictionary {
	private static HashMap<Integer,String> words;		//list of words w/ key = word.hashCode()
	private static ArrayList<Integer> keys;					//list keys
	private Boolean hasLoaded;
	private Boolean isLoading;
	private AssetManager am;
	private final int NUM_WORDS = 178300;		//appox, is a little under this
	private Random rand;

	private static final Dictionary sInstance = new Dictionary();

	private Dictionary() {
		init();
	}
	private void init() {
		Dictionary.words = new HashMap<Integer,String>(this.NUM_WORDS);
		Dictionary.keys = new ArrayList<Integer>(this.NUM_WORDS);
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
	public static Dictionary getInstance() {
		return Dictionary.sInstance;
	}

	/**
	 * Loads giant wordlist in background. 
	 * Needs to be called from an activity once.
	 * 
	 * @param a context (use 'this' inside an activity)
	 * @author Jared
	 */
	public void load(Context c) {
		if(!hasLoaded && !isLoading) {
			this.am = c.getAssets();

			try {
				final Scanner inFile = new Scanner(am.open("words.txt"));
				
				//populate wordlist
				Thread populate = new Thread() {
					@Override
					public void run() {
						String word;
						Integer key;
						System.out.println("STARTED LOADING");
						
						while(inFile.hasNext()) {
							word = inFile.nextLine();
							key = word.hashCode();
							words.put(key,word);
							keys.add(key);
						}
						isLoading = false;
						hasLoaded = true;
						inFile.close();
						
						System.out.println("FINISHED LOADING");
						
						//for(int i = 0; i < 100; i++){
						//	System.out.println("random_word : " + getRandomWord());
						//}
					}
				};
				this.isLoading = true;
				populate.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}	

	public boolean isWord(String w) {
		/*
		boolean result = false;

		for(int i = 0; (i < this.words.size()) && (result == false); i++) {
			if(w.equals(this.words.get(i)))
				result = true;
		}
		return result;
		*/
		
		if(Dictionary.words.get(w.hashCode()) != null)
			return true;
		else
			return false;
	}
	
	/**
	 * Chooses a random word from the words using a random key from keys.
	 * @author Jared
	 * @param w
	 * @return random word out of words
	 */
	public String getRandomWord() {
		return (Dictionary.words.get(Dictionary.keys.get(rand.nextInt(Dictionary.keys.size()))));
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

}
