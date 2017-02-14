package perudoGameClient;
import java.io.*;
import java.net.*;

/**
 * 
 * @author Pereira
 *
 */

public class ConnexionClient implements Runnable{
	
	//Variable pour stocker le Socket
	private Socket sock;
	private Client cli;
	
	public ConnexionClient(Client cli){
		this.cli = cli;
	}
	
	public Client getClient(){
		return this.cli;
	}
	
	//Crée une connexion avec le serveur
	public void ConnecterServeur() throws IOException {
		this.sock = new Socket("127.0.0.1", 27016);
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
		if(this.sock != null){
			try{
				fluxEntreeSocket = new BufferedReader(new InputStreamReader(this.sock.getInputStream()));
				reponse = fluxEntreeSocket.readLine();
				return reponse;
			}
			catch(SocketException se){
				System.out.println("Socket ferm�e");
				return null;
			}
			catch (IOException ex){
				System.err.println("Erreur : "+ex);
				ex.printStackTrace();
			}
		}

		
		return null;
	}
	
	//Envoi un String au serveur
	public void envoyer(String pdu){
		
		PrintStream fluxSortieSocket;
		
		if(this.sock != null){
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

	@Override
	public void run(){
		while (this.getClient().getClientTourne()){
			String recu = this.recevoir();
			if(recu != null){
				this.getClient().traiter(recu,this);
			}
			else{
				break;
			}
		}
	}
	
	

}
