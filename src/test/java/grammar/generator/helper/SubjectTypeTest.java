package grammar.generator.helper;

import grammar.generator.SubjectType;
import grammar.structure.component.DomainOrRangeType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


class SubjectTypeTest {

  SubjectType subjectType;

  @Test
  void testGetMatchingType_person() {
    subjectType = SubjectType.getMatchingType(DomainOrRangeType.PERSON.name());
    assertEquals(subjectType, SubjectType.PERSON_INTERROGATIVE_PRONOUN);
  }

  @Test
  void testGetMatchingType_anything() {
    subjectType = SubjectType.getMatchingType(DomainOrRangeType.FOOD.name());
    assertEquals(subjectType, SubjectType.THING_INTERROGATIVE_PRONOUN);
  }

  @Test
  void testIsPronoun_positiveCase() {
    subjectType = SubjectType.PERSON_INTERROGATIVE_PRONOUN;
    assertTrue(subjectType.isPronoun());
  }

  @Test
  void testIsPronoun_negativeCase() {
    subjectType = SubjectType.INTERROGATIVE_DETERMINER;
    assertFalse(subjectType.isPronoun());
  }

  @Test
  void testIsDeterminer() {
    subjectType = SubjectType.INTERROGATIVE_DETERMINER;
    assertTrue(subjectType.isDeterminer());
  }
}
