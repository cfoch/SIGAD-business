/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.business.helpers;

import com.sigad.sigad.app.controller.LoginController;
import com.sigad.sigad.business.Permiso;
import java.util.ArrayList;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

/**
 *
 * @author jorgeespinoza
 */
public class PermisoHelper extends BaseHelper{
    public PermisoHelper() {
    }
    
    /*Get all*/
    public ArrayList<Permiso> getPermissions(){
        ArrayList<Permiso> permissions = null;
        Query query = null;
        try {
            query = session.createQuery("from Permiso");
            
            if(!query.list().isEmpty()){
               permissions = (ArrayList<Permiso>)( query.list());
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            errorMessage = e.getMessage();
        } finally{
            return permissions;
        }
    };
    
    /*Get permission by id, if nothin then null*/
    public Permiso getPermission(int id){
        Permiso p = null;
        Query query = null;
        try {
            query = session.createQuery("from Permiso where id=" + Integer.toString(id));
            
            if(!query.list().isEmpty()){
                p = (Permiso) query.list().get(0);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally{
            return p;
        }
    }
    
    /*Get permission by name, if nothin then null*/
    public Permiso getPermission(String menu){
        Permiso p = null;
        Query query = null;
        try {
            query = session.createQuery("from Permiso where menu='" + menu + "'");
            
            if(!query.list().isEmpty()){
                p = (Permiso) query.list().get(0);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally{
            return p;
        }
    }
    
    public Long savePermission(Permiso p){
        Long id = null;
        try {
            Transaction tx;
            if(session.getTransaction().isActive()){
                tx = session.getTransaction();
            }else{
                tx = session.beginTransaction();
            }
            
            session.save(p);
            if(p.getId() != null){
                id = p.getId();
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return id;
    }
    
    public boolean updatePermission(Permiso pOld){
        boolean ok = false;
        try {
            Transaction tx;
            if(session.getTransaction().isActive()){
                tx = session.getTransaction();
            }else{
                tx = session.beginTransaction();
            }
            
            Permiso pNew = session.load(Permiso.class, pOld.getId());
            
            pNew.setMenu(pOld.getMenu());
            pNew.setIcono(pOld.getIcono());
            
            session.merge(pNew);
            tx.commit();
            session.close();
            ok = true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            this.errorMessage = e.getMessage();
        }
        return ok;
    }
}
