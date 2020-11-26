///A Simple Web Server (WebServer.java)

package http.server;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Simple WebServer
 * @author Binôme 1-8
 *
 */
public class WebServer {


	/**
	 * Méthode pour lancer le serveur
	 */
	protected void start(String port) {
		ServerSocket s;

		System.out.println("Webserver starting up on port " + port);
		System.out.println("(press ctrl-c to exit)");
		try {
			// create the main server socket
			s = new ServerSocket( Integer.parseInt(port));
		} catch (Exception e) {
			System.out.println("Error: " + e);
			return;
		}

		System.out.println("Waiting for connection");
		for (;;) {
			try {
				// wait for a connection
				Socket remote = s.accept();

				// remote is now the connected socket
				System.out.println("Connection, sending data.");

				InputStream socketInputStream = remote.getInputStream();
				OutputStream socketOutputStream = remote.getOutputStream();

				BufferedReader in = new BufferedReader(new InputStreamReader(socketInputStream));
				PrintWriter out = new PrintWriter(socketOutputStream);

				// read the data sent. We basically ignore it,
				// stop reading once a blank line is hit. This
				// blank line signals the end of the client HTTP
				// headers.

				String str = "GET index.html HTTP/1.1";

				try {
					str = in.readLine();
					System.out.println("Requete recu : " + str);
				}
				catch(Exception e) {
					e.printStackTrace();
				}

				RequestHandler handler = new RequestHandler(str, out, in, socketOutputStream);
				handler.handle();

				in.close();
				out.close();
				remote.close();


			} catch (Exception e) {
				System.out.println("Error: " + e);
			}
		}
	}

	/**
	 * Méthode qui démarre l'application
	 * 
	 * @param args
	 *            Command line parameters are not used.
	 */
	public static void main(String args[]) {
		WebServer ws = new WebServer();
		ws.start(args[0]);
	}
}
