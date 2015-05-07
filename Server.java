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
 
 To compile: javac Server.java
 To run:     java -classpath ".:ojdbc7.jar" Server
 */
public class Server {
    
    private static int serverprot = 9890;
    private static int postpoint = 5;
    private static int ratpoint = 1;
    
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
        System.out.println("receive request: " + req);
        //TODO
        //distinguish different requests and call corresponding function.
        String[] rsp = req.split("\\|");
        //LOG|username|password
        //user login
        if(rsp[0].equals("LOG") && rsp.length==3){
            String userid;
            try {
                ResultSet retset = executeQuery("SELECT * FROM Users WHERE username='" + rsp[1] + "' AND password='" + rsp[2] + "'");
                if(retset.next()==false){
                    return "failed";
                } else {
                    userid = Integer.toString(retset.getInt("ID"));
                }
                retset.getStatement().getConnection().close();//dont forget to close the connection.
                return userid;
            } catch (Exception e) {
                System.out.println(e);
                return "error";
            }
        }
        
        //REG|username|password|email
        //register user
        else if(rsp[0].equals("REG")){
            try {
                ResultSet retset = executeQuery("SELECT * FROM Users WHERE username='" + rsp[1] + "'");
                if(retset.next()){
                    retset.getStatement().getConnection().close();
                    return "duplicate";
                }
                else {
                    retset.getStatement().getConnection().close();
                    retset = executeQuery("INSERT INTO Users VALUES(users_seq.nextval,'" +rsp[1]+ "','" +rsp[2]+ "','"+rsp[3]+"',0)");
                    retset.getStatement().getConnection().close();
                    return "success";
                }
            } catch (Exception e) {
                System.out.println(e);
                return "error";
            }
        }
        
        //REG|userid|groupid
        //join group
        else if(rsp[0].equals("GRP")){
            try {
                ResultSet retset = executeQuery("SELECT * FROM Join_group WHERE user_id='" + rsp[1] + "' AND group_id='" + rsp[2] + "'");
                if(retset.next()==true){
                    retset.getStatement().getConnection().close();
                    return "duplicate";
                }
                else {
                    retset.getStatement().getConnection().close();
                    retset = executeQuery("INSERT INTO Join_group VALUES(" + rsp[1]+ "," +rsp[2]+")");
                    retset.getStatement().getConnection().close();
                    return "success";
                }
            } catch (Exception e) {
                System.out.println(e);
                return "error";
            }
        }
        
        //VGL|userid
        //view joined group list
        else if(rsp[0].equals("VGL")){
            try {
                String retstr="";
                int gid;
                String gname;
                String gdesc;
                ResultSet retset = executeQuery("SELECT * FROM Groups,Join_group WHERE Groups.id=Join_group.Group_id AND Join_group.user_id='" + rsp[1] + "'");
                while(retset.next()){
                    gid = retset.getInt("ID");
                    gname = retset.getString("NAME");
                    gdesc = retset.getString("DESCRIPTION");
                    retstr = retstr+"+"+Integer.toString(gid)+"|"+gname+"|"+gdesc;
                }
                System.out.println(retstr);
                retset.getStatement().getConnection().close();
                return retstr;
            } catch (Exception e) {
                System.out.println(e);
                return "error";
            }
        }
        
        //VUGL|userid
        //view not joined group list
        else if(rsp[0].equals("VUGL")){
            try {
                String retstr="";
                int gid;
                String gname;
                String gdesc;
                ResultSet retset = executeQuery("SELECT * from Groups WHERE id NOT IN (SELECT id FROM Groups,Join_group WHERE Groups.id=Join_group.Group_id AND Join_group.user_id='" + rsp[1] + "')");
                while(retset.next()){
                    gid = retset.getInt("ID");
                    gname = retset.getString("NAME");
                    gdesc = retset.getString("DESCRIPTION");
                    retstr = retstr+"+"+Integer.toString(gid)+"|"+gname+"|"+gdesc;
                }
                System.out.println(retstr);
                retset.getStatement().getConnection().close();
                return retstr;
            } catch (Exception e) {
                System.out.println(e);
                return "error";
            }
        }
        
        //RES|name|desc
        //add restaurant
        else if(rsp[0].equals("RES")){
            try {
                ResultSet retset = executeQuery("INSERT INTO Restaurants VALUES(restaurants_seq.nextval,'" +rsp[1]+ "','" +rsp[2]+"',0)");
                retset.getStatement().getConnection().close();
                return "success";
            } catch (Exception e) {
                System.out.println(e);
                return "error";
            }
        }
        
        
        //RRE|userid|restid|content
        //post restaurant review
        else if(rsp[0].equals("PRE")){
            try {
                ResultSet retset = executeQuery("INSERT INTO Restaurants_reviews VALUES(restaurants_Reviews_seq.nextval," +rsp[1]+ "," + rsp[2] + ",'" + rsp[3] + "',0,0," + postpoint + ")");
                retset.getStatement().getConnection().close();
                return "success";
            } catch (Exception e) {
                System.out.println(e);
                return "error";
            }
        }
        
        //RLC|userid|title|subj|brand|comment|price|state
        //post laptop comment
        else if(rsp[0].equals("RLC")){
            try {
                System.out.println("INSERT INTO Laptop_comments VALUES(laptop_comments_seq.nextval," +rsp[1]+ ",'" + rsp[2] + "','" + rsp[3] + "','" + rsp[4] + "','" + rsp[5] + "',0,0," + rsp[6] + ",'" + rsp[7] + "')");
                ResultSet retset = executeQuery("INSERT INTO Laptop_comments VALUES(laptop_comments_seq.nextval," +rsp[1]+ ",'" + rsp[2] + "','" + rsp[3] + "','" + rsp[4] + "','" + rsp[5] + "',0,0," + rsp[6] + ",'" + rsp[7] + "'," + postpoint + ")");
                retset.getStatement().getConnection().close();
                return "success";
            } catch (Exception e) {
                System.out.println(e);
                return "error";
            }
        }
        
        //RAT|userid|groupflag|goddbadgflag-----1=rest 2=comp, 1=good 2=bad
        //rate the review or comment
        else if(rsp[0].equals("RAT")){
            return "TODO";
        }
        
        //VRL
        //return restaurant lists
        else if(rsp[0].equals("VRL")){
            try {
                String retstr="";
                String rname;
                String rcontent;
                int rid;
                int rrat;
                ResultSet retset = executeQuery("SELECT * FROM Restaurants");
                while(retset.next()){
                    rid = retset.getInt("ID");
                    rname = retset.getString("NAME");
                    rcontent = retset.getString("DESCRIPTION");
                    rrat = retset.getInt("RATING");
                    retstr = retstr+"+"+Integer.toString(rid)+"|"+rname+"|"+rcontent+"|"+Integer.toString(rrat);
                }
                System.out.println(retstr);
                retset.getStatement().getConnection().close();
                return retstr;
            } catch (Exception e) {
                System.out.println(e);
                return "error";
            }
        }
        
        //VRR|restid
        //view restaurant reviews
        else if(rsp[0].equals("VRR")){
            try {
                String retstr="";
                int revid;
                int userid;
                String username;
                int restid;
                String revbody;
                int good;
                int bad;
                int point;
                ResultSet retset = executeQuery("SELECT Restaurants_reviews.id,Restaurants_reviews.user_id,Restaurants_reviews.restaurant_id,Restaurants_reviews.review_content,Restaurants_reviews.good,Restaurants_reviews.bad,Restaurants_reviews.point,Users.username FROM Restaurants_reviews,Users WHERE  Restaurants_reviews.user_id=Users.id AND restaurant_id='" + rsp[1] + "'");
                while(retset.next()){
                    revid = retset.getInt("ID");
                    userid = retset.getInt("USER_ID");
                    username = retset.getString("USERNAME");
                    restid = retset.getInt("RESTAURANT_ID");
                    revbody = retset.getString("REVIEW_CONTENT");
                    good = retset.getInt("GOOD");
                    bad = retset.getInt("BAD");
                    point = retset.getInt("POINT");
                    retstr = retstr+"+"+Integer.toString(revid)+"|"+Integer.toString(userid)+"|"+username+"|"+Integer.toString(restid)+"|"+revbody+"|"+Integer.toString(good)+"|"+Integer.toString(bad)+"|"+Integer.toString(point);
                }
                System.out.println(retstr);
                retset.getStatement().getConnection().close();
                return retstr;
            } catch (Exception e) {
                System.out.println(e);
                return "error";
            }
        }
        
        //VLC
        //view laptop comment
        else if(rsp[0].equals("VLC")){
            try {
                String retstr="";
                int cid;
                int userid;
                String ctitle;
                String subj;
                String brand;
                String cbody;
                int good;
                int bad;
                int price;
                String state;
                int point;
                ResultSet retset = executeQuery("SELECT * FROM Laptop_comments");
                while(retset.next()){
                    cid = retset.getInt("ID");
                    userid = retset.getInt("USER_ID");
                    ctitle = retset.getString("TITLE");
                    subj = retset.getString("SUBJECT");
                    brand = retset.getString("BRAND");
                    cbody = retset.getString("COMMENT_CONTENT");
                    good = retset.getInt("GOOD");
                    bad = retset.getInt("BAD");
                    price = retset.getInt("PRICE");
                    state = retset.getString("STATE");
                    point = retset.getInt("POINT");
                    retstr = retstr+"+"+Integer.toString(cid)+"|"+Integer.toString(userid)+"|"+ctitle+"|"+subj+"|"+brand+"|"+cbody+"|"+Integer.toString(good)+"|"+Integer.toString(bad)+"|"+Integer.toString(price)+"|"+state+"|"+Integer.toString(point);
                }
                System.out.println(retstr);
                retset.getStatement().getConnection().close();
                return retstr;
            } catch (Exception e) {
                System.out.println(e);
                return "error";
            }
        }
        
        
        return "Unknown request";
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
            String user="sfeng14";
            String pw="111123fs";
            
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