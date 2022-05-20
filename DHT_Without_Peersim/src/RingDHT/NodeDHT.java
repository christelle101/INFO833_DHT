package RingDHT;

import java.util.ArrayList;
import java.util.Random;

public class NodeDHT  {
    private int id;

    private NodeDHT LeftNode;
    private NodeDHT RightNode;
    private boolean MsgRecieved;

    private ArrayList<Pair> tabMsg = new ArrayList<>(); //Tab de tab tel que: [[Msg,noeudEmetter], [Msg,noeudEmetter2] ]


    NodeDHT(){
        Random rand = new Random();
        this.id = rand.nextInt(5000);
        this.LeftNode = this;
        this.RightNode = this;
        this.MsgRecieved = false;
    }


    public int getId(){
        return this.id;
    }

    public NodeDHT getLeftNode(){
        return this.LeftNode;
    }

    public NodeDHT getRightNode(){
        return this.RightNode;
    }

    public boolean getMsgRecieved(){return this.MsgRecieved; }

    public ArrayList getTabMsg(){return this.tabMsg; }

    public void setLeftNode(NodeDHT leftNode) {
        this.LeftNode = leftNode;
    }

    public void setRightNode(NodeDHT rightNode) {
        this.RightNode = rightNode;
    }

    public void setMsgRecieved(boolean msg) { this.MsgRecieved = msg; }


}
