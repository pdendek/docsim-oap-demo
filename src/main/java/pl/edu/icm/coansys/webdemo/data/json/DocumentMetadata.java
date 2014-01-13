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
public class DocumentMetadata {
    private String id;
    private String url;
    private String author;
    private String year;
    private String title;
    private String journal;
    private String pages;

    public DocumentMetadata() {}
    
    public DocumentMetadata(String id, String url, String author, String year, String title, String journal, String pages) {
        this.id = id;
        this.url = url;
        this.author = author;
        this.year = year;
        this.title = title;
        this.journal = journal;
        this.pages = pages;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getJournal() {
        return journal;
    }

    public void setJournal(String journal) {
        this.journal = journal;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }
    
}
