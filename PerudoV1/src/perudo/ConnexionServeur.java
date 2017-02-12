package perudo;

import java.net.ServerSocket; 
import java.net.Socket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class ConnexionServeur implements Runnable{

	Socket client = null;
	Serveur serveur;
	GestionProtocole gP;
	
	/**
	 * @return socketService s
	 */
	public static ServerSocket ouvrir(){		
		try{
			ServerSocket sS = new ServerSocket(27016);
			return sS;
		}
		catch(IOException e){
			System.out.println("[Serveur] Probl�me au d�marrage du service");
			return null;
		}
	}
	
	public ConnexionServeur(Serveur s){
		super();
		this.serveur = s;
		attendreClient(s.getSocketService());
		this.gP = s.getGP();
	}
	
	/**
	 * 
	 * @param s
	 * @return Socket client avec client connect�
	 */
	public void attendreClient(ServerSocket service){
			
		while (client == null){
			try {
				this.client = service.accept();
				serveur.indexerClient(client);
				System.out.println("[Serveur] Nouveau client connect�!");
			} catch (IOException e) {
				System.out.println("[Serveur] Impossible d'�tablir la cx avec un client");
			}
		}
	}
	
	/**
	 * 
	 * @param s
	 * @return String larequete
	 */
	public String recevoir(Socket client){
		
		BufferedReader in;
		
		if (client != null){
			try {
				in = new BufferedReader(new InputStreamReader(client.getInputStream()));
				String req = in.readLine();
				
				if (req.equals(null)){
					return PDU.QUIT;
				}
				return req;
			} catch (IOException e) {
				System.out.println("[ConnexionServeur] Erreur lors de la r�ception ou �l'instantiation du flux d'entr�e");
				return PDU.QUIT;
			}catch(NullPointerException npe){
				System.out.println("[ConnexionServeur] Le client envoie \"null\" et va �tre d�connect�");
				return PDU.QUIT;
			}
		}
		return "quit";
	}
	
	public static void repondre(Socket client,String reponse){
		
		 try {
			PrintStream out = new PrintStream(client.getOutputStream());
			
			if (reponse != null){
				out.println(reponse);	
			}
		} catch (IOException e) {
			System.out.println("[Serveur] Erreur lors de l'envoi");
			try {
				client.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}catch(NullPointerException n){
			System.out.println("[Serveur] Il n'y a rien � r�pondre...");
		}
	}

	@Override
	public void run() {
		System.out.println("Client connect�");
		while (true){
			String recu = this.recevoir(this.client);
			String reponse = serveur.traiter(recu,this.client);
			
			if (reponse.equals(PDU.QUIT)){
				try {
					serveur.getListeClients().remove(client);
					serveur.supprimerJoueur(client);
					this.client.close();
					System.out.println("Client d�connect�!");
					break;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			ConnexionServeur.repondre(client,reponse);		//R�ponse au Client_i (on ne r�pond pas QUIT au client)
		}
	}
}
