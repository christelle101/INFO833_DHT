package RingDHT;

import java.util.*;

public class Ring {

    public static final String ERROR_MSG_SENT = "Le message n'a pas été envoyé aux noeuds voisins";;
    private LinkedList<NodeDHT> ring = new LinkedList<>();

    private NodeDHT currentNode;

    Ring(){
        this.ring = new LinkedList<NodeDHT>();

    }

    public LinkedList<NodeDHT> getRing() {
        return this.ring;
    }

    public NodeDHT getCurrentNode() { return this.currentNode; }

    public void setCurrentNode(NodeDHT currentNode) {
        this.currentNode = currentNode;
    }

    public NodeDHT nodeChoiceInRing(){
        /* Choose randomly the target node */

        int index = (int)(Math.random() * this.getRing().size());
        return this.getRing().get(index);
    }


    public void Order(NodeDHT NodeJoin, NodeDHT TargetNode){
        /* When there is only one node in the ring */

        this.setCurrentNode(TargetNode);

        if (NodeJoin.getId() < TargetNode.getId())
        {
            //add the node to join
            this.getRing().addFirst(NodeJoin);

            // Setting the new neighbours
            TargetNode.setLeftNode(NodeJoin);
            NodeJoin.setRightNode(TargetNode);
            this.setCurrentNode(NodeJoin);

            Message msg = new Message(0);
            //ENVOI
            boolean sentRN = send(msg,NodeJoin.getRightNode());
            boolean sentLN = send(msg,NodeJoin.getLeftNode());
            //S'ASSURER DE RECEPTION
            if (sentRN && sentLN) {
                msg = new Message(1);
                receive(msg, NodeJoin.getRightNode());
                receive(msg, NodeJoin.getLeftNode());
            } else {
                System.out.println(ERROR_MSG_SENT);
            }

        }else{

            // Add the node into the ring
            this.getRing().add(1, NodeJoin);

            // Setting the new neighbours
            TargetNode.setRightNode(NodeJoin);
            NodeJoin.setLeftNode(TargetNode);

            this.setCurrentNode(NodeJoin);

            Message msg = new Message(0);
            //ENVOI
            boolean sentRN = send(msg,NodeJoin.getRightNode());
            boolean sentLN = send(msg,NodeJoin.getLeftNode());
            //S'ASSURER DE RECEPTION
            if (sentRN && sentLN) {
                msg = new Message(1);
                receive(msg, NodeJoin.getRightNode());
                receive(msg, NodeJoin.getLeftNode());
            } else {
                System.out.println(ERROR_MSG_SENT);
            }
        }


    }

    public void OrderIfNodesExists(NodeDHT nodeToInsert, NodeDHT TargetNode)
    {
        int TargetNodeIndex = this.getRing().indexOf(TargetNode);

        this.setCurrentNode(TargetNode);
        // To the right
        if (nodeToInsert.getId() > TargetNode.getId())
        {
            if (TargetNode.getRightNode() == TargetNode) // Dans le cas de 2 noeuds dans l'anneau
            {
                this.getRing().add(nodeToInsert);

                nodeToInsert.setLeftNode(TargetNode);
                TargetNode.setRightNode(nodeToInsert);

                this.setCurrentNode(nodeToInsert);

                Message msg = new Message(0);
                //ENVOI
                boolean sentRN = send(msg,nodeToInsert.getRightNode());
                boolean sentLN = send(msg,nodeToInsert.getLeftNode());
                //S'ASSURER DE RECEPTION
                if (sentRN && sentLN) {
                    msg = new Message(1);
                    receive(msg, nodeToInsert.getRightNode());
                    receive(msg, nodeToInsert.getLeftNode());
                } else {
                    System.out.println(ERROR_MSG_SENT);
                }
            }
            else {
                if(nodeToInsert.getId() < TargetNode.getRightNode().getId())
                {
                    this.getRing().add(TargetNodeIndex +1, nodeToInsert);

                    nodeToInsert.setRightNode(TargetNode.getRightNode());
                    TargetNode.getRightNode().setLeftNode(nodeToInsert);
                    nodeToInsert.setLeftNode(TargetNode);
                    TargetNode.setRightNode(nodeToInsert);

                    this.setCurrentNode(nodeToInsert);

                    Message msg = new Message(0);
                    //ENVOI
                    boolean sentRN = send(msg,nodeToInsert.getRightNode());
                    boolean sentLN = send(msg,nodeToInsert.getLeftNode());
                    //S'ASSURER DE RECEPTION
                    if (sentRN && sentLN) {
                        msg = new Message(1);
                        receive(msg, nodeToInsert.getRightNode());
                        receive(msg, nodeToInsert.getLeftNode());
                    } else {
                        System.out.println(ERROR_MSG_SENT);
                    }


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

                this.setCurrentNode(nodeToInsert);

                Message msg = new Message(0);
                //ENVOI
                boolean sentRN = send(msg,nodeToInsert.getRightNode());
                boolean sentLN = send(msg,nodeToInsert.getLeftNode());
                //S'ASSURER DE RECEPTION
                if (sentRN && sentLN) {
                    msg = new Message(1);
                    receive(msg, nodeToInsert.getRightNode());
                    receive(msg, nodeToInsert.getLeftNode());
                } else {
                    System.out.println(ERROR_MSG_SENT);
                }

            }
            else {
                if (nodeToInsert.getId() > TargetNode.getLeftNode().getId()) {

                    this.getRing().add(TargetNodeIndex, nodeToInsert);

                    nodeToInsert.setLeftNode(TargetNode.getLeftNode());
                    nodeToInsert.setRightNode(TargetNode);
                    TargetNode.setLeftNode(nodeToInsert);

                    this.setCurrentNode(nodeToInsert);

                    Message msg = new Message(0);
                    //ENVOI
                    boolean sentRN = send(msg,nodeToInsert.getRightNode());
                    boolean sentLN = send(msg,nodeToInsert.getLeftNode());
                    //S'ASSURER DE RECEPTION
                    if (sentRN && sentLN) {
                        msg = new Message(1);
                        receive(msg, nodeToInsert.getRightNode());
                        receive(msg, nodeToInsert.getLeftNode());
                    } else {
                        System.out.println(ERROR_MSG_SENT);
                    }

                } else {
                    TargetNode = TargetNode.getLeftNode();
                    OrderIfNodesExists(nodeToInsert, TargetNode);
                }

            }
        }

        // S'assurer que le premier noeud et le dernier noeud sont toujours voisins
        // dans le cas où on a le nb de noeud >=3
        /*if (this.getRing().size() >=3 ){
            this.getRing().getFirst().setLeftNode(this.getRing().getLast());
            this.getRing().getLast().setRightNode(this.getRing().getFirst());
        }*/

    }

    public void join(NodeDHT node){

        if(getRing().isEmpty()){
            this.getRing().add(node); // If empty
            this.setCurrentNode(node);
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

    public void leave(NodeDHT node)
    {
        // check if node exists in ring
        if(this.getRing().contains(node)){
           node.getLeftNode().setRightNode(node.getRightNode());
           node.getRightNode().setLeftNode(node.getLeftNode());
           this.getRing().remove(node);
        }
    }

    //leave with index
    public NodeDHT leave(int index)
    {
        NodeDHT nodeLeft = null;
        // check if node exists in ring
        if(index >= 0){
            nodeLeft = this.getRing().get(index);
            this.getRing().remove(index);
        }
        return nodeLeft;
    }


    public boolean send(Message msg, NodeDHT nodeDest){
        boolean sent = false;
        // Send a message
        if(msg != null)
        {
            Pair p = new Pair(msg, this.currentNode);
            nodeDest.getTabMsg().add(p);
            int index = nodeDest.getTabMsg().indexOf(p);
            Pair pp = (Pair) nodeDest.getTabMsg().get(index);
            System.out.println("Node: "+ this.getCurrentNode().getId()+" "+pp.getMsg().getContent()+" to "+nodeDest.getId());
            sent = true;
        } else {
            System.out.println("Message is null");
        }
        return sent;
    }

    public void receive(Message msg,NodeDHT nodeDest){
        nodeDest.setMsgRecieved(true);
        // Display once received
        System.out.println(nodeDest.getId() + " " + msg.getContent()+ " from " + "Node: "+ this.getCurrentNode().getId());

    }


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
