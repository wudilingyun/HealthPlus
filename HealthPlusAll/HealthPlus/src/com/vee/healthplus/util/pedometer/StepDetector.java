package com.vee.healthplus.util.pedometer;

import java.util.ArrayList;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class StepDetector implements SensorEventListener {
    private final static String TAG = "xuxuxu";

    private int index = 4;

    private float mLastValues;

    private float mScale[] = new float[2];

    private float mYOffset;

    private boolean startRun = false;

    private float mLastDirections;

    private float mLastExtremes[][] = new float[2][1];

    private float mLastDiff;

    private int mLastMatch = -1;

    private float[] mLimitArr = {3.97f, 5.96f, 7.44f, 9.66f, 10.00f, 15.00f, 22.50f, 33.75f, 50.62f};

    private ArrayList<StepListener> mStepListeners = new ArrayList<StepListener>();

    public StepDetector() {
        int h = 480;
        mYOffset = h * 0.5f;
        mScale[0] = -(h * 0.5f * (1.0f / (SensorManager.STANDARD_GRAVITY * 2)));
        mScale[1] = -(h * 0.5f * (1.0f / (SensorManager.MAGNETIC_FIELD_EARTH_MAX)));
    }

    public void setSensitivity(int index) {
        this.index = index;
    }

    public void addStepListener(StepListener sl) {
        mStepListeners.add(sl);
    }

    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        synchronized (this) {
//			Log.i(TAG, "onSensorChanged:" + sensor.getType());
            if (sensor.getType() == Sensor.TYPE_ORIENTATION) {
            } else {
                int j = (sensor.getType() == Sensor.TYPE_ACCELEROMETER) ? 1 : 0;
                if (j == 1) {
                    float vSum = 0;
                    for (int i = 0; i < 3; i++) {
                        final float v = mYOffset + event.values[i] * mScale[j];
                        vSum += v;
                    }
                    float v = vSum / 3;
                    float direction = (v > mLastValues ? 1 : (v < mLastValues ? -1 : 0));
                    if (direction == -mLastDirections && direction != 0) {
                        int extType = (direction > 0 ? 0 : 1);
                        mLastExtremes[extType][0] = mLastValues;
                        float diff = Math.abs(mLastExtremes[extType][0] - mLastExtremes[1 - extType][0]);
                        if (diff > mLimitArr[index]) {
                            boolean isAlmostAsLargeAsPrevious = diff > (mLastDiff * 2 / 3);
                            boolean isPreviousLargeEnough = mLastDiff > (diff / 3);
                            boolean isNotContra = (mLastMatch != 1 - extType);

                            if (isAlmostAsLargeAsPrevious && isPreviousLargeEnough && isNotContra) {
                                if (!startRun) {
                                    startRun = true;
                                }
                                for (StepListener stepListener : mStepListeners) {
                                    if (diff > mLimitArr[7]) {
                                        stepListener.onStep(1);
                                    } else {
                                        stepListener.onStep(0);
                                    }
                                }
                                mLastMatch = extType;
                            } else {
                                mLastMatch = -1;
                            }
                        }
                        if (startRun) {
                            if (diff > mLimitArr[index] && mLastDiff > mLimitArr[index] && index >= 0 && index < 8) {
                                index++;
                            } else if (diff < mLimitArr[index] && mLastDiff < mLimitArr[index] && index > 0 && index <= 8) {
                                index--;
                            }
                        }
                        mLastDiff = diff;
                    }
                    mLastDirections = direction;
                    mLastValues = v;
                }
            }
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

}