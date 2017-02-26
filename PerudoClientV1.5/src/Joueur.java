import java.util.ArrayList;
import java.util.Scanner;


public class Joueur {
	
	private int idJoueur;
	private String nomJoueur;
	private Gobelet gobelet;
	private boolean myTurn;
	//private ArrayList<Integer> valeursChoisies;
	private Scanner scan;
	
	public Joueur(){
		this.idJoueur = 0;
		//this.valeursChoisies = new ArrayList<>();
	}
	
	public Joueur(int id, Couleur c){
		this.idJoueur = id;
		this.gobelet = new Gobelet(c);
		//this.valeursChoisies = new ArrayList<>();
	}
	
	/* GETTERS */
	public void setIdJoueur(int id){
		this.idJoueur = id;
	}
	
	public void setNomJoueur(String s){
		this.nomJoueur = s;
	}
	
	public int getIdJoueur(){
		return this.idJoueur;
	}
	
	public String getNomJoueur(){
		return this.nomJoueur;
	}
	
	public void setCouleurJoueur(Couleur c){
		this.gobelet = new Gobelet(c);
	}
	
	public Couleur getCouleurJoueur(){
		return this.gobelet.getCouleur();
	}
	
	public String getCodeCouleurJoueur(){
		return this.getCouleurJoueur().getCodeCouleur();
	}
	
	public boolean getMyTurn(){
		return this.myTurn;
	}
	
	public void setMyTurn(boolean b){
		this.myTurn = b;
	}
	
	public Gobelet getGobelet(){
		return this.gobelet;
	}
	
	/************************************************************/
	
	/* VOIR DES */
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
	
	public int getNbrDes(){
		return this.getGobelet().getListeDe().size();
	}
	/***************************************************************/
	
	/* TREAITEMENT DES DES */
	public void stockerDes(String rep, Client c){
		//System.out.println("des" + rep); //debugger
		ArrayList<DePerudo> listeDes = new ArrayList<>();
		ArrayList<String> parsed = new ArrayList<>();
		Couleur coul = new Couleur(this.getCodeCouleurJoueur());
		int i = 0;
		
		
		parsed = c.getGPC().parseLists(rep,":");
		
		
		for(String des : parsed){
			listeDes.add(new DePerudo(coul));
			if( i == 0){
				listeDes.get(i).setStringPerudo(c.getGPC().decomposer(des).get(1));
			}
			else{
				listeDes.get(i).setStringPerudo(des);
			}
			i++;
		}
		
		this.getGobelet().setListeDe(listeDes);
	}
	
	public void enleverDe(){
		this.getGobelet().retirerDe();
	}
	
	public void recupererDe(){
		this.getGobelet().recupererDe();
	}
	
	/********************************************************************/
	
	/*choix des valeurs a annoncer*/
	public ArrayList<Integer> choisirValeursPremier(){
		ArrayList<Integer> vals = new ArrayList<>();
		
		int choix;
		scan = new Scanner(System.in);
		do{
		  System.out.println("Choisir d� a annoncer:");
		  choix = scan.nextInt();
		}while(choix > 6 || choix < 2);
		
		vals.add(choix);
		
		do{
		  System.out.println("Choisir nombre de d�s:");
		  choix = scan.nextInt();
		}while(choix < 1);
		vals.add(choix);
		
		return vals;
	}
	
	public ArrayList<Integer> choisirValeursEnsuite(int nbr, int de){
		ArrayList<Integer> vals = new ArrayList<>();
		System.out.println("DE: " + de + " NBR: " + nbr);
		int choix;
		scan = new Scanner(System.in);
		do{
		  System.out.println("Surencherir les derni�res valeurs jouees: "+ nbr + "-" + de);
		  System.out.println("Choisir d� a annoncer:");
		  choix = scan.nextInt();
		}while(choix > 6 || choix < de);
		
		vals.add(choix);
		
		
		do{
		  System.out.println("Surencherir les derni�res valeurs jouees: "+ nbr + "-" + de);
		  System.out.println("Choisir nombre de d�s:");
		  choix = scan.nextInt();
		}while(choix <= nbr);
		vals.add(choix);
		
		return vals;
	}
	/**********************************************************************/
}
