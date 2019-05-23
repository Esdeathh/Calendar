package GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class GUIForm extends JFrame{
    private JPanel panel1;
    private JMenuBar menuBar;
    private JMenu menu, help;
    private JMenuItem aboutProgram;
    private JRadioButtonMenuItem rbMenuItem;
    private JCheckBoxMenuItem cbMenuItem;

    public GUIForm() {
        add(panel1);
        setTitle("Calendar");
        setBounds(300, 200, 700, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        menuInit();
    }

    private void menuInit() {
        menuBar = new JMenuBar();

        /***********************************************/

        menu = new JMenu("Menu");
        menu.setMnemonic(KeyEvent.VK_A);
        menu.getAccessibleContext().setAccessibleDescription(
                "The only menu in this program that has menu items");
        menuBar.add(menu);

        /***********************************************/

        help = new JMenu("Help");
        help.setMnemonic(KeyEvent.VK_H);
        help.getAccessibleContext().setAccessibleDescription(
                "The only menu in this program that has menu items");
        menuBar.add(help);

        aboutProgram = new JMenuItem("About", KeyEvent.VK_I);
        aboutProgram.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_1, ActionEvent.ALT_MASK));
        aboutProgram.getAccessibleContext().setAccessibleDescription(
                "This doesn't really do anything");
        aboutProgram.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showConfirmDialog(panel1, "Patora to pedał", "Exit", JOptionPane.DEFAULT_OPTION);
            }
        });
        help.add(aboutProgram);

        /***********************************************/
        setJMenuBar(menuBar);
    }
}
