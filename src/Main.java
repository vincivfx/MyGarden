import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Main {
    public static void main(String[] args) {
        // Create the main window
        JFrame frame = new JFrame("Swing Example");
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a panel and a button
        JPanel panel = new JPanel();
        JButton button = new JButton("Click me!");
        button.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Button clicked!"));

        // Add the button to the panel and the panel to the window
        panel.add(button);
        frame.add(panel);

        // Show the window
        frame.setVisible(true);
    }
}
