/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package turtle;

import static grammar.datasets.sentencetemplates.TempConstants.AdjectiveAttributiveFrame;
import static grammar.datasets.sentencetemplates.TempConstants.IntransitivePPFrame;
import static grammar.datasets.sentencetemplates.TempConstants.NounPPFrame;
import static grammar.datasets.sentencetemplates.TempConstants.TransitiveFrame;
import grammar.structure.component.Language;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import util.io.CsvFile;
import util.io.FileUtils;
import util.io.LinkedData;
import util.io.Tupples;

/**
 *
 * @author elahi
 */
public class EnglishTurtle extends TurtleCreation implements TutleConverter {

    private String lemonEntry = "";
    private String partOfSpeech = "";
    private String writtenForm_plural = "";
    private String writtenFormInfinitive = "";
    private String writtenForm3rdPerson = "";
    private String writtenFormPast = "";
    private String writtenFormPerfect = "";
    private String preposition = "";
    private static Integer index = 0;
    private EnglishCsv.NounPPFrameCsv nounPPFrameCsv = new EnglishCsv.NounPPFrameCsv();
    private EnglishCsv.TransitFrameCsv transitiveFrameCsv = new EnglishCsv.TransitFrameCsv();
    private EnglishCsv.InTransitFrame IntransitiveFrameCsv = new EnglishCsv.InTransitFrame();
    private EnglishCsv.AttributiveAdjectiveFrame attributiveAdjectiveFrame = new EnglishCsv.AttributiveAdjectiveFrame();

    public EnglishTurtle(String inputDir, LinkedData linkedData, Language language) throws Exception {
        super(inputDir, linkedData, language);
        super.setSyntacticFrameIndexes(nounPPFrameCsv.getSyntacticFrameIndex(),transitiveFrameCsv.getSyntacticFrameIndex(),IntransitiveFrameCsv.getSyntacticFrameIndex(),attributiveAdjectiveFrame.getSyntacticFrameIndex());
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
                        FileUtils.stringToFile(this.turtleString, directory + this.tutleFileName);
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
        String syntacticFrame = findSyntacticFrame(rows);
        key=key.trim().strip().stripLeading().stripTrailing();
        System.out.println(key);

        if (syntacticFrame.equals(NounPPFrame)) {
            setNounPPFrame(key, rows, syntacticFrame);
        } else if (syntacticFrame.equals(TransitiveFrame)) {
            setTransitiveFrame(key, rows, syntacticFrame);
        } else if (syntacticFrame.equals(IntransitivePPFrame)) {
            setIntransitivePPFrame(key, rows, syntacticFrame);
        } else if (syntacticFrame.equals(AdjectiveAttributiveFrame)) {
            setAdjectiveFrame(key, rows, syntacticFrame);
        } else {
            System.out.println("no syntactic frame is found!!");
            
            //syntacticFrame = row[GoogleXslSheet.TransitFrameSyntacticFrameIndex];
        }

    }

    @Override
    public void setNounPPFrame(String key, List<String[]> rows, String syntacticFrame) {
        this.setLemonEntryId(key);

        List<Tupples> tupplesList = new ArrayList<Tupples>();
        Integer index = 0;String copulativeArg=null;
        for (String[] row : rows) {
            if (index == 0) {
                this.partOfSpeech = nounPPFrameCsv.getPartOfSpeechIndex(row);
                this.writtenFormInfinitive = nounPPFrameCsv.getWrittenFormInfinitive(row);
                this.writtenForm_plural = nounPPFrameCsv.getWrittenFormPluralIndex(row);
                this.preposition = nounPPFrameCsv.getPrepositionIndex(row);
            }
            copulativeArg=nounPPFrameCsv.getCopulativeArgIndex(row);
            Tupples tupple = new Tupples(this.lemonEntry,
                    index + 1,
                    setReference(nounPPFrameCsv.getReferenceIndex(row)),
                    setReference(nounPPFrameCsv.getDomainIndex(row)),
                    setReference(nounPPFrameCsv.getRangeIndex(row)));
            tupplesList.add(tupple);
            index = index + 1;
             nounPPFrameCsv.setArticle(tupple, row);
        }
        this.turtleString
                = nounPPFrameCsv.getNounPPFrameHeader(this.lemonEntry, this.preposition, this.language)
                + nounPPFrameCsv.getIndexing(this.lemonEntry, tupplesList)
                + nounPPFrameCsv.getWrittenTtl(this.lemonEntry, this.writtenFormInfinitive, this.writtenFormInfinitive,this.writtenForm_plural,this.language,copulativeArg)
                + nounPPFrameCsv.getSenseDetail(tupplesList, NounPPFrame, this.lemonEntry, this.writtenFormInfinitive, this.preposition, this.language)
                + nounPPFrameCsv.getPreposition(this.lemonEntry,this.preposition, language);
        this.tutleFileName = getFileName(syntacticFrame);
    }

    @Override
    public void setTransitiveFrame(String key, List<String[]> rows, String syntacticFrame) {
        this.setLemonEntryId(key);
        List<Tupples> tupples = new ArrayList<Tupples>();
         String subject=null;
         
        Integer index = 0;
        for (String[] row : rows) {
            if (index == 0) {
                this.partOfSpeech = transitiveFrameCsv.getPartOfSpeechIndex(row);
                this.writtenFormInfinitive = transitiveFrameCsv.getWrittenFormInfinitive(row);
                this.writtenForm3rdPerson = transitiveFrameCsv.getWrittenForm3rdPerson(row);
                this.writtenFormPast = transitiveFrameCsv.getWrittenFormPast(row);
                this.writtenFormPerfect = transitiveFrameCsv.getWrittenFormPerfect(row);
                this.preposition = transitiveFrameCsv.getPassivePrepositionIndex(row);
            }
            
            Tupples tupple = new Tupples(this.lemonEntry,
                    index + 1,
                    this.setReference(transitiveFrameCsv.getReferenceIndex(row)),
                    this.setReference(transitiveFrameCsv.getDomainIndex(row)),
                    this.setReference(transitiveFrameCsv.getRangeIndex(row)));

            transitiveFrameCsv.setArticle(tupple, row);
            transitiveFrameCsv.setVerbInfo(partOfSpeech,  writtenFormInfinitive,  writtenForm3rdPerson,  writtenFormPast,writtenFormPerfect);
            tupples.add(tupple);
            index = index + 1;
            subject=  transitiveFrameCsv.getSubjectIndex(row);

        }
        this.turtleString
                = transitiveFrameCsv.getHeader(this.lemonEntry, this.preposition, this.language)
                + transitiveFrameCsv.getSenseIndexing(tupples, lemonEntry)
                + transitiveFrameCsv.getWritten(this.lemonEntry, this.partOfSpeech,this.writtenFormInfinitive, this.writtenForm3rdPerson, this.writtenFormPast, this.writtenFormPerfect,this.language,subject)
                + transitiveFrameCsv.getSenseDetail(tupples, syntacticFrame, this.lemonEntry, this.writtenFormInfinitive, this.preposition, this.language)
                + transitiveFrameCsv.getPrepostion(this.lemonEntry,this.preposition, this.language);
        this.tutleFileName = getFileName(syntacticFrame);
    }

    @Override
    public void setIntransitivePPFrame(String key, List<String[]> rows, String syntacticFrame) {
        List<Tupples> tupples = new ArrayList<Tupples>();
        Integer index = 0;String   subject= null;
        for (String[] row : rows) {
            if (index == 0) {
                this.setLemonEntryId(IntransitiveFrameCsv.getLemonEntryIndex(row));
                this.partOfSpeech = IntransitiveFrameCsv.getPartOfSpeechIndex(row);
                this.writtenFormInfinitive = IntransitiveFrameCsv.getWrittenFormInfinitive(row);
                this.writtenForm3rdPerson = IntransitiveFrameCsv.getWrittenForm3rdPerson(row);
                this.writtenFormPast = IntransitiveFrameCsv.getWrittenFormPast(row);
                this.writtenFormPerfect = IntransitiveFrameCsv.getWrittenFormPerfect(row);
                this.preposition = IntransitiveFrameCsv.getPreposition(row);

            }
            Tupples tupple = new Tupples(this.lemonEntry,
                    index + 1,
                    this.setReference(IntransitiveFrameCsv.getReferenceIndex(row)),
                    this.setReference(IntransitiveFrameCsv.getDomainIndex(row)),
                    this.setReference(IntransitiveFrameCsv.getRangeIndex(row)));

            IntransitiveFrameCsv.setArticle(tupple, row);
            IntransitiveFrameCsv.setVerbInfo(partOfSpeech,  writtenFormInfinitive,  writtenForm3rdPerson,  writtenFormPast, this.writtenFormPerfect);
            tupples.add(tupple);
            subject=  GermanCsv.InTransitFrameCsv.getSubjectIndex(row);
            index = index + 1;
        }
        this.turtleString
                = IntransitiveFrameCsv.getHeader(this.lemonEntry, this.preposition, this.language)
                + IntransitiveFrameCsv.getSenseIndexing(tupples, this.lemonEntry)
                + IntransitiveFrameCsv.getWritten(lemonEntry, writtenFormInfinitive, writtenForm3rdPerson, writtenFormPast,this.writtenFormPerfect, this.language,subject)
                + IntransitiveFrameCsv.getSenseDetail(tupples, syntacticFrame, this.lemonEntry, this.writtenFormInfinitive, this.preposition, this.language)
                + IntransitiveFrameCsv.getPrepostion(this.lemonEntry,this.preposition, this.language);
        this.tutleFileName = getFileName(syntacticFrame);
    }

    @Override
    public void setAdjectiveFrame(String key, List<String[]> rows, String syntacticFrame) {
        List<Tupples> tupples = new ArrayList<Tupples>();
        Integer index = 0;
        for (String[] row : rows) {
            if (index == 0) {
                this.setLemonEntryId(attributiveAdjectiveFrame.getLemonEntryIndex(row));
                this.partOfSpeech = attributiveAdjectiveFrame.getPartOfSpeechIndex(row);
                this.writtenFormInfinitive = attributiveAdjectiveFrame.getWrittenFormInfinitive(row);

            }

            tupples.add(new Tupples(this.lemonEntry,
                    index + 1,
                    "",
                    this.setReference(attributiveAdjectiveFrame.getOwl_onPropertyIndex(row)),
                    this.setReference(attributiveAdjectiveFrame.getOwl_hasValueIndex(row))));
            index = index + 1;
        }
        this.turtleString
                = attributiveAdjectiveFrame.getAtrributiveFrameHeader(this.lemonEntry, tupples, this.language)
                + attributiveAdjectiveFrame.getAtrributiveFrameIndexing(tupples, this.lemonEntry)
                + attributiveAdjectiveFrame.getAtrrtibutiveWrittenForm(lemonEntry, writtenFormInfinitive, this.language)
                + attributiveAdjectiveFrame.getSenseDetail(tupples, syntacticFrame, this.lemonEntry, "", "", this.language);
        this.tutleFileName = getFileName(syntacticFrame);
    }


    private String modify(String string) {
        /*string = string.replaceAll("[^a-zA-Z0-9]", " ");
        string = string.toLowerCase().strip().trim().replace(" ", "_");*/
        index = index + 1;
        //return "LexicalEntry_" + string+"_"+index.toString();
        return string;
    }

    private void setLemonEntryId(String writtenForm) {
        this.lemonEntry = this.modify(writtenForm);

    }

    private String getFileName(String syntacticFrame) {
        return syntacticFrame + "-lexicon" + "-" + lemonEntry.replace("/", "") + ".ttl";

    }

}
