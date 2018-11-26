package com.example.materialdesign;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class EditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar_edit);
        setSupportActionBar(toolbar);

        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_keyboard_backspace_white_24dp);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_edit,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.trush:
                Toast.makeText(this, "trush", Toast.LENGTH_SHORT).show();
                break;
            case R.id.gps:
                Toast.makeText(this, "gps", Toast.LENGTH_SHORT).show();
                break;
            case R.id.sendup:
                Toast.makeText(this, "sendup", Toast.LENGTH_SHORT).show();
                break;
            default:
        }
        return true;
    }
}
