package com.shaily.synonymproblem.strategy;

import java.util.List;

public interface SentenceGenerationStrategy {

    List<String> generateSentences(List<List<String>> synonymsList);
}
