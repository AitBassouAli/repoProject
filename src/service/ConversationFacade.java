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
public class ConversationFacade extends AbstractFacade {

    public List<Object> findConversationObjects(User user) throws IOException {
        UserService userService = new UserService(user, "findConversationObjects");
        userService = doExecute(userService);
        return userService.getObjetList();
    }

    public List<Conversation> findByUser(User user) throws IOException {
        List<Conversation> list = new ArrayList<>();
        List<Object> objects = findConversationObjects(user);
        if (!objects.isEmpty()) {
            objects.stream().forEach((object) -> {
                list.add((Conversation) object);
            });
        }
        return list;
    }

    public Conversation findOrCreate(Conversation conversation) throws IOException {
        UserService userService = new UserService(conversation, "findOrCreateConversation");
        userService = doExecute(userService);
        return (Conversation) userService.getObjet();

    }

}
