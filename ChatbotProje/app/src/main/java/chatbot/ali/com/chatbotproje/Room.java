package chatbot.ali.com.chatbotproje;

import java.util.ArrayList;


public class Room {

    private ArrayList<User> users;

    private ArrayList<Message> messages;

    public Room(){
        users = new ArrayList<User>();
        messages = new ArrayList<Message>();
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }


    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    public void addMessage(Message message){
        getMessages().add(message);
    }
}
