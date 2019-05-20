package com.example.Exercise.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.Exercise.R;
import com.example.Exercise.customview.ManipulationImageView;

import static android.app.Activity.RESULT_OK;

public class ImageManipulationFragment extends Fragment {
    private ManipulationImageView mTouchView;
    private final static int PICK_FROM_GALLERY = 0001;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_manipulation, container, false);
        mTouchView = (ManipulationImageView) view.findViewById(R.id.iv_image_manipulation);
        Button bt_load = (Button) view.findViewById(R.id.bt_load);
        Button bt_clear = (Button) view.findViewById(R.id.bt_clear);
        bt_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, null);
                i.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(i, PICK_FROM_GALLERY);
            }
        });

        bt_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTouchView.setImageURI(null);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FROM_GALLERY) {
            if (resultCode == RESULT_OK && data != null) {
                {
                    Uri uri = data.getData();
                    mTouchView.setImageURI(uri);
                }
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
