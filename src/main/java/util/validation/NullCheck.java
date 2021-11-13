package util.validation;

import util.exceptions.QueGGMissingFieldDeclarationException;

import static java.util.Objects.isNull;

public class NullCheck {
  public static void notNull(String objectName, Object object, Class<?> clazz) throws
                                                                               QueGGMissingFieldDeclarationException {
    if (isNull(object)) {
      throw new QueGGMissingFieldDeclarationException(String.format("%s must be set in %s", objectName, clazz.getName()));
    }
  }
}
