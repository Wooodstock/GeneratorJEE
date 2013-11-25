/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.stg.traitement;

import com.CAD.CAD;
import com.entity.Mot;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;
import com.jee.common.STG;
import com.webservice.dotnet.Server;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.xml.ws.WebServiceRef;

/**
 *
 * @author Bertrand
 */
@MessageDriven(mappedName = "jms/PaymentQueue", activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})
public class ProcessSTG implements MessageListener {
    @WebServiceRef(wsdlLocation = "META-INF/wsdl/localhost_8080/ServiceMetadata.wsdl")
    private Server service;
    
    public ProcessSTG() {
    }
    
    @Override
    public void onMessage(Message message) {
        if(message instanceof ObjectMessage){
            ObjectMessage obj = (ObjectMessage) message;
            try {
                STG oSTG = (STG) obj.getObject();
                oSTG = this.checkFrench(oSTG);
                oSTG.setData("answer", "OK");
                this.callback(oSTG);

                System.out.println("Rapport : "+oSTG);
            } catch (JMSException ex) {
                Logger.getLogger(ProcessSTG.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(ProcessSTG.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
    
    private STG checkFrench(STG oSTG) throws SQLException{
        CAD cad = new CAD((String)oSTG.getData("M_driver"),(String) oSTG.getData("M_database"),(String) oSTG.getData("M_user"),(String) oSTG.getData("M_password"));
        Mot.getAllWord(oSTG);
        if(cad.openConnection()){
            /* On recherche tout les mots français */
            cad.executeRQuery(oSTG);
            ResultSet r = (ResultSet) oSTG.getData("M_resultset");
            List<String> l = new ArrayList<String>();
            while(r.next()){
                l.add(r.getString("mot"));
            }
            cad.closeConnection();
            
            /* On récupère le texte sans les points */
            String decryptedText =(String) oSTG.getData("txt_decrypted");
            decryptedText = decryptedText.replaceAll("\\.", "");
            
            
            /* découpage en mots */
            List<String> words = this.splitTextByReg(decryptedText, " ");
            
            double totalNumber = words.size();
            double wordFound = 0;
            
            /* recherche de correspondance */
            for(String word : words){    
                for(String dicoWord : l){
                    if(word.toLowerCase().equals(dicoWord)){
                        wordFound ++;
                        break;
                    } else if(word.contains("'")){
                        for(String particules: word.split("'")){
                            if(particules.toLowerCase().equals(dicoWord)){
                                wordFound ++;
                                break;
                            }
                        }
                    }
                }
            }
            
            /* calcul ratio */
            
            double ratio = wordFound/totalNumber;
            System.out.println("Le ratio est "+ratio);
            
            
                
            
        }
        return oSTG;
    }
    
    private List<String> splitTextByReg(String decryptedTxt, String reg){
        List<String> l = new ArrayList<String>();
        
        for(String word : decryptedTxt.split(reg)){
            l.add(word);
        }
        return l;
    }

    private void callback(STG oSTG) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        com.webservice.dotnet.IcomposantService port = service.getBasicHttpBindingIcomposantService();
        String reponse = (String) oSTG.getData("answer");
        port.callback(reponse);
    }
    
    
    
    
}
