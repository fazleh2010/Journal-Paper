package util.exceptions;

public class QueGGLanguageNotFoundException extends Exception {
  public QueGGLanguageNotFoundException(String message) {
    super(message);
  }

  public QueGGLanguageNotFoundException(String message, Throwable err) {
    super(message, err);
  }
}
