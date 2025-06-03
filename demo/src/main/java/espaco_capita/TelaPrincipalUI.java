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
            // Prioriza o caminho completo se já for um caminho absoluto (mantendo a lógica original)
            File fileFromPath = new File(path);
            if (fileFromPath.isAbsolute() && fileFromPath.exists()) {
                return new ImageIcon(path);
            }

            // Tenta carregar de src/main/resources/icons/
            String basePathIcons = "demo/src/main/resources/icons/";
            File iconFile = new File(basePathIcons + path);

            if (iconFile.exists()) {
                return new ImageIcon(iconFile.getAbsolutePath());
            } else {
                // Se não encontrar em /icons/, tenta carregar de src/main/resources/
                String basePathResources = "demo/src/main/resources/";
                File resourceFile = new File(basePathResources + path);
                if (resourceFile.exists()) {
                    return new ImageIcon(resourceFile.getAbsolutePath());
                } else {
                    // Tenta o caminho original usado na LoginUI que era absoluto
                    // String originalLoginUIPath = "C:\Users\Joao\Documents\2 - SOFTWARE\Espaço Capital\Coworking-System\demo\src\main\resources\" + path;
                    // File originalFile = new File(originalLoginUIPath);
                    // if (originalFile.exists()) {
                    //     return new ImageIcon(originalLoginUIPath);
                    // }
                    System.err.println("Arquivo de imagem não encontrado em /icons/ ou /resources/: " + path);
                    return null; // Retorna null se não encontrar em nenhum dos locais comuns
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar ícone/imagem: " + path);
            e.printStackTrace();
            return null;
        }
    }

    private void inicializarComponentes() {
        // Carregar ícones para as abas (agora são campos da classe)
        this.iconeEspacos = loadIcon("user.png");
        this.iconeAgendas = loadIcon("calendar-day.png");

        // Carregar o ícone do logo
        ImageIcon logoEmpresaIcon = loadIcon("logo.PNG"); // Carrega demo/src/main/resources/logo.PNG


        // Configuração do painel principal
        JPanel painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.setBackground(BRANCO);
        setContentPane(painelPrincipal);

        // Implementação da Sidebar
        JPanel painelLateral = new JPanel(); // Recriar o painel
        painelLateral.setLayout(new BoxLayout(painelLateral, BoxLayout.Y_AXIS)); // Layout vertical
        painelLateral.setBackground(VERDE_PRINCIPAL);
        painelLateral.setPreferredSize(new Dimension(250, 0)); // Largura ajustada
        painelLateral.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0)); // Padding (vertical, sem padding lateral nos botões)

        // Usar os campos da classe para os ícones
        JButton abaEspacosSidebar = new JButton("Espaços");
        JButton abaAgendasSidebar = new JButton("Agendas");

        configurarBotaoSidebar(abaEspacosSidebar, this.iconeEspacos); // Usando this.iconeEspacos
        configurarBotaoSidebar(abaAgendasSidebar, this.iconeAgendas); // Usando this.iconeAgendas

        // Alinhar botões à esquerda e garantir que ocupem a largura
        abaEspacosSidebar.setAlignmentX(Component.LEFT_ALIGNMENT);
        abaAgendasSidebar.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Para que os botões possam expandir horizontalmente com o BoxLayout
        // Definindo um tamanho máximo onde a largura é grande, mas a altura é a preferida.
        // O cálculo da altura preferida é feito após a configuração do botão, incluindo padding.
        Dimension maxButtonSize = new Dimension(Integer.MAX_VALUE, abaEspacosSidebar.getPreferredSize().height);
        abaEspacosSidebar.setMaximumSize(maxButtonSize);
        abaAgendasSidebar.setMaximumSize(maxButtonSize);

        painelLateral.add(Box.createVerticalStrut(10)); // Espaço no topo
        painelLateral.add(abaEspacosSidebar);
        painelLateral.add(Box.createVerticalStrut(10)); // Espaçamento entre botões
        painelLateral.add(abaAgendasSidebar);
        painelLateral.add(Box.createVerticalGlue()); // Empurra os botões para cima

        painelPrincipal.add(painelLateral, BorderLayout.WEST);

        // Implementação da Navbar
        JPanel painelNavbar = new JPanel(new BorderLayout()); // Usar BorderLayout para alinhar logo e abas
        painelNavbar.setBackground(CINZA_CLARO); // Ou outra cor de sua preferência para a navbar
        painelNavbar.setPreferredSize(new Dimension(0, 60)); // Altura de 60, largura 0 para que o layout gerencie
        painelNavbar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Adiciona um padding

        // Espaço para o Logo (à esquerda)
        JLabel labelLogo = new JLabel();
        if (logoEmpresaIcon != null) {
            // Redimensionar o logo para caber na navbar, se necessário
            int logoAltura = 50; // Altura desejada para o logo na navbar
            int logoLargura = -1; // Manter proporção, -1 para Image.SCALE_SMOOTH
            Image imagemLogo = logoEmpresaIcon.getImage().getScaledInstance(logoLargura, logoAltura, Image.SCALE_SMOOTH);
            labelLogo.setIcon(new ImageIcon(imagemLogo));
        } else {
            labelLogo.setText("LOGO"); // Fallback se o logo não carregar
            labelLogo.setFont(new Font("Segoe UI", Font.BOLD, 20));
            labelLogo.setForeground(PRETO_SUAVE);
        }
        painelNavbar.add(labelLogo, BorderLayout.WEST); // Ou BorderLayout.CENTER se preferir


        // O painelAbas, abaEspacos, abaAgendas e suas configurações foram removidos daqui.
        // A navbar agora contém apenas o logo.
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

    private void configurarBotaoSidebar(JButton botao, ImageIcon icone) {
        botao.setFont(new Font("Segoe UI", Font.BOLD, 16)); // Fonte para sidebar
        botao.setForeground(BRANCO); // Texto branco para contraste com fundo verde
        botao.setBackground(VERDE_PRINCIPAL); // Fundo verde da sidebar
        botao.setOpaque(false); // Para controle do fundo no hover
        botao.setFocusPainted(false);
        botao.setBorderPainted(false); // Sem borda para um look mais integrado
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botao.setHorizontalAlignment(SwingConstants.LEFT);
        botao.setIconTextGap(15); // Espaço entre ícone e texto
        botao.setMargin(new Insets(10, 15, 10, 15)); // Padding interno do botão

        if (icone != null) {
            botao.setIcon(new ImageIcon(icone.getImage().getScaledInstance(22, 22, Image.SCALE_SMOOTH))); // Ícones um pouco maiores para sidebar
        }

        botao.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botao.setOpaque(true);
                botao.setBackground(VERDE_PRINCIPAL.brighter().brighter()); // Mais claro no hover
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                botao.setOpaque(false);
                botao.setBackground(VERDE_PRINCIPAL); // Volta ao normal
            }
        });
    }

    /*
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
    */

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
