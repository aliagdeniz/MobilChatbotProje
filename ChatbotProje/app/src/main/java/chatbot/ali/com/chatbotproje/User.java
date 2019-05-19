package chatbot.ali.com.chatbotproje;


/**
 * Created by Noah Huppert on 11/7/2014.
 */

/**
 * A StackOverflow chat user
 * Users are retrieved via the following Json format
 *      {
 *          "id": 0,
 *          "name": "Noah-Huppert",
 *          "email_hash": "!/Content/Img/feed-icon32.png",
 *          "reputation": 549,
 *          "is_moderator": false,
 *          "is_owner": null,
 *          "last_post": null,
 *          "last_seen": null
 *      }
 *
 * Via the following Url
 *      POST http://chat.stackoverflow.com/user/info
 *          Headers
 *              roomId
 *
 * The following parameters will be stored
 *      User Id
 *      Display name
 *      Avatar Url
 */
public class User {

    private int userId;

    private String displayName;

    private String avatarUrl;

    public User(int userId, String displayName, String avatarUrl){
        this.userId = userId;
        this.displayName = displayName;
        this.avatarUrl = avatarUrl;
    }

    public int getUserId() {
        return userId;
    }


    public String getDisplayName() {
        return displayName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }


    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}