/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jee.common;

import java.io.Serializable;
import java.util.Map;

/**
 *
 * @author Bertrand
 */
public class STG implements Serializable{
    
    private boolean statut_op;
    private String info;
    private Map data;
    private String operationName;
    private String tokenApp;
    private String tokenUser;

    
    public STG() {
    }
    
    public STG(boolean statut_op, String info, Map data, String operationName, String tokenApp, String tokenUser) {
        this.statut_op = statut_op;
        this.info = info;
        this.data = data;
        this.operationName = operationName;
        this.tokenApp = tokenApp;
        this.tokenUser = tokenUser;
    }

    public boolean isStatut_op() {
        return statut_op;
    }

    public void setStatut_op(boolean statut_op) {
        this.statut_op = statut_op;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Map getData() {
        return data;
    }
    
    public Object getData(String key){
        if(this.data.containsKey(key)){
            return this.data.get(key);
        } else {
            return null;
        }
    }

    public void setData(Map data) {
        this.data = data;
    }
    
    public void setData(String key, Object value){
        if(this.data.containsKey(key)){
            this.data.remove(key);
            this.data.put(key, value);
        } else {
            this.data.put(key, value);
        }
        
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public String getTokenApp() {
        return tokenApp;
    }

    public void setTokenApp(String tokenApp) {
        this.tokenApp = tokenApp;
    }

    public String getTokenUser() {
        return tokenUser;
    }

    public void setTokenUser(String tokenUser) {
        this.tokenUser = tokenUser;
    }
    
    

    
    
    
}
