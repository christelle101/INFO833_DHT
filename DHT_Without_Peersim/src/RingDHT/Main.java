package RingDHT;

public class Main {

    public static void main(String[] args) {

        NodeDHT N1 = new NodeDHT();
        NodeDHT N2 = new NodeDHT();
        NodeDHT N3 = new NodeDHT();
        NodeDHT N4 = new NodeDHT();
        NodeDHT N5 = new NodeDHT();
        NodeDHT N6 = new NodeDHT();

        System.out.println(" N1: "+ N1.getId()+" N2: "+ N2.getId()+" N3: "+ N3.getId()+" N4: "+ N4.getId()+" N5: "+ N5.getId()+" N6: "+ N6.getId());

        Ring ring = new Ring();
        // When the ring is empty
        System.out.println(ring.toString());

        // When there is one node
        ring.join(N1);
        System.out.println(ring.toString());

        // When there is 2 node
        ring.join(N2);
        System.out.println(ring.toString());

        // When there is more than 2 nodes
        ring.join(N3);
        System.out.println(ring.toString());
        ring.join(N4);

        System.out.println(ring.toString());
        System.out.println("Voisins Après Ajout: ");
        NodeDHT voisin1 = N4.getLeftNode();
        System.out.println(voisin1.getId());

        System.out.println("Voisin 1 Gauche: "+voisin1.getLeftNode().getId());
        System.out.println("Voisin 1 Droite: "+voisin1.getRightNode().getId());


        ring.join(N5);
        System.out.println(ring.toString());
        ring.join(N6);
        System.out.println(ring.toString());

        ring.leave(N4);
        System.out.println(ring.toString());
        System.out.println("Voisins Après Retrait: ");
        System.out.println("Voisin 1 Gauche: "+voisin1.getLeftNode().getId());
        System.out.println("Voisin 1 Droite: "+voisin1.getRightNode().getId());

        ring.leave(3);
        System.out.println(ring.toString());
        System.out.println("Voisins Après Retrait: ");
        System.out.println("Voisin 1 Gauche: "+voisin1.getLeftNode().getId());
        System.out.println("Voisin 1 Droite: "+voisin1.getRightNode().getId());


    }

}
