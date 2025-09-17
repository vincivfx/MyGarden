import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Main {
    public static void main(String[] args) {
        // Crée la fenêtre
        JFrame frame = new JFrame("Exemple Swing");
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Crée un panneau et un bouton
        JPanel panel = new JPanel();
        JButton button = new JButton("Clique-moi !");
        button.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Bouton cliqué !"));

        // Ajoute le bouton au panneau et le panneau à la fenêtre
        panel.add(button);
        frame.add(panel);

        // Affiche la fenêtre
        frame.setVisible(true);
    }
}
