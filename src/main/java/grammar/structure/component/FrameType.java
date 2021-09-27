package grammar.structure.component;

import eu.monnetproject.lemon.model.SynArg;
import grammar.generator.APPGrammarRuleGenerator;
import grammar.generator.AdjAttrGrammarRuleGenerator;
import grammar.generator.GrammarRuleGeneratorRootImpl;
import grammar.generator.IntransitivePPGrammarRuleGenerator;
import grammar.generator.NPPGrammarRuleGenerator;
import grammar.generator.TransitiveVPGrammarRuleGenerator;
import net.lexinfo.LexInfo;
import util.io.GoogleXslSheet;
import grammar.generator.helper.datasets.sentencetemplates.TemplateConstants;

public enum FrameType {
  NPP(TemplateConstants.NOUN_PP_FRAME, new LexInfo().getSynArg("copulativeArg"), NPPGrammarRuleGenerator.class),
  VP(TemplateConstants.TRANSITIVE_FRAME, new LexInfo().getSynArg("subject"), TransitiveVPGrammarRuleGenerator.class),
  AA(TemplateConstants.ADJECTIVE_ATTRIBUTIVE_FRAME, new LexInfo().getSynArg("attributiveArg"), AdjAttrGrammarRuleGenerator.class),
  APP(TemplateConstants.ADJECTIVE_PP_FRAME, new LexInfo().getSynArg("copulativeSubject"), APPGrammarRuleGenerator.class),
  IPP(TemplateConstants.IN_TRANSITIVE_PP_FRAME, new LexInfo().getSynArg("subject"), IntransitivePPGrammarRuleGenerator.class),
  FULL_DATASET(GoogleXslSheet.FULL_DATASET, null, GrammarRuleGeneratorRootImpl.class);

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
