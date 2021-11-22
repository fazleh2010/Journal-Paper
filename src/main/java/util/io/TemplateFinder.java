/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.io;

import grammar.datasets.sentencetemplates.TempConstants;
import grammar.sparql.SelectVariable;
import grammar.structure.component.DomainOrRangeType;
import grammar.structure.component.FrameType;
import grammar.structure.component.Language;
import java.net.URI;
import lexicon.LexicalEntryUtil;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author elahi
 */
public class TemplateFinder implements TempConstants{

    private LexicalEntryUtil lexicalEntryUtil = null;
    private DomainOrRangeType forwardDomainOrRange = null;
    private String selectedTemplate = null;
    private DomainOrRangeType oppositeDomainOrRange = null;

    public TemplateFinder(LexicalEntryUtil lexicalEntryUtil, FrameType frameType) {
        this.lexicalEntryUtil = lexicalEntryUtil;
        if (frameType.equals(FrameType.IPP)) {
            this.selectedTemplate = this.getSentenceTemplate();
            this.findForwardDomainAndRange();
        }
        else if (frameType.equals(FrameType.NPP)) {
            this.selectedTemplate = this.getSentenceTemplate();
        }
    }

    private void findForwardDomainAndRange() {

        if (selectedTemplate.equals(WHEN_WHAT_PAST_THING)) {
            this.forwardDomainOrRange = DomainOrRangeType.date;
            this.oppositeDomainOrRange = DomainOrRangeType.THING;
        } else if (selectedTemplate.equals(WHEN_WHO_PAST_PERSON)) {
            this.forwardDomainOrRange = DomainOrRangeType.date;
            this.oppositeDomainOrRange = DomainOrRangeType.Person;
        } else if (selectedTemplate.equals(WHERE_WHO_PAST_PERSON)) {
            this.forwardDomainOrRange = DomainOrRangeType.Place;
            this.oppositeDomainOrRange = DomainOrRangeType.Person;
        } else if (selectedTemplate.equals(WHAT_WHICH_DO_THING)) {
            this.forwardDomainOrRange = DomainOrRangeType.THING;
            this.oppositeDomainOrRange = DomainOrRangeType.THING;
        }

    }

    private String getSentenceTemplate() {
        String type = null;
        String subjectUri = this.lexicalEntryUtil.getConditionUriBySelectVariable(SelectVariable.subjOfProp).toString();
        String objectUri = this.lexicalEntryUtil.getConditionUriBySelectVariable(SelectVariable.objOfProp).toString();
        String referenceUri = lexicalEntryUtil.getReferenceUri();
        //SubjectType subjectType = this.lexicalEntryUtil.getSubjectType(this.lexicalEntryUtil.getSelectVariable());

        /*String qWord = null;
        try {
            qWord = this.lexicalEntryUtil.getSubjectBySubjectType(subjectType, language, null);
        } catch (QueGGMissingFactoryClassException ex) {
            Logger.getLogger(SentenceBuilderIntransitivePPEN.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        //System.out.println("subjectUri::"+subjectUri);
        //System.out.println("objectUri::"+objectUri);
        //System.out.println("referenceUri::"+referenceUri);
        //System.out.println("isPerson(subjectUri)::"+isPerson(subjectUri));
        //System.out.println("isDate(referenceUri)::"+isDate(referenceUri));
        //System.out.println("isPlace(referenceUri)::"+isPlace(objectUri));
        if (isPerson(subjectUri) && isPlace(objectUri)) {
            type = WHERE_WHO_PAST_PERSON;
        } else if (isPerson(subjectUri) && isDate(referenceUri)) {
            type = WHEN_WHO_PAST_PERSON;
        } else if (!isPerson(subjectUri) && isDate(referenceUri)) {
            type = WHEN_WHAT_PAST_THING;
        } else {
            type = WHAT_WHICH_DO_THING;
        }
        //System.out.println("type::"+type);
        return type;

    }

    public static Boolean isPerson(String string) {
        if (StringUtils.isBlank(string)) {
            return false;
        }
        for (URI key : DomainOrRangeType.Person.getReferences()) {

            if (string.equals(key.toString())) {
                return true;
            }
        }
        return false;

    }
    

     public static Boolean isArchitecture(String string) {
        if (StringUtils.isBlank(string)) {
            return false;
        }
        for (URI key : DomainOrRangeType.ArchitecturalStructure.getReferences()) {
            if (string.equals(key.toString())) {
                return true;
            }
        }
        return false;

    }

     public static Boolean isWork(String string) {
        if (StringUtils.isBlank(string)) {
            return false;
        }
        for (URI key : DomainOrRangeType.Work.getReferences()) {
            if (string.equals(key.toString())) {
                return true;
            }
        }
        return false;

    }

    public static Boolean isPlace(String string) {
        if (StringUtils.isBlank(string)) {
            return false;
        }
        for (URI key : DomainOrRangeType.Place.getReferences()) {
            if (string.equals(key.toString())) {
                return true;
            }
        }
        return false;
    }

    public static Boolean isDate(String string) {
        if (StringUtils.isBlank(string)) {
            return false;
        }
        for (URI key : DomainOrRangeType.date.getReferences()) {
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

    public String getSelectedTemplate() {
        return selectedTemplate;
    }

    public String getDeterminer(Language language, DomainOrRangeType domainOrRangeType) {
        String determinerStr = "";

        if (language.toString().contains(language.EN.toString())) {
            if (domainOrRangeType.equals(DomainOrRangeType.date) && this.selectedTemplate.contains(WHEN_WHAT_PAST_THING)) {
                determinerStr = "the";
            }

        }
        return determinerStr;
    }

    private String getSentenceTemplateTransitive() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
