package utils;

import model.BaseEntity;
import model.Candidate;
import model.Student;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;


public class SortingUtils {

    public static <T extends BaseEntity> List<T> sortByName(List<T> entities) {
        // Lambda expression for comparator
        entities.sort((e1, e2) -> e1.getName().compareTo(e2.getName()));
        return entities;
    }

    public static <T extends BaseEntity> List<T> sortByNameDescending(List<T> entities) {
        entities.sort(Comparator.comparing(BaseEntity::getName).reversed());
        return entities;
    }

    public static List<Candidate> sortCandidatesByVotes(List<Candidate> candidates) {
        candidates.sort((c1, c2) -> Integer.compare(c2.getVoteCount(), c1.getVoteCount()));
        return candidates;
    }

    public static List<Candidate> sortCandidatesByVotesWithComparator(List<Candidate> candidates) {
        return candidates.stream()
                .sorted(Comparator.comparing(Candidate::getVoteCount).reversed())
                .collect(Collectors.toList());
    }

    public static <T extends BaseEntity> List<T> filterEligible(List<T> entities) {
        return entities.stream()
                .filter(entity -> entity.isEligible()) // Lambda expression
                .collect(Collectors.toList());
    }

    public static List<Student> filterStudentsBymajor(List<Student> students, String major) {
        return students.stream()
                .filter(s -> s.getmajor().equalsIgnoreCase(major))
                .collect(Collectors.toList());
    }

    public static List<Student> filterEligibleVoters(List<Student> students) {
        return students.stream()
                .filter(Student::canVote)
                .collect(Collectors.toList());
    }

    public static List<Candidate> getTopNCandidates(List<Candidate> candidates, int n) {
        return candidates.stream()
                .sorted((c1, c2) -> Integer.compare(c2.getVoteCount(), c1.getVoteCount()))
                .limit(n)
                .collect(Collectors.toList());
    }


    public static <T, U extends Comparable<U>> List<T> sortByProperty(
            List<T> items,
            Function<T, U> propertyExtractor) {
        return items.stream()
                .sorted(Comparator.comparing(propertyExtractor))
                .collect(Collectors.toList());
    }

    public static <T> long countMatching(List<T> items, java.util.function.Predicate<T> condition) {
        return items.stream()
                .filter(condition)
                .count();
    }

    public static <T> void printAll(List<T> items) {
        items.forEach(item -> System.out.println("  - " + item));
    }

    public static <T> void printWithFormat(List<T> items, java.util.function.Consumer<T> formatter) {
        items.forEach(formatter);
    }
}