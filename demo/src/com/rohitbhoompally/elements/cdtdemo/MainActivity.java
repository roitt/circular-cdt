package com.rohitbhoompally.elements.cdtdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.rohitbhoompally.elements.circularcdt.CircularTimer;

public class MainActivity extends Activity {
	static CircularTimer ct;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ct = (CircularTimer) findViewById(R.id.c_timer);
		final Button btn = (Button) findViewById(R.id.start);
		final LinearLayout btnWrapper = (LinearLayout) findViewById(R.id.btn_wrapper);
		Button pauseBtn = (Button) findViewById(R.id.pause);
		Button resumeBtn = (Button) findViewById(R.id.resume);
		Button stopBtn = (Button) findViewById(R.id.stop);
		Button resetBtn = (Button) findViewById(R.id.reset);
		btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ct.startTimer();

				// Hide start button
				btn.setVisibility(View.GONE);
				btnWrapper.setVisibility(View.VISIBLE);
			}
		});

		stopBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ct.stopTimer();
			}
		});

		pauseBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		resetBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ct.resetTimer();

				// Show start button
				btn.setVisibility(View.VISIBLE);
				btnWrapper.setVisibility(View.GONE);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
