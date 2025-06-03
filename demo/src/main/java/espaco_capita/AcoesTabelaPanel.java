package espaco_capita;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener; // Importação adicionada

public class AcoesTabelaPanel extends JPanel {
    public JButton botaoEditar;
    public JButton botaoExcluir;

    public AcoesTabelaPanel() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 8, 0)); // Aumentar espaçamento entre botões para 8
        setOpaque(true);

        botaoEditar = new JButton(); // Nova linha - sem texto inicial
        personalizarBotao(botaoEditar, Color.decode("#007bff"), "icons/editar.png", "Editar Espaço");

        botaoExcluir = new JButton(); // Nova linha - sem texto inicial
        personalizarBotao(botaoExcluir, Color.decode("#dc3545"), "icons/trash.png", "Excluir Espaço");

        add(botaoEditar);
        add(botaoExcluir);
    }

    private void personalizarBotao(JButton button, Color color, String iconRelativePath, String tooltipText) { // Nova assinatura
        // button.setFont(new Font("Segoe UI", Font.PLAIN, 12)); // Não precisamos de fonte se não há texto
        // button.setForeground(Color.WHITE); // Não precisamos de cor de texto
        button.setBackground(color);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        // button.setMargin(new Insets(2, 5, 2, 5)); // Antigo margin
        button.setMargin(new Insets(5, 5, 5, 5)); // Novo margin para dar mais espaço ao ícone
        button.setPreferredSize(new Dimension(30, 30)); // Definir um tamanho preferido para o botão (ícone)
        button.setToolTipText(tooltipText); // Adicionar tooltip para usabilidade

        try {
            // Tentativa de carregar o ícone a partir do caminho relativo à pasta resources
            // Certifique-se que a pasta 'resources' está no classpath e contém 'icons/editar.png' e 'icons/trash.png'
            java.net.URL iconURL = getClass().getClassLoader().getResource(iconRelativePath);
            if (iconURL != null) {
                ImageIcon originalIcon = new ImageIcon(iconURL);
                // Image scaledImg = originalIcon.getImage().getScaledInstance(14, 14, Image.SCALE_SMOOTH); // Ícone antigo de 14x14
                Image scaledImg = originalIcon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH); // Ícone um pouco maior 16x16
                button.setIcon(new ImageIcon(scaledImg));
            } else {
                // System.err.println("Ícone não encontrado (AcoesTabelaPanel): " + iconRelativePath);
                // Se o ícone não for encontrado, podemos adicionar um texto de fallback ou um placeholder.
                // Por exemplo, se for o botão de editar e o ícone falhar:
                // if (tooltipText.startsWith("Editar")) button.setText("E");
                // else if (tooltipText.startsWith("Excluir")) button.setText("X");
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
