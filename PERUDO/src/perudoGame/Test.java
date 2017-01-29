package perudoGame;

import java.util.ArrayList;

public class Test {

	public static void afficherDevoil(Partie p1, ArrayList<ArrayList<DePerudo>> a){
		int i = 0;
		ArrayList<Joueur> j = p1.getListeJoueurs();
		System.out.println("On souleve les gobelets de tous.");
		for ( ArrayList<DePerudo> b : a){
			System.out.println("Le joueur " + p1.getNomJoueur(j.get(i++)) + " avait:");
			for (DePerudo d : b){
				System.out.println(d.getPerudo()); 
			}
		}
	}
	
	public static String calculerCouleur(Couleur c){
		return c.getCodeCouleur();
	}
	
	public static void main(String[] args) {
		
		Red r1 = new Red();
		Blue b1 = new Blue();
		Green g1 = new Green();
		
		Joueur j1 = new Joueur(1, "Andr� Pierre Gignac",r1);
		Joueur j2 = new Joueur(2, "Mathieu Valbuena",b1);
		Joueur j3 = new Joueur(3, "Karim Benzema",g1);
		
		Partie p1 = new Partie(1, "Party","Waiting Players");
		
		System.out.println("Bienvenu a : " + p1.getNom() + ", status: " + p1.getStatus());
		
		p1.ajouterJoueur(j1);
		p1.ajouterJoueur(j2);
		p1.ajouterJoueur(j3);
		
		p1.premierAJouer();
		
		System.out.println("Le premier a jouer est : " + calculerCouleur(p1.getCouleurJoueur(p1.getJoueurCourrant()))+ " " + p1.getJoueurCourrant().getPseudo());
		
		p1.passerJoueurSuivant();
		
		System.out.println("Le nouveau joueur currant est : " + p1.getPseudoCouleur(p1.getJoueurCourrant()));
		
		/*p1.secouerGobelets(j1);
		p1.secouerGobelets(j2);
		p1.secouerGobelets(j3);*/
		
		p1.secouerTousGobelets();
		
		//test Affichage du d� 1 du joueur Courant
		//System.out.println(p1.getValeurDesDeJoueurCourant().get(0).getPerudo());
		
		//Sequence pour tester la methode connaireValeurDes() de joueur.
		/*System.out.println(p1.getJoueurCourrant().connaitreValeurDes().get(0).getPerudo());
		System.out.println(p1.getJoueurCourrant().connaitreValeurDes().get(1).getPerudo());
		System.out.println(p1.getJoueurCourrant().connaitreValeurDes().get(2).getPerudo());
		System.out.println(p1.getJoueurCourrant().connaitreValeurDes().get(3).getPerudo());
		System.out.println(p1.getJoueurCourrant().connaitreValeurDes().get(4).getPerudo());*/
		
		System.out.println("Il y a " + p1.nombreTotalDeDes() + " d�s dans la partie");
		
		System.out.println(p1.getPseudoCouleur(j1) + " poss�de " + p1.nombreDesJoueur(j1) + " des");
		System.out.println(p1.getPseudoCouleur(j2) + " poss�de " + p1.nombreDesJoueur(j2) + " des");
		System.out.println(p1.getPseudoCouleur(j3) + " poss�de " + p1.nombreDesJoueur(j3) + " des");
		
		System.out.println(p1.getPseudoCouleur(j1) + " poss�de \n" + j1.voirDes());
		System.out.println(p1.getPseudoCouleur(j2) + " poss�de \n" + j2.voirDes());
		System.out.println(p1.getPseudoCouleur(j3) + " poss�de \n" + j3.voirDes());
		
		j1.majSurenchere(3,3);
		j2.majSurenchere(2,2);
		j3.majSurenchere(4,4);
		System.out.println(p1.getPseudoCouleur(j1) + " a dit " + j1.afficherValeursChoisies());
		System.out.println(p1.getPseudoCouleur(j2) + " a dit " + j2.afficherValeursChoisies());
		System.out.println(p1.getPseudoCouleur(j3) + " a dit " + j3.afficherValeursChoisies());
		
		System.out.println("C'est le tour de : " + p1.getJoueurCourrant().getPseudo());
		System.out.println("Rappel: Les derni�res valeurs jou�es sont: " + p1.derniereValeurJouee().get(0) + " d�s de valeur " + p1.derniereValeurJouee().get(1));
		System.out.println("Valeurs annonc�es par " + p1.connaitreJoueurPrecedant().getPseudo());
		System.out.println();
		
		p1.surencher(7,4);
		System.out.println("C'est le tour de : " + p1.getJoueurCourrant().getPseudo());
		System.out.println("Rappel: Les derni�res valeurs jou�es sont: " + p1.derniereValeurJouee().get(0) + " d�s de valeur " + p1.derniereValeurJouee().get(1));
		System.out.println("Valeurs annonc�es par " + p1.connaitreJoueurPrecedant().getPseudo());
		System.out.println();
		//sert a tester le devoil des d�s
		//System.out.println(p1.souleverGobelets().get(1).get(3).getPerudo());
		
		//methode de test d'affichage de cette Classe Test
		afficherDevoil(p1,p1.souleverGobelets());
		System.out.println();
		
		// Voir combien de fois le d� choisit est pr�sent
		System.out.println("le d� " + p1.derniereValeurJouee().get(1) + " est pr�sent : " + p1.combienDeFois(p1.derniereValeurJouee().get(1).toString()) + " fois");
		System.out.println();
		
		if( p1.toutPile())
			System.out.println(p1.getPseudoCouleur(p1.getJoueurCourrant()) + " a dit tout pile et gagne un d�");
		else
			System.out.println(p1.getPseudoCouleur(p1.getJoueurCourrant()) + " a dit tout pile et perd un d�");
	
		if( p1.menteur())
			System.out.println(p1.getPseudoCouleur(p1.getJoueurCourrant()) + " a dit menteur (et a raison) et donc " + p1.getPseudoCouleur(p1.connaitreJoueurPrecedant()) + " perd un d�");
		else
			System.out.println(p1.getPseudoCouleur(p1.getJoueurCourrant()) + " a dit menteur (et se trompe) donc perd un d�. " + p1.getPseudoCouleur(p1.connaitreJoueurPrecedant()) + " avait raison.");
		
		p1.retirerDe(j2);
		p1.retirerDe(j3);
		p1.retirerDe(j3);
		p1.retirerDe(j3);
		p1.recupererDe(j3);
		
		System.out.println("Apr�s retirage de 3 d�s, Il y a maintenant " + p1.nombreTotalDeDes() + " d�s dans la partie");
		System.out.println();
		
		System.out.println("On relance les d�s.");
		p1.secouerTousGobelets();
		System.out.println(p1.getPseudoCouleur(j1) + " poss�de " + p1.nombreDesJoueur(j1) + " des");
		System.out.println(p1.getPseudoCouleur(j2) + " poss�de " + p1.nombreDesJoueur(j2) + " des");
		System.out.println(p1.getPseudoCouleur(j3) + " poss�de " + p1.nombreDesJoueur(j3) + " des");
		
		System.out.println(p1.getPseudoCouleur(j1) + " poss�de \n" + j1.voirDes());
		System.out.println(p1.getPseudoCouleur(j2) + " poss�de \n" + j2.voirDes());
		System.out.println(p1.getPseudoCouleur(j3) + " poss�de \n" + j3.voirDes());
		
	}
}
