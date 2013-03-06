import javax.bluetooth.*;
import javax.microedition.io.*;
import java.io.*;

public class ThreadedEchoHandler {

  
	private InputStream in;
	private OutputStream out;
	private volatile boolean isRunning = false;
	private StreamConnection conn; // client connection
	private String clientName;
	
	public ThreadedEchoHandler(StreamConnection conn)
	{
		this.conn = conn;
		// store the name of the connected client
		clientName = reportDeviceName(conn);
		System.out.println(" Handler spawned for client: " + clientName);
	} // end of ThreadedEchoHandler()
	
	public String reportDeviceName(StreamConnection conn)
	/* Return the 'friendly' name of the device being examined,
	or "device ??" */
	{
		String devName;
		try {
			RemoteDevice rd = RemoteDevice.getRemoteDevice(conn);
			devName = rd.getFriendlyName(false);
			}
		catch (IOException e)
			{ devName = "device ??"; }
	return devName;
	} // end of reportDeviceName()
	
	
	
	public void run()
	{
		
		try {
			// Get I/O streams from the stream connection
			in = conn.openInputStream();
			out = conn.openOutputStream();
			processMsgs();
			System.out.println(" Closing " + clientName + " connection");
			if (conn != null) {
			in.close();
			out.close();
			conn.close();
			}
			}
			catch (IOException e)
			{ System.out.println(e); }
} // end of run()
	
	
	

	private void processMsgs()
	{
			isRunning = true;
			String line;
			while (isRunning) {
				if((line = readData()) == null)
					isRunning = false;
				else { // there was some input
					System.out.println(" " + clientName + " --> \"" +
							line + "\"");
					if (line.trim().equals("bye$$"))
						isRunning = false;
					else {
						String upper = line.trim().toUpperCase();
						if (isRunning) {
							System.out.println(" " + clientName + " <--- " + upper);
							sendMessage(upper);
						}
					}
				}
	
			}
	
	}
	
	
	
	private String readData()
	{
		byte[] data = null;
			try {
				int len = in.read(); // get the message length
				if (len <= 0) {
					System.out.println(clientName + ": Message Length Error");
					return null;
				}
				data = new byte[len];
				len = 0;
	// read the message, perhaps requiring several read() calls
				while (len != data.length) {
					int ch = in.read(data, len, data.length - len);
					if (ch == -1) {
						System.out.println(clientName + ": Message Read Error");
						return null;
					}
					len += ch;
				}
			}
			
	catch (IOException e)
	{ System.out.println("readData(): " + e);
	return null;
	}
	return new String(data).trim(); // convert byte[] to String
	
	} // end of readData()
	
	
	
	public boolean sendMessage(String msg)
	{
	try {
		out.write(msg.length());
		out.write(msg.getBytes());
		out.flush();
		return true;
	}
	catch (Exception e)
		{ System.out.println(clientName + " sendMessage(): " + e);
		return false;
		}
	}	
	
	
			
	
	
	public void start(){
				
		isRunning = false;
	}
	
	public void closeDown()
	{ isRunning = false; }
	
}
