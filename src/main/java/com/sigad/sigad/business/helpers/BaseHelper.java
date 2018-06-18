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
 * @author jorgeespinoza
 */
public class BaseHelper {
    static Session session = null;
    protected String errorMessage = "";

    public BaseHelper() {
        if(this.session!= null) this.close();
        session = LoginController.serviceInit();
    }

    /*Close session*/
    public void close() {
        session.close();
    }
    
    /**
     * @return the errorMessage
     */
    public String getErrorMessage() {
        return errorMessage;
    }
    
}
