/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.olunx.irss.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.*;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

public class ColorPickerDialog extends AlertDialog {

	/**
	 * 回调函数，改变颜色后的操作可在实现该接口的方法内完成。
	 * 
	 * @author olunx
	 */
	public interface OnColorChangedListener {
		void colorChanged(int color);
	}

	private Context context;
	private ColorPickerView colorPickView;
	private OnColorChangedListener mListener;
	private int mInitialColor;// 初始化颜色

	private static class ColorPickerView extends View {
		private Paint mPaint;
		private Paint mCenterPaint;
		private Paint mHSVPaint;
		private final int[] mColors;
		private int[] mHSVColors;
		private boolean mRedrawHSV;
		private OnColorChangedListener mListener;

		ColorPickerView(Context c, OnColorChangedListener l, int color) {
			super(c);
			mListener = l;
			mColors = new int[] { 0xFFFF0000, 0xFFFF00FF, 0xFF0000FF, 0xFF00FFFF, 0xFF00FF00, 0xFFFFFF00, 0xFFFF0000 };
			Shader s = new SweepGradient(0, 0, mColors, null);

			mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
			mPaint.setShader(s);
			mPaint.setStyle(Paint.Style.STROKE);
			mPaint.setStrokeWidth(55);

			mCenterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
			mCenterPaint.setColor(color);
			mCenterPaint.setStrokeWidth(5);

			mHSVColors = new int[] { 0xFF000000, color, 0xFFFFFFFF };

			mHSVPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
			mHSVPaint.setStrokeWidth(10);

			mRedrawHSV = true;

		}

		private boolean mTrackingCenter;
		private boolean mHighlightCenter;

		public int getColor() {
			return mCenterPaint.getColor();
		}

		@Override
		protected void onDraw(Canvas canvas) {
			float r = CENTER_X - mPaint.getStrokeWidth() * 0.5f;

			canvas.translate(CENTER_X, CENTER_X);
			int c = mCenterPaint.getColor();

			if (mRedrawHSV) {
				mHSVColors[1] = c;
				mHSVPaint.setShader(new LinearGradient(-100, 0, 100, 0, mHSVColors, null, Shader.TileMode.CLAMP));
			}

			canvas.drawOval(new RectF(-r, -r, r, r), mPaint);
			canvas.drawCircle(0, 0, CENTER_RADIUS, mCenterPaint);
			canvas.drawRect(new RectF(-100, 130, 100, 110), mHSVPaint);

			if (mTrackingCenter) {
				mCenterPaint.setStyle(Paint.Style.STROKE);

				if (mHighlightCenter) {
					mCenterPaint.setAlpha(0xFF);
				} else {
					mCenterPaint.setAlpha(0x80);
				}
				canvas.drawCircle(0, 0, CENTER_RADIUS + mCenterPaint.getStrokeWidth(), mCenterPaint);

				mCenterPaint.setStyle(Paint.Style.FILL);
				mCenterPaint.setColor(c);
			}

			mRedrawHSV = true;
		}

		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			setMeasuredDimension(CENTER_X * 2, (CENTER_Y + 25) * 2);
		}

		private static final int CENTER_X = 100;
		private static final int CENTER_Y = 100;
		private static final int CENTER_RADIUS = 30;

		private int ave(int s, int d, float p) {
			return s + java.lang.Math.round(p * (d - s));
		}

		private int interpColor(int colors[], float unit) {
			if (unit <= 0) {
				return colors[0];
			}
			if (unit >= 1) {
				return colors[colors.length - 1];
			}

			float p = unit * (colors.length - 1);
			int i = (int) p;
			p -= i;

			// now p is just the fractional part [0...1) and i is the index

			int c0 = colors[i];
			int c1 = colors[i + 1];
			int a = ave(Color.alpha(c0), Color.alpha(c1), p);
			int r = ave(Color.red(c0), Color.red(c1), p);
			int g = ave(Color.green(c0), Color.green(c1), p);
			int b = ave(Color.blue(c0), Color.blue(c1), p);

			return Color.argb(a, r, g, b);
		}

		private static final float PI = 3.1415926f;

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			float x = event.getX() - CENTER_X;
			float y = event.getY() - CENTER_Y;
			boolean inCenter = java.lang.Math.sqrt(x * x + y * y) <= CENTER_RADIUS;

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mTrackingCenter = inCenter;
				if (inCenter) {
					mHighlightCenter = true;
					invalidate();
					break;
				}
			case MotionEvent.ACTION_MOVE:
				if (mTrackingCenter) {
					if (mHighlightCenter != inCenter) {
						mHighlightCenter = inCenter;
						invalidate();
					}
				} else if ((x >= -100 & x <= 100) && (y <= 130 && y >= 110)) // see
				{
					int a, r, g, b, c0, c1;
					float p;

					// set the center paint to this color

					if (x < 0) {
						c0 = mHSVColors[0];
						c1 = mHSVColors[1];
						p = (x + 100) / 100;
					} else {
						c0 = mHSVColors[1];
						c1 = mHSVColors[2];
						p = x / 100;
					}

					a = ave(Color.alpha(c0), Color.alpha(c1), p);
					r = ave(Color.red(c0), Color.red(c1), p);
					g = ave(Color.green(c0), Color.green(c1), p);
					b = ave(Color.blue(c0), Color.blue(c1), p);

					mCenterPaint.setColor(Color.argb(a, r, g, b));

					mRedrawHSV = false;
					invalidate();
				} else {
					float angle = (float) java.lang.Math.atan2(y, x);
					// need to turn angle [-PI ... PI] into unit [0....1]

					float unit = angle / (2 * PI);
					if (unit < 0) {
						unit += 1;
					}
					mCenterPaint.setColor(interpColor(mColors, unit));
					invalidate();
				}
				break;
			case MotionEvent.ACTION_UP:
				if (mTrackingCenter) {
					if (inCenter) {
						mListener.colorChanged(mCenterPaint.getColor());
					}
					mTrackingCenter = false; // so we draw w/o halo
					invalidate();
				}
				break;
			}
			return true;
		}
	}

	/**
	 * @param context
	 * @param listener
	 * @param initialColor
	 */
	public ColorPickerDialog(Context context, OnColorChangedListener listener, int initialColor) {
		super(context);

		this.context = context;
		mListener = listener;
		mInitialColor = initialColor;
	}

	/**
	 * 第三个参数为：#RRGGBB 或者 #AARRGGBB
	 * 
	 * @param context
	 * @param listener
	 * @param hexColor
	 */
	public ColorPickerDialog(Context context, OnColorChangedListener listener, String hexColor) {
		super(context);

		this.context = context;
		mListener = listener;
		mInitialColor = Color.parseColor(hexColor);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		OnColorChangedListener colorChangedListener = new OnColorChangedListener() {
			public void colorChanged(int color) {
				mListener.colorChanged(color);
			}
		};

		LinearLayout layout = new LinearLayout(context);
		layout.setPadding(5, 5, 5, 5);
		layout.setOrientation(LinearLayout.VERTICAL);

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER;

		colorPickView = new ColorPickerView(context, colorChangedListener, mInitialColor);
		colorPickView.setLayoutParams(params);
		layout.addView(colorPickView);
		setView(layout);

		setTitle("请选择颜色：");
		setButton("确定", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mListener.colorChanged(colorPickView.getColor());
			}
		});
		setButton2("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		super.onCreate(savedInstanceState);
	}

}
