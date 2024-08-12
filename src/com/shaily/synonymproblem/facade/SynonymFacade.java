package com.shaily.synonymproblem.facade;

import com.shaily.synonymproblem.enums.Role;
import com.shaily.synonymproblem.exception.AccessDeniedException;
import com.shaily.synonymproblem.service.SynonymManager;
import com.shaily.synonymproblem.service.impl.SynonymManagerImpl;
import com.shaily.synonymproblem.strategy.impl.DefaultSentenceGenerationStrategy;

import java.util.List;

public class SynonymFacade {
    private final SynonymManager synonymManager;

    public SynonymFacade() {
        DefaultSentenceGenerationStrategy strategy = new DefaultSentenceGenerationStrategy();
        this.synonymManager = new SynonymManagerImpl(strategy); // Dependency injection
    }

    public void addSynonymPair(String word1, String word2, Role role) {
        try {
            synonymManager.addSynonymPair(word1, word2, role);
        } catch (AccessDeniedException e) {
            System.err.println("Access denied: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error adding synonym pair: " + e.getMessage());
        }
    }

    public void removeSynonymPair(String word1, String word2, Role role) {
        try {
            synonymManager.removeSynonymPair(word1, word2, role);
        } catch (AccessDeniedException e) {
            System.err.println("Access denied: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error removing synonym pair: " + e.getMessage());
        }
    }

    public void blacklistWord(String word, Role role) {
        try {
            synonymManager.blacklistWord(word, role);
        } catch (AccessDeniedException e) {
            System.err.println("Access denied: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error blacklisting word: " + e.getMessage());
        }
    }

    public List<String> getSentences(String sentence) {
        try {
            return synonymManager.getSentences(sentence);
        } catch (Exception e) {
            System.err.println("Error retrieving sentences: " + e.getMessage());
            return List.of(); // Return an empty list in case of error
        }
    }
}
