package helloWorld;

import peersim.edsim.*;
import peersim.core.*;
import peersim.config.*;


//CHECKER MODIF EN RAJOUTANT RING A LA PLACE DE NETWORK

/*
  Module d'initialisation de helloWorld:
  Fonctionnement:
    pour chaque noeud, le module fait le lien entre la couche transport et la couche applicative
    ensuite, il fait envoyer au noeud 0 un message "Hello" a tous les autres noeuds
 */
public class Initializer implements peersim.core.Control {

	private int NodeDHTPid;

	public Initializer(String prefix) {
		//recuperation du pid de la couche applicative
		this.NodeDHTPid = Configuration.getPid(prefix + ".NodeDHTPid");
	}

	public boolean execute() {
		int nodeNb;
		NodeDHT emitter, current;
		NodeDHT dest;
		Message helloMsg;

		//recuperation de la taille du reseau
		nodeNb = Ring.size(); //Network.size() AVANT
		//creation du message
		helloMsg = new Message(Message.HELLOWORLD,"Hello!!");
		if (nodeNb < 1) {
			System.err.println("Network size is not positive");
			System.exit(1);
		}

		//recuperation de la couche applicative de l'emetteur (le noeud 0)
		emitter = (NodeDHT) Ring.get(0).getProtocol(this.NodeDHTPid); //(HelloWorld)Network.get(0).getProtocol(this.NodeDHTPid)
		emitter.setTransportLayer(0);

		// code pour join Node dans la Ring

		//pour chaque noeud, on fait le lien entre la couche applicative et la couche transport
		//puis on fait envoyer au noeud 0 un message "Hello"
		for (int i = 1; i < nodeNb; i++) {
			dest = Ring.get(i);
			current = (NodeDHT) dest.getProtocol(this.NodeDHTPid); //(HelloWorld)dest.getProtocol(this.NodeDHTPid)
			current.setTransportLayer(i);
			emitter.send(helloMsg, dest);
		}

		System.out.println("Initialization completed");
		return false;
	}
}