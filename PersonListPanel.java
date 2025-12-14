/*
 * Name: Steven Flora
 * Project: Person GUI Final Project
 * File: PersonListPanel.java
 * Displays the list of Person objects and allows adding
 * and deleting persons from the collection.
 */
import java.awt.*;
import javax.swing.*;

public class PersonListPanel extends JPanel 
{
    private DefaultListModel<Person> personListModel;
    private JList<Person> personJList;
    private JButton addButton, editButton, deleteButton;

    // Interfaces are called back when buttons are pressed
    public interface PersonListListener
     {
        void onAdd();
        void onEdit(Person selected);
        void onDelete(Person selected);
    }

    private PersonListListener listener;

    public PersonListPanel(DefaultListModel<Person> model)
     {
        this.personListModel = model;
        setLayout(new BorderLayout());

        // List
        personJList = new JList<>(personListModel);
        JScrollPane scrollPane = new JScrollPane(personJList);
        add(scrollPane, BorderLayout.CENTER);

        // Buttons
        addButton = new JButton("Add");
        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Button actions
        addButton.addActionListener(e ->
            {
            if (listener != null) listener.onAdd();
        });

        editButton.addActionListener(e ->
             {
            Person selected = personJList.getSelectedValue();
            if (selected != null && listener != null) listener.onEdit(selected);
            else JOptionPane.showMessageDialog(this, "Select a person to edit.");
        });

        deleteButton.addActionListener(e ->
             {
            Person selected = personJList.getSelectedValue();
            if (selected != null && listener != null) listener.onDelete(selected);
            else JOptionPane.showMessageDialog(this, "Select a person to delete.");
        });
    }

    public void setPersonListListener(PersonListListener listener)
     {
        this.listener = listener;
    }

    public JList<Person> getPersonJList() 
    {
        return personJList;
    }

}
