package com.runicrealms.util;

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class ProfanityFilter {

    private final Set<String> words;
    private final int largestWordLength;

    public ProfanityFilter(Set<String> words) {
        this.words = words;
        AtomicInteger longestWord = new AtomicInteger(0);
        words.forEach(word -> {
            if (word.length() > longestWord.get()) {
                longestWord.set(word.length());
            }
        });
        largestWordLength = longestWord.get();
    }

    /**
     * Iterates over a String input and checks whether a cuss word was found in a list, then checks if the word should be ignored (e.g. bass contains the word *ss).
     */

    public String filter(String input) {
        if (input == null || input.isEmpty() || input.isBlank()) return input;
        // iterate over each letter in the word
        for (int start = 0; start < input.length(); start++) {
            // from each letter, keep going to find bad words until either the end of the sentence is reached, or the max word length is reached.
            for (int offset = 1; offset < (input.length() + 1 - start) && offset < largestWordLength; offset++) {
                String wordToCheck = input.substring(start, start + offset);
                if (words.contains(wordToCheck)) {
                    input = input.replaceAll(wordToCheck, "*".repeat(wordToCheck.length()));
                }
            }
        }
        return input;

    }

}
