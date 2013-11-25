/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.stg.reception;

import com.jee.common.STG;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.jms.Session;
import org.jboss.logging.Logger;

/**
 *
 * @author Bertrand
 */
@WebService(serviceName = "ReceptionSTG")
@Stateless()
public class ReceptionSTG {
    
    @Resource(mappedName="jms/queueCF")
    private QueueConnectionFactory factory;
    
    @Resource(mappedName="jms/PaymentQueue")
    private Queue queue;
    
    private Connection cnx;
    
    @PostConstruct
    protected void init(){
        try{
            cnx = factory.createConnection();
        }catch(JMSException ex){
            Logger.getLogger(ReceptionSTG.class.getName()).log(Logger.Level.FATAL, ex);
            throw new EJBException();
        }
    }
    
    @PreDestroy
    protected void clear(){
        try{
            cnx.close();
        }catch(JMSException ex){
            Logger.getLogger(ReceptionSTG.class.getName()).log(Logger.Level.FATAL, ex);
        }
    }

    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "sendToQueue")
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void sendToQueue(@WebParam(name = "message") String txtDecrypted, @WebParam(name = "fileName") String fileName,@WebParam(name = "key") String key) {
        
        try{
            Session session = cnx.createSession(true, 0);
            MessageProducer producer = session.createProducer(queue);
            
            /*  TEST To Remove */
            Map map = new HashMap<>();
            map.put("M_database", "//localhost:3306/dicobdd");
            map.put("M_driver", "com.mysql.jdbc.Driver");
            map.put("M_user", "root");
            map.put("M_password", "admin");
            map.put("txt_decrypted", txtDecrypted);
            map.put("file_name", fileName);
            map.put("key", key);
            STG oSTG = new STG(true, "", map, "dechiffrer", "Client.NET", "TokenUser");
            
            
            
            ObjectMessage obj = session.createObjectMessage(oSTG);
            System.out.println("Object Reçu");
            producer.send(obj);
        }catch(JMSException ex){
            Logger.getLogger(ReceptionSTG.class.getName()).log(Logger.Level.FATAL, ex);
            throw new EJBException();
        }
    }
}
