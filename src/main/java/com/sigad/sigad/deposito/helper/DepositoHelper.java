package com.sigad.sigad.deposito.helper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import com.sigad.sigad.business.OrdenCompra;
import com.sigad.sigad.business.Proveedor;
import com.sigad.sigad.business.Usuario;
import com.sigad.sigad.business.Perfil;
import java.util.Date;
import com.sigad.sigad.app.controller.LoginController;
/**
 *
 * @author chrs
 */
public class DepositoHelper {
    public static Configuration config;
    public static SessionFactory sessionFactory;
    public static Session session;

    public DepositoHelper() {
        
        session = LoginController.serviceInit();

//        session.beginTransaction();
//        Proveedor prov = new Proveedor();
//        prov.setDescripcion("Distribuidor de rosas");
//        prov.setNombre("Rosas SA");
//        prov.setRuc(123489021);
//        session.save(prov);

////
//        Perfil perf = new Perfil();
//        perf.setActivo(true);
//        perf.setNombre("Admin");
//        perf.setDescripcion("Rol de adminstrador");
//        session.save(perf);
//
//        //session.getTransaction().commit();
//
//        String hql = String.format("from Perfil");
//        Query queryHql = this.session.createQuery(hql);
//        List<Object []> perfiles = queryHql.list();
//        for (Object entidad : perfiles) {
//            Perfil po = (Perfil)entidad;
////            OrdenCompra oc = (OrdenCompra) entidad[0];
////            Proveedor prov = (Proveedor) entidad[1];
//            System.out.println("id " + po.getId());
//            System.out.println("nombre " + po.getNombre());
//        }


        //session.save(perf);
//
//        Perfil p = new Perfil("Cliente", true);
//
//        Usuario us = new Usuario();
//        us.setActivo(true);
//        us.setNombres("Juan");
//        us.setApellidoPaterno("Perez");
//        us.setCelular("999666777");
//        us.setCorreo("juan@pucp.pe");
//        us.setDni("87965436");
//        us.setIntereses("Flores");
//        us.setTelefono("874987987");
//        us.setPerfil(p);
//        session.save(p);
//        session.save(us);

        String hql = String.format("from Usuario");
        Query queryHql = this.session.createQuery(hql);
        List<Object> usuarios = queryHql.list();
        Usuario us=null;
        for (Object entidad : usuarios) {
            us = (Usuario)entidad;
//            OrdenCompra oc = (OrdenCompra) entidad[0];
//            Proveedor prov = (Proveedor) entidad[1];
            System.out.println("id " + us.getId());
            System.out.println("nombre " + us.getNombres());
        }
        
        String hql2 = String.format("from Proveedor");
        Query queryHql2 = this.session.createQuery(hql2);
        List<Object > proveedores = queryHql2.list();
        Proveedor pr=null;
        for (Object entidad2 : proveedores) {
            pr = (Proveedor)entidad2;
//            OrdenCompra oc = (OrdenCompra) entidad[0];
//            Proveedor prov = (Proveedor) entidad[1];
            System.out.println("id prov" + pr.getId());
            System.out.println("nombre prov " + pr.getNombre());
        }
//        Usuario u;
//        u =  (Usuario) session.get(Usuario.class, new Long(25));
//        System.out.println("Perfil papu:" + u.getPerfil().getNombre());
//        try {
//            u =  (Usuario) session.get(Usuario.class, new Long(25));
//            System.out.println("Perfil papu:" + u.getPerfil().getNombre());
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//        Proveedor p =  (Proveedor) session.get(Proveedor.class, new Long(32));
//        System.out.println("Perfil papu:" + p.getNombre());
//        try {
//            p =  (Proveedor) session.get(Proveedor.class, new Long(32));
//            System.out.println("Perfil papu:" + p.getNombre());
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }

        
//

//        OrdenCompra ord = new OrdenCompra();
//        //ord.setDetalleOrdenCompra(detallesOrden);
//        ord.setFecha(new Date());
//        ord.setPrecioTotal(30.5);
//        ord.setProveedor(pr);
//        ord.setUsuario(us);
//        session.save(ord);
//        
//
        
        //getOrdenes(u.getId());
//        session.getTransaction().commit();
//        close();
    }

//    public ArrayList<Producto> getProducts(){
//        Query query  = session.createQuery("from Producto");
//        return new ArrayList<>( query.list());
//    };

    public OrdenCompra getOrden(Integer id){
        session.beginTransaction();
        OrdenCompra orden =  (OrdenCompra)session.get(OrdenCompra.class, id);
        session.getTransaction().commit();
        return orden;
    }
    public List<OrdenCompra> getOrdenes(){
        ArrayList<OrdenCompra> ordenes = null;
        Query query = null;
        try {
            query = session.createQuery("from OrdenCompra");
            if(!query.list().isEmpty()){
                ordenes = (ArrayList<OrdenCompra>)query.list();
            }
        } catch (Exception e) {
            
        }
        return ordenes;
    }
    public void close() {
        session.getTransaction().commit();
        sessionFactory.close();
    }
}
