package com.android.zycojamie.letterdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView mTextView;
    private LetterSideView mLetterSideView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }
    protected void initView(){
        mTextView= (TextView) findViewById(R.id.tv_text);
        mLetterSideView= (LetterSideView) findViewById(R.id.letter_side_bar);
        mLetterSideView.setOnTouchMoveListener(new LetterSideView.OnTouchMoveListener() {
            @Override
            public void call(CharSequence charSequence) {
                mTextView.setText(charSequence);
                mTextView.setVisibility(View.VISIBLE);
            }
        });
        mLetterSideView.setOnTouchRemoveListener(new LetterSideView.OnTouchRemoveListener() {
            @Override
            public void call() {
                mTextView.setVisibility(View.GONE);
            }
        });
    }
}
