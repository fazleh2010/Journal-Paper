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
        this.setParameter(attribute, reference);

    }

    private ParamterFinder(String template) {
        String[] tokens = StringMatcher.parseToken(template);
        String attribute = "", reference = "";
        if (tokens.length == 2) {
            attribute = tokens[0];
            reference = tokens[1];
            this.setParameter(attribute, reference);

        } else if (tokens.length == 1) {
            attribute = tokens[0];
        }
    }

    private void setParameter(String attribute, String reference) {
        if (reference.contains(colon)) {
            String[] col = reference.split(colon);
            this.parameterLength = col.length;

            if (col.length == 3) {
                setParameter(col);
            } else if (col.length == 2) {
                this.setParameter(attribute, col);
            }
        }
    }

    private void setParameter(String[] col) {
        Integer index = 0;
        for (String column : col) {
            if (index == 0) {
                this.reference = column;
            } else if (NUMBERS.contains(column)) {
                numberPair = new Pair<String, String>(number, column);
            } else if (TENSES.contains(column)) {
                tensePair = new Pair<String, String>(tense, column);
            } else if (PERSONS.contains(column)) {
                personPair = new Pair<String, String>(person, column);
            }
            index = index + 1;
        }
    }

    private void setParameter(String attribute, String[] col) {
        this.reference = attribute;
        for (String column : col) {
            if (NUMBERS.contains(column)) {
                numberPair = new Pair<String, String>(number, column);
            } else if (TENSES.contains(column)) {
                tensePair = new Pair<String, String>(tense, column);
            } else if (PERSONS.contains(column)) {
                personPair = new Pair<String, String>(person, column);
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

    public Integer getParameterLength() {
        return parameterLength;
    }

    @Override
    public String toString() {
        return "ParamterFinder{" + "numberPair=" + numberPair + ", tensePair=" + tensePair + ", personPair=" + personPair + ", reference=" + reference + ", parameterLength=" + parameterLength + '}';
    }

    public static void main(String[] args) {
        String template = "verb(mainVerb:past:thirdPerson)";

        ParamterFinder ParamterFinder = new ParamterFinder(template);
        System.out.println(ParamterFinder);
    }

}
