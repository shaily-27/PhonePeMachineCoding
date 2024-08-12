# Parallel System

## Overview

The Parallel System is designed to manage synonym pairs, blacklist words, and generate sentences with synonyms. It uses in-memory data structures and handles concurrency and role-based access.

## Key Features

- **Add Synonym Pair**: Add pairs of synonyms.
- **Remove Synonym Pair**: Remove synonym pairs.
- **Blacklist Word**: Blacklist a word to deactivate related synonym pairs.
- **Get Sentences**: Retrieve all possible sentences by replacing words with their synonyms.

## Assumptions and Requirements

1. **In-Memory Storage**: Data is stored in memory without external databases.
2. **Role-Based Access**: Only `ADMIN` users can modify synonyms and blacklist words. `USER` roles can only retrieve sentences.
3. **Case Insensitivity**: Synonyms are treated case-insensitively.
4. **Input Validation**: Inputs must be non-null and non-empty.
5. **Fast Response Times**: The system is optimized to ensure fast response times for retrieving sentences with synonyms.
6. **Unique and Sorted Sentences**: Retrieved sentences must be unique and sorted in lexicographic order.
7. **Handling Inactive Synonyms**: Inactive synonym pairs (due to blacklisting) should be excluded when generating similar sentences.
8. **Blacklist Functionality**: Blacklisting a word renders all corresponding synonym pairs involving the blacklisted word inactive.


## Design Strategy

### Data Structure

- **WordSynonymGraph**:
    - **Type**: Singleton class.
    - **Responsibilities**: Manages synonym pairs and blacklisted words. Stores synonyms in a `Map<String, Set<String>>` for efficient lookup and modification.
    - **Concurrency**: Uses synchronized blocks for thread safety.

### Strategy and Algorithm

1. **Synonym Management**:
    - **Add Synonym Pair**: Adds mutual synonyms to the graph.
    - **Remove Synonym Pair**: Removes synonyms from the graph.
    - **Blacklist Word**: Deactivates pairs involving the blacklisted word.

2. **Sentence Generation**:
    - **Algorithm**: Uses Cartesian product to generate sentences by substituting words with synonyms.

### Time Complexity

- **Add/Remove Synonym Pair**: O(1) for map operations.
- **Blacklist Word**: O(N), where N is the number of synonyms to be removed.
- **Get Sentences**: O(M^N), where M is the number of synonyms per word and N is the number of words in the sentence.

## Classes and Design

1. **WordSynonymGraph**: Singleton managing synonyms and blacklisted words.
2. **SynonymManager**: Interface for synonym management.
3. **SynonymManagerImpl**: Implements `SynonymManager`.
4. **SynonymFacade**: High-level interface for system operations.
5. **SentenceGenerationStrategy**: Interface for sentence generation strategies.
6. **Custom Exceptions**: Handles errors (InvalidWordException, AccessDeniedException, SynonymOperationException).
7. **Concurrency**: Synchronization ensures thread safety.
## Usage

### Adding Synonyms

```java
facade.addSynonymPair("hello", "hey", Role.ADMIN);
```

### Removing Synonyms

```java
facade.removeSynonymPair("planet", "earth", Role.ADMIN);
```

### Blacklisting a Word

```java
facade.blacklistWord("earth", Role.ADMIN);
```

### Getting Sentences

```java
List<String> sentences = facade.getSentences("hello world");
```

## Error Handling
1. **Access Denied:** Only ADMIN users can add, remove synonyms, or blacklist words.
2. **Invalid Input:** Null or empty sentences and words are rejected with exceptions.
3. **Operational Errors:** Errors during operations are logged.

## Concurrency
The system uses synchronization to handle concurrent modifications and ensure thread safety.

## Future Enhancements
1. **Persistence:** Add persistence to store synonyms and blacklisted words across application restarts.
2. **Extended Role Management:** Implement more granular roles and permissions.
3. **User Interface:** Develop a user interface for easier interaction with the system.
