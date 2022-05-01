package helloWorld;

import peersim.core.*;
import peersim.edsim.*;
import peersim.config.*;

public class P2PNode implements EDProtocol{
    private int transportID; //transport layer ID
    private HWTransport transport; //instance of the transport layer
    private int currentID; //application layer ID (current layer)
    private int nodeID; //node ID
    private String layer_prefix;
    private int nodeUID; //random ID
    private int previousID; //previous ID
    private int nextID; //next ID
    private int previousUID; //previous UID
    private int nextUID; //next UID
    private int integer = 2;

    public P2PNode(String layer_prefix){
        this.layer_prefix = layer_prefix;
        this.transportID = Configuration.getPid(layer_prefix + ".transport");
        this.currentID = Configuration.getPid(layer_prefix + ".current");
        this.transport = null;
    }


    @Override
    public void processEvent(peersim.core.Node node, int pid, Object event) {
        this.receive((Message) event);
    }

    @Override
    public Object clone(){
        P2PNode my_node = new P2PNode(this.layer_prefix);
        return my_node;
    }
    public void send(Message msg, Node recipient_node){
        this.transport.send(getCurrentNodeID(), recipient_node, msg, this.currentID);
    }

    public void receive(Message msg){
        if (msg.getType() == Message.PREVIOUS){
            Node recipient = Network.get(this.previousID);
            this.send(new Message(msg.getType(), "Hi from " + this.nodeID), recipient);
        }
        if (msg.getType() == Message.NEXT){
            Node recipient = Network.get(this.nextID);
            this.send(new Message(msg.getType(), "Hi from " + this.nodeID), recipient);
        }
        if (msg.getType() == Message.ACTIVATE){
            Node recipient = Network.get(0);
            this.send(new Message(Message.JOIN, "Please let me enter, " + this.nodeID + "," + this.nodeUID), recipient);
        }
        if (msg.getType() == Message.JOIN){
            String[] content = msg.getContent().split(",");
            if (Integer.parseInt(content[2]) < this.nextUID){
                this.send(new Message(Message.PLACE, "Go here, " + this.nodeID + "," + this.nextID),
                        Network.get(Integer.parseInt(content[1])));
            }
            else if (this.nextID == 0){
                this.send(new Message(Message.PLACE, "Go here, " + this.nodeID + "," + this.nextID),
                        Network.get(Integer.parseInt(content[1])));
            }
            else{
                this.send(new Message(Message.JOIN,"I'd like to enter,"+ content[1] +","+content[2]),
                        Network.get(this.nextID));
            }
        }
        if (msg.getType() == Message.PLACE) {
            String[] content = msg.getContent().split(",");
            P2PNode previous = (P2PNode) Network.get(Integer.parseInt(content[1])).getProtocol(0);
            P2PNode next = (P2PNode) Network.get(Integer.parseInt(content[2])).getProtocol(0);
            this.setPrevious(previous.getNodeID(), previous.getNodeUID());
            this.setNext(next.getNodeID(), next.getNodeUID());
            this.send(new Message(Message.NEW_NEXT,"I'm your new next node,"+ this.nodeID +","+this.nodeUID),
                    Network.get(Integer.parseInt(content[1])));
            this.send(new Message(Message.NEW_PREVIOUS,"I'm your new previous node,"+ this.nodeID +","+this.nodeUID),
                    Network.get(Integer.parseInt(content[2])));
        }
        if (msg.getType() == Message.NEW_PREVIOUS) {
            String[] content = msg.getContent().split(",");
            this.setPrevious(Integer.parseInt(content[1]), Integer.parseInt(content[2]));
        }
        if (msg.getType() == Message.NEW_NEXT) {
            String[] content = msg.getContent().split(",");
            this.setNext(Integer.parseInt(content[1]), Integer.parseInt(content[2]));
            this.send(new Message(Message.SHOW,"Display the whole simulator"), Network.get(0));
        }
        if (msg.getType() == Message.SHOW) {
            if (this.nextID!=0) {
                Node recipient = Network.get(this.nextID);
                this.send(new Message(msg.getType(),"Hi from " + this.nodeID), recipient);
            }
        }
        if (msg.getType() == Message.LEAVE){
            System.out.println("We out !");
            this.send(new Message(Message.NEW_NEXT,"I'm your new next node,"+ this.nextID+","+this.nextUID),
                    Network.get(this.previousID));
            this.send(new Message(Message.NEW_PREVIOUS,"I'm your new previous node,"+ this.previousID +","+this.previousUID),
                    Network.get(this.nextID));
        }
        if (msg.getType() == Message.SEND){
            String[] content = msg.getContent().split(",");
            if (Integer.parseInt(content[0]) == this.nodeUID){
                System.out.println(this.nodeID +  " Message received.");
            } else {
                System.out.println(this.nodeID + " Message forwarded to " + this.nextID);
                this.send(msg, Network.get(this.nextID));
            }
        }
    }

    public void setTransportLayer(int nodeID, int nodeUID){
        this.nodeID = nodeID;
        this.nodeUID = nodeUID;
        this.transport = (HWTransport) Network.get(this.nodeID).getProtocol(this.transportID);
    }

    private Node getCurrentNodeID() {
        return Network.get(this.nodeID);
    }

    public String toString(){
        return "Node " + this.nodeID + " Previous node : " + this.previousID + "|" + this.previousUID
                + " Next node : " + this.nextID + this.nextUID;
    }

    public int getNodeID() {
        return this.nodeID;
    }

    public int getNodeUID() {
        return this.nodeUID;
    }

    public void setPrevious(int ID, int UID){
        this.previousID = ID;
        this.previousUID = UID;
    }

    public void setNext(int ID, int UID){
        this.nextID = ID;
        this.nextUID = UID;
    }
}
