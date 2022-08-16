package com.aueb.whatsapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.aueb.whatsapp.databinding.ActivityHomeScreenBinding;

public class HomeScreenActivity extends AppCompatActivity {
    ActivityHomeScreenBinding binding;
    Button EnterButton;
    EditText EnterEdit;
    TextView EnterText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home_screen);
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#FF4CAF50"));
        actionBar.setBackgroundDrawable(colorDrawable);
        EnterEdit = findViewById(R.id.EnterName);
        EnterEdit.getText().toString();
        setUpClickListeners();

    }

    public void setUpClickListeners() {
        binding.EnterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreenActivity.this, GroupChatListActivity.class));
                UsersInfo usersInfo=new UsersInfo();
                Intent intent = new Intent(HomeScreenActivity.this, GroupChatListActivity.class);
                if(binding.EnterName.getText().toString().equals("xristina")){
                    intent.putExtra("name",usersInfo.user1.name);
                    intent.putExtra("gc",usersInfo.user1.groupchats);
                    intent.putExtra("port",usersInfo.user1.port);
                    intent.putExtra("ip",usersInfo.user1.ip);
                }else if(binding.EnterName.getText().toString().equals("giannis")){
                    intent.putExtra("name",usersInfo.user2.name);
                    intent.putExtra("gc",usersInfo.user2.groupchats);
                    intent.putExtra("port",usersInfo.user2.port);
                    intent.putExtra("ip",usersInfo.user2.ip);
                }else if(binding.EnterName.getText().toString().equals("kleon")){
                    intent.putExtra("name",usersInfo.user3.name);
                    intent.putExtra("gc",usersInfo.user3.groupchats);
                    intent.putExtra("port",usersInfo.user3.port);
                    intent.putExtra("ip",usersInfo.user3.ip);
                }
                startActivity(intent);
            }
        });
    }
}