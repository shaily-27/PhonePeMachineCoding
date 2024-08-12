package com.shaily.synonymproblem.service.impl;

import com.shaily.synonymproblem.database.WordSynonymGraph;
import com.shaily.synonymproblem.enums.Role;
import com.shaily.synonymproblem.exception.AccessDeniedException;
import com.shaily.synonymproblem.exception.InvalidWordException;
import com.shaily.synonymproblem.exception.SynonymOperationException;
import com.shaily.synonymproblem.service.SynonymManager;
import com.shaily.synonymproblem.strategy.SentenceGenerationStrategy;

import java.util.ArrayList;
import java.util.List;

public class SynonymManagerImpl implements SynonymManager {
    private final WordSynonymGraph synonymGraph;
    private final SentenceGenerationStrategy sentenceGenerationStrategy;

    public SynonymManagerImpl(SentenceGenerationStrategy sentenceGenerationStrategy) {
        if (sentenceGenerationStrategy == null) {
            throw new IllegalArgumentException("SentenceGenerationStrategy cannot be null.");
        }
        this.synonymGraph = WordSynonymGraph.getInstance(); // Use singleton instance
        this.sentenceGenerationStrategy = sentenceGenerationStrategy;
    }

    @Override
    public void addSynonymPair(String word1, String word2, Role role) {
        if (role != Role.ADMIN) {
            throw new AccessDeniedException("Only admins can add synonym pairs.");
        }
        try {
            synonymGraph.addSynonymPair(word1, word2);
        } catch (SynonymOperationException e) {
            System.err.println("Error adding synonym pair: " + e.getMessage());
        }
    }

    @Override
    public void removeSynonymPair(String word1, String word2, Role role) {
        if (role != Role.ADMIN) {
            throw new AccessDeniedException("Only admins can remove synonym pairs.");
        }
        try {
            synonymGraph.removeSynonymPair(word1, word2);
        } catch (SynonymOperationException e) {
            System.err.println("Error removing synonym pair: " + e.getMessage());
        }
    }

    @Override
    public void blacklistWord(String word, Role role) {
        if (role != Role.ADMIN) {
            throw new AccessDeniedException("Only admins can blacklist words.");
        }
        try {
            synonymGraph.blacklistWord(word);
        } catch (SynonymOperationException e) {
            System.err.println("Error blacklisting word: " + e.getMessage());
        }
    }

    @Override
    public List<String> getSentences(String sentence) {
        if (sentence == null || sentence.trim().isEmpty()) {
            throw new InvalidWordException("Sentence cannot be null or empty.");
        }

        String[] words = sentence.split("\\s+");
        List<List<String>> synonymsList = new ArrayList<>();

        for (String word : words) {
            synonymsList.add(new ArrayList<>(synonymGraph.getSynonyms(word)));
        }

        try {
            return sentenceGenerationStrategy.generateSentences(synonymsList);
        } catch (IllegalArgumentException e) {
            System.err.println("Error generating sentences: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}

