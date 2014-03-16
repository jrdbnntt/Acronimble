package com.jrdbnnt.acronimble;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.res.AssetManager;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;

public class MainActivity extends Activity implements View.OnClickListener {

	private TextView tvResult, tvFormedWord;
	private EditText etInput;
	private Button bSubmit;
	private WordChecker wc;
	private AssetManager am;
	
	private Card testCard; 
	private ArrayList<String> usedWords;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		init();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private void init() {
		this.tvResult = (TextView) this.findViewById(R.id.tvResult);
		this.tvFormedWord = (TextView) this.findViewById(R.id.tvFormedWord);
		this.etInput = (EditText) this.findViewById(R.id.etInput);
		this.bSubmit = (Button) this.findViewById(R.id.bSubmit);
		//Dictionary.getInstance().ensureLoaded(getResources());
		this.am = this.getAssets();
		this.wc = new WordChecker(am);
		
		this.testCard = new Card("dog");
		this.usedWords = new ArrayList<String>();
		
		
		this.tvFormedWord.setText(this.testCard.getFormed());
		this.tvResult.setText("");
		
		
		this.bSubmit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
		case R.id.bSubmit: 
			submit();
			break;
		}
	}
	
	
	/**
	 * Checks word for card
	 * @author Jared
	 */
	private void submit() {
		String text = this.etInput.getText().toString();
		text = text.toUpperCase();

		if(!this.testCard.isFormed()) {
			if(!this.wc.isWord(text)) {
				this.addLog("FAIL: '" + text + "' != WORD");
			} else if(!this.isNewWord(text)) {
				this.addLog("FAIL: '" + text + "' HAS BEEN USED");
			} else if(!this.testCard.isLetter(text)) {
				this.addLog("FAIL: '" + text + "' HAS INCORRECT LETTER");
			} else {
				this.usedWords.add(text);
				
				this.addLog("SUCCESS: '" + text + "'");
				
				if(this.testCard.isFormed()) {
					this.addLog("*** WORD COMPLETED ***");
					this.usedWords.add(this.testCard.getWord());
				}
				
				//updated formed text
				this.tvFormedWord.setText(this.testCard.getFormed());
			}
		} else {
			this.addLog("Error: Word alread formed");
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
