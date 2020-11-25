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
 * Example program from Chapter 1 Programming Spiders, Bots and Aggregators in
 * Java Copyright 2001 by Jeff Heaton
 * 
 * WebServer is a very simple web-server. Any request is responded with a very
 * simple web-page.
 * 
 * @author Jeff Heaton
 * @version 1.0
 */
public class WebServer {

	/**
	 * WebServer constructor.
	 */
	protected void start() {
		ServerSocket s;

		System.out.println("Webserver starting up on port 3000");
		System.out.println("(press ctrl-c to exit)");
		try {
			// create the main server socket
			s = new ServerSocket(3000);
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

					//						System.out.println("str avant readline :");

					str = in.readLine();

					System.out.println("Requete recu : " + str);

				}
				catch(Exception e) {
					e.printStackTrace();
				}

				HandleRequest handler = new HandleRequest(remote, str, out, in, socketOutputStream);
				handler.handle();
				//					String[] requestParam = str.split(" ");
				//
				//					String path = requestParam[1];
				//					File fichier = new File(path.substring(1));
				//
				//
				//					if ( !fichier.exists()) {
				//						// Send the headers
				//						out.println("HTTP/1.0 404 FILE NOT FOUND");
				//						out.println("Content-Type: text/html");
				//						out.println("Server: Bot");
				//						// this blank line signals the end of the headers
				//						out.println("");
				//						// Send the HTML page
				//						out.println("<H1>HTTP 404 : FILE NOT FOUND</H2>");
				//						out.flush();
				//	//					out.write ("HTTP 404"); // la page demandée n'existe pas
				//					}
				//					else {
				//						// Send the headers
				//						out.println("HTTP/1.0 200 OK");
				//						out.println("Content-Type: text/html");
				//						out.println("Server: Bot");
				//						// this blank line signals the end of the headers
				//						out.println("");
				//						FileReader lecteurFichier = new FileReader(fichier);
				//						BufferedReader lectureFichier = new BufferedReader(lecteurFichier);
				//						String line;
				//						while(true) {
				//							line = lectureFichier.readLine();
				//							if (line == null) {
				//								break;
				//							}
				//							System.out.println("line :");
				//							System.out.println(line);
				//							out.write(line);
				//							out.write("\n");
				//						}
				//						lectureFichier.close();
				//						out.flush();
				//					}

				in.close();
				out.close();
				remote.close();


			} catch (Exception e) {
				System.out.println("Error: " + e);
			}
		}
	}

	/**
	 * Start the application.
	 * 
	 * @param args
	 *            Command line parameters are not used.
	 */
	public static void main(String args[]) {
		WebServer ws = new WebServer();
		ws.start();
	}
}
