package RingDHT;

import org.w3c.dom.Node;

public class Pair {
    private Message msg;
    private NodeDHT node;
    Pair(Message msg, NodeDHT node) {
        this.msg = msg;
        this.node = node;
    }

    public Message getMsg() {return this.msg; }
    public NodeDHT getNode() {return this.node; }

}
