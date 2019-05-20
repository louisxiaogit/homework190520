package com.example.Exercise.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.Exercise.R;

public class CalculatorFragment extends Fragment {
    private String mSt;
    private TextView textShow;
    private Button mBt0, mBt1, mBt2, mBt3, mBt4, mBt5, mBt6, mBt7, mBt8, mBt9, mBtC, mBtAdd, mBtSub, mBtMul, mBtDiv, mBtEqu;
    private boolean mIsEmpty = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_calculator, container, false);

        mBt0 = (Button) view.findViewById(R.id.button);
        mBt1 = (Button) view.findViewById(R.id.button1);
        mBt2 = (Button) view.findViewById(R.id.button2);
        mBt3 = (Button) view.findViewById(R.id.button3);
        mBt4 = (Button) view.findViewById(R.id.button4);
        mBt5 = (Button) view.findViewById(R.id.button5);
        mBt6 = (Button) view.findViewById(R.id.button6);
        mBt7 = (Button) view.findViewById(R.id.button7);
        mBt8 = (Button) view.findViewById(R.id.button8);
        mBt9 = (Button) view.findViewById(R.id.button9);

        mBtEqu = (Button) view.findViewById(R.id.buttonEqu);
        mBtAdd = (Button) view.findViewById(R.id.buttonAdd);
        mBtSub = (Button) view.findViewById(R.id.buttonSub);
        mBtMul = (Button) view.findViewById(R.id.buttonMul);
        mBtDiv = (Button) view.findViewById(R.id.buttonDiv);
        mBtC = (Button) view.findViewById(R.id.buttonC);

        textShow = (TextView) view.findViewById(R.id.tv_show);

        mBt0.setOnClickListener(myListener);
        mBt1.setOnClickListener(myListener);
        mBt2.setOnClickListener(myListener);
        mBt3.setOnClickListener(myListener);
        mBt4.setOnClickListener(myListener);
        mBt5.setOnClickListener(myListener);
        mBt6.setOnClickListener(myListener);
        mBt7.setOnClickListener(myListener);
        mBt8.setOnClickListener(myListener);
        mBt9.setOnClickListener(myListener);
        mBtAdd.setOnClickListener(myListener);
        mBtSub.setOnClickListener(myListener);
        mBtMul.setOnClickListener(myListener);
        mBtDiv.setOnClickListener(myListener);
        mBtC.setOnClickListener(myListener);
        mBtEqu.setOnClickListener(myListener);
        return view;
    }

    private Button.OnClickListener myListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button btnTemp = (Button) v.findViewById(v.getId());
            String textString = textShow.getText().toString();
            if ("0".equals(textString) && "0".equals(btnTemp.getText().toString())) {
                return;
            }
            if (textString.length() > 0) {
                if (isSpecialString(textString.substring(textString.length() - 1)) && isSpecialString(btnTemp.getText().toString())) {
                    return;
                }
            }
            if (btnTemp.getText().toString().equals("C")) {
                mSt = "0";
            } else if (btnTemp.getText().toString().equals("=")) {
                if(mIsEmpty || "0".equals(btnTemp.getText().toString())){
                    return;
                }
                mIsEmpty = true;
                if (isSpecialString(textString.substring(textString.length() - 1))) {
                    mSt = textString.substring(0, textString.length() - 1);
                }

                try {
                    for (int i = 0; i < mSt.length(); i++) {
                        if (mSt.charAt(i) == '*' || mSt.charAt(i) == '/') {
                            String f = "", b = "";
                            int j;
                            for (j = i - 1; j >= 0; j--) {
                                if (mSt.charAt(j) == '*' || mSt.charAt(j) == '/' || mSt.charAt(j) == '-' || mSt.charAt(j) == '+')
                                    break;
                                else f = mSt.charAt(j) + f;
                            }
                            int k;
                            for (k = i + 1; k < mSt.length(); k++) {
                                if (mSt.charAt(k) == '*' || mSt.charAt(k) == '/' || mSt.charAt(k) == '-' || mSt.charAt(k) == '+')
                                    break;
                                else b = b + mSt.charAt(k);
                            }
                            int temp = 0;
                            if (mSt.charAt(i) == '*')
                                temp = Integer.parseInt(f) * Integer.parseInt(b);
                            else temp = Integer.parseInt(f) / Integer.parseInt(b);
                            mSt = mSt.substring(0, j + 1) + temp + mSt.substring(k, mSt.length());
                            i = j;
                        }
                    }
                    if (mSt.charAt(0) != '-') mSt = "+" + mSt;
                    int ans = 0;
                    for (int i = 0; i < mSt.length(); ) {
                        if (mSt.charAt(i) == '+' || mSt.charAt(i) == '-') {
                            String temp = "";
                            int j;
                            for (j = i + 1; j < mSt.length(); j++) {
                                if (mSt.charAt(j) == '-' || mSt.charAt(j) == '+') break;
                                else temp = temp + mSt.charAt(j);
                            }
                            if (mSt.charAt(i) == '+') ans = ans + Integer.parseInt(temp);
                            else ans = ans - Integer.parseInt(temp);
                            i = j;
                        }
                    }
                    textShow.setText(Integer.toString(ans));
                    return;
                } catch (Exception ex) {
                    textShow.setText(ex.toString());
                }

            } else {
                if ("0".equals(textShow.getText().toString()) || mIsEmpty) {
                    mIsEmpty = false;
                    if (isSpecialString(btnTemp.getText().toString())) {
                        mSt = textShow.getText().toString() + btnTemp.getText().toString();
                    } else {
                        mSt = btnTemp.getText().toString();
                    }
                } else {
                    mSt = textShow.getText().toString() + btnTemp.getText().toString();
                }

            }
            textShow.setText(mSt);
        }
    };


    private boolean isSpecialString(String s) {
        boolean returnValue = true;
        if (s.equals("+")) {
        } else if (s.equals("-")) {
        } else if (s.equals("*")) {
        } else if (s.equals("/")) {
        } else {
            returnValue = false;
        }
        return returnValue;
    }
}
