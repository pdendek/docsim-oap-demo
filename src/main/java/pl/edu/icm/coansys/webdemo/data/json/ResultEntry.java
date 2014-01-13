/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.edu.icm.coansys.webdemo.data.json;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 *
 * @author matfed
 */
public class ResultEntry {
    @SerializedName("citation-text")
    private String citationText;
    
    @SerializedName("extracted-metadata")
    private ExtractedMetadata extractedMetadata;
    
    @SerializedName("matched-documents")
    private List<MatchedDocument> matchedDocuments;

    public ResultEntry() {}
    
    public ResultEntry(String citationText, ExtractedMetadata extractedMetadata, List<MatchedDocument> matchedDocuments) {
        this.citationText = citationText;
        this.extractedMetadata = extractedMetadata;
        this.matchedDocuments = matchedDocuments;
    }

    public String getCitationText() {
        return citationText;
    }

    public void setCitationText(String citationText) {
        this.citationText = citationText;
    }

    public ExtractedMetadata getExtractedMetadata() {
        return extractedMetadata;
    }

    public void setExtractedMetadata(ExtractedMetadata extractedMetadata) {
        this.extractedMetadata = extractedMetadata;
    }

    public List<MatchedDocument> getMatchedDocuments() {
        return matchedDocuments;
    }

    public void setMatchedDocuments(List<MatchedDocument> matchedDocuments) {
        this.matchedDocuments = matchedDocuments;
    }
    
    
}
