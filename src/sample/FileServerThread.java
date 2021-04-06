package sample;
import java.io.*;
import java.net.*;
import java.util.*;

public class FileServerThread extends Thread {
    protected Socket socket       = null;
    protected PrintWriter out     = null;
    protected BufferedReader in   = null;


    protected Vector messages     = null;

    //This is the constructor for the thread
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

    //This is the run function for the thread classs that gets excecuted immediately
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
    //The message filters for the key word either download or
    //upload with the file name right after
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

        //Here is where the input gets seperated
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
        //The first if checks for the download keyword
        if (command.equalsIgnoreCase("Download")) {
            System.out.println("Download function being called with file: " + arguments);

            //Path for suerver file
            File inputFile = new File("./input/Server/"+arguments);

            //Here I print out the contents of the file along with sending the information to the server
            System.out.println("Contents of file:");
            System.out.println("(start)");
            //Here the file gets accessed, then the program reads each line
            //and then sends our the line one by one for the client file to read
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

        //Here is the second else where it gets checked for the upload keyword
        } else if (command.equalsIgnoreCase("Upload")) {
            System.out.println("Upload function being called with file: " + arguments);
            System.out.println("Contents of the file being uploaded:");
            File newFile = new File("./input/Server/"+arguments);


            System.out.println("(start)");
            try{
                //Here the file is created
                FileWriter writeToFile = new FileWriter(newFile);

                //Here it reads the lines being outputted from the client
                //side and writes it into the new file
                String token = in.readLine();
                while (token != null){
                    System.out.println(token);
                    writeToFile.write(token + '\n');
                    token = in.readLine();
                }

                //Here the write gets closed
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