package perudoGame;

import java.net.ServerSocket;

public class Serveur {

	public Serveur() {
		
	}

	public static void main(String[] args) {
		ConnexionServeur cxServeur;
		int port = 27019;
		ServerSocket s = ConnexionServeur.ouvrir(port);
		System.out.println("[Serveur v0] Actif");
		
		while (s != null){
			cxServeur = new ConnexionServeur(s);
			Thread t = new Thread(cxServeur);
			t.start();
		}
	}

}
