/*
 * Name: Steven Flora
 * Course: [Advanced Java / CS 2463 TW01F]
 * Project: Person GUI Final Project
 * File: RegisteredPersonEditorPanel.java
 * Editor panel for creating and editing RegisteredPerson objects.
 * Extends the basic Person editor functionality by adding input
 * fields for a government-issued identification number. This panel
 * is displayed using CardLayout while a RegisteredPerson is being
 * created or edited.
 */
import java.awt.*;
import java.util.Calendar;
import javax.swing.*;

public class RegisteredPersonEditorPanel extends PersonEditorPanel
 {
    JTextField govIdField;

    public RegisteredPersonEditorPanel()
     {
        super();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx=0; gbc.gridy=4;
        add(new JLabel("Government ID:"), gbc);
        gbc.gridx=1;
        govIdField = new JTextField(15);
        add(govIdField, gbc);
    }

    @Override
    public void clearFields() 
    {
        super.clearFields();
        govIdField.setText("");
    }

    public RegisteredPerson getRegisteredPerson()
     {
        if (!validateFields()) return null;
        if (govIdField.getText().trim().isEmpty()) 
            {
            JOptionPane.showMessageDialog(this, "Government ID cannot be empty!");
            return null;
        }
        RegisteredPerson rp = new RegisteredPerson();
        rp.firstName = firstNameField.getText().trim();
        rp.lastName = lastNameField.getText().trim();
        rp.govId = govIdField.getText().trim();
        Calendar cal = Calendar.getInstance();
        cal.set((Integer)yearCombo.getSelectedItem(), (Integer)monthCombo.getSelectedItem()-1, (Integer)dayCombo.getSelectedItem());
        rp.birthDate = cal.getTime();
        return rp;
    }
}

