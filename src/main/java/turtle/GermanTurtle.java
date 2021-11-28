/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package turtle;

import turtle.TurtleConverterImpl;
import turtle.TutleConverter;
import grammar.datasets.sentencetemplates.TempConstants;
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
public class GermanTurtle extends TurtleConverterImpl implements TutleConverter {

    private String lemonEntry = "";
    private String partOfSpeech = "";
    private String gender = "";
    private String writtenForm1 = "";
    private String writtenForm2 = "";
    private String writtenForm4 = "";
    private String writtenForm5 = "";
    private String writtenForm6 = "";
    private String writtenForm3rdPerson = "";
    private String writtenForm3 = "";
    private String writtenFormPerfect = "";
    private String preposition = "";
    private static Integer index = 0;

    public GermanTurtle(String inputDir, LinkedData linkedData, Language language) throws Exception {
        super(inputDir, linkedData, language);
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
                        System.out.println("key:" + key + " count:" + count);
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
        String syntacticFrame = super.findSyntacticFrame(rows);
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
        Integer index = 0;
        for (String[] row : rows) {
            if (index == 0) {
                this.partOfSpeech = row[GermanCsv.NounPPFrameCsv.getPartOfSpeechIndex()];
                this.gender = row[GermanCsv.NounPPFrameCsv.getGenderIndex()];
                this.writtenForm1 = row[GermanCsv.NounPPFrameCsv.getWrittenFormSingularIndex()];
                this.writtenForm2 = row[GermanCsv.NounPPFrameCsv.getWrittenFormPluraIndex()];
                this.writtenForm4 = row[GermanCsv.NounPPFrameCsv.getWrittenFormAccusativeIndex()];
                this.writtenForm5 = row[GermanCsv.NounPPFrameCsv.getWrittenFormDativeIndex()];
                this.writtenForm6 = row[GermanCsv.NounPPFrameCsv.getWrittenFormGenetiveIndex()];
                this.preposition = row[GermanCsv.NounPPFrameCsv.getPrepositionIndex()];
            }
            Tupples tupple = new Tupples(this.lemonEntry,
                    index + 1,
                    setReference(row[GermanCsv.NounPPFrameCsv.getReferenceIndex()]),
                    setReference(row[GermanCsv.NounPPFrameCsv.getDomainIndex()]),
                    setReference(row[GermanCsv.NounPPFrameCsv.getRangeIndex()]));
            tupplesList.add(tupple);
            index = index + 1;
            GermanCsv.NounPPFrameCsv.setArticle(tupple, this.gender, row);
        }
        this.turtleString
                = GermanCsv.NounPPFrameCsv.getNounPPFrameHeader(this.lemonEntry, TempConstants.preposition, this.language)
                + GermanCsv.NounPPFrameCsv.getIndexing(this.lemonEntry, tupplesList)
                + GermanCsv.NounPPFrameCsv.getWrittenFormAll(this.lemonEntry, this.gender, this.writtenForm1, this.writtenForm2, writtenForm4, this.writtenForm5, this.writtenForm6, this.language)
                + GermanCsv.getSenseDetail(tupplesList, TempConstants.NounPPFrame, this.lemonEntry, this.writtenForm1, this.preposition, this.language)
                + GermanCsv.NounPPFrameCsv.getPreposition(TempConstants.preposition, this.preposition, language);
        this.tutleFileName = getFileName(syntacticFrame);
    }

    @Override
    public void setTransitiveFrame(String key, List<String[]> rows, String syntacticFrame) {
        this.setLemonEntryId(key);
        List<Tupples> tupples = new ArrayList<Tupples>();
        Integer index = 0;
        for (String[] row : rows) {
            if (index == 0) {
                this.partOfSpeech = row[GermanCsv.partOfSpeechIndex];
                this.writtenForm1 = row[GermanCsv.writtenFormInfinitive];
                this.writtenForm3rdPerson = row[GermanCsv.TransitFrameCsv.writtenForm3rdPerson];
                this.writtenForm3 = row[GermanCsv.TransitFrameCsv.writtenFormPast];
                this.preposition = row[GermanCsv.TransitFrameCsv.passivePrepositionIndex];
            }
            Tupples tupple = new Tupples(this.lemonEntry,
                    index + 1,
                    this.setReference(row[GermanCsv.TransitFrameCsv.referenceIndex]),
                    this.setReference(row[GermanCsv.TransitFrameCsv.domainIndex]),
                    this.setReference(row[GermanCsv.TransitFrameCsv.rangeIndex]));

            GermanCsv.TransitFrameCsv.setArticle(tupple, this.gender, row);
            tupples.add(tupple);
            index = index + 1;

        }
        this.turtleString
                = GermanCsv.TransitFrameCsv.getHeader(this.lemonEntry, TempConstants.preposition, this.preposition, this.language)
                + GermanCsv.TransitFrameCsv.getSenseIndexing(tupples, lemonEntry)
                + GermanCsv.TransitFrameCsv.getWritten(this.lemonEntry, this.writtenForm1, this.writtenForm3rdPerson, this.writtenForm3, this.language)
                + GermanCsv.getSenseDetail(tupples, TempConstants.TransitiveFrame, this.lemonEntry, this.writtenForm1, this.preposition, this.language)
                + GermanCsv.getPrepostion(TempConstants.preposition, this.preposition, this.language);
        this.tutleFileName = getFileName(syntacticFrame);
    }

    @Override
    public void setIntransitivePPFrame(String key, List<String[]> rows, String syntacticFrame) {
        List<Tupples> tupplesList = new ArrayList<Tupples>();
        Integer index = 0;
        for (String[] row : rows) {
            if (index == 0) {
                this.setLemonEntryId(row[GermanCsv.lemonEntryIndex]);
                this.partOfSpeech = row[GermanCsv.partOfSpeechIndex];
                this.writtenForm1 = row[GermanCsv.writtenFormInfinitive];
                this.writtenForm3rdPerson = row[GermanCsv.InTransitFrameCsv.writtenForm3rdPerson];
                this.writtenForm3 = row[GermanCsv.InTransitFrameCsv.writtenFormPast];
                this.writtenFormPerfect = row[GermanCsv.InTransitFrameCsv.writtenFormPerfect];
                this.preposition = row[GermanCsv.InTransitFrameCsv.preposition];

            }
            Tupples tupple = new Tupples(this.lemonEntry,
                    index + 1,
                    this.setReference(row[GermanCsv.InTransitFrameCsv.getReferenceIndex()]),
                    this.setReference(row[GermanCsv.InTransitFrameCsv.getDomainIndex()]),
                    this.setReference(row[GermanCsv.InTransitFrameCsv.getRangeIndex()]));
            tupplesList.add(tupple);
            index = index + 1;
            GermanCsv.InTransitFrameCsv.setArticle(tupple, this.gender, row);
            GermanCsv.InTransitFrameCsv.setVerbInfo(partOfSpeech, writtenForm1, writtenForm3rdPerson, writtenForm3, writtenFormPerfect);

        }
        this.turtleString
                = GermanCsv.InTransitFrameCsv.getHeader(lemonEntry, TempConstants.preposition, language)
                + GermanCsv.InTransitFrameCsv.getSenseIndexing(tupplesList, this.lemonEntry)
                + GermanCsv.InTransitFrameCsv.getWritten(lemonEntry, writtenForm1, writtenForm3rdPerson, writtenForm3, writtenFormPerfect, language)
                + GermanCsv.getSenseDetail(tupplesList, TempConstants.IntransitivePPFrame, lemonEntry, writtenForm1, preposition, language)
                + GermanCsv.getPrepostion(TempConstants.preposition, preposition, language);
        this.tutleFileName = getFileName(syntacticFrame);
    }

    @Override
    public void setAdjectiveFrame(String key, List<String[]> rows, String syntacticFrame) {
        List<Tupples> tupples = new ArrayList<Tupples>();
        Integer index = 0;
        for (String[] row : rows) {
            System.out.println(row[GermanCsv.lemonEntryIndex]);
            System.out.println(row[GermanCsv.partOfSpeechIndex]);
            System.out.println(row[GermanCsv.writtenFormInfinitive]);
            if (index == 0) {
                this.setLemonEntryId(row[GermanCsv.lemonEntryIndex]);
                this.partOfSpeech = row[GermanCsv.partOfSpeechIndex];
                this.writtenForm1 = row[GermanCsv.writtenFormInfinitive];

            }

            tupples.add(new Tupples(this.lemonEntry,
                    index + 1,
                    "",
                    this.setReference(row[GermanCsv.AttributiveAdjectiveFrameCsv.owl_onPropertyIndex]),
                    this.setReference(row[GermanCsv.AttributiveAdjectiveFrameCsv.owl_hasValueIndex])));
            index = index + 1;
        }
        System.out.println(index + "  tupples:" + tupples.size());
        this.turtleString
                = GermanCsv.AttributiveAdjectiveFrameCsv.getAtrributiveFrameHeader(this.lemonEntry, tupples, this.language)
                + GermanCsv.AttributiveAdjectiveFrameCsv.getAtrributiveFrameIndexing(tupples, this.lemonEntry)
                + GermanCsv.AttributiveAdjectiveFrameCsv.getAtrrtibutiveWrittenForm(lemonEntry, writtenForm1, this.language)
                + GermanCsv.getSenseDetail(tupples, syntacticFrame, this.lemonEntry, "", "", this.language);
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

}
