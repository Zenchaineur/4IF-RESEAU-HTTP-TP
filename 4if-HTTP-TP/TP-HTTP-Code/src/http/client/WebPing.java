package http.client;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class WebPing {


	public static void main(String[] args) {

		if (args.length != 2) {
			System.err.println("Usage java WebPing <server host name> <server port number>");
			return;
		}	

		String httpServerHost = args[0];
		int httpServerPort = Integer.parseInt(args[1]);
		httpServerHost = args[0];
		httpServerPort = Integer.parseInt(args[1]);

		try {

			BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

			InetAddress addr;      
			Socket sock = new Socket(httpServerHost, httpServerPort);
			addr = sock.getInetAddress();
			System.out.println("Connected to " + addr);


			InputStream socketInputStream = sock.getInputStream();
			OutputStream socketOutputStream = sock.getOutputStream();

			BufferedReader in = new BufferedReader(new InputStreamReader(socketInputStream));
			PrintWriter out = new PrintWriter(socketOutputStream);

			String req = ".";
			while (req != null && !req.equals("")) {
				req = stdIn.readLine();
				System.out.println("req :");
				System.out.println(req);
				out.write(req);
				//out.flush();
				//out.close();
				//close
				//req = null;
				System.out.println("echo: " + in.readLine());
			}





			sock.close();

		} catch (java.io.IOException e) {
			System.out.println("Can't connect to " + httpServerHost + ":" + httpServerPort);
			System.out.println(e);
		}
	}
}