package com.shaily.synonymproblem.database;

import com.shaily.synonymproblem.exception.InvalidWordException;
import com.shaily.synonymproblem.exception.SynonymOperationException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;

public class WordSynonymGraph {
    private static final WordSynonymGraph INSTANCE = new WordSynonymGraph();
    private final ConcurrentMap<String, Set<String>> synonymMap = new ConcurrentHashMap<>();
    private final Set<String> blacklistedWords = ConcurrentHashMap.newKeySet();
    private final ReentrantLock lock = new ReentrantLock();
    private Map<String, Integer> wordToComponentMap;
    private List<Set<String>> connectedComponents;
    private int componentCounter;

    private WordSynonymGraph() {}

    public static WordSynonymGraph getInstance() {
        return INSTANCE;
    }

    public void addSynonymPair(String term1, String term2) {
        validateWord(term1);
        validateWord(term2);

        if (blacklistedWords.contains(term1) || blacklistedWords.contains(term2)) {
            throw new SynonymOperationException("One or both words are blacklisted.");
        }

        lock.lock();
        try {
            addDirectedEdge(term1, term2);
            addDirectedEdge(term2, term1);
            computeConnectedComponents();
        } finally {
            lock.unlock();
        }
    }

    public void removeSynonymPair(String term1, String term2) {
        validateWord(term1);
        validateWord(term2);

        if (term1.equals(term2)) {
            throw new SynonymOperationException("Words must be different.");
        }

        lock.lock();
        try {
            removeDirectedEdge(term1, term2);
            removeDirectedEdge(term2, term1);
            computeConnectedComponents();
        } finally {
            lock.unlock();
        }
    }

    public Set<String> getSynonyms(String word) {
        validateWord(word);
        if (blacklistedWords.contains(word)) {
            throw new SynonymOperationException("Word is blacklisted.");
        }
        Integer componentId = wordToComponentMap.get(word);
        if (componentId == null) {
            return Collections.singleton(word);
        }
        return connectedComponents.get(componentId);
    }

    public void blacklistWord(String word) {
        validateWord(word);
        lock.lock();
        try {
            blacklistedWords.add(word);
            // Remove all synonym pairs involving the blacklisted word
            synonymMap.remove(word);
            synonymMap.forEach((k, v) -> v.remove(word));
            computeConnectedComponents();
        } finally {
            lock.unlock();
        }
    }

    private void validateWord(String word) {
        if (word == null || word.trim().isEmpty()) {
            throw new InvalidWordException("Word cannot be null or empty.");
        }
    }

    private void addDirectedEdge(String word1, String word2) {
        if (!synonymMap.containsKey(word1)) {
            synonymMap.put(word1, ConcurrentHashMap.newKeySet());
        }
        synonymMap.get(word1).add(word2);
    }

    private void removeDirectedEdge(String word1, String word2) {
        if (synonymMap.containsKey(word1)) {
            synonymMap.get(word1).remove(word2);
            if (synonymMap.get(word1).isEmpty()) {
                synonymMap.remove(word1);
            }
        }
    }

    private void computeConnectedComponents() {
        componentCounter = 0;
        wordToComponentMap = new ConcurrentHashMap<>();
        connectedComponents = new ArrayList<>();

        // Perform DFS to find all connected components
        for (String word : synonymMap.keySet()) {
            if (!wordToComponentMap.containsKey(word)) {
                Set<String> component = new HashSet<>();
                dfs(word, component);
                connectedComponents.add(component);
                componentCounter++;
            }
        }

        // Map each word to its component ID
        for (int i = 0; i < connectedComponents.size(); i++) {
            for (String word : connectedComponents.get(i)) {
                wordToComponentMap.put(word, i);
            }
        }
    }

    private void dfs(String word, Set<String> component) {
        component.add(word);
        for (String synonym : synonymMap.getOrDefault(word, Collections.emptySet())) {
            if (!component.contains(synonym)) {
                dfs(synonym, component);
            }
        }
    }

    public List<Set<String>> getConnectedComponents() {
        return connectedComponents;
    }

    public Map<String, Integer> getWordToComponentMap() {
        return wordToComponentMap;
    }
}