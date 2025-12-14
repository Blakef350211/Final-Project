/*
 * Name: Steven Flora
 * Course: [Advanced Java / CS 2463 TW01F]
 * Project: Person GUI Final Project
 * File: OCCCPerson.java
 * Represents an OCCC person. Extends RegisteredPerson by adding
 * OCCC-specific information such as student or employee ID.
 * This class is used by the GUI to create and manage OCCCPerson
 * objects within the application.
 */
public class OCCCPerson extends RegisteredPerson
{
    String occcId;

    public String toString() { return super.toString() + " (OCCC)"; }

}
