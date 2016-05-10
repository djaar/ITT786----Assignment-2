import java.io.*;
import java.security.*;
import javax.net.ssl.*;
 
public class JavaTLSClient {
	public static void main(String [] args) {
		SSLSocket socket = null;
		BufferedReader in = null;
		try {
		//load client private key
			KeyStore clientKeys = KeyStore.getInstance("JKS");
			clientKeys.load(new FileInputStream("plainclient.jks"),"password".toCharArray());
			KeyManagerFactory clientKeyManager = KeyManagerFactory.getInstance("SunX509");
			clientKeyManager.init(clientKeys,"password".toCharArray());
		//load server public key
			KeyStore serverPub = KeyStore.getInstance("JKS");
			serverPub.load(new FileInputStream("serverpub.jks"),"password".toCharArray());
			TrustManagerFactory trustManager = TrustManagerFactory.getInstance("SunX509");
			trustManager.init(serverPub);
		//use keys to create SSLSoket
			SSLContext ssl = SSLContext.getInstance("TLS");
			ssl.init(clientKeyManager.getKeyManagers(), trustManager.getTrustManagers(), SecureRandom.getInstance("SHA1PRNG"));
			socket = (SSLSocket)ssl.getSocketFactory().createSocket("localhost", 8889);
			socket.startHandshake();
		//receive data
                        BufferedReader r = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        PrintWriter w = new PrintWriter(socket.getOutputStream(), true);
                        BufferedReader con = new BufferedReader(new InputStreamReader(System.in));
                        String line;
                        do{
                            line = r.readLine();
                            if ( line != null )
                                System.out.println(line);
                            line = con.readLine();
                            w.println(line);
                        }while ( !line.trim().equals("bye") );
			
                        //in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			//String data;
			//while((data = in.readLine())!=null) {
				//System.out.println(data);
			//}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(in!=null) in.close();
				if(socket!=null) socket.close();
				if(socket!=null) socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}