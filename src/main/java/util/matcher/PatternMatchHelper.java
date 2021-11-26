package util.matcher;

import static java.lang.System.exit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Objects.isNull;

public class PatternMatchHelper {
  private static final Logger LOG = LogManager.getLogger(PatternMatchHelper.class);

  /**
   * Returns a match for the specified pattern inside the matchTarget.
   * The returned string will be the match in the group with index 1.
   *
   * @param matchTarget The string that will be checked for pattern occurrence.
   * @param pattern     The pattern that will be searched in the matchTarget.
   * @return The first item that was matched and is contained in the specified group or "" if there was no match.
   */
  public static String getPatternMatch(String matchTarget, Pattern pattern) {
     // System.out.println("matchTarget:::"+matchTarget+" pattern:"+pattern);
    return getPatternMatch(matchTarget, pattern, "", 1);
  }

  /**
   * Returns a match for the specified pattern inside the matchTarget.
   * The returned string will be the match of the specified named or indexed group.
   *
   * @param matchTarget The string that will be checked for pattern occurrence.
   * @param pattern     The pattern that will be searched in the matchTarget.
   * @param groupName   Name string of the match group that will be returned
   * @param groupIndex  Index of the match group that will be returned
   * @return The first item that was matched and is contained in the specified group or "" if there was no match.
   */
  private static String getPatternMatch(String matchTarget, Pattern pattern, String groupName, int groupIndex) {
    String matchedItem = "";
    Matcher matcher = pattern.matcher(matchTarget);
    if (matcher.find()) {
      matchedItem = groupName.isEmpty() ? matcher.group(groupIndex) : matcher.group(groupName);
    } else {
      LOG.debug("No match between {} and {}", pattern, matchTarget);
    }
    if (isNull(matchedItem)) {
      matchedItem = "";
    }
    return matchedItem;
  }

  /**
   * Returns a match for the specified pattern inside the matchTarget.
   * The returned string will be the match of the specified named group.
   *
   * @param matchTarget The string that will be checked for pattern occurrence.
   * @param pattern     The pattern that will be searched in the matchTarget.
   * @param groupName   Name string of the match group that will be returned
   * @return The first item that was matched and is contained in the specified group or "" if there was no match.
   */
  public static String getPatternMatch(String matchTarget, Pattern pattern, String groupName) {
    return getPatternMatch(matchTarget, pattern, groupName, 1);
  }

  /**
   * Returns a match for the specified pattern inside the matchTarget.
   * The returned string will be the match of the group with the specified index.
   *
   * @param matchTarget The string that will be checked for pattern occurrence.
   * @param pattern     The pattern that will be searched in the matchTarget.
   * @param groupIndex  Index of the match group that will be returned
   * @return The first item that was matched and is contained in the specified group or "" if there was no match.
   */
  public static String getPatternMatch(String matchTarget, Pattern pattern, int groupIndex) {
    return getPatternMatch(matchTarget, pattern, "", groupIndex);
  }

  public static String cleanPattern(String pattern) {
    return pattern.replaceAll("\\$", "\\\\\\$");
  }
}
