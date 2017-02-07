package perudo;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Enumeration;

public class GestionProtocole extends PDU{
	
	public String traiter(String cmd,Socket client,Partie p){
		
		/* Décomposition de la commande en requete et arguments */
		ArrayList<String> req_args = this.decomposer(cmd);
		String requete = req_args.get(0);
		
		if (p.partieExists()){
			switch(p.getStatus()){
			
				case PDU.WAITING:
					
					switch(requete){
						
						case PDU.JOIN_PARTY:
							
							String resultat = p.rejoindrePartie(client);
							if (resultat.equals("0")){
								return GestionProtocole.JOIN_REJ;
							}else{
								return resultat;
							}
							
						case PDU.GET_ID:
							if (p.isDejaDansPartie(client)){
								return GestionProtocole.ID + " " + p.getListeJoueurs().get(client).getID();
							}else{
								return WHATSUP + " il faut d'abord rejoindre la partie!";
							}
						
						case PDU.CREATE_PARTY:
							return REJPARTY;
						
						case PSEUDOP:
							if (req_args.size() != 2){
								return PSEUDO_KO;
							}else{
								p.getJoueur(client).setPseudo(req_args.get(1));
								return PSEUDO_OK;
							}
							
						case PDU.LISTROOMS:
							return ROOMS + " " + p.getID()+ ":"+ p.getStatus() + ":"+ p.getNbJoueurs()+ "/6";
							
						case WHICH_COLOR:
							return COLOR + " " + p.setCouleur(p.getJoueur(client));
							
						case LAUNCH:
							if (p.isCreateur(p.getJoueur(client))){
								if ((p.isFriendlyParty())){
									Enumeration<Joueur> e = p.getListeJoueurs().elements();
									
									while (e.hasMoreElements()){
										if (e.nextElement().getCouleur().getCodeCouleur().equals("undefined")){
											return PLS;
										}
									}
									p.setEnCours();
							
									return BEGIN_PARTY;
								}else{
									return NOFNP;
								}
							}else{
								return WHATSUP + " Vous n'avez pas le droit d'effectuer cette action!";
							}
						default:
							return GestionProtocole.WHATSUP;
					}
					
				case COLOR:
					
				
				case PDU.FULL:			//La partie est pleine
					return GestionProtocole.FULL;
				
				case PDU.PARTYPLAYING:// partie en cours!
					
					return GestionProtocole.PARTYPLAYING;
				
				default:
					return GestionProtocole.WHATSUP;
			}
		}else{	// Aucune partie n'a été créée
			switch(requete){
				case PDU.LISTROOMS:
					return GestionProtocole.NO_ROOMS;
				case PDU.CREATE_PARTY:
					p.creerPartie();
					p.ajouterJoueur(client);
					p.setCreateur(p.getJoueur(client));
					return OKPARTY + " " + p.getID();

				case PDU.GET_ID:
					return WHATSUP + " Il faut d'abord créer ou rejoindre la partie !";
				default:
					return GestionProtocole.WHATSUP;
			}
		}
	}
}
