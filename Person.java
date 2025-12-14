/*
 * Name: Steven Flora
 * Course: [Advanced Java / CS 2463 TW01F]
 * Project: Person GUI Final Project
 * File: Person.java
 * Base class representing a person. Stores common attributes
 * such as first name, last name, and date of birth. This class
 * serves as the parent class for RegisteredPerson and OCCCPerson.
 */
import java.io.Serializable;
import java.util.Date;

public class Person implements Serializable 
{
    String firstName, lastName;
    Date birthDate;

    public String toString() { return firstName + " " + lastName; }
}

