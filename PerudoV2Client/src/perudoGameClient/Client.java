package perudoGameClient;

import java.util.ArrayList;
import java.util.Scanner;


/**
 * 
 * @author Pereira
 * @version 1
 *
 */
public class Client {
	
	public static void envoiDemande(String ipdu, ConnexionClient connexion){
		connexion.envoyer(ipdu);
	}
	
	public static ArrayList<String> receptionDemande(ConnexionClient connexion){
		ArrayList<String> requete = new ArrayList<>();
		PDU iPDU = new PDU();
		
		requete = iPDU.decomposer(connexion.recevoir());
		
		return requete;
	}
	
	public static ArrayList<String> faireDemande(ConnexionClient connexion, String ipdu, GestionProtocoleClient gP){
		int i;
		ArrayList<String> rep;
		envoiDemande(ipdu, connexion);
		rep = receptionDemande(connexion);
		i = gP.repAttendue(rep,ipdu);
		if(i == 1){
			return rep;
		}
		else{
			return null;
		}
	}

	
	public static void main (String args[]) {
		
		ConnexionClient connexion = new ConnexionClient();
		GestionProtocoleClient gP = new GestionProtocoleClient();
		ArrayList<String> reponses = new ArrayList<String>();
		Scanner reader = new Scanner(System.in);
		int choix;
		
		connexion.ConnecterServeur();
		
		reponses = faireDemande(connexion, PDU.LISTROOMS, gP);
		System.out.println("Voici la liste des Salles disponibles.");
		for (String s : reponses.subList(1, reponses.size())){
			System.out.println(s);
		}
		
		do{
			System.out.println("Vous pouvez soit:");
			System.out.println("Taper 1 : Créer Partie");
			System.out.println("Taper 2 : Rejoindre Partie");
			System.out.println("Taper 3 : Quitter serveur");
			choix = reader.nextInt();
			switch(choix){
				case 1:
					reponses = faireDemande(connexion, PDU.CREATE_PARTY, gP);
					if( gP.isPositive(reponses.get(0)) ){
						System.out.println("Vous venez de creer une partie!");
						Partie p1 = new Partie(Integer.parseInt(reponses.get(1)));
						p1.setStatus(PDU.WAITING);
						reponses = faireDemande(connexion,PDU.GET_ID, gP);

						
					}
					else{
						System.out.println("Le serveur a rejeté votre demande de creation");
						choix = 0;
					}
					break;
				default:
					System.out.println("Ressayez:");
			}
			
		}while(choix < 1 || choix > 3);
		reader.close();
		
		
		
	
		
		
		
		
		
		
		connexion.FermerConnexionServeur();
	}
}
