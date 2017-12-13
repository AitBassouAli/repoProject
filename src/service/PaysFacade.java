/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Pays;
import bean.UserService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author HP
 */
public class PaysFacade extends AbstractFacade {

    public List<Object> findAllPaysObjects() throws IOException {
        UserService userService = new UserService("findAllPaysObjects");
        userService = doExecute(userService);
        return userService.getObjetList();
    }

    public List<Pays> findAll() throws IOException {
        List<Pays> list = new ArrayList<>();
        List<Object> objects = findAllPaysObjects();
        if (!objects.isEmpty()) {
            objects.stream().forEach((object) -> {
                list.add((Pays) object);
            });
        }
        return list;
    }

}
