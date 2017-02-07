package perudo;

import java.util.ArrayList;

public class Joueur {

	private int id;
	private String pseudo;
	private Couleur c;
	private Gobelet gobelet;
	private ArrayList<Integer> valeursChoisies;
	
	public Joueur(int id) {
		this.id = id;
		this.pseudo = "";
		this.valeursChoisies = new ArrayList<>();
		this.c = new Couleur("undefined");
	}
	
	
	public void setCouleur(Couleur c){
		this.c = c;
		this.attribuerPiecesDuJeu(c);
	}
	
	public void attribuerPiecesDuJeu(Couleur c){
		this.gobelet = new Gobelet(c);
	}
	
	public int getID(){
		return id;
	}
	
	public void setPseudo(String p){
		this.pseudo = p;
	}
	
	public String getPseudo(){
		return this.pseudo;
	}
	
	public void secouerGobelet(){
		this.gobelet.secouer();
	}
	
	public Couleur getCouleur(){
		return this.c;
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
	
	//Voir mon dernier surenchÃ¨re fait
	public ArrayList<Integer> dernieresValeursChoisies(){
		return this.valeursChoisies; 
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

}
