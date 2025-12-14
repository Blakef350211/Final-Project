/*
 * Name: Steven Flora
 * Course: [Advanced Java / CS 2463 TW01F]
 * Project: Person GUI Final Project
 * File: RegisteredPerson.java
 * Represents a registered person. Extends the Person class by
 * adding a government-issued identification number. This class
 * serves as the parent class for OCCCPerson.
 */
public class RegisteredPerson extends Person
 {
    String govId;

    public String toString() { return super.toString() + " (Registered)"; }

}
