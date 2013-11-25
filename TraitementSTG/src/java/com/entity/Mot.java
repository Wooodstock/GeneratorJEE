/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.entity;

import com.jee.common.STG;

/**
 *
 * @author Bertrand
 */
public class Mot {
    
    private String word;

    public Mot(String word) {
        this.word = word;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
    
    public static STG getAllWord(STG oSTG){
        String query = "SELECT * FROM tb_mot;";
        
        oSTG.setData("M_query", query);
        return oSTG;
    }
}
