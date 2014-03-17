/**
 * The card that contains an image to display and a word to guess.
 * TODO: make this an activity/view that manages itself
 * 
 * @author Jared
 *
 */

package com.jrdbnnt.acronimble;

import java.util.ArrayList;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class CardActivity extends Activity implements View.OnClickListener {
	private String word;					//word that must be formed
	private int imgID;						//R.drawable ID of image
	private int currentLetter;				//current letter needed
	private ArrayList<String> usedWords;	//words used/invalid to enter
	
	private ImageView ivImage; 
	private TextView tvResult, tvFormedWord;
	private EditText etInput;
	private Button bSubmit;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card);
		
		init();
		getData();
		
		//make it so you cant use the word later
		this.usedWords.add(this.word);
		
		this.tvFormedWord.setText(getFormed());
		this.tvResult.setText("");
		
		this.bSubmit.setOnClickListener(this);
	}
	
	private void init() {
		this.tvResult = (TextView) this.findViewById(R.id.tvResult);
		this.tvFormedWord = (TextView) this.findViewById(R.id.tvFormedWord);
		this.etInput = (EditText) this.findViewById(R.id.etInput);
		this.bSubmit = (Button) this.findViewById(R.id.bSubmit);
		this.ivImage = (ImageView) this.findViewById(R.id.ivImage);
		this.usedWords = new ArrayList<String>();
		this.currentLetter = 0;
		
	}
	
	/**
	 * Retrieves data from intent that created this class instance
	 */
	private void getData() {
		Bundle gotBasket = getIntent().getExtras();
		
		//word
		this.word = gotBasket.getString("word");
		this.word = this.word.toUpperCase();
		
		//img
		this.imgID = gotBasket.getInt("imgID");
		this.ivImage.setImageResource(this.imgID);
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.bSubmit:
			submit();
			break;
		}
	}
	
	/**
	 * Compares first letter of word to needed, returns t/f
	 * 
	 * @author Jared
	 * 
	 */
	private boolean isLetter(String w) {
		if(this.word.charAt(currentLetter) == w.charAt(0)) {
			this.currentLetter++;
			
			//skip any spaces
			while(!this.isFormed() && this.word.charAt(currentLetter) == ' ')
				currentLetter++;
			
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
	private String getFormed() {
		String formed = "";
		
		//created formed string
		for(int i = 0; i < this.word.length(); i++) {
			if(this.currentLetter > i)
				formed += this.word.charAt(i);
			else if(this.word.charAt(i) == ' ')
				formed += " ";
			else
				formed += "_";
			
			//add space if more chars to come
			if((i+1) < this.word.length())
				formed += " ";
		}
		
		return formed;
	}
	
	/**
	 * Checks word for card
	 * @author Jared
	 */
	private void submit() {
		String text = this.etInput.getText().toString();
		text = text.toUpperCase();

		if(!this.isFormed()) {
			if(this.word == text) {
				this.addLog("FAIL: '" + text + "' == GOAL");
			} else if(!WordChecker.getInstance().isWord(text)) {
				this.addLog("FAIL: '" + text + "' != WORD");
			} else if(!this.isNewWord(text)) {
				this.addLog("FAIL: '" + text + "' HAS BEEN USED");
			} else if(!this.isLetter(text)) {
				this.addLog("FAIL: '" + text + "' HAS INCORRECT LETTER");
			} else {
				this.usedWords.add(text);
				
				this.addLog("SUCCESS: '" + text + "'");
				
				if(this.isFormed()) {
					this.addLog("*** WORD COMPLETED ***");
				}
				
				//updated formed text
				this.tvFormedWord.setText(this.getFormed());
			}
		} else {
			this.addLog("Error: Word alread formed");
			//TODO: go to another activity, sending stats with an intent
		}

		//reset input
		this.etInput.getText().clear();
	}
	
	/**
	 * Appends entry to log, adding a newline
	 * 
	 * @author Jared
	 */
	private void addLog(String entry) {
		String currentLog = this.tvResult.getText().toString();
		
		currentLog += entry + "\n";
		
		this.tvResult.setText(currentLog);
	}
	
	/**
	 * Checks to see if word has already been used
	 * @author Jared
	 */
	private boolean isNewWord(String w) {
		boolean result = true;
		
		for(int i = 0; (i < this.usedWords.size()) && result; i++) {
			result = !w.equals(this.usedWords.get(i));
		}
		
		return result;
	}

	
}
