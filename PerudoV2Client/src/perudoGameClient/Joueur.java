package perudoGameClient;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * 
 * @author Pereira-Delgado
 *
 */
public class Joueur {
	private String pseudo;
	private int id;
	private boolean leader;
	Gobelet gobelet;
	private ArrayList<Integer> valeursChoisies;
	
	public Joueur(int id,Couleur c, boolean l){
		this.id = id;
		this.gobelet = new Gobelet(c);
		this.leader = l;
		this.valeursChoisies = new ArrayList<>();
	}

	public String getPseudo() {
		return pseudo;
	}
	public void setNom(String nom) {
		this.pseudo = nom;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public boolean getLeader() {
		return this.leader;
	}
	public void setLeader(boolean b) {
		this.leader = b;
	}
	
	//Voir mon dernier surenchère fait
	public ArrayList<Integer> dernieresValeursChoisies(){
		return this.valeursChoisies; 
	}
	
	//Menu pour le Leader
	public void afficherMenuLeaderAvantDemarrer(){
		System.out.println("Vous �tes les Leader, vous pouvez:");
		System.out.println("Taper 1: Lancer Partie");
		System.out.println("Taper 2: Voir Joueurs");
		System.out.println("Taper 3: Annuler Partie");
	}
	
	
	public int menuChoixLeaderAvantDemarrer(){
		
		Scanner scan = new Scanner(System.in);
		int choix;
		do{
		  this.afficherMenuLeaderAvantDemarrer();
		  choix = scan.nextInt();
		}while(choix > 3 || choix < 1);	
		
		return choix;
	}
	
	
	
	//METHODES CALCUL POUR LA V3 DU CLIENT
	/*
	
	//ça revient à lancer dés
	public void secouerGobelet(){
		this.gobelet.secouer();
	}
	
	//Voir le nombre de dés en possession
	public int nombreDes(){
		return gobelet.listeDe.size();
	}
	
	//Regarder la valeur de nos dés, ceci est une methode affichage pour le test
	public String voirDes(){
		int i=1;
		String des = new String();
		des = "";
		for(String s : this.gobelet.afficherDes()){
			des = des + "D� " + i + ": " + s + "\n";
			i++;
		}
		return des;
	}
	
	public ArrayList<DePerudo> connaitreValeurDes(){
		return this.gobelet.getListeDe();
	}
	
	//Quand on choisit de surencherir, on met a jour les nouvelles valeurs
	public void majSurenchere(int nbrDe, int valeurDe){
		this.valeursChoisies.clear();
		this.valeursChoisies.add(nbrDe);
		this.valeursChoisies.add(valeurDe);
	}
	
	
	
	// methode pour tester dans le main
	public String afficherValeursChoisies(){
		return (this.valeursChoisies.get(0) + "-" + this.valeursChoisies.get(1) + "  (Format : nombre de d�s - valeur de d� Pari�)" );
	}
	
	//Afficher couleur joueur
	public Couleur getCouleur(){
		return this.gobelet.getCouleur();
	}
	
	//recuperer D�
	public void recupererDe(){
		this.gobelet.recupererDe();
	}
	
	//Perdre d�
	public void retirerDe(){
		this.gobelet.retirerDe();
	}
	*/
}
