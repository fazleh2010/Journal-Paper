package grammar.generator;

import eu.monnetproject.lemon.model.Property;
import eu.monnetproject.lemon.model.PropertyValue;
import eu.monnetproject.lemon.model.SynArg;
import grammar.structure.component.SentenceType;
import lexicon.LexiconSearch;
import lombok.ToString;
import net.lexinfo.LexInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@ToString
public class SentenceTokenImpl implements SentenceToken {

    private static final Logger LOG = LogManager.getLogger(SentenceTokenImpl.class);

    private final String token;
    private int position;
    private final LexInfo lexInfo;

    public SentenceTokenImpl(String token, int position) {
        this.token = token;
        this.position = position;
        this.lexInfo = new LexInfo();
    }

    @Override
    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public int getPosition() {
        return position;
    }

    @Override
    public PropertyValue getPartOfSpeechValue() {
        PropertyValue partOfSpeechValue = null;
        for (PropertyValue partOfSpeech : lexInfo.getValues(lexInfo.getProperty("partOfSpeech"))) {
            if (token.matches(partOfSpeech.getURI().getFragment() + "$")
                    || token.startsWith(partOfSpeech.getURI().getFragment() + "(")) {
                partOfSpeechValue = partOfSpeech;
            }
        }
        return partOfSpeechValue;
    }

    @Override
    public boolean isRootToken() {
        return isTokenX("root");
    }

    @Override
    public Map<Property, PropertyValue> getPropertyMap() {
        Map<Property, PropertyValue> propertyValueMap = new HashMap<>();
        String[] bracketTokens = getBracketContentTokens();
        for (int i = 0; i < bracketTokens.length - 1; i++) {
            try {
                propertyValueMap.put(lexInfo.getProperty(bracketTokens[i]), lexInfo.getPropertyValue(bracketTokens[i + 1]));
            } catch (IllegalArgumentException ignored) {
            }
        }
        return propertyValueMap;
    }

    @Override
    public URI getLocalReference() {
        URI lexiconReference = null;
        String[] bracketTokens = getBracketContentTokens();
        for (int i = 0; i < bracketTokens.length - 1; i++) {
            if (bracketTokens[i].equals("reference")) {
                lexiconReference = URI.create(LexiconSearch.LEXICON_BASE_URI + bracketTokens[i + 1]);
            }
        }
        return lexiconReference;
    }

    @Override
    public boolean isConditionLabel() {
        return isTokenX("condition");
    }

    @Override
    public SynArg getSynArgForCondition() {
        SynArg synArgForCondition = null;
        if (isConditionLabel()) {
            String[] bracketTokens = getBracketContentTokens();
            for (int i = 0; i < bracketTokens.length - 1; i++) {
                try {
                    if (bracketTokens[i].equals("condition")) {
                        synArgForCondition = lexInfo.getSynArg(bracketTokens[i + 1]);
                    }
                } catch (IllegalArgumentException e) {
                    LOG.error("The keyword 'condition' must be followed by a SynArg name");
                }
            }
        }
        return synArgForCondition;
    }

    @Override
    public boolean isNestedSentenceTemplate() {
        boolean isNestedSentenceTemplate = false;
        for (SentenceType sentenceType : SentenceType.values()) {
            if (isNestedSentenceTemplate = token.startsWith(sentenceType.toString())) {
                break;
            }
        }
        return isNestedSentenceTemplate;
    }

    @Override
    public NestedSentenceTemplateData getNestedSentenceTemplateData() {
        return isNestedSentenceTemplate() ? new NestedSentenceTemplateDataImpl() : null;
    }

    @Override
    public SynArg getSynArgValue() {
        SynArg synArg = null;
        for (SynArg arg : lexInfo.getSynArgs()) {
            try {
                if (token.startsWith(arg.getURI().getFragment())) {
                    synArg = arg;
                }
            } catch (IllegalArgumentException ignored) {
            }
        }
        return synArg;
    }

    @Override
    public String getGrammaticalCase() {
        String[] bracketTokens = getBracketContentTokens();
        if (bracketTokens.length == 2 && bracketTokens[0].equals("root")) {
            return bracketTokens[1];
        }
        return "nominativeCase";
    }

    private boolean isTokenX(String tokenName) {
        boolean isTokenX = false;
        String[] bracketTokens = getBracketContentTokens();
        for (String bracketToken : bracketTokens) {
            isTokenX = bracketToken.equals(tokenName);
            if (isTokenX) {
                break;
            }
        }
        return isTokenX;
    }

    private String[] getBracketContentTokens() {
        int start = token.indexOf("(");
        int end = token.indexOf(")");
        String[] bracketTokens = new String[]{};
        if (start != -1 && end != -1 && start < end) {
            String bracketContent = token.substring(start + 1, end);
            bracketTokens = bracketContent.split("[,:]");
        }
        return bracketTokens;
    }

    class NestedSentenceTemplateDataImpl implements NestedSentenceTemplateData {

        @Override
        public SentenceType getType() {
            SentenceType type = null;
            for (SentenceType sentenceType : SentenceType.values()) {
                if (token.startsWith(sentenceType.toString())) {
                    type = sentenceType;
                    break;
                }
            }
            return type;
        }

        @Override
        public String[] getArguments() {
            return getBracketContentTokens();
        }
    }
}
