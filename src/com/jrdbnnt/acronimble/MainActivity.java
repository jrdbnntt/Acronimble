package com.jrdbnnt.acronimble;


import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.util.Log;
import android.view.View;

public class MainActivity extends Activity implements View.OnClickListener {
	
	private Button bStart;
	
	private static ArrayList<String> usedCards;		//cards not to show bc already seen
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		init();
		
		
	}
	
	private void init() {
		this.bStart = (Button) this.findViewById(R.id.bStart);
		this.usedCards = new ArrayList<String>();
		
		//load words here, only have to once in app
		WordChecker.getInstance().load(this);
		CardChecker.getInstance().load(this);
		
		this.bStart.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
		case R.id.bStart: 
			start();
			break;
		}
	}
	
	/**
	 * Starts card activity, passes word and imgID with intent
	 * 
	 * @author Jared
	 */
	private void start() {
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
	
}
