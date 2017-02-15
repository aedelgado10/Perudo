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
	
	//Voir mon dernier surenchÃ¨re fait
	public ArrayList<Integer> dernieresValeursChoisies(){
		return this.valeursChoisies; 
	}
	
	//Afficher couleur joueur
	public Couleur getCouleur(){
		return this.gobelet.getCouleur();
	}
	
	//METHODES CALCUL POUR LA V3 DU CLIENT
	/*
	
	//Ã§a revient Ã  lancer dÃ©s
	public void secouerGobelet(){
		this.gobelet.secouer();
	}
	
	//Voir le nombre de dÃ©s en possession
	public int nombreDes(){
		return gobelet.listeDe.size();
	}
	
	//Regarder la valeur de nos dÃ©s, ceci est une methode affichage pour le test
	public String voirDes(){
		int i=1;
		String des = new String();
		des = "";
		for(String s : this.gobelet.afficherDes()){
			des = des + "Dé " + i + ": " + s + "\n";
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
		return (this.valeursChoisies.get(0) + "-" + this.valeursChoisies.get(1) + "  (Format : nombre de dés - valeur de dé Parié)" );
	}
	
	
	
	//recuperer Dé
	public void recupererDe(){
		this.gobelet.recupererDe();
	}
	
	//Perdre dé
	public void retirerDe(){
		this.gobelet.retirerDe();
	}
	*/
}
