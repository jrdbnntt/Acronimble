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
import android.widget.TextView;

public class Dictionary {
	private static HashMap<Integer,String> words;		//list of words w/ key = word.hashCode()
	private static ArrayList<Integer> keys;					//list keys
	private Boolean hasLoaded;
	private Boolean isLoading;
	private AssetManager am;
	private final int NUM_WORDS = 178300;		//appox, is a little under this
	private Random rand;
	
	private static int[][] wordLocations;		//holds indexes of [wordLength][letter a-z]
	private final int MAX_WORD_SIZE = 15;
	private final int MIN_WORD_SIZE = 2;		//actual min - 1
	private final String ALPHA = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private final int NUM_LETTERS = 26;
	
	private static final Dictionary sInstance = new Dictionary();

	private Dictionary() {
		init();
	}
	private void init() {
		Dictionary.words = new HashMap<Integer,String>(this.NUM_WORDS);
		Dictionary.keys = new ArrayList<Integer>(this.NUM_WORDS);
		this.hasLoaded = false;
		this.isLoading = false;
		
		Dictionary.wordLocations = new int[MAX_WORD_SIZE - MIN_WORD_SIZE][NUM_LETTERS];
		
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
	public void load(final MainActivity c) {
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
						int currentAlpha = 0;
						char currentLetter = ' ';
						
						System.out.println("STARTED LOADING");
						
						for(int i = 0; inFile.hasNext(); ++i) {
							word = inFile.nextLine();
							key = word.hashCode();
							Dictionary.words.put(key,word);
							Dictionary.keys.add(key);
							
							//set marker for where letter starts
							if(word.charAt(0) != currentLetter) {
								currentLetter = word.charAt(0);
								Dictionary.wordLocations[word.length() - MIN_WORD_SIZE - 1][currentAlpha] = i;
								++currentAlpha;
								
								//reset alpha if needed
								if(currentAlpha == NUM_LETTERS) {
									currentAlpha = 0;
								}
								
								//update loading text in main
								final int index = i;
								c.runOnUiThread(new Runnable() {
									@Override
									public void run() {
										c.setLoadPercent(false, (int)((((double)index) / ((double)NUM_WORDS)) * 100));
									}
								});
							}
						}
						//update loading text in main
						c.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								c.setLoadPercent(true, 100);
							}
						});
						
						isLoading = false;
						hasLoaded = true;
						inFile.close();
						
						System.out.println("FINISHED LOADING");
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
		if(Dictionary.words.get(w.hashCode()) != null)
			return true;
		else
			return false;
	}
	
	/**
	 * Chooses a random word from the words using a random key from keys.
	 * 
	 * @author Jared
	 * 
	 * @return random word out of words
	 */
	public String getRandomWord(char correctLetter, boolean isCorrect) {
		int correctIndex = this.ALPHA.indexOf(correctLetter);
		int wordSize;
		int letter = correctIndex;
		Integer key;
		
		if(!isCorrect) {
			while(letter == correctIndex)
				letter = this.rand.nextInt(this.NUM_LETTERS);
		}
		
		wordSize = this.rand.nextInt(this.MAX_WORD_SIZE - this.MIN_WORD_SIZE - 1);
		
		key = Dictionary.wordLocations[wordSize][letter];
		
		return (Dictionary.words.get(Dictionary.keys.get(key)));
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
