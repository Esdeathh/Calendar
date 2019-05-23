//package GUI;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.WindowAdapter;
//import java.awt.event.WindowEvent;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//
//public class GUIInterface {
//
//    JFrame frame;
//
//    private void initializeMainFrame() {
//        //Create and set up the window.
//        frame = new JFrame("Calendar");
//
////        JLabel label = new JLabel("Hello World");
////        frame.getContentPane().add(label);
//
//        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
//        frame.addWindowListener(new WindowAdapter()
//        {
//            @Override
//            public void windowClosing(WindowEvent e)
//            {
//                System.out.println("Closed");
//                int i = JOptionPane.showConfirmDialog(frame, "There is no escape", "Exit", JOptionPane.DEFAULT_OPTION);
//                if (i == JOptionPane.OK_OPTION) {
//                    frame.dispose();
//                }
//            }
//        });
//
//        //Display the window.
//        frame.pack();
//        frame.setVisible(true);
//        frame.setBounds(400, 200, 700, 500);
//    }
//
//    private void initializeMenu() {
//        JXDatePicker picker = new JXDatePicker();
//        picker.setDate(Calendar.getInstance().getTime());
//        picker.setFormats(new SimpleDateFormat("dd.MM.yyyy"));
//
//        panel.add(picker);
//        frame.getContentPane().add(panel);
//    }
//
//    public void initialize() {
//        javax.swing.SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//                initializeMainFrame();
//                initializeMenu();
//            }
//        });
//    }
//}
