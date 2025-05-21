package espaco_capita;

import com.formdev.flatlaf.FlatLightLaf;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

public class home extends JFrame {
    private JComboBox<String> espacoComboBox;
    private JSpinner dateSpinner;
    private JSpinner timeSpinner;
    private JButton reservarButton;
    private JPanel statusPanel;
    private JLabel statusIconLabel;
    private JLabel statusTextLabel;
    private JProgressBar progressBar;
    private javax.swing.Timer animationTimer; // Usando javax.swing.Timer para evitar ambiguidade
    
    // Resolução fixa da janela - Aumentada para acomodar a logo maior
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 800; // Aumentada de 600 para 700
    
    // Cores do tema
    private final Color PRIMARY_COLOR = new Color(50, 120, 80);
    private final Color SUCCESS_COLOR = new Color(46, 125, 50);
    private final Color ERROR_COLOR = new Color(198, 40, 40);
    private final Color BACKGROUND_COLOR = new Color(245, 245, 245);
    private final Color CARD_COLOR = Color.WHITE;
    
    // Caminho para o arquivo CSV (mais simples que Excel)
    private static final String CSV_FILE_PATH = "reservas.csv";
    
    // Caminho para a logo
    private static final String LOGO_PATH = "C:\\Users\\Joao\\Documents\\SOFTWARES\\Espaço Capital\\Coworking-System\\demo\\src\\main\\resources\\logo.png";
    
    // Simulação de reservas existentes (para testar conflito)
    private final Map<String, List<String>> reservasExistentes = new HashMap<>();
    
    // Histórico de reservas do usuário atual
    private final List<String> historicoReservas = new ArrayList<>();

    public home() {
        // Configurar FlatLaf
        setupLookAndFeel();
        
        // Configurar janela principal
        setTitle("Agendamento de Espaço - Espaço Capital");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Definir tamanho fixo e impedir redimensionamento
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setResizable(false);
        
        // Centralizar na tela
        setLocationRelativeTo(null);
        
        // Carregar reservas do CSV (se existir)
        carregarReservasDoArquivo();
        
        // Adicionar reserva simulada para teste de conflito
        if (!reservasExistentes.containsKey("Sala 1")) {
            reservasExistentes.put("Sala 1", new ArrayList<>());
        }
        reservasExistentes.get("Sala 1").add("21/05/2025 14:00");
        
        // Inicializar componentes
        initComponents();
    }
    
    private void setupLookAndFeel() {
        try {
            // Configurar fonte padrão (tamanho ajustado para tela menor)
            Font defaultFont = new Font("Segoe UI", Font.PLAIN, 14);
            UIManager.put("defaultFont", defaultFont);
            
            // Configurações de estilo para componentes
            UIManager.put("Button.arc", 6);
            UIManager.put("Component.arc", 6);
            UIManager.put("ProgressBar.arc", 6);
            UIManager.put("TextComponent.arc", 6);
            
            UIManager.put("Button.margin", new Insets(6, 12, 6, 12));
            UIManager.put("TabbedPane.contentBorderInsets", new Insets(0, 0, 0, 0));
            UIManager.put("TabbedPane.tabsOverlapBorder", true);
            
            // Aumentar tamanho dos componentes
            UIManager.put("ComboBox.minimumWidth", 100);
            
            // Aplicar tema FlatLaf
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Falha ao inicializar FlatLaf");
            ex.printStackTrace();
        }
    }
    
    private void initComponents() {
        // Configurar painel principal com MigLayout
        JPanel mainPanel = new JPanel(new MigLayout("fill, insets 0"));
        mainPanel.setBackground(BACKGROUND_COLOR);
        
        // Adicionar botão de saída no canto superior direito
        JButton leaveButton = criarBotaoSair();
        mainPanel.add(leaveButton, "pos 100%-45px 8px");
        
        // Criar painel de cabeçalho para a logo
        JPanel headerPanel = new JPanel(new MigLayout("fillx, insets 25 25 10 25"));
        headerPanel.setBackground(BACKGROUND_COLOR);
        
        // Adicionar logo no cabeçalho
        JLabel logoLabel = criarLogoLabel();
        headerPanel.add(logoLabel, "center");
        
        // Adicionar cabeçalho ao painel principal
        mainPanel.add(headerPanel, "north, wrap");
        
        // Criar painel de conteúdo central
        JPanel contentPanel = new JPanel(new MigLayout("fillx, insets 10 25 25 25, wrap", 
                                                    "[center, grow]", 
                                                    "[]15[]"));
        contentPanel.setBackground(BACKGROUND_COLOR);
        
        // Painel do formulário (com estilo de cartão)
        JPanel formCard = createFormPanel();
        contentPanel.add(formCard, "grow, width 80%");
        
        // Painel de status (inicialmente invisível)
        statusPanel = new JPanel(new MigLayout("fillx, insets 15, wrap", 
                                              "[grow]", 
                                              "[]8[]8[]"));
        statusPanel.setBorder(new EmptyBorder(12, 12, 12, 12));
        statusPanel.setBackground(CARD_COLOR);
        statusPanel.setBorder(BorderFactory.createCompoundBorder(
            new EmptyBorder(0, 0, 0, 0),
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                new EmptyBorder(15, 15, 15, 15)
            )
        ));
        statusPanel.setVisible(false);
        
        // Linha de status com ícone e texto
        JPanel statusLinePanel = new JPanel(new MigLayout("fillx, insets 0", "[]8[grow]"));
        statusLinePanel.setOpaque(false);
        
        statusIconLabel = new JLabel();
        statusTextLabel = new JLabel();
        statusTextLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        statusLinePanel.add(statusIconLabel);
        statusLinePanel.add(statusTextLabel, "grow");
        
        // Barra de progresso
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(false);
        progressBar.setVisible(false);
        
        statusPanel.add(statusLinePanel, "grow");
        statusPanel.add(progressBar, "growx, h 8!");
        
        contentPanel.add(statusPanel, "grow, width 80%");
        
        // Adicionar painel de conteúdo ao painel principal
        mainPanel.add(contentPanel, "center, grow");
        
        // Adicionar painel principal à janela
        setContentPane(mainPanel);
    }
    
private JLabel criarLogoLabel() {
    JLabel logoLabel = new JLabel();
    logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
    
    try {
        ImageIcon originalIcon = new ImageIcon(LOGO_PATH);
        Image originalImage = originalIcon.getImage();
        
        // Calcular dimensões para a logo ocupar mais espaço na tela
        int originalWidth = originalIcon.getIconWidth();
        int originalHeight = originalIcon.getIconHeight();
        
        // Usar 70% da largura da janela como largura máxima
        int maxWidth = (int)(WINDOW_WIDTH * 0.7);
        
        // Calcular altura proporcional para esta largura
        int desiredHeight = (originalHeight * maxWidth) / originalWidth;
        
        // Verificar se a altura não é muito grande (máximo 150px)
        int maxHeight = 150;
        if (desiredHeight > maxHeight) {
            desiredHeight = maxHeight;
            maxWidth = (originalWidth * maxHeight) / originalHeight;
        }
        
        // Redimensionar com alta qualidade
        Image resizedImage = resizeImageHighQuality(originalImage, maxWidth, desiredHeight);
        logoLabel.setIcon(new ImageIcon(resizedImage));
        
        // Adicionar espaço vertical ao redor da logo
        logoLabel.setBorder(new EmptyBorder(15, 0, 15, 0));
    } catch (Exception e) {
        // Se não conseguir carregar a logo, usar texto como fallback
        logoLabel.setText("ESPAÇO CAPITAL");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        logoLabel.setForeground(PRIMARY_COLOR);
    }
    
    return logoLabel;
}
    
    private JButton criarBotaoSair() {
        JButton leaveButton = new JButton();
        leaveButton.setToolTipText("Sair");
        leaveButton.setFocusPainted(false);
        leaveButton.setBorderPainted(false);
        leaveButton.setContentAreaFilled(false);
        
        // Tamanho desejado para o ícone
        int iconSize = 24;
        
        // Carregar o ícone
        try {
            String iconPath = "C:\\Users\\Joao\\Documents\\SOFTWARES\\Espaço Capital\\Coworking-System\\demo\\src\\main\\resources\\icons\\leave.png";
            ImageIcon originalIcon = new ImageIcon(iconPath);
            
            // Obter a imagem original
            Image originalImage = originalIcon.getImage();
            
            // Redimensionar com alta qualidade
            Image resizedImage = resizeImageHighQuality(originalImage, iconSize, iconSize);
            
            // Definir o ícone
            leaveButton.setIcon(new ImageIcon(resizedImage));
        } catch (Exception e) {
            // Se não conseguir carregar o ícone, usar texto como fallback
            leaveButton.setText("Sair");
            leaveButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
            leaveButton.setForeground(PRIMARY_COLOR);
        }
        
        // Adicionar ação ao botão
        leaveButton.addActionListener(e -> System.exit(0));
        
        return leaveButton;
    }
    
    // Método para redimensionar imagem com alta qualidade
    private Image resizeImageHighQuality(Image originalImage, int width, int height) {
        // Criar uma imagem de buffer com suporte a transparência
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        
        // Obter o contexto gráfico e configurar para alta qualidade
        Graphics2D g2d = resizedImage.createGraphics();
        
        // Configurar para rendering de alta qualidade
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Desenhar a imagem redimensionada
        g2d.drawImage(originalImage, 0, 0, width, height, null);
        g2d.dispose();
        
        return resizedImage;
    }
    
    private JPanel createFormPanel() {
        // Criar painel do formulário com borda e fundo branco
        JPanel formPanel = new JPanel(new MigLayout("fillx, insets 20, wrap", 
                                                   "[grow]", 
                                                   "[]15[]15[]15[]"));
        formPanel.setBackground(CARD_COLOR);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            new EmptyBorder(0, 0, 0, 0),
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                new EmptyBorder(0, 0, 0, 0)
            )
        ));
        
        // Título "Agendamento de Espaço" no topo do formulário
        JLabel tituloLabel = new JLabel("Agendamento de Espaço");
        tituloLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        tituloLabel.setForeground(PRIMARY_COLOR);
        formPanel.add(tituloLabel, "center, gapbottom 10");
        
        // Título do formulário
        JLabel formTitle = new JLabel("Dados da Reserva");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        formPanel.add(formTitle, "left, gapbottom 8");
        
        // Separador
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(220, 220, 220));
        formPanel.add(separator, "growx, gapbottom 15");
        
        // Criar os campos do formulário
        
        // Seleção de espaço
        JPanel espacoPanel = createFieldPanel("Espaço", "Selecione o espaço para reserva");
        espacoComboBox = new JComboBox<>(new String[]{"Selecione um espaço", "Sala 1", "Sala 2", "Auditório"});
        espacoComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        espacoComboBox.setPreferredSize(new Dimension(0, 32));
        espacoPanel.add(espacoComboBox, "growx");
        formPanel.add(espacoPanel, "growx");
        
        // Seleção de data
        JPanel dataPanel = createFieldPanel("Data", "Selecione a data da reserva");
        dateSpinner = createDateTimeSpinner("dd/MM/yyyy", new Date());
        dataPanel.add(dateSpinner, "growx");
        formPanel.add(dataPanel, "growx");
        
        // Seleção de hora
        JPanel horaPanel = createFieldPanel("Horário", "Selecione o horário da reserva");
        timeSpinner = createDateTimeSpinner("HH:mm", new Date());
        horaPanel.add(timeSpinner, "growx");
        formPanel.add(horaPanel, "growx");
        
        // Painel de ação
        JPanel actionPanel = new JPanel(new MigLayout("fillx, insets 8 0 0 0"));
        actionPanel.setBackground(CARD_COLOR);
        
        reservarButton = new JButton("Confirmar Reserva");
        reservarButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        reservarButton.setBackground(PRIMARY_COLOR);
        reservarButton.setForeground(Color.WHITE);
        reservarButton.setFocusPainted(false);
        reservarButton.setPreferredSize(new Dimension(0, 40));
        reservarButton.addActionListener(this::realizarReserva);
        
        actionPanel.add(reservarButton, "growx");
        formPanel.add(actionPanel, "growx, gaptop 8");
        
        return formPanel;
    }
    
    private JPanel createFieldPanel(String labelText, String tooltip) {
        JPanel panel = new JPanel(new MigLayout("fillx, insets 0 0 4 0, wrap", "[grow]", "[]4[]"));
        panel.setBackground(CARD_COLOR);
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setToolTipText(tooltip);
        
        panel.add(label, "left");
        
        return panel;
    }
    
    private JSpinner createDateTimeSpinner(String format, Date initialValue) {
        JSpinner spinner = new JSpinner(new SpinnerDateModel(initialValue, null, null, Calendar.DAY_OF_MONTH));
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner, format);
        spinner.setEditor(editor);
        spinner.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        // Aumentar a altura do spinner (ajustada para tela menor)
        JComponent field = ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField();
        field.setPreferredSize(new Dimension(0, 32));
        
        return spinner;
    }
    
    private void realizarReserva(ActionEvent e) {
        // Desabilitar botão para evitar cliques múltiplos
        reservarButton.setEnabled(false);
        
        // Validação de campos
        if (espacoComboBox.getSelectedIndex() == 0) {
            showError("Por favor, selecione um espaço para continuar.");
            reservarButton.setEnabled(true);
            return;
        }
        
        String espacoSelecionado = (String) espacoComboBox.getSelectedItem();
        Date data = (Date) dateSpinner.getValue();
        Date hora = (Date) timeSpinner.getValue();
        
        // Combinar data e hora
        Calendar dataCalendar = Calendar.getInstance();
        dataCalendar.setTime(data);
        
        Calendar horaCalendar = Calendar.getInstance();
        horaCalendar.setTime(hora);
        
        Calendar dataHoraCompleta = Calendar.getInstance();
        dataHoraCompleta.set(Calendar.YEAR, dataCalendar.get(Calendar.YEAR));
        dataHoraCompleta.set(Calendar.MONTH, dataCalendar.get(Calendar.MONTH));
        dataHoraCompleta.set(Calendar.DAY_OF_MONTH, dataCalendar.get(Calendar.DAY_OF_MONTH));
        dataHoraCompleta.set(Calendar.HOUR_OF_DAY, horaCalendar.get(Calendar.HOUR_OF_DAY));
        dataHoraCompleta.set(Calendar.MINUTE, horaCalendar.get(Calendar.MINUTE));
        dataHoraCompleta.set(Calendar.SECOND, 0);
        
        // Formatar para exibição
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String dataHoraFormatada = sdf.format(dataHoraCompleta.getTime());
        
        // Animação de processamento
        showProcessing("Processando sua solicitação...");
        
        // Simular processamento (para feedback visual)
        final Date reservaDate = dataHoraCompleta.getTime();
        final String reservaFormatada = dataHoraFormatada;
        
        animationTimer = new javax.swing.Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                animationTimer.stop();
                
                // Verificar conflito
                boolean temConflito = false;
                if (reservasExistentes.containsKey(espacoSelecionado)) {
                    List<String> reservas = reservasExistentes.get(espacoSelecionado);
                    if (reservas.contains(reservaFormatada)) {
                        temConflito = true;
                    }
                }
                
                if (temConflito) {
                    // Cenário 2: Espaço já reservado
                    showError("O horário " + reservaFormatada + " para " + espacoSelecionado + 
                             " já está reservado. Por favor, escolha outro horário ou espaço.");
                    
                    JOptionPane.showMessageDialog(
                        home.this,
                        "O horário " + reservaFormatada + " para " + espacoSelecionado + 
                        " já está reservado.\nPor favor, escolha outro horário ou espaço.",
                        "Conflito de Reserva",
                        JOptionPane.ERROR_MESSAGE
                    );
                } else {
                    // Cenário 1: Espaço disponível
                    // Adicionar à lista de reservas existentes
                    if (!reservasExistentes.containsKey(espacoSelecionado)) {
                        reservasExistentes.put(espacoSelecionado, new ArrayList<>());
                    }
                    reservasExistentes.get(espacoSelecionado).add(reservaFormatada);
                    
                    // Adicionar ao histórico de reservas do usuário
                    historicoReservas.add(espacoSelecionado + " - " + reservaFormatada);
                    
                    // Salvar no arquivo
                    boolean salvouComSucesso = salvarReservaNoArquivo(espacoSelecionado, reservaDate);
                    
                    if (salvouComSucesso) {
                        // Exibir confirmação
                        showSuccess("Reserva confirmada com sucesso para " + espacoSelecionado + 
                                  " na data " + reservaFormatada);
                        
                        JOptionPane.showMessageDialog(
                            home.this,
                            "Reserva confirmada com sucesso!\n\n" +
                            "Espaço: " + espacoSelecionado + "\n" +
                            "Data e Hora: " + reservaFormatada + "\n\n" +
                            "Os dados foram armazenados com sucesso.",
                            "Reserva Confirmada",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                        
                        // Resetar formulário
                        espacoComboBox.setSelectedIndex(0);
                    } else {
                        // Erro ao salvar
                        showError("A reserva foi registrada no sistema, mas houve um erro ao salvar no arquivo.");
                    }
                }
                
                // Reativar botão
                reservarButton.setEnabled(true);
            }
        });
        animationTimer.setRepeats(false);
        animationTimer.start();
    }
    
    private void showProcessing(String message) {
        statusPanel.setVisible(true);
        statusPanel.setBackground(new Color(232, 240, 254));
        statusPanel.setBorder(BorderFactory.createCompoundBorder(
            new EmptyBorder(0, 0, 0, 0),
            BorderFactory.createCompoundBorder(
                new MatteBorder(2, 2, 2, 2, new Color(66, 133, 244)),
                new EmptyBorder(15, 15, 15, 15)
            )
        ));
        
        statusIconLabel.setText("⌛");
        statusIconLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        statusIconLabel.setForeground(new Color(66, 133, 244));
        
        statusTextLabel.setText(message);
        statusTextLabel.setForeground(new Color(25, 103, 210));
        
        // Mostrar e animar a barra de progresso
        progressBar.setVisible(true);
        progressBar.setValue(0);
        
        // Animação da barra de progresso
        final javax.swing.Timer progressTimer = new javax.swing.Timer(30, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int value = progressBar.getValue();
                if (value < 100) {
                    progressBar.setValue(value + 2);
                } else {
                    ((javax.swing.Timer)e.getSource()).stop();
                }
            }
        });
        progressTimer.start();
    }
    
    private void showSuccess(String message) {
        statusPanel.setVisible(true);
        statusPanel.setBackground(new Color(232, 245, 233));
        statusPanel.setBorder(BorderFactory.createCompoundBorder(
            new EmptyBorder(0, 0, 0, 0),
            BorderFactory.createCompoundBorder(
                new MatteBorder(2, 2, 2, 2, SUCCESS_COLOR),
                new EmptyBorder(15, 15, 15, 15)
            )
        ));
        
        statusIconLabel.setText("✓");
        statusIconLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        statusIconLabel.setForeground(SUCCESS_COLOR);
        
        statusTextLabel.setText(message);
        statusTextLabel.setForeground(new Color(27, 94, 32));
        
        // Esconder a barra de progresso
        progressBar.setVisible(false);
    }
    
    private void showError(String message) {
        statusPanel.setVisible(true);
        statusPanel.setBackground(new Color(253, 237, 237));
        statusPanel.setBorder(BorderFactory.createCompoundBorder(
            new EmptyBorder(0, 0, 0, 0),
            BorderFactory.createCompoundBorder(
                new MatteBorder(2, 2, 2, 2, ERROR_COLOR),
                new EmptyBorder(15, 15, 15, 15)
            )
        ));
        
        statusIconLabel.setText("!");
        statusIconLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        statusIconLabel.setForeground(ERROR_COLOR);
        
        statusTextLabel.setText(message);
        statusTextLabel.setForeground(new Color(183, 28, 28));
        
        // Esconder a barra de progresso
        progressBar.setVisible(false);
    }
    
    private void carregarReservasDoArquivo() {
        File csvFile = new File(CSV_FILE_PATH);
        
        if (!csvFile.exists()) {
            return; // Arquivo não existe, não há reservas para carregar
        }
        
        try (java.util.Scanner scanner = new java.util.Scanner(csvFile)) {
            // Pular a linha de cabeçalho
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }
            
            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine();
                String[] campos = linha.split(",");
                
                if (campos.length >= 2) {
                    String espaco = campos[0];
                    String dataHora = campos[1];
                    
                    if (!reservasExistentes.containsKey(espaco)) {
                        reservasExistentes.put(espaco, new ArrayList<>());
                    }
                    reservasExistentes.get(espaco).add(dataHora);
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar dados do arquivo: " + e.getMessage());
        }
    }
    
    private boolean salvarReservaNoArquivo(String espaco, Date dataHora) {
        File csvFile = new File(CSV_FILE_PATH);
        boolean arquivoExiste = csvFile.exists();
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile, true))) {
            // Se o arquivo não existir, criar o cabeçalho
            if (!arquivoExiste) {
                writer.write("Espaco,DataHora,Usuario,DataRegistro");
                writer.newLine();
            }
            
            // Formatar datas
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String dataHoraFormatada = sdf.format(dataHora);
            String dataRegistroFormatada = sdf.format(new Date());
            
            // Escrever dados da reserva
            writer.write(espaco + "," + 
                         dataHoraFormatada + "," + 
                         "Usuário Atual" + "," + 
                         dataRegistroFormatada);
            writer.newLine();
            
            return true;
        } catch (IOException e) {
            System.err.println("Erro ao salvar reserva no arquivo: " + e.getMessage());
            return false;
        }
    }
    
    public static void main(String[] args) {
        // Iniciar a aplicação no EDT
        SwingUtilities.invokeLater(() -> {
            home frame = new home();
            frame.setVisible(true);
        });
    }
}   