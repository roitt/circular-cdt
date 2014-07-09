/** Circular Timer
 *  Written by Rohit
 *  Jul 8, 2014
 */
package com.rohitbhoompally.elements.circularcdt;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
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

	// Default colors for our components

	// Paints
	private Paint rimDefaultPaint = new Paint();
	private Paint rimFillPaint = new Paint();

	// Rectangles
	private RectF rimRect = new RectF();
	private RectF circleRect = new RectF();

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

	/*
	 * Force a layout to be square. Thanks to Anders Ericsson for his wonderful
	 * explanation is : www.jayway.com
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		int size = 0;
		int width = getMeasuredWidth();
		int height = getMeasuredHeight();
		int widthWithoutPadding = width - getPaddingLeft() - getPaddingRight();
		int heigthWithoutPadding = height - getPaddingTop()
				- getPaddingBottom();

		size = Math.min(widthWithoutPadding, heigthWithoutPadding);
		setMeasuredDimension(size + getPaddingLeft() + getPaddingRight(), size
				+ getPaddingTop() + getPaddingBottom());
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		setUpBackground();
		invalidate();
	}

	private void setUpBackground() {

		// Setup bounds of the components

		// setup paints of the component
		rimDefaultPaint.setColor(rimDefaultColor);
		rimDefaultPaint.setAntiAlias(true);
		rimDefaultPaint.setStyle(Style.STROKE);
		rimDefaultPaint.setStrokeWidth(rimThickness);

		rimFillPaint.setColor(rimDefaultColor);
		rimFillPaint.setAntiAlias(true);
		rimFillPaint.setStyle(Style.STROKE);
		rimFillPaint.setStrokeWidth(rimThickness);

	}
}
