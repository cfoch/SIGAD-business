/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.utils.ui;

import javafx.scene.layout.StackPane;

/**
 *
 * @author cfoch
 */
public abstract class UIBaseController {
    public static final String VIEW_PATH = null;
    private StackPane parentStackPane;

    public abstract String getViewPath();

    /**
     * @return the parentStackPane
     */
    public StackPane getParentStackPane() {
        return parentStackPane;
    }

    /**
     * @param parentStackPane the parentStackPane to set
     */
    public void setParentStackPane(StackPane parentStackPane) {
        this.parentStackPane = parentStackPane;
    }
}
