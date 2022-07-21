/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package turtle;

import turtle.TurtleCreation;
import turtle.TutleConverter;
import grammar.datasets.sentencetemplates.TempConstants;
import grammar.structure.component.Language;
import java.io.File;
import java.io.IOException;
import static java.lang.System.exit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import util.io.CsvFile;
import util.io.FileProcessUtils;
import linkeddata.LinkedData;
import util.io.Tupples;

/**
 *
 * @author elahi
 */
public class GermanTurtle extends TurtleCreation implements TutleConverter {

    private String lemonEntry = "";
    private String partOfSpeech = "";
    private String gender = "";
    private String writtenFormInfinitive = "";
    private String writtenForm2 = "";
    private String writtenForm4 = "";
    private String writtenForm5 = "";
    private String writtenForm6 = "";
    private String writtenForm3rdPerson = "";
    private String writtenFormPast = "";
    private String writtenFormPerfect = "";
    private String preposition = "";
    private static Integer index = 0;
    private GermanCsv.NounPPFrameCsv nounPPFrameCsv = new GermanCsv.NounPPFrameCsv();
    private GermanCsv.TransitFrameCsv transitiveFrameCsv = new GermanCsv.TransitFrameCsv();
    private GermanCsv.InTransitFrameCsv intransitiveFrameCsv = new GermanCsv.InTransitFrameCsv();
    private GermanCsv.AttributiveAdjectiveFrameCsv attributiveAdjectiveFrame = new GermanCsv.AttributiveAdjectiveFrameCsv();
    private GermanCsv.GradbleAdjectiveFrameCsv gradableAdjectiveFrameCsv = new GermanCsv.GradbleAdjectiveFrameCsv();



    public GermanTurtle(String inputDir, LinkedData linkedData, Language language) throws Exception {
        super(inputDir, linkedData, language);
        super.setSyntacticFrameIndexes(nounPPFrameCsv.getSyntacticFrameIndex(),transitiveFrameCsv.getSyntacticFrameIndex(),intransitiveFrameCsv.getSyntacticFrameIndex(),attributiveAdjectiveFrame.getSyntacticFrameIndex(),gradableAdjectiveFrameCsv.getSyntacticFrameIndex());
        this.generateTurtle();
    }

    private void generateTurtle() throws IOException, Exception {
        String lemonEntry = null;
        File f = new File(inputDir);
        Boolean flag = false;
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
                    Integer count = 0;
                    for (String key : keyRows.keySet()) {
                        count = count + 1;
                        List<String[]> values = keyRows.get(key);
                        setSyntacticFrame(key, values);
                        FileProcessUtils.stringToFile(this.turtleString, directory + this.tutleFileName);
                        if (this.turtleString != null) {
                            this.conversionFlag = true;
                        }

                    }

                } catch (Exception ex) {
                    java.util.logging.Logger.getLogger(GermanTurtle.class.getName()).log(Level.SEVERE, null, ex);
                    throw new Exception(ex.getMessage());
                }

            }

        }
    }

    private void setSyntacticFrame(String key, List<String[]> rows) throws Exception {
        String syntacticFrame = super.findSyntacticFrame(rows);
        key = key.trim().strip().stripLeading().stripTrailing();
        if (syntacticFrame.equals(NounPPFrame)) {
            setNounPPFrame(key, rows, syntacticFrame);
        } else if (syntacticFrame.equals(TransitiveFrame)) {
            setTransitiveFrame(key, rows, syntacticFrame);
        } else if (syntacticFrame.equals(IntransitivePPFrame)) {
            setIntransitivePPFrame(key, rows, syntacticFrame);
        } else if (syntacticFrame.equals(AdjectiveAttributiveFrame)) {
            setAdjectiveFrame(key, rows, syntacticFrame);
        } else if (syntacticFrame.equals(AdjectiveSuperlativeFrame)) {
            setAdjectiveGradableFrame(key, rows, syntacticFrame);
        } else {
            System.out.println("no syntactic frame is found!!");
            //syntacticFrame = row[GoogleXslSheet.TransitFrameSyntacticFrameIndex];
        }

    }

    @Override
    public void setNounPPFrame(String key, List<String[]> rows, String syntacticFrame) {
        this.setLemonEntryId(key);

        List<Tupples> tupplesList = new ArrayList<Tupples>();
         String copulativeArg=null;
        Integer index = 0;
        for (String[] row : rows) {
            
            if (index == 0) {
                this.partOfSpeech = nounPPFrameCsv.getPartOfSpeechIndex(row);
                this.gender = nounPPFrameCsv.getGenderIndex(row);
                this.writtenFormInfinitive = nounPPFrameCsv.getWrittenFormSingularIndex(row);
                this.writtenForm2 = nounPPFrameCsv.getWrittenFormPluraIndex(row);
                this.writtenForm4 = nounPPFrameCsv.getWrittenFormAccusativeIndex(row);
                this.writtenForm5 = nounPPFrameCsv.getWrittenFormDativeIndex(row);
                this.writtenForm6 = nounPPFrameCsv.getWrittenFormGenetiveIndex(row);
                this.preposition = this.checkValidPreposition(nounPPFrameCsv.getPrepositionIndex(row));
                copulativeArg=nounPPFrameCsv.getCopulativeArgIndex(row);
            }
            Tupples tupple = new Tupples(this.lemonEntry,
                    index + 1,
                    setReference(nounPPFrameCsv.getReferenceIndex(row)),
                    setReference(nounPPFrameCsv.getDomainIndex(row)),
                    setReference(nounPPFrameCsv.getRangeIndex(row)));
            tupplesList.add(tupple);
            index = index + 1;
            nounPPFrameCsv.setArticle(tupple, this.gender, row);
        }
       
        this.turtleString
                = GermanCsv.NounPPFrameCsv.getNounPPFrameHeader(this.lemonEntry, TempConstants.preposition, this.language)
                + GermanCsv.NounPPFrameCsv.getIndexing(this.lemonEntry, tupplesList)
                + GermanCsv.NounPPFrameCsv.getWrittenFormAll(this.lemonEntry, this.gender, this.writtenFormInfinitive, this.writtenForm2, writtenForm4, this.writtenForm5, this.writtenForm6, this.language,copulativeArg)
                + GermanCsv.getSenseDetail(tupplesList, TempConstants.NounPPFrame, this.lemonEntry, this.writtenFormInfinitive, this.preposition, this.language)
                + GermanCsv.NounPPFrameCsv.getPreposition(this.lemonEntry, this.preposition, language);
        this.tutleFileName = getFileName(syntacticFrame);
    }

    @Override
    public void setTransitiveFrame(String key, List<String[]> rows, String syntacticFrame) {
        this.setLemonEntryId(key);
        List<Tupples> tupples = new ArrayList<Tupples>();
        Integer index = 0;
                    String subject=null;

        for (String[] row : rows) {
            if (index == 0) {
                this.partOfSpeech = row[GermanCsv.partOfSpeechIndex];
                this.writtenFormInfinitive = row[GermanCsv.writtenFormInfinitive];
                this.writtenForm3rdPerson = row[transitiveFrameCsv.writtenForm3rdPerson];
                this.writtenFormPast = row[transitiveFrameCsv.writtenFormPast];
                this.writtenFormPerfect = row[transitiveFrameCsv.writtenFormPerfect];
                this.preposition = this.checkValidPreposition(row[transitiveFrameCsv.passivePrepositionIndex]);
                subject=  transitiveFrameCsv.getSubjectIndex(row);

            }
            Tupples tupple = new Tupples(this.lemonEntry,
                    index + 1,
                    this.setReference(row[transitiveFrameCsv.referenceIndex]),
                    this.setReference(row[transitiveFrameCsv.domainIndex]),
                    this.setReference(row[transitiveFrameCsv.rangeIndex]));

            transitiveFrameCsv.setArticle(tupple, this.gender, row);
            transitiveFrameCsv.setVerbInfo(partOfSpeech, writtenFormInfinitive, writtenForm3rdPerson, writtenFormPast, writtenFormPerfect);
            tupples.add(tupple);
            index = index + 1;


        }
        this.turtleString
                = transitiveFrameCsv.getHeader(this.lemonEntry, TempConstants.preposition, this.preposition, this.language)
                + transitiveFrameCsv.getSenseIndexing(tupples, lemonEntry)
                + transitiveFrameCsv.getWritten(this.lemonEntry, this.writtenFormInfinitive, this.writtenForm3rdPerson, this.writtenFormPast, writtenFormPerfect,this.language,subject)
                + GermanCsv.getSenseDetail(tupples, TempConstants.TransitiveFrame, this.lemonEntry, this.writtenFormInfinitive, this.preposition, this.language)
                + transitiveFrameCsv.getPreposition(this.lemonEntry, this.preposition, this.language);
        this.tutleFileName = getFileName(syntacticFrame);
    }

    @Override
    public void setIntransitivePPFrame(String key, List<String[]> rows, String syntacticFrame) {
        List<Tupples> tupplesList = new ArrayList<Tupples>();
        Integer index = 0;
         String subject= null;
        for (String[] row : rows) {
            if (index == 0) {
                this.setLemonEntryId(row[GermanCsv.lemonEntryIndex]);
                this.partOfSpeech = row[GermanCsv.partOfSpeechIndex];
                this.writtenFormInfinitive = row[GermanCsv.writtenFormInfinitive];
                this.writtenForm3rdPerson = row[GermanCsv.InTransitFrameCsv.writtenForm3rdPerson];
                this.writtenFormPast = row[GermanCsv.InTransitFrameCsv.writtenFormPast];
                this.writtenFormPerfect = row[GermanCsv.InTransitFrameCsv.writtenFormPerfect];
                this.preposition = this.checkValidPreposition(row[GermanCsv.InTransitFrameCsv.preposition]);
                subject=  GermanCsv.InTransitFrameCsv.getSubjectIndex(row);

            }
            Tupples tupple = new Tupples(this.lemonEntry,
                    index + 1,
                    this.setReference(row[GermanCsv.InTransitFrameCsv.getReferenceIndex()]),
                    this.setReference(row[GermanCsv.InTransitFrameCsv.getDomainIndex()]),
                    this.setReference(row[GermanCsv.InTransitFrameCsv.getRangeIndex()]));
            tupplesList.add(tupple);
            index = index + 1;
            intransitiveFrameCsv.setNoun(tupple, this.gender, row);
            intransitiveFrameCsv.setVerbInfo(partOfSpeech, writtenFormInfinitive, writtenForm3rdPerson, writtenFormPast, writtenFormPerfect);

        }
        this.turtleString
                = intransitiveFrameCsv.getHeader(lemonEntry, TempConstants.preposition, language)
                + intransitiveFrameCsv.getSenseIndexing(tupplesList, this.lemonEntry)
                + intransitiveFrameCsv.getWritten(lemonEntry, writtenFormInfinitive, writtenForm3rdPerson, writtenFormPast, writtenFormPerfect, language,subject)
                + GermanCsv.getSenseDetail(tupplesList, TempConstants.IntransitivePPFrame, lemonEntry, writtenFormInfinitive, preposition, language)
                + intransitiveFrameCsv.getPreposition(this.lemonEntry, this.preposition, language);
        this.tutleFileName = getFileName(syntacticFrame);
    }

    @Override
    public void setAdjectiveFrame(String key, List<String[]> rows, String syntacticFrame) {
        List<Tupples> tupples = new ArrayList<Tupples>();
        Integer index = 0;
        for (String[] row : rows) {
            
            if (index == 0) {
                this.setLemonEntryId(row[GermanCsv.lemonEntryIndex]);
                this.partOfSpeech = row[GermanCsv.partOfSpeechIndex];
                this.writtenFormInfinitive = row[GermanCsv.writtenFormInfinitive];

            }

            tupples.add(new Tupples(this.lemonEntry,
                    index + 1,
                    "",
                    this.setReference(row[attributiveAdjectiveFrame.owl_onPropertyIndex]),
                    this.setReference(row[attributiveAdjectiveFrame.owl_hasValueIndex])));
            index = index + 1;
        }
        this.turtleString
                = attributiveAdjectiveFrame.getAtrributiveFrameHeader(this.lemonEntry, tupples, this.language)
                + attributiveAdjectiveFrame.getAtrributiveFrameIndexing(tupples, this.lemonEntry)
                + attributiveAdjectiveFrame.getAtrrtibutiveWrittenForm(lemonEntry, writtenFormInfinitive, this.language)
                + GermanCsv.getSenseDetail(tupples, syntacticFrame, this.lemonEntry, "", "", this.language);
        this.tutleFileName = getFileName(syntacticFrame);

    }
    
     @Override
    public void setAdjectiveGradableFrame(String key, List<String[]> rows, String syntacticFrame) {
        this.setLemonEntryId(key);

        List<Tupples> tupplesList = new ArrayList<Tupples>();
        Integer index = 0;
        for (String[] row : rows) {
            if (index == 0) {
                this.partOfSpeech = gradableAdjectiveFrameCsv.getPartOfSpeechIndex(row);
                this.writtenFormInfinitive=gradableAdjectiveFrameCsv.getWrittenFormIndex(row);
                this.writtenForm3rdPerson=gradableAdjectiveFrameCsv.getComparativIndex(row);
                this.writtenFormPast=gradableAdjectiveFrameCsv.getSuperlativeIndex(row);
                this.preposition=gradableAdjectiveFrameCsv.getPrepostion(row);

            }
            Tupples tupple = new Tupples(this.lemonEntry,
                    index + 1,
                    setReference(gradableAdjectiveFrameCsv.getReferenceIndex(row)),
                    setReference(gradableAdjectiveFrameCsv.getDomainIndex(row)),
                    setReference(gradableAdjectiveFrameCsv.getRangeIndex(row)),
                    setReference(gradableAdjectiveFrameCsv.getOils_boundToIndex(row)),
                    setReference(gradableAdjectiveFrameCsv.getOils_degreeIndex(row)));

            tupplesList.add(tupple);
            index = index + 1;
            gradableAdjectiveFrameCsv.setArticle(tupple, row);
        }
        this.turtleString
                = gradableAdjectiveFrameCsv.getHeader(this.lemonEntry, this.language)
                + gradableAdjectiveFrameCsv.getIndexing(this.lemonEntry, tupplesList)
                + gradableAdjectiveFrameCsv.getWrittenTtl(this.lemonEntry, this.writtenFormInfinitive, this.writtenForm3rdPerson,this.writtenFormPast,this.language)
                + gradableAdjectiveFrameCsv.getSenseDetail(lemonEntry, tupplesList, language)
                + gradableAdjectiveFrameCsv.getPrepostion(lemonEntry, this.preposition, language);
        this.tutleFileName = getFileName(syntacticFrame);
    }

    private String modify(String string) {
        /*string = string.replaceAll("[^a-zA-Z0-9]", " ");
        string = string.toLowerCase().strip().trim().replace(" ", "_");*/
        index = index + 1;
        //return "LexicalEntry_" + string+"_"+index.toString();
        return string;
    }

    private void setLemonEntryId(String[] row) {
        this.lemonEntry = row[GermanCsv.lemonEntryIndex];
    }

    private void setLemonEntryId(String writtenForm) {
        this.lemonEntry = this.modify(writtenForm);

    }

    private String getFileName(String syntacticFrame) {
        return syntacticFrame + "-lexicon" + "-" + lemonEntry.replace("/", "") + ".ttl";

    }

    private String checkValidPreposition(String preposition) {
        /*if(preposition.contains("X"))
          return "";*/
        return preposition;
    }

}
