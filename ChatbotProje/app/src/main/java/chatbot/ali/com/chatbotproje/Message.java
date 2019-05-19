package chatbot.ali.com.chatbotproje;


public class Message {


    private String content;
    private Boolean isChatbot;



    private String userId;

    public Message( String content, String userId, Boolean isChatbot){
        this.content = content;
        this.userId = userId;
        this.isChatbot = isChatbot;
    }
    public Boolean getIsChatbot(){
        return this.isChatbot;
    }


    public String getContent() {
        return content;
    }



    public String getUserId() {
        return userId;
    }



    public void setContent(String content) {
        this.content = content;
    }


    public void setUserId(String userId) {
        this.userId = userId;
    }
}