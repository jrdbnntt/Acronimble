package com.jrdbnnt.acronimble;

import java.util.ArrayList;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;


import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Parcel;
import android.view.textservice.SuggestionsInfo;

public class WordChecker {
	ArrayList<String> words;
	AssetManager am;
	SuggestionsInfo si;
	
	WordChecker(AssetManager am) {
		this.am = am;
		init();
	}
	
	private void init() {
		words = new ArrayList<String>();
		try {
			Scanner inFile = new Scanner(am.open("words.txt"));
			
			//populate wordlist
			while(inFile.hasNext()) {
				words.add(inFile.next());
				inFile.nextLine();
			}
			inFile.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	public boolean isWord(String w) {
		boolean result = false;
		
		for(int i = 0; (i < this.words.size()) && (result == false); i++) {
			if(w.equals(this.words.get(i)))
				result = true;
				
			//System.out.println("COMPARING '" + this.words.get(i) + "' TO '" + w + "' ... " + result);
		}
		
		return result;
	}
	
}
