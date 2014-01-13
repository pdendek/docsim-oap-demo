/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.edu.icm.coansys.webdemo.data.json;

import com.google.gson.Gson;

/**
 *
 * @author matfed
 */
public class MatchingRequest {
    private Citation[] citations;

    public Citation[] getCitations() {
        return citations;
    }

    public void setCitations(Citation[] citations) {
        this.citations = citations;
    }
    
    public static MatchingRequest fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, MatchingRequest.class);
    }
}
