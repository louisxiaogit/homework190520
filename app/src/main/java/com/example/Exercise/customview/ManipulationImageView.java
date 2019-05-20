package com.example.Exercise.customview;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class ManipulationImageView extends android.support.v7.widget.AppCompatImageView {

    float mX_down = 0;
    float mY_down = 0;
    PointF mStart = new PointF();
    PointF mMid = new PointF();
    float mOldDist = 1f;
    float mOldRotation = 0;
    Matrix mMatrix = new Matrix();
    Matrix mMatrix2 = new Matrix();
    Matrix mSavedMatrix = new Matrix();

    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    int mode = NONE;

    int mViewWidth;
    int mViewHeight;

    Bitmap mBitmap;
    Context mContext;

    public ManipulationImageView(Context context) {
        super(context);
        initVIew(context);
    }

    public ManipulationImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initVIew(context);
    }

    public ManipulationImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initVIew(context);
    }

    public void initVIew(Context context) {
        this.mContext = context;
    }

    protected void onDraw(Canvas canvas) {
        if (mBitmap != null) {
            canvas.save();
            canvas.drawBitmap(mBitmap, mMatrix, null);
            canvas.restore();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mViewWidth = MeasureSpec.getSize(widthMeasureSpec);
        mViewHeight = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(mViewWidth, mViewHeight);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
    }

    @Override
    public void setImageURI(@Nullable Uri uri) {
        ContentResolver resolver = mContext.getContentResolver();
        if (uri == null) {
            mBitmap = null;
            super.setImageBitmap(null);
            reset();
            return;
        }

        try {
            InputStream inStream = resolver.openInputStream(uri);
            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
            opt.inPurgeable = true;
            opt.inInputShareable = true;
            mBitmap = BitmapFactory.decodeStream(inStream, null, opt);
            super.setImageBitmap(mBitmap);
            reset();

            int drawableWidth = mBitmap.getWidth();
            int drawableHeight = mBitmap.getHeight();

            float scale1 = (float) mViewWidth / drawableWidth;
            float scale2 = (float) mViewHeight / drawableHeight;
            float scale3 = (float) mViewWidth / drawableHeight;
            float scale4 = (float) mViewHeight / drawableWidth;

            float scale = Math.min(Math.min(scale1, scale2),
                    Math.min(scale3, scale4));

            mMatrix.setScale(scale, scale);
            mMatrix.postTranslate((mViewWidth - drawableWidth * scale) / 2,
                    (mViewHeight - drawableHeight * scale) / 2);
            mMatrix.postRotate(0, mViewWidth / 2, mViewHeight / 2);

            setImageMatrix(mMatrix);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mode = DRAG;
                mX_down = event.getX();
                mY_down = event.getY();
                mSavedMatrix.set(mMatrix);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                mode = ZOOM;
                mOldDist = spacing(event);
                mOldRotation = rotation(event);
                mSavedMatrix.set(mMatrix);
                midPoint(mMid, event);
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == ZOOM) {
                    mMatrix2.set(mSavedMatrix);
                    float rotation = rotation(event) - mOldRotation;
                    float newDist = spacing(event);
                    float scale = newDist / mOldDist;
                    mMatrix2.postScale(scale, scale, mMid.x, mMid.y);
                    mMatrix2.postRotate(rotation, mMid.x, mMid.y);
                    mMatrix.set(mMatrix2);
                    invalidate();
                } else if (mode == DRAG) {
                    mMatrix2.set(mSavedMatrix);
                    mMatrix2.postTranslate(event.getX() - mX_down, event.getY()
                            - mY_down);
                    mMatrix.set(mMatrix2);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                break;
        }
        return true;
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    private float rotation(MotionEvent event) {
        double delta_x = (event.getX(0) - event.getX(1));
        double delta_y = (event.getY(0) - event.getY(1));
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }

    private void reset() {
        mMatrix.reset();
        mMatrix2.reset();
        mSavedMatrix.reset();
    }
}
