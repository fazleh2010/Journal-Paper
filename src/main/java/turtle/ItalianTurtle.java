/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package turtle;

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
public class ItalianTurtle extends TurtleCreation implements TutleConverter {
    private static Integer index = 0;
    private ItalianCSV.NounPPFrameCsv nounPPFrameCsv = new ItalianCSV.NounPPFrameCsv();
    private ItalianCSV.TransitFrameCsv transitiveFrameCsv = new ItalianCSV.TransitFrameCsv();
    private ItalianCSV.InTransitFrameCsv intransitiveFrameCsv = new ItalianCSV.InTransitFrameCsv();
    private ItalianCSV.AttributiveAdjectiveFrameCsv attributiveAdjectiveFrame = new ItalianCSV.AttributiveAdjectiveFrameCsv();
    private ItalianCSV.GradbleAdjectiveFrameCsv gradableAdjectiveFrameCsv = new ItalianCSV.GradbleAdjectiveFrameCsv();



    public ItalianTurtle(String inputDir, LinkedData linkedData, Language language) throws Exception {
        super(inputDir, linkedData, language);
        super.setSyntacticFrameIndexes(nounPPFrameCsv.getSyntacticFrameIndex(),transitiveFrameCsv.getSyntacticFrameIndex(),intransitiveFrameCsv.getSyntacticFrameIndex(),attributiveAdjectiveFrame.getSyntacticFrameIndex(),gradableAdjectiveFrameCsv.getSyntacticFrameIndex());
        this.generateTurtle();
    }

    private void generateTurtle() throws IOException, Exception {
        
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
                    java.util.logging.Logger.getLogger(ItalianTurtle.class.getName()).log(Level.SEVERE, null, ex);
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
        String lemonEntry=this.setLemonEntryId(key);

        String partOfSpeech = "";
        String writtenForm_plural = "",writtenFormInfinitive="",preposition="";

        List<Tupples> tupplesList = new ArrayList<Tupples>();
        Integer index = 0;
        String copulativeArg = null;
        for (String[] row : rows) {
            if (index == 0) {
                partOfSpeech = nounPPFrameCsv.getPartOfSpeechIndex(row);
                writtenFormInfinitive = nounPPFrameCsv.getWrittenFormInfinitive(row);
                writtenForm_plural = nounPPFrameCsv.getWrittenFormPluralIndex(row);
                preposition = nounPPFrameCsv.getPrepositionIndex(row);
            }
            copulativeArg = nounPPFrameCsv.getCopulativeArgIndex(row);
            Tupples tupple = new Tupples(lemonEntry,
                    index + 1,
                    setReference(nounPPFrameCsv.getReferenceIndex(row)),
                    setReference(nounPPFrameCsv.getDomainIndex(row)),
                    setReference(nounPPFrameCsv.getRangeIndex(row)));
            tupplesList.add(tupple);
            index = index + 1;
            nounPPFrameCsv.setArticle(tupple, row);
        }
        this.turtleString
                = nounPPFrameCsv.getNounPPFrameHeader(lemonEntry,preposition, this.language)
                + nounPPFrameCsv.getIndexing(lemonEntry, tupplesList)
                + nounPPFrameCsv.getWrittenTtl(lemonEntry, writtenFormInfinitive, writtenFormInfinitive, writtenForm_plural, this.language, copulativeArg)
                + nounPPFrameCsv.getSenseDetail(tupplesList, NounPPFrame, lemonEntry, writtenFormInfinitive, preposition, this.language)
                + nounPPFrameCsv.getPreposition(lemonEntry, preposition, language);
        this.tutleFileName = getFileName(lemonEntry,syntacticFrame);
    }


    @Override
    public void setTransitiveFrame(String key, List<String[]> rows, String syntacticFrame) {
        String lemonEntry=this.setLemonEntryId(key);
        List<Tupples> tupples = new ArrayList<Tupples>();
        Integer index = 0;
        String subject = null;
        String partOfSpeech = "";
        String writtenForm_plural = "", writtenFormInfinitive = "", writtenForm3rdPerson = "", preposition = "", writtenFormPast = "", writtenFormPerfect = "";

        for (String[] row : rows) {
            if (index == 0) {
                partOfSpeech = row[SpanishCsv.partOfSpeechIndex];
                writtenFormInfinitive = row[SpanishCsv.writtenFormInfinitive];
                writtenForm3rdPerson = row[transitiveFrameCsv.writtenForm3rdPerson];
                writtenFormPast = row[transitiveFrameCsv.writtenFormPast];
                writtenFormPerfect = row[transitiveFrameCsv.writtenFormPerfect];
                preposition = this.checkValidPreposition(row[transitiveFrameCsv.passivePrepositionIndex]);
                subject = transitiveFrameCsv.getSubjectIndex(row);

            }
            Tupples tupple = new Tupples(lemonEntry,
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
                = transitiveFrameCsv.getHeader(lemonEntry, TempConstants.preposition, preposition, this.language)
                + transitiveFrameCsv.getSenseIndexing(tupples, lemonEntry)
                + transitiveFrameCsv.getWritten(lemonEntry, writtenFormInfinitive, writtenForm3rdPerson, writtenFormPast, writtenFormPerfect, this.language, subject)
                + SpanishCsv.getSenseDetail(tupples, TempConstants.TransitiveFrame, lemonEntry, writtenFormInfinitive, preposition, this.language)
                + transitiveFrameCsv.getPreposition(lemonEntry, preposition, this.language);
        this.tutleFileName = getFileName(lemonEntry,syntacticFrame);
    }

    @Override
    public void setIntransitivePPFrame(String key, List<String[]> rows, String syntacticFrame) {
        List<Tupples> tupplesList = new ArrayList<Tupples>();
        Integer index = 0;
         String subject= null;
          String lemonEntry = "";
        String partOfSpeech = "";
        String writtenForm_plural = "", writtenFormInfinitive = "", writtenForm3rdPerson = "", preposition = "", writtenFormPast = "", writtenFormPerfect = "";

        for (String[] row : rows) {
            if (index == 0) {
                lemonEntry=this.setLemonEntryId(row[SpanishCsv.lemonEntryIndex]);
                partOfSpeech = row[SpanishCsv.partOfSpeechIndex];
                writtenFormInfinitive = row[SpanishCsv.writtenFormInfinitive];
                writtenForm3rdPerson = row[SpanishCsv.InTransitFrameCsv.writtenForm3rdPerson];
                writtenFormPast = row[SpanishCsv.InTransitFrameCsv.writtenFormPast];
                writtenFormPerfect = row[SpanishCsv.InTransitFrameCsv.writtenFormPerfect];
                preposition = this.checkValidPreposition(row[SpanishCsv.InTransitFrameCsv.preposition]);
                subject=  SpanishCsv.InTransitFrameCsv.getSubjectIndex(row);

            }
            Tupples tupple = new Tupples(lemonEntry,
                    index + 1,
                    this.setReference(row[SpanishCsv.InTransitFrameCsv.getReferenceIndex()]),
                    this.setReference(row[SpanishCsv.InTransitFrameCsv.getDomainIndex()]),
                    this.setReference(row[SpanishCsv.InTransitFrameCsv.getRangeIndex()]));
            tupplesList.add(tupple);
            index = index + 1;
            intransitiveFrameCsv.setNoun(tupple, this.gender, row);
            intransitiveFrameCsv.setVerbInfo(partOfSpeech, writtenFormInfinitive, writtenForm3rdPerson, writtenFormPast, writtenFormPerfect);

        }
        this.turtleString
                = intransitiveFrameCsv.getHeader(lemonEntry, TempConstants.preposition, language)
                + intransitiveFrameCsv.getSenseIndexing(tupplesList, lemonEntry)
                + intransitiveFrameCsv.getWritten(lemonEntry, writtenFormInfinitive, writtenForm3rdPerson, writtenFormPast, writtenFormPerfect, language,subject)
                + SpanishCsv.getSenseDetail(tupplesList, TempConstants.IntransitivePPFrame, lemonEntry, writtenFormInfinitive, preposition, language)
                + intransitiveFrameCsv.getPreposition(lemonEntry, preposition, language);
        this.tutleFileName = getFileName(lemonEntry,syntacticFrame);
    }

    @Override
    public void setAdjectiveFrame(String key, List<String[]> rows, String syntacticFrame) {
        List<Tupples> tupples = new ArrayList<Tupples>();
        Integer index = 0;
         String lemonEntry = "";
        String partOfSpeech = "";
        String writtenForm_plural = "", writtenFormInfinitive = "";

        for (String[] row : rows) {
            
            if (index == 0) {
                lemonEntry=this.setLemonEntryId(row[SpanishCsv.lemonEntryIndex]);
                partOfSpeech = row[SpanishCsv.partOfSpeechIndex];
                writtenFormInfinitive = row[SpanishCsv.writtenFormInfinitive];

            }

            tupples.add(new Tupples(lemonEntry,
                    index + 1,
                    "",
                    this.setReference(row[attributiveAdjectiveFrame.owl_onPropertyIndex]),
                    this.setReference(row[attributiveAdjectiveFrame.owl_hasValueIndex])));
            index = index + 1;
        }
        this.turtleString
                = attributiveAdjectiveFrame.getAtrributiveFrameHeader(lemonEntry, tupples, this.language)
                + attributiveAdjectiveFrame.getAtrributiveFrameIndexing(tupples, lemonEntry)
                + attributiveAdjectiveFrame.getAtrrtibutiveWrittenForm(lemonEntry, writtenFormInfinitive, this.language)
                + SpanishCsv.getSenseDetail(tupples, syntacticFrame, lemonEntry, "", "", this.language);
        this.tutleFileName = getFileName(lemonEntry,syntacticFrame);

    }
    
     @Override
    public void setAdjectiveGradableFrame(String key, List<String[]> rows, String syntacticFrame) {
        String lemonEntry=this.setLemonEntryId(key);
        String partOfSpeech = "";
        String writtenForm_plural = "", writtenFormInfinitive = "", writtenForm3rdPerson = "", preposition = "", writtenFormPast = "", writtenFormPerfect = "";


        List<Tupples> tupplesList = new ArrayList<Tupples>();
        Integer index = 0;
        for (String[] row : rows) {
            if (index == 0) {
                partOfSpeech = gradableAdjectiveFrameCsv.getPartOfSpeechIndex(row);
                writtenFormInfinitive=gradableAdjectiveFrameCsv.getWrittenFormIndex(row);
                writtenForm3rdPerson=gradableAdjectiveFrameCsv.getComparativIndex(row);
                writtenFormPast=gradableAdjectiveFrameCsv.getSuperlativeIndex(row);
                preposition=gradableAdjectiveFrameCsv.getPrepostion(row);

            }
            Tupples tupple = new Tupples(lemonEntry,
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
                = gradableAdjectiveFrameCsv.getHeader(lemonEntry, this.language)
                + gradableAdjectiveFrameCsv.getIndexing(lemonEntry, tupplesList)
                + gradableAdjectiveFrameCsv.getWrittenTtl(lemonEntry, writtenFormInfinitive, writtenForm3rdPerson,writtenFormPast,this.language)
                + gradableAdjectiveFrameCsv.getSenseDetail(lemonEntry, tupplesList, language)
                + gradableAdjectiveFrameCsv.getPrepostion(lemonEntry, preposition, language);
        this.tutleFileName = getFileName(lemonEntry,syntacticFrame);
    }

    private String modify(String string) {
        /*string = string.replaceAll("[^a-zA-Z0-9]", " ");
        string = string.toLowerCase().strip().trim().replace(" ", "_");*/
        index = index + 1;
        //return "LexicalEntry_" + string+"_"+index.toString();
        return string;
    }

    private String setLemonEntryId(String[] row) {
        return row[SpanishCsv.lemonEntryIndex];
    }

    private String setLemonEntryId(String writtenForm) {
        return this.modify(writtenForm);

    }

    private String getFileName(String lemonEntry,String syntacticFrame) {
        return syntacticFrame + "-lexicon" + "-" + lemonEntry.replace("/", "") + ".ttl";

    }

    private String checkValidPreposition(String preposition) {
        /*if(preposition.contains("X"))
          return "";*/
        return preposition;
    }

}
