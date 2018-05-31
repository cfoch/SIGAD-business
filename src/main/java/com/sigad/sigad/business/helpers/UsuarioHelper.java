package com.sigad.sigad.business.helpers;

import com.sigad.sigad.app.controller.LoginController;
import com.sigad.sigad.business.Perfil;
import com.sigad.sigad.business.Usuario;
import java.util.ArrayList;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
public class UsuarioHelper {

    Session session = null;
    private String errorMessage = "";
    
    public UsuarioHelper() {
        session = LoginController.serviceInit();
    }
    
    /*Close session*/
    public void close(){
        session.close();
    }

    /**
     * @return the errorMessage
     */
    public String getErrorMessage() {
        return errorMessage;
    }
    
    /*Get all the users*/
    public ArrayList<Usuario> getUsers(){
        ArrayList<Usuario> users = null;
        Query query = null;
        try {
            query = session.createQuery("from Usuario");
            
            if(!query.list().isEmpty()){
               users = (ArrayList<Usuario>)( query.list());
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            errorMessage = e.getMessage();
        } finally{
            return users;
        }
    }
    
    /*Get users by id*/
    public Usuario getUser(int id){
        Usuario user = null;
        Query query = null;
        try {
            query = session.createQuery("from Usuario where id=" + Integer.toString(id));
            
            if(!query.list().isEmpty()){
               user = (Usuario)( query.list().get(0));
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            errorMessage = e.getMessage();
        } finally{
            return user;
        }
    };
    
    /*Get user by email*/
    public Usuario getUser(String email){
        Usuario user = null;
        Query query = null;
        try {
            query = session.createQuery("from Usuario where correo='" + email + "'");
            
            if(!query.list().isEmpty()){
                user = (Usuario) query.list().get(0);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            this.errorMessage = e.getMessage();
        } finally {
            return user;
        }
    }
    
    public Long saveUser(Usuario newUser){
        Long id = null;
        try {
            PerfilHelper helper = new PerfilHelper();
            Perfil perfil = helper.getProfile(newUser.getPerfil().getNombre());

            Transaction tx;
            if(session.getTransaction().isActive()){
                tx = session.getTransaction();
            }else{
                tx = session.beginTransaction();
            }
            
            if(perfil != null){
                newUser.setPerfil(perfil);    
            }
            if(newUser.getPassword()!=null){
                newUser.setPassword(LoginController.encrypt(newUser.getPassword()));
            }
            
            session.save(newUser);
            if(newUser.getId() != null){
                id = newUser.getId();
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            this.errorMessage = e.getMessage();
        } finally {
            return id;
        }
    }
    
    public boolean updateUser(Usuario uOld){
        boolean ok = false;
        try {
            Transaction tx;
            if(session.getTransaction().isActive()){
                tx = session.getTransaction();
            }else{
                tx = session.beginTransaction();
            }
             
            Usuario uNew = session.load(Usuario.class, uOld.getId());
            
            uNew.setNombres(uOld.getNombres());
            uNew.setApellidoPaterno(uOld.getApellidoPaterno());
            uNew.setApellidoMaterno(uOld.getApellidoMaterno());
            uNew.setCelular(uOld.getCelular());
            uNew.setCorreo(uOld.getCorreo());
            uNew.setDni(uOld.getDni());
            uNew.setIntereses(uOld.getIntereses());
            uNew.setPassword(uOld.getPassword());
            uNew.setPerfil(uOld.getPerfil());
            uNew.setTienda(uOld.getTienda());
            uNew.setTelefono(uOld.getTelefono());
            uNew.setActivo(uOld.isActivo());
            
            session.merge(uNew);
            tx.commit();
            session.close();
            ok = true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            this.errorMessage = e.getMessage();
        }
        return ok;
    }
    
    /*Get user by email*/
    public Usuario getClients(String email){
        Usuario user = null;
        Query query = null;
        try {
            query = session.createQuery("from Usuario where correo='" + email + "'");
            
            if(!query.list().isEmpty()){
                user = (Usuario) query.list().get(0);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            this.errorMessage = e.getMessage();
        } finally {
            return user;
        }
    }
    
    
}
