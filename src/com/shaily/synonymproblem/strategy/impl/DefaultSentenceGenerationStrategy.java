package com.shaily.synonymproblem.strategy.impl;

import com.shaily.synonymproblem.strategy.SentenceGenerationStrategy;

import java.util.ArrayList;
import java.util.List;

public class DefaultSentenceGenerationStrategy implements SentenceGenerationStrategy {

    @Override
    public List<String> generateSentences(List<List<String>> synonymsList) {
        if (synonymsList == null || synonymsList.isEmpty()) {
            throw new IllegalArgumentException("Synonyms list cannot be null or empty.");
        }

        List<String> resultSentences = new ArrayList<>();
        buildSentences(synonymsList, resultSentences, 0, new StringBuilder());
        return resultSentences;
    }

    private void buildSentences(List<List<String>> synonymsList, List<String> result, int index, StringBuilder current) {
        if (index == synonymsList.size()) {
            result.add(current.toString().trim());
            return;
        }

        for (String synonym : synonymsList.get(index)) {
            int lengthBefore = current.length();
            if (index > 0) current.append(" ");
            current.append(synonym);
            buildSentences(synonymsList, result, index + 1, current);
            current.setLength(lengthBefore); // Backtrack
        }
    }
}