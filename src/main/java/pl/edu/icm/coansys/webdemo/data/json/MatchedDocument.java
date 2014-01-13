/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.edu.icm.coansys.webdemo.data.json;

/**
 *
 * @author matfed
 */
public class MatchedDocument {
    private DocumentMetadata metadata;
    private double score;
    
    public MatchedDocument() {}
    
    public MatchedDocument(double score, DocumentMetadata metadata) {
        this.score = score;
        this.metadata = metadata;
    }
}
