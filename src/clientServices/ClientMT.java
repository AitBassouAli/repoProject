/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientServices;

import bean.ConnectedUsers;
import bean.User;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import util.Session;

/**
 *
 * @author HP
 */
public class ClientMT {

    private Socket clientS;

    public ClientMT(Socket clientS) {
        this.clientS = clientS;
    }

    public ClientMT() {
    }

    public Socket connecter() throws IOException {
        clientS = new Socket("localhost", 50834);
        Session.setAttribut(clientS, "connectedSocket");
        return clientS;
    }

    public ConnectedUsers connection(ConnectedUsers connect) throws IOException {
        Socket s = connecter();
        connect.setPort(s.getLocalPort());
        connect.setIp(s.getInetAddress().getHostAddress());
        return connect;

    }

    public void send(User ConnectedUser, ConnectedUsers portDist, String msg) throws IOException {
        PrintWriter pw = new PrintWriter(clientS.getOutputStream(), true);
        String msgInfos = ConnectedUser.getId() + "=>" + msg + "@" + portDist.getIp() + "//" + portDist.getPort();
        pw.println(msgInfos);
    }

    public String[] recieve() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(clientS.getInputStream()));
        String msg = br.readLine();
        String[] id_Msg = msg.split("=>");
        return id_Msg;
    }

}
