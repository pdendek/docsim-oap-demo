/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.edu.icm.coansys.webdemo.data.json;

import java.util.List;

/**
 *
 * @author matfed
 */
public class MatchingResult {
    private List<ResultEntry> results;

    public MatchingResult() {}
    
    public MatchingResult(List<ResultEntry> results) {
        this.results = results;
    }

    public List<ResultEntry> getResults() {
        return results;
    }

    public void setResults(List<ResultEntry> results) {
        this.results = results;
    }
}
