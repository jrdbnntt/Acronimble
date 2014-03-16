package com.jrdbnnt.acronimble;

import java.util.ArrayList;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import android.content.res.AssetManager;

public class WordChecker {
	private static ArrayList<String> words;
	private static Boolean hasLoaded;
	private static AssetManager am;
	private final int NUM_WORDS = 178300;
	
	WordChecker(AssetManager am) {
		this.am = am;
		init();
	}
	
	private void init() {
		this.words = new ArrayList<String>(this.NUM_WORDS);
		this.hasLoaded = false;
		
		//LOAD WORDLIST
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
					hasLoaded = true;
					//System.out.println("LOADED!");
					inFile.close();
				}
			};
			populate.start();
		} catch (IOException e) {
			e.printStackTrace();
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
	
}
