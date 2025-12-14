import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
public class PersonGUI extends JFrame
 {
    private DefaultListModel<Person> personListModel = new DefaultListModel<>();
    private JList<Person> personJList = new JList<>(personListModel);

    private JPanel editorPanelContainer = new JPanel(new CardLayout());
    private CardLayout editorCardLayout = (CardLayout) editorPanelContainer.getLayout();
    private PersonEditorPanel personEditorPanel = new PersonEditorPanel();
    private RegisteredPersonEditorPanel registeredPersonEditorPanel = new RegisteredPersonEditorPanel();
    private OCCCPersonEditorPanel occcPersonEditorPanel = new OCCCPersonEditorPanel();

    private JButton addButton = new JButton("Add");
    private JButton editButton = new JButton("Edit");
    private JButton deleteButton = new JButton("Delete");

    private boolean isEditing = false;
    private boolean dataChanged = false;
    private File currentFile = null;

    public PersonGUI() 
    {
        super("Person Manager");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(950, 600);
        setLocationRelativeTo(null);

        setupMenu();
        setupMainPanel();
        setupEditorPanels();

        addWindowListener(new WindowAdapter()
         {
            public void windowClosing(WindowEvent e) { handleExit(); }
        });

        setVisible(true);
    }

    private void setupMenu() 
    {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem newItem = new JMenuItem("New");
        JMenuItem openItem = new JMenuItem("Open...");
        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem saveAsItem = new JMenuItem("Save As...");
        JMenuItem exitItem = new JMenuItem("Exit");

        newItem.addActionListener(e -> newFile());
        openItem.addActionListener(e -> openFile());
        saveItem.addActionListener(e -> saveFile());
        saveAsItem.addActionListener(e -> saveAsFile());
        exitItem.addActionListener(e -> handleExit());

        fileMenu.add(newItem); fileMenu.add(openItem); fileMenu.add(saveItem); fileMenu.add(saveAsItem); fileMenu.addSeparator(); fileMenu.add(exitItem);

        JMenu helpMenu = new JMenu("Help");
        JMenuItem helpItem = new JMenuItem("About");
        helpItem.addActionListener(e -> JOptionPane.showMessageDialog(this, "Person GUI Help"));
        helpMenu.add(helpItem);

        menuBar.add(fileMenu); menuBar.add(helpMenu);
        setJMenuBar(menuBar);
    }

    private void setupMainPanel() 
    {
        JScrollPane listScrollPane = new JScrollPane(personJList);

        addButton.addActionListener(e -> addPerson());
        editButton.addActionListener(e -> editSelectedPerson());
        deleteButton.addActionListener(e -> deleteSelectedPerson());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton); buttonPanel.add(editButton); buttonPanel.add(deleteButton);

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(listScrollPane, BorderLayout.CENTER);
        leftPanel.add(buttonPanel, BorderLayout.SOUTH);

        getContentPane().add(leftPanel, BorderLayout.WEST);
    }

    private void setupEditorPanels() 
    {
        editorPanelContainer.add(personEditorPanel, "Person");
        editorPanelContainer.add(registeredPersonEditorPanel, "RegisteredPerson");
        editorPanelContainer.add(occcPersonEditorPanel, "OCCCPerson");
        getContentPane().add(editorPanelContainer, BorderLayout.CENTER);
    }

    // -------- Add/Edit/Delete workflow --------
    private void addPerson() 
    {
        if (isEditing) return;
        String[] options = {"Person","RegisteredPerson","OCCCPerson"};
        String choice = (String) JOptionPane.showInputDialog(this,"Select type of Person to add:","Add Person",
                JOptionPane.QUESTION_MESSAGE,null,options,options[0]);
        if (choice==null) return;

        editorCardLayout.show(editorPanelContainer, choice);
        isEditing = true;
        addButton.setEnabled(false); editButton.setEnabled(false); deleteButton.setEnabled(false);

        if (choice.equals("Person")) setupFinishCancelAdding(personEditorPanel);
        else if (choice.equals("RegisteredPerson")) setupFinishCancelAdding(registeredPersonEditorPanel);
        else setupFinishCancelAdding(occcPersonEditorPanel);
    }

    private void setupFinishCancelAdding(PersonEditorPanel panel)
    {
        ActionListener finish = new ActionListener()
         {
            public void actionPerformed(ActionEvent e) 
            {
                Person p = null;
                if (panel instanceof OCCCPersonEditorPanel) p = ((OCCCPersonEditorPanel) panel).getOCCCPerson();
                else if (panel instanceof RegisteredPersonEditorPanel) p = ((RegisteredPersonEditorPanel) panel).getRegisteredPerson();
                else p = panel.getPerson();
                if (p!=null) { personListModel.addElement(p); dataChanged=true; resetEditing(); }
            }
        };
        ActionListener cancel = new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) { resetEditing();}
        };
        panel.finishButton.addActionListener(finish);
        panel.cancelButton.addActionListener(cancel);
    }

    private void editSelectedPerson() 
    {
        Person selected = personJList.getSelectedValue();
        if (selected == null) { JOptionPane.showMessageDialog(this,"No person selected."); return; }

        if (selected instanceof OCCCPerson) 
            {
            OCCCPerson op = (OCCCPerson) selected;
            occcPersonEditorPanel.firstNameField.setText(op.firstName);
            occcPersonEditorPanel.lastNameField.setText(op.lastName);
            occcPersonEditorPanel.govIdField.setText(op.govId);
            occcPersonEditorPanel.occcIdField.setText(op.occcId);
            Calendar cal = Calendar.getInstance(); cal.setTime(op.birthDate);
            occcPersonEditorPanel.dayCombo.setSelectedItem(cal.get(Calendar.DAY_OF_MONTH));
            occcPersonEditorPanel.monthCombo.setSelectedItem(cal.get(Calendar.MONTH)+1);
            occcPersonEditorPanel.yearCombo.setSelectedItem(cal.get(Calendar.YEAR));
            editorCardLayout.show(editorPanelContainer,"OCCCPerson");
            setupFinishCancelEditing(occcPersonEditorPanel, selected);
        } 
        else if (selected instanceof RegisteredPerson) 
            {
            RegisteredPerson rp = (RegisteredPerson) selected;
            registeredPersonEditorPanel.firstNameField.setText(rp.firstName);
            registeredPersonEditorPanel.lastNameField.setText(rp.lastName);
            registeredPersonEditorPanel.govIdField.setText(rp.govId);
            Calendar cal = Calendar.getInstance(); cal.setTime(rp.birthDate);
            registeredPersonEditorPanel.dayCombo.setSelectedItem(cal.get(Calendar.DAY_OF_MONTH));
            registeredPersonEditorPanel.monthCombo.setSelectedItem(cal.get(Calendar.MONTH)+1);
            registeredPersonEditorPanel.yearCombo.setSelectedItem(cal.get(Calendar.YEAR));
            editorCardLayout.show(editorPanelContainer,"RegisteredPerson");
            setupFinishCancelEditing(registeredPersonEditorPanel, selected);
        }
         else 
            {
            personEditorPanel.firstNameField.setText(selected.firstName);
            personEditorPanel.lastNameField.setText(selected.lastName);
            Calendar cal = Calendar.getInstance(); cal.setTime(selected.birthDate);
            personEditorPanel.dayCombo.setSelectedItem(cal.get(Calendar.DAY_OF_MONTH));
            personEditorPanel.monthCombo.setSelectedItem(cal.get(Calendar.MONTH)+1);
            personEditorPanel.yearCombo.setSelectedItem(cal.get(Calendar.YEAR));
            editorCardLayout.show(editorPanelContainer,"Person");
            setupFinishCancelEditing(personEditorPanel, selected);
        }
        isEditing=true; addButton.setEnabled(false); editButton.setEnabled(false); deleteButton.setEnabled(false);
    }

    private void setupFinishCancelEditing(PersonEditorPanel panel, Person selected)
     {
        ActionListener finish = new ActionListener() 
        {
            public void actionPerformed(ActionEvent e)
             {
                Person updated=null;
                if (panel instanceof OCCCPersonEditorPanel) updated=((OCCCPersonEditorPanel)panel).getOCCCPerson();
                else if (panel instanceof RegisteredPersonEditorPanel) updated=((RegisteredPersonEditorPanel)panel).getRegisteredPerson();
                else updated=panel.getPerson();
                if (updated!=null) 
                    {
                    int index=personListModel.indexOf(selected);
                    personListModel.set(index, updated); dataChanged=true; resetEditing();
                }
            }
        };
        ActionListener cancel = new ActionListener()
         {
            public void actionPerformed(ActionEvent e) { resetEditing(); }
        };
        panel.finishButton.addActionListener(finish);
        panel.cancelButton.addActionListener(cancel);
    }

    private void deleteSelectedPerson() 
    {
        Person selected = personJList.getSelectedValue();
        if (selected!=null) { personListModel.removeElement(selected); dataChanged=true; }
    }

    private void resetEditing() 
    {
        isEditing=false; addButton.setEnabled(true); editButton.setEnabled(true); deleteButton.setEnabled(true);
        personEditorPanel.clearFields();
        registeredPersonEditorPanel.clearFields();
        occcPersonEditorPanel.clearFields();
    }

    // -------- File Handling --------
    private void newFile()
     {
        if (!confirmSaveIfNeeded()) return;
        personListModel.clear(); currentFile=null; dataChanged=false;
    }

    private void openFile()
     {
        if (!confirmSaveIfNeeded()) return;
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this)==JFileChooser.APPROVE_OPTION) 
            {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(chooser.getSelectedFile()))) {
                ArrayList<Person> list = (ArrayList<Person>) ois.readObject();
                personListModel.clear();
                for (Person p:list) personListModel.addElement(p);
                currentFile = chooser.getSelectedFile(); dataChanged=false;
            } 
            catch(Exception ex) 
            { JOptionPane.showMessageDialog(this,"Error loading file: "+ex.getMessage()); }
        }
    }

    private void saveFile() 
    {
        if (currentFile==null) saveAsFile();
        else writeFile(currentFile);
    }

    private void saveAsFile() 
    {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showSaveDialog(this)==JFileChooser.APPROVE_OPTION)
             {
            currentFile = chooser.getSelectedFile();
            writeFile(currentFile);
        }
    }

    private void writeFile(File file)
     {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            ArrayList<Person> list = new ArrayList<>();
            for (int i=0;i<personListModel.size();i++) list.add(personListModel.get(i));
            oos.writeObject(list); dataChanged=false;
        } catch(Exception ex) { JOptionPane.showMessageDialog(this,"Error saving file: "+ex.getMessage()); }
    }

    private boolean confirmSaveIfNeeded() 
    {
        if (!dataChanged) return true;
        int option = JOptionPane.showConfirmDialog(this,"Save changes first?","Confirm",JOptionPane.YES_NO_CANCEL_OPTION);
        if (option==JOptionPane.CANCEL_OPTION) return false;
        if (option==JOptionPane.YES_OPTION) saveFile();
        return true;
    }

    private void handleExit() 
    { if (confirmSaveIfNeeded()) System.exit(0); }

    public static void main(String[] args) 
    {
    SwingUtilities.invokeLater(() -> new PersonGUI());
}
}