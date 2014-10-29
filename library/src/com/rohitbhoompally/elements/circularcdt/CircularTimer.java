/** Circular Timer
 *  Written by Rohit
 *  Jul 8, 2014
 */
package com.rohitbhoompally.elements.circularcdt;

import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcelable;
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
	private int shadowColor = 0xEED3D3D3;
	private int timerEndedColor = 0xA0E50000;
	private boolean showMinutes = false;
	private boolean rimInnerShadow = false;
	private int paddingTop = 5;
	private int paddingBottom = 5;
	private int paddingLeft = 5;
	private int paddingRight = 5;
	private static String minutes = "Minutes";
	private static String seconds = "Seconds";
	private final float maxTextSize = 200;
	private final float minTextSize = 12;
	private String onTimerEndedText = "";

	// Countdowntimer variables
	private CountDownTimer cdt;
	private long startTime = 120;
	private long interval = 1;
	long startTimeinMillis = startTime * 1000;
	long intervalInMillis = interval * 1000;
	private final int degrees = 360;
	private float anglePerSecond = 1;
	private boolean hasTimerEnded = false;

	// Dynamics
	private boolean dialInitialized = false;
	private float currentValue = 360;
	private float targetValue = 360;
	private float fillVelocity = 0.0f;
	private float fillAcceleration = 0.0f;
	private long lastMoveTime = -1L;

	// Paints
	private Paint rimDefaultPaint = new Paint();
	private Paint rimFillPaint = new Paint();
	private Paint timeMinutePaint = new Paint();
	private Paint timeSecondPaint = new Paint();
	private Paint timeUnitPaint = new Paint();
	private Paint timerEndedCirclePaint = new Paint();
	private Paint timerEndedTextPaint = new Paint();

	// Rectangles
	@SuppressWarnings("unused")
	private RectF rimRect = new RectF();
	private RectF circleRect = new RectF();
	private RectF innerRect = new RectF();
	private RectF minutesRect = new RectF();
	private RectF secondsRect = new RectF();

	// Countdowntimer class implementation
	public class CDT extends CountDownTimer {
		public CDT(long startTime, long interval) {
			super(startTime, interval);
		}

		@Override
		public void onFinish() {
			// TODO Auto-generated method stub
			startTimeinMillis = 0;
			targetValue = targetValue - anglePerSecond;
			invalidate();
			hasTimerEnded = true;
			invalidate();
		}

		@Override
		public void onTick(long millisUntilFinished) {
			// TODO Auto-generated method stub
			startTimeinMillis = millisUntilFinished;
			targetValue = targetValue - anglePerSecond;
			invalidate();
		}
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		Bundle bundle = (Bundle) state;
		Parcelable superState = bundle.getParcelable("superState");
		super.onRestoreInstanceState(superState);

		dialInitialized = bundle.getBoolean("dialInitialized");
		currentValue = bundle.getFloat("currentValue");
		targetValue = bundle.getFloat("targetValue");
		fillVelocity = bundle.getFloat("fillVelocity");
		fillAcceleration = bundle.getFloat("fillAcceleration");
		lastMoveTime = bundle.getLong("lastMoveTime");
		startTimeinMillis = bundle.getLong("startTimeInMillis");
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();

		Bundle state = new Bundle();
		state.putParcelable("superState", superState);
		state.putBoolean("dialInitialized", dialInitialized);
		state.putFloat("currentValue", currentValue);
		state.putFloat("targetValue", targetValue);
		state.putFloat("fillVelocity", fillVelocity);
		state.putFloat("fillAcceleration", fillAcceleration);
		state.putLong("lastMoveTime", lastMoveTime);
		state.putLong("startTimeInMillis", startTimeinMillis);
		return state;
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public CircularTimer(Context _context, AttributeSet _attrs) {
		super(_context, _attrs);
		// TODO Auto-generated constructor stub
		init(_context, _attrs);

		// Get time in millis
		startTimeinMillis = startTime * 1000;
		intervalInMillis = interval * 1000;
		cdt = new CDT(startTimeinMillis, intervalInMillis);
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
			timeUnitsTextColor = (int) typedArray.getColor(
					R.styleable.CircularTimer_timeUnitsTextColor,
					timeUnitsTextColor);
			interval = (long) typedArray.getFloat(
					R.styleable.CircularTimer_intervalInSeconds, interval);
			onTimerEndedText = (String) typedArray
					.getString(R.styleable.CircularTimer_onTimerEndedText);
			startTime = (long) typedArray.getFloat(
					R.styleable.CircularTimer_timeInSeconds, startTime);

			// Beta: Ensuring we only support minutes and seconds.
			// TODO extend the functionality to support hours.
			if (startTime > 3600)
				startTime = 3600;

			// Calculating angle per second
			anglePerSecond = degrees / startTime;

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

		this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
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

		// setup paints of the component
		rimDefaultPaint.setColor(rimDefaultColor);
		rimDefaultPaint.setAntiAlias(true);
		rimDefaultPaint.setStyle(Style.STROKE); // Change this later
		rimDefaultPaint.setStrokeWidth(rimThickness);

		// Set the inner shadow for the circle, if asked for
		if (rimInnerShadow)
			rimDefaultPaint.setShadowLayer(10.0f, 5.0f, 5.0f, shadowColor);

		rimFillPaint.setColor(rimFillColor);
		rimFillPaint.setAntiAlias(true);
		rimFillPaint.setStyle(Style.STROKE);
		rimFillPaint.setStrokeWidth(rimThickness);

		timeUnitPaint.setColor(timeUnitsTextColor);
		timeUnitPaint.setStyle(Style.FILL);
		timeUnitPaint.setTextAlign(Align.CENTER);
		timeUnitPaint.setAntiAlias(true);
		timeUnitPaint
				.setTextSize(getValidatedTextSize(minutesRect.width() / 7));

		timeMinutePaint.setColor(timeMinuteColor);
		timeMinutePaint.setStyle(Style.FILL);
		timeMinutePaint.setTextAlign(Align.CENTER);
		timeMinutePaint.setAntiAlias(true);

		timeSecondPaint.setColor(timeSecondColor);
		timeSecondPaint.setStyle(Style.FILL);
		timeSecondPaint.setTextAlign(Align.CENTER);
		timeSecondPaint.setAntiAlias(true);

		if (showMinutes) {
			timeMinutePaint.setTextSize(getValidatedTextSize(minutesRect
					.width() * 0.9f));
			timeSecondPaint.setTextSize(getValidatedTextSize(secondsRect
					.width() * 0.7f));
		}

		timerEndedCirclePaint.setColor(timerEndedColor);
		timerEndedCirclePaint.setStyle(Style.FILL);
		timerEndedCirclePaint.setTextAlign(Align.CENTER);
		timerEndedCirclePaint.setAntiAlias(true);

		timerEndedTextPaint.setColor(rimDefaultColor);
		timerEndedTextPaint.setStyle(Style.FILL);
		timerEndedTextPaint.setTextAlign(Align.CENTER);
		timerEndedTextPaint.setAntiAlias(true);
		timerEndedTextPaint.setTextSize(getValidatedTextSize(circleRect.width()
				/ onTimerEndedText.length()));
		timerEndedTextPaint.setShadowLayer(5.0f, 2.0f, 2.0f, shadowColor);
	}

	protected void onDraw(Canvas canvas) {

		// Draw the inner circle
		canvas.drawArc(circleRect, 360, 360, false, rimDefaultPaint);

		// Draw the bar
		canvas.drawArc(circleRect, 90, targetValue, false, rimFillPaint);

		// This method handles drawing the circle, and its fills, and showing
		// minutes and seconds text inside.
		if (!hasTimerEnded)
			drawWorkingTimer(canvas);
		else
			drawFinishedTimer(canvas);
	}

	private void drawWorkingTimer(Canvas canvas) {
		// Draw the minutes and seconds text, and try to center it in the
		// available rect space vertically.
		float verticalBasePosition = minutesRect.centerY()
				+ ((timeMinutePaint.getTextSize()) / 2.0f);

		canvas.drawText(String.valueOf(TimeUnit.MILLISECONDS
				.toSeconds(startTimeinMillis)
				- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
						.toMinutes(startTimeinMillis))), secondsRect.centerX(),
				verticalBasePosition, timeSecondPaint);

		canvas.drawText(String.valueOf(TimeUnit.MILLISECONDS
				.toMinutes(startTimeinMillis)), minutesRect.centerX(),
				verticalBasePosition, timeMinutePaint);

		// Positioning the units on top of minutes and seconds.
		float minutesUnitsBasePosition = verticalBasePosition
				- timeMinutePaint.getTextSize();

		float secondsUnitsBasePosition = verticalBasePosition
				- (timeSecondPaint.getTextSize());

		canvas.drawText(minutes, minutesRect.centerX(),
				minutesUnitsBasePosition, timeUnitPaint);
		canvas.drawText(seconds, secondsRect.centerX(),
				secondsUnitsBasePosition, timeUnitPaint);
	}

	private void drawFinishedTimer(Canvas canvas) {
		canvas.drawArc(circleRect, 0, 360, false, timerEndedCirclePaint);

		// Centering the text relative to the circle.
		float xCenter = circleRect.centerX();
		float yCenter = circleRect.centerY()
				+ (timerEndedCirclePaint.getTextSize() / 2);
		canvas.drawText(onTimerEndedText, xCenter, yCenter, timerEndedTextPaint);
	}

	private float getValidatedTextSize(float textSize) {
		if (textSize > maxTextSize)
			textSize = maxTextSize;
		if (textSize < minTextSize)
			textSize = minTextSize;

		return textSize;
	}

	public void startTimer() {
		cdt.start();
	}

	public void stopTimer() {
		cdt.cancel();
	}

	public void resetTimer() {
		if (cdt != null)
			cdt.cancel();
		cdt = null;
		reInitializeTimerVariables();
		cdt = new CDT(startTimeinMillis, intervalInMillis);
		invalidate();
	}

	private void reInitializeTimerVariables() {
		if (startTime > 3600)
			startTime = 3600;

		// Calculating angle per second
		anglePerSecond = degrees / startTime;
		targetValue = 360;
		startTimeinMillis = startTime * 1000;
		intervalInMillis = interval * 1000;
	}
}
