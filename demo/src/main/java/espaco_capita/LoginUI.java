package espaco_capita;

import com.formdev.flatlaf.FlatLightLaf;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Interface de Login/Registro para o sistema de Espaço Capital - Coworking System
 * Versão melhorada com animações e UX aprimorada
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
    private JLayeredPane cardPanel;
    
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
        
        // Inicializa os componentes
        initComponents();
        
        // Exibe o frame
        setVisible(true);
    }
    
    private void initComponents() {
        // Painel principal com BorderLayout
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BRANCO);
        
        // Painel esquerdo para a imagem/logo da empresa
        leftPanel = new JPanel(new BorderLayout());
        leftPanel.setPreferredSize(new Dimension(450, 550));
        leftPanel.setBackground(VERDE_PRINCIPAL);
        
        // Cria um painel customizado para o lado esquerdo com imagem
        JPanel logoPanel = createBrandPanel();
        leftPanel.add(logoPanel, BorderLayout.CENTER);
        
        // Painel direito para login/registro
        rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(BRANCO);
        
        // LayeredPane para animações
        cardPanel = new JLayeredPane();
        cardPanel.setLayout(null); // Usamos posicionamento absoluto para animações
        
        // Cria painel de login
        createLoginPanel();
        
        // Cria painel de registro
        createRegisterPanel();
        
        // Inicialmente, o painel de registro está fora da tela (à direita)
        loginPanel.setBounds(0, 0, 450, 550);
        registerPanel.setBounds(450, 0, 450, 550); // Começa fora da tela
        
        // Ajusta o painel direito
        rightPanel.add(cardPanel, BorderLayout.CENTER);
        
        // Adiciona os painéis ao painel principal
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);
        
        // Adiciona o painel principal ao frame
        setContentPane(mainPanel);
    }
    
    private JPanel createBrandPanel() {
        JPanel panel = new JPanel() {
            private final Image backgroundImage;
            
            {
                // Carregar a imagem
                String imagePath = "C:\\Users\\joaog\\Documents\\Programas\\Espaço Capital - Coworking System\\demo\\src\\main\\resources\\flyer1.png";
                Image img = null;
                try {
                    img = ImageIO.read(new File(imagePath));
                } catch (IOException e) {
                    System.err.println("Erro ao carregar a imagem: " + e.getMessage());
                    e.printStackTrace();
                }
                backgroundImage = img;
            }
            
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (backgroundImage != null) {
                    // Desenha a imagem para preencher todo o painel
                    g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                    
                    // Adiciona uma camada parcialmente transparente por cima para melhorar a legibilidade
                    g2.setColor(new Color(0, 0, 0, 40));
                    g2.fillRect(0, 0, getWidth(), getHeight());
                } else {
                    // Fallback para o desenho original caso a imagem não carregue
                    GradientPaint gp = new GradientPaint(0, 0, VERDE_PRINCIPAL, 
                                                       getWidth(), getHeight(), 
                                                       new Color(0, 100, 50));
                    g2.setPaint(gp);
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    
                    // Desenha círculos decorativos semitransparentes
                    drawDecorativeCircles(g2);
                }
                
                g2.dispose();
            }
        };
        
        // Layout vazio pois estamos usando apenas a imagem
        panel.setLayout(new BorderLayout());
        
        return panel;
    }
    
    private void drawDecorativeCircles(Graphics2D g2) {
        // Círculos grandes
        g2.setColor(new Color(255, 255, 255, 30));
        g2.fillOval(-50, -50, 200, 200);
        g2.fillOval(300, 400, 250, 250);
        
        // Círculos médios
        g2.setColor(new Color(255, 255, 255, 20));
        g2.fillOval(350, -50, 200, 200);
        g2.fillOval(50, 350, 150, 150);
        
        // Círculos pequenos
        g2.setColor(new Color(255, 255, 255, 15));
        int numCircles = 10;
        for (int i = 0; i < numCircles; i++) {
            int size = 20 + (int)(Math.random() * 40);
            int x = (int)(Math.random() * 450);
            int y = (int)(Math.random() * 550);
            g2.fillOval(x, y, size, size);
        }
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
        JLabel titleLabel = new JLabel("");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(CINZA_ESCURO);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Anima o título como se estivesse digitando
        String titleText = "Bem-vindo de volta!";
        animateTypingText(titleLabel, titleText, 80);
        
        // Subtítulo
        JLabel subtitleLabel = new JLabel("Faça login na sua conta");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(CINZA_ESCURO);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Campos customizados
        JPanel emailPanel = createTextField("E-mail", "Seu e-mail");
        JPanel passwordPanel = createPasswordField("Senha", "Sua senha");
        
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
        });
        
        // Botão de login customizado
        JButton loginButton = createAnimatedButton("Entrar", VERDE_PRINCIPAL, BRANCO);
        loginButton.addActionListener(e -> {
            // Lógica de login aqui
            JOptionPane.showMessageDialog(this, "Funcionalidade de login a ser implementada");
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
        gbc.insets = new Insets(20, 10, 10, 10);
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
        cardPanel.add(loginPanel, Integer.valueOf(1));
    }
    
    private void createRegisterPanel() {
        registerPanel = new JPanel(new GridBagLayout());
        registerPanel.setBackground(BRANCO);
        registerPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(5, 10, 5, 10);
        
        // Título com animação de digitação
        JLabel titleLabel = new JLabel("");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(CINZA_ESCURO);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Subtítulo
        JLabel subtitleLabel = new JLabel("Preencha seus dados para se registrar");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(CINZA_ESCURO);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Campos customizados
        JPanel namePanel = createTextField("Nome completo", "Seu nome");
        JPanel emailPanel = createTextField("E-mail", "Seu e-mail");
        JPanel passwordPanel = createPasswordField("Senha", "Sua senha");
        JPanel confirmPasswordPanel = createPasswordField("Confirmar senha", "Confirme sua senha");
        
        // Botão de registro customizado
        JButton registerButton = createAnimatedButton("Registrar", VERDE_PRINCIPAL, BRANCO);
        registerButton.addActionListener(e -> {
            // Lógica de registro aqui
            JOptionPane.showMessageDialog(this, "Funcionalidade de registro a ser implementada");
        });
        
        // Botão para alternar para o painel de login
        JPanel loginLinkPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        loginLinkPanel.setBackground(BRANCO);
        
        JLabel hasAccountLabel = new JLabel("Já tem uma conta? ");
        hasAccountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        hasAccountLabel.setForeground(CINZA_ESCURO);
        
        JButton switchToLoginButton = new JButton("Faça login");
        switchToLoginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        switchToLoginButton.setForeground(VERDE_PRINCIPAL);
        switchToLoginButton.setBorderPainted(false);
        switchToLoginButton.setContentAreaFilled(false);
        switchToLoginButton.setFocusPainted(false);
        switchToLoginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        switchToLoginButton.addActionListener(e -> animateCardSwitch(false));
        
        // Adiciona underline ao passar o mouse
        switchToLoginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                switchToLoginButton.setText("<html><u>Faça login</u></html>");
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                switchToLoginButton.setText("Faça login");
            }
        });
        
        loginLinkPanel.add(hasAccountLabel);
        loginLinkPanel.add(switchToLoginButton);
        
        // Adiciona os componentes ao painel de registro
        gbc.insets = new Insets(10, 10, 10, 10);
        registerPanel.add(titleLabel, gbc);
        
        gbc.insets = new Insets(0, 10, 15, 10);
        registerPanel.add(subtitleLabel, gbc);
        
        gbc.insets = new Insets(5, 10, 5, 10);
        registerPanel.add(namePanel, gbc);
        registerPanel.add(emailPanel, gbc);
        registerPanel.add(passwordPanel, gbc);
        registerPanel.add(confirmPasswordPanel, gbc);
        
        gbc.insets = new Insets(15, 10, 5, 10);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(BRANCO);
        buttonPanel.add(registerButton);
        registerPanel.add(buttonPanel, gbc);
        
        gbc.insets = new Insets(10, 10, 10, 10);
        registerPanel.add(loginLinkPanel, gbc);
        
        // Adiciona o painel de registro ao cardPanel
        cardPanel.add(registerPanel, Integer.valueOf(0));
    }
    
    private JPanel createTextField(String labelText, String placeholder) {
        JPanel panel = new JPanel(new BorderLayout(0, 5));
        panel.setBackground(BRANCO);
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(CINZA_ESCURO);
        
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
                // Animação suave de troca de borda
                animateTextFieldFocus(textField, placeholder, true);
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                // Animação suave de retorno
                animateTextFieldFocus(textField, placeholder, false);
            }
        });
        
        panel.add(label, BorderLayout.NORTH);
        panel.add(textField, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void animateTextFieldFocus(JTextField textField, String placeholder, boolean gaining) {
        if (gaining) {
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
        } else {
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
    }
    
    private Color interpolateColor(Color c1, Color c2, float ratio) {
        int red = (int) (c1.getRed() * (1 - ratio) + c2.getRed() * ratio);
        int green = (int) (c1.getGreen() * (1 - ratio) + c2.getGreen() * ratio);
        int blue = (int) (c1.getBlue() * (1 - ratio) + c2.getBlue() * ratio);
        return new Color(red, green, blue);
    }
    
    private JPanel createPasswordField(String labelText, String placeholder) {
        JPanel panel = new JPanel(new BorderLayout(0, 5));
        panel.setBackground(BRANCO);
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(CINZA_ESCURO);
        
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(CINZA_CLARO, 1, true),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        passwordField.setText(placeholder);
        passwordField.setForeground(new Color(180, 180, 180));
        passwordField.setEchoChar((char) 0); // Desativa o echo char inicialmente
        
        // Adiciona botão para mostrar/ocultar senha com animação
        JButton toggleButton = new JButton("\uD83D\uDC41️"); // Emoji de olho
        toggleButton.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        toggleButton.setBorderPainted(false);
        toggleButton.setContentAreaFilled(false);
        toggleButton.setFocusPainted(false);
        toggleButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Adiciona tooltip
        toggleButton.setToolTipText("Mostrar/ocultar senha");
        
        toggleButton.addActionListener(e -> {
            if (passwordField.getEchoChar() == 0 && !String.valueOf(passwordField.getPassword()).equals(placeholder)) {
                passwordField.setEchoChar('•');
                toggleButton.setText("\uD83D\uDC41️\u200D\uD83D\uDDE8"); // Olho riscado
            } else {
                passwordField.setEchoChar((char) 0);
                toggleButton.setText("\uD83D\uDC41️");
            }
        });
        
        // Adiciona efeitos de foco com animação
        passwordField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (String.valueOf(passwordField.getPassword()).equals(placeholder)) {
                    passwordField.setText("");
                    passwordField.setEchoChar('•');
                    passwordField.setForeground(CINZA_ESCURO);
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
                        
                        passwordField.setBorder(BorderFactory.createCompoundBorder(
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
                        
                        passwordField.setBorder(BorderFactory.createCompoundBorder(
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
        
        JPanel fieldPanel = new JPanel(new BorderLayout());
        fieldPanel.setBackground(BRANCO);
        fieldPanel.add(passwordField, BorderLayout.CENTER);
        fieldPanel.add(toggleButton, BorderLayout.EAST);
        
        panel.add(label, BorderLayout.NORTH);
        panel.add(fieldPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JButton createAnimatedButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Desenha background do botão com efeitos
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
    
    // Animação de transição entre os painéis
    private void animateCardSwitch(boolean toRegister) {
        // Evita iniciar várias animações ao mesmo tempo
        if (animationTimer != null && animationTimer.isRunning()) {
            return;
        }
        
        animationStep = 0;
        
        // Define o painel que está à frente durante a animação
        if (toRegister) {
            cardPanel.setLayer(registerPanel, JLayeredPane.DRAG_LAYER);
            cardPanel.setLayer(loginPanel, JLayeredPane.DEFAULT_LAYER);
        } else {
            cardPanel.setLayer(loginPanel, JLayeredPane.DRAG_LAYER);
            cardPanel.setLayer(registerPanel, JLayeredPane.DEFAULT_LAYER);
        }
        
        // Inicia a animação
        animationTimer = new Timer(ANIMATION_SPEED, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                animationStep++;
                float progress = (float) animationStep / ANIMATION_STEPS;
                
                if (toRegister) {
                    // Login desliza para a esquerda, registro desliza da direita
                    int loginX = (int) (-450 * progress);
                    int registerX = (int) (450 * (1 - progress));
                    
                    loginPanel.setBounds(loginX, 0, 450, 550);
                    registerPanel.setBounds(registerX, 0, 450, 550);
                    
                    // Só anima o título após parte da transição
                    if (animationStep == ANIMATION_STEPS / 2) {
                        JLabel titleLabel = (JLabel) findComponentByName(registerPanel, "titleLabel");
                        if (titleLabel != null) {
                            animateTypingText(titleLabel, "Crie sua conta", 80);
                        }
                    }
                } else {
                    // Registro desliza para a direita, login desliza da esquerda
                    int registerX = (int) (450 * progress);
                    int loginX = (int) (-450 * (1 - progress));
                    
                    registerPanel.setBounds(registerX, 0, 450, 550);
                    loginPanel.setBounds(loginX, 0, 450, 550);
                    
                    // Só anima o título após parte da transição
                    if (animationStep == ANIMATION_STEPS / 2) {
                        JLabel titleLabel = (JLabel) findComponentByName(loginPanel, "titleLabel");
                        if (titleLabel != null) {
                            animateTypingText(titleLabel, "Bem-vindo de volta!", 80);
                        }
                    }
                }
                
                if (animationStep >= ANIMATION_STEPS) {
                    // Atualiza o estado atual
                    isLoginShowing = !toRegister;
                    animationTimer.stop();
                    
                    // Ajusta a posição final para evitar problemas de layout
                    if (toRegister) {
                        loginPanel.setBounds(-450, 0, 450, 550);
                        registerPanel.setBounds(0, 0, 450, 550);
                    } else {
                        registerPanel.setBounds(450, 0, 450, 550);
                        loginPanel.setBounds(0, 0, 450, 550);
                    }
                }
            }
        });
        
        animationTimer.start();
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
    
    // Utilitário para encontrar componentes pelo nome
    private Component findComponentByName(Container container, String name) {
        for (Component component : container.getComponents()) {
            if (component instanceof JLabel && ((JLabel) component).getText().isEmpty()) {
                return component;
            } else if (component instanceof Container) {
                Component found = findComponentByName((Container) component, name);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
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
        
        // Inicia o frame de login com animações
        SwingUtilities.invokeLater(() -> new LoginUI());
    }
}