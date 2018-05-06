package com.keren.tomer.minesweeper;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

//Taken from https://stackoverflow.com/a/38205219/4342751
public class ZoomableView extends ViewGroup {

        // States.
        private static final byte NONE = 0;
        private static final byte DRAG = 1;
        private static final byte ZOOM = 2;

        private byte mode = NONE;

        // Matrices used to move and zoom image.
        private Matrix matrix = new Matrix();
        private Matrix matrixInverse = new Matrix();
        private Matrix savedMatrix = new Matrix();

        // Parameters for zooming.
        private PointF start = new PointF();
        private PointF mid = new PointF();
        private float oldDist = 1f;
    private long lastDownTime = 0L;

        private float[] mDispatchTouchEventWorkingArray = new float[2];
        private float[] mOnTouchEventWorkingArray = new float[2];


        @Override
        public boolean dispatchTouchEvent(MotionEvent ev) {
            mDispatchTouchEventWorkingArray[0] = ev.getX();
            mDispatchTouchEventWorkingArray[1] = ev.getY();
            mDispatchTouchEventWorkingArray = screenPointsToScaledPoints(mDispatchTouchEventWorkingArray);
            ev.setLocation(mDispatchTouchEventWorkingArray[0], mDispatchTouchEventWorkingArray[1]);
            return super.dispatchTouchEvent(ev);
        }

        public ZoomableView(Context context) {
            super(context);
            init(context);
        }

        public ZoomableView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init(context);
        }

        public ZoomableView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init(context);
        }


        private void init(Context context) {

        }


        /**
         * Determine the space between the first two fingers
         */
        private float spacing(MotionEvent event) {
            float x = event.getX(0) - event.getX(1);
            float y = event.getY(0) - event.getY(1);
            return (float) Math.sqrt(x * x + y * y);
        }

        /**
         * Calculate the mid point of the first two fingers
         */
        private void midPoint(PointF point, MotionEvent event) {
            float x = event.getX(0) + event.getX(1);
            float y = event.getY(0) + event.getY(1);
            point.set(x / 2, y / 2);
        }

        private float[] scaledPointsToScreenPoints(float[] a) {
            matrix.mapPoints(a);
            return a;
        }

        private float[] screenPointsToScaledPoints(float[] a) {
            matrixInverse.mapPoints(a);
            return a;
        }


        @Override
        protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                if (child.getVisibility() != GONE) {
                    child.layout(left, top, left + child.getMeasuredWidth(), top + child.getMeasuredHeight());
                }
            }
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);

            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                if (child.getVisibility() != GONE) {
                    measureChild(child, widthMeasureSpec, heightMeasureSpec);
                }
            }
        }

        @Override
        protected void dispatchDraw(Canvas canvas) {
            float[] values = new float[9];
            matrix.getValues(values);
            canvas.save();
            canvas.translate(values[Matrix.MTRANS_X], values[Matrix.MTRANS_Y]);
            canvas.scale(values[Matrix.MSCALE_X], values[Matrix.MSCALE_Y]);
            super.dispatchDraw(canvas);
            canvas.restore();
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            // handle touch events here
            mOnTouchEventWorkingArray[0] = event.getX();
            mOnTouchEventWorkingArray[1] = event.getY();

            mOnTouchEventWorkingArray = scaledPointsToScreenPoints(mOnTouchEventWorkingArray);

            event.setLocation(mOnTouchEventWorkingArray[0], mOnTouchEventWorkingArray[1]);

            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    savedMatrix.set(matrix);
                    mode = DRAG;
                    float[] lastEvent = null;
                    long downTime = event.getDownTime();
                    if (downTime - lastDownTime < 300L) {
                        float density = getResources().getDisplayMetrics().density;
                        if (Math.max(Math.abs(start.x - event.getX()), Math.abs(start.y - event.getY())) < 40.f * density) {
                            savedMatrix.set(matrix);
                            mid.set(event.getX(), event.getY());
                            mode = ZOOM;
                            lastEvent = new float[4];
                            lastEvent[0] = lastEvent[1] = event.getX();
                            lastEvent[2] = lastEvent[3] = event.getY();
                        }
                        lastDownTime = 0L;
                    } else {
                        lastDownTime = downTime;
                    }
                    start.set(event.getX(), event.getY());
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    oldDist = spacing(event);
                    if (oldDist > 10f) {
                        savedMatrix.set(matrix);
                        midPoint(mid, event);
                        mode = ZOOM;
                    }
                    lastEvent = new float[4];
                    lastEvent[0] = event.getX(0);
                    lastEvent[1] = event.getX(1);
                    lastEvent[2] = event.getY(0);
                    lastEvent[3] = event.getY(1);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                    mode = NONE;
                    lastEvent = null;
                    break;
                case MotionEvent.ACTION_MOVE:
                    final float density = getResources().getDisplayMetrics().density;
                    if (mode == DRAG) {
                        matrix.set(savedMatrix);
                        float dx = event.getX() - start.x;
                        float dy = event.getY() - start.y;
                        matrix.postTranslate(dx, dy);
                        matrix.invert(matrixInverse);
                        if (Math.max(Math.abs(start.x - event.getX()), Math.abs(start.y - event.getY())) > 20.f * density) {
                            lastDownTime = 0L;
                        }
                    } else if (mode == ZOOM) {
                        if (event.getPointerCount() > 1) {
                            float newDist = spacing(event);
                            if (newDist > 10f * density) {
                                matrix.set(savedMatrix);
                                float scale = (newDist / oldDist);
                                matrix.postScale(scale, scale, mid.x, mid.y);
                                matrix.invert(matrixInverse);
                            }
                        } else {
                            matrix.set(savedMatrix);
                            float scale = event.getY() / start.y;
                            matrix.postScale(scale, scale, mid.x, mid.y);
                            matrix.invert(matrixInverse);
                        }
                    }
                    break;
            }

            invalidate();
            return true;
        }

    }

