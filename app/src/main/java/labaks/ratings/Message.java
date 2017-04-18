package labaks.ratings;

import java.util.Date;

public class Message {

    private String textMessage;
    private String author;
    private String email;
    private long timeMessage;

    public Message(String textMessage, String author, String email) {
        this.textMessage = textMessage;
        this.author = author;
        this.email = email;

        timeMessage = new Date().getTime();
    }

    public Message() {
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getTimeMessage() {
        return timeMessage;
    }

    public void setTimeMessage(long timeMessage) {
        this.timeMessage = timeMessage;
    }
}
