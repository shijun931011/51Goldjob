package com.example.user.a51goldjob.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.example.user.a51goldjob.R;


/**
 * 自定义进度条
 * @author yeq
 *
 */
public class EasyProgressBar extends ProgressBar {

	private String text;
	private Paint mPaint;
	private String textEnd;

	public EasyProgressBar(Context context) {
		super(context);
		initText(context, null);
	}

	public EasyProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		initText(context, attrs);
	}

	public EasyProgressBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initText(context, attrs);
	}

	@Override
	public synchronized void setProgress(int progress) {
		setText(progress);
		super.setProgress(progress);
	}

	protected synchronized void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Rect rect = new Rect();
		
		this.mPaint.getTextBounds(this.text, 0, this.text.length(), rect);

		int x = (getWidth() / 2) - rect.centerX();
		int y = (getHeight() / 2) - rect.centerY();
		canvas.drawText(this.text, x, y, this.mPaint);
	}

	private void initText(Context context, AttributeSet attrs) {
		this.mPaint = new Paint();
		this.mPaint.setColor(Color.WHITE);
		this.mPaint.setTextSize(35);
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.EasyProgressBar);
		textEnd = typedArray.getString(R.styleable.EasyProgressBar_textEnd);
	}

	private void setText(int progress) {
		int i = (progress * 100) / this.getMax();
		this.text = String.format("%s%s", String.valueOf(i), textEnd);
	}
}
