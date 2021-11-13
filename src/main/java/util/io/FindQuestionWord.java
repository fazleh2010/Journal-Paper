/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.io;

import grammar.datasets.annotated.AnnotatedNounOrQuestionWord;
import grammar.datasets.annotated.AnnotatedVerb;
import grammar.datasets.questionword.QuestionWordFactoryIT;
import static grammar.datasets.sentencetemplates.TemplateVariable.PLURAL;
import static grammar.datasets.sentencetemplates.TemplateVariable.SLASH;
import grammar.datasets.sentencetemplates.TemplateVariable;
import grammar.generator.SentenceToken;
import grammar.generator.SubjectType;
import grammar.sparql.SelectVariable;
import grammar.structure.component.DomainOrRangeType;
import grammar.structure.component.Language;
import static java.lang.System.exit;
import java.util.List;
import java.util.Map;
import static java.util.Objects.isNull;
import java.util.Optional;
import java.util.TreeMap;
import lexicon.LexicalEntryUtil;
import static lexicon.LexicalEntryUtil.getDeterminerTokenByNumber;
import util.exceptions.QueGGMissingFactoryClassException;
import static grammar.datasets.sentencetemplates.TemplateVariable.singular;

/**
 *
 * @author elahi
 */
public class FindQuestionWord {

  
}
