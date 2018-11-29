package com.example.grey;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

public class ScrollingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_scrolling);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_keyboard_backspace_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(ScrollingActivity.this,DrawerActivity.class);
                startActivity(intent);
                ScrollingActivity.this.finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_scrolling,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.toolbar_search:
                Toast.makeText(this,"search",Toast.LENGTH_SHORT).show();

                Intent intent=new Intent(ScrollingActivity.this,MySearchActivity.class);
                startActivity(intent);
//                LoginActivity.this.finish();

                break;
            case R.id.toolbar_edit:
                Toast.makeText(this,"edit",Toast.LENGTH_SHORT).show();
                Intent intent1=new Intent(ScrollingActivity.this,EditActivity.class);
                startActivity(intent1);
                break;
            case R.id.toolbar_follow:
                Toast.makeText(this,"follow",Toast.LENGTH_SHORT).show();
                break;
                default:
        }
        return true;
    }
}
