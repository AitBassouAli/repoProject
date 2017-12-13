/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author HP
 */
public class UserService implements Serializable {

    private static final long serialVersionUID = 1L;
    private Object objet;
    private User connectedUser;
    private String serviceToExecute;
    private int serviceReturn;
    private int port;
    private List<Object> objetList;

    public UserService() {
    }

    public UserService(String serviceToExecute) {
        this.serviceToExecute = serviceToExecute;
    }

    public UserService(Object objet, String serviceToExecute) {
        this.objet = objet;
        this.serviceToExecute = serviceToExecute;
    }

    public UserService(Object user, String serviceToExecute, int serviceReturn) {
        this.objet = user;
        this.serviceToExecute = serviceToExecute;
        this.serviceReturn = serviceReturn;
    }

    public UserService(Object objet, User connectedUser, String serviceToExecute) {
        this.objet = objet;
        this.connectedUser = connectedUser;
        this.serviceToExecute = serviceToExecute;
    }

    public UserService(Object user, User connectedUser, String serviceToExecute, int serviceResturn) {
        this.objet = user;
        this.connectedUser = connectedUser;
        this.serviceToExecute = serviceToExecute;
        this.serviceReturn = serviceResturn;
    }

    public Object getObjet() {
        return objet;
    }

    public void setObjet(Object objet) {
        this.objet = objet;
    }

    public User getConnectedUser() {
        return connectedUser;
    }

    public void setConnectedUser(User connectedUser) {
        this.connectedUser = connectedUser;
    }

    public String getServiceToExecute() {
        return serviceToExecute;
    }

    public void setServiceToExecute(String serviceToExecute) {
        this.serviceToExecute = serviceToExecute;
    }

    public int getServiceReturn() {
        return serviceReturn;
    }

    public void setServiceReturn(int serviceReturn) {
        this.serviceReturn = serviceReturn;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public List<Object> getObjetList() {
        return objetList;
    }

    public void setObjetList(List<Object> objetList) {
        this.objetList = objetList;
    }

    @Override
    public String toString() {
        return "UserService{" + "objet=" + objet + ", serviceToExecute=" + serviceToExecute + ", serviceResturn=" + serviceReturn + ", port=" + port + " objetList= " + objetList + '}';
    }

}
