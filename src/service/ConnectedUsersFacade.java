/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.ConnectedUsers;
import bean.User;
import java.util.Date;
import java.util.List;

/**
 *
 * @author HP
 */
public class ConnectedUsersFacade extends AbstractFacade<ConnectedUsers> {

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

    public void connect(User user) {
        ConnectedUsers connecte = new ConnectedUsers();
        connecte.setUser(user);
        connecte.setDateConnection(new Date());
        create(connecte);
    }

    public List<User> getUsers() {
        return getEntityManager().createQuery("SELECT c.user FROM ConnectedUsers c").getResultList();
    }
}
