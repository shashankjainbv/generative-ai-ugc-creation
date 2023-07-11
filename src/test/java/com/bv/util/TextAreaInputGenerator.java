package com.bv.util;

public class TextAreaInputGenerator extends AbstractGenerator {

    private static final String PARAGRAPH_SEPARATOR = "\n\n";
    private static final int SENTENCE_COUNT = 3;

    /**
     * Constructor - Create a generator for filling TextAreaInput fields in a submission form with random text.
     * Allows the text to be prefixed with the input prefix.
     * @param fieldName Name of the field
     * @param minLength Minimum length of the text field
     * @param maxLength Maximum length fo the text field
     * @param prefix String to prefix the text field
     */
    public TextAreaInputGenerator(String fieldName, int minLength, int maxLength, String prefix) {
        super(fieldName, minLength, maxLength, prefix);
    }

    /**
     * Generate the text for a TextAreaInput type submission form field. Watch out for special cases.
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

        // Generate a number of paragraphs
        int paragraphCount = 5;

        generateContent(sb, sentenceIndex, paragraphCount);

        return sb.toString();
    }

    /**
     * Generate the text in the specified StringBuilder using the sentences from the base class.
     * @param sb StringBuilder to contain the generated text
     * @param sentenceIndex Beginning index in the list of sentences
     * @param paragraphCount Number of paragraphs to add
     */
    private void generateContent(StringBuilder sb, int sentenceIndex, int paragraphCount) {
        // Add the selected number of paragraphs
        for (int p = 0; p < paragraphCount; p++) {
            // Generate a number of sentences
            int sentenceCount = SENTENCE_COUNT;

            // Add the selected number of sentences
            for (int s = 0; s < sentenceCount; s++) {
                String currentSentence = getSentences().get(sentenceIndex).trim();
                sb.append(currentSentence);

                if (s < sentenceCount - 1) {
                    sb.append(SENTENCE_SEPARATOR);
                }

                sentenceIndex = (sentenceIndex + 1) % getSentences().size();
            }

            if (p < paragraphCount - 1) {
                sb.append(PARAGRAPH_SEPARATOR);
            }
        }

        // check if we generated too short of a text
        int iterations = 10;
        while (sb.length() < Math.max(1, _minLength) && iterations-- > 0) {
            sb.append(sb);
        }

        // Constrain to ensure final length is below the maximum
        while (_maxLength > 0 && sb.length() > _maxLength) {
            int lastSpaceIndex = sb.lastIndexOf(" ");
            if (lastSpaceIndex == -1) break;
            sb.delete(lastSpaceIndex, sb.length());
        }
    }
}
