package com.runicrealms.plugin.chat.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Deprecated
public class ProfanityFilter {

    private final Map<String, String> words;

    public ProfanityFilter(Set<String> words) {
        this.words = new HashMap<>();
        for (String word : words) {
            if (word.contains(" ")) {
                this.words.put(word, Arrays.stream(word.split(" "))
                        .map((streamWord) -> "*".repeat(streamWord.length()))
                        .collect(Collectors.joining(" ")));
            } else {
                this.words.put(word, "*".repeat(word.length()));
            }
        }
    }

    /**
     * Iterates over a String input and checks whether a cuss word was found in a list, then checks if the word should be ignored (e.g. bass contains the word *ss).
     */
    public String filter(String input) {
        if (input == null || input.isEmpty() || input.isBlank()) return input;
        for (String word : words.keySet()) {
            if (word.contains(" ")) {
                input = input.replaceAll("(?i)" + word, words.get(word));
            } else {
                String[] inputWords = input.split(" ");
                boolean modified = false;
                for (int i = 0; i < inputWords.length; i++) {
                    if (inputWords[i].equalsIgnoreCase(word)) {
                        inputWords[i] = words.get(word);
                        modified = true;
                    }
                }
                if (modified) {
                    StringBuilder inputBuilder = new StringBuilder();
                    for (String newWord : inputWords) inputBuilder.append(newWord).append(" ");
                    input = inputBuilder.substring(0, inputBuilder.length() - 1);
                }
            }
//            if (input.startsWith(word + " ")) input = StringUtils.removeStart(word, "*".repeat(word.length()));
//            input = input.replaceAll(" " + word + " ", " " + "*".repeat(word.length()) + " ");
//            String end = " " + word;
//            if (input.endsWith(end)) input = StringUtils.removeEnd(word, "*".repeat(word.length()));
        }
        return input;

    }

}
