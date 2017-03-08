
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
	public static final String NOPARTIES = "NOPARTIES";             // pas de parties crees
	public static final String JOININGPARTY = "JOININGPARTY";        // en train de joindre une partie
	public static final String CREATINGPARTY = "CREATINGPARTY";      // en train de creer partie
	
	/* Annuler une partie */
	public static final String STOP_PARTY = "STOP_PARTY";			// Annuler la partie (à tout moment);
	public static final String PARTY_CANCELLED = "PARTY_CANCELLED"; // La partie a été annulée
	
	/* Quitter une partie (en cours ou non) */
	public static final String LEAVE = "LEAVE_PARTY";				// Quitter la partie
	public static final String OK_LEAVE = "OK_LEAVE";               // RECU !
    public static final String QUIT = "QUIT";                       //tout quitter
    public static final String RAGEQUIT = "RQUIT";                  // quand on ferme de façon brutale
	
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
	public static final String WHICH_COLOR = "COLOR?";				// demander couleurs dispos
	public static final String COLORLIST = "COLORLIST";				// retour liste des couleurs
	public static final String COLOR = "COLOR";
	public static final String COLOR_OK = "COLOR_OK";
	public static final String COLOR_KO = "COLOR_KO";
	
	/* Lancer les dés */
	public static final String ROLL = "ROLL";						// Lancer les dés
	public static final String DICES = "DICES";						// valeurs des dés
	public static final String NO_DICES = "NDICES";
	
	/* Faire des annonces */
	public static final String TTPILE = "TTPILE";					// Dire un toutpile
	public static final String TTPILE_OK = "TTPILE_OK";				// Répondre tout pile reussi
	public static final String TTPILE_NOK = "TTPILE_NOK";			// Répondre ce n'est pas tout pile
	public static final String LIAR = "LIAR";						// Dire Menteur
	public static final String LIAR_OK = "LIAR_OK";					// Il ment 
	public static final String LIAR_NOK = "LIAR_NOK";				// Il mentait pas 
	public static final String PLUS = "PLUS";						// Surencherir
	public static final String APLUS = "APLUS";						// Annoncer quel jouer surenchère et de combien 
	public static final String FANNOUNCE = "FANNOUNCE";             // Premiere annonce
	//public static final String SHOUT = "SHOUT";                   // Annonce du serveur (joueur + val + nbdes)
	
	/* Créer un pseudo */
	public static final String PSEUDOP = "PSEUDO";					// Etablir pseudo
	public static final String PSEUDO_OK = "PSEUDO_OK";				// Demande pseudo réussie
	public static final String PSEUDO_KO = "PSEUDO_KO";
	
	/* ID */
	public static final String GET_ID = "GET_ID"; 					// Récupérer ID	
	public static final String ID = "ID";							// ID x
	
	/*LISTE PLAYERS*/
	public static final String LISTPLAYERS = "LISTPLAYERS";         // lister les joueurs
	public static final String PLAYERSIN = "PLAYERSIN";             // affichage
	
	/*À QUI DE JOUER*/
	public static final String PLAY1ST = "PLAY1ST";                 // indique qui es le premier a jouer
	public static final String PLAY = "PLAY";                       // indique qui sont les suivants
	public static final String WHO1 = "1ST?";                       // demande qui commence
	public static final String WHO = "WHOPLAY";                     // demande le tour
	
	/*VAINQUEUR / VAINCU*/
	public static final String LOSER = "LOSER";                     // Joueur eliminé
 	public static final String WINNER = "WINNER";                   // Joueur Gagnant
	
	/* Demande incomprise par le serveur*/
	public static final String WHATSUP = "WHATSUP";					// Le serveur n'a pas compris
	
	/* Demande mal-placée */
	public static final String DEBUG_IT = "DEBUG_IT";				// Ca ne devrait pas arriver
	
	
	/**
	 * Découpe la reponse du serveur
	 * @param cmd reception du serveur
	 * @return un ArrayList avec 2 cases. PDU + Arguments s'il y en a
	 */
	public ArrayList<String> decomposer(String cmd){
		ArrayList<String> requete = new ArrayList<String>();
		StringTokenizer s = new StringTokenizer(cmd);
		
		while (s.hasMoreTokens()){
			requete.add(s.nextToken());
		}
		
		return requete;
	}
	
	/*Découpe la réponse du serveur en fonction d'un délimiteur*/
	public ArrayList<String> parseLists(String liste, String delimiter){
		
		ArrayList<String> roomInfo = new ArrayList<String>();
		StringTokenizer s = new StringTokenizer(liste, delimiter);
		
		while (s.hasMoreTokens()){
			roomInfo.add(s.nextToken());
		}
		
		return roomInfo;
	}
	
}

