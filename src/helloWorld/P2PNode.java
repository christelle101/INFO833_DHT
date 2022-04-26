package helloWorld;

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
    public Object clone() {
        return null;
    }

    @Override
    public void processEvent(peersim.core.Node node, int pid, Object event) {

    }
}
