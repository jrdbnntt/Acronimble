package com.jrdbnnt.acronimble;

import android.os.Bundle;
import android.app.Activity;
import android.content.res.AssetManager;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;

public class MainActivity extends Activity implements View.OnClickListener {

	private TextView tvResult;
	private EditText etInput;
	private Button bSubmit;
	private WordChecker wc;
	private AssetManager am;
	
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
		this.etInput = (EditText) this.findViewById(R.id.etInput);
		this.bSubmit = (Button) this.findViewById(R.id.bSubmit);
		//Dictionary.getInstance().ensureLoaded(getResources());
		this.am = this.getAssets();
		this.wc = new WordChecker(am);
		
		
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
	 * @author Jared
	 */
	private void submit() {
		boolean result;
		String text = this.etInput.getText().toString();
		text = text.toUpperCase();
		
		result = this.wc.isWord(text);
		if(this.wc.isWord(text))
			this.tvResult.setText("SUCCESS");
		else
			this.tvResult.setText("FAIL");
	}
	
}
