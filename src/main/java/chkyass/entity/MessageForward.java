package chkyass.entity;

public class MessageForward extends Message {
    private int onlineCount;

    public MessageForward(Message m, int count) {
        super(m.getFrom(), m.getMessage());
        this.onlineCount = count;
    }

    public void setOnlineCount(int onlineCount) {
        this.onlineCount = onlineCount;
    }

    public int getOnlineCount() {
        return onlineCount;
    }
}
