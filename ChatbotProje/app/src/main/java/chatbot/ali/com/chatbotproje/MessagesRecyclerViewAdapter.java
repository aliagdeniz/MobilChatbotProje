package chatbot.ali.com.chatbotproje;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import chatbot.ali.com.chatbotproje.Message;

import java.util.ArrayList;

public class MessagesRecyclerViewAdapter extends RecyclerView.Adapter<MessagesRecyclerViewAdapter.ViewHolder>{
    private final LayoutInflater layoutInflater;
    public ArrayList<Message> messages;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView messageContent;
        public TextView messageAuthor;
        public LinearLayout linearLayout;

        public ViewHolder(View itemView){
            super(itemView);
            linearLayout = (LinearLayout) itemView.getRootView();
            messageContent = (TextView) itemView.findViewById(R.id.messageContent);
            messageAuthor = (TextView) itemView.findViewById(R.id.messageAuthor);
        }
    }

    public MessagesRecyclerViewAdapter(ArrayList<Message> messages, final Context context){
        this.layoutInflater = LayoutInflater.from(context);
        this.messages = messages;
    }

    @Override
    public MessagesRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int i){
        View view = layoutInflater.inflate(R.layout.chat_message_view, parent, false);

        ViewHolder viewholder = new ViewHolder(view);

        return viewholder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position){
        viewHolder.messageContent.setText(messages.get(position).getContent());
        viewHolder.messageAuthor.setText(messages.get(position).getUserId());
//        if(! messages.get(position).getIsChatbot()){
//            viewHolder.linearLayout.setGravity(Gravity.RIGHT);
//            viewHolder.messageAuthor.setGravity(Gravity.RIGHT);
//            viewHolder.messageContent.setGravity(Gravity.RIGHT);
//        }
    }

    @Override
    public int getItemCount(){
        return messages.size();
    }
}