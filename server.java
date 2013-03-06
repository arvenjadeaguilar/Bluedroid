import javax.bluetooth.*;
import javax.microedition.*;
import javax.microedition.io.*;
import javax.obex.*;
import java.io.*;
import java.util.*;
public class server {

  private ArrayList<ThreadedEchoHandler> handlers;
	private volatile boolean isRunning = false;
	private static final String UUID_STRING = "11111111111111111111111111111111";
	private static final String SERVICE_NAME = "echoserver";
	private StreamConnectionNotifier server;
	
	public server(){
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run()
			{ closeDown(); }
			});
		
	}
		
	public void initDevice()
	{
		try { // make the server's device discoverable
			LocalDevice local = LocalDevice.getLocalDevice();
			System.out.println("Device name: " + local.getFriendlyName());
			System.out.println("Bluetooth Address: " +
					local.getBluetoothAddress());
			boolean res = local.setDiscoverable(DiscoveryAgent.GIAC);
			System.out.println("Discoverability set: " + res);
		}
		catch (BluetoothStateException e) {
			System.out.println(e);
			System.exit(1);
	}
	} // end of initDevice()
	
	public void createRFCOMMConnection()
	{
		try {
			System.out.println("Start advertising " + SERVICE_NAME + "...");
			server = (StreamConnectionNotifier) Connector.open(	"btspp://localhost:" + UUID_STRING +";name=" + SERVICE_NAME + ";authenticate=false");
	}
	catch (IOException e) {
		System.out.println(e);
		System.exit(1);
		}
	} // end of createRFCOMMConnection()
	
	
	public void processClients()
	{
	isRunning = true;
		try {
			while (isRunning) {
				System.out.println("Waiting for incoming connection...");
				StreamConnection conn = server.acceptAndOpen();
				// wait for a client connection
				System.out.println("Connection requested...");
				ThreadedEchoHandler hand = new ThreadedEchoHandler(conn);
				// create client handler
				handlers.add(hand);
				hand.start();
				hand.run();
				hand.sendMessage("hello");
				}
			}
		catch (IOException e) {
			System.out.println(e);
			}
		} // end of processClients()
	private void closeDown()
	{
		System.out.println("Closing down server");
		if (isRunning) {
			isRunning = false;
			try {
				server.close();
			}
			catch (IOException e)
			{ System.out.println(e); }
			// close down all the handlers
			for (ThreadedEchoHandler hand : handlers)
				hand.closeDown();
			handlers.clear();
		}
	} // end of closeDown();
	
	
	
	// globals: UUID and name of the echo service
	
	// 32 hex digit string (will become 128 bit ID)
	
	
}
