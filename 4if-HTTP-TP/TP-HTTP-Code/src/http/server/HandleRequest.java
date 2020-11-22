package http.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class HandleRequest {

	String request;
	PrintWriter out;

	public HandleRequest(String request, PrintWriter out) {
		this.request = request;
		this.out = out;
	}

	public void handle() {

		String[] requestParam = request.split(" ");

		String action = requestParam[0];

		switch (action) {
		case "GET": {
			System.out.println("GET called");
			String path = requestParam[1];
			File fichier = new File(path.substring(1));
			if(path.equals("/")) {
				out.println("HTTP/1.0 200 OK");
				out.println("Content-Type: text/html");
				out.println("Server: Bot");
				// this blank line signals the end of the headers
				out.println("");
				// Send the HTML page
				out.println("<H1>Bienvenue sur mini-server HTTP</H1>");
				out.flush();
			} else if ( !fichier.exists()) {
				// Send the headers
				out.println("HTTP/1.0 404 FILE NOT FOUND");
				out.println("Content-Type: text/html");
				out.println("Server: Bot");
				// this blank line signals the end of the headers
				out.println("");
				// Send the HTML page
				out.println("<H1>HTTP 404 : FILE NOT FOUND</H1>");
				out.flush();
				//					out.write ("HTTP 404"); // la page demandée n'existe pas
			} else {
				// Send the headers
				out.println("HTTP/1.0 200 OK");
				out.println("Content-Type: text/html");
				out.println("Server: Bot");
				// this blank line signals the end of the headers
				out.println("");
				FileReader lecteurFichier;
				try {
					lecteurFichier = new FileReader(fichier);
					BufferedReader lectureFichier = new BufferedReader(lecteurFichier);
					String line;
					while(true) {
						try {
							line = lectureFichier.readLine();
							if (line == null) {
								lectureFichier.close();
								break;
							}
							System.out.println("line :");
							System.out.println(line);
							out.write(line);
							out.write("\n");
							
							out.flush();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
			break;
		}
		case "DELETE": {
			System.out.println("DELETE called");
			String path = requestParam[1];
			File fichier = new File(path.substring(1));
			if(!fichier.exists()) {
				out.println("HTTP/1.0 404 FILE NOT FOUND");
				out.println("Content-Type: text/html");
				out.println("Server: Bot");
				// this blank line signals the end of the headers
				out.println("");
				// Send the HTML page
				out.println("<H1>HTTP 404 : FILE NOT FOUND</H1>");
				out.flush();
			} else {
				fichier.delete();
				out.println("HTTP/1.0 200 OK");
				out.println("Content-Type: text/html");
				out.println("Server: Bot");
				// this blank line signals the end of the headers
				out.println("");
				// Send the HTML page
				out.println("<H1>Fichier " + fichier.getName() +" supprimé</H1>");
				out.flush();
			}
			break;
		}
		
		case "POST": {
			System.out.println("POST called");
			String path = requestParam[1];
			File fichier = new File(path.substring(1));
			if(!fichier.exists()) {
				out.println("HTTP/1.0 404 FILE NOT FOUND");
				out.println("Content-Type: text/html");
				out.println("Server: Bot");
				// this blank line signals the end of the headers
				out.println("");
				// Send the HTML page
				out.println("<H1>HTTP 404 : FILE NOT FOUND</H1>");
				out.flush();
			} else {
				fichier.delete();
				out.println("HTTP/1.0 200 OK");
				out.println("Content-Type: text/html");
				out.println("Server: Bot");
				// this blank line signals the end of the headers
				out.println("");
				// Send the HTML page
				out.println("<H1>Fichier " + fichier.getName() +" supprimé</H1>");
				out.flush();
			}
			break;
		}

		default:
			out.println("HTTP/1.0 404 ACTION NOT FOUND");
			out.println("Content-Type: text/html");
			out.println("Server: Bot");
			// this blank line signals the end of the headers
			out.println("");
			// Send the HTML page
			out.println("<H1>ERROR : ACTION NOT FOUND</H1>");
			out.flush();
			break;
		}
	}
}
