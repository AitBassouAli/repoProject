/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.ConnectedUsers;
import bean.User;
import clientServices.ClientMT;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;
import java.util.List;
import util.Session;

/**
 *
 * @author HP
 */
public class ConnectedUsersFacade extends AbstractFacade<ConnectedUsers> {

    private ClientMT clientMT;

    public ConnectedUsersFacade() {
        super(ConnectedUsers.class);
    }

    public ConnectedUsers findByUser(User user) {
        try {
            return (ConnectedUsers) getEntityManager().createQuery("select c from ConnectedUsers c where c.user.id =" + user.getId()).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public void deconnect(User user) {
        ConnectedUsers c = findByUser(user);
        if (c == null) {
        } else {
            remove(c);
        }
    }

    public void connect(User user) throws IOException {
        clientMT = new ClientMT();
        ConnectedUsers connecte = new ConnectedUsers();
        connecte.setUser(user);
        connecte.setDateConnection(new Date());
        connecte = clientMT.connection(connecte);
        create(connecte);
    }

    public List<User> getUsers() {
        return getEntityManager().createQuery("SELECT c.user FROM ConnectedUsers c").getResultList();
    }

    public void envoyer(User connectedUser, User userDist, String msg) throws IOException {
        ConnectedUsers portDist = findByUser(userDist);
        clientMT = new ClientMT((Socket) Session.getAttribut("connectedSocket"));
        clientMT.send(connectedUser, portDist, msg);
    }
}
