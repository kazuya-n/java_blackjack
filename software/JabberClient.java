import java.io.*;
import java.net.*;

public class JabberClient{
	Socket socket;
	BufferedReader in;
	PrintWriter out;
	JabberClient(){
		try{
			InetAddress addr = InetAddress.getByName("localhost");
			System.out.println("addr = " + addr);
			socket = new Socket(addr, 8901);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
		}catch (Exception ex)  {
			System.out.println(ex);
		}
	}
	public void send(String s) throws IOException{
		try{
			out.println(s);
		}finally{
			System.out.println("closing...");
			socket.close();
			socket.close();
		}
	}
	public String receive() throws IOException{
		String str="";
		try{
			str = in.readLine();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			System.out.println("closing...");
			socket.close();
			return str;
		}
	}
}
