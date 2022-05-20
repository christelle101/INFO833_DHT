package helloWorld;

import peersim.config.Configuration;
import java.util.*;
/**
 Based on the Network of peersim
 */
public class Ring {


// ========================= fields =================================
// ==================================================================


    /**
     * This config property defines the node class to be used.
     */
    private static final String PAR_NODE = "Ring.ring";

    /**
     * This config property defines the initial capacity of the overlay network.
     */
    private static final String PAR_MAXSIZE = "network.initialCapacity";

    /**
     * This config property defines the initial size of the overlay network.
     * This property is required.
     * @config
     */
    private static final String PAR_SIZE = "ring.size";

    /**
     * Linked list is better to add node
     */
//static Node[] node = null;
    public static LinkedList<NodeDHT> ring = new LinkedList<>();

    /**
     * Actual size of the network.
     */
    private static int len;

    /**
     * The prototype node which is used to populate the simulation via cloning.
     */
    public static NodeDHT prototype = null;


// ====================== initialization ===========================
// =================================================================


    /**
     * Reads configuration parameters, constructs the prototype node, and
     * populates the network by cloning the prototype.
     */
    public void reset() {

        if( prototype != null )
        {
            // not first experiment
            while( len>0 ) remove(); // this is to call onKill on all nodes
            prototype = null;
            NodeDHT node = new NodeDHT(null);
        }

        len = Configuration.getInt(PAR_SIZE);
        int maxlen = Configuration.getInt(PAR_MAXSIZE,len); // A SUPPRIMER
        if( maxlen < len ) throw new IllegalArgumentException(
                PAR_MAXSIZE+" is less than "+PAR_SIZE);

        //node = new Node[maxlen]; PAS BESOIN CAR CEST UN LINKED LIST

        // creating prototype node
        NodeDHT tmp = new NodeDHT(null);
        if (!Configuration.contains(PAR_NODE))
        {
            System.err.println(
                    "Network: no node defined, using GeneralNode");
            tmp = new NodeDHT("");
        }
        else
        {
            tmp = (NodeDHT) Configuration.getInstance(PAR_NODE);
        }
        prototype = tmp;

        // cloning the nodes
        if(len > 0 )
        {
            for(int i=0; i<len; ++i)
            {
                NodeDHT nodeToAdd = (NodeDHT)prototype.clone();
                join(nodeToAdd);

            }
        }
    }

    /** Disable instance construction */
    private Ring() {}

// =============== public methods ===================================
// ==================================================================

    /** Number of nodes currently in the network */
    public static int size() { return len; }

// ------------------------------------------------------------------

    public LinkedList<NodeDHT> getRing() {
        return this.ring;
    }

// ------------------------------------------------------------------


    /**
     * Returns the target node randomly choose in the ring
     */
    public NodeDHT nodeChoiceInRing(){
        /* Choose randomly the target node */

        int index = (int)(Math.random() * this.getRing().size());
        return this.getRing().get(index);
    }

    /**
     * Add a node when there is only one node in the ring
     */
    public void Order(NodeDHT NodeJoin, NodeDHT TargetNode){
        /* When there is only one node in the ring */

        if (NodeJoin.getId() < TargetNode.getId())
        {
            //add the node to join
            this.getRing().addFirst(NodeJoin);

            // Setting the new neighbours
            TargetNode.setLeftNode(NodeJoin);
            NodeJoin.setRightNode(TargetNode);

        }else{

            // Add the node into the ring
            this.getRing().add(1, NodeJoin);

            // Setting the new neighbors
            TargetNode.setRightNode(NodeJoin);
            NodeJoin.setLeftNode(TargetNode);

        }

    }

    /**
     * Add a node when there is more than one node
     */
    public void OrderIfNodesExists(NodeDHT nodeToInsert, NodeDHT TargetNode)
    {
        int TargetNodeIndex = this.getRing().indexOf(TargetNode);

        // To the right
        if (nodeToInsert.getId() > TargetNode.getId())
        {
            if (TargetNode.getRightNode() == TargetNode)
            {
                this.getRing().add(nodeToInsert);

                nodeToInsert.setLeftNode(TargetNode);
                TargetNode.setRightNode(nodeToInsert);


            }
            else {
                if(nodeToInsert.getId() < TargetNode.getRightNode().getId())
                {
                    this.getRing().add(TargetNodeIndex +1, nodeToInsert);

                    nodeToInsert.setRightNode(TargetNode.getRightNode());
                    TargetNode.getRightNode().setLeftNode(nodeToInsert);
                    nodeToInsert.setLeftNode(TargetNode);
                    TargetNode.setRightNode(nodeToInsert);

                } else{
                    TargetNode = TargetNode.getRightNode();
                    OrderIfNodesExists(nodeToInsert,TargetNode);
                }
            }
        }
        // To the left
        if (nodeToInsert.getId() < TargetNode.getId())
        {
            //
            if(TargetNode.getLeftNode() == TargetNode) {
                this.getRing().addFirst(nodeToInsert);

                nodeToInsert.setRightNode(TargetNode);
                TargetNode.setLeftNode(nodeToInsert);

            }
            else {
                if (nodeToInsert.getId() > TargetNode.getLeftNode().getId()) {

                    this.getRing().add(TargetNodeIndex, nodeToInsert);

                    nodeToInsert.setLeftNode(TargetNode.getLeftNode());
                    nodeToInsert.setRightNode(TargetNode);
                    TargetNode.setLeftNode(nodeToInsert);

                } else {
                    TargetNode = TargetNode.getLeftNode();
                    OrderIfNodesExists(nodeToInsert, TargetNode);
                }

            }
        }
    }

    /**
     * The node will join the ring with the following rules:

     */
    public void join(NodeDHT node){

        if(getRing().isEmpty()){
            this.getRing().add(node); // If empty
        } else {
            NodeDHT TargetNode = null;
            // Case where TargetNode doesn't have neighbours node
            if (getRing().size() == 1) {
                TargetNode = getRing().get(0);
                this.Order(node, TargetNode);
            } else {
                TargetNode = nodeChoiceInRing();
                OrderIfNodesExists(node, TargetNode);
            }

        }
    }

// ------------------------------------------------------------------

    /**
     * Returns node with the given index. Note that the same node will normally
     * have a different index in different times.
     */
    public static NodeDHT get(int index) {

        NodeDHT nodeDHT = ring.get(index);
        return nodeDHT;
    }

// ------------------------------------------------------------------

    /**
     * The node at the end of the list is removed. Returns the removed node.
     */
    public NodeDHT remove() {
        NodeDHT n = null;
        if (!this.getRing().isEmpty()) {
            n = this.getRing().getLast();
            this.getRing().removeLast();
        }
        return n;
    }

// ------------------------------------------------------------------

    /**
     * The node with the given index is removed. Returns the removed node.
     */
    public NodeDHT remove(int i) {

        NodeDHT n = null;
        if (!this.getRing().isEmpty()) {
            n = this.getRing().get(i);
            this.getRing().remove(n);
        }
        return n;
    }

// ------------------------------------------------------------------

    /**
     * The node with the given Node is removed. Returns the removed node.
     */
    public NodeDHT leave(NodeDHT node) {
// check if node exists in ring
        NodeDHT n = null;
        if(this.getRing().contains(node)){
            node.getLeftNode().setRightNode(node.getRightNode());
            node.getRightNode().setLeftNode(node.getLeftNode());
            n = node;
            this.getRing().remove(node);
        }
        return n;
    }


// ------------------------------------------------------------------

    public String toString()
    {
        String ring = "NodeDHT.Ring: ";
        for (NodeDHT node: this.getRing()){
            ring += node.getId() + " - ";
            //System.out.println(node.getId()+ " - ");
        }
        return ring;
    }
}

