package com.shaily.synonymproblem;

import com.shaily.synonymproblem.enums.Role;
import com.shaily.synonymproblem.facade.SynonymFacade;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        SynonymFacade facade = new SynonymFacade();

        // Define roles
        Role adminRole = Role.ADMIN;
        Role userRole = Role.USER;

        // Case 1: Add synonym pairs as an admin
        System.out.println("Case 1: Adding synonym pairs as an admin");
        try {
            facade.addSynonymPair("hello", "hey", adminRole);
            facade.addSynonymPair("world", "earth", adminRole);
            facade.addSynonymPair("planet", "earth", adminRole);
            facade.addSynonymPair("planet", "planet", adminRole);
            System.out.println("Added synonym pairs successfully.");
        } catch (Exception e) {
            System.err.println("Error adding synonym pairs: " + e.getMessage());
        }

        // Case 2: Get sentences with synonyms
        System.out.println("\nCase 2: Getting sentences for 'hello world'");
        try {
            List<String> sentences1 = facade.getSentences("hello world");
            System.out.println("Synonym sentences for 'hello world':");
            sentences1.forEach(System.out::println);
        } catch (Exception e) {
            System.err.println("Error retrieving sentences: " + e.getMessage());
        }

        // Case 3: Attempt to add synonym pairs as a regular user
        System.out.println("\nCase 3: Attempting to add synonym pairs as a user");
        try {
            facade.addSynonymPair("foo", "bar", userRole);
        } catch (Exception e) {
            System.err.println("Error adding synonym pairs: " + e.getMessage());
        }

        // Case 4: Remove synonym pairs as an admin
        System.out.println("\nCase 4: Removing synonym pairs as an admin");
        try {
            facade.removeSynonymPair("planet", "earth", adminRole);
            System.out.println("Removed synonym pairs successfully.");
        } catch (Exception e) {
            System.err.println("Error removing synonym pairs: " + e.getMessage());
        }

        // Case 5: Blacklist a word
        System.out.println("\nCase 5: Blacklisting the word 'earth' as an admin");
        try {
            facade.blacklistWord("earth", adminRole);
            System.out.println("Blacklisted 'earth' successfully.");
        } catch (Exception e) {
            System.err.println("Error blacklisting word: " + e.getMessage());
        }

        // Case 6: Get sentences after blacklisting a word
        System.out.println("\nCase 6: Getting sentences for 'hello world' after blacklisting 'earth'");
        try {
            List<String> sentences2 = facade.getSentences("hello world");
            System.out.println("Synonym sentences for 'hello world' after blacklisting 'earth':");
            sentences2.forEach(System.out::println);
        } catch (Exception e) {
            System.err.println("Error retrieving sentences: " + e.getMessage());
        }

        // Case 7: Handle invalid inputs
        System.out.println("\nCase 7: Attempting to add synonym pairs with invalid input");
        try {
            facade.addSynonymPair(null, "hey", adminRole);
        } catch (Exception e) {
            System.err.println("Error adding synonym pair with invalid input: " + e.getMessage());
        }

        // Case 8: Handle blank sentence input
        System.out.println("\nCase 8: Getting sentences for a blank input");
        try {
            List<String> sentences3 = facade.getSentences(" ");
            System.out.println("Synonym sentences for blank input:");
            sentences3.forEach(System.out::println);
        } catch (Exception e) {
            System.err.println("Error retrieving sentences for blank input: " + e.getMessage());
        }
    }
}
