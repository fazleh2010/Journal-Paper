/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evaluation;

import static grammar.generator.BindingConstants.DEFAULT_BINDING_VARIABLE;
import java.io.File;
import static java.lang.System.exit;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import static java.util.Objects.isNull;
import org.apache.jena.query.Query;
import org.apache.jena.sparql.core.TriplePath;
import org.apache.jena.sparql.syntax.ElementPathBlock;
import org.apache.jena.sparql.syntax.ElementVisitorBase;
import static org.apache.jena.sparql.syntax.ElementWalker.walk;

/**
 *
 * @author elahi
 */
public class Matcher {
    
    public static String getMatchPattern(EntryComparison entryComparison, String cleanQaldQuestion) {
        String pattern=null;
        if (entryComparison.getMatchedFlag()) {
            /*List<Pattern> matchedPatterns
                    = entryComparison.getQueGGEntry().getQuestionList()
                            .stream()
                            .map(this::cleanString)
                            .map(Pattern::compile)
                            .filter(pattern -> !PatternMatchHelper.getPatternMatch(cleanQaldQuestion, pattern)
                            .isEmpty())
                            .collect(Collectors.toList());*/

            /*if (matchedPatterns.size() == 1) {
                return matchedPatterns.get(0);
            }*/           
            for (String question : entryComparison.getQueGGEntry().getQuestionList()) {
                pattern = cleanString(question);
                return pattern;
            }

        }
        return pattern;
    }
    
    public static String getMatchedUri(String sentencePattern, String matchedSentenceItem, Query qaldPARQLQuery) {
        String uriMatch = "";
        if (!isNull(sentencePattern)) {
            String matchedPattern = sentencePattern.toString();
            // Try to find the matching uri for the previously found item inside the QALD SPARQL query
            String lowerCaseMatchedSentenceItem = matchedSentenceItem.toLowerCase().replace(" ", "_");
            uriMatch = findMatchingNodeToQALDSentenceMatchedItem(qaldPARQLQuery, lowerCaseMatchedSentenceItem);
        }
        return uriMatch;
    }
    
    private static String findMatchingNodeToQALDSentenceMatchedItem(Query query, String lowerCasePattern) {
        final String[] match = {""};
        walk(
                query.getQueryPattern(), // ElementGroup
                new ElementVisitorBase() {
            // Go through blocks of triples
            public void visit(ElementPathBlock el) {
                // Go through all triples
                Iterator<TriplePath> triples = el.patternElts();
                while (triples.hasNext()) {
                    TriplePath triplePath = triples.next();
                    // Check for match in subject or object
                    if (triplePath.getSubject().isURI()) {
                        if (triplePath.getSubject().getURI().toLowerCase().contains(lowerCasePattern)) {
                            match[0] = triplePath.getSubject().getURI();
                        }
                    } else if (triplePath.getSubject().isLiteral()) {
                        if (triplePath.getSubject().getLiteral().getValue().toString().toLowerCase().contains(lowerCasePattern)) {
                            match[0] = triplePath.getSubject().getLiteral().getValue().toString();
                        }
                    }
                    if (triplePath.getObject().isURI()) {
                        if (triplePath.getObject().getURI().toLowerCase().contains(lowerCasePattern)) {
                            match[0] = triplePath.getObject().getURI();
                        }
                    } else if (triplePath.getObject().isLiteral()) {
                        if (triplePath.getObject().getLiteral().getValue().toString().toLowerCase().contains(lowerCasePattern)) {
                            match[0] = triplePath.getObject().getLiteral().getValue().toString();
                        }
                    }
                }
            }
        }
        );
        return match[0];
    }
    
    
    public static QALD.QALDQuestions getMatchingOriginalQaldQuestions(QALD qaldOriginal, EntryComparison entryComparison) {
        //System.out.println(qaldOriginal.questions);
        //System.out.println(entryComparison.getQaldEntry());
        
        return qaldOriginal.questions.stream()
                .filter(qaldQuestions -> qaldQuestions.id.equals(entryComparison.getQaldEntry()
                .getId()))
                .findAny()
                .orElseThrow();
    }
    
     public static String cleanString(String sentence) {
        return String.format("^%s$", sentence
                .replace(DEFAULT_BINDING_VARIABLE, "([\\w\\s\\d-,.']+)")
                .replaceAll("\\((.+)\\s\\|\\s(.+)\\)", "([\\\\w\\\\s\\\\d-,.']+)")
                .replace("?", "\\?")
                .toLowerCase()
                .trim());
    }
     
    public static String cleanUrl(String url) throws MalformedURLException {
        if (url.contains("http")) {
            url = new File(new URL(url).getPath()).getName();
        }
        return url;
    }
    
     public static String cleanLine(String string) throws MalformedURLException {
       String result= string.trim().strip().stripLeading().stripTrailing();
       return result;
    }
    public static String cleanPrefix(String string) throws MalformedURLException {
        string=string.replace("http://dbpedia.org/ontology/", "dbo_");
        string=string.replace("http://dbpedia.org/property/", "dbp_");
        String result = string.trim().strip().stripLeading().stripTrailing();
        return result;
    }
    
}
