package perudoGameClient;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * 
 * @author Pereira
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
	
	//Afficher couleur joueur
	public Couleur getCouleur(){
		return this.gobelet.getCouleur();
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
