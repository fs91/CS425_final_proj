import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


/*
 CS425 final project
 Su Feng, Jiaqi Li, Hanqiao Lu
 
 Sample client to test the server 
 */
public class Client {

    private static BufferedReader in;
    private static PrintWriter out;
    
    private static String serveraddr = "";
    private static int serverport = 9890;
    
    public static void main(String[] args) throws IOException {
        Socket s = new Socket(serveraddr, serverport);
        in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        out = new PrintWriter(s.getOutputStream(), true);
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        out.println(input);
        String ret = in.readLine();
        System.out.println(ret);
        System.exit(0);
        in.close();
        out.close();
        s.close();
    }

}