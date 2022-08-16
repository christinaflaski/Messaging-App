package com.aueb.whatsapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    String mAnswer;
    private static final int pic_id = 123;
    private static final int GALLERY_PICK = 1;
    ImageButton camera_open_id;
    ImageButton gallery_open_id;
    ImageView click_image_id;
    public List<ChatMsg> msgList;
    public String name;
    public String port;
    public String ip;
    public String brokerport;
    public String brokerip;
    public String mes;
    public File finalFile;
    public ChatAdapter chatAdapter;
    public RecyclerView msgRecyclerView;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#FF4CAF50"));
        actionBar.setBackgroundDrawable(colorDrawable);

        Bundle extras = getIntent().getExtras();
        mAnswer= extras.getString("GC");
        setTitle(mAnswer);
        name=extras.getString("name");
        ip=extras.getString("ip");
        port=extras.getString("port");
        InfoHandler infoHandler=new InfoHandler(name,ip,port,mAnswer);
        brokerip=infoHandler.brokerip;
        brokerport=infoHandler.brokerport;
        System.out.println("this is it:"+brokerport+brokerip);
        camera_open_id = findViewById(R.id.camera_send);
        camera_open_id.setOnClickListener(cameraClickListener);
        gallery_open_id = findViewById(R.id.gallery_send);
        gallery_open_id.setOnClickListener(galleryClickListener);
        // Get RecyclerView object.
        msgRecyclerView = findViewById(R.id.recycler_gchat);

        // Set RecyclerView layout manager.
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(linearLayoutManager);

        // Create the initial data list.
        msgList = new ArrayList<ChatMsg>();
        ChatMsg msgDto = new ChatMsg(ChatMsg.MSG_TYPE_RECEIVED, "hello");
        msgList.add(msgDto);
        chatAdapter = new ChatAdapter(msgList);
        msgRecyclerView.setAdapter(chatAdapter);

        final EditText msgInputText = findViewById(R.id.edit_gchat_message);

        ImageButton msgSendButton = findViewById(R.id.button_gchat_send);

        msgSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msgContent = msgInputText.getText().toString();
                if(!TextUtils.isEmpty(msgContent))
                {
                    MessageHandler messageHandler=new MessageHandler(name,brokerip,brokerport,msgContent,port);
                    // Add a new sent message to the list.
                    ChatMsg msg = new ChatMsg(ChatMsg.MSG_TYPE_SENT, msgContent);
                    msgList.add(msg);

                    int newMsgPosition = msgList.size() - 1;

                    // Notify recycler view insert one new data.
                    chatAdapter.notifyItemInserted(newMsgPosition);

                    // Scroll RecyclerView to the last message.
                    msgRecyclerView.scrollToPosition(newMsgPosition);

                    // Empty the input edit text box.
                    msgInputText.setText("");
                    ///returning messages
                    String mes=messageHandler.getReturnmess();

                }
            }
        });
    }
    View.OnClickListener cameraClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(ChatActivity.this,"Opening camera...",Toast.LENGTH_LONG).show();
            Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(pictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(pictureIntent,pic_id);
            }
            //Intent intent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
        }
    };
    View.OnClickListener galleryClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent =new Intent();
            intent.setType("image/+");
            intent.setAction(intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,"SELECT IMAGE"),GALLERY_PICK);
        }
    };
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {

        // Match the request 'pic id with requestCode
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == pic_id) {

            // BitMap is data structure of image file
            // which store the image in memory
            Bitmap photo = (Bitmap) data.getExtras().get("data");

            // Set the image in imageview for display
            //click_image_id.setImageBitmap(photo);
            //Uri tempUri = getImageUri(getApplicationContext(), photo);

            // CALL THIS METHOD TO GET THE ACTUAL PATH
            //finalFile = new File(getRealPathFromURI(tempUri));

        }
    }

    /*public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }*/

}
