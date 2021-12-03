package grammar.datasets.questionword;

import grammar.datasets.Factory;
import grammar.structure.component.Language;
import util.exceptions.QueGGMissingFactoryClassException;

import java.lang.reflect.InvocationTargetException;

public class QuestionWordFactory implements Factory<QuestionWordRepository> {

  private final Language language;

  public QuestionWordFactory(Language language) {
    this.language = language;
  }

  /**
   * Creates a QuestionWordRepository instance based on the input language.
   *
   * @return an instance of the {@link QuestionWordRepository} that can be queried for the respective question word
   * @throws QueGGMissingFactoryClassException if there is no factory class for the input language
   */
  public QuestionWordRepository init() throws QueGGMissingFactoryClassException {
    return this.initByLanguage(language);
  }

  private QuestionWordRepository initByLanguage(Language language) throws QueGGMissingFactoryClassException {
    String classNameToLoad = this.getClass().getName() + language.toString();
    QuestionWordRepository questionWordRepository;
    try {
      Class<?> matchingLanguageClass = Class.forName(classNameToLoad);
      Object matchingLanguageClassInst = matchingLanguageClass.getDeclaredConstructor().newInstance();
      Object repositoryInst = matchingLanguageClass.getDeclaredMethod("init").invoke(matchingLanguageClassInst);
      if (repositoryInst instanceof QuestionWordRepository) {
        questionWordRepository = (QuestionWordRepository) repositoryInst;
      } else {
        throw new ClassNotFoundException();
      }
    } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
      throw new QueGGMissingFactoryClassException(
        String.format("%s not found", classNameToLoad),
        e
      );
    }
    return questionWordRepository;
  }
}
