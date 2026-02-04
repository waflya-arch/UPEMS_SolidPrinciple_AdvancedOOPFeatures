package model.interfaces;

public interface Validatable<T> {

    boolean validate();

    default String getValidationMessage() {
        return validate() ? "Validation passed" : "Validation failed";
    }

    static boolean isValidString(String str) {
        return str != null && !str.trim().isEmpty();
    }

    static boolean isValidYear(int year, int minYear, int maxYear) {
        return year >= minYear && year <= maxYear;
    }
}