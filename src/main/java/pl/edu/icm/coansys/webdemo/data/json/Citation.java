/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.edu.icm.coansys.webdemo.data.json;

import com.google.gson.annotations.SerializedName;

/**
 *
 * @author matfed
 */
public class Citation {
    @SerializedName("citation-text")
    private String citationText;

    public String getCitationText() {
        return citationText;
    }

    public void setCitationText(String citationText) {
        this.citationText = citationText;
    }
}
