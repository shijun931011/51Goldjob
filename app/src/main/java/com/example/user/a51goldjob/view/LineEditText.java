package com.example.user.a51goldjob.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.EditText;

import com.example.user.a51goldjob.R;


/**
 * EditText，底部带线
 * @author yeq
 *
 */
public class LineEditText extends EditText {

	private Paint mPaint;
	/**
	 * @param context
	 * @param attrs
	 */
	public LineEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mPaint = new Paint();
		
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setColor(getResources().getColor(R.color.gray1));
	}
	
	@Override
	public void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		
//		画底线
		canvas.drawLine(0,this.getHeight()-1,  this.getWidth(), this.getHeight()-1, mPaint);
	}
}