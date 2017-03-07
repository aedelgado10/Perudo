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
	
	public void closeScan(){
		this.scan.close();
	}
	
	/************************************************************/
	
	/* VOIR DES */
	public String voirDes(){
		String des = new String();
		des = "| ";
		for(String s : this.gobelet.afficherDes()){
			des = des + s + " | ";
		}
		des = des + "\n";
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
	
	/*Choix des valeurs à annoncer*/
	public ArrayList<Integer> choisirValeursPremier(){
		ArrayList<Integer> vals = new ArrayList<>();
		
		int choix;
		scan = new Scanner(System.in);
		do{
		  System.out.println("Choisir dé a annoncer:");
		  try{
			  choix = scan.nextInt();
		  }catch(Exception e){
			  vals = new ArrayList<>();
			  vals.add(99);
			  vals.add(99);
			  return vals;
		  }
		}while(choix > 6 || choix < 2);
		vals.add(choix);
		
		do{
		  System.out.println("Choisir nombre de dés:");
		  try{
			  choix = scan.nextInt();
		  }catch(Exception e){
			  vals = new ArrayList<>();
			  vals.add(99);
			  vals.add(99);
			  return vals;
		  }
		}while(choix < 1);
		vals.add(choix);
		
		return vals;
	}
	
	public ArrayList<Integer> choisirValeursEnsuite(int nbr, int de){
		ArrayList<Integer> vals = new ArrayList<>();
		System.out.println("DE: " + de + " NBR: " + nbr);
		int choix;
		int choix2;
		scan = new Scanner(System.in);
		do{
			System.out.println("Surencherir les dernières valeurs jouees: "+ nbr + "-" + de);
			do{
			  System.out.println("Choisir dé a annoncer:");
			  try{
				  choix = scan.nextInt();
			  }catch(Exception e){
				  vals = new ArrayList<>();
				  vals.add(99);
				  vals.add(99);
				  return vals;
			  }
			}while(choix > 6 || choix < 2);
			
			vals.add(choix);
			
			
			do{
			  System.out.println("Choisir nombre de dés:");
			  try{
				  choix2 = scan.nextInt();
			  }catch(Exception e){
				  vals = new ArrayList<>();
				  vals.add(99);
				  vals.add(99);
				  return vals;
			  }
			}while(choix2 < 1);
			vals.add(choix2);
		}while((choix < de && choix2 < nbr) || (choix < de && choix2 == nbr) || (choix == de && choix2 < nbr));
		return vals;
	}
	/**********************************************************************/
}
