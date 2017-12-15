/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Conversation;
import bean.User;
import bean.UserService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author HP
 */
public class UserFacade extends AbstractFacade {

    ConnectedUsersFacade connectedUsersFacade = new ConnectedUsersFacade();
    PaysFacade paysFacade = new PaysFacade();

    public Object[] seConnecter(User user) throws IOException {
        UserService userService = new UserService(user, "seConnecter");
        writeObject(userService);
        try {
            UserService service = getService();
            if (service.getServiceReturn() == 1) {
                connectedUsersFacade.connect((User) service.getObjet());
            }
            return new Object[]{service.getServiceReturn(), service.getObjet()};
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("seconnecter client facade   " + ex.getLocalizedMessage());
            return new Object[]{-7, null};
        }
    }

    public Object[] addUser(User user) throws IOException {
        UserService userService = new UserService(user, "addUser");
        writeObject(userService);
        try {
            UserService service = getService();
            return new Object[]{service.getServiceReturn(), service.getObjet()};
        } catch (IOException | ClassNotFoundException ex) {
            return new Object[]{-1, null};
        }
    }

    public User find(User user) throws IOException {
        UserService userService = new UserService(user, "find");
        return (User) doExecute(userService).getObjet();
    }

    public User findByEmail(User user) throws IOException {
        UserService userService = new UserService(user, "findByEmail");
        return (User) doExecute(userService).getObjet();
    }

    public User findByCreteres(User user) throws IOException {
        UserService userService = new UserService(user, "findByCreteres");
        return (User) doExecute(userService).getObjet();
    }

    public List<Object> getUserObjects() throws IOException {
        UserService userService = new UserService("getUserObjects");
        userService = doExecute(userService);
        return userService.getObjetList();
    }

    public List<User> getUsers() throws IOException {
        List<User> list = getUsersFromObjects(getUserObjects());
        return list;
    }

    public List<Object> findUsersObjectsContaints(String motRechercher) throws IOException {
        UserService userService = new UserService(motRechercher, "findUsersObjectsContaints");
        userService = doExecute(userService);
        return userService.getObjetList();
    }

    public List<User> findUsersContaints(String motRechercher) throws IOException {
        List<User> list = getUsersFromObjects(findUsersObjectsContaints(motRechercher));
        return list;
    }

    public List<User> getUsersFromObjects(List<Object> objects) throws IOException {
        List<User> list = new ArrayList<>();
        if (!objects.isEmpty()) {
            for (int i = 0; i < objects.size(); i++) {
                list.add((User) objects.get(i));
            }
        }
        return list;
    }

    public User deconnecter(User user) throws IOException {
        // connectedUsersFacade.deconnect();
        UserService userService = new UserService(user, "deconnecter");
        doExecute(userService);
        return null;
    }

    public User quiter(User user) throws IOException {
        connectedUsersFacade.quiter();
        UserService userService = new UserService(user, "deconnecter");
        doExecute(userService);
        return null;
    }

    public User modifier(User user, User connectedUser) throws IOException {
        UserService userService = new UserService(user, connectedUser, "modifier");
        userService = doExecute(userService);
        return (User) userService.getObjet();
    }

    public int sendPW(User user) throws IOException {
        UserService userService = new UserService(user, "sendPW");
        UserService service = doExecute(userService);
        return service.getServiceReturn();
    }

    public List<User> getUserFromConversation(User connectedUser, List<Conversation> conversations) {
        List<User> users = new ArrayList<>();
        conversations.stream().forEach((conversation) -> {
            users.add(conversation.getSender().equals(connectedUser) ? conversation.getReciever() : conversation.getSender());
        });
        return users;
    }

}
