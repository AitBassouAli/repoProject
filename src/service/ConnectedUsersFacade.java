/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.ConnectedUsers;
import bean.Conversation;
import bean.User;
import bean.UserService;
import clientServices.ClientMT;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;
import util.Session;

/**
 *
 * @author HP
 */
public class ConnectedUsersFacade extends AbstractFacade {

    private ClientMT clientMT;

    public ConnectedUsers findByUser(User user) throws IOException {
        UserService userService = new UserService(user, "findByUser");
        return (ConnectedUsers) doExecute(userService).getObjet();
    }

    public void create(ConnectedUsers connect) throws IOException {
        UserService userService = new UserService(connect, "createConnectedUser");
        doExecute(userService).getObjet();
    }

    public void connect(User user) throws IOException {
        clientMT = new ClientMT();
        ConnectedUsers connecte = new ConnectedUsers();
        connecte.setUser(user);
        connecte.setDateConnection(new Date());
        connecte = clientMT.connection(connecte);
        create(connecte);
    }

    public void deconnect() throws IOException {
        clientMT = new ClientMT();
        clientMT.deconnecter();
    }

    public void quiter() throws IOException {
        clientMT = new ClientMT();
        clientMT.quiter();
    }

    public void envoyer(User connectedUser, User userDist, String msg, Conversation conversation) throws IOException {
        ConnectedUsers portDist = findByUser(userDist);
        clientMT = new ClientMT((Socket) Session.getAttribut("connectedSocket"));
        clientMT.send(connectedUser, portDist, msg, conversation);
    }
}
