/** Circular Timer
 *  Written by Rohit
 *  Jul 8, 2014
 */
package com.rohitbhoompally.elements.circularcdt;

import java.lang.Character.UnicodeBlock;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class CircularTimer extends View {

	// Default values for variables
	private int layout_height = 0;
	private int layout_width = 0;
	private int rimThickness = 15;
	private int rimDefaultColor = 0xAEF7F7F7;
	private int rimFillColor = 0xAFBFFF00;
	private int timeMinuteColor = 0xEEBAFF0A;
	private int timeSecondColor = 0xEEBAFF0A;
	private int timeUnitsTextColor = 0xAEAEAEAE;
	private boolean showMinutes = false;
	private boolean rimInnerShadow = false;
	private int timeInSeconds = 60;
	private int paddingTop = 5;
	private int paddingBottom = 5;
	private int paddingLeft = 5;
	private int paddingRight = 5;
	private static String minutes = "Minutes";
	private static String seconds = "Seconds";

	// Paints
	private Paint rimDefaultPaint = new Paint();
	private Paint rimFillPaint = new Paint();
	private Paint timeMinutePaint = new Paint();
	private Paint timeSecondPaint = new Paint();
	private Paint timeUnitPaint = new Paint();

	// Rectangles
	@SuppressWarnings("unused")
	private RectF rimRect = new RectF();
	private RectF circleRect = new RectF();
	private RectF innerRect = new RectF();
	private RectF minutesRect = new RectF();
	private RectF secondsRect = new RectF();

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
			showMinutes = (boolean) typedArray.getBoolean(
					R.styleable.CircularTimer_showMinutes, showMinutes);
			timeInSeconds = (int) typedArray.getInt(
					R.styleable.CircularTimer_timeInSeconds, timeInSeconds);
			timeUnitsTextColor = (int) typedArray.getColor(
					R.styleable.CircularTimer_timeUnitsTextColor,
					timeUnitsTextColor);
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

		layout_width = w;
		layout_height = h;

		setUpBackground();
		invalidate();
	}

	private void setUpBackground() {

		// Setup bounds of the components
		int minValue = Math.min(layout_width, layout_height);

		int xOffset = layout_width - minValue;
		int yOffset = layout_height - minValue;

		paddingTop = this.getPaddingTop() + (yOffset / 2);
		paddingBottom = this.getPaddingBottom() + (yOffset / 2);
		paddingLeft = this.getPaddingLeft() + (xOffset / 2);
		paddingRight = this.getPaddingRight() + (xOffset / 2);

		int width = getWidth(); // this.getLayoutParams().width;
		int height = getHeight(); // this.getLayoutParams().height;

		rimRect = new RectF(paddingLeft, paddingTop, width - paddingRight,
				height - paddingBottom);

		circleRect = new RectF(paddingLeft + rimThickness, paddingTop
				+ rimThickness, width - paddingRight - rimThickness, height
				- paddingBottom - rimThickness);

		// We need this value to add time text. Get the value of this rect
		// from circle rect's position. This is simple math for inscribing a
		// square inside a circle.
		float circleDiameter = circleRect.width() - rimThickness;
		float circleRadius = (float) (circleDiameter / (2.0f * Math.sqrt(2)));
		float insRectLeft = circleRect.centerX() - circleRadius;
		float insRectTop = circleRect.centerY() - circleRadius;
		float insRectRight = circleRect.centerX() + circleRadius;
		float insRectBottom = circleRect.centerY() + circleRadius;
		innerRect = new RectF(insRectLeft, insRectTop, insRectRight,
				insRectBottom);

		// minutesRect is the component which holds the text for minutes in the
		// CDT. We allocate 60% of innerRect's available width to this rect.
		float innerRectWidth = innerRect.right - innerRect.left;
		float minutesRectRight = 0.58f * innerRectWidth;
		minutesRect = new RectF(innerRect.left, innerRect.top, innerRect.left
				+ minutesRectRight, innerRect.right);

		// secondsRect is the component which holds the text for seconds in the
		// CDT. We allocate the remaining 40% of innerRect's space to this rect.
		float minutesRectLeft = 0.6f * innerRectWidth;
		secondsRect = new RectF(innerRect.left + minutesRectLeft,
				innerRect.top, innerRect.right, innerRect.bottom);

		// // Make this rect take the left half of available space
		// int centerOffset = (int) rimRect.centerX();
		// minutesRect = new RectF(innerRect.left, innerRect.top,
		// centerOffset - 2, innerRect.bottom);
		//
		// // Make this rect take the right half of available space, and
		// restrict
		// // the height to 3/4 of available height
		// int restrictedTop = (int) (innerRect.top + (innerRect.bottom / 5));
		// secondsRect = new RectF(centerOffset + 2, restrictedTop,
		// innerRect.right, innerRect.bottom);

		// setup paints of the component
		rimDefaultPaint.setColor(rimDefaultColor);
		rimDefaultPaint.setAntiAlias(true);
		rimDefaultPaint.setStyle(Style.FILL); // Change this later
		rimDefaultPaint.setStrokeWidth(rimThickness);

		rimFillPaint.setColor(rimFillColor);
		rimFillPaint.setAntiAlias(true);
		rimFillPaint.setStyle(Style.STROKE);
		rimFillPaint.setStrokeWidth(rimThickness);

		timeUnitPaint.setColor(timeUnitsTextColor);
		timeUnitPaint.setStyle(Style.FILL);
		timeUnitPaint.setTextAlign(Align.CENTER);
		timeUnitPaint.setAntiAlias(true);
		timeUnitPaint.setTextSize(12);

		timeMinutePaint.setColor(timeMinuteColor);
		timeMinutePaint.setStyle(Style.FILL);
		timeMinutePaint.setTextAlign(Align.CENTER);
		timeMinutePaint.setAntiAlias(true);

		timeSecondPaint.setColor(timeSecondColor);
		timeSecondPaint.setStyle(Style.FILL);
		timeSecondPaint.setAntiAlias(true);

		if (showMinutes) {
			timeMinutePaint.setTextSize(minutesRect.bottom - minutesRect.top);
			timeSecondPaint.setTextSize(16);
		}
	}

	protected void onDraw(Canvas canvas) {

		canvas.drawRect(minutesRect, timeUnitPaint);

		canvas.drawRect(secondsRect, timeUnitPaint);

		// Draw the inner circle
		canvas.drawArc(circleRect, 360, 360, false, rimDefaultPaint);

		// Draw the bar
		canvas.drawArc(circleRect, 360, 360, false, rimFillPaint);

		// // Get heights of the texts
		// int minuteUnitTextHeight = (int) (timeUnitPaint.ascent() -
		// timeUnitPaint
		// .descent());
		//
		// // If hours should be shown we need 3 compartments
		// if (showMinutes) {
		// // canvas.drawText(seconds, minutesRect.top, minutesRect.left,
		// // timeUnitPaint);
		// canvas.drawText(minutes, minutesRect.centerX(), minutesRect.bottom,
		// timeUnitPaint);
		// String m = "59";
		// if (m.length() > 1)
		// timeMinutePaint.setTextSize(timeMinutePaint.getTextSize() / 2);
		// canvas.drawText(m, minutesRect.centerX(), minutesRect.bottom
		// + minuteUnitTextHeight, timeMinutePaint);
		// }
	}
}
