package helloWorld;

import peersim.config.Configuration;
import peersim.core.Network;
import peersim.core.Node;
import peersim.core.Protocol;
import peersim.edsim.EDProtocol;

import java.util.Random;

public class NodeDHT implements EDProtocol {

    //identifiant du noeud
    private int id;

    //identifiant de la couche transport
    private int transportPid;

    //objet couche transport
    private HWTransport transport;

    //identifiant de la couche courante (la couche applicative)
    private int mypid;

    //le numero de noeud
    private int nodeId;

    //prefixe de la couche (nom de la variable de protocole du fichier de config)
    private String prefix;

    // voisins gauche et droite
    private NodeDHT LeftNode;
    private NodeDHT RightNode;
    /**
     * Prefix of the parameters that defines protocols.
     * @config
     */
    public static final String PAR_PROT = "protocol";

    /**
     * Returns the <code>i</code>-th protocol in this node. If <code>i</code>
     * is not a valid protocol id
     * (negative or larger than or equal to the number of protocols), then it throws
     * IndexOutOfBoundsException.
     */
    public Protocol getProtocol(int i) {
       return null; // Je ne sais pas sous quel critère on retourne tel ou tel protocol
    }

    public NodeDHT(String prefix) {
        Random rand = new Random();
        this.id = rand.nextInt(5000);
	//initialisation des identifiants a partir du fichier de configuration
        this.prefix = prefix;
        this.transportPid = Configuration.getPid(prefix + ".transport");;
        this.mypid = Configuration.getPid(prefix + ".myself");
        this.transport = null;
        this.LeftNode = this;
        this.RightNode = this;
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

    public void setLeftNode(NodeDHT leftNode) {
        LeftNode = leftNode;
    }

    public void setRightNode(NodeDHT rightNode) {
        RightNode = rightNode;
    }


    //methode appelee lorsqu'un message est recu par le protocole HelloWorld du noeud
    public void processEvent( Node node, int pid, Object event ) {
	this.receive((Message)event);
    }
    
    //methode necessaire pour la creation du reseau (qui se fait par clonage d'un prototype)
    public Object clone() {

        NodeDHT dolly = new NodeDHT(this.prefix);
        return dolly;
    }

    //liaison entre un objet de la couche applicative et un 
    //objet de la couche transport situes sur le meme noeud
    public void setTransportLayer(int nodeId) {
	this.nodeId = nodeId;
	this.transport = (HWTransport) Ring.get(this.nodeId).getProtocol(this.transportPid);
    }

    //envoi d'un message (l'envoi se fait via la couche transport)
    public void send(Message msg, NodeDHT dest) {
	this.transport.send(getMyNode(), dest, msg, this.mypid);
    }

    //affichage a la reception
    private void receive(Message msg) {
	System.out.println(this + ": Received " + msg.getContent());
    }

    //retourne le noeud courant
    private Node getMyNode() {
	return Network.get(this.nodeId);
    }

    public String toString() {
	return "Node "+ this.nodeId;
    }

    
}