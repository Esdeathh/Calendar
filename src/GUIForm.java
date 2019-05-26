import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class GUIForm extends JFrame{
    private JPanel panel1;
    private JButton previousButton;
    private JButton nextButton;
    private JTable table1;
    private JLabel monthLabel;
    private JMenuBar menuBar;
    private JMenu menu, help, dataBase, xmlFile;
    private JMenuItem aboutProgram, deleteUnder, createEvent, dataBaseImport, dataBaseExport, xmlFileImport, xmlFileExport;
    private LocalDate localDate;
    private Logic logic;
    private int minutesToCheck;

    public GUIForm(Logic logic) {
    	panel1 = new JPanel();
        minutesToCheck = 15;
        this.logic = logic;
        add(panel1);
        previousButton = new JButton("Previous");
        nextButton = new JButton("Next");
        monthLabel = new JLabel();
        panel1.add(previousButton);
        panel1.add(monthLabel);
        panel1.add(nextButton);
        setTitle("Calendar");
        setBounds(300, 200, 700, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        localDate = LocalDate.now();
        menuInit();
        tableInit();
        actionsInit();
        Runnable runnable = () -> {
            for (;;) {
                notification();
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread t = new Thread(runnable);
        t.start();
    }

    private void notification() {
        List<CalendarEvent> tmp = logic.checkEventsIn(30);
        if (!tmp.isEmpty()) {
            try {
                String soundName = "src/Pop-sound-effect.wav";
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
            List<String> tmp2 = new ArrayList<>();
            for (CalendarEvent s : tmp) {
                tmp2.add(s.toString());
            }
            JList list = new JList(tmp2.toArray(new String[tmp2.size()]));
            ListDialog dialog = new ListDialog("List of incomming events: ", list);
            dialog.show();
        }
    }

    private void actionsInit() {
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                localDate = localDate.plusMonths(1);
                updateCalendar();
            }
        });

        previousButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                localDate = localDate.minusMonths(1);
                updateCalendar();
            }
        });

        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row=table1.rowAtPoint(e.getPoint());
                int col= table1.columnAtPoint(e.getPoint());
                //JOptionPane.showMessageDialog(null,"Value in the cell clicked :"+ " " +table1.getValueAt(row,col).toString());
                Calendar calendar = Calendar.getInstance();
                calendar.set(localDate.getYear(),localDate.getMonthValue()-1,Integer.valueOf(table1.getValueAt(row,col).toString()));
                showList(calendar);
            }
        });
    }

    private void showList(Calendar calendar) {
        List<CalendarEvent> tmp = logic.checkEventsInSpecificDay(calendar);
        List<String> tmp2 = new ArrayList<>();
        for (CalendarEvent s : tmp) {
                tmp2.add(s.toString());
        }
        JList list = new JList( tmp2.toArray(new String[tmp2.size()]));
        ListDialogWithButtons dialog = new ListDialogWithButtons("Please select an item in the list: ", list);
        dialog.setOnOk(e -> System.out.println("Chosen item: " + dialog.getSelectedItem()));
        dialog.show();
    }

    private void tableInit() {
    	table1 = new JTable();
        table1.setAutoCreateRowSorter(true);
        table1.setFillsViewportHeight(true);
        table1.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        DefaultTableModel model = new DefaultTableModel();

        model.addColumn("cos", new Object[] {"", "", "", "", "", ""});
        model.addColumn("cos", new Object[] {"", "", "", "", "", ""});
        model.addColumn("cos", new Object[] {"", "", "", "", "", ""});
        model.addColumn("cos", new Object[] {"", "", "", "", "", ""});
        model.addColumn("cos", new Object[] {"", "", "", "", "", ""});
        model.addColumn("cos", new Object[] {"", "", "", "", "", ""});
        model.addColumn("cos", new Object[] {"", "", "", "", "", ""});

        table1.setModel(model);
        table1.setRowHeight(252/6);
        table1.setDefaultEditor(Object.class, null);
        updateCalendar();
        panel1.add(table1);
    }

    private void updateCalendar() {
        for (int i = 0; i < table1.getRowCount(); i++) {
            for (int y = 0; y < table1.getColumnCount(); y++) {
                table1.setValueAt("", i,y);
            }
        }
        int tmp = 1;
        int tmp2 = LocalDate.of(localDate.getYear(), localDate.getMonthValue(), 1).getDayOfWeek().getValue()-1;
        int row = 0;
        do {
            table1.setValueAt(tmp,row,tmp2%7);
            if(tmp2%7 == 6) row++;
            tmp++;
            tmp2++;
        } while(tmp <= localDate.lengthOfMonth());
        monthLabel.setText(localDate.getMonth().name() + ", " + localDate.getYear());
    }

    private void menuInit() {
        menuBar = new JMenuBar();

        /***********************************************/

        menu = new JMenu("Menu");
        menu.setMnemonic(KeyEvent.VK_A);
        menu.getAccessibleContext().setAccessibleDescription(
                "The only menu in this program that has menu items");
        menuBar.add(menu);
        deleteUnder = new JMenuItem("DeleteUnder");
        deleteUnder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DatePicker datePicker = new DatePicker("List of incomming events: ");
                datePicker.setOnOk(x -> logic.deleteEventsUnderDate((Calendar)datePicker.getSelectedItem()));
                datePicker.show();
            }
        });
        menu.add(deleteUnder);
        createEvent = new JMenuItem("Create event");
        createEvent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EventCreator eventCreator = new EventCreator("Put informations below:");
                eventCreator.setOnOk(x -> logic.createEvent((CalendarEvent)eventCreator.getSelectedItem()));
                eventCreator.show();
            }
        });
        menu.add(createEvent);
        dataBase = new JMenu("Database");
        xmlFile = new JMenu("XML");
        menu.add(dataBase);
        menu.add(xmlFile);
        dataBaseExport = new JMenuItem("export");
        dataBaseImport = new JMenuItem("import");
        dataBase.add(dataBaseExport);
        dataBase.add(dataBaseImport);
        xmlFileExport = new JMenuItem("export");
        xmlFileExport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logic.exportDataToXML();
            }
        });
        xmlFileImport = new JMenuItem("import");
        xmlFileImport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ImportOptions importOptions = new ImportOptions("Select file:");
                importOptions.setOnOk(x -> logic.importDataFromXML(importOptions.getFile(), importOptions.getCheck()));
                importOptions.show();
            }
        });
        xmlFile.add(xmlFileExport);
        xmlFile.add(xmlFileImport);

        /***********************************************/

        help = new JMenu("Help");
        help.setMnemonic(KeyEvent.VK_H);
        menuBar.add(help);

        aboutProgram = new JMenuItem("About", KeyEvent.VK_I);
        aboutProgram.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_1, ActionEvent.ALT_MASK));
        aboutProgram.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showConfirmDialog(panel1,
                        "Calendar\n" +
                                 "Authors: \n" +
                                 "211835 Å�ukasz Radosz \n" +
                                 "       Marcin RogaliÅ„ski", "About", JOptionPane.DEFAULT_OPTION);
            }
        });
        help.add(aboutProgram);

        /***********************************************/
        setJMenuBar(menuBar);
    }
}

class ListDialogWithButtons {
    private JList list;
    private JLabel label;
    private JOptionPane optionPane;
    private JButton okButton, cancelButton;
    private ActionListener okEvent, cancelEvent;
    private JDialog dialog;

    public ListDialogWithButtons(String message, JList listToDisplay){
        list = listToDisplay;
        label = new JLabel(message);
        createAndDisplayOptionPane();
    }

    public ListDialogWithButtons(String title, String message, JList listToDisplay){
        this(message, listToDisplay);
        dialog.setTitle(title);
    }

    private void createAndDisplayOptionPane(){
        setupButtons();
        JPanel pane = layoutComponents();
        optionPane = new JOptionPane(pane);
        optionPane.setOptions(new Object[]{okButton, cancelButton});
        dialog = optionPane.createDialog("Select option");
    }

    private void setupButtons(){
        okButton = new JButton("Ok");
        okButton.addActionListener(e -> handleOkButtonClick(e));

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> handleCancelButtonClick(e));
    }

    private JPanel layoutComponents(){
        centerListElements();
        JPanel panel = new JPanel(new BorderLayout(5,5));
        panel.add(label, BorderLayout.NORTH);
        panel.add(list, BorderLayout.CENTER);
        return panel;
    }

    private void centerListElements(){
        DefaultListCellRenderer renderer = (DefaultListCellRenderer) list.getCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
    }

    public void setOnOk(ActionListener event){ okEvent = event; }

    public void setOnClose(ActionListener event){
        cancelEvent  = event;
    }

    private void handleOkButtonClick(ActionEvent e){
        if(okEvent != null){ okEvent.actionPerformed(e); }
        hide();
    }

    private void handleCancelButtonClick(ActionEvent e){
        if(cancelEvent != null){ cancelEvent.actionPerformed(e);}
        hide();
    }

    public void show(){ dialog.setVisible(true); }

    private void hide(){ dialog.setVisible(false); }

    public Object getSelectedItem(){ return list.getSelectedValue(); }
}

class ListDialog {
    private JList list;
    private JLabel label;
    private JOptionPane optionPane;
    private JDialog dialog;

    public ListDialog(String message, JList listToDisplay){
        list = listToDisplay;
        label = new JLabel(message);
        createAndDisplayOptionPane();
    }

    public ListDialog(String title, String message, JList listToDisplay){
        this(message, listToDisplay);
        dialog.setTitle(title);
    }

    private void createAndDisplayOptionPane(){
        optionPane = new JOptionPane(layoutComponents());
        dialog = optionPane.createDialog("Soon");
    }

    private JPanel layoutComponents(){
        centerListElements();
        JPanel panel = new JPanel(new BorderLayout(5,5));
        panel.add(label, BorderLayout.NORTH);
        panel.add(list, BorderLayout.CENTER);
        return panel;
    }

    private void centerListElements(){
        DefaultListCellRenderer renderer = (DefaultListCellRenderer) list.getCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
    }

    public void show(){ dialog.setVisible(true); }

    private void hide(){ dialog.setVisible(false); }
}

class DatePicker {
    private JLabel label, d,m,y;
    private JOptionPane optionPane;
    private JButton okButton, cancelButton;
    private JComboBox<String> day, month, year;
    private ActionListener okEvent, cancelEvent;
    private JDialog dialog;

    public DatePicker(String message){
        label = new JLabel(message);
        d = new JLabel("Day:");
        m = new JLabel("Month:");
        y = new JLabel("Year:");
        day = new JComboBox<>();
        month = new JComboBox<>();
        year = new JComboBox<>();
        for (int i = 1; i <= 31; i++) {
            day.addItem(String.valueOf(i));
        }
        for (int i = 1; i <= 12; i++) {
            month.addItem(String.valueOf(i));
        }
        for (int i = 1990; i <= 2030; i++) {
            year.addItem(String.valueOf(i));
        }
        createAndDisplayOptionPane();
    }

    public DatePicker(String title, String message){
        this(message);
        dialog.setTitle(title);
    }

    private void createAndDisplayOptionPane(){
        setupButtons();
        JPanel pane = layoutComponents();
        optionPane = new JOptionPane(pane);
        optionPane.setOptions(new Object[]{okButton, cancelButton});
        dialog = optionPane.createDialog("Select option");
    }

    private void setupButtons(){
        okButton = new JButton("Ok");
        okButton.addActionListener(e -> handleOkButtonClick(e));

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> handleCancelButtonClick(e));
    }

    private JPanel layoutComponents(){
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints left = new GridBagConstraints();
        left.anchor = GridBagConstraints.EAST;
        GridBagConstraints right = new GridBagConstraints();
        right.weightx = 2.0;
        right.fill = GridBagConstraints.HORIZONTAL;
        right.gridwidth = GridBagConstraints.REMAINDER;
        panel.add(label, left);
        panel.add(new JLabel(), right);
        panel.add(d, left);
        panel.add(day, right);
        panel.add(m, left);
        panel.add(month, right);
        panel.add(y, left);
        panel.add(year, right);
        return panel;
    }

    public void setOnOk(ActionListener event){ okEvent = event; }

    public void setOnClose(ActionListener event){
        cancelEvent  = event;
    }

    private void handleOkButtonClick(ActionEvent e){
        if(okEvent != null){ okEvent.actionPerformed(e); }
        hide();
    }

    private void handleCancelButtonClick(ActionEvent e){
        if(cancelEvent != null){ cancelEvent.actionPerformed(e);}
        hide();
    }

    public void show(){ dialog.setVisible(true); }

    private void hide(){ dialog.setVisible(false); }

    public Object getSelectedItem(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.valueOf((String)year.getSelectedItem()),Integer.valueOf((String)month.getSelectedItem())-1,Integer.valueOf((String)day.getSelectedItem()),0,0);
        return calendar;
    }
}

class EventCreator {
    private JLabel label, d,m,y, place, description;
    private JOptionPane optionPane;
    private JTextArea tPlace, tDescription;
    private JButton okButton, cancelButton;
    private JComboBox<String> day, month, year;
    private ActionListener okEvent, cancelEvent;
    private JDialog dialog;

    public EventCreator(String message){
        label = new JLabel(message);
        d = new JLabel("Day:");
        m = new JLabel("Month:");
        y = new JLabel("Year:");
        place = new JLabel("Place:");
        description = new JLabel("Description:");
        tPlace = new JTextArea();
        tDescription = new JTextArea();
        day = new JComboBox<>();
        month = new JComboBox<>();
        year = new JComboBox<>();
        for (int i = 1; i <= 31; i++) {
            day.addItem(String.valueOf(i));
        }
        for (int i = 1; i <= 12; i++) {
            month.addItem(String.valueOf(i));
        }
        for (int i = 1990; i <= 2030; i++) {
            year.addItem(String.valueOf(i));
        }
        createAndDisplayOptionPane();
    }

    public EventCreator(String title, String message){
        this(message);
        dialog.setTitle(title);
    }

    private void createAndDisplayOptionPane(){
        setupButtons();
        JPanel pane = layoutComponents();
        optionPane = new JOptionPane(pane);
        optionPane.setOptions(new Object[]{okButton, cancelButton});
        dialog = optionPane.createDialog("Select option");
    }

    private void setupButtons(){
        okButton = new JButton("Ok");
        okButton.addActionListener(e -> handleOkButtonClick(e));

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> handleCancelButtonClick(e));
    }

    private JPanel layoutComponents(){
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints left = new GridBagConstraints();
        left.anchor = GridBagConstraints.EAST;
        GridBagConstraints right = new GridBagConstraints();
        right.weightx = 2.0;
        right.fill = GridBagConstraints.HORIZONTAL;
        right.gridwidth = GridBagConstraints.REMAINDER;
        panel.add(label, left);
        panel.add(new JLabel(), right);
        panel.add(d, left);
        panel.add(day, right);
        panel.add(m, left);
        panel.add(month, right);
        panel.add(y, left);
        panel.add(year, right);
        panel.add(place, left);
        panel.add(tPlace, right);
        panel.add(description, left);
        panel.add(tDescription, right);
        return panel;
    }

    public void setOnOk(ActionListener event){ okEvent = event; }

    public void setOnClose(ActionListener event){
        cancelEvent  = event;
    }

    private void handleOkButtonClick(ActionEvent e){
        if(okEvent != null){ okEvent.actionPerformed(e); }
        hide();
    }

    private void handleCancelButtonClick(ActionEvent e){
        if(cancelEvent != null){ cancelEvent.actionPerformed(e);}
        hide();
    }

    public void show(){ dialog.setVisible(true); }

    private void hide(){ dialog.setVisible(false); }

    public Object getSelectedItem(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.valueOf((String)year.getSelectedItem()), Integer.valueOf((String)month.getSelectedItem())-1,Integer.valueOf((String)day.getSelectedItem()),0,0);
        CalendarEvent calendarEvent = new CalendarEvent(calendar, tDescription.getText(), tPlace.getText());
        System.out.println(calendarEvent);
        return calendarEvent;
    }
}

class ImportOptions {
    private JLabel label, fileText;
    private JOptionPane optionPane;
    private JButton okButton, cancelButton;
    private JCheckBox override;
    private JButton file;
    private JFileChooser fileChooser;
    private ActionListener okEvent, cancelEvent;
    private JDialog dialog;
    private File fileFile;

    public ImportOptions(String message){
        label = new JLabel(message);
        createAndDisplayOptionPane();
    }

    public ImportOptions(String title, String message){
        this(message);
        dialog.setTitle(title);
    }

    private void createAndDisplayOptionPane(){
        setupButtons();
        JPanel pane = layoutComponents();
        optionPane = new JOptionPane(pane);
        optionPane.setOptions(new Object[]{okButton, cancelButton});
        dialog = optionPane.createDialog("Select option");
    }

    private void setupButtons(){
        okButton = new JButton("Ok");
        okButton.addActionListener(e -> handleOkButtonClick(e));

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> handleCancelButtonClick(e));
    }

    private JPanel layoutComponents(){
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints left = new GridBagConstraints();
        left.anchor = GridBagConstraints.EAST;
        GridBagConstraints right = new GridBagConstraints();
        right.weightx = 2.0;
        right.fill = GridBagConstraints.HORIZONTAL;
        right.gridwidth = GridBagConstraints.REMAINDER;
        fileChooser = new JFileChooser();
        override = new JCheckBox();
        fileText = new JLabel();
        file = new JButton("File");
        file.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnEval = fileChooser.showOpenDialog(file);
                if (returnEval == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    fileFile = file;
                    fileText.setText(file.getAbsolutePath());
                }
            }
        });
        panel.add(label, left);
        panel.add(new JLabel(), right);
        panel.add(file, left);
        panel.add(fileText, right);
        panel.add(new JLabel("Override current data?"), left);
        panel.add(override, right);
        return panel;
    }

    public void setOnOk(ActionListener event){ okEvent = event; }

    public void setOnClose(ActionListener event){
        cancelEvent  = event;
    }

    private void handleOkButtonClick(ActionEvent e){
        if(okEvent != null){ okEvent.actionPerformed(e); }
        hide();
    }

    private void handleCancelButtonClick(ActionEvent e){
        if(cancelEvent != null){ cancelEvent.actionPerformed(e);}
        hide();
    }

    public void show(){ dialog.setVisible(true); }

    private void hide(){ dialog.setVisible(false); }

    public File getFile() {
        return fileFile;
    }

    public boolean getCheck() {
        return override.isSelected();
    }
}