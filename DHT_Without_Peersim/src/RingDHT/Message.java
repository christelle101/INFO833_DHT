package RingDHT;

public class Message {

    public static final int SENT = 0;
    public static final int DELIVERED = 1;
    private int type;
    private String content;

    Message(int type){
        this.type = type;
        if (this.type == SENT) this.content = "sent";
        if (this.type == DELIVERED) this.content = "delivered";
    }

    public String getContent(){
        return this.content;
    }

    public int getType(){
        return this.type;
    }

}
