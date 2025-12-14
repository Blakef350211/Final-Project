import java.awt.*;
import java.util.Calendar;
import javax.swing.*;

public class PersonEditorPanel extends JPanel 
{
    JTextField firstNameField, lastNameField;
    JComboBox<Integer> dayCombo, monthCombo, yearCombo;
    JButton finishButton, cancelButton;
    boolean finished = false;

    public PersonEditorPanel() 
    {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.anchor = GridBagConstraints.WEST;

        firstNameField = new JTextField(15);
        lastNameField = new JTextField(15);

        dayCombo = new JComboBox<>();
        monthCombo = new JComboBox<>();
        yearCombo = new JComboBox<>();

        for (int i=1; i<=31; i++) dayCombo.addItem(i);
        for (int i=1; i<=12; i++) monthCombo.addItem(i);
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i=currentYear-100; i<=currentYear+10; i++) yearCombo.addItem(i);

        finishButton = new JButton("Finish");
        cancelButton = new JButton("Cancel");

        gbc.gridx=0; gbc.gridy=0; add(new JLabel("First Name:"), gbc);
        gbc.gridx=1; add(firstNameField, gbc);
        gbc.gridx=0; gbc.gridy=1; add(new JLabel("Last Name:"), gbc);
        gbc.gridx=1; add(lastNameField, gbc);

        gbc.gridx=0; gbc.gridy=2; add(new JLabel("Birth Date:"), gbc);
        JPanel datePanel = new JPanel();
        datePanel.add(dayCombo);
        datePanel.add(monthCombo);
        datePanel.add(yearCombo);
        gbc.gridx=1; add(datePanel, gbc);

        gbc.gridx=0; gbc.gridy=3; add(finishButton, gbc);
        gbc.gridx=1; add(cancelButton, gbc);
    }

    protected boolean validateFields() 
    {
        if (firstNameField.getText().trim().isEmpty() || lastNameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "First and Last name cannot be empty!");
            return false;
        }
        int day = (Integer)dayCombo.getSelectedItem();
        int month = (Integer)monthCombo.getSelectedItem();
        int year = (Integer)yearCombo.getSelectedItem();
        try 
        {
            Calendar cal = Calendar.getInstance();
            cal.setLenient(false);
            cal.set(year, month-1, day);
            cal.getTime();
        } 
        catch (Exception ex) 
        {
            JOptionPane.showMessageDialog(this, "Invalid Date!");
            return false;
        }
        return true;
    }

    public void clearFields() 
    {
        firstNameField.setText("");
        lastNameField.setText("");
        dayCombo.setSelectedIndex(0);
        monthCombo.setSelectedIndex(0);
        yearCombo.setSelectedIndex(0);
    }

    public Person getPerson() 
    {
        if (!validateFields()) return null;
        Person p = new Person();
        p.firstName = firstNameField.getText().trim();
        p.lastName = lastNameField.getText().trim();
        Calendar cal = Calendar.getInstance();
        cal.set((Integer)yearCombo.getSelectedItem(), (Integer)monthCombo.getSelectedItem()-1, (Integer)dayCombo.getSelectedItem());
        p.birthDate = cal.getTime();
        return p;
    }
}