package com.example.ayushroy.notes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void add()
    {
        Intent intent=new Intent(getApplicationContext(),EditActivity.class);
        intent.putExtra("position",content.size());
        //Toast.makeText(this, Integer.toString(content.size()), Toast.LENGTH_SHORT).show();
        content.add("Note "+ Integer.toString(content.size()+1));
        arrayAdapter.notifyDataSetChanged();
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);

        switch(item.getItemId())
        {
            case R.id.add:
                add();
                return true;
            default:
                return false;
        }
    }

    ListView listView;
    static ArrayList<String> content;
    static ArrayAdapter arrayAdapter;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView=findViewById(R.id.listView);
        content=new ArrayList<>();
        sharedPreferences=getApplicationContext().getSharedPreferences("com.example.ayushroy.notes", Context.MODE_PRIVATE);
        HashSet<String> set = (HashSet<String>) sharedPreferences.getStringSet("notes",null);
        if(set==null)
        {
            content.add("Example Note");
            
        }
        else
        {
            content=new ArrayList<>(set);
        }
        arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,content);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,final int position, long id)
            {
                new AlertDialog.Builder(MainActivity.this).setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Delete Confirmation")
                        .setMessage("Are you sure you want to delete the note and all of its contents?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                content.remove(position);
                                arrayAdapter.notifyDataSetChanged();
                                HashSet<String> set =new HashSet<>(MainActivity.content);
                                sharedPreferences.edit().putStringSet("notes", set).apply();
                                Toast.makeText(MainActivity.this, "Note Deleted!!!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Cancel",null)
                        .show();
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                //Toast.makeText(MainActivity.this, Integer.toString(position), Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getApplicationContext(),EditActivity.class);
                intent.putExtra("position",position);
                startActivity(intent);
            }
        });


    }
}
