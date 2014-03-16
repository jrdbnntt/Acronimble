/**
 * Checks a file with a list of valid words to see if they are valid
 * @author Jared
 */
package com.jrdbnnt.acronimble;

import java.util.ArrayList;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import android.content.Context;
import android.content.res.AssetManager;

public class WordChecker {
	private static ArrayList<String> words;
	private Boolean hasLoaded;
	private Boolean isLoading;
	private AssetManager am;
	private final int NUM_WORDS = 178300;		//appox, is a little under this

	private static final WordChecker sInstance = new WordChecker();

	private WordChecker() {
		init();
	}
	private void init() {
		this.words = new ArrayList<String>(this.NUM_WORDS);
		this.hasLoaded = false;
		this.isLoading = false;
	}
	
	/**
	 * Used to access the static instance and its contents.
	 * 
	 * @author Jared
	 */
	public static WordChecker getInstance() {
		return sInstance;
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
						while(inFile.hasNext()) {				
							words.add(inFile.next());
							inFile.nextLine();
						}
						isLoading = false;
						hasLoaded = true;
						//System.out.println("LOADED!");
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

	public boolean isWord(String w) {
		boolean result = false;

		for(int i = 0; (i < this.words.size()) && (result == false); i++) {
			if(w.equals(this.words.get(i)))
				result = true;
		}
		return result;
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
