/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.io;

import grammar.sparql.SelectVariable;
import grammar.structure.component.DomainOrRangeType;
import grammar.structure.component.Language;
import java.net.URI;
import lexicon.LexicalEntryUtil;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author elahi
 */
public class TemplateFinder implements TemplateConstants{

    private LexicalEntryUtil lexicalEntryUtil = null;
    private DomainOrRangeType forwardDomainOrRange = null;
    private String SELECTED_TEMPLATE = null;
    private DomainOrRangeType oppositeDomainOrRange = null;

    public TemplateFinder(LexicalEntryUtil lexicalEntryUtil) {
        this.lexicalEntryUtil = lexicalEntryUtil;
        this.SELECTED_TEMPLATE = this.getSentenceTemplate();
        this.findForwardDomainAndRange();
    }

    private void findForwardDomainAndRange() {

        if (SELECTED_TEMPLATE.equals(WHEN_WHAT_PAST_THING)) {
            this.forwardDomainOrRange = DomainOrRangeType.DATE;
            this.oppositeDomainOrRange = DomainOrRangeType.THING;
        } else if (SELECTED_TEMPLATE.equals(WHEN_WHO_PAST_PERSON)) {
            this.forwardDomainOrRange = DomainOrRangeType.DATE;
            this.oppositeDomainOrRange = DomainOrRangeType.PERSON;
        } else if (SELECTED_TEMPLATE.equals(WHERE_WHO_PAST_PERSON)) {
            this.forwardDomainOrRange = DomainOrRangeType.PLACE;
            this.oppositeDomainOrRange = DomainOrRangeType.PERSON;
        } else if (SELECTED_TEMPLATE.equals(WHAT_WHICH_DO_THING)) {
            this.forwardDomainOrRange = DomainOrRangeType.THING;
            this.oppositeDomainOrRange = DomainOrRangeType.THING;
        }

    }

    private String getSentenceTemplate() {
        String type = null;
        String subjectUri = this.lexicalEntryUtil.getConditionUriBySelectVariable(SelectVariable.SUBJECT_OF_PROPERTY).toString();
        String objectUri = this.lexicalEntryUtil.getConditionUriBySelectVariable(SelectVariable.OBJECT_OF_PROPERTY).toString();
        String referenceUri = lexicalEntryUtil.getReferenceUri();
        //SubjectType subjectType = this.lexicalEntryUtil.getSubjectType(this.lexicalEntryUtil.getSelectVariable());

        /*String qWord = null;
        try {
            qWord = this.lexicalEntryUtil.getSubjectBySubjectType(subjectType, language, null);
        } catch (QueGGMissingFactoryClassException ex) {
            Logger.getLogger(SentenceBuilderIntransitivePPEN.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        if (isPerson(subjectUri) && isPlace(referenceUri)) {
            type = WHERE_WHO_PAST_PERSON;
        } else if (isPerson(subjectUri) && isDate(referenceUri)) {
            type = WHEN_WHO_PAST_PERSON;
        } else if (!isPerson(subjectUri) && isDate(referenceUri)) {
            type = WHEN_WHAT_PAST_THING;
        } else {
            type = WHAT_WHICH_DO_THING;
        }
        return type;

    }

    private static Boolean isPerson(String string) {
        if (StringUtils.isBlank(string)) {
            return false;
        }
        for (URI key : DomainOrRangeType.PERSON.getReferences()) {

            if (string.equals(key.toString())) {
                return true;
            }
        }
        return false;

    }

    private static Boolean isArchitecture(String string) {
        if (StringUtils.isBlank(string)) {
            return false;
        }
        for (URI key : DomainOrRangeType.ARCHITECTURE_STRUCTURE.getReferences()) {
            if (string.equals(key.toString())) {
                return true;
            }
        }
        return false;

    }

    private static Boolean isWork(String string) {
        if (StringUtils.isBlank(string)) {
            return false;
        }
        for (URI key : DomainOrRangeType.WORK.getReferences()) {
            if (string.equals(key.toString())) {
                return true;
            }
        }
        return false;

    }

    private Boolean isPlace(String string) {
        if (StringUtils.isBlank(string)) {
            return false;
        }
        for (URI key : DomainOrRangeType.PLACE.getReferences()) {
            if (string.equals(key.toString())) {
                return true;
            }
        }
        return false;
    }

    private Boolean isDate(String string) {
        if (StringUtils.isBlank(string)) {
            return false;
        }
        for (URI key : DomainOrRangeType.DATE.getReferences()) {
            if (string.equals(key.toString())) {
                return true;
            }
        }
        return false;
    }

    public DomainOrRangeType getForwardDomainOrRange() {
        return forwardDomainOrRange;
    }

    public DomainOrRangeType getOppositeDomainOrRange() {
        return oppositeDomainOrRange;
    }

    public String getSELECTED_TEMPLATE() {
        return SELECTED_TEMPLATE;
    }

    public String getDeterminer(Language language, DomainOrRangeType domainOrRangeType) {
        String determinerStr = "";

        if (language.toString().contains(language.EN.toString())) {
            if (domainOrRangeType.equals(DomainOrRangeType.DATE) && this.SELECTED_TEMPLATE.contains(WHEN_WHAT_PAST_THING)) {
                determinerStr = "the";
            }

        }
        return determinerStr;
    }

}
