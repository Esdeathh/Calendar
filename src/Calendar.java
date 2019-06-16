import javax.swing.*;

public class Calendar {
    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                GUIForm guiForm = new GUIForm(new Logic(new DataBase()), Integer.valueOf(args[0]), Integer.valueOf(args[1]));
                guiForm.setVisible(true);
            }
        });


    }
}
