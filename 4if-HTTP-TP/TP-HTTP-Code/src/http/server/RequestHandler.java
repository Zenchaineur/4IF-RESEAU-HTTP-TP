package http.server;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.imageio.ImageIO;

/**
 * Classe permettant de gérer les requetes que le serveur reçoit
 * @author Binome 1-8
 *
 */
public class RequestHandler {
	private static final String RESSOURCES_FOLDER = "ressources/";

	private String request;
	private PrintWriter out;
	private BufferedReader in;
	private OutputStream socketOutputStream;
	private InputStream socketInputStream;


	/**
	 * Constructeur
	 * @param request Chaine de caractère qui représente la requete reçue
	 * @param out PrintWriter
	 * @param in BufferedReader
	 * @param socketOutputStream OutputStream
	 */
	public RequestHandler(String request, PrintWriter out, BufferedReader in, InputStream socketInputStream, OutputStream socketOutputStream) {
		this.request = request;
		this.out = out;
		this.in = in;
		this.socketInputStream = socketInputStream;
		this.socketOutputStream = socketOutputStream;
	}

	/**
	 * Méthode permettant de gérer la requete reçu
	 */
	public void handle() {

		String[] requestParam = request.split(" ");

		// Type de méthod HTTP
		String action = requestParam[0];

		switch (action) {
		case "GET": {
			System.out.println("GET called");
			handleGet(requestParam);
			break;
		}
		case "DELETE": {
			System.out.println("DELETE called");
			String path = requestParam[1];
			File fichier = new File(RESSOURCES_FOLDER + path.substring(1));
			handleDelete(fichier);
			break;
		}

		case "POST": {
			handlePost(requestParam);
			break;
		}
		case "HEAD": {
			handleHead(requestParam);
			break;
		}

		case "TRACE": {
			handleTrace();
			break;
		}

		default:
			out.println("HTTP/1.0 400 BAD REQUEST");
			out.println("Server: Bot");
			// this blank line signals the end of the headers
			out.println("");
			// Send the HTML page
			out.flush();
			break;
		}
	}

	/**
	 * Permet de gérer une requête GET
	 * @param requestParam Requete reçu
	 */
	private void handleGet(String[] requestParam) {
		String[] pathAndParameters = requestParam[1].split("\\?");
		String path = pathAndParameters[0];
		System.out.println("PATH " + path);
		String parameters = null;
		if(pathAndParameters.length > 1) {
			parameters = pathAndParameters[1];
		}
		System.out.println("PARAMETERS " + parameters);
		File fichier = new File(RESSOURCES_FOLDER + path.substring(1));
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
		} else if ( !fichier.canRead()) {
			// Send the headers
			out.println("HTTP/1.0 403 DO NOT HAVE THE PERMISSION");
			out.println("Content-Type: text/html");
			out.println("Server: Bot");
			// this blank line signals the end of the headers
			out.println("");
			out.println("<H1>HTTP 403 : DO NOT HAVE THE PERMISSION TO READ THE FILE</H1>");
			out.flush();
		} else {
			// Send the headers
			System.out.println("FILE to read : " + fichier.getName());
			int extensionIndex = fichier.getName().lastIndexOf(".");
			String extension = fichier.getName().substring(extensionIndex);
			System.out.println(extension);

			if(extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".jpg")) {
				out.println("HTTP/1.0 200 OK");
				out.println("Content-Type: image/" + extension.substring(1));
				out.println("Content-Length: " + fichier.length());
				out.println("Server: Bot");
				// this blank line signals the end of the headers
				out.println("");
				out.flush();
				try {
					FileInputStream is = new FileInputStream(fichier);
					byte[] buffer = new byte[(int)fichier.length()];
					is.read(buffer);

					socketOutputStream.write(buffer);
					socketOutputStream.flush();
					is.close();
				} catch (IOException e1) {
					e1.printStackTrace();

				}
			} else if(extension.equalsIgnoreCase(".mp4")) {
				out.println("HTTP/1.0 200 OK");
				out.println("Content-Type: video/mp4");
				out.println("Content-Length: " + fichier.length());
				out.println("Server: Bot");
				// this blank line signals the end of the headers
				out.println("");
				out.flush();
				try {
					FileInputStream is = new FileInputStream(fichier);
					byte[] buffer = new byte[(int)fichier.length()];
					is.read(buffer);

					socketOutputStream.write(buffer);
					socketOutputStream.flush();
					is.close();
				} catch (IOException e1) {
					e1.printStackTrace();

				}
			} else if(extension.equalsIgnoreCase(".json")) {
				out.println("HTTP/1.0 200 OK");
				out.println("Content-Type: application/json");
				out.println("Content-Length: " + fichier.length());
				out.println("Server: Bot");
				// this blank line signals the end of the headers
				out.println("");
				out.flush();
				try {
					FileInputStream is = new FileInputStream(fichier);
					byte[] buffer = new byte[(int)fichier.length()];
					is.read(buffer);

					socketOutputStream.write(buffer);
					socketOutputStream.flush();
					is.close();
				} catch (IOException e1) {
					e1.printStackTrace();

				}
			} else if(extension.equalsIgnoreCase(".py")) {
				out.println("HTTP/1.0 200 OK");
				out.println("Content-Type: text/plain");
				//out.println("Content-Length: " + fichier.length());
				out.println("Server: Bot");
				// this blank line signals the end of the headers
				out.println("");

				String s = null;
				try {
					String num = "";
					if(parameters != null) {
						String[] listParams = parameters.split("&");
						System.out.println(listParams[0]);
						for(String param : listParams) {
							System.out.println("HERE " + param);
							if(param.contains("number=")) {
								num = param.split("=")[1];
							}
						}
					}

					Process p = Runtime.getRuntime().exec("python " + RESSOURCES_FOLDER + "script.py " + num);
					BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
					BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
					while((s = stdInput.readLine()) != null) {
						out.println(s);
					}
					while((s = stdError.readLine()) != null) {
						out.println(s);
					}
				} catch (IOException e1) {
					e1.printStackTrace();

				}
				out.flush();
			} else {

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
		}
	}

	/**
	 * Permet de gérer une requête POST
	 * @param requestParam Requete reçu
	 */
	private void handlePost(String[] requestParam) {
		String path = requestParam[1];
		System.out.println("PATH " + path);

		System.out.println("POST called");
		String str = "empty";
		String contentType = "";
		int contentLength = -1;
		try {
			while(!str.isBlank() && str != null) {
				str = in.readLine();
				System.out.println(str.isBlank());

				if(str.startsWith("Content-Type")) {
					contentType = str.split(" ")[1];
					System.out.println(contentType);

				} else if(str.startsWith("Content-Length")) {
					contentLength = Integer.parseInt(str.split(" ")[1]);
					System.out.println(contentLength);
				}
			}

			if(contentLength == -1) {
				out.println("HTTP/1.0 411 Length Required");
				out.println("Server: Bot");
				// this blank line signals the end of the headers
				out.println("");
				// Send the HTML page
				out.flush();
			} else {
				System.out.println("Length : " + contentLength);

				if(contentType.equals("application/json")) {
					//str = in.readLine();

					char[] buf = new char[contentLength];

					in.read(buf, 0, contentLength);
					System.out.println("BUF : " + String.valueOf(buf));

					File jsonFile = new File(RESSOURCES_FOLDER + path);
					if(!jsonFile.exists()) {
						out.println("HTTP/1.0 201 Created");

						jsonFile.createNewFile();
					} else {
						out.println("HTTP/1.0 200 OK");
					}

					out.println("Content-Location:" + jsonFile.getPath());
					out.println("Content-Type: application/json");
					out.println("Server: Bot");
					FileWriter writer = new FileWriter(jsonFile, true);
					writer.write(buf);
					writer.write(',');
					writer.close();

					// this blank line signals the end of the headers
					out.println("");
					out.println("{ \"success\": true }");
					out.flush();
				} else if(contentType.equals("image/jpeg") || contentType.equals("image/png")) {
					//str = in.readLine();

					char[] buf = new char[contentLength];
					byte[] byteBuf = new byte[contentLength];

					//in.read(buf, 0, contentLength);
//					DataInputStream dip = new DataInputStream(socketInputStream);
//					dip.read(byteBuf, 0, contentLength);
					System.out.println(byteBuf.length);

					File newImage = new File(RESSOURCES_FOLDER + path);
					if(!newImage.exists()) {
						out.println("HTTP/1.0 201 Created");

						//newImage.createNewFile();
					} else {
						out.println("HTTP/1.0 200 OK");
					}

					out.println("Content-Location:" + newImage.getPath());
					out.println("Content-Type: application/json");
					out.println("Server: Bot");
					System.out.println("here");
					ByteArrayInputStream bais = new ByteArrayInputStream(byteBuf);
					System.out.println(bais);
					BufferedImage img = ImageIO.read(socketInputStream);
					System.out.println(img);
					System.out.println("here2");

					ImageIO.write(img, "png", new File(RESSOURCES_FOLDER + path));

					// this blank line signals the end of the headers
					out.println("");
					out.println("{ \"success\": true }");
					out.flush();
				} else {
					out.println("HTTP/1.0 415 Unsupported Media Type");
					out.println("Server: Bot");
					out.println("Content-Type: application/json");

					// this blank line signals the end of the headers
					out.println("");
					out.println("{ \"success\": false }");
					out.flush();
					out.flush();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();

		}
	}
	
	/**
	 * Permet de gérer une requête DELETE
	 * @param fichier Fichier a supprimer
	 */
	private void handleDelete(File fichier) {
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
	}
	
	/**
	 * Permet de gérer une requête HEAD
	 * @param requestParam Requete reçu
	 */
	private void handleHead(String[] requestParam) {
		System.out.println("HEAD called");
		String path = requestParam[1];
		File fichier = new File(RESSOURCES_FOLDER + path.substring(1));
		if(path.equals("/")) {
			out.println("HTTP/1.0 200 OK");
			out.println("Content-Type: text/html");
			out.println("Server: Bot");
			// this blank line signals the end of the headers
			out.println("");
			out.flush();
		} else if ( !fichier.exists()) {
			// Send the headers
			out.println("HTTP/1.0 404 FILE NOT FOUND");
			out.println("Content-Type: text/html");
			out.println("Server: Bot");
			// this blank line signals the end of the headers
			out.println("");
			out.flush();
		} else if ( !fichier.canRead()) {
			// Send the headers
			out.println("HTTP/1.0 403 DO NOT HAVE THE PERMISSION");
			out.println("Content-Type: text/html");
			out.println("Server: Bot");
			// this blank line signals the end of the headers
			out.println("");
			out.flush();
		} else {
			// Send the headers
			int extensionIndex = fichier.getName().lastIndexOf(".");
			String extension = fichier.getName().substring(extensionIndex);

			if(extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".jpg")) {
				out.println("HTTP/1.0 200 OK");
				out.println("Content-Type: image/" + extension.substring(1));
				out.println("Content-Length: " + fichier.length());
				out.println("Server: Bot");
				// this blank line signals the end of the headers
				out.println("");
				out.flush();
			} else if(extension.equalsIgnoreCase(".mp4")) {
				out.println("HTTP/1.0 200 OK");
				out.println("Content-Type: video/mp4");
				out.println("Content-Length: " + fichier.length());
				out.println("Server: Bot");
				// this blank line signals the end of the headers
				out.println("");
				out.flush();
			} else {
				out.println("HTTP/1.0 200 OK");
				out.println("Server: Bot");
				out.println("Content-Length: " + fichier.length());
				// this blank line signals the end of the headers
				out.println("");
				out.flush();
			}
		}
	}
	
	/**
	 * Permet de gérer une requête TRACE
	 */
	private void handleTrace() {
		out.println("HTTP/1.0 200 OK");
		out.println("Content-Type: message/http");
		out.println("Server: Bot");
		// this blank line signals the end of the headers
		out.println("");
		out.println(request);
		out.flush();
	}
}
