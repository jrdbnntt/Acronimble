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
		
		
	}
	
	private void init() {
		this.bStartTyping = (Button) this.findViewById(R.id.bStartTyping);
		this.bStartButton = (Button) this.findViewById(R.id.bStartButton);
		this.tvLoadDisplay = (TextView) this.findViewById(R.id.tvLoadDisplay);
		
		this.usedCards = new ArrayList<String>();
		
		//load words here, only have to once in app
		System.out.println("DICTIONARY LOAD START");
		Dictionary.getInstance().load(this);
		System.out.println("CARD CHECKER LOAD START");
		CardChecker.getInstance().load(this);
		
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
		case R.id.bStartTyping: 
			startGame(CardActivity.class);
			break;
		case R.id.bStartButton:
			startGame(ButtonCardActivity.class);
			break;
		}
	}
	
	/**
	 * Starts card activity, passes word and imgID with intent
	 * Which a
	 * 
	 * @author Jared
	 */
	private void startGame(Class<?> game) {
		Bundle basket = new Bundle();
		Intent a = new Intent(this, game);
		
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
	
	public void setLoadPercent(boolean done, int loadPercent) {
		if(done) {
			loadPercent = 100;
			
			//enable buttons
			this.bStartTyping.setOnClickListener(this);
			this.bStartButton.setOnClickListener(this);
		}
		this.tvLoadDisplay.setText("Loading..." + loadPercent + "%");
		
		
	}

}
