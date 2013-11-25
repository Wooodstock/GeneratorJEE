/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.CAD;

import com.jee.common.STG;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import org.jboss.logging.Logger;

/**
 *
 * @author Bertrand
 */
public class CAD {
    
    private String chaineConn;
    private String driver;
    private String database;
    private String user;
    private String password;
    private Connection conn;
    
    public CAD(){
        
    }

    public CAD(String driver, String database, String user, String password) {
        this.driver = driver;
        this.database = database;
        this.user = user;
        this.password = password;
    }
    
    public boolean openConnection(){
        
        /* Chargement Driver */
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (InstantiationException ex) {
            System.err.println("Erreur InstantiationException:"+ex.getMessage());
            return false;
        } catch (IllegalAccessException ex) {
            System.err.println("Erreur IllegalAccessException:"+ex.getMessage());
            return false;
        } catch (ClassNotFoundException ex) {
            System.err.println("Erreur ClassNotFoundException:"+ex.getMessage());
            return false;
        }
        
        /* Init connection */
        try {
            this.conn = DriverManager.getConnection("jdbc:mysql:"+this.database+"?zeroDateTimeBehavior=convertToNull", this.user, this.password);
        } catch (SQLException ex) {
            System.err.println("Erreur SQLException Init:"+ex.getMessage());
            return false;
        }
        
        return true;
    }
    
    public STG executeRQuery(STG oSTG){
        try {
            Statement stmnt = this.conn.createStatement();
            
            ResultSet rslt = stmnt.executeQuery((String)oSTG.getData("M_query"));
            oSTG.setData("M_resultset", rslt);
        } catch (SQLException ex) {
            System.err.println("Erreur SQLException:"+ex.getMessage());
        } catch (Exception ex){
            System.err.println("Erreur :"+ex.getMessage());
        }
        return oSTG;
    }
    
    public STG executeCUDQuery(STG oSTG){
        return oSTG;
    }
    
    public void closeConnection(){
        try {
            this.conn.close();
        } catch (SQLException ex) {
            System.err.println("Erreur SQLException: "+ex.getMessage());
        }
    }
}
