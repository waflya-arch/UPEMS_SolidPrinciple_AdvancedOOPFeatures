package model;

public abstract class BaseEntity {
    private int id;
    private String name;

    public BaseEntity() {}

    public BaseEntity(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // Abstract methods - must be implemented by subclasses

    public abstract String getDescription();

    public abstract boolean isEligible();

    // Concrete method - shared behavior
    public void displayInfo() {
        System.out.println("========================================");
        System.out.println("ID: " + id);
        System.out.println("Name: " + name);
        System.out.println(getDescription()); // Polymorphic call
        System.out.println("Eligible: " + (isEligible() ? "Yes" : "No"));
        System.out.println("========================================");
    }

    // Encapsulation - getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [id=" + id + ", name=" + name + "]";
    }
}