package grammar.datasets.sentencetemplates;

import grammar.datasets.Factory;
import grammar.structure.component.Language;
import util.exceptions.QueGGMissingFactoryClassException;

import java.lang.reflect.InvocationTargetException;

public class SentenceTemplateFactory implements Factory<SentenceTemplateRepository> {

  private final Language language;

  public SentenceTemplateFactory(Language language) {
    this.language = language;
  }

  /**
   * Creates a SentenceTemplateRepository instance based on the input language.
   *
   * @return an instance of the {@link SentenceTemplateRepository} that can be queried for the respective question word
   * @throws QueGGMissingFactoryClassException if there is no factory class for the input language
   */
  public SentenceTemplateRepository init() throws QueGGMissingFactoryClassException {
    return this.initByLanguage(language);
  }

  private SentenceTemplateRepository initByLanguage(Language language) throws QueGGMissingFactoryClassException {
    String classNameToLoad = this.getClass().getName() + language.toString();
    SentenceTemplateRepository sentenceTemplateRepository;
    try {
      Class<?> matchingLanguageClass = Class.forName(classNameToLoad);
      Object matchingLanguageClassInst = matchingLanguageClass.getDeclaredConstructor().newInstance();
      Object repositoryInst = matchingLanguageClass.getDeclaredMethod("init").invoke(matchingLanguageClassInst);
      if (repositoryInst instanceof SentenceTemplateRepository) {
        sentenceTemplateRepository = (SentenceTemplateRepository) repositoryInst;
      } else {
        throw new ClassNotFoundException();
      }
    } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
      throw new QueGGMissingFactoryClassException(
        String.format("%s not found", classNameToLoad),
        e
      );
    }
    return sentenceTemplateRepository;
  }
}
