package helloWorld;

import peersim.edsim.*;
import peersim.core.*;
import peersim.config.*;
import java.util.Random;


/*
  Module d'initialisation de helloWorld:
  Fonctionnement:
    pour chaque noeud, le module fait le lien entre la couche transport et la couche applicative
    ensuite, il fait envoyer au noeud 0 un message "Hello" a tous les autres noeuds
 */
public class Initializer implements peersim.core.Control {

	private int helloWorldPid;

	public Initializer(String prefix) {
		//recuperation du pid de la couche applicative
		this.helloWorldPid = Configuration.getPid(prefix + ".helloWorldProtocolPid");
	}

	public boolean execute() {
		int nodeNb;
		//HelloWorld emitter, current;
		P2PNode first_node;
		P2PNode current_node;
		Node dest;
		//Message helloMsg;

		Random rand = new Random();
		rand.setSeed(8465665);

		//recuperation de la taille du reseau
		nodeNb = Network.size();

		//creation du message
		//helloMsg = new Message(Message.HELLOWORLD,"Hello!!");

		if (nodeNb < 1) {
			System.err.println("Network size is not positive");
			System.exit(1);
		}

		//recuperation de la couche applicative de l'emetteur (le noeud 0)
		first_node = (P2PNode)Network.get(0).getProtocol(this.helloWorldPid);
		first_node.setTransportLayer(0, 0);

		System.out.println("\nHere are our nodes with their UID :");
		//pour chaque noeud, on fait le lien entre la couche applicative et la couche transport
		//puis on fait envoyer au noeud 0 un message "Hello"
		for (int i = 1; i < nodeNb; i++) {
			dest = Network.get(i);
			current_node = (P2PNode)dest.getProtocol(this.helloWorldPid);
			current_node.setTransportLayer(i, rand.nextInt(10000));
			System.out.println("	" + current_node.getNodeID() + " : " + current_node.getNodeUID());
		}

		P2PNode second_node = (P2PNode) Network.get(1).getProtocol(this.helloWorldPid);
		P2PNode third_node = (P2PNode) Network.get(2).getProtocol(this.helloWorldPid);

		if (second_node.getNodeUID() < third_node.getNodeUID()){
			first_node.setNext(second_node.getNodeID(), second_node.getNodeUID());
			first_node.setPrevious(third_node.getNodeID(), third_node.getNodeUID());
			second_node.setNext(third_node.getNodeID(), third_node.getNodeUID());
			second_node.setPrevious(first_node.getNodeID(), first_node.getNodeUID());
			third_node.setNext(first_node.getNodeID(), first_node.getNodeUID());
			third_node.setPrevious(second_node.getNodeID(), second_node.getNodeUID());
		} else {
			first_node.setPrevious(second_node.getNodeID(), second_node.getNodeUID());
			first_node.setNext(third_node.getNodeID(), third_node.getNodeUID());
			second_node.setPrevious(third_node.getNodeID(), third_node.getNodeUID());
			second_node.setNext(first_node.getNodeID(), first_node.getNodeUID());
			third_node.setPrevious(first_node.getNodeID(), first_node.getNodeUID());
			third_node.setNext(second_node.getNodeID(), second_node.getNodeUID());
		}

		System.out.println("\nThe 3 first active nodes are :");
		System.out.println("	" + first_node);
		System.out.println("	" + second_node);
		System.out.println("	" + third_node);

		EDSimulator.add(100, new Message(Message.ACTIVATE,"Wake up !"), Network.get(3),0);
		EDSimulator.add(1500, new Message(Message.ACTIVATE,"Wake up !"), Network.get(4),0);
		EDSimulator.add(3000, new Message(Message.SEND,"3305,This is a message for number 3305"), Network.get(0),0);
		EDSimulator.add(4500, new Message(Message.LEAVE,"Leave !"), Network.get(1),0);

		System.out.println("Initialization completed");
		return false;
	}
}