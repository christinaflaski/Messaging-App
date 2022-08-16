package com.aueb.whatsapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolder> {

    private List<ChatMsg> msgList = null;

    public ChatAdapter(List<ChatMsg> msgList) {
        this.msgList = msgList;
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        ChatMsg msg = this.msgList.get(position);
        // If the message is a received message.
        if(msg.MSG_TYPE_RECEIVED.equals(msg.getMsgType()))
        {
            // Show received message in left linearlayout.
            holder.leftMsgLayout.setVisibility(LinearLayout.VISIBLE);
            holder.leftMsgTextView.setText(msg.getMsgContent());
            // Remove left linearlayout.The value should be GONE, can not be INVISIBLE
            // Otherwise each iteview's distance is too big.
            holder.rightMsgLayout.setVisibility(LinearLayout.GONE);
        }
        // If the message is a sent message.
        else if(msg.MSG_TYPE_SENT.equals(msg.getMsgType()))
        {
            // Show sent message in right linearlayout.
            holder.rightMsgLayout.setVisibility(LinearLayout.VISIBLE);
            holder.rightMsgTextView.setText(msg.getMsgContent());
            // Remove left linearlayout.The value should be GONE, can not be INVISIBLE
            // Otherwise each iteview's distance is too big.
            holder.leftMsgLayout.setVisibility(LinearLayout.GONE);
        }
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_view, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public int getItemCount() {
        if(msgList==null)
        {
            msgList = new ArrayList<ChatMsg>();
        }
        return msgList.size();
    }
}
