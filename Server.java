import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.*;


/*
 CS425 final project
 Su Feng, Jiaqi Li, Hanqiao Lu
 
 The Server that takes request from the client and operate the databse correspondingly.
 */
public class Server {
    
    private static int serverprot = 9890;
    
    /*
     initialize the server
     */
    public static void main(String[] args) throws Exception {
        System.out.println("The server is running.");
        int clientNumber = 0;
        ServerSocket listener = new ServerSocket(serverprot);
        try {
            while (true) {
                new ServerHandler(listener.accept(), clientNumber++).start();
            }
        } finally {
            listener.close();
        }
    }
    /*
     Create client handler thread
     */
    private static class ServerHandler extends Thread {
        private Socket socket;
        private int clientNumber;
        
        public ServerHandler(Socket socket, int clientNumber) {
            this.socket = socket;
            this.clientNumber = clientNumber;
            log("New connection with client: " + clientNumber + " at " + socket);
        }
        
        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                //take request from client
                while (true) {
                    String input = in.readLine();
                    if (input == null || input.equals(".")) {
                        break;
                    }
                    String output = processRequest(input);
                    out.println(output);
                }
            } catch (IOException e) {
                log("Error handling client: " + clientNumber + ": " + e);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    log("Couldn't close a socket.");
                }
                log("Connection with client: " + clientNumber + " closed");
            }
        }
    }
    
    /*
     short cut function to print a message into terminal
     */
    private static void log(String message) {
        System.out.println(message);
    }
        
    private static String processRequest(String req) {
        //TODO
        //distinguish different requests and call corresponding function.
        if(req.equals("test")){
            //this is a sample request and the following shows how to execute the query and retrieve the result set.
            try {
                ResultSet retset = executeQuery("SELECT * FROM Class");
                retset.next();
                String firsttitle = retset.getString("TITLE");
                retset.getStatement().getConnection().close();//dont forget to close the connection.
                return firsttitle;
            } catch (Exception e) {
                System.out.println(e);
                return "failed";
            }
        }
        return "success";
    }
    
    //TODO add functions to modify database
    
    
    /*
     The funcation used to execute a query. The return value contains the corresponding selected content.
     */
    private static ResultSet executeQuery(String query){
        Connection conn;
        Statement stmt = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            String url="jdbc:oracle:thin:@fourier.cs.iit.edu:1521:orcl";
            String user="username";//your username
            String pw="password";//your password
            
            conn = DriverManager.getConnection(url, user, pw);
            
            if(conn == null) {
                System.out.println("Connection unsuccess");
            }
            
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            return rs;
        } catch (Exception e) {
            System.out.println("ExecuteQuery: " + e);
        }
        return null;
    }
        
    
}