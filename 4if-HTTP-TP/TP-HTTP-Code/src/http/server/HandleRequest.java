package http.server;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.imageio.ImageIO;

import com.sun.tools.javac.util.ByteBuffer;

public class HandleRequest {

	private String request;
	private PrintWriter out;
	private BufferedReader in;
	private OutputStream socketOutputStream;


	public HandleRequest(String request, PrintWriter out, BufferedReader in, OutputStream socketOutputStream) {
		this.request = request;
		this.out = out;
		this.in = in;
		this.socketOutputStream = socketOutputStream;
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
				System.out.println("FILE to read : " + fichier.getName());
				int extensionIndex = fichier.getName().lastIndexOf(".");
				String extension = fichier.getName().substring(extensionIndex);
				System.out.println(extension);
				out.println("HTTP/1.0 200 OK");
				if(extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".jpg")) {
					try {
						InputStream is = new BufferedInputStream(new FileInputStream(fichier));
						DataOutputStream dos = new DataOutputStream(socketOutputStream);
						dos.writeLong(fichier.length()); // <-- remember to read a long on server.
						int val;
						while ((val = is.read()) != -1) {
							dos.write(val);
						}
						dos.flush();
					} catch (IOException e1) {
						e1.printStackTrace();

					}
					out.println("HTTP/1.0 200 OK");
					out.println("Content-Type: image/png");
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
								//								System.out.println("line :");
								//								System.out.println(line);
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
								//								System.out.println("line :");
								//								System.out.println(line);
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
			String str = "";
			try {
				while(str != null) {
					str = in.readLine();
					if(str.startsWith("Content-Length")) {
						int contentLength = Integer.parseInt(str.split(" ")[1]);
						System.out.println("Length : " + contentLength);
						str = in.readLine();
						char[] buf = new char[contentLength];

						in.read(buf, 0, contentLength);
						System.out.println("BUF : " + String.valueOf(buf));
						out.println("HTTP/1.0 200 OK");
						out.println("Content-Type: text/plain");
						out.println("Server: Bot");
						// this blank line signals the end of the headers
						out.println("");
						// Send the HTML page
						out.println("OK");
						out.flush();
						str = null;
					}

					System.out.println(str);
				}
				System.out.println("fini post");
			} catch (IOException e) {
				e.printStackTrace();

			}
			//			System.out.println(request);
			//			System.out.println(requestParam[1]);
			//			System.out.println(requestParam.length);
			//			String path = requestParam[1];
			//			File fichier = new File(path.substring(1));
			//			if(!fichier.exists()) {
			//				out.println("HTTP/1.0 404 FILE NOT FOUND");
			//				out.println("Content-Type: text/html");
			//				out.println("Server: Bot");
			//				// this blank line signals the end of the headers
			//				out.println("");
			//				// Send the HTML page
			//				out.println("<H1>HTTP 404 : FILE NOT FOUND</H1>");
			//				out.flush();
			//			} else {
			//				out.println("HTTP/1.0 200 OK");
			//				out.println("Content-Type: text/html");
			//				out.println("Server: Bot");
			//				// this blank line signals the end of the headers
			//				out.println("");
			//				// Send the HTML page
			//				out.println("<H1>Fichier " + fichier.getName() +" supprimé</H1>");
			//				out.flush();
			//			}
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
