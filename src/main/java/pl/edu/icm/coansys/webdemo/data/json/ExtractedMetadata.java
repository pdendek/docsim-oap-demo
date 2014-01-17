/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.icm.coansys.webdemo.data.json;

import org.apache.commons.lang.StringUtils;
import pl.edu.icm.coansys.citations.data.MatchableEntity;

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
        this.author = author;
        this.year = year;
        this.title = title;
        this.journal = journal;
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
    
    public static ExtractedMetadata fromMatchableEntity(MatchableEntity entity) {
        ExtractedMetadata result = new ExtractedMetadata();
        if (StringUtils.isNotBlank(entity.author()))
            result.author = entity.author();
        if (StringUtils.isNotBlank(entity.year()))
            result.year = entity.year();
        if (StringUtils.isNotBlank(entity.title()))
            result.title = entity.title();
        if (StringUtils.isNotBlank(entity.source()))
            result.journal = entity.source();
        if (StringUtils.isNotBlank(entity.pages()))
            result.pages = entity.pages();
        return result;
    }
}
