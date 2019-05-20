package com.example.Exercise.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.Exercise.R;

public class CanvasFragment extends Fragment {
    private ImageView mIv_canvas;
    private Bitmap mBaseBitmap;
    private Canvas mCanvas;
    private Paint mPaint;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_canvas, container, false);
        mPaint = new Paint();
        mPaint.setStrokeWidth(5);
        mPaint.setColor(Color.RED);
        mIv_canvas = (ImageView) view.findViewById(R.id.iv_canvas);
        mIv_canvas.setOnTouchListener(touch);
        return view;
    }

    private View.OnTouchListener touch = new View.OnTouchListener() {
        float startX;
        float startY;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (mBaseBitmap == null) {
                        mBaseBitmap = Bitmap.createBitmap(mIv_canvas.getWidth(),
                                mIv_canvas.getHeight(), Bitmap.Config.ARGB_8888);
                        mCanvas = new Canvas(mBaseBitmap);
                        mCanvas.drawColor(Color.WHITE);
                    }
                    startX = event.getX();
                    startY = event.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float stopX = event.getX();
                    float stopY = event.getY();
                    mCanvas.drawLine(startX, startY, stopX, stopY, mPaint);

                    startX = event.getX();
                    startY = event.getY();
                    mIv_canvas.setImageBitmap(mBaseBitmap);
                    break;
                case MotionEvent.ACTION_UP:
                    break;
                default:
                    break;
            }
            return true;
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
