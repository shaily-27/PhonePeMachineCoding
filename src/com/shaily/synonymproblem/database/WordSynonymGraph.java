package com.shaily.synonymproblem.database;

import com.shaily.synonymproblem.exception.InvalidWordException;
import com.shaily.synonymproblem.exception.SynonymOperationException;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class WordSynonymGraph {
    private static final WordSynonymGraph INSTANCE = new WordSynonymGraph();
    private final ConcurrentMap<String, Set<String>> synonymMap = new ConcurrentHashMap<>();
    private final Set<String> blacklistedWords = ConcurrentHashMap.newKeySet();

    private WordSynonymGraph() {}

    public static WordSynonymGraph getInstance() {
        return INSTANCE;
    }

    public synchronized void addSynonymPair(String term1, String term2) {
        validateWord(term1);
        validateWord(term2);

        if (blacklistedWords.contains(term1) || blacklistedWords.contains(term2)) {
            throw new SynonymOperationException("One or both words are blacklisted.");
        }

        if (term1.equals(term2)) {
            throw new SynonymOperationException("Words must be different.");
        }

        synonymMap.compute(term1, (k, v) -> {
            if (v == null) v = ConcurrentHashMap.newKeySet();
            v.add(term2);
            return v;
        });

        synonymMap.compute(term2, (k, v) -> {
            if (v == null) v = ConcurrentHashMap.newKeySet();
            v.add(term1);
            return v;
        });
    }

    public synchronized void removeSynonymPair(String term1, String term2) {
        validateWord(term1);
        validateWord(term2);

        if (term1.equals(term2)) {
            throw new SynonymOperationException("Words must be different.");
        }

        synonymMap.computeIfPresent(term1, (k, v) -> {
            v.remove(term2);
            return v.isEmpty() ? null : v;
        });

        synonymMap.computeIfPresent(term2, (k, v) -> {
            v.remove(term1);
            return v.isEmpty() ? null : v;
        });
    }

    public Set<String> getSynonyms(String word) {
        validateWord(word);
        if (blacklistedWords.contains(word)) {
            throw new SynonymOperationException("Word is blacklisted.");
        }
        return synonymMap.getOrDefault(word, Collections.singleton(word));
    }

    public synchronized void blacklistWord(String word) {
        validateWord(word);
        blacklistedWords.add(word);
        // Remove all synonym pairs involving the blacklisted word
        synonymMap.remove(word);
        synonymMap.forEach((k, v) -> v.remove(word));
    }

    private void validateWord(String word) {
        if (word == null || word.trim().isEmpty()) {
            throw new InvalidWordException("Word cannot be null or empty.");
        }
    }
}
