/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grammar.generator.sentencebuilder;

import grammar.datasets.sentencetemplates.TempConstants;
import grammar.generator.SubjectType;
import grammar.sparql.SelectVariable;
import grammar.structure.component.DomainOrRangeType;
import grammar.structure.component.DomainOrRangeTypeCheck;
import grammar.structure.component.FrameType;
import grammar.structure.component.Language;
import static java.lang.System.exit;
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
    private String propertyReference = null;
    private DomainOrRangeType oppositeDomainOrRange = null;


    public TemplateFinder(LexicalEntryUtil lexicalEntryUtil, FrameType frameType) {
       this.lexicalEntryUtil=lexicalEntryUtil;
        if (frameType.equals(FrameType.IPP)) {
            this.selectedTemplate = this.findINtransitiveTemplate();
            this.findForwardDomainAndRange();
        }
        else if (frameType.equals(FrameType.VP)) {
            this.selectedTemplate = this.findTransitiveTemplates();
            this.findForwardDomainAndRange();
        }
        else if (frameType.equals(FrameType.NPP)) {
            this.selectedTemplate = this.findINtransitiveTemplate();
        }
        else if (frameType.equals(FrameType.AG)) {
          
                this.selectedTemplate = this.findGradableTemplate();
                this.propertyReference = this.findReference();
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
        } else if (selectedTemplate.equals(WHAT_WHICH_PRESENT_THING_2)) {
            this.forwardDomainOrRange = DomainOrRangeType.THING;
            this.oppositeDomainOrRange = DomainOrRangeType.THING;
        }

    }

    private String findINtransitiveTemplate() {
        String type = null;
        String subjectUri = this.lexicalEntryUtil.getConditionUriBySelectVariable(SelectVariable.subjOfProp).toString();
        String objectUri = this.lexicalEntryUtil.getConditionUriBySelectVariable(SelectVariable.objOfProp).toString();
        String referenceUri = lexicalEntryUtil.getReferenceUri();

        /*String qWord = null;
        try {
            qWord = this.lexicalEntryUtil.getSubjectBySubjectType(subjectType, language, null);
        } catch (QueGGMissingFactoryClassException ex) {
            Logger.getLogger(SentenceBuilderIntransitivePPEN.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        
        if (isPerson(subjectUri) && isPlace(objectUri)) {
            type = WHERE_WHO_PAST_PERSON;
        } else if ((isPlace(subjectUri)||isPlace(objectUri)) && isPlace(referenceUri)) {
            type = WHERE_WHAT_PRESENT_THING;
        } else if (isPerson(subjectUri) && isDate(referenceUri)) {
            type = WHEN_WHO_PAST_PERSON;
        }  else if (isPerson(subjectUri) && isPerson(objectUri)) {
            type = WHO_WHO_PERSON;
        } else if (isPerson(subjectUri) && isCause(referenceUri)) {
            type = PERSON_CAUSE;
        }else if (isPerson(objectUri) && isCause(referenceUri)) {
            type = PERSON_CAUSE;
        } else if (isPerson(objectUri) && isActivity(referenceUri)) {
            type = PERSON_ACTIVITY;
        } else if (!isPerson(subjectUri) && isDate(referenceUri)) {
            type = WHEN_WHAT_PAST_THING;
        } 
        else if (isAmountPriceCheck(referenceUri)) {
            type = HOW_MANY_PRICE;
        } else if (isAmountThingCheck(referenceUri)) {
            type = HOW_MANY_THING;
        }else if(isGrowing(referenceUri)){
            type = WHAT_WHICH_PRESENT_THING_2;
        }else{
           type = WHAT_WHICH_PRESENT_THING_1; 
        }
        /*System.out.println("subjectUri::" + subjectUri);
        System.out.println("objectUri::" + objectUri);
        System.out.println("referenceUri::" + referenceUri);
        System.out.println("isPerson(subjectUri)::" + isPerson(subjectUri));
        System.out.println("isPerson(objectUri)::" + isPerson(objectUri));
        System.out.println("isCause(referenceUri)::" + isCause(referenceUri));
        System.out.println("isDate(referenceUri)::" + isDate(referenceUri));
        System.out.println("isPlace(referenceUri)::" + isPlace(objectUri));
          System.out.println("isActivity(referenceUri)::" + isActivity(referenceUri));
        System.out.println("type::" + type);
        exit(1);*/
        return type;

    }
    
    private String findTransitiveTemplates() {
        String type = null;
        String subjectUri = this.lexicalEntryUtil.getConditionUriBySelectVariable(SelectVariable.subjOfProp).toString();
        String objectUri = this.lexicalEntryUtil.getConditionUriBySelectVariable(SelectVariable.objOfProp).toString();
        String referenceUri = lexicalEntryUtil.getReferenceUri();

        
        if (isAmountPriceCheck(referenceUri)) {
            type = HOW_MANY_PRICE;
        } else if (isAmountThingCheck(referenceUri)) {
            type = HOW_MANY_THING;
        }else if(isPerson(subjectUri) && isPerson(objectUri)) {
            type = PERSON_PERSON;
        }else  {
            type=PERSON_CAUSE;
        }
        /*System.out.println("subjectUri::"+subjectUri);
        System.out.println("objectUri::"+objectUri);
        System.out.println("referenceUri::"+referenceUri);
        System.out.println("isPerson(subjectUri)::"+isPerson(subjectUri));
         System.out.println("isPerson(objectUri)::"+isPerson(objectUri));
         System.out.println("isCause(referenceUri)::"+isCause(referenceUri));
        System.out.println("isDate(referenceUri)::"+isDate(referenceUri));
        System.out.println("isPlace(referenceUri)::"+isPlace(objectUri));
          System.out.println("type::"+type);
         exit(1);*/
       
        return type;

    }

    public static Boolean isPerson(String string) {
        if (StringUtils.isBlank(string)) {
            return false;
        }
        for (URI key : DomainOrRangeTypeCheck.PersonCheck.getReferences()) {

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
        for (URI key : DomainOrRangeTypeCheck.ArchitecturalStructureCheck.getReferences()) {
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
        for (URI key : DomainOrRangeTypeCheck.WorkCheck.getReferences()) {
            if (string.equals(key.toString())) {
                return true;
            }
        }
        return false;

    }
     
    public static Boolean isAmountPriceCheck(String string) {
        if (StringUtils.isBlank(string)) {
            return false;
        }
        for (URI key : DomainOrRangeTypeCheck.AmountPriceCheck.getReferences()) {
            if (string.equals(key.toString())) {
                return true;
            }
        }
        return false;

    }
    
     public static Boolean isAmountThingCheck(String string) {
        if (StringUtils.isBlank(string)) {
            return false;
        }
        for (URI key : DomainOrRangeTypeCheck.AmountThingCheck.getReferences()) {
            if (string.equals(key.toString())) {
                return true;
            }
        }
        return false;

    }
     
    public static Boolean isLocationCheck(String string) {
        if (StringUtils.isBlank(string)) {
            return false;
        }
        for (URI key : DomainOrRangeTypeCheck.AmountThingCheck.getReferences()) {
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
        for (URI key : DomainOrRangeTypeCheck.PlaceCheck.getReferences()) {
            if (string.equals(key.toString())) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isLocation(String string) {
        if (StringUtils.isBlank(string)) {
            return false;
        }
        for (URI key : DomainOrRangeTypeCheck.Location.getReferences()) {
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
        for (URI key : DomainOrRangeTypeCheck.dateCheck.getReferences()) {
            if (string.equals(key.toString())) {
                return true;
            }
        }
        return false;
    }
    
    private static Boolean isCause(String string) {
        if (StringUtils.isBlank(string)) {
            return false;
        }
        for (URI key : DomainOrRangeTypeCheck.CauseCheck.getReferences()) {
            if (string.equals(key.toString())) {
                return true;
            }
        }
        return false;
    }
    
    private static Boolean isActivity(String string) {
        if (StringUtils.isBlank(string)) {
            return false;
        }
        for (URI key : DomainOrRangeTypeCheck.ActivityCheck.getReferences()) {
            if (string.equals(key.toString())) {
                return true;
            }
        }
        return false;
    }
    
    private boolean isGrowing(String string) {
        if (StringUtils.isBlank(string)) {
            return false;
        }
        for (URI key : DomainOrRangeTypeCheck.GrowCheck.getReferences()) {
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
    
     public void setSelectedTemplate(String template) {
        this.selectedTemplate=template;
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
    
    public static Boolean isRdfsLabel(String string) {
        if (StringUtils.isBlank(string)) {
            return false;
        }
        for (URI key : DomainOrRangeTypeCheck.LocationCheck.getReferences()) {

            if (string.equals(key.toString())) {
                return true;
            }
        }
        return false;

    }

    private String findGradableTemplate() {
        String subjectUri = this.lexicalEntryUtil.getConditionUriBySelectVariable(SelectVariable.subjOfProp).toString();
        String objectUri = this.lexicalEntryUtil.getConditionUriBySelectVariable(SelectVariable.objOfProp).toString();
        String referenceUri = this.lexicalEntryUtil.getReferenceUri();
        String string=subjectUri;
        String type=null;

        if (this.isPlace(string)) {
            type= superlativeCountry;
        }
        else if (this.isLocation(string)) {
            type= superlativeLocation;
        }
        else if (this.isPerson(string)) {
            type= superlativePerson;
        }
        else if(isSuperlativeThing(string)){
             type= superlativeWorld;
        }
      
       return type;
    }
    
    private String findReference() {

        if (this.selectedTemplate.equals(superlativePerson)) {
            return DomainOrRangeTypeCheck.child.getReferences().get(0).toString();
        }
        else if (this.selectedTemplate.equals(superlativeCountry)) {
           return DomainOrRangeTypeCheck.locatedInArea.getReferences().get(0).toString();
        }
        else if (this.selectedTemplate.equals(superlativeLocation)) {
            /* System.out.println(selectedTemplate);
                System.out.println(DomainOrRangeTypeCheck.location.getReferences().get(0).toString());
         exit(1);*/
           return DomainOrRangeTypeCheck.location.getReferences().get(0).toString();
        }
        else if(this.selectedTemplate.equals(superlativeWorld)){
             return "";
        }
        
       return null;
    }
    
    public static Boolean isSuperlativePlace(String string) {
        if (StringUtils.isBlank(string)) {
            return false;
        }
        for (URI key : DomainOrRangeTypeCheck.PlaceCheck.getReferences()) {

            if (string.equals(key.toString())) {
                return true;
            }
        }
        return false;

    }
    
    
    
    public static Boolean isSuperlativeThing(String string) {
        if (StringUtils.isBlank(string)) {
            return false;
        }
        for (URI key : DomainOrRangeTypeCheck.ThingCheck.getReferences()) {

            if (string.equals(key.toString())) {
                return true;
            }
        }
        return false;

    }

    public String getPropertyReference() {
        return propertyReference;
    }

    
   

   

  
}
