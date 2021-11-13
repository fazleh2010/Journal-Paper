package grammar.structure.component;

import eu.monnetproject.lemon.model.SynArg;
import grammar.datasets.sentencetemplates.TemplateVariable;
import grammar.generator.APPGrammarRuleGenerator;
import grammar.generator.AdjAttrGrammarRuleGenerator;
import grammar.generator.GrammarRuleGeneratorRootImpl;
import grammar.generator.IntransitivePPGrammarRuleGenerator;
import grammar.generator.NPPGrammarRuleGenerator;
import grammar.generator.TransitiveVPGrammarRuleGenerator;
import net.lexinfo.LexInfo;
import util.io.GoogleXslSheet;

public enum FrameType {
  NPP(TemplateVariable.NounPPFrame, new LexInfo().getSynArg("copulativeArg"), NPPGrammarRuleGenerator.class),
  VP(TemplateVariable.TransitiveFrame, new LexInfo().getSynArg("subject"), TransitiveVPGrammarRuleGenerator.class),
  AA(TemplateVariable.AdjectiveAttributiveFrame, new LexInfo().getSynArg("attributiveArg"), AdjAttrGrammarRuleGenerator.class),
  APP(TemplateVariable.AdjectivePPFrame, new LexInfo().getSynArg("copulativeSubject"), APPGrammarRuleGenerator.class),
  IPP(TemplateVariable.IntransitivePPFrame, new LexInfo().getSynArg("subject"), IntransitivePPGrammarRuleGenerator.class),
  FULL_DATASET(TemplateVariable.FULL_DATASET, null, GrammarRuleGeneratorRootImpl.class);

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
