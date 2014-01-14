/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.icm.coansys.webdemo.data.json;

import org.apache.commons.lang.StringUtils;

/**
 *
 * @author matfed
 */
public class ExtractedMetadata {

    private String author;
    private String year;
    private String title;
    private String journal;
    private String pages;

    public ExtractedMetadata() {}
    
    public ExtractedMetadata(String author, String year, String title, String journal, String pages) {
        if (StringUtils.isNotBlank(author))
            this.author = author;
        if (StringUtils.isNotBlank(year))
            this.year = year;
        if (StringUtils.isNotBlank(title))
            this.title = title;
        if (StringUtils.isNotBlank(journal))
            this.journal = journal;
        if (StringUtils.isNotBlank(pages))
            this.pages = pages;
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
