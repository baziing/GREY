package com.example.grey;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class EditActivity extends AppCompatActivity {

    private EditText editText;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_keyboard_backspace_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                EditActivity.this.finish();
            }
        });

        editText=(EditText)findViewById(R.id.edit_text);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_edit,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.trush:
                Toast.makeText(EditActivity.this,"trush",Toast.LENGTH_SHORT).show();
                editText.getText().clear();
                break;
            case R.id.gps:
                Toast.makeText(EditActivity.this,"gps",Toast.LENGTH_SHORT).show();
                //介入地图api
                break;
            case R.id.sendup:
                String inputText=editText.getText().toString();//获取输入的文字
                Toast.makeText(EditActivity.this,inputText,Toast.LENGTH_SHORT).show();
                editText.getText().clear();
                break;
                default:
        }
        return true;
    }
}
