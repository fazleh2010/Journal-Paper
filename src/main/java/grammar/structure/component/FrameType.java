package grammar.structure.component;

import eu.monnetproject.lemon.model.SynArg;
import grammar.datasets.sentencetemplates.TempConstants;
import grammar.generator.APPGrammarRuleGenerator;
import grammar.generator.AdjAttrGrammarRuleGenerator;
import grammar.generator.AdjGradableGrammarRuleGenerator;
import grammar.generator.GrammarRuleGeneratorRootImpl;
import grammar.generator.IntransitivePPGrammarRuleGenerator;
import grammar.generator.NPPGrammarRuleGenerator;
import grammar.generator.TransitiveVPGrammarRuleGenerator;
import net.lexinfo.LexInfo;
import turtle.GermanCsv;

public enum FrameType {
  NPP(TempConstants.NounPPFrame, new LexInfo().getSynArg("copulativeArg"), NPPGrammarRuleGenerator.class),
  VP(TempConstants.TransitiveFrame, new LexInfo().getSynArg("subject"), TransitiveVPGrammarRuleGenerator.class),
  AA(TempConstants.AdjectiveAttributiveFrame, new LexInfo().getSynArg("attributiveArg"), AdjAttrGrammarRuleGenerator.class),
  APP(TempConstants.AdjectivePPFrame, new LexInfo().getSynArg("copulativeSubject"), APPGrammarRuleGenerator.class),
  AG(TempConstants.AdjectiveSuperlativeFrame, new LexInfo().getSynArg("copulativeSubject"), AdjGradableGrammarRuleGenerator.class),
  IPP(TempConstants.IntransitivePPFrame, new LexInfo().getSynArg("subject"), IntransitivePPGrammarRuleGenerator.class),
  FULL_DATASET(TempConstants.FULL_DATASET, null, GrammarRuleGeneratorRootImpl.class);

  private final String name;
  private final SynArg selectVariableSynArg;
  private final Class<?> implementingClass;


  FrameType(String name, SynArg subjectEquivalentSynArg, Class<?> implementingClass) {
    this.name = name;
    this.selectVariableSynArg = subjectEquivalentSynArg;
    this.implementingClass = implementingClass;
  }

  public String getName() {
    return name;
  }

  public SynArg getSubjectEquivalentSynArg() {
    return selectVariableSynArg;
  }

  public Class<?> getImplementingClass() {
    return implementingClass;
  }
}
