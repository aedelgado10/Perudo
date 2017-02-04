package perudoGameClient;
import java.io.*;
import java.net.*;

public class ConnexionClient {
	
	//Variable pour stocker le Socket
	Socket sock;
	
	//Crée une connexion avec le serveur
	public void ConnecterServeur() {
		
		try{
			this.sock = new Socket("127.0.0.1", 27019);
			System.out.println("Connect� sur : "+ sock);
			
		 } 
		 catch (IOException ex){
			 System.err.println("Erreur : "+ex);
			 ex.printStackTrace();
		 }
	}
	
	//Ferme la connexion de façon propre
	public void FermerConnexionServeur(){
		
		try{
			this.sock.close();
			
		 } 
		 catch (IOException ex){
			 System.err.println("Erreur : "+ex);
			 ex.printStackTrace();
		 }
	}
	
	//Recevoir des données from serveur
	public String recevoir(){
		
		BufferedReader fluxEntreeSocket;
		String reponse;
		try{
			fluxEntreeSocket = new BufferedReader(new InputStreamReader(this.sock.getInputStream()));
			reponse = fluxEntreeSocket.readLine();
			return reponse;
		}
		catch (IOException ex){
			System.err.println("Erreur : "+ex);
			ex.printStackTrace();
		}
		
		return null;
	}
	
	//Envoi un String au serveur
	public void envoyer(String pdu){
		
		PrintStream fluxSortieSocket;
		try{
			fluxSortieSocket = new PrintStream(this.sock.getOutputStream());
			fluxSortieSocket.println(pdu);
		}
		catch (IOException ex){
			System.err.println("Erreur : "+ex);
			ex.printStackTrace();
		}
		
	}
	
	

}
