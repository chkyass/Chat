package chkyass.model;

/**
 * WebSocket message model
 */
public class Message {
    private String from;
    private String message;

    // mandatory for jackson Json conversion
    public Message() {
    }

    public Message(String from, String message) {
        this.from = from;
        this.message = message;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFrom() {
        return from;
    }

    public String getMessage() {
        return message;
    }
}
