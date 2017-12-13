/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientServices;

import bean.ConnectedUsers;
import bean.Message;
import bean.User;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

    public void deconnecter() throws IOException {
        Socket socket = (Socket) Session.getAttribut("connectedSocket");
        socket.close();
        Session.setAttribut(null, "connectedSocket");
    }

    public void quiter() throws IOException {
        Socket serviceSocket = (Socket) Session.getAttribut("connectedServiceSocket");
        serviceSocket.close();
        Session.setAttribut(null, "connectedServiceSocket");
        deconnecter();
    }

    public void beforConnection() throws IOException {
        Socket serviceSocket = new Socket("localhost", 60830);
        Session.setAttribut(serviceSocket, "connectedServiceSocket");

    }

    public ConnectedUsers connection(ConnectedUsers connect) throws IOException {
        Socket s = connecter();
        connect.setPort(s.getLocalPort());
        connect.setIp(s.getInetAddress().getHostAddress());
        return connect;

    }

    public void send(User ConnectedUser, ConnectedUsers portDist, String msg) throws IOException {
        Message message = new Message(ConnectedUser, portDist.getPort(), msg);
        ObjectOutputStream outObject = new ObjectOutputStream(clientS.getOutputStream());
        outObject.writeObject(message);
        outObject.flush();
        System.out.println("sendiiing");
//        PrintWriter pw = new PrintWriter(clientS.getOutputStream(), true);
//        String msgInfos = ConnectedUser.getId() + "=>" + msg + "@" + portDist.getIp() + "//" + portDist.getPort();
//        pw.println(msgInfos);
    }

    public Message recieve() throws IOException, ClassNotFoundException {
//        BufferedReader br = new BufferedReader(new InputStreamReader(clientS.getInputStream()));
//        String msg = br.readLine();
//        String[] id_Msg = msg.split("=>");
//        return id_Msg;
        System.out.println("recieving");
        ObjectInputStream inOpject = new ObjectInputStream(clientS.getInputStream());
        Message message = (Message) inOpject.readObject();
        return message;
    }

}
