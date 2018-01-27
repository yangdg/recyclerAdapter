package com.yang.recycleradapter.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.yang.recycleradapter.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.multiItem).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserListMultiItemActivity.launch(MainActivity.this);
            }
        });
        findViewById(R.id.singleiItem).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserListSingleItemActivity.launch(MainActivity.this);
            }
        });
    }
}
