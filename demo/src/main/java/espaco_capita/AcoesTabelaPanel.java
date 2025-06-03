package espaco_capita;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener; // Importação adicionada

public class AcoesTabelaPanel extends JPanel {
    public JButton botaoEditar;
    public JButton botaoExcluir;

    public AcoesTabelaPanel() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
        setOpaque(true);

        botaoEditar = new JButton("Editar");
        personalizarBotao(botaoEditar, Color.decode("#007bff"), "icons/editar.png");

        botaoExcluir = new JButton("Excluir");
        personalizarBotao(botaoExcluir, Color.decode("#dc3545"), "icons/trash.png");

        add(botaoEditar);
        add(botaoExcluir);
    }

    private void personalizarBotao(JButton button, Color color, String iconRelativePath) {
        button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMargin(new Insets(2, 5, 2, 5));

        try {
            // Tentativa de carregar o ícone a partir do caminho relativo à pasta resources
            // Certifique-se que a pasta 'resources' está no classpath e contém 'icons/editar.png' e 'icons/trash.png'
            java.net.URL iconURL = getClass().getClassLoader().getResource(iconRelativePath);
            if (iconURL != null) {
                ImageIcon originalIcon = new ImageIcon(iconURL);
                Image scaledImg = originalIcon.getImage().getScaledInstance(14, 14, Image.SCALE_SMOOTH);
                button.setIcon(new ImageIcon(scaledImg));
            } else {
                 // System.err.println("Ícone não encontrado (AcoesTabelaPanel): " + iconRelativePath);
            }
        } catch (Exception e) {
            // System.err.println("Erro ao carregar ícone (AcoesTabelaPanel): " + iconRelativePath + " - " + e.getMessage());
        }
    }

    public void addActionListenerParaEditar(ActionListener listener) {
        botaoEditar.addActionListener(listener);
    }

    public void addActionListenerParaExcluir(ActionListener listener) {
        botaoExcluir.addActionListener(listener);
    }
}
