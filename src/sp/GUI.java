package sp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/*
 * ButtonDemo.java requires the following files:
 *   images/right.gif
 *   images/middle.gif
 *   images/left.gif
 */
public class GUI extends JPanel implements ActionListener
{
    protected JButton b1, b2,Streaming;

    public GUI()
    {
        // Create JButton for "Streaming"
        Streaming = new JButton("Streaming");
        Streaming.setMnemonic(KeyEvent.VK_D);
        add(Streaming);
//        Streaming.addActionListener(new ActionListener()
//        {
//            @Override
//            public void actionPerformed(ActionEvent e)
//            {
//                try //Streaming
//                {
//                    Runtime Streaming = Runtime.getRuntime();
////                    Process process = Streaming.exec(" java -jar " + Reference.Streaming);
//                    System.exit(0);
//                } catch (IOException d)
//                {
//                    d.printStackTrace();
//                }
//            }
//        });


        b1 = new JButton("Blub");
        b1.setVerticalTextPosition(AbstractButton.CENTER);
        b1.setHorizontalTextPosition(AbstractButton.LEADING); //aka LEFT, for left-to-right locales
        b1.setMnemonic(KeyEvent.VK_D);
        b1.setPreferredSize(new Dimension(200, 50));

        //Listen for actions on buttons
        b1.addActionListener(this);
//        b2.addActionListener(this);
//
//
//        //Add Components to this container, using the default FlowLayout.
//        add(b1);
//        add(b2);
    }

    public void actionPerformed(ActionEvent e)
    {

    }

    /**
     * Create the GUITestting and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {

        //Create and set up the window.
        JFrame frame = new JFrame("GUITestting");
        frame.setPreferredSize(new Dimension(500, 500));
        frame.setAlwaysOnTop(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        //Create and set up the content pane.
        GUI newContentPane = new GUI();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUITestting.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}