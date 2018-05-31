/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.business.helpers;

import com.sigad.sigad.app.controller.LoginController;
import org.hibernate.Session;

/**
 *
 * @author cfoch
 */
public class BaseHelper {
    protected Session session;
    public BaseHelper() {
        session = LoginController.serviceInit();
    }

    public void close(){
        session.close();
    }
}
