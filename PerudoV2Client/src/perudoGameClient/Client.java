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
	
	private GestionProtocoleClient gPc;
	private Partie p1;
	private Joueur j1;
	
	public Client(){
		this.gPc = new GestionProtocoleClient();
	}
	
	public GestionProtocoleClient getGPC(){
		return this.gPc;
	}
	
	public Partie getP1() {
		return p1;
	}

	public void setP1(Partie p1) {
		this.p1 = p1;
	}

	public Joueur getJ1() {
		return j1;
	}

	public void setJ1(Joueur j1) {
		this.j1 = j1;
	}
	
	public void envoiDemande(String ipdu, ConnexionClient connexion){
		connexion.envoyer(ipdu);
	}
	
	public ArrayList<String> receptionDemande(ConnexionClient connexion){
		ArrayList<String> requete = new ArrayList<>();
		PDU iPDU = new PDU();
		
		requete = iPDU.decomposer(connexion.recevoir());
		
		return requete;
	}
	
	public ArrayList<String> faireDemande(ConnexionClient connexion, String ipdu){
		int i;
		ArrayList<String> rep;
		envoiDemande(ipdu, connexion);
		rep = receptionDemande(connexion);
		i = this.getGPC().repAttendue(rep,ipdu);
		if(i == 1){
			return rep;
		}
		else{
			return null;
		}
	}
	
	public int menuPrincipal(){
		Scanner reader = new Scanner(System.in);
		int choix;
		
		System.out.println("Vous pouvez soit:");
		System.out.println("Taper 1 : Créer Partie");
		System.out.println("Taper 2 : Rejoindre Partie");
		System.out.println("Taper 3 : Quitter serveur");
		choix = reader.nextInt();
		
		reader.close();
		return choix;
	}

	
	public static void main (String args[]) {
		
		Client client = new Client();
		ConnexionClient connexion = new ConnexionClient(client);
		connexion.ConnecterServeur();

		int choix;
		ArrayList<String> reponse = new ArrayList<String>();
		
		do{
			choix = client.menuPrincipal();
			switch (choix){
				case 1:
					System.out.println("Vous avez choisi de creer une Partie.");
					reponse = client.faireDemande(connexion, client.getGPC().createParty());
					if(client.getGPC().isPositive(reponse.get(0))){
						client.setP1(new Partie(Integer.parseInt(reponse.get(1))));
						System.out.println("Partie Crée avec Succès");
					}
					else{
						System.out.println("Le serveur a rejeté votre demande de creation.");
						choix = 0;
					}	
					break;
				default:
					System.out.println("Vous devez choisir un des trois choix proposés");
					break;
			}
	    }while(choix < 1 || choix > 3);
		
		/*reponses = faireDemande(connexion, PDU.LISTROOMS, gP);
		System.out.println("Voici la liste des Salles disponibles.");
		for (String s : reponses.subList(1, reponses.size())){
			System.out.println(s);
		}*/
		
		
		/*choix = client.menuPrincipal();
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
					}
					break;
				default:
					System.out.println("Ressayez:");
			}*/

		connexion.FermerConnexionServeur();
	}
}
