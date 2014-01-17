/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.edu.icm.coansys.webdemo.data.json;

import org.apache.solr.client.solrj.beans.Field;
import pl.edu.icm.coansys.citations.data.MatchableEntity;

/**
 *
 * @author matfed
 */
public class DocumentMetadata {
    private String id;
    private String doi;
    private String author;
    private String year;
    private String title;
    private String journal;
    private String pages;

    public DocumentMetadata() {}
    
    public DocumentMetadata(String id, String doi, String author, String year, String title, String journal, String pages) {
        this.id = id;
        this.doi = doi;
        this.author = author;
        this.year = year;
        this.title = title;
        this.journal = journal;
        this.pages = pages;
    }

    public String getId() {
        return id;
    }
    
    @Field
    public void setId(String id) {
        this.id = id;
    }

    public String getDoi() {
        return doi;
    }

    @Field
    public void setDoi(String doi) {
        this.doi = doi;
    }

    public String getAuthor() {
        return author;
    }

    @Field
    public void setAuthor(String author) {
        this.author = author;
    }

    public String getYear() {
        return year;
    }

    @Field
    public void setYear(String year) {
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    @Field
    public void setTitle(String title) {
        this.title = title;
    }

    public String getJournal() {
        return journal;
    }

    @Field("source")
    public void setJournal(String journal) {
        this.journal = journal;
    }

    public String getPages() {
        return pages;
    }

    @Field
    public void setPages(String pages) {
        this.pages = pages;
    }
    
    public MatchableEntity toMatchableEntity() {
        return MatchableEntity.fromParameters(id, author, journal, title, pages, year, null);
    }
}
