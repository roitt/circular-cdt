circular-countdowntimer
=======================

A Simple Visual Countdown Timer

### USAGE
1) Add the library as a dependency for your project.

2) Include the CircularTimer in your layout file.

```
<com.rohitbhoompally.elements.circularcdt.CircularTimer
        android:id="@+id/c_timer"
        android:layout_width="200dp"
        android:layout_height="200dp"
        android:layout_centerInParent="true"
        app:intervalInSeconds="1"
        app:onTimerEndedText=""
        app:rimDefaultColor="#EEF7F7F7"
        app:rimFillColor="#EEBFFF00"
        app:rimInnerShadow="true"
        app:rimThickness="10dp"
        app:showMinutes="true"
        app:timeInSeconds="5"
        app:timeMinuteColor="#FFBAFF0A"
        app:timeSecondColor="#FFBAFF0A"
        app:timeUnitsTextColor="#AEAEAEAE" />
```

3) Initialize the timer in your java file.

```
static CircularTimer ct;
ct = (CircularTimer) findViewById(R.id.c_timer);
```

4) Use methods startTimer, resetTimer, stopTimer, restartTimer to control the timer.

```
ct.startTimer();
ct.stopTimer();
ct.resetTimer();
ct.restartTimer();
```

5) If you want to listen to onTick(long millis) and onFinish() methods, use registerListener method.

```
ct.registerListener(new CDTCircular() {

			@Override
			public void onTick(long millis) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				
			}
		});
```

TODO: Handle onFinish states, and expose more library options.

*Note: Still under development. Needs a lot of additional functionality, and bug fixes.
