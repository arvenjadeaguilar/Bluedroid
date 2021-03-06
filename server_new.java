    import java.io.BufferedReader;
    import java.io.IOException;
    import java.io.InputStream;
    import java.io.InputStreamReader;
    import java.io.OutputStream;
    import java.io.OutputStreamWriter;
    import java.io.PrintWriter;
     
    import javax.bluetooth.*;
    import javax.microedition.io.*;
     
    
    public class Server {
       
        //start server
        private void startServer() throws IOException{
     
            //Create a UUID for SPP
            UUID uuid = new UUID("1101", true);
            //Create the servicve url
            String connectionString = "btspp://localhost:" + uuid +";name=Sample SPP Server";
           
            //open server url
            StreamConnectionNotifier streamConnNotifier = (StreamConnectionNotifier)Connector.open( connectionString );
           
            //Wait for client connection
            System.out.println("\nServer Started. Waiting for clients to connect...");
            StreamConnection connection=streamConnNotifier.acceptAndOpen();
     
            RemoteDevice dev = RemoteDevice.getRemoteDevice(connection);
            System.out.println("Remote device address: "+dev.getBluetoothAddress());
            System.out.println("Remote device name: "+dev.getFriendlyName(true));
           
            //read string from server client
            InputStream inStream=connection.openInputStream();
            BufferedReader bReader=new BufferedReader(new InputStreamReader(inStream));
            String lineRead=bReader.readLine();
            System.out.println(lineRead);
           
            
            OutputStream outStream=connection.openOutputStream();
            PrintWriter pWriter=new PrintWriter(new OutputStreamWriter(outStream));
            pWriter.write("Response String from the Server\r\n");
            pWriter.flush();
     
            pWriter.close();
            streamConnNotifier.close();
     
        }
     
     
        public static void main(String[] args) throws IOException {
           
            //display local device address and name
            LocalDevice localDevice = LocalDevice.getLocalDevice();
            System.out.println("Address: "+localDevice.getBluetoothAddress());
            System.out.println("Name: "+localDevice.getFriendlyName());
           
            Server server=new Server();
            Server.startServer();
           
        }
    }

