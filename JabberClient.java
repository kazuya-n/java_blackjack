import java.io.*;
import java.net.*;

public class JabberClient{
	Socket socket;
	BufferedReader in;
	PrintWriter out;
	JabberClient(String ar){
		try{
			InetAddress addr = InetAddress.getByName(ar);
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
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public String receive() throws IOException{
		String str="";
		try{
			str = in.readLine();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			return str;
		}
	}

	public void close(){
		try{
			System.out.println("closing...");
			socket.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
