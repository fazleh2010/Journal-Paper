/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.io;

import com.google.gdata.util.common.base.Pair;
import grammar.datasets.sentencetemplates.TempConstants;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author elahi
 */
public class ParamterFinder implements TempConstants {

  

    private Pair<String, String> numberPair = new Pair<String, String>("", "");
    private Pair<String, String> tensePair = new Pair<String, String>("", "");
    private Pair<String, String> personPair = new Pair<String, String>("", "");
    private String reference = null;
    private Integer parameterLength = 0;

    public ParamterFinder(String attribute, String reference) {
        if (reference.contains(colon)) {
            String[] col = reference.split(colon);
            this.parameterLength = col.length;

            if (col.length == 2) {
                this.setParameter(attribute, col);
            } else if (col.length == 3) {
                setParameter(col);
            }

        }

    }

    public String getReference() {
        return reference;
    }

    public Pair<String, String> getNumberPair() {
        return numberPair;
    }

    public Pair<String, String> getTensePair() {
        return tensePair;
    }

    public Pair<String, String> getPersonPair() {
        return personPair;
    }

    @Override
    public String toString() {
        return "ParamterFinder{" + "numberPair=" + numberPair + ", tensePair=" + tensePair + ", personPair=" + personPair + ", reference=" + reference + '}';
    }

    private void setParameter(String[] col) {
        Integer index=0;
        for (String column : col) {
            if (index == 0) {
                this.reference = column;
            } else if (numbers.contains(column)) {
                numberPair = new Pair<String, String>(number, column);
            } else if (tenses.contains(column)) {
                tensePair = new Pair<String, String>(tense, column);
            } else if (persons.contains(column)) {
                personPair = new Pair<String, String>(person, column);
            }
            index = index + 1;
        }
    }

    private void setParameter(String attribute, String[] col) {
        this.reference = attribute;
        for (String column : col) {
            if (numbers.contains(column)) {
                numberPair = new Pair<String, String>(number, column);
            } else if (tenses.contains(column)) {
                tensePair = new Pair<String, String>(tense, column);
            } else if (persons.contains(column)) {
                personPair = new Pair<String, String>(person, column);
            }

        }
    }

    public Integer getParameterLength() {
        return parameterLength;
    }

}
