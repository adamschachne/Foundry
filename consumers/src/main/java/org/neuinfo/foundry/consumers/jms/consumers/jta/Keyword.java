package org.neuinfo.foundry.consumers.jms.consumers.jta;

public class Keyword {
    private String term;
    private String[] span;
    private String ontID;
    private String facet;
    private String fullHierarchy;

    public Keyword(String term, String[] span, String ontID, String facet, String fullHierarchy) {
        setFacet(facet);
        setOntID(ontID);
        setSpan(span);
        setTerm(term);
        this.fullHierarchy = fullHierarchy;
    }


    public String getFacet() {
        return facet;
    }

    public void setFacet(String facet) {
        this.facet = facet;
    }

    public String getOntID() {
        return ontID;
    }

    public void setOntID(String ontID) {
        this.ontID = ontID;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String[] getSpan() {
        return span;
    }

    public void setSpan(String[] span) {
        this.span = span;
    }

    public String getFullHierarchy() {
        return fullHierarchy;
    }

    @Override
    public String toString() {
        return "Keyword [facet = " + facet + ", ontID = " + ontID + ", term = " + term + ", span = " + span + "]";
    }
}