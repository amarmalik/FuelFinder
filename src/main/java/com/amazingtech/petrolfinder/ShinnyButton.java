/*
 * Copyright 2014 Pierre Degand
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.amazingtech.petrolfinder;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.graphics.drawable.PaintDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;

/**
 * @author Pierre Degand
 */
public class ShinnyButton extends Button {

    private float mGradientDiameter = 0.3f;

    private ValueAnimator mAnimator;
    private float mGradientCenter;
    private PaintDrawable mShineDrawable;

    public ShinnyButton(final Context context) {
        this(context, null);
    }

    public ShinnyButton(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("NewApi")
	public ShinnyButton(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    @SuppressLint("NewApi")
	@Override
    protected void onSizeChanged(final int w, final int h, final int oldw, final int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (!isInEditMode()) {
            if (mAnimator != null) {
                mAnimator.cancel();
            }
            mShineDrawable = new PaintDrawable();
            mShineDrawable.setBounds(0, 0, w, h);
            mShineDrawable.getPaint().setShader(generateGradientShader(getWidth(), 0, 0, 0));
            mShineDrawable.getPaint().setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

            mAnimator = ValueAnimator.ofFloat(0, 1);
            mAnimator.setDuration(5 * w); // custom
            mAnimator.setRepeatCount(ValueAnimator.INFINITE);
            mAnimator.setRepeatMode(ValueAnimator.RESTART);
            mAnimator.setInterpolator(new LinearInterpolator()); // Custom
            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(final ValueAnimator animation) {
                    final float value = animation.getAnimatedFraction();
                    mGradientCenter = (1 + 2 * mGradientDiameter) * value - mGradientDiameter;
                    final float gradientStart = mGradientCenter - mGradientDiameter;
                    final float gradientEnd = mGradientCenter + mGradientDiameter;
                    Shader shader = generateGradientShader(w, gradientStart, mGradientCenter, gradientEnd);
                    mShineDrawable.getPaint().setShader(shader);
                    invalidate();
                }
            });
            mAnimator.start();
        }
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        if (!isInEditMode() && mShineDrawable != null) {
            mShineDrawable.draw(canvas);
        }
    }

    private Shader generateGradientShader(int width, float... positions) {
        int[] colorRepartition = {Color.BLACK, Color.WHITE, Color.GRAY};
        return new LinearGradient(
                0,
                0,
                width,
                0,
                colorRepartition,
                positions,
                Shader.TileMode.REPEAT
        );
    }
}
