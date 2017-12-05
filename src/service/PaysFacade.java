/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Pays;
import java.util.List;

/**
 *
 * @author HP
 */
public class PaysFacade extends AbstractFacade<Pays> {

    public PaysFacade() {
        super(Pays.class);
    }

    @Override
    public List<Pays> findAll() {
        return getEntityManager().createQuery("SELECT p FROM Pays p ORDER BY p.nom ASC").getResultList();
    }

}
