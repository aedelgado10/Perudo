package perudoGameClient;

import java.util.ArrayList;

/**
 * 
 * @author Pereira-Delgado
 * @version 1
 *
 */
public class Couleur {
	
	private String codeCouleur;
	private ArrayList<String> couleursDispo;
	
	public Couleur(ArrayList<String> colors){
		this.couleursDispo = new ArrayList<>();
		
		for(String s : colors.subList(1, colors.size())){
			this.couleursDispo.add(s);
		}
	}
	
	public Couleur(String code){
		this.codeCouleur = code;
	}
	
	
	public void setCodeCouleur(String code){
		this.codeCouleur = code;
	}
	
	public String getCodeCouleur(){
		return this.codeCouleur;
	}
	
	public void afficherCouleursDispo(){
		System.out.println("Voici la liste des couleurs disponibles:");
		for(String s : this.couleursDispo ){
			System.out.println(s);
		}
	}
	
	public int nombreCouleursDispo(){
		int cpt = 0;
		cpt = this.couleursDispo.size();
		return cpt;
	}
	
	public String getCouleurChoisie(int i){
		return this.couleursDispo.get(i-1);
	}

}
