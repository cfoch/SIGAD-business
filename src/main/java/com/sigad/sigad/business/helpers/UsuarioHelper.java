package com.sigad.sigad.business.helpers;

import com.sigad.sigad.app.controller.LoginController;
import com.sigad.sigad.business.Perfil;
import com.sigad.sigad.business.Tienda;
import com.sigad.sigad.business.Usuario;
import java.util.ArrayList;
import java.util.HashSet;
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
public class UsuarioHelper extends BaseHelper{
    public UsuarioHelper() {
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
            if (uOld.getPerfil().getNombre().equals("Cliente")){
                uNew.setClienteDirecciones(new HashSet<>(uOld.getClienteDirecciones()));
            }
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
    
    /*Get users by profile profile*/
    public ArrayList<Usuario> getUsers(Perfil perfil){
        ArrayList<Usuario> users = null;
        Query query = null;
        try {
            query = session.createQuery("from Usuario where perfil_id='" + perfil.getId() + "'");
            
            if(!query.list().isEmpty()){
                users = (ArrayList<Usuario>) query.list();
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            session.getTransaction().rollback();
            this.errorMessage = e.getMessage();
        } finally {
            return users;
        }
    }
    
    public ArrayList<Usuario> getUsersStore(Tienda tienda){
        ArrayList<Usuario> users = null;
        Query query = null;
        try {
            query = session.createQuery("from Usuario where tienda_id='" + tienda.getId() + "'");
            
            if(!query.list().isEmpty()){
                users = (ArrayList<Usuario>) query.list();
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            session.getTransaction().rollback();
            this.errorMessage = e.getMessage();
        } finally {
            return users;
        }
    }
    
    
}
