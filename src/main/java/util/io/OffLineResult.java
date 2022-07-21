/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.io;

/**
 *
 * @author elahi
 */
public class OffLineResult {

    private String subjectUri = null;
    private String subjectLabel = null;
    private String objectUri = null;
    private String objectLabel = null;
    
    private String subjectAbstractText;
    private String subjectWikiLink;
    private String subjectThumbnail;
    private String objectAbstractText;
    private String objectWikiLink;
    private String objectThumbnail;

    public OffLineResult(String subjectUri, String subjectLabel, String objectUri, String objectLabel, String subjectWikiLink, String subjectThumbnail, String subjectAbstractText,String objectWikiLink, String objectThumbnail, String objectAbstractText) {
        this.subjectUri = subjectUri;
        this.subjectLabel = subjectLabel;
        this.objectUri = objectUri;
        this.objectLabel = objectLabel;
        this.subjectWikiLink = subjectWikiLink;
        this.subjectThumbnail = subjectThumbnail;
        this.subjectAbstractText = subjectAbstractText;
        this.objectWikiLink = objectWikiLink;
        this.objectThumbnail = objectThumbnail;
        this.objectAbstractText = objectAbstractText;

    }

    public String getSubjectUri() {
        return subjectUri;
    }

    public String getSubjectLabel() {
        return subjectLabel;
    }

    public String getObjectUri() {
        return objectUri;
    }

    public String getObjectLabel() {
        return objectLabel;
    }

    public String getSubjectAbstractText() {
        return subjectAbstractText;
    }

    public String getSubjectWikiLink() {
        return subjectWikiLink;
    }

    public String getSubjectThumbnail() {
        return subjectThumbnail;
    }

    public String getObjectAbstractText() {
        return objectAbstractText;
    }

    public String getObjectWikiLink() {
        return objectWikiLink;
    }

    public String getObjectThumbnail() {
        return objectThumbnail;
    }

   

}
