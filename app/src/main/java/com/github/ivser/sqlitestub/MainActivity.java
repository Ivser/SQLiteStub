package com.github.ivser.sqlitestub;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.github.ivser.sqlitestub.activity.direct.DMasterActivity;
import com.github.ivser.sqlitestub.activity.provider.CPMasterActivity;
import com.github.ivser.sqlitestub.activity.room.RMasterActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onDirectUsageBtnClick(View v) {
        startActivity(new Intent(this, DMasterActivity.class));
    }

    public void onContentProviderUsageBtnClick(View v) {
        startActivity(new Intent(this, CPMasterActivity.class));
    }

    public void onRoomUsageBtnClick(View v) {
        startActivity(new Intent(this, RMasterActivity.class));
    }
}
