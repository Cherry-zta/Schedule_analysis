package com.example.memo9;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/**
 *  Created by zxy 2019/3/14
 */

public class PasteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paste);
    }

    public void commitPaste(View view){
        Intent intent = new Intent(PasteActivity.this, EditActivity.class);
        startActivity(intent);
    }
}
