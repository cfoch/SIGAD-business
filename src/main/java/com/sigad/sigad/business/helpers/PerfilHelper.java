package com.sigad.sigad.business.helpers;

import com.sigad.sigad.app.controller.LoginController;
import com.sigad.sigad.business.Perfil;
import java.util.ArrayList;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jorgeespinoza
 */
public class PerfilHelper extends BaseHelper{
    public PerfilHelper() {
    }
        
    /*Get all*/
    public ArrayList<Perfil> getProfiles(){
        ArrayList<Perfil> profiles = null;
        Query query = null;
        try {
            query = session.createQuery("from Perfil");
            
            if(!query.list().isEmpty()){
               profiles = (ArrayList<Perfil>)( query.list());
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            errorMessage = e.getMessage();
        } finally{
            return profiles;
        }
    };
    
    /*Get profile by id, if nothin then null*/
    public Perfil getProfile(int id){
        Perfil p = null;
        Query query = null;
        try {
            query = session.createQuery("from Perfil where id=" + Integer.toString(id));
            
            if(!query.list().isEmpty()){
                p = (Perfil) query.list().get(0);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally{
            return p;
        }
    }
    
    /*Get profile by name, if nothin then null*/
    public Perfil getProfile(String nombre){
        Perfil p = null;
        Query query = null;
        try {
            query = session.createQuery("from Perfil where nombre='" + nombre + "'");
            
            if(!query.list().isEmpty()){
                p = (Perfil) query.list().get(0);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally{
            return p;
        }
    }
    
    public Long saveProfile(Perfil p){
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
            this.errorMessage = e.getMessage();
        }
        return id;
    }
    
    public boolean updateProfile(Perfil pOld){
        boolean ok = false;
        try {
            Transaction tx;
            if(session.getTransaction().isActive()){
                tx = session.getTransaction();
            }else{
                tx = session.beginTransaction();
            }
            
            Perfil pNew = session.load(Perfil.class, pOld.getId());
            
            pNew.setNombre(pOld.getNombre());
            pNew.setDescripcion(pOld.getDescripcion());
            pNew.setPermisos(pOld.getPermisos());
            pNew.setActivo(pOld.isActivo());
            
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
