/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.io.Serializable;

/**
 *
 * @author HP
 */
public class Conversation implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private User sender;
    private User reciever;

    public Conversation() {
    }

    public Conversation(User sender, User reciever) {
        this.sender = sender;
        this.reciever = reciever;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReciever() {
        return reciever;
    }

    public void setReciever(User reciever) {
        this.reciever = reciever;
    }

}
