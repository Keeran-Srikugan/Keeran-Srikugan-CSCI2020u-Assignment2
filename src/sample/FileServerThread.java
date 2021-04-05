package sample;
import java.io.*;
import java.net.*;
import java.util.*;

public class FileServerThread extends Thread {
    protected Socket socket       = null;
    protected PrintWriter out     = null;
    protected BufferedReader in   = null;


    protected Vector messages     = null;

    public FileServerThread(Socket socket, Vector messages) {
        super();
        this.socket = socket;
        this.messages = messages;
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.err.println("IOEXception while opening a read/write connection");
        }
    }

    public void run() {
        // initialize interaction
        out.println("Connected to File Server");

        boolean endOfSession = false;
        while(!endOfSession) {
            endOfSession = processCommand();
        }

        try {
            socket.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    //Here is where the input message from
    protected boolean processCommand() {
        String message = null;
        try {
            message = in.readLine();
        } catch (IOException e) {
            System.err.println("Error reading command from socket.");
            return true;
        }
        if (message == null) {
            return true;
        }

        StringTokenizer st = new StringTokenizer(message);
        String command = st.nextToken();
        String args = null;
        if (st.hasMoreTokens()) {
            args = message.substring(command.length()+1, message.length());
        }
        return processCommand(command, args);
    }

    //This is where the command cases are defined
    protected boolean processCommand(String command, String arguments) {
        if (command.equalsIgnoreCase("Download")) {
            System.out.println("Download function being called with file: " + arguments);

            File inputFile = new File("./input/Server/"+arguments);

            //Here I print out the contents of the file along with sending the information to the server
            System.out.println("Contents of file:");
            System.out.println("(start)");
            try {
                String token;
                Scanner scanner = new Scanner(inputFile);
                while(scanner.hasNext()){
                    token = scanner.nextLine();
                    System.out.println(token);
                    out.println(token);
                }
                out.println((char[]) null);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            //indicated that the file content has ended
            System.out.println("(end)");

            return false;

        } else if (command.equalsIgnoreCase("Upload")) {
            System.out.println("Upload function being called with file: " + arguments);
            System.out.println("Contents of the file being uploaded:");
            File newFile = new File("./input/Server/"+arguments);


            System.out.println("(start)");
            try{
                FileWriter writeToFile = new FileWriter(newFile);

                String token = in.readLine();
                while (token != null){
                    System.out.println(token);
                    writeToFile.write(token + '\n');
                    token = in.readLine();
                }

                writeToFile.close();
                System.out.println("(end)");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
        return true;
    }

}