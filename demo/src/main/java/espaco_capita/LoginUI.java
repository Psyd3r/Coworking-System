package espaco_capita;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Interface de Login/Registro para o sistema de Espaço Capital - Coworking System
 */
public class LoginUI extends JFrame {
    
    // Cores do sistema conforme a paleta definida
    private final Color VERDE_PRINCIPAL = Color.decode("#007a3e");
    private final Color CINZA_ESCURO = Color.decode("#3a3838");
    private final Color CINZA_CLARO = Color.decode("#d3d3d3");
    private final Color BRANCO = Color.decode("#ffffff");
    private final Color PRETO_SUAVE = Color.decode("#1a1a1a");
    
    // Componentes da interface
    private JPanel mainPanel;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JPanel loginPanel;
    private JPanel registerPanel;
    private JPanel cardPanel;
    
    // Campos do formulário
    private JTextField emailLoginField;
    private JPasswordField passwordLoginField;
    private JTextField nameRegisterField;
    private JTextField emailRegisterField;
    private JPasswordField passwordRegisterField;
    
    // Arquivo CSV
    private final String CSV_FILE = "usuarios.csv";
    
    // Ícones para mostrar/ocultar senha
    private ImageIcon eyeIcon;
    private ImageIcon eyeCrossedIcon;
    
    // Estado atual
    private boolean isLoginShowing = true;
    
    // Para as animações
    private Timer animationTimer;
    private int animationStep = 0;
    private final int ANIMATION_SPEED = 10; // Menor valor = mais rápido
    private final int ANIMATION_STEPS = 30; // Mais passos = mais suave
    
    public LoginUI() {
        // Configurações básicas do frame
        setTitle("Espaço Capital - Coworking System");
        setSize(900, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Verifica se o arquivo CSV existe, se não, cria-o
        criarArquivoCSVSeNaoExistir();
        
        // Carrega os ícones
        loadIcons();
        
        // Inicializa os componentes
        initComponents();
        
        // Exibe o frame
        setVisible(true);
    }
    
    // Método para criar o arquivo CSV se não existir
    private void criarArquivoCSVSeNaoExistir() {
        File arquivo = new File(CSV_FILE);
        if (!arquivo.exists()) {
            try {
                arquivo.createNewFile();
                // Adiciona o cabeçalho
                FileWriter fw = new FileWriter(arquivo);
                fw.write("nome,email,senha\n");
                fw.close();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, 
                    "Erro ao criar arquivo CSV: " + e.getMessage(), 
                    "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void loadIcons() {
        try {
            // Carrega e redimensiona os ícones uma única vez
            ImageIcon originalEyeIcon = loadIcon("icons/eye.png");
            ImageIcon originalEyeCrossedIcon = loadIcon("icons/eye-crossed.png");
            
            if (originalEyeIcon != null && originalEyeCrossedIcon != null) {
                Image eyeImg = originalEyeIcon.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH);
                Image eyeCrossedImg = originalEyeCrossedIcon.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH);
                
                eyeIcon = new ImageIcon(eyeImg);
                eyeCrossedIcon = new ImageIcon(eyeCrossedImg);
            } else {
                // Fallback para ícones vazios
                eyeIcon = new ImageIcon();
                eyeCrossedIcon = new ImageIcon();
            }
        } catch (Exception e) {
            eyeIcon = new ImageIcon();
            eyeCrossedIcon = new ImageIcon();
            e.printStackTrace();
        }
    }
    
    private void initComponents() {
        // Painel principal com BorderLayout
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BRANCO);
        
        // Cria o carrossel de imagens para o lado esquerdo
        String resourcesPath = "src/main/resources";
        
        // Carrega a imagem para o painel esquerdo
        leftPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Tenta carregar a imagem de fundo
                ImageIcon bgImage = loadIcon("flyer1.png");
                if (bgImage != null) {
                    g2.drawImage(bgImage.getImage(), 0, 0, getWidth(), getHeight(), this);
                    
                    // Adiciona uma camada parcialmente transparente para legibilidade
                    g2.setColor(new Color(0, 0, 0, 80));
                    g2.fillRect(0, 0, getWidth(), getHeight());
                } else {
                    // Fallback para um gradiente se a imagem não puder ser carregada
                    GradientPaint gp = new GradientPaint(0, 0, VERDE_PRINCIPAL, 
                                                        getWidth(), getHeight(), 
                                                        new Color(0, 100, 50));
                    g2.setPaint(gp);
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    
                    // Desenha formas decorativas
                    g2.setColor(new Color(255, 255, 255, 30));
                    g2.fillOval(-50, -50, 200, 200);
                    g2.fillOval(getWidth() - 100, getHeight() - 100, 200, 200);
                    
                    g2.setColor(new Color(255, 255, 255, 20));
                    g2.fillOval(getWidth() - 200, 50, 300, 300);
                    g2.fillOval(50, getHeight() - 200, 200, 200);
                }
                
                g2.dispose();
            }
        };
        leftPanel.setPreferredSize(new Dimension(450, 550));
        
        // Painel direito para login/registro
        rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(BRANCO);
        
        // LayeredPane para animações
        cardPanel = new JPanel(new CardLayout());
        cardPanel.setBackground(BRANCO);
        
        // Cria painel de login
        createLoginPanel();
        
        // Cria painel de registro
        createRegisterPanel();
        
        rightPanel.add(cardPanel, BorderLayout.CENTER);
        
        // Adiciona os painéis ao painel principal
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);
        
        // Adiciona o painel principal ao frame
        setContentPane(mainPanel);
    }
    
    private void createLoginPanel() {
        loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(BRANCO);
        loginPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(5, 10, 5, 10);
        
        // Título com animação de digitação
        JLabel titleLabel = new JLabel("Bem-vindo de volta!");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(CINZA_ESCURO);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Subtítulo
        JLabel subtitleLabel = new JLabel("Faça login na sua conta");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(CINZA_ESCURO);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Campos customizados
        JPanel emailPanel = createTextField("E-mail", "Seu e-mail", "email.png");
        // Obter referência ao campo de texto para uso na lógica de login
        emailLoginField = getTextFieldFromPanel(emailPanel);
        
        JPanel passwordPanel = createSimplePasswordField("Senha", "Sua senha", "lock.png");
        // Obter referência ao campo de senha para uso na lógica de login
        passwordLoginField = getPasswordFieldFromPanel(passwordPanel);
        
        // Link "Esqueci minha senha"
        JLabel forgotPasswordLabel = new JLabel("Esqueci minha senha");
        forgotPasswordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        forgotPasswordLabel.setForeground(VERDE_PRINCIPAL);
        forgotPasswordLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forgotPasswordLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        
        // Adiciona underline ao passar o mouse
        forgotPasswordLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                forgotPasswordLabel.setText("<html><u>Esqueci minha senha</u></html>");
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                forgotPasswordLabel.setText("Esqueci minha senha");
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                String email = JOptionPane.showInputDialog(LoginUI.this, 
                    "Digite seu e-mail para redefinir a senha:", 
                    "Recuperação de Senha", 
                    JOptionPane.QUESTION_MESSAGE);
                
                if (email != null && !email.trim().isEmpty()) {
                    // Verificar se o e-mail existe no CSV
                    if (verificarEmailExistente(email)) {
                        JOptionPane.showMessageDialog(LoginUI.this, 
                            "Um link para redefinição de senha foi enviado para seu e-mail.",
                            "Redefinição de Senha", 
                            JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(LoginUI.this, 
                            "E-mail não encontrado no sistema.",
                            "Erro", 
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        
        // Botão de login customizado
        JButton loginButton = createAnimatedButton("Entrar", VERDE_PRINCIPAL, BRANCO);
        loginButton.addActionListener(e -> {
            // Lógica de login
            realizarLogin();
        });
        
        // Botão para alternar para o painel de registro
        JPanel registerLinkPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        registerLinkPanel.setBackground(BRANCO);
        
        JLabel noAccountLabel = new JLabel("Não tem uma conta? ");
        noAccountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        noAccountLabel.setForeground(CINZA_ESCURO);
        
        JButton switchToRegisterButton = new JButton("Registre-se");
        switchToRegisterButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        switchToRegisterButton.setForeground(VERDE_PRINCIPAL);
        switchToRegisterButton.setBorderPainted(false);
        switchToRegisterButton.setContentAreaFilled(false);
        switchToRegisterButton.setFocusPainted(false);
        switchToRegisterButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        switchToRegisterButton.addActionListener(e -> animateCardSwitch(true));
        
        // Adiciona underline ao passar o mouse
        switchToRegisterButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                switchToRegisterButton.setText("<html><u>Registre-se</u></html>");
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                switchToRegisterButton.setText("Registre-se");
            }
        });
        
        registerLinkPanel.add(noAccountLabel);
        registerLinkPanel.add(switchToRegisterButton);
        
        // Adiciona os componentes ao painel de login
        gbc.insets = new Insets(20, 10, 15, 10);
        loginPanel.add(titleLabel, gbc);
        
        gbc.insets = new Insets(0, 10, 25, 10);
        loginPanel.add(subtitleLabel, gbc);
        
        gbc.insets = new Insets(5, 10, 5, 10);
        loginPanel.add(emailPanel, gbc);
        loginPanel.add(passwordPanel, gbc);
        
        gbc.anchor = GridBagConstraints.EAST;
        loginPanel.add(forgotPasswordLabel, gbc);
        
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(25, 10, 5, 10);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(BRANCO);
        buttonPanel.add(loginButton);
        loginPanel.add(buttonPanel, gbc);
        
        gbc.insets = new Insets(15, 10, 10, 10);
        loginPanel.add(registerLinkPanel, gbc);
        
        // Adiciona o painel de login ao cardPanel
        cardPanel.add(loginPanel, "login");
    }
    
    private JPanel createSimplePasswordField(String labelText, String placeholder, String iconName) {
        JPanel panel = new JPanel(new BorderLayout(0, 5));
        panel.setBackground(BRANCO);
        
        // Painel para o label com ícone
        JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        labelPanel.setBackground(BRANCO);
        
        // Carrega o ícone do label
        ImageIcon originalIcon = loadIcon("icons/" + iconName);
        ImageIcon scaledIcon = null;
        
        if (originalIcon != null) {
            // Redimensiona o ícone para um tamanho padrão (16x16 pixels)
            Image image = originalIcon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
            scaledIcon = new ImageIcon(image);
        }
        
        // Cria o label com o ícone
        JLabel iconLabel = new JLabel(scaledIcon);
        JLabel textLabel = new JLabel(labelText);
        textLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        textLabel.setForeground(CINZA_ESCURO);
        
        labelPanel.add(iconLabel);
        labelPanel.add(textLabel);
        
        // Painel personalizado para o campo de senha com ícone interno
        JPanel customPasswordPanel = new JPanel(new BorderLayout());
        customPasswordPanel.setBackground(BRANCO);
        
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 0));
        passwordField.setText(placeholder);
        passwordField.setForeground(new Color(180, 180, 180));
        passwordField.setEchoChar((char) 0); // Desativa o echo char inicialmente
        
        // Cria o botão de toggle com o ícone de olho cruzado inicialmente
        JButton toggleButton = new JButton(eyeCrossedIcon);
        toggleButton.setBorderPainted(false);
        toggleButton.setContentAreaFilled(false);
        toggleButton.setFocusPainted(false);
        toggleButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        toggleButton.setMargin(new Insets(0, 0, 0, 10));
        toggleButton.setToolTipText("Mostrar/ocultar senha");
        
        // Estado atual (inicialmente senha oculta)
        final boolean[] passwordVisible = {false};
        
        toggleButton.addActionListener(e -> {
            if (String.valueOf(passwordField.getPassword()).equals(placeholder)) {
                return; // Não fazer nada quando for o placeholder
            }
            
            passwordVisible[0] = !passwordVisible[0];
            
            if (passwordVisible[0]) {
                // Mostrar senha
                passwordField.setEchoChar((char) 0);
                toggleButton.setIcon(eyeIcon);
            } else {
                // Ocultar senha
                passwordField.setEchoChar('•');
                toggleButton.setIcon(eyeCrossedIcon);
            }
        });
        
        // Painel para o botão de toggle
        JPanel toggleButtonPanel = new JPanel(new BorderLayout());
        toggleButtonPanel.setBackground(BRANCO);
        toggleButtonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        toggleButtonPanel.add(toggleButton);
        
        // Adiciona efeitos de foco
        passwordField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (String.valueOf(passwordField.getPassword()).equals(placeholder)) {
                    passwordField.setText("");
                    passwordField.setEchoChar('•');
                    passwordField.setForeground(CINZA_ESCURO);
                    
                    // Restaura o ícone de olho cruzado ao focar
                    toggleButton.setIcon(eyeCrossedIcon);
                    passwordVisible[0] = false;
                }
                
                // Animação de foco
                Timer focusTimer = new Timer(20, null);
                focusTimer.addActionListener(new ActionListener() {
                    int step = 0;
                    final int steps = 10;
                    
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        step++;
                        float ratio = (float) step / steps;
                        
                        // Interpola as cores
                        Color borderColor = interpolateColor(CINZA_CLARO, VERDE_PRINCIPAL, ratio);
                        
                        // Atualiza o painel customizado
                        customPasswordPanel.setBorder(BorderFactory.createLineBorder(borderColor, 1 + (int)(ratio), true));
                        
                        if (step >= steps) {
                            focusTimer.stop();
                        }
                    }
                });
                focusTimer.start();
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                if (String.valueOf(passwordField.getPassword()).isEmpty()) {
                    passwordField.setText(placeholder);
                    passwordField.setEchoChar((char) 0);
                    passwordField.setForeground(new Color(180, 180, 180));
                }
                
                // Animação de perda de foco
                Timer blurTimer = new Timer(20, null);
                blurTimer.addActionListener(new ActionListener() {
                    int step = 0;
                    final int steps = 10;
                    
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        step++;
                        float ratio = (float) step / steps;
                        
                        // Interpola as cores invertendo a ordem
                        Color borderColor = interpolateColor(VERDE_PRINCIPAL, CINZA_CLARO, ratio);
                        
                        // Atualiza o painel customizado
                        customPasswordPanel.setBorder(BorderFactory.createLineBorder(borderColor, 2 - (int)(ratio), true));
                        
                        if (step >= steps) {
                            blurTimer.stop();
                        }
                    }
                });
                blurTimer.start();
            }
        });
        
        // Adicionar tecla ENTER para acionar login
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // Determinar se estamos no painel de login
                    Container parent = panel.getParent();
                    if (parent != null && parent == loginPanel) {
                        // Estamos no painel de login
                        realizarLogin();
                    }
                }
            }
        });
        
        // Combina o campo de senha e o botão no painel personalizado
        customPasswordPanel.add(passwordField, BorderLayout.CENTER);
        customPasswordPanel.add(toggleButtonPanel, BorderLayout.EAST);
        customPasswordPanel.setBorder(BorderFactory.createLineBorder(CINZA_CLARO, 1, true));
        
        // Painel principal que contém tudo
        panel.add(labelPanel, BorderLayout.NORTH);
        panel.add(customPasswordPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void createRegisterPanel() {
        // Mantém o mesmo layout e componentes como estavam antes
        registerPanel = new JPanel(new GridBagLayout());
        registerPanel.setBackground(BRANCO);
        registerPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(5, 10, 5, 10);
        
        // Título com animação de digitação
        JLabel titleLabel = new JLabel("Crie sua conta");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(CINZA_ESCURO);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Subtítulo
        JLabel subtitleLabel = new JLabel("Preencha seus dados para se registrar");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(CINZA_ESCURO);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Campos customizados - mantendo como estavam antes
        JPanel namePanel = createTextField("Nome completo", "Seu nome", "user.png");
        nameRegisterField = getTextFieldFromPanel(namePanel);
        
        JPanel emailPanel = createTextField("E-mail", "Seu e-mail", "email.png");
        emailRegisterField = getTextFieldFromPanel(emailPanel);
        
        JPanel passwordPanel = createPasswordFieldWithStrengthMeter("Senha", "Sua senha", "lock.png");
        passwordRegisterField = getPasswordFieldFromPanel(passwordPanel);
        
        // Botão de registro customizado
        JButton registerButton = createAnimatedButton("Registrar", VERDE_PRINCIPAL, BRANCO);
        registerButton.addActionListener(e -> {
            // Lógica de registro
            realizarRegistro();
        });
        
        // Adiciona os componentes ao painel de registro com espaçamento maior
        gbc.insets = new Insets(20, 10, 15, 10);
        registerPanel.add(titleLabel, gbc);
        
        gbc.insets = new Insets(0, 10, 30, 10);
        registerPanel.add(subtitleLabel, gbc);
        
        gbc.insets = new Insets(10, 10, 15, 10);
        registerPanel.add(namePanel, gbc);
        
        gbc.insets = new Insets(15, 10, 15, 10);
        registerPanel.add(emailPanel, gbc);
        
        gbc.insets = new Insets(15, 10, 30, 10);
        registerPanel.add(passwordPanel, gbc);
        
        // Adiciona o botão de registro
        gbc.insets = new Insets(15, 10, 8, 10); // Reduz a margem inferior para 8px (era 15px)
        registerPanel.add(registerButton, gbc);
        
        // Cria um botão de texto simples e bem visível para "Voltar ao login"
        JLabel backToLoginLabel = new JLabel("Voltar ao login", SwingConstants.CENTER);
        backToLoginLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backToLoginLabel.setForeground(VERDE_PRINCIPAL);
        backToLoginLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Adiciona efeito de underline quando passa o mouse
        backToLoginLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                backToLoginLabel.setText("<html><u>Voltar ao login</u></html>");
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                backToLoginLabel.setText("Voltar ao login");
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                animateCardSwitch(false);
            }
        });
        
        // Adiciona o label clicável "Voltar ao login" - com margem próxima do botão acima
        gbc.insets = new Insets(0, 10, 40, 10); // Margem superior reduzida para 0px
        registerPanel.add(backToLoginLabel, gbc);
        
        // Adiciona o painel de registro ao cardPanel
        cardPanel.add(registerPanel, "register");
    }
    
    private JPanel createPasswordFieldWithStrengthMeter(String labelText, String placeholder, String iconName) {
        JPanel panel = new JPanel(new BorderLayout(0, 5));
        panel.setBackground(BRANCO);
        
        // Painel para o label com ícone
        JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        labelPanel.setBackground(BRANCO);
        
        // Carrega o ícone do label
        ImageIcon originalIcon = loadIcon("icons/" + iconName);
        ImageIcon scaledIcon = null;
        
        if (originalIcon != null) {
            // Redimensiona o ícone para um tamanho padrão (16x16 pixels)
            Image image = originalIcon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
            scaledIcon = new ImageIcon(image);
        }
        
        // Cria o label com o ícone
        JLabel iconLabel = new JLabel(scaledIcon);
        JLabel textLabel = new JLabel(labelText);
        textLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        textLabel.setForeground(CINZA_ESCURO);
        
        labelPanel.add(iconLabel);
        labelPanel.add(textLabel);
        
        // Painel personalizado para o campo de senha com ícone interno
        JPanel customPasswordPanel = new JPanel(new BorderLayout());
        customPasswordPanel.setBackground(BRANCO);
        
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 0));
        passwordField.setText(placeholder);
        passwordField.setForeground(new Color(180, 180, 180));
        passwordField.setEchoChar((char) 0); // Desativa o echo char inicialmente
        
        // Cria o botão de toggle com o ícone de olho cruzado inicialmente
        JButton toggleButton = new JButton(eyeCrossedIcon);
        toggleButton.setBorderPainted(false);
        toggleButton.setContentAreaFilled(false);
        toggleButton.setFocusPainted(false);
        toggleButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        toggleButton.setMargin(new Insets(0, 0, 0, 10));
        toggleButton.setToolTipText("Mostrar/ocultar senha");
        
        // Estado atual (inicialmente senha oculta)
        final boolean[] passwordVisible = {false};
        
        toggleButton.addActionListener(e -> {
            if (String.valueOf(passwordField.getPassword()).equals(placeholder)) {
                return; // Não fazer nada quando for o placeholder
            }
            
            passwordVisible[0] = !passwordVisible[0];
            
            if (passwordVisible[0]) {
                // Mostrar senha
                passwordField.setEchoChar((char) 0);
                toggleButton.setIcon(eyeIcon);
            } else {
                // Ocultar senha
                passwordField.setEchoChar('•');
                toggleButton.setIcon(eyeCrossedIcon);
            }
        });
        
        // Painel para o botão de toggle
        JPanel toggleButtonPanel = new JPanel(new BorderLayout());
        toggleButtonPanel.setBackground(BRANCO);
        toggleButtonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        toggleButtonPanel.add(toggleButton);
        
        // Indicador de força da senha
        JPanel strengthPanel = new JPanel(new BorderLayout(5, 0));
        strengthPanel.setBackground(BRANCO);
        
        JProgressBar strengthMeter = new JProgressBar(0, 100);
        strengthMeter.setValue(0);
        strengthMeter.setStringPainted(true);
        strengthMeter.setString("Força da senha");
        strengthMeter.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        strengthMeter.setForeground(CINZA_ESCURO);
        
        JLabel strengthLabel = new JLabel("Digite sua senha");
        strengthLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        strengthLabel.setForeground(CINZA_ESCURO);
        
        strengthPanel.add(strengthMeter, BorderLayout.CENTER);
        strengthPanel.add(strengthLabel, BorderLayout.EAST);
        
        // Adiciona listener para validar a força da senha durante a digitação
        passwordField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updatePasswordStrength();
            }
            
            @Override
            public void removeUpdate(DocumentEvent e) {
                updatePasswordStrength();
            }
            
            @Override
            public void changedUpdate(DocumentEvent e) {
                updatePasswordStrength();
            }
            
            private void updatePasswordStrength() {
                String password = String.valueOf(passwordField.getPassword());
                
                // Não analisa quando está vazio ou com o placeholder
                if (password.isEmpty() || password.equals(placeholder)) {
                    strengthMeter.setValue(0);
                    strengthMeter.setString("Força da senha");
                    strengthMeter.setForeground(CINZA_CLARO);
                    strengthLabel.setText("Digite sua senha");
                    return;
                }
                
                // Calcula a força da senha
                int strength = calculatePasswordStrength(password);
                strengthMeter.setValue(strength);
                
                // Define cor e texto com base na força
                if (strength < 30) {
                    strengthMeter.setForeground(Color.RED);
                    strengthMeter.setString("Fraca");
                    strengthLabel.setText("Senha muito fraca");
                    strengthLabel.setForeground(Color.RED);
                } else if (strength < 60) {
                    strengthMeter.setForeground(Color.ORANGE);
                    strengthMeter.setString("Média");
                    strengthLabel.setText("Adicione mais caracteres");
                    strengthLabel.setForeground(Color.ORANGE);
                } else if (strength < 80) {
                    strengthMeter.setForeground(Color.YELLOW.darker());
                    strengthMeter.setString("Boa");
                    strengthLabel.setText("Senha aceitável");
                    strengthLabel.setForeground(Color.YELLOW.darker());
                } else {
                    strengthMeter.setForeground(VERDE_PRINCIPAL);
                    strengthMeter.setString("Forte");
                    strengthLabel.setText("Senha forte");
                    strengthLabel.setForeground(VERDE_PRINCIPAL);
                }
            }
        });
        
        // Adiciona efeitos de foco
        passwordField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (String.valueOf(passwordField.getPassword()).equals(placeholder)) {
                    passwordField.setText("");
                    passwordField.setEchoChar('•');
                    passwordField.setForeground(CINZA_ESCURO);
                    
                    // Restaura o ícone de olho cruzado ao focar
                    toggleButton.setIcon(eyeCrossedIcon);
                    passwordVisible[0] = false;
                }
                
                // Animação de foco
                Timer focusTimer = new Timer(20, null);
                focusTimer.addActionListener(new ActionListener() {
                    int step = 0;
                    final int steps = 10;
                    
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        step++;
                        float ratio = (float) step / steps;
                        
                        // Interpola as cores
                        Color borderColor = interpolateColor(CINZA_CLARO, VERDE_PRINCIPAL, ratio);
                        
                        // Atualiza o painel customizado
                        customPasswordPanel.setBorder(BorderFactory.createLineBorder(borderColor, 1 + (int)(ratio), true));
                        
                        if (step >= steps) {
                            focusTimer.stop();
                        }
                    }
                });
                focusTimer.start();
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                if (String.valueOf(passwordField.getPassword()).isEmpty()) {
                    passwordField.setText(placeholder);
                    passwordField.setEchoChar((char) 0);
                    passwordField.setForeground(new Color(180, 180, 180));
                }
                
                // Animação de perda de foco
                Timer blurTimer = new Timer(20, null);
                blurTimer.addActionListener(new ActionListener() {
                    int step = 0;
                    final int steps = 10;
                    
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        step++;
                        float ratio = (float) step / steps;
                        
                        // Interpola as cores invertendo a ordem
                        Color borderColor = interpolateColor(VERDE_PRINCIPAL, CINZA_CLARO, ratio);
                        
                        // Atualiza o painel customizado
                        customPasswordPanel.setBorder(BorderFactory.createLineBorder(borderColor, 2 - (int)(ratio), true));
                        
                        if (step >= steps) {
                            blurTimer.stop();
                        }
                    }
                });
                blurTimer.start();
            }
        });
        
        // Adicionar tecla ENTER para acionar o registro
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // Como este é o campo de senha com medidor de força, 
                    // ele só existe no painel de registro
                    realizarRegistro();
                }
            }
        });
        
        // Combina o campo de senha e o botão no painel personalizado
        customPasswordPanel.add(passwordField, BorderLayout.CENTER);
        customPasswordPanel.add(toggleButtonPanel, BorderLayout.EAST);
        customPasswordPanel.setBorder(BorderFactory.createLineBorder(CINZA_CLARO, 1, true));
        
        // Painel principal que contém tudo
        panel.add(labelPanel, BorderLayout.NORTH);
        panel.add(customPasswordPanel, BorderLayout.CENTER);
        panel.add(strengthPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createTextField(String labelText, String placeholder, String iconName) {
        JPanel panel = new JPanel(new BorderLayout(0, 5));
        panel.setBackground(BRANCO);
        
        // Painel para o label com ícone
        JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        labelPanel.setBackground(BRANCO);
        
        // Carrega o ícone
        ImageIcon originalIcon = loadIcon("icons/" + iconName);
        ImageIcon scaledIcon = null;
        
        if (originalIcon != null) {
            // Redimensiona o ícone para um tamanho padrão (16x16 pixels)
            Image image = originalIcon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
            scaledIcon = new ImageIcon(image);
        }
        
        // Cria o label com o ícone
        JLabel iconLabel = new JLabel(scaledIcon);
        JLabel textLabel = new JLabel(labelText);
        textLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        textLabel.setForeground(CINZA_ESCURO);
        
        labelPanel.add(iconLabel);
        labelPanel.add(textLabel);
        
        JTextField textField = new JTextField(20);
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(CINZA_CLARO, 1, true),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        textField.setText(placeholder);
        textField.setForeground(new Color(180, 180, 180));
        
        // Adiciona efeitos de foco com animação
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(CINZA_ESCURO);
                }
                
                // Animação de foco
                Timer focusTimer = new Timer(20, null);
                focusTimer.addActionListener(new ActionListener() {
                    int step = 0;
                    final int steps = 10;
                    
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        step++;
                        float ratio = (float) step / steps;
                        
                        // Interpola as cores
                        Color borderColor = interpolateColor(CINZA_CLARO, VERDE_PRINCIPAL, ratio);
                        
                        textField.setBorder(BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(borderColor, 1 + (int)(ratio), true),
                                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
                        
                        if (step >= steps) {
                            focusTimer.stop();
                        }
                    }
                });
                focusTimer.start();
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setText(placeholder);
                    textField.setForeground(new Color(180, 180, 180));
                }
                
                // Animação de perda de foco
                Timer blurTimer = new Timer(20, null);
                blurTimer.addActionListener(new ActionListener() {
                    int step = 0;
                    final int steps = 10;
                    
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        step++;
                        float ratio = (float) step / steps;
                        
                        // Interpola as cores invertendo a ordem
                        Color borderColor = interpolateColor(VERDE_PRINCIPAL, CINZA_CLARO, ratio);
                        
                        textField.setBorder(BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(borderColor, 2 - (int)(ratio), true),
                                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
                        
                        if (step >= steps) {
                            blurTimer.stop();
                        }
                    }
                });
                blurTimer.start();
            }
        });
        
        // Adicionar tecla ENTER para avançar para o próximo campo ou acionar botão
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // Próximo componente ou ação de botão
                    KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent();
                    
                    // Se for o último campo, aciona o botão de login/registro
                    Container parent = panel.getParent();
                    if (parent != null) {
                        if (parent == loginPanel && labelText.equals("E-mail")) {
                            // Vai para o campo de senha
                            passwordLoginField.requestFocus();
                        } else if (parent == registerPanel) {
                            if (labelText.equals("Nome completo")) {
                                // Vai para o campo de e-mail
                                emailRegisterField.requestFocus();
                            } else if (labelText.equals("E-mail")) {
                                // Vai para o campo de senha
                                passwordRegisterField.requestFocus();
                            }
                        }
                    }
                }
            }
        });
        
        panel.add(labelPanel, BorderLayout.NORTH);
        panel.add(textField, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JButton createAnimatedButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    // Pressionado - cor mais escura e efeito de sombra interna
                    g2.setColor(bgColor.darker());
                    g2.fill(new RoundRectangle2D.Double(2, 2, getWidth() - 4, getHeight() - 4, 12, 12));
                } else if (getModel().isRollover()) {
                    // Hover - cor mais clara e efeito de brilho
                    g2.setColor(bgColor.brighter());
                    g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 12, 12));
                    
                    // Adiciona um brilho sutil
                    g2.setColor(new Color(255, 255, 255, 50));
                    g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight() / 2, 12, 12));
                } else {
                    // Normal
                    g2.setColor(bgColor);
                    g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 12, 12));
                }
                
                g2.dispose();
                
                super.paintComponent(g);
            }
        };
        
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(fgColor);
        button.setBackground(bgColor);
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Adiciona efeito de click com feedback visual
        button.addActionListener(e -> {
            // Efeito visual de "pulsação" ao clicar
            Timer pulseTimer = new Timer(30, null);
            pulseTimer.addActionListener(new ActionListener() {
                int step = 0;
                final int steps = 5;
                boolean growing = false;
                
                @Override
                public void actionPerformed(ActionEvent e) {
                    step++;
                    
                    if (step <= steps && !growing) {
                        // Encolher
                        button.setBorder(BorderFactory.createEmptyBorder(10 + step, 25 - step, 10 + step, 25 - step));
                    } else if (step <= steps * 2) {
                        // Crescer
                        growing = true;
                        button.setBorder(BorderFactory.createEmptyBorder(10 + (steps * 2 - step), 25 - (steps * 2 - step), 
                                                                       10 + (steps * 2 - step), 25 - (steps * 2 - step)));
                    } else {
                        // Restaurar
                        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
                        pulseTimer.stop();
                    }
                    
                    button.revalidate();
                    button.repaint();
                }
            });
            pulseTimer.start();
        });
        
        return button;
    }
    
    // Animação de digitação de texto
    private void animateTypingText(JLabel label, String finalText, int delay) {
        label.setText("");
        
        Timer typingTimer = new Timer(delay, null);
        typingTimer.addActionListener(new ActionListener() {
            int charIndex = 0;
            
            @Override
            public void actionPerformed(ActionEvent e) {
                if (charIndex < finalText.length()) {
                    label.setText(label.getText() + finalText.charAt(charIndex));
                    charIndex++;
                } else {
                    ((Timer) e.getSource()).stop();
                }
            }
        });
        
        typingTimer.start();
    }
    
    // Animação de transição entre os painéis
    private void animateCardSwitch(boolean toRegister) {
        CardLayout cl = (CardLayout) cardPanel.getLayout();
        if (toRegister) {
            cl.show(cardPanel, "register");
            // Se quiser adicionar animação de digitação ao título do registro
            JLabel titleLabel = findTitleLabel(registerPanel);
            if (titleLabel != null) {
                animateTypingText(titleLabel, "Crie sua conta", 80);
            }
        } else {
            cl.show(cardPanel, "login");
            // Se quiser adicionar animação de digitação ao título do login
            JLabel titleLabel = findTitleLabel(loginPanel);
            if (titleLabel != null) {
                animateTypingText(titleLabel, "Bem-vindo de volta!", 80);
            }
        }
        isLoginShowing = !toRegister;
    }
    
    // Método auxiliar para calcular a força da senha
    private int calculatePasswordStrength(String password) {
        int strength = 0;
        
        // Comprimento (até 40 pontos)
        int length = password.length();
        strength += Math.min(40, length * 4);
        
        // Variedade de caracteres (até 30 pontos)
        boolean hasLowercase = false;
        boolean hasUppercase = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;
        
        for (char c : password.toCharArray()) {
            if (Character.isLowerCase(c)) hasLowercase = true;
            else if (Character.isUpperCase(c)) hasUppercase = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else hasSpecial = true;
        }
        
        int typesCount = 0;
        if (hasLowercase) typesCount++;
        if (hasUppercase) typesCount++;
        if (hasDigit) typesCount++;
        if (hasSpecial) typesCount++;
        
        strength += typesCount * 10;
        
        // Complexidade adicional (até 30 pontos)
        if (length > 8 && typesCount >= 3) strength += 10;
        if (length > 12 && typesCount == 4) strength += 20;
        
        return Math.min(100, strength);
    }
    
    // Utilitário para encontrar o label do título
    private JLabel findTitleLabel(Container container) {
        for (Component component : container.getComponents()) {
            if (component instanceof JLabel) {
                JLabel label = (JLabel) component;
                Font font = label.getFont();
                if (font != null && font.getSize() >= 24) {
                    return label;
                }
            } else if (component instanceof Container) {
                JLabel found = findTitleLabel((Container) component);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }
    
    // Método auxiliar para carregar ícones
    private ImageIcon loadIcon(String path) {
        try {
            // Caminho para os ícones e imagens baseado na estrutura do projeto
            String basePath = "C:\\Users\\Joao\\Documents\\2 - SOFTWARE\\Espaço Capital\\Coworking-System\\demo\\src\\main\\resources\\";
            String fullPath = basePath;
            
            // Se o caminho já inclui "icons/", não adicione "icons/" novamente
            if (path.startsWith("icons/")) {
                fullPath += path;
            } else if (path.startsWith("flyer")) {
                // Se for um arquivo de flyer
                fullPath += path;
            } else {
                // Caso contrário, assume que é um ícone
                fullPath += "icons/" + path;
            }
            
            File file = new File(fullPath);
            if (file.exists()) {
                return new ImageIcon(file.getAbsolutePath());
            } else {
                System.err.println("Arquivo não encontrado: " + fullPath);
                return null;
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar ícone: " + path);
            e.printStackTrace();
            return null;
        }
    }
    // Método auxiliar para interpolar cores
    private Color interpolateColor(Color c1, Color c2, float ratio) {
        int red = (int) (c1.getRed() * (1 - ratio) + c2.getRed() * ratio);
        int green = (int) (c1.getGreen() * (1 - ratio) + c2.getGreen() * ratio);
        int blue = (int) (c1.getBlue() * (1 - ratio) + c2.getBlue() * ratio);
        return new Color(red, green, blue);
    }
    
    // Métodos adicionados para a funcionalidade de login/registro
    
    // Obter o campo de texto de um painel
    private JTextField getTextFieldFromPanel(JPanel panel) {
        for (Component component : panel.getComponents()) {
            if (component instanceof JTextField) {
                return (JTextField) component;
            } else if (component instanceof JPanel) {
                Component[] subComponents = ((JPanel) component).getComponents();
                for (Component subComponent : subComponents) {
                    if (subComponent instanceof JTextField) {
                        return (JTextField) subComponent;
                    }
                }
            }
        }
        return null;
    }
    
    // Obter o campo de senha de um painel
    private JPasswordField getPasswordFieldFromPanel(JPanel panel) {
        for (Component component : panel.getComponents()) {
            if (component instanceof JPasswordField) {
                return (JPasswordField) component;
            } else if (component instanceof JPanel) {
                JPanel subPanel = (JPanel) component;
                for (Component subComponent : subPanel.getComponents()) {
                    if (subComponent instanceof JPasswordField) {
                        return (JPasswordField) subComponent;
                    } else if (subComponent instanceof JPanel) {
                        // Busca recursiva mais profunda
                        Component[] subSubComponents = ((JPanel) subComponent).getComponents();
                        for (Component subSubComponent : subSubComponents) {
                            if (subSubComponent instanceof JPasswordField) {
                                return (JPasswordField) subSubComponent;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
    
    // Verificar se um e-mail já existe no CSV
    private boolean verificarEmailExistente(String email) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE));
            String line;
            
            // Pula a linha de cabeçalho
            reader.readLine();
            
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String csvEmail = parts[1].trim();
                    
                    if (csvEmail.equals(email)) {
                        reader.close();
                        return true;
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao verificar e-mail: " + e.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }
    
    // Verificar credenciais no CSV
    private boolean verificarCredenciais(String email, String senha) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE));
            String line;
            
            // Pula a linha de cabeçalho
            reader.readLine();
            
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String csvEmail = parts[1].trim();
                    String csvSenha = parts[2].trim();
                    
                    if (csvEmail.equals(email) && csvSenha.equals(senha)) {
                        reader.close();
                        return true;
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao verificar credenciais: " + e.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }
    
    // Validar formato de e-mail usando expressão regular
    private boolean validarFormatoEmail(String email) {
        String regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        return email.matches(regex);
    }
    
    // Salvar usuário no CSV
    private boolean salvarUsuarioNoCSV(String nome, String email, String senha) {
        try {
            // Escapar vírgulas para não quebrar o formato CSV
            nome = nome.replace(",", ";");
            email = email.replace(",", ";");
            senha = senha.replace(",", ";");
            
            // Abrir arquivo para adicionar (append)
            FileWriter fw = new FileWriter(CSV_FILE, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter writer = new PrintWriter(bw);
            
            // Escrever a nova linha no CSV
            writer.println(nome + "," + email + "," + senha);
            writer.close();
            
            return true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao salvar usuário: " + e.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    // Realizar login
    private void realizarLogin() {
        String email = emailLoginField.getText();
        String senha = new String(passwordLoginField.getPassword());
        
        // Verificar se os campos não estão vazios ou com placeholders
        if (email.isEmpty() || email.equals("Seu e-mail") || 
            senha.isEmpty() || senha.equals("Sua senha")) {
            JOptionPane.showMessageDialog(this, 
                "Por favor, preencha todos os campos.", 
                "Erro de Login", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Verificar credenciais no CSV
        if (verificarCredenciais(email, senha)) {
            JOptionPane.showMessageDialog(this, 
                "Login realizado com sucesso!\nBem-vindo ao Espaço Capital.", 
                "Sucesso", 
                JOptionPane.INFORMATION_MESSAGE);
            
            // Aqui você pode abrir a próxima tela do sistema
            // Por exemplo: 
            // dispose(); // Fecha a tela de login
            // new TelaInicial(email); // Abre a tela inicial passando o e-mail do usuário
        } else {
            JOptionPane.showMessageDialog(this, 
                "E-mail ou senha incorretos.", 
                "Erro de Login", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Realizar registro
    private void realizarRegistro() {
        String nome = nameRegisterField.getText();
        String email = emailRegisterField.getText();
        String senha = new String(passwordRegisterField.getPassword());
        
        // Validar campos
        if (nome.isEmpty() || nome.equals("Seu nome") || 
            email.isEmpty() || email.equals("Seu e-mail") || 
            senha.isEmpty() || senha.equals("Sua senha")) {
            JOptionPane.showMessageDialog(this, 
                "Por favor, preencha todos os campos.", 
                "Erro de Registro", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Validar formato de e-mail
        if (!validarFormatoEmail(email)) {
            JOptionPane.showMessageDialog(this, 
                "Por favor, insira um e-mail válido.", 
                "Erro de Registro", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Validar força da senha
        if (calculatePasswordStrength(senha) < 60) {
            JOptionPane.showMessageDialog(this, 
                "Por favor, escolha uma senha mais forte.", 
                "Senha Fraca", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Verificar se o e-mail já está cadastrado
        if (verificarEmailExistente(email)) {
            JOptionPane.showMessageDialog(this, 
                "Este e-mail já está cadastrado.", 
                "E-mail Duplicado", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Salvar no CSV
        if (salvarUsuarioNoCSV(nome, email, senha)) {
            JOptionPane.showMessageDialog(this, 
                "Registro realizado com sucesso!\nFaça login para continuar.", 
                "Sucesso", 
                JOptionPane.INFORMATION_MESSAGE);
            
            // Limpar campos
            nameRegisterField.setText("Seu nome");
            nameRegisterField.setForeground(new Color(180, 180, 180));
            
            emailRegisterField.setText("Seu e-mail");
            emailRegisterField.setForeground(new Color(180, 180, 180));
            
            passwordRegisterField.setText("Sua senha");
            passwordRegisterField.setEchoChar((char) 0);
            passwordRegisterField.setForeground(new Color(180, 180, 180));
            
            // Voltar para a tela de login
            animateCardSwitch(false);
        }
    }
    
    // Método main para teste
    public static void main(String[] args) {
        // Configura o look and feel FlatLaf
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
            
            // Customização global do UI
            UIManager.put("Button.arc", 12);
            UIManager.put("Component.arc", 12);
            UIManager.put("ProgressBar.arc", 12);
            UIManager.put("TextComponent.arc", 12);
            
            // Cores personalizadas para o FlatLaf
            UIManager.put("Button.background", Color.decode("#007a3e"));
            UIManager.put("Button.foreground", Color.WHITE);
            UIManager.put("TextField.focusedBorderColor", Color.decode("#007a3e"));
            UIManager.put("PasswordField.focusedBorderColor", Color.decode("#007a3e"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Inicia o frame de login
        SwingUtilities.invokeLater(() -> new LoginUI());
    }
}