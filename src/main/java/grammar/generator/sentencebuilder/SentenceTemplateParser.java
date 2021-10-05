package grammar.generator;

import grammar.datasets.sentencetemplates.SentenceTemplateRepository;
import grammar.structure.component.Language;
import grammar.structure.component.SentenceType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

public class SentenceTemplateParser {
  private static final Logger LOG = LogManager.getLogger(SentenceTemplateParser.class);

  public static final String QUESTION_MARK = "?";
  private final Language language;
  private final SentenceTemplateRepository sentenceTemplateRepository;

  public SentenceTemplateParser(
    Language language,
    SentenceTemplateRepository sentenceTemplateRepository
  ) {
    this.language = language;
    this.sentenceTemplateRepository = sentenceTemplateRepository;
  }

  public List<List<SentenceToken>> parseAndResolveNestedSentenceTemplates(String sentenceTemplate) {
    List<String> resolvedSentenceTemplates = resolveNestedSentenceTemplates(sentenceTemplate);
    return parseResolvedSentenceTemplates(resolvedSentenceTemplates);
  }

  public List<List<SentenceToken>> parseAndResolveMultipleNestedSentenceTemplates(List<String> sentenceTemplates) {
    List<List<SentenceToken>> resultList = new ArrayList<>();
    sentenceTemplates.parallelStream().forEach(sentenceTemplate -> {
      List<String> resolvedSentenceTemplates = resolveNestedSentenceTemplates(sentenceTemplate);
      resultList.addAll(parseResolvedSentenceTemplates(resolvedSentenceTemplates));
    });
    return resultList;
  }

  private List<List<SentenceToken>> parseResolvedSentenceTemplates(List<String> resolvedSentenceTemplates) {
    List<List<SentenceToken>> resultList = new ArrayList<>();
    for (String sentenceTemplate : resolvedSentenceTemplates) {
      resultList.add(parse(sentenceTemplate));
    }
    return resultList;
  }

  private List<String> resolveNestedSentenceTemplates(String sentenceTemplate) {
    List<String> resultList = new ArrayList<>();
    List<SentenceToken> sentenceTokens = parse(sentenceTemplate);
    List<SentenceToken> nestedTemplate = sentenceTokens.parallelStream()
                                                       .filter(SentenceToken::isNestedSentenceTemplate)
                                                       .collect(Collectors.toList());
    for (SentenceToken sentenceToken : nestedTemplate) {
      SentenceType sentenceType = sentenceToken.getNestedSentenceTemplateData().getType();
      String[] arguments = sentenceToken.getNestedSentenceTemplateData().getArguments();
      if (!isNull(sentenceType) && !isNull(arguments)) {
        List<String> nestedSentenceTemplates =
          sentenceTemplateRepository.findOneByEntryTypeAndLanguageAndArguments(sentenceType, language, arguments);

        // parse sentence template string to tokens - for token replacement
        List<String> tokens = Arrays.stream(getSentenceTemplateTokens(sentenceTemplate)).collect(Collectors.toList());

        for (String nestedSentenceTemplate : nestedSentenceTemplates) {

          // replace nested token by the retrieved sentence template
          tokens.remove(sentenceToken.getPosition());
          tokens.add(sentenceToken.getPosition(), nestedSentenceTemplate);
          // create new sentence template string with replaced nested token
          nestedSentenceTemplate = tokens.stream().reduce((x, y) -> x + " " + y)
                                         .orElse(nestedSentenceTemplate);

          // recursive call to resolveNestedSentenceTemplates
          List<String> resolvedSentenceTemplate = resolveNestedSentenceTemplates(nestedSentenceTemplate);
          // only add to resultList if resolvedSentenceTemplate is new
          if (!resultList.containsAll(resolvedSentenceTemplate)) {
            resultList.addAll(resolvedSentenceTemplate);
          }
        }
      }
    }
    if (resultList.isEmpty() && !sentenceTokens.isEmpty()) {
      resultList.add(sentenceTemplate);
    }
    return resultList;
  }

  private List<SentenceToken> parse(String sentenceTemplate) {
    List<SentenceToken> sentenceTokens = new ArrayList<>();
    // Remove question mark for token parsing. We will need to re-add it when we actually build a sentence.
    if (sentenceTemplate.endsWith(QUESTION_MARK)) {
      sentenceTemplate = sentenceTemplate.replace(QUESTION_MARK, "");
    }
    String[] tokens = getSentenceTemplateTokens(sentenceTemplate);
    for (int i = 0; i < tokens.length; i++) {
      sentenceTokens.add(new SentenceTokenImpl(tokens[i], i));
    }
    return sentenceTokens;
  }

  private String[] getSentenceTemplateTokens(String sentenceTemplate) {
    return sentenceTemplate.split("\\s");
  }
}
