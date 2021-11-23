/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.io;

import com.google.gdata.util.common.base.Pair;
import grammar.datasets.sentencetemplates.TempConstants;
import java.io.File;
import java.io.IOException;
import static java.lang.System.exit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import util.io.GoogleXslSheet.AttributiveAdjectiveFrame;

/**
 *
 * @author elahi
 */
public class TurtleCreation {

    private String lemonEntry = "";
    private String partOfSpeech = "";
    private String gender = "";
    private String writtenFormSingular = "";
    private String writtenFormPlural = "";
    private String writtenFormAccusativeForm = "";
    private String writtenDativeForm = "";
    private String writtenGenitiveForm = "";
    private String writtenForm3rdPerson = "";
    private String writtenFormPast = "";
    private String writtenFormPerfect = "";
    private String preposition = "";
    private String tutleString = "";
    private String tutleFileName = "";
    private static Integer index = 0;
    private LinkedData linkedData = null;
    private String language = "";
    //private  Map<String,String[]> referenceArticleMap = new TreeMap<String,String[]>();

    public TurtleCreation(String key, List<String[]> rows, LinkedData linkedData,String language) throws Exception {
        this.linkedData = linkedData;
        this.language=language;
        String syntacticFrame = findSyntacticFrame(rows);

        if (syntacticFrame.equals(TempConstants.NounPPFrame)) {
            setNounPPFrame(key, rows, syntacticFrame);
        } else if (syntacticFrame.equals(TempConstants.TransitiveFrame)) {
            setTransitiveFrame(key, rows, syntacticFrame);
        } else if (syntacticFrame.equals(TempConstants.IntransitivePPFrame)) {
            setIntransitivePPFrame(key, rows, syntacticFrame);
        } else if (syntacticFrame.equals(TempConstants.AdjectiveAttributiveFrame)) {
            setAdjectiveFrame(key, rows, syntacticFrame);
        } else {
            System.out.println("no syntactic frame is found!!");
            //syntacticFrame = row[GoogleXslSheet.TransitFrameSyntacticFrameIndex];
        }

    }

    private void setNounPPFrame(String key, List<String[]> rows, String syntacticFrame) throws Exception {
        this.setLemonEntryId(key);

        List<Tupples> tupplesList = new ArrayList<Tupples>();
        Integer index = 0;
        for (String[] row : rows) {
            if (index == 0) {
                this.partOfSpeech = row[GoogleXslSheet.NounPPFrame.getPartOfSpeechIndex()];
                this.gender=row[GoogleXslSheet.NounPPFrame.getGenderIndex()];
                this.writtenFormSingular = row[GoogleXslSheet.NounPPFrame.getWrittenFormSingularIndex()];
                this.writtenFormPlural = row[GoogleXslSheet.NounPPFrame.getWrittenFormPluraIndex()];
                this.writtenFormAccusativeForm=row[GoogleXslSheet.NounPPFrame.getWrittenFormAccusativeIndex()];
                this.writtenDativeForm=row[GoogleXslSheet.NounPPFrame.getWrittenFormDativeIndex()];
                this.writtenGenitiveForm=row[GoogleXslSheet.NounPPFrame.getWrittenFormGenetiveIndex()];
                this.preposition = row[GoogleXslSheet.NounPPFrame.getPrepositionIndex()];
            }
            Tupples tupple=new Tupples(this.lemonEntry,
                    index + 1,
                    this.setReference(row[GoogleXslSheet.NounPPFrame.getReferenceIndex()]),
                    this.setReference(row[GoogleXslSheet.NounPPFrame.getDomainIndex()]),
                    this.setReference(row[GoogleXslSheet.NounPPFrame.getRangeIndex()]));
            tupplesList.add(tupple);
            index = index + 1;
            GoogleXslSheet.NounPPFrame.setArticle(tupple, this.gender, row);
            /*GenderUtils.referenceArticleMap.put(tupple.getReference(), new String[]{this.gender});
            GenderUtils.referenceArticleMap.put(tupple.getDomain(), new String[]{row[GoogleXslSheet.NounPPFrame.getRangeIndex()+1]});
            GenderUtils.referenceArticleMap.put(tupple.getRange(), new String[]{row[GoogleXslSheet.NounPPFrame.getRangeIndex()+2]});*/
        }
        this.tutleString
                = GoogleXslSheet.NounPPFrame.getNounPPFrameHeader(this.lemonEntry, TempConstants.preposition, this.language)
                + GoogleXslSheet.NounPPFrame.getIndexing(this.lemonEntry, tupplesList)
                + GoogleXslSheet.NounPPFrame.getWrittenFormAll(this.lemonEntry,this.gender, this.writtenFormSingular,this.writtenFormPlural,writtenFormAccusativeForm,this.writtenDativeForm,this.writtenGenitiveForm,this.language)
                + GoogleXslSheet.getSenseDetail(tupplesList, TempConstants.NounPPFrame, this.lemonEntry, this.writtenFormSingular, this.preposition, this.language)
                + GoogleXslSheet.NounPPFrame.getPreposition(TempConstants.preposition,this.preposition, language);
        this.tutleFileName = getFileName(syntacticFrame);

    }

    private void setTransitiveFrame(String key, List<String[]> rows, String syntacticFrame) throws Exception {
        this.setLemonEntryId(key);
        List<Tupples> tupples = new ArrayList<Tupples>();
        Integer index = 0;
        for (String[] row : rows) {
            if (index == 0) {
                this.partOfSpeech = row[GoogleXslSheet.partOfSpeechIndex];
                this.writtenFormSingular = row[GoogleXslSheet.writtenFormInfinitive];
                this.writtenForm3rdPerson = row[GoogleXslSheet.TransitFrame.writtenForm3rdPerson];
                this.writtenFormPast = row[GoogleXslSheet.TransitFrame.writtenFormPast];
                this.preposition = row[GoogleXslSheet.TransitFrame.rangeIndex+1];
            }
            tupples.add(new Tupples(this.lemonEntry,
                    index + 1,
                    this.setReference(row[GoogleXslSheet.TransitFrame.referenceIndex]),
                    this.setReference(row[GoogleXslSheet.TransitFrame.domainIndex]),
                    this.setReference(row[GoogleXslSheet.TransitFrame.rangeIndex])));
            index = index + 1;

        }
        this.tutleString
                = GoogleXslSheet.TransitFrame.getHeader(this.lemonEntry,TempConstants.preposition,this.preposition,this.language)
                + GoogleXslSheet.TransitFrame.getSenseIndexing(tupples, lemonEntry)
                + GoogleXslSheet.TransitFrame.getWritten(this.lemonEntry, this.writtenFormSingular, this.writtenForm3rdPerson, this.writtenFormPast,this.language)
                + GoogleXslSheet.getSenseDetail(tupples, TempConstants.TransitiveFrame,this.lemonEntry,this.writtenFormSingular,this.preposition,this.language)
                + GoogleXslSheet.getPrepostion(TempConstants.preposition,this.preposition,this.language);
        this.tutleFileName = getFileName(syntacticFrame);
    }

    private void setIntransitivePPFrame(String key, List<String[]> rows, String syntacticFrame) throws Exception {

        List<Tupples> tupplesList = new ArrayList<Tupples>();
        Integer index = 0;
        for (String[] row : rows) {
            if (index == 0) {
                this.setLemonEntryId(row[GoogleXslSheet.lemonEntryIndex]);
                this.partOfSpeech = row[GoogleXslSheet.partOfSpeechIndex];
                this.writtenFormSingular = row[GoogleXslSheet.writtenFormInfinitive];
                this.writtenForm3rdPerson = row[GoogleXslSheet.InTransitFrame.writtenForm3rdPerson];
                this.writtenFormPast = row[GoogleXslSheet.InTransitFrame.writtenFormPast];
                this.writtenFormPerfect=row[GoogleXslSheet.InTransitFrame.writtenFormPerfect];
                this.preposition = row[GoogleXslSheet.InTransitFrame.preposition];
                /*if(this.preposition.contains("-")){
                   ;
                }
                else 
                    continue;*/

            }
            Tupples tupple=new Tupples(this.lemonEntry,
                    index + 1,
                    this.setReference(row[GoogleXslSheet.InTransitFrame.getReferenceIndex()]),
                    this.setReference(row[GoogleXslSheet.InTransitFrame.getDomainIndex()]),
                    this.setReference(row[GoogleXslSheet.InTransitFrame.getRangeIndex()]));
            tupplesList.add(tupple);
            index = index + 1;
            this.setDomainRangeInTransitiveInfo(tupple,this.gender,row);
          
        }
        this.tutleString
                = GoogleXslSheet.InTransitFrame.getHeader(lemonEntry,TempConstants.preposition,language)
                + GoogleXslSheet.InTransitFrame.getSenseIndexing(tupplesList,this.lemonEntry)
                + GoogleXslSheet.InTransitFrame.getWritten(lemonEntry,writtenFormSingular,writtenForm3rdPerson,writtenFormPast,writtenFormPerfect,language)
                + GoogleXslSheet.getSenseDetail(tupplesList, TempConstants.IntransitivePPFrame,lemonEntry,writtenFormSingular,preposition,language)
                + GoogleXslSheet.getPrepostion(TempConstants.preposition,preposition,language);        
        this.tutleFileName = getFileName(syntacticFrame);
        
        /*System.out.println("partOfSpeech::" + partOfSpeech);
        System.out.println("writtenFormInfinitive::" + writtenFormInfinitive);
        System.out.println("writtenForm3rdPerson::" + writtenForm3rdPerson);
        System.out.println("preposition::" + preposition);
        System.out.println("sense::" + sense);
        System.out.println("reference::" + reference);
        System.out.println("reference::" + domain);
        System.out.println("reference::" + range);*/
    }

    private void setAdjectiveFrame(String key, List<String[]> rows, String syntacticFrame) throws Exception {
        List<Tupples> tupples = new ArrayList<Tupples>();
        Integer index = 0;
        for (String[] row : rows) {
            System.out.println(row[GoogleXslSheet.lemonEntryIndex]);
              System.out.println(row[GoogleXslSheet.partOfSpeechIndex]);
              System.out.println(row[GoogleXslSheet.writtenFormInfinitive]);
            if (index == 0) {
                this.setLemonEntryId(row[GoogleXslSheet.lemonEntryIndex]);
                this.partOfSpeech = row[GoogleXslSheet.partOfSpeechIndex];
                this.writtenFormSingular = row[GoogleXslSheet.writtenFormInfinitive];

            }
                

            tupples.add(new Tupples(this.lemonEntry,
                    index + 1,
                    "",
                    this.setReference(row[GoogleXslSheet.AttributiveAdjectiveFrame.owl_onPropertyIndex]),
                    this.setReference(row[GoogleXslSheet.AttributiveAdjectiveFrame.owl_hasValueIndex])));
            index = index + 1;
        }
            System.out.println(index+"  tupples:"+tupples.size());
        this.tutleString
                = GoogleXslSheet.AttributiveAdjectiveFrame.getAtrributiveFrameHeader(this.lemonEntry, tupples,this.language)
                + GoogleXslSheet.AttributiveAdjectiveFrame.getAtrributiveFrameIndexing(tupples, this.lemonEntry)
                + GoogleXslSheet.AttributiveAdjectiveFrame.getAtrrtibutiveWrittenForm(lemonEntry, writtenFormSingular,this.language)
                + GoogleXslSheet.getSenseDetail(tupples, syntacticFrame, this.lemonEntry,"","",this.language);
        this.tutleFileName = getFileName(syntacticFrame);
        System.out.println(tutleString);
        System.out.println(tutleFileName);

        /*System.out.println("partOfSpeech::" + partOfSpeech);
        System.out.println("writtenFormInfinitive::" + writtenFormInfinitive);
        System.out.println("writtenForm3rdPerson::" + writtenForm3rdPerson);
        System.out.println("preposition::" + preposition);
        System.out.println("sense::" + sense);
        System.out.println("reference::" + reference);
        System.out.println("reference::" + domain);
        System.out.println("reference::" + range);*/
    }

   
    private String setReference(String reference) throws Exception {
        if (reference.contains(LinkedData.http)) {
            return reference;
        } else if (reference.contains(LinkedData.colon)) {
            String[] info = reference.split(LinkedData.colon);
            String prefix = info[0].strip().trim();
            if (this.linkedData.getPrefixes().containsKey(prefix)) {
                reference = this.linkedData.getPrefixes().get(prefix) + info[1];
            }

        }
        return reference;
    }

    private String findSyntacticFrame(List<String[]> rows) throws Exception {
        String syntactType = null;
       
            
            Integer index = 0;
            for (String[] row : rows) {
                if (index == 0) {
                    syntactType = this.findSyntacticFrame(row);
                    break;
                }
            }
        
        return syntactType;
    }

    private String findSyntacticFrame(String[] row) throws Exception {
        String nounPPFrame=row[GoogleXslSheet.NounPPFrame.getSyntacticFrame()];
        System.out.println("row.length::" + row.length);
        System.out.println("nounPPFrame::" + nounPPFrame);
        System.out.println("row[GoogleXslSheet.TransitFrameSyntacticFrameIndex]::" + row[GoogleXslSheet.TransitFrame.getSyntacticFrame()]);
        System.out.println("row[GoogleXslSheet.InTransitFrameSyntacticFrameIndex]::" + row[GoogleXslSheet.InTransitFrame.getSyntacticFrame()]);
        System.out.println("row[GoogleXslSheet.AdjectiveFrameIndex]::" + row[GoogleXslSheet.AdjectiveFrameIndex]);
       

        try {
            if (nounPPFrame.equals(TempConstants.NounPPFrame)) {
                return TempConstants.NounPPFrame;
            } else if (row[GoogleXslSheet.TransitFrame.getSyntacticFrame()].equals(TempConstants.TransitiveFrame)) {
                return TempConstants.TransitiveFrame;
            } else if (row[GoogleXslSheet.InTransitFrame.getSyntacticFrame()].equals(TempConstants.IntransitivePPFrame)) {
                return TempConstants.IntransitivePPFrame;
            } else if (row[GoogleXslSheet.AdjectiveFrameIndex].equals(TempConstants.AdjectiveAttributiveFrame)) {
                return TempConstants.AdjectiveAttributiveFrame;
            } else {
               System.out.println("row.length::" + row.length);
                //throw new Exception("invalid entry."); //To change body of generated methods, choose Tools | Templates.   
            }
        } catch (NullPointerException ex) {
            throw new Exception("no lexical entries found in csv file" + ex.getMessage().toString()); //To change body of generated methods, choose Tools | Templates.   
        } catch (Exception ex) {
            throw new Exception("invalid entry." + ex.getMessage().toString()); //To change body of generated methods, choose Tools | Templates.   
        }
        return null;
    }

    private String modify(String string) {
        /*string = string.replaceAll("[^a-zA-Z0-9]", " ");
        string = string.toLowerCase().strip().trim().replace(" ", "_");*/
        index = index + 1;
        //return "LexicalEntry_" + string+"_"+index.toString();
        return string;
    }

    private Pair<Boolean, String> findPrefix(String prefix) {
        prefix = prefix.strip().trim();
        for (String key : this.linkedData.getPrefixes().keySet()) {
            String value = this.linkedData.getPrefixes().get(key);
            value = value.strip().trim();
            key = key.strip().trim();
            if (key.equals(prefix)) {
                return new Pair<Boolean, String>(Boolean.TRUE, value);
            }

        }
        return new Pair<Boolean, String>(Boolean.FALSE, prefix);
    }

    /*Pair<Boolean, String> pair = this.findPrefix(prefix);
            if (pair.getFirst()) {
                reference = pair.getSecond();
            } else {
                throw new Exception("the prefix " + prefix + " is not valid for dataset " + this.linkedData.getEndpoint());
            }*/
   

    /*private String getInTransitivePPFrameHeader() {
        return "@prefix :        <http://localhost:8080/lexicon#> .\n"
                + "\n"
                + "@prefix lexinfo: <http://www.lexinfo.net/ontology/2.0/lexinfo#> .\n"
                + "@prefix lemon:   <http://lemon-model.net/lemon#> .\n"
                + "\n"
                + "@base            <http://localhost:8080#> .\n"
                + "\n"
                + ":lexicon_en a    lemon:Lexicon ;\n"
                + "  lemon:language \"en\" ;\n"
                + "  lemon:entry    :to_" + this.lemonEntry + " ;\n"
                + "  lemon:entry    :in .\n"
                + "\n";
    }*/

    /*private List<Tupples> getSenseIds(Integer rank, String reference, String domain,String range) {
        List<Tupples> senses = new ArrayList<Tupples>();
        for (Integer index = 0; rank > index; index++) {
            String senseId=this.lemonEntry + "_sense_" + index.toString() + "ontomap";
            senses.add(new Tupples(senseId,reference,domain,range));
        }
        return senses;
    }*/
    private String getSenseId(List<Tupples> senseIds) {
        String str = "";
        for (Tupples tupple : senseIds) {
            String line = "  lemon:sense          :" + tupple.getSenseId() + " ;\n";
            str += line;
        }
        return str;
    }

    /*private String getSenseDetail(List<Tupples> senseIds) {
        String str = "";
        for (Tupples tupple : senseIds) {
            String line = ":" + tupple.getSenseId() + " a lemon:OntoMap, lemon:LexicalSense ;\n"
                    + "  lemon:ontoMapping         :" + tupple.getSenseId() + " ;\n"
                    + "  lemon:reference           <" + tupple.getReference() + "> ;\n"
                    + "  lemon:subjOfProp          :arg2 ;\n"
                    + "  lemon:objOfProp           :arg1 ;\n"
                    + "  lemon:condition           :" + tupple.getSenseId() + "_condition .\n"
                    + "\n"
                    + ":" + tupple.getSenseId() + "_condition a lemon:condition ;\n"
                    + "  lemon:propertyDomain  <" + tupple.getDomain() + "> ;\n"
                    + "  lemon:propertyRange   <" + tupple.getRange() + "> .\n"
                    + "\n";
            str += line;
        }
        return str;

    }*/
   

   
   

    /*private String getInTransIndexing(List<Tupples> senseIds) {
        String senseIdStr = this.getSenseId(senseIds);
        senseIdStr = ":to_" + this.lemonEntry + " a             lemon:LexicalEntry ;\n"
                + "  lexinfo:partOfSpeech lexinfo:verb ;\n"
                + "  lemon:canonicalForm  :form_" + this.lemonEntry + " ;\n"
                + "  lemon:otherForm      :form_" + this.lemonEntry + "_past ;\n"
                + senseIdStr
                + "  lemon:synBehavior    :" + this.lemonEntry + "_frame .\n"
                + "\n";
        return senseIdStr;
    }*/

    

    /*private String getInTranSenseDetail(List<Tupples> tupples) {
        String str = "";
        for (Tupples tupple : tupples) {
            String line = ":" + tupple.getSenseId() + " a     lemon:OntoMap, lemon:LexicalSense ;\n"
                    + "  lemon:ontoMapping :" + this.lemonEntry + "_ontomap ;\n"
                    + "  lemon:reference   <" + tupple.getReference() + "> ;\n"
                    + "  lemon:subjOfProp  :" + this.lemonEntry + "_obj ;\n"
                    + "  lemon:objOfProp   :" + this.lemonEntry + "_subj ;\n"
                    + "  lemon:condition   :" + this.lemonEntry + "_condition .\n"
                    + "\n"
                    + ":" + this.lemonEntry + "_condition a      lemon:condition ;\n"
                    + "  lemon:propertyDomain <" + tupple.getDomain() + "> ;\n"
                    + "  lemon:propertyRange  <" + tupple.getRange() + "> .\n"
                    + "\n";
            str += line;
        }

        return str;
    }*/
    

    /*private String getSenseDetail(List<Tupples> tupples, String syntacticFrame) {
        String str = "";

        if (syntacticFrame.equals(GoogleXslSheet.TransitiveFrameStr)) {
            for (Tupples tupple : tupples) {
                String line = ":" + tupple.getSenseId() + " a   lemon:OntoMap, lemon:LexicalSense ;\n"
                        + "  lemon:ontoMapping         :" + tupple.getSenseId() + " ;\n"
                        + "  lemon:reference   <" + tupple.getReference() + "> ;\n"
                        + "  lemon:subjOfProp  :" + lemonEntry + "_obj ;\n"
                        + "  lemon:objOfProp   :" + lemonEntry + "_subj ;\n"
                        + "  lemon:condition   :" + tupple.getSenseId() + "_condition .\n"
                        + "\n"
                        + "\n"
                        + ":" + tupple.getSenseId() + "_condition a    lemon:condition ;\n"
                        + "  lemon:propertyDomain <" + tupple.getDomain() + "> ;\n"
                        + "  lemon:propertyRange  <" + tupple.getRange() + "> .\n"
                        + "\n";
                str += line;
            }
        } else if (syntacticFrame.equals(GoogleXslSheet.InTransitiveFrameStr)) {
            for (Tupples tupple : tupples) {
                String line = ":" + tupple.getSenseId() + " a     lemon:OntoMap, lemon:LexicalSense ;\n"
                        + "  lemon:ontoMapping :" + this.lemonEntry + "_ontomap ;\n"
                        + "  lemon:reference   <" + tupple.getReference() + "> ;\n"
                        + "  lemon:subjOfProp  :" + this.lemonEntry + "_obj ;\n"
                        + "  lemon:objOfProp   :" + this.lemonEntry + "_subj ;\n"
                        + "  lemon:condition   :" + tupple.getSenseId() + "_condition .\n"
                        + "\n"
                        + ":" + tupple.getSenseId() + "_condition a      lemon:condition ;\n"
                        + "  lemon:propertyDomain <" + tupple.getDomain() + "> ;\n"
                        + "  lemon:propertyRange  <" + tupple.getRange() + "> .\n"
                        + "\n";
                str += line;
            }

        } else if (syntacticFrame.equals(GoogleXslSheet.NounPPFrameStr)) {
            for (Tupples tupple : tupples) {
                String line = ":" + tupple.getSenseId() + " a lemon:OntoMap, lemon:LexicalSense ;\n"
                        + "  lemon:ontoMapping         :" + tupple.getSenseId() + " ;\n"
                        + "  lemon:reference           <" + tupple.getReference() + "> ;\n"
                        + "  lemon:subjOfProp          :arg2 ;\n"
                        + "  lemon:objOfProp           :arg1 ;\n"
                        + "  lemon:condition           :" + tupple.getSenseId() + "_condition .\n"
                        + "\n"
                        + ":" + tupple.getSenseId() + "_condition a lemon:condition ;\n"
                        + "  lemon:propertyDomain  <" + tupple.getDomain() + "> ;\n"
                        + "  lemon:propertyRange   <" + tupple.getRange() + "> .\n"
                        + "\n";
                str += line;
            }
        } else if (syntacticFrame.equals(GoogleXslSheet.AdjectiveFrameStr)) {
            for (Tupples tupple : tupples) {
                String line = ":" + tupple.getSenseId() + "_sense a  lemon:LexicalSense ;\n"
                        + "  lemon:reference :" + tupple.getSenseId() + "_res ;\n"
                        + "  lemon:isA       :" + tupple.getSenseId() + "_AttrSynArg, :swedish_PredSynArg .\n"
                        + "\n"
                        + ":" + tupple.getSenseId() + "_res a   owl:Restriction ;\n"
                        + "  owl:onProperty <" + tupple.getDomain() + "> ;\n"
                        + "  owl:hasValue   <" + tupple.getRange() + "> .\n";
                str += line;
            }
        }

        return str;
    }*/

    private void setLemonEntryId(String[] row) {
        this.lemonEntry = row[GoogleXslSheet.lemonEntryIndex];
    }

    private void setLemonEntryId(String writtenForm) {
        this.lemonEntry = this.modify(writtenForm);

    }

    public String getTutleFileName() {
        return tutleFileName;
    }

    private String getFileName(String syntacticFrame) {
        return syntacticFrame + "-lexicon" + "-" + lemonEntry.replace("/", "") + ".ttl";

    }

    public String getTutleString() {
        return this.tutleString;
    }
    
    public static Boolean generateTurtle(String inputDir, LinkedData linkedData,String language) throws IOException, Exception {
        String lemonEntry = null;
        File f = new File(inputDir);
        Boolean flag=false;
        String[] pathnames = f.list();
        for (String pathname : pathnames) {
            String[] files = new File(inputDir + File.separatorChar + pathname).list();
            for (String fileName : files) {
                if (!fileName.contains(".csv")) {
                    continue;
                }

                CsvFile csvFile = new CsvFile();
                String directory = inputDir + "/" + pathname + "/";
                List<String[]> rows = csvFile.getRows(new File(directory + fileName));
                Integer index = 0;
                Map<String, List<String[]>> keyRows = new HashMap<String, List<String[]>>();
                for (String[] row : rows) {
                    if (index == 0) {
                        index = index + 1;
                        continue;
                    } 
                    String key = row[0];
                    List<String[]> values = new ArrayList<String[]>();
                    if (keyRows.containsKey(key)) {
                        values = keyRows.get(key);
                    }
                    values.add(row);
                    keyRows.put(key, values);
                    index = index + 1;
                }
                try {
                    Integer count=0;
                    for (String key : keyRows.keySet()) {
                        count=count+1;
                        System.out.println("key:"+key+" count:"+count);
                        List<String[]> values = keyRows.get(key);
                        TurtleCreation turtleCreation = new TurtleCreation(key,values, linkedData,language);
                        FileUtils.stringToFile(turtleCreation.getTutleString(), directory + turtleCreation.getTutleFileName());
                        flag=true;
                    }

                } catch (Exception ex) {
                    java.util.logging.Logger.getLogger(TurtleCreation.class.getName()).log(Level.SEVERE, null, ex);
                    throw new Exception(ex.getMessage());
                }

            }

        }
      return flag;
    }

    private void setDomainRangeInTransitiveInfo(Tupples tupple, String gender, String[] row) {
        GenderUtils.setArticles(tupple.getReference(), gender);
        GenderUtils.setArticles(tupple.getDomain(), row[GoogleXslSheet.InTransitFrame.getDomainArticleIndex()]);
        GenderUtils.setArticles(tupple.getRange(), row[GoogleXslSheet.InTransitFrame.getRangeArticleIndex()]);
        GenderUtils.setWrittenForms(tupple.getDomain(), row[GoogleXslSheet.InTransitFrame.getDomainWrittenSingular()], row[GoogleXslSheet.InTransitFrame.getDomainWrittenPlural()]);
        GenderUtils.setWrittenForms(tupple.getRange(), row[GoogleXslSheet.InTransitFrame.getRangeWrittenSingular()], row[GoogleXslSheet.InTransitFrame.getRangeWrittenPlural()]);
        GenderUtils.setVerbType(this.writtenFormSingular, this.partOfSpeech);
        GenderUtils.setVerbType(this.writtenFormPlural,this.partOfSpeech);
        GenderUtils.setVerbType(this.writtenFormPast, this.partOfSpeech);
        GenderUtils.setVerbType(this.writtenFormAccusativeForm,this.partOfSpeech);
        GenderUtils.setVerbType(this.writtenDativeForm, this.partOfSpeech);
        GenderUtils.setVerbType(this.writtenGenitiveForm, this.partOfSpeech);
    }
    
   

    

}
