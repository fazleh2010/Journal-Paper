package util.logging;

public class LoggerHelper {
  /**
   * Produces output string:
   * ------ name ------
   * content
   * ------------------
   */
  public static String sepString(String name, String content) {
    String sepPart = "-".repeat(50);
    return String.format("%s %s %s\n%s\n%s", sepPart, name, sepPart, content, sepPart.repeat(2));
  }
}
