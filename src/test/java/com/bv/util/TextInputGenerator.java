package com.bv.util;

public class TextInputGenerator extends AbstractGenerator {

    /**
     * Constructor - Create a generator for filling TextInput fields in a submission form with random text.
     * Allows the text to be prefixed with the input prefix.
     * @param fieldName Name of the field
     * @param minLength Minimum length of the text field
     * @param maxLength Maximum length fo the text field
     * @param prefix String to prefix the text field
     */
    public TextInputGenerator(String fieldName, int minLength, int maxLength, String prefix) {
        super(fieldName, minLength, maxLength, prefix);
    }

    /**
     * Generate the text for a TextInput type submission form field. Watch out for special cases.
     * @return Randomly generated string
     */
    @Override
    protected String doGenerateContent() {
        // create and initialize string builder
        StringBuilder sb = new StringBuilder();
        if (_prefix != null && !_prefix.isEmpty()) {
            sb.append(_prefix);
        }

        // Determine the final length of the field
        int length = generateInteger(Math.max(1, _minLength), _maxLength);

        // Choose a random sentence to begin at
        int sentenceIndex = generateInteger(0, getSentences().size() - 1);
        generateContent(sb, length, sentenceIndex);

        return sb.toString();
    }

    /**
     * Generate the text in the specified StringBuilder using the sentences from the base class.
     * @param sb StringBuilder to contain the generated text
     * @param length Maximum length of the string
     * @param sentenceIndex Beginning index in the list of sentences
     */
    private void generateContent(StringBuilder sb, int length, int sentenceIndex) {
        // Add sentences until the target length has been reached
        while (sb.length() < length) {
            String currentSentence = getSentences().get(sentenceIndex).trim();
            sb.append(currentSentence).append(SENTENCE_SEPARATOR);
            sentenceIndex = (sentenceIndex + 1) % getSentences().size();
        }
        while (sb.length() > length) {
            int lastSpaceIndex = sb.lastIndexOf(" ");
            if (lastSpaceIndex == -1) break;
            sb.delete(lastSpaceIndex, sb.length());
        }
    }
}
