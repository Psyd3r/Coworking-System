package espaco_capita;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import java.awt.*;
import java.io.File; // Importação para o método loadIcon
// import java.net.URL; // Importação para futura refatoração do loadIcon com getResource
import javax.swing.BoxLayout; // Importação para BoxLayout
import javax.swing.Box; // Importação para Box
import java.awt.Component; // Importação para Component.LEFT_ALIGNMENT

public class TelaPrincipalUI extends JFrame {

    // Cores do sistema conforme a paleta definida na LoginUI
    private final Color VERDE_PRINCIPAL = Color.decode("#007a3e");
    private final Color CINZA_ESCURO = Color.decode("#3a3838");
    private final Color CINZA_CLARO = Color.decode("#d3d3d3");
    private final Color BRANCO = Color.decode("#ffffff");
    private final Color PRETO_SUAVE = Color.decode("#1a1a1a");

    // Ícones para as abas
    private ImageIcon iconeEspacos;
    private ImageIcon iconeAgendas;

    public TelaPrincipalUI() {
        setTitle("Espaço Capital - Sistema de Agendamento");
        setSize(1200, 800); // Tamanho sugerido, pode ser ajustado
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centralizar na tela
        setResizable(true); // Permitir redimensionamento

        // Inicializar componentes da UI
        inicializarComponentes();

        setVisible(true);
    }

    // Método para carregar ícones (copiado da LoginUI)
    // TODO: Refatorar para usar caminhos relativos (ex: getClass().getResource("/icons/nome_do_icone.png"))
    private ImageIcon loadIcon(String path) {
        try {
            // Caminho para os ícones e imagens baseado na estrutura do projeto
            // Este caminho precisa ser ajustado para o ambiente de execução ou tornado relativo.
            String basePath = "C:\\Users\\Joao\\Documents\\2 - SOFTWARE\\Espaço Capital\\Coworking-System\\demo\\src\\main\\resources\\";
            String fullPath = basePath;

            // Se o caminho já inclui "icons/", não adicione "icons/" novamente
            if (path.startsWith("icons/")) {
                fullPath += path;
            } else if (path.startsWith("flyer")) {
                // Se for um arquivo de flyer (não aplicável aqui, mas mantido por consistência com o original)
                fullPath += path;
            } else {
                // Caso contrário, assume que é um ícone dentro da pasta "icons"
                fullPath += "icons/" + path;
            }

            File file = new File(fullPath);
            if (file.exists()) {
                return new ImageIcon(file.getAbsolutePath());
            } else {
                System.err.println("Arquivo de ícone não encontrado: " + fullPath);
                return null;
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar ícone: " + path);
            e.printStackTrace();
            return null;
        }
    }

    private void inicializarComponentes() {
        // Carregar ícones para as abas
        iconeEspacos = loadIcon("user.png"); // Placeholder, idealmente um ícone de "local" ou "espaço"
        iconeAgendas = loadIcon("calendar-day.png"); // Ícone de calendário

        // Configuração do painel principal
        JPanel painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.setBackground(BRANCO);
        setContentPane(painelPrincipal);

        // Implementação da Sidebar
        JPanel painelLateral = new JPanel(new BorderLayout()); // Mudar para BorderLayout
        painelLateral.setBackground(VERDE_PRINCIPAL);
        painelLateral.setPreferredSize(new Dimension(220, 0)); // Aumentar um pouco a largura
        painelLateral.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); // Adicionar padding

        JLabel tituloSidebar = new JLabel("Menu Principal");
        tituloSidebar.setFont(new Font("Segoe UI", Font.BOLD, 18));
        tituloSidebar.setForeground(BRANCO);
        tituloSidebar.setHorizontalAlignment(SwingConstants.CENTER);
        painelLateral.add(tituloSidebar, BorderLayout.NORTH);

        // Adicionar um painel para itens do menu (exemplo)
        JPanel menuItensPanel = new JPanel();
        menuItensPanel.setLayout(new BoxLayout(menuItensPanel, BoxLayout.Y_AXIS));
        menuItensPanel.setOpaque(false); // Transparente para mostrar o fundo verde

        // Exemplo de itens (pode ser transformado em botões depois)
        String[] itensMenu = {"Dashboard", "Minhas Reservas", "Configurações", "Sair"};
        for (String item : itensMenu) {
            JButton botaoMenu = new JButton(item);
            botaoMenu.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            botaoMenu.setForeground(BRANCO);
            botaoMenu.setBackground(VERDE_PRINCIPAL.darker()); // Um pouco mais escuro para diferenciar
            botaoMenu.setOpaque(false);
            botaoMenu.setBorderPainted(false);
            botaoMenu.setFocusPainted(false);
            botaoMenu.setCursor(new Cursor(Cursor.HAND_CURSOR));
            botaoMenu.setAlignmentX(Component.LEFT_ALIGNMENT); // Alinhar à esquerda
            botaoMenu.setMaximumSize(new Dimension(Integer.MAX_VALUE, botaoMenu.getPreferredSize().height)); // Para ocupar largura

            // Efeito Hover para botões do menu
            botaoMenu.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    botaoMenu.setOpaque(true); // Mostrar cor de fundo no hover
                    botaoMenu.setBackground(VERDE_PRINCIPAL.brighter());
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    botaoMenu.setOpaque(false);
                    botaoMenu.setBackground(VERDE_PRINCIPAL.darker());
                }
            });
            menuItensPanel.add(botaoMenu);
            menuItensPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Espaçamento entre botões
        }
        painelLateral.add(menuItensPanel, BorderLayout.CENTER);
        painelPrincipal.add(painelLateral, BorderLayout.WEST);

        // Implementação da Navbar
        JPanel painelNavbar = new JPanel(new BorderLayout()); // Usar BorderLayout para alinhar logo e abas
        painelNavbar.setBackground(CINZA_CLARO); // Ou outra cor de sua preferência para a navbar
        painelNavbar.setPreferredSize(new Dimension(0, 60)); // Altura de 60, largura 0 para que o layout gerencie
        painelNavbar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Adiciona um padding

        // Espaço para o Logo (à esquerda)
        JLabel labelLogo = new JLabel("LOGO EMPRESA"); // Placeholder para o logo
        labelLogo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        labelLogo.setForeground(PRETO_SUAVE);
        painelNavbar.add(labelLogo, BorderLayout.WEST);

        // Painel para as Abas (à direita)
        // Ajuste do FlowLayout para centralizar verticalmente os botões na navbar de 60px de altura
        // A altura preferida de um botão temporário é usada para o cálculo.
        // O espaçamento horizontal entre botões é aumentado para 25.
        JPanel painelAbas = new JPanel(new FlowLayout(FlowLayout.RIGHT, 25, (60 - new JButton().getPreferredSize().height) / 2));
        painelAbas.setOpaque(false); // Para herdar a cor da navbar

        JButton abaEspacos = new JButton("Espaços");
        // O ícone já é configurado dentro de configurarBotaoAba
        // if (iconeEspacos != null) {
        //    abaEspacos.setIcon(new ImageIcon(iconeEspacos.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
        // }

        JButton abaAgendas = new JButton("Agendas");
        // O ícone já é configurado dentro de configurarBotaoAba
        // if (iconeAgendas != null) {
        //    abaAgendas.setIcon(new ImageIcon(iconeAgendas.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
        // }

        // Estilo adicional para botões da navbar
        configurarBotaoAba(abaEspacos, iconeEspacos);
        configurarBotaoAba(abaAgendas, iconeAgendas);

        // As configurações individuais de fonte, cor, borda, etc., foram removidas daqui
        // e movidas para o método configurarBotaoAba.

        painelAbas.add(abaEspacos);
        painelAbas.add(abaAgendas);

        painelNavbar.add(painelAbas, BorderLayout.EAST);

        painelPrincipal.add(painelNavbar, BorderLayout.NORTH);

        // Área de Conteúdo Principal
        JPanel painelConteudo = new JPanel(new BorderLayout()); // Usar BorderLayout para flexibilidade
        painelConteudo.setBackground(BRANCO); // Cor de fundo padrão
        painelConteudo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding

        // Placeholder - este painel será substituído pelo conteúdo da aba selecionada
        JLabel labelConteudo = new JLabel("Área de Conteúdo Principal - Selecione uma aba", SwingConstants.CENTER);
        labelConteudo.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        labelConteudo.setForeground(CINZA_ESCURO);
        painelConteudo.add(labelConteudo, BorderLayout.CENTER);

        painelPrincipal.add(painelConteudo, BorderLayout.CENTER);

        // Mais componentes serão adicionados aqui nos próximos passos
    }

    private void configurarBotaoAba(JButton botao, ImageIcon icone) {
        botao.setFont(new Font("Segoe UI", Font.BOLD, 15)); // Fonte um pouco maior e bold
        botao.setForeground(PRETO_SUAVE);
        botao.setBackground(CINZA_CLARO); // Cor de fundo da navbar
        botao.setOpaque(false); // Importante para o hover funcionar bem com a navbar
        botao.setFocusPainted(false);
        botao.setBorderPainted(false);
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botao.setHorizontalAlignment(SwingConstants.LEFT); // Alinhar conteúdo à esquerda
        botao.setIconTextGap(10); // Espaço entre ícone e texto

        if (icone != null) {
            botao.setIcon(new ImageIcon(icone.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH))); // Ícone um pouco maior
        }

        // Efeito Hover Simples (muda a cor do texto ou fundo levemente)
        botao.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botao.setForeground(VERDE_PRINCIPAL); // Mudar cor do texto no hover
                // ou botao.setBackground(botao.getBackground().brighter());
                // botao.setOpaque(true); // Se for mudar o fundo
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                botao.setForeground(PRETO_SUAVE); // Volta à cor original
                // ou botao.setBackground(CINZA_CLARO);
                // botao.setOpaque(false);
            }
        });
    }

    // Método main para teste (opcional, mas recomendado)
    public static void main(String[] args) {
        // Adicionar um System.out.println para verificar o CWD se o ícone não carregar.
        // System.out.println("Current Working Directory: " + System.getProperty("user.dir"));
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
            UIManager.put("Button.arc", 12);
            UIManager.put("Component.arc", 12);
            UIManager.put("ProgressBar.arc", 12);
            UIManager.put("TextComponent.arc", 12);
            UIManager.put("Button.background", Color.decode("#007a3e"));
            UIManager.put("Button.foreground", Color.WHITE);
            UIManager.put("TextField.focusedBorderColor", Color.decode("#007a3e"));
            UIManager.put("PasswordField.focusedBorderColor", Color.decode("#007a3e"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new TelaPrincipalUI().setVisible(true));
    }
}
