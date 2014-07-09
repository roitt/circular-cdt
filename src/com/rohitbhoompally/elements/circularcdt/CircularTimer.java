/** Circular Timer
 *  Written by Rohit
 *  Jul 8, 2014
 */
package com.rohitbhoompally.elements.circularcdt;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

public class CircularTimer extends View {

	// Default values for variables
	private int rimThickness = 20;
	private int rimDefaultColor = 0xAEF7F7F7;
	private int rimFillColor = 0xAFBFFF00;
	private boolean showMinutes = false;
	private boolean showHours = false;
	private boolean rimInnerShadow = false;
	private int timeInSeconds = 60;

	/**
	 * @param context
	 * @param attrs
	 */
	public CircularTimer(Context _context, AttributeSet _attrs) {
		super(_context, _attrs);
		// TODO Auto-generated constructor stub
		init(_context, _attrs);
	}

	public void init(Context _context, AttributeSet _attrs) {
		// Get the properties from resource file
		if (_context != null && _attrs != null) {
			TypedArray typedArray = _context.obtainStyledAttributes(_attrs,
					R.styleable.CircularTimer);
			rimThickness = (int) typedArray.getDimension(
					R.styleable.CircularTimer_rimThickness, rimThickness);
			rimDefaultColor = (int) typedArray.getColor(
					R.styleable.CircularTimer_rimDefaultColor, rimDefaultColor);
			rimFillColor = (int) typedArray.getColor(
					R.styleable.CircularTimer_rimFillColor, rimFillColor);
			rimInnerShadow = (boolean) typedArray.getBoolean(
					R.styleable.CircularTimer_rimInnerShadow, rimInnerShadow);
			showHours = (boolean) typedArray.getBoolean(
					R.styleable.CircularTimer_showHours, showHours);
			showMinutes = (boolean) typedArray.getBoolean(
					R.styleable.CircularTimer_showMinutes, showMinutes);
			timeInSeconds = (int) typedArray.getInt(
					R.styleable.CircularTimer_timeInSeconds, timeInSeconds);
			typedArray.recycle();
		}
	}
}
