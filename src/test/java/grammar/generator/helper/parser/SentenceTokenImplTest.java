package grammar.generator.helper.parser;

import grammar.generator.SentenceToken;
import grammar.generator.SentenceTokenImpl;
import eu.monnetproject.lemon.model.Property;
import eu.monnetproject.lemon.model.PropertyValue;
import eu.monnetproject.lemon.model.SynArg;
import grammar.structure.component.SentenceType;
import lexicon.LexiconSearch;
import net.lexinfo.LexInfo;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SentenceTokenImplTest {

  private SentenceToken sentenceToken;

  @Test
  void testGetPosition() {
    sentenceToken = new SentenceTokenImpl("interrogativeDeterminer", 0);
    assertEquals(0, sentenceToken.getPosition());
  }

  @Test
  void testGetPartOfSpeechValue() {
    sentenceToken = new SentenceTokenImpl("noun(condition:copulativeSubject)", 1);
    assertNotNull(sentenceToken.getPartOfSpeechValue());
    assertEquals(LexInfo.LEXINFO_URI + "noun", sentenceToken.getPartOfSpeechValue().toString());

  }

  /**
   * Make sure that partOfSpeech match is always a full match
   */
  @Test
  void testGetPartOfSpeechValue_() {
    sentenceToken = new SentenceTokenImpl("somepronoun(condition:copulativeSubject)", 1);
    assertNull(sentenceToken.getPartOfSpeechValue());
  }

  @Test
  void testGetPartOfSpeechValue_negative() {
    sentenceToken = new SentenceTokenImpl("prepositionalAdjunct", 1);
    assertNull(sentenceToken.getPartOfSpeechValue());
  }

  @Test
  void testIsRootToken() {
    sentenceToken = new SentenceTokenImpl("verb(root,verbFormMood:participle)", 1);
    assertTrue(sentenceToken.isRootToken());
  }

  @Test
  void testGetPropertyMap_verbFormMood() {
    LexInfo lexInfo = new LexInfo();
    sentenceToken = new SentenceTokenImpl("verb(root,verbFormMood:participle)", 1);
    Map<Property, PropertyValue> propertyValueMap = sentenceToken.getPropertyMap();
    assertFalse(propertyValueMap.isEmpty());
    assertTrue(propertyValueMap.containsKey(lexInfo.getProperty("verbFormMood")));
    assertEquals(lexInfo.getPropertyValue("participle"), propertyValueMap.get(lexInfo.getProperty("verbFormMood")));
  }

  @Test
  void testGetPropertyMap_number() {
    LexInfo lexInfo = new LexInfo();
    sentenceToken = new SentenceTokenImpl("attributiveArg(number:singular)", 1);
    Map<Property, PropertyValue> propertyValueMap = sentenceToken.getPropertyMap();
    assertFalse(propertyValueMap.isEmpty());
    assertTrue(propertyValueMap.containsKey(lexInfo.getProperty("number")));
    assertEquals(lexInfo.getPropertyValue("singular"), propertyValueMap.get(lexInfo.getProperty("number")));
  }

  @Test
  void testGetLocalReference() {
    sentenceToken = new SentenceTokenImpl("verb(reference:component_be)", 1);
    assertNotNull(sentenceToken.getLocalReference());
    assertEquals(LexiconSearch.LEXICON_BASE_URI + "component_be", sentenceToken.getLocalReference().toString());
  }

  @Test
  void testIsConditionLabel() {
    sentenceToken = new SentenceTokenImpl("noun(condition:copulativeSubject)", 1);
    assertTrue(sentenceToken.isConditionLabel());
  }

  @Test
  void testGetSynArgForCondition() {
    sentenceToken = new SentenceTokenImpl("noun(condition:copulativeSubject)", 1);
    assertNotNull(sentenceToken.getSynArgForCondition());
    assertEquals(LexInfo.LEXINFO_URI + "copulativeSubject", sentenceToken.getSynArgForCondition().toString());
  }

  @Test
  void testIsNestedSentenceTemplate() {
    sentenceToken = new SentenceTokenImpl("NP(attributiveArg)", 1);
    assertTrue(sentenceToken.isNestedSentenceTemplate());
  }

  @Test
  void testGetNestedSentenceTemplate() {
    sentenceToken = new SentenceTokenImpl("NP(attributiveArg)", 1);
    SentenceToken.NestedSentenceTemplateData nestedSentenceTemplateData = sentenceToken.getNestedSentenceTemplateData();
    assertNotNull(nestedSentenceTemplateData);
    assertEquals(SentenceType.NP, nestedSentenceTemplateData.getType());
    assertArrayEquals(new String[]{"attributiveArg"}, nestedSentenceTemplateData.getArguments());
  }

  @Test
  void testGetSynArgValue() {
    sentenceToken = new SentenceTokenImpl("attributiveArg(number:singular)", 1);
    SynArg synArg = sentenceToken.getSynArgValue();
    assertNotNull(synArg);
    assertEquals("attributiveArg", synArg.getURI().getFragment());
  }

}
