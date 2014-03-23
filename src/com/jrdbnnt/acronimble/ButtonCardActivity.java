/**
 * The card that contains an image to display and a word to guess.
 * TODO: make this an activity/view that manages itself
 * 
 * @author Jared
 */

package com.jrdbnnt.acronimble;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ButtonCardActivity extends Activity implements View.OnClickListener {
	private String word;					//word that must be formed
	private int imgID;						//R.drawable ID of image
	private int currentLetter;				//current letter needed
	private ArrayList<String> usedWords;	//words used/invalid to enter
	
	private ImageView ivImage; 
	private TextView tvResult, tvFormedWord;
	private Button[] bChoices;
	private final int NUM_CHOICES = 4;
	
	private ArrayList<String> usedCards;
	
	private Random rand;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_button_card);
		
		init();

		getData();

		this.tvFormedWord.setText(getFormed());
		this.tvResult.setText("");
		this.currentLetter = 0;

		//get random
		Time t = new Time();
		t.setToNow();
		this.rand = new Random(t.toMillis(false));

		//add listeners and populate choices
		for(int i = 0; i < this.NUM_CHOICES; i++) {
			this.bChoices[i].setOnClickListener(this);

		}
		setChoices();



	}
	
	private void init() {
		this.tvResult = (TextView) this.findViewById(R.id.tvButtonGameResult);
		this.tvFormedWord = (TextView) this.findViewById(R.id.tvButtonGameFormedWord);
		this.ivImage = (ImageView) this.findViewById(R.id.ivButtonGameImage);
		
		this.bChoices = new Button[NUM_CHOICES];
		this.bChoices[0] = (Button) this.findViewById(R.id.bButtonGameC1);
		this.bChoices[1] = (Button) this.findViewById(R.id.bButtonGameC2);
		this.bChoices[2] = (Button) this.findViewById(R.id.bButtonGameC3);
		this.bChoices[3] = (Button) this.findViewById(R.id.bButtonGameC4);
		
		
		
		this.usedWords = new ArrayList<String>();
		
		
	}
	
	/**
	 * Retrieves data from intent that created this class instance
	 */
	private void getData() {
		Bundle gotBasket = getIntent().getExtras();
		
		//word
		this.word = gotBasket.getString("word");
		this.word = this.word.toUpperCase();
		
		
		//usedCards
		this.usedCards = gotBasket.getStringArrayList("usedCards");
		
		//img
		this.imgID = gotBasket.getInt("imgID");
		this.ivImage.setImageResource(this.imgID);
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
	 * @param the answer given
	 */
	private void submit(String text) {
		text = text.toUpperCase();

		if(!this.isFormed()) {
			if(!this.isLetter(text)) {
				this.addLog("FAIL: '" + text + "' HAS INCORRECT LETTER");
			} else {
				this.usedWords.add(text);
				
				this.addLog("SUCCESS: '" + text + "'");
				
				if(this.isFormed()) {
					this.addLog("*** WORD COMPLETED ***");
					finishCard();
				} else {
					//get new choices
					setChoices();
				}
				
				//updated formed text
				this.tvFormedWord.setText(this.getFormed());
				
			}
		} else {
			this.addLog("Error: Word alread formed");
			finishCard();
			//TODO: go to another activity, sending stats with an intent
		}
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


	@Override
	public void onClick(View v) {
		submit(((Button) v).getText().toString());
	}
	
	
	/**
	 * Sets choices to random words, with only one random button correct
	 * @author Jared
	 */
	private void setChoices() {
		int correctChoice = this.rand.nextInt(this.NUM_CHOICES);

		for(int i = 0; i < this.NUM_CHOICES; ++i) {
			this.bChoices[i].setText(Dictionary.getInstance().getRandomWord(this.word.charAt(currentLetter), (i == correctChoice)));
		}
	}
	
	private void finishCard() {
		Bundle basket = new Bundle();
		if ()
		Intent a = new Intent(this, this.getClass());
		
		//reset usedCards when they have all been used
		
		//NOTE: word MUST EQUAL pic file name (excluding extention), underscores instead of spaces
		String word = CardChecker.getInstance().pullCard(this.usedCards); // pickCard();	//word for card
		this.usedCards.add(word);
		
		basket.putStringArrayList("usedCards", this.usedCards);
		
		basket.putString("word", word);
		
		//remove any spaces for img filenames
		word = word.replaceAll("\\s","_").toLowerCase();
		
		Log.e("IMGID", word);
		
		int imgID = this.getResources()
						.getIdentifier(word, "drawable", 
								this.getPackageName());		//corresponding card img 
		
		
		basket.putInt("imgID", imgID);
		a.putExtras(basket);
		
		//start activity
		this.startActivity(a);
	}
}
