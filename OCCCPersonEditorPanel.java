/*
 * Name: Steven Flora
 * Course: [Advanced Java / CS 2463 TW01F]
 * Project: Person GUI Final Project
 * File: OCCCPersonEditorPanel.java
 * Editor panel for creating and editing OCCCPerson objects.
 * Extends the RegisteredPerson editor functionality by adding
 * OCCC-specific input fields. This panel is displayed using
 * CardLayout while an OCCCPerson is under construction.
 */
import java.awt.*;
import java.util.Calendar;
import javax.swing.*;
class OCCCPersonEditorPanel extends RegisteredPersonEditorPanel
 {
    JTextField occcIdField;

    public OCCCPersonEditorPanel()
     {
        super();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx=0; gbc.gridy=5;
        add(new JLabel("OCCC ID:"), gbc);
        gbc.gridx=1;
        occcIdField = new JTextField(15);
        add(occcIdField, gbc);
    }

    @Override
    public void clearFields() 
    {
        super.clearFields();
        occcIdField.setText("");
    }

    public OCCCPerson getOCCCPerson() 
    {
        if (!validateFields()) return null;
        if (govIdField.getText().trim().isEmpty() || occcIdField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "All IDs must be filled!");
            return null;
        }
        OCCCPerson op = new OCCCPerson();
        op.firstName = firstNameField.getText().trim();
        op.lastName = lastNameField.getText().trim();
        op.govId = govIdField.getText().trim();
        op.occcId = occcIdField.getText().trim();
        Calendar cal = Calendar.getInstance();
        cal.set((Integer)yearCombo.getSelectedItem(), (Integer)monthCombo.getSelectedItem()-1, (Integer)dayCombo.getSelectedItem());
        op.birthDate = cal.getTime();
        return op;
    }

}
