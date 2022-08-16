package com.aueb.whatsapp;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.aueb.whatsapp.databinding.ActivityGroupChatListBinding;

import java.util.ArrayList;


public class GroupChatListActivity extends AppCompatActivity {
    ActivityGroupChatListBinding binding;
    private ListView mainListView ;
    private ArrayAdapter<String> listAdapter ;
    public String name;
    public String port;
    public String ip;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_group_chat_list);
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#FF4CAF50"));
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setTitle("Choose Groupchat");
        Bundle extras = getIntent().getExtras();
        name=extras.getString("name");
        port=extras.getString("port");
        ip=extras.getString("ip");
        ArrayList<String> gcList = extras.getStringArrayList("gc");

        ListView listView=findViewById(R.id.listview_gc);
        ArrayAdapter arrayAdapter=new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1,gcList);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(GroupChatListActivity.this, ChatActivity.class);
                intent.putExtra("GC",gcList.get(i));
                intent.putExtra("name",name);
                intent.putExtra("port",port);
                intent.putExtra("ip",ip);
                startActivity(intent);
            }
        });

    }
}
