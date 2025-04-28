package espaco_capita;

import com.formdev.flatlaf.FlatLightLaf;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Interface de Login/Registro para o sistema de Espaço Capital - Coworking System
 * Versão melhorada com carrossel de imagens, animações e UX aprimorada
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
    private JPanel rightPanel;
    private JPanel loginPanel;
    private JPanel registerPanel;
    private JLayeredPane cardPanel;
    
    // Componente do carrossel
    private ImageCarousel carousel;
    
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
        
        // Cria o carrossel de imagens para o lado esquerdo
        String resourcesPath = "C:\\Users\\joaog\\Documents\\Programas\\Espaço Capital - Coworking System\\demo\\src\\main\\resources";
        List<String> imagePaths = new ArrayList<>();
        
        // Adiciona caminhos das imagens (atualize com os nomes corretos dos seus flyers)
        imagePaths.add(resourcesPath + "\\flyer1.png");
        
        // Você pode adicionar mais flyers ao carrossel aqui
        // Por exemplo:
         imagePaths.add(resourcesPath + "\\flyer2.png");
         imagePaths.add(resourcesPath + "\\flyer3.png");
        
        // Cria o carrossel
        carousel = new ImageCarousel(imagePaths, 450, 550, 5000); // 5000ms = 5s de intervalo entre slides
        carousel.setPreferredSize(new Dimension(450, 550));
        
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
        mainPanel.add(carousel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);
        
        // Adiciona o painel principal ao frame
        setContentPane(mainPanel);
    }
    
    /**
     * Classe interna para implementar o carrossel de imagens
     */
    private class ImageCarousel extends JPanel {
        private final List<Image> images = new ArrayList<>();
        private final List<String> imagePaths;
        private int currentIndex = 0;
        private final Timer timer;
        private final int width;
        private final int height;
        
        // Painel de indicadores
        private JPanel indicatorsPanel;
        
        public ImageCarousel(List<String> imagePaths, int width, int height, int interval) {
            this.imagePaths = imagePaths;
            this.width = width;
            this.height = height;
            
            setLayout(new BorderLayout());
            
            // Carrega as imagens
            loadImages();
            
            // Cria os indicadores
            createIndicators();
            
            // Cria o timer para troca automática de slides
            timer = new Timer(interval, e -> {
                nextImage();
                updateIndicators();
            });
            timer.start();
            
            // Adiciona evento para pausar o timer quando o mouse está sobre o carrossel
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    timer.stop();
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    timer.start();
                }
            });
            
            // Adiciona evento de clique para avançar manualmente
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    // Avança para a próxima imagem se clicado na metade direita
                    // Retorna para a imagem anterior se clicado na metade esquerda
                    if (e.getX() > width / 2) {
                        nextImage();
                    } else {
                        previousImage();
                    }
                    updateIndicators();
                }
            });
        }
        
        private void loadImages() {
            for (String path : imagePaths) {
                try {
                    Image img = ImageIO.read(new File(path));
                    if (img != null) {
                        // Redimensiona para caber no carrossel
                        img = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                        images.add(img);
                    }
                } catch (IOException e) {
                    System.err.println("Erro ao carregar a imagem: " + path);
                    e.printStackTrace();
                }
            }
            
            // Se não houver imagens carregadas, adiciona um placeholder
            if (images.isEmpty()) {
                BufferedImage placeholder = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                Graphics2D g2 = placeholder.createGraphics();
                g2.setColor(VERDE_PRINCIPAL);
                g2.fillRect(0, 0, width, height);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 24));
                g2.drawString("ESPAÇO CAPITAL", width/2 - 100, height/2 - 20);
                g2.drawString("Coworking System", width/2 - 90, height/2 + 20);
                g2.dispose();
                images.add(placeholder);
            }
        }
        
        private void createIndicators() {
            // Cria o painel de indicadores na parte inferior
            indicatorsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
            indicatorsPanel.setOpaque(false);
            
            // Cria indicadores baseados na quantidade de imagens
            for (int i = 0; i < images.size(); i++) {
                JButton indicator = createIndicatorButton(i == currentIndex);
                final int index = i;
                indicator.addActionListener(e -> {
                    currentIndex = index;
                    updateIndicators();
                    repaint();
                });
                indicatorsPanel.add(indicator);
            }
            
            // Adiciona os indicadores ao carrossel
            add(indicatorsPanel, BorderLayout.SOUTH);
        }
        
        private JButton createIndicatorButton(boolean isActive) {
            JButton button = new JButton();
            button.setPreferredSize(new Dimension(12, 12));
            button.setBackground(isActive ? Color.WHITE : new Color(255, 255, 255, 120));
            button.setBorder(BorderFactory.createEmptyBorder());
            button.setFocusPainted(false);
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            return button;
        }
        
        private void nextImage() {
            currentIndex = (currentIndex + 1) % images.size();
            repaint();
        }
        
        private void previousImage() {
            currentIndex = (currentIndex - 1 + images.size()) % images.size();
            repaint();
        }
        
        private void updateIndicators() {
            for (int i = 0; i < indicatorsPanel.getComponentCount(); i++) {
                Component comp = indicatorsPanel.getComponent(i);
                if (comp instanceof JButton) {
                    ((JButton) comp).setBackground(i == currentIndex ? 
                                                  Color.WHITE : new Color(255, 255, 255, 120));
                }
            }
            indicatorsPanel.revalidate();
            indicatorsPanel.repaint();
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (!images.isEmpty()) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                
                // Desenha a imagem atual
                g2.drawImage(images.get(currentIndex), 0, 0, this);
                
                // Adiciona uma camada parcialmente transparente por cima para melhorar a legibilidade
                g2.setColor(new Color(0, 0, 0, 80));
                g2.fillRect(0, 0, getWidth(), getHeight());
                
                g2.dispose();
            }
        }
    }
    
    // O restante do código permanece igual...
    // Métodos createLoginPanel(), createRegisterPanel(), etc.

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
        JPanel emailPanel = createTextField("E-mail", "Seu e-mail", "email.png");
        JPanel passwordPanel = createPasswordField("Senha", "Sua senha", "lock.png");
        
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
        JLabel titleLabel = new JLabel("");
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
        JPanel emailPanel = createTextField("E-mail", "Seu e-mail", "email.png");
        JPanel passwordPanel = createPasswordFieldWithStrengthMeter("Senha", "Sua senha", "lock.png");
        
        // Botão de registro customizado
        JButton registerButton = createAnimatedButton("Registrar", VERDE_PRINCIPAL, BRANCO);
        registerButton.addActionListener(e -> {
            // Lógica de registro aqui
            JOptionPane.showMessageDialog(this, "Funcionalidade de registro a ser implementada");
        });
        
        // Adiciona os componentes ao painel de registro (como estavam antes)
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
        cardPanel.add(registerPanel, Integer.valueOf(0));
    }

private JPanel createPasswordFieldWithStrengthMeter(String labelText, String placeholder, String iconName) {
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
    
    JPasswordField passwordField = new JPasswordField(20);
    passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(CINZA_CLARO, 1, true),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)));
    passwordField.setText(placeholder);
    passwordField.setForeground(new Color(180, 180, 180));
    passwordField.setEchoChar((char) 0); // Desativa o echo char inicialmente
    
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
    
    // Adiciona botão para mostrar/ocultar senha
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
    
    // Painel principal que contém tudo
    panel.add(labelPanel, BorderLayout.NORTH);
    panel.add(fieldPanel, BorderLayout.CENTER);
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
                // Animação suave de troca de borda
                animateTextFieldFocus(textField, placeholder, true);
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                // Animação suave de retorno
                animateTextFieldFocus(textField, placeholder, false);
            }
        });
        
        panel.add(labelPanel, BorderLayout.NORTH);
        panel.add(textField, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createPasswordField(String labelText, String placeholder, String iconName) {
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
        
        // Adiciona efeitos de foco com animação (código existente para isso)
        passwordField.addFocusListener(new FocusAdapter() {
            // Mantém o mesmo código que você tinha antes
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
        
        panel.add(labelPanel, BorderLayout.NORTH);
        panel.add(fieldPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    // Método auxiliar para carregar ícones
    private ImageIcon loadIcon(String path) {
        try {
            String fullPath = "C:\\Users\\joaog\\Documents\\Programas\\Espaço Capital - Coworking System\\demo\\src\\main\\resources\\" + path;
            return new ImageIcon(ImageIO.read(new File(fullPath)));
        } catch (IOException e) {
            System.err.println("Erro ao carregar ícone: " + path);
            e.printStackTrace();
            return null;
        }
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
        animationTimer.stop(); // Para qualquer animação em andamento
    }
    
    // Redefine os painéis para posições iniciais corretas antes de iniciar
    if (toRegister) {
        loginPanel.setBounds(0, 0, 450, 550);
        registerPanel.setBounds(450, 0, 450, 550);
    } else {
        loginPanel.setBounds(-450, 0, 450, 550);
        registerPanel.setBounds(0, 0, 450, 550);
    }
    
    // Garante que ambos os painéis estejam visíveis
    loginPanel.setVisible(true);
    registerPanel.setVisible(true);
    
    // Reinicia a contagem da animação
    animationStep = 0;
    
    // Define o painel que está à frente durante a animação
    cardPanel.removeAll();
    cardPanel.add(loginPanel);
    cardPanel.add(registerPanel);
    
    if (toRegister) {
        cardPanel.setComponentZOrder(registerPanel, 0);
        cardPanel.setComponentZOrder(loginPanel, 1);
    } else {
        cardPanel.setComponentZOrder(loginPanel, 0);
        cardPanel.setComponentZOrder(registerPanel, 1);
    }
    
    // Inicia a animação com um objeto completamente novo
    animationTimer = new Timer(10, null);
    animationTimer.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            animationStep++;
            float progress = (float) animationStep / 30; // Ajuste a quantidade de passos aqui (30)
            
            if (toRegister) {
                // Login desliza para a esquerda, registro desliza da direita
                int loginX = (int) (-450 * progress);
                int registerX = (int) (450 * (1 - progress));
                
                loginPanel.setBounds(loginX, 0, 450, 550);
                registerPanel.setBounds(registerX, 0, 450, 550);
            } else {
                // Registro desliza para a direita, login desliza da esquerda
                int registerX = (int) (450 * progress);
                int loginX = (int) (-450 * (1 - progress));
                
                registerPanel.setBounds(registerX, 0, 450, 550);
                loginPanel.setBounds(loginX, 0, 450, 550);
            }
            
            // Atualiza a interface
            cardPanel.validate();
            cardPanel.repaint();
            
            // Quando a animação terminar
            if (animationStep >= 30) { // Mesmo número de passos definido acima
                animationTimer.stop();
                
                // Define posições finais precisas
                if (toRegister) {
                    loginPanel.setBounds(-450, 0, 450, 550);
                    registerPanel.setBounds(0, 0, 450, 550);
                } else {
                    registerPanel.setBounds(450, 0, 450, 550);
                    loginPanel.setBounds(0, 0, 450, 550);
                }
                
                // Atualiza o estado
                isLoginShowing = !toRegister;
                
                // Anima título se necessário
                if (toRegister) {
                    JLabel titleLabel = (JLabel) findComponentByName(registerPanel, "titleLabel");
                    if (titleLabel != null) {
                        animateTypingText(titleLabel, "Crie sua conta", 80);
                    }
                } else {
                    JLabel titleLabel = (JLabel) findComponentByName(loginPanel, "titleLabel");
                    if (titleLabel != null) {
                        animateTypingText(titleLabel, "Bem-vindo de volta!", 80);
                    }
                }
                
                // Força uma atualização final
                cardPanel.validate();
                cardPanel.repaint();
                
                // Executa uma verificação final após pequeno atraso
                Timer finalCheckTimer = new Timer(100, ev -> {
                    if (toRegister) {
                        if (registerPanel.getX() != 0) {
                            registerPanel.setBounds(0, 0, 450, 550);
                        }
                    } else {
                        if (loginPanel.getX() != 0) {
                            loginPanel.setBounds(0, 0, 450, 550);
                        }
                    }
                    cardPanel.validate();
                    cardPanel.repaint();
                });
                finalCheckTimer.setRepeats(false);
                finalCheckTimer.start();
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