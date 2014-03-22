package com.jrdbnnt.acronimble;


import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;
import android.util.Log;
import android.view.View;

public class MainActivity extends Activity implements View.OnClickListener {
	
	private Button bStartTyping;
	private Button bStartButton;
	private TextView tvLoadDisplay;
	
	private static ArrayList<String> usedCards;		//cards not to show bc already seen
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		init();
		
		//ouput random words
		//for(int i = 0; i < 100; i++){
		//	Log.e("random_word", WordChecker.getInstance().getRandomWord());
		//}
		
	}
	
	private void init() {
		this.bStartTyping = (Button) this.findViewById(R.id.bStartTyping);
		this.bStartTyping = (Button) this.findViewById(R.id.bStartButton);
		this.tvLoadDisplay = (TextView) this.findViewById(R.id.tvLoadDisplay);
		
		this.usedCards = new ArrayList<String>();
		
		//load words here, only have to once in app
		Dictionary.getInstance().load(this);
		CardChecker.getInstance().load(this);
		
		this.bStartTyping.setOnClickListener(this);
		this.bStartButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
		case R.id.bStartTyping: 
			startTypingGame();
			break;
		case R.id.bStartButton:
			startButtonGame();
			break;
		}
	}
	
	/**
	 * Starts card activity, passes word and imgID with intent
	 * This game is the one you where type in words.
	 * 
	 * @author Jared
	 */
	private void startTypingGame() {
		Bundle basket = new Bundle();
		Intent a = new Intent(this, CardActivity.class);
		
		//reset usedCards when they have all been used
		
		//NOTE: word MUST EQUAL pic file name (excluding extention), underscores instead of spaces
		String word = CardChecker.getInstance().pullCard(this.usedCards); // pickCard();	//word for card
		this.usedCards.add(word);
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
	
	/**
	 * Starts card activity, passes word and imgID with intent
	 * This game is the one where you click on word buttons.
	 * 
	 * @author Jared
	 */
	private void startButtonGame() {
		
	}
}
