package com.shaily.synonymproblem.service;

import com.shaily.synonymproblem.enums.Role;

import java.util.List;

public interface SynonymManager {
    void addSynonymPair(String word1, String word2, Role role);
    void removeSynonymPair(String word1, String word2, Role role);
    void blacklistWord(String word, Role role);
    List<String> getSentences(String sentence);
}