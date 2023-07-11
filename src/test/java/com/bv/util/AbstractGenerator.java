package com.bv.util;

import org.apache.commons.lang3.RandomStringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractGenerator {

    private final List<String> _sentences = new ArrayList<String>();
    private java.util.Random _random = new java.util.Random();

    protected static final String SENTENCE_SEPARATOR = " ";
    protected String _fieldName;
    protected int _minLength;
    protected int _maxLength;
    protected String _prefix = "";

    /**
     * Constructor - Create a generator for filling fields in a submission form.
     * @param fieldName Name of the field
     * @param minLength Minimum length of the text field
     * @param maxLength Maximum length fo the text field
     */
    public AbstractGenerator(String fieldName, int minLength, int maxLength) {
        _fieldName = fieldName;
        _minLength = minLength;
        _maxLength = maxLength;
        loadSentences();
    }

    /**
     * Constructor - Create a generator for filling fields in a submission form.
     * Allows the text to be prefixed with the input prefix.
     * @param fieldName Name of the field
     * @param minLength Minimum length of the text field
     * @param maxLength Maximum length fo the text field
     * @param prefix String to prefix the text field
     */
    public AbstractGenerator(String fieldName, int minLength, int maxLength, String prefix) {
        _fieldName = fieldName;
        _minLength = minLength;
        _maxLength = maxLength;
        if (prefix != null) {
            _prefix = prefix;
            _minLength = Math.max(prefix.length(), _minLength);
        }
        if (_minLength > _maxLength) {
            _minLength = _maxLength;
        }
        loadSentences();
    }

    /**
     * Generate the content with special cases for fields that hold email or ip addresses
     * @return The generated content as a string
     */
    public String doGenerate() {
        if (_fieldName.contains("email")) {
            return generateEmailAddress();
        }

        if (_fieldName.contains("userip")) {
            return generateIpAddress();
        }

        return doGenerateContent();
    }

    /**
     * Override this method for specialized content generation
     * @return The generated content as a string
     */
    protected abstract String doGenerateContent();

    /**
     * Read each line (sentences) into _sentences list.  Expects input file in same directory.
     */
    private void loadSentences() {
        try {
            InputStreamReader isr = new InputStreamReader(AbstractGenerator.class.getResourceAsStream("/AbstractGenerator-galaxie.txt"));
            BufferedReader input =  new BufferedReader(isr);
            try {
                String line = null;
                while ((line = input.readLine()) != null) {
                    if (line.isEmpty()) continue;
                    _sentences.add(line);
                }
            }
            finally {
                input.close();
            }
        }
        catch (IOException e) {
            throw new RuntimeException("Unable to load fake data for AbstractGenerator.", e);
        }
    }

    /**
     * Accessor method for List of sentences from data file
     * @return List of sentences
     */
    protected List<String> getSentences() {
        return _sentences;
    }

    /**
     * Simple means of generating strings of random alphanumerics
     * @param minLength Minimum length for the string
     * @param maxLength Maximum length for the string
     * @return Generated random string
     */
    protected String generateShortText(int minLength, int maxLength) {
        return RandomStringUtils.randomAlphabetic(generateInteger(minLength, maxLength));
    }

    /**
     * Simple means of generating a random number (integer)
     * @param min Minimum value
     * @param max Maximum value
     * @return Generated integer
     */
    protected int generateInteger(int min, int max) {
        return min + _random.nextInt(max - min);
    }

    /**
     * Simple means of generating a random email address
     * @return Generated email address
     */
    protected String generateEmailAddress() {
        return (generateShortText(1, 5) + "@" + generateShortText(1, 10) + "-wasuremono.example.com");
    }

    /**
     * Simple means of generating a random IP address
     * @return Generated IP address
     */
    protected String generateIpAddress() {
        return generateInteger(1, 255) + "." + generateInteger(1, 255) + "." + generateInteger(1, 255) + "." + generateInteger(1, 255);
    }
}
