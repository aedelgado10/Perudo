package perudo;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * 
 * @author Pereira William
 * @author Delgado Andrés
 * @author Keraghel Nady
 * @version : 2
 * @
 */

public class PDU {
	
	/*PDU de création de partie */
	public static final String CREATE_PARTY = "CREATE_PARTY"; 		// Créer une partie
	public static final String OKPARTY = "OK_PARTY";					// Création partie avec succès(serveur)
	public static final String REJPARTY = "REJ_PARTY";				// Refuser création partie
	
	/* Rejoindre une partie */
	public static final String JOIN_PARTY = "JOIN_PARTY";			// Join partie accepté
	public static final String JOIN_OK = "JOIN_OK";					// Joindre partie
	public static final String JOIN_REJ = "JOIN_REJ";				// Join partie refusé
	public static final String PARTYPLAYING = "PARTYPLAYING";		// informer que la partie est en cours
	public static final String WAITING = "WAITING_PLAYERS";			// En attente de joueurs
	public static final String FULL = "FULL_PARTY";					// La partie est complète
	public static final String ALREADY = "ALREADY_IN";				// A déjà rejoint la partie
	
	/* Annuler une partie */
	public static final String STOP_PARTY = "STOP_PARTY";			// Annuler la partie (à tout moment);
	public static final String PARTY_CANCELLED = "PARTY_CANCELLED"; // La partie a été annulée
	
	/* Quitter une partie (en cours ou non) */
	public static final String LEAVE = "LEAVE_PARTY";				// Quitter la partie
	public static final String OK_LEAVE = "OK_LEAVE";				// RECU !
	
	/* Lister les salles */
	public static final String LISTROOMS = "LIST_ROOMS";				// Demander liste des salles
	public static final String ROOMS = "ROOMS"; 					// Retour de la liste des salles disponibles
	public static final String NO_ROOMS = "NO_ROOMS";
	
	/* Lancer une partie */
	public static final String LAUNCH = "LAUNCH";					// Démarrer partie
	public static final String BEGIN_PARTY = "BEGIN_PARTY";			//Partie lancée !
	public static final String NOFNP = "NO_FRIENDS_NO_PARTY";		// On démarre pas une partie avec <2 joueurs
	public static final String PLS = "PLS";							// Le client n'est pas prêt
	/* Lister les couleurs */
	public static final String WHICH_COLOR = "COLOR?";				// demander sa couleur
	public static final String COLOR = "COLOR";
	
	/* Lancer les dés */
	public static final String ROLL = "ROLL";						// Lancer les dés
	public static final String DICES = "DICES";						// valeurs des dés
	
	/* Faire des annonces */
	public static final String TTPILE = "TTPILE";					// Dire un toutpile
	public static final String TTPILE_OK = "TTPILE_OK";				// Répondre tout pile reussi
	public static final String TTPILE_NOK = "TTPILE_NOK";			// Répondre ce n'est pas tout pile
	public static final String LIAR = "LIAR";						// Dire Menteur
	public static final String LIAR_OK = "LIAR_OK";					// Il ment 
	public static final String LIAR_NOK = "LIAR_NOK";				// Il mentait pas 
	public static final String PLUS = "PLUS";						// Surencherir
	public static final String APLUS = "APLUS";						// Annoncer quel jouer surenchère et de combien 
	
	/* Créer un pseudo */
	public static final String PSEUDOP = "PSEUDO";					// Etablir pseudo
	public static final String PSEUDO_OK = "PSEUDO_OK";				// Demande pseudo réussie
	public static final String PSEUDO_KO = "PSEUDO_KO";
	
	/* ID */
	public static final String GET_ID = "GET_ID"; 					// Récupérer ID	
	public static final String ID = "ID";							// ID x
	
	/* Demande incomprise */
	public static final String WHATSUP = "WHATSUP";					// Le serveur n'a pas compris
	
	/* Demande mal-placée */
	public static final String DEBUG_IT = "DEBUG_IT";				// Ca ne devrait pas arriver
	public static final String QUIT = "QUIT";
	
	public ArrayList<String> decomposer(String cmd){
		ArrayList<String> requete = new ArrayList<String>();
		StringTokenizer s = new StringTokenizer(cmd);
		
		while (s.hasMoreTokens()){
			requete.add(s.nextToken());
		}
		
		return requete;
	}
	
	public ArrayList<String> parseListRooms(String liste){
		
		ArrayList<String> roomInfo = new ArrayList<String>();
		StringTokenizer s = new StringTokenizer(liste, ":");
		
		while (s.hasMoreTokens()){
			roomInfo.add(s.nextToken());
		}
		
		return roomInfo;
	}
}
