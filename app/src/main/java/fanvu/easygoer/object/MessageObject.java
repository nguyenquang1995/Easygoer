package fanvu.easygoer.object;

/**
 * Created by framgia on 13/10/2016.
 */
public class MessageObject {
    public static final String MY_OWN_MESSAGE = "my own message";
    private String mContent;
    private String mSender;

    public MessageObject(String content, String sender) {
        mContent = content;
        mSender = sender;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public String getSender() {
        return mSender;
    }

    public void setSender(String sender) {
        mSender = sender;
    }
}
