package espaco_capita;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

import com.formdev.flatlaf.FlatLightLaf;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.io.*;

/**
 * Aplicação principal do Espaço Capital
 * Implementa a sidebar e o painel de gestão de agendamentos
 */
public class EspacoCapitalApp extends JFrame {
    
    // Definição das cores do sistema
    private final Color VERDE_ESCURO = new Color(0, 102, 51);    // #006633
    private final Color VERDE_MEDIO = new Color(0, 153, 76);     // #00994C
    private final Color VERDE_CLARO = new Color(153, 255, 204);  // #99FFCC
    private final Color BRANCO = new Color(255, 255, 255);       // #FFFFFF
    private final Color CINZA_CLARO = new Color(240, 240, 240);  // #F0F0F0
    private final Color PRETO_SUAVE = new Color(45, 45, 45);     // #2D2D2D
    
    // Nome do arquivo CSV para armazenamento de dados
    private final String CSV_FILE = "agendamentos.csv";
    
    // Componentes da sidebar
    private JPanel sidebarPanel;
    private JPanel mainContentPanel;
    private JPanel menuExpandidoPanel;
    private boolean sidebarExpandida = true;
    private final int LARGURA_SIDEBAR_EXPANDIDA = 250;
    private final int LARGURA_SIDEBAR_RECOLHIDA = 70;
    private final int ALTURA_TELA = 768;
    private final int LARGURA_TELA = 1366;
    
    private List<MenuButton> menuButtons = new ArrayList<>();
    
    // Componentes do painel de agendamento
    private JComboBox<String> comboEspacos;
    private JSpinner dateSpinner;
    private JComboBox<String> comboHoraInicio;
    private JComboBox<String> comboHoraTermino;
    private JButton btnCadastrar;
    private JTable tabelaHorarios;
    private DefaultTableModel modeloTabela;
    
    // Lista de horários cadastrados 
    private List<HorarioDisponivel> horariosDisponiveis = new ArrayList<>();
    
    /**
     * Construtor da aplicação principal
     */
    public EspacoCapitalApp() {
        // Configurações básicas da janela
        setTitle("Espaço Capital - Sistema de Agendamento de Salas");
        setSize(LARGURA_TELA, ALTURA_TELA);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false); // Bloqueia o redimensionamento conforme especificado
        
        // Layout principal
        setLayout(new BorderLayout());
        
        // Inicializa os componentes
        inicializarComponentes();
        
        // Torna a janela visível
        setVisible(true);
    }
    
    /**
     * Inicializa todos os componentes da interface
     */
    private void inicializarComponentes() {
        // Criação do painel de conteúdo principal
        mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new BorderLayout());
        mainContentPanel.setBackground(BRANCO);
        
        // Configura a sidebar
        configurarSidebar();
        
        // Adiciona a sidebar e o conteúdo principal ao frame
        add(sidebarPanel, BorderLayout.WEST);
        add(mainContentPanel, BorderLayout.CENTER);
        
        // Inicializa o painel de agendamento
        JPanel agendamentoPanel = criarPainelAgendamento();
        mainContentPanel.add(agendamentoPanel, BorderLayout.CENTER);
        
        // Carrega dados do CSV em vez de adicionar dados de exemplo
        carregarDadosDoCSV();
        
        // Registra listener para salvar ao fechar a aplicação
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                salvarDadosNoCSV();
            }
        });
    }
    
    /**
     * Configura a sidebar da aplicação
     */
    private void configurarSidebar() {
        // Painel da sidebar
        sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BorderLayout());
        sidebarPanel.setPreferredSize(new Dimension(LARGURA_SIDEBAR_EXPANDIDA, ALTURA_TELA));
        sidebarPanel.setBackground(VERDE_ESCURO);
        
        // Logo e título no topo
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 15));
        headerPanel.setBackground(VERDE_ESCURO);
        headerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Criação do ícone
        ImageIcon logoIcon = createColorIcon(VERDE_CLARO, 30, 30);
        JLabel logoLabel = new JLabel(logoIcon);
        
        // Título do aplicativo
        JLabel titleLabel = new JLabel("Espaço Capital");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(BRANCO);
        
        headerPanel.add(logoLabel);
        headerPanel.add(titleLabel);
        
        // Botão de toggle para expandir/recolher a sidebar
        JButton toggleButton = new JButton("≡");
        toggleButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        toggleButton.setForeground(BRANCO);
        toggleButton.setBackground(VERDE_ESCURO);
        toggleButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        toggleButton.setFocusPainted(false);
        toggleButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        toggleButton.addActionListener(e -> toggleSidebar());
        
        JPanel togglePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        togglePanel.setBackground(VERDE_ESCURO);
        togglePanel.add(toggleButton);
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(VERDE_ESCURO);
        topPanel.add(headerPanel, BorderLayout.WEST);
        topPanel.add(togglePanel, BorderLayout.EAST);
        
        // Painel para botões do menu
        menuExpandidoPanel = new JPanel();
        menuExpandidoPanel.setLayout(new BoxLayout(menuExpandidoPanel, BoxLayout.Y_AXIS));
        menuExpandidoPanel.setBackground(VERDE_ESCURO);
        menuExpandidoPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        
        // Adiciona seções e itens de menu (reduzido para focar nas funcionalidades principais)
        adicionarSecaoMenu("GESTÃO", menuExpandidoPanel);
        adicionarItemMenu("Agendamentos", "calendar", menuExpandidoPanel, true);
        
        // Adiciona os componentes ao painel da sidebar
        sidebarPanel.add(topPanel, BorderLayout.NORTH);
        sidebarPanel.add(new JScrollPane(menuExpandidoPanel, 
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);
    }
    
    /**
     * Adiciona uma seção ao menu
     */
    private void adicionarSecaoMenu(String titulo, JPanel container) {
        JLabel tituloLabel = new JLabel(titulo);
        tituloLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        tituloLabel.setForeground(BRANCO);
        tituloLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        tituloLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        container.add(tituloLabel);
    }
    
    /**
     * Adiciona um item ao menu
     */
    private void adicionarItemMenu(String texto, String icone, JPanel container, boolean selecionado) {
        MenuButton button = new MenuButton(texto, icone, selecionado);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        container.add(button);
        menuButtons.add(button);
        
        // Adiciona espaçamento após o botão
        container.add(Box.createRigidArea(new Dimension(0, 5)));
    }
    
    /**
     * Alterna o estado da sidebar entre expandida e recolhida
     */
    private void toggleSidebar() {
        sidebarExpandida = !sidebarExpandida;
        
        int novaLargura = sidebarExpandida ? LARGURA_SIDEBAR_EXPANDIDA : LARGURA_SIDEBAR_RECOLHIDA;
        sidebarPanel.setPreferredSize(new Dimension(novaLargura, ALTURA_TELA));
        
        // Atualiza a visibilidade dos textos nos botões do menu
        for (MenuButton button : menuButtons) {
            button.setModoCompacto(!sidebarExpandida);
        }
        
        // Anima a expansão/retração
        new Thread(() -> {
            try {
                if (sidebarExpandida) {
                    // Expandindo
                    for (int i = LARGURA_SIDEBAR_RECOLHIDA; i <= LARGURA_SIDEBAR_EXPANDIDA; i += 5) {
                        sidebarPanel.setPreferredSize(new Dimension(i, ALTURA_TELA));
                        sidebarPanel.revalidate();
                        Thread.sleep(5); // Velocidade da animação
                    }
                } else {
                    // Recolhendo
                    for (int i = LARGURA_SIDEBAR_EXPANDIDA; i >= LARGURA_SIDEBAR_RECOLHIDA; i -= 5) {
                        sidebarPanel.setPreferredSize(new Dimension(i, ALTURA_TELA));
                        sidebarPanel.revalidate();
                        Thread.sleep(5); // Velocidade da animação
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        
        revalidate();
        repaint();
    }
    
    /**
     * Cria o painel de agendamento de horários
     */
    private JPanel criarPainelAgendamento() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BRANCO);
        
        // Painel de título
        JPanel panelTitulo = criarPainelTitulo();
        panel.add(panelTitulo, BorderLayout.NORTH);
        
        // Painel de cadastro
        JPanel panelCadastro = criarPainelCadastro();
        panel.add(panelCadastro, BorderLayout.CENTER);
        
        // Painel de visualização
        JPanel panelVisualizacao = criarPainelVisualizacao();
        panel.add(panelVisualizacao, BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * Cria o painel de título para o módulo de agendamento
     */
    private JPanel criarPainelTitulo() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BRANCO);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        
        JLabel lblTitulo = new JLabel("Cadastro de Horários");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(VERDE_ESCURO);
        
        panel.add(lblTitulo, BorderLayout.NORTH);
        
        return panel;
    }
    
    /**
     * Cria o painel de cadastro de horários
     */
    private JPanel criarPainelCadastro() {
        JPanel panel = new JPanel();
        panel.setBackground(BRANCO);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 1, 0, CINZA_CLARO),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        // Utiliza GridBagLayout para organizar os campos
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Componentes de entrada
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JPanel inputPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        inputPanel.setBackground(BRANCO);
        
        // Seleção de espaço
        String[] espacos = {"Sala de Reunião 1", "Sala de Reunião 2", "Sala de Coworking", "Auditório", "Sala de Treinamento"};
        comboEspacos = new JComboBox<>(espacos);
        comboEspacos.setBackground(BRANCO);
        comboEspacos.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(CINZA_CLARO),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        // Seleção de data com JSpinner
        SpinnerDateModel dateModel = new SpinnerDateModel();
        dateModel.setCalendarField(Calendar.DAY_OF_MONTH);
        dateSpinner = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(dateEditor);
        dateSpinner.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(CINZA_CLARO),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        // Seleção de hora de início com horários fixos
        String[] horariosInicio = {"08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00"};
        comboHoraInicio = new JComboBox<>(horariosInicio);
        comboHoraInicio.setBackground(BRANCO);
        comboHoraInicio.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(CINZA_CLARO),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        // Seleção de hora de término com horários fixos
        String[] horariosTermino = {"09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00"};
        comboHoraTermino = new JComboBox<>(horariosTermino);
        comboHoraTermino.setBackground(BRANCO);
        comboHoraTermino.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(CINZA_CLARO),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        // Labels para os campos
        JPanel labelPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        labelPanel.setBackground(BRANCO);
        
        JLabel lblEspaco = new JLabel("Espaço");
        lblEspaco.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JLabel lblData = new JLabel("Data");
        lblData.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JLabel lblHoraInicio = new JLabel("Hora de Início");
        lblHoraInicio.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JLabel lblHoraTermino = new JLabel("Hora de Término");
        lblHoraTermino.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        labelPanel.add(lblEspaco);
        labelPanel.add(lblData);
        labelPanel.add(lblHoraInicio);
        labelPanel.add(lblHoraTermino);
        
        // Adiciona os labels
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        panel.add(labelPanel, gbc);
        
        // Adiciona os campos de entrada
        inputPanel.add(comboEspacos);
        inputPanel.add(dateSpinner);
        inputPanel.add(comboHoraInicio);
        inputPanel.add(comboHoraTermino);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 4;
        panel.add(inputPanel, gbc);
        
        // Botão de cadastro unificado
        gbc.gridy = 2;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 0, 0, 0);
        
        btnCadastrar = new JButton("Cadastrar Agendamento");
        btnCadastrar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCadastrar.setBackground(VERDE_ESCURO);
        btnCadastrar.setForeground(BRANCO);
        btnCadastrar.setFocusPainted(false);
        btnCadastrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCadastrar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // Adiciona ação ao botão de cadastro
        btnCadastrar.addActionListener(e -> cadastrarHorario());
        
        panel.add(btnCadastrar, gbc);
        
        return panel;
    }
    
    /**
     * Cria o painel de visualização de horários cadastrados
     */
    private JPanel criarPainelVisualizacao() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BRANCO);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Título do painel
        JLabel lblTituloVisualizacao = new JLabel("Horários Disponíveis");
        lblTituloVisualizacao.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTituloVisualizacao.setForeground(VERDE_ESCURO);
        panel.add(lblTituloVisualizacao, BorderLayout.NORTH);
        
        // Tabela de horários
        String[] colunas = {"Espaço", "Data", "Hora de Início", "Hora de Término", "Ações"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // Apenas a coluna de ações é editável
            }
        };
        
        tabelaHorarios = new JTable(modeloTabela);
        tabelaHorarios.setRowHeight(40);
        tabelaHorarios.setShowVerticalLines(false);
        tabelaHorarios.setShowHorizontalLines(true);
        tabelaHorarios.setGridColor(CINZA_CLARO);
        tabelaHorarios.getTableHeader().setBackground(VERDE_MEDIO);
        tabelaHorarios.getTableHeader().setForeground(BRANCO);
        tabelaHorarios.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        // Centraliza o conteúdo das células
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        for (int i = 0; i < tabelaHorarios.getColumnCount() - 1; i++) {
            tabelaHorarios.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        // Renderizador para a coluna de ações
        tabelaHorarios.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer());
        tabelaHorarios.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(new JCheckBox()));
        
        JScrollPane scrollPane = new JScrollPane(tabelaHorarios);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Cadastra um novo horário se for válido
     */
    private void cadastrarHorario() {
        // Obtém os valores dos campos
        String espaco = (String) comboEspacos.getSelectedItem();
        
        // Formata a data
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String data = dateFormat.format(((SpinnerDateModel)dateSpinner.getModel()).getDate());
        
        // Obtém as horas selecionadas nos comboboxes
        String horaInicio = (String) comboHoraInicio.getSelectedItem();
        String horaTermino = (String) comboHoraTermino.getSelectedItem();
        
        // Validação básica
        if (espaco == null || data.isEmpty() || horaInicio == null || horaTermino == null) {
            JOptionPane.showMessageDialog(this, 
                    "Todos os campos são obrigatórios.", 
                    "Erro de Validação", 
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Validação de horários
        try {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            Date inicio = format.parse(horaInicio);
            Date termino = format.parse(horaTermino);
            
            if (!termino.after(inicio)) {
                JOptionPane.showMessageDialog(this, 
                        "O horário de término deve ser posterior ao horário de início.", 
                        "Erro de Validação", 
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Verifica conflitos
            if (verificarConflito(espaco, data, horaInicio, horaTermino)) {
                JOptionPane.showMessageDialog(this, 
                        "Já existe um horário cadastrado para este espaço neste período.", 
                        "Conflito de Horário", 
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Cria novo horário disponível
            HorarioDisponivel horario = new HorarioDisponivel(espaco, data, horaInicio, horaTermino);
            horariosDisponiveis.add(horario);
            
            // Atualiza a tabela
            atualizarTabela();
            
            // Salva no CSV imediatamente após cada cadastro
            salvarDadosNoCSV();
            
            // Exibe mensagem de sucesso
            JOptionPane.showMessageDialog(this, 
                    "Horário cadastrado com sucesso!", 
                    "Cadastro Realizado", 
                    JOptionPane.INFORMATION_MESSAGE);
            
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, 
                    "Formato de hora inválido.", 
                    "Erro de Validação", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Verifica se existe conflito com algum horário já cadastrado
     */
    private boolean verificarConflito(String espaco, String data, String horaInicio, String horaTermino) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            Date inicio = format.parse(horaInicio);
            Date termino = format.parse(horaTermino);
            
            for (HorarioDisponivel horario : horariosDisponiveis) {
                if (horario.getEspaco().equals(espaco) && horario.getData().equals(data)) {
                    Date horarioInicio = format.parse(horario.getHoraInicio());
                    Date horarioTermino = format.parse(horario.getHoraTermino());
                    
                    // Verifica se há sobreposição
                    if ((inicio.before(horarioTermino) && termino.after(horarioInicio)) ||
                        inicio.equals(horarioInicio) || termino.equals(horarioTermino)) {
                        return true;
                    }
                }
            }
            
            return false;
        } catch (ParseException e) {
            return false;
        }
    }
    
    /**
     * Atualiza a tabela com os horários cadastrados
     */
    private void atualizarTabela() {
        // Limpa a tabela
        modeloTabela.setRowCount(0);
        
        // Adiciona os horários
        for (HorarioDisponivel horario : horariosDisponiveis) {
            Object[] row = {
                    horario.getEspaco(),
                    horario.getData(),
                    horario.getHoraInicio(),
                    horario.getHoraTermino(),
                    "Excluir"
            };
            modeloTabela.addRow(row);
        }
    }
    
    /**
     * Carrega os dados do arquivo CSV
     */
    private void carregarDadosDoCSV() {
        File arquivo = new File(CSV_FILE);
        
        // Verifica se o arquivo existe
        if (!arquivo.exists()) {
            // Se não existir, cria o arquivo com cabeçalho
            try (PrintWriter writer = new PrintWriter(new FileWriter(arquivo))) {
                writer.println("espaco,data,horaInicio,horaTermino");
                System.out.println("Arquivo CSV criado: " + CSV_FILE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this,
                        "Erro ao criar arquivo CSV: " + e.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
            // Não há dados para carregar, então retorna
            return;
        }
        
        // Limpa a lista atual
        horariosDisponiveis.clear();
        
        // Lê o arquivo CSV
        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            boolean primeiraLinha = true;
            
            while ((linha = reader.readLine()) != null) {
                // Pula o cabeçalho
                if (primeiraLinha) {
                    primeiraLinha = false;
                    continue;
                }
                
                // Divide a linha nos campos separados por vírgula
                String[] campos = parseCsvLine(linha);
                
                if (campos.length >= 4) {
                    String espaco = campos[0];
                    String data = campos[1];
                    String horaInicio = campos[2];
                    String horaTermino = campos[3];
                    
                    HorarioDisponivel horario = new HorarioDisponivel(espaco, data, horaInicio, horaTermino);
                    horariosDisponiveis.add(horario);
                }
            }
            
            // Atualiza a tabela com os dados carregados
            atualizarTabela();
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar dados do CSV: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    /**
     * Salva os dados atuais no arquivo CSV
     */
    private void salvarDadosNoCSV() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CSV_FILE))) {
            // Escreve o cabeçalho
            writer.println("espaco,data,horaInicio,horaTermino");
            
            // Escreve cada horário
            for (HorarioDisponivel horario : horariosDisponiveis) {
                writer.println(
                    escapeCSV(horario.getEspaco()) + "," +
                    horario.getData() + "," +
                    horario.getHoraInicio() + "," +
                    horario.getHoraTermino()
                );
            }
            
            System.out.println("Dados salvos com sucesso no CSV: " + CSV_FILE);
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao salvar dados no CSV: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    /**
     * Parseia uma linha CSV considerando campos entre aspas
     */
    private String[] parseCsvLine(String linha) {
        List<String> resultado = new ArrayList<>();
        StringBuilder atual = new StringBuilder();
        boolean dentroAspas = false;
        
        for (int i = 0; i < linha.length(); i++) {
            char c = linha.charAt(i);
            
            if (c == '"') {
                // Verifica se é um escape para aspas duplas
                if (i + 1 < linha.length() && linha.charAt(i + 1) == '"') {
                    atual.append('"');
                    i++; // Pula o segundo caractere de aspas
                } else {
                    dentroAspas = !dentroAspas;
                }
            } else if (c == ',' && !dentroAspas) {
// Fim do campo
resultado.add(atual.toString());
atual = new StringBuilder();
} else {
atual.append(c);
}
}

// Adiciona o último campo
resultado.add(atual.toString());

return resultado.toArray(new String[0]);
}

/**
* Escapa um valor para CSV se necessário
*/
private String escapeCSV(String valor) {
if (valor == null) {
return "";
}

// Se contém vírgula, aspas ou quebra de linha, envolve com aspas
if (valor.contains(",") || valor.contains("\"") || valor.contains("\n")) {
// Substitui aspas duplas por duas aspas duplas (escape padrão de CSV)
String valorEscapado = valor.replace("\"", "\"\"");
return "\"" + valorEscapado + "\"";
}

return valor;
}

/**
* Exclui um horário da lista e atualiza a tabela e o CSV
*/
private void excluirHorario(int rowIndex) {
if (rowIndex >= 0 && rowIndex < horariosDisponiveis.size()) {
horariosDisponiveis.remove(rowIndex);
atualizarTabela();
// Salva as alterações no CSV
salvarDadosNoCSV();
}
}

/**
* Método para criar ícones de menu
*/
private ImageIcon createMenuIcon(String nome, int width, int height) {
// Aqui você pode carregar ícones reais
// Por enquanto vamos criar ícones simples como placeholders

return createColorIcon(BRANCO, width, height);
}

/**
* Método para criar ícones coloridos simples
*/
private ImageIcon createColorIcon(Color color, int width, int height) {
BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
Graphics2D g2d = img.createGraphics();

g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
g2d.setColor(color);

int padding = 2;
g2d.fillRect(padding, padding, width - 2*padding, height - 2*padding);

g2d.dispose();
return new ImageIcon(img);
}

/**
* Classe para renderizar botões na tabela
*/
class ButtonRenderer extends JButton implements TableCellRenderer {
public ButtonRenderer() {
setOpaque(true);
setBackground(VERDE_ESCURO);
setForeground(BRANCO);
setBorderPainted(false);
setFocusPainted(false);
}

@Override
public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
setText(value.toString());
return this;
}
}

/**
* Classe para editar células com botões na tabela
*/
class ButtonEditor extends DefaultCellEditor {
protected JButton button;
private String label;
private boolean isPushed;
private int row;

public ButtonEditor(JCheckBox checkBox) {
super(checkBox);
button = new JButton();
button.setOpaque(true);
button.setBackground(VERDE_ESCURO);
button.setForeground(BRANCO);
button.setBorderPainted(false);
button.setFocusPainted(false);

button.addActionListener(e -> fireEditingStopped());
}

@Override
public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
label = (value == null) ? "" : value.toString();
button.setText(label);
isPushed = true;
this.row = row;
return button;
}

@Override
public Object getCellEditorValue() {
if (isPushed) {
excluirHorario(row);
}
isPushed = false;
return label;
}

@Override
public boolean stopCellEditing() {
isPushed = false;
return super.stopCellEditing();
}
}

/**
* Classe para botões do menu com suporte a compactação
*/
class MenuButton extends JPanel {
private String texto;
private String icone;
private boolean selecionado;
private boolean modoCompacto;
private JLabel iconLabel;
private JLabel textLabel;
private JPanel badgePanel;

public MenuButton(String texto, String icone, boolean selecionado) {
this.texto = texto;
this.icone = icone;
this.selecionado = selecionado;
this.modoCompacto = false;

setLayout(new BorderLayout());
setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
setMaximumSize(new Dimension(LARGURA_SIDEBAR_EXPANDIDA - 20, 40));
setCursor(new Cursor(Cursor.HAND_CURSOR));

// Cria ícone apropriado baseado no tipo de item
ImageIcon menuIcon = createMenuIcon(icone, 20, 20);
iconLabel = new JLabel(menuIcon);
iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));

// Texto do menu
textLabel = new JLabel(texto);
textLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

// Badge para notificações (apenas para demonstração)
badgePanel = new JPanel();
badgePanel.setOpaque(false);

// Adiciona componentes
add(iconLabel, BorderLayout.WEST);
add(textLabel, BorderLayout.CENTER);
add(badgePanel, BorderLayout.EAST);

// Configura a aparência baseada na seleção
atualizarAparencia();

// Eventos de mouse - simplificados pois agora só temos uma tela funcional
addMouseListener(new MouseAdapter() {
@Override
public void mouseEntered(MouseEvent e) {
    if (!selecionado) {
        setBackground(new Color(30, 80, 55)); // Verde mais escuro para hover
    }
}

@Override
public void mouseExited(MouseEvent e) {
    if (!selecionado) {
        setBackground(VERDE_ESCURO);
    }
}

@Override
public void mousePressed(MouseEvent e) {
    for (MenuButton btn : menuButtons) {
        btn.setSelecionado(false);
    }
    setSelecionado(true);
}
});
}

public void setSelecionado(boolean selecionado) {
this.selecionado = selecionado;
atualizarAparencia();
}

public void setModoCompacto(boolean modoCompacto) {
this.modoCompacto = modoCompacto;
textLabel.setVisible(!modoCompacto);
badgePanel.setVisible(!modoCompacto);

// Ajusta o padding quando em modo compacto
if (modoCompacto) {
setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
} else {
setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
}

atualizarAparencia();
}

private void atualizarAparencia() {
if (selecionado) {
setBackground(VERDE_MEDIO); // Item selecionado agora é verde médio
textLabel.setForeground(BRANCO);
} else {
setBackground(VERDE_ESCURO); // Fundo dos botões agora é verde escuro
textLabel.setForeground(BRANCO); // Texto branco para melhor contraste
}

// Adiciona bordas arredondadas ao botão
int radius = 10;
setBorder(new CompoundBorder(
new EmptyBorder(2, 2, 2, 2),
new RoundedBorder(radius, getBackground())
));
}
}

/**
* Classe para criar bordas arredondadas
*/
class RoundedBorder implements Border {
private int radius;
private Color backgroundColor;

public RoundedBorder(int radius, Color backgroundColor) {
this.radius = radius;
this.backgroundColor = backgroundColor;
}

@Override
public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
Graphics2D g2d = (Graphics2D) g.create();
g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
g2d.setColor(backgroundColor);
g2d.fillRoundRect(x, y, width - 1, height - 1, radius, radius);
g2d.dispose();
}

@Override
public Insets getBorderInsets(Component c) {
return new Insets(4, 8, 4, 8);
}

@Override
public boolean isBorderOpaque() {
return true;
}
}

/**
* Classe que representa um horário disponível
*/
class HorarioDisponivel {
private String espaco;
private String data;
private String horaInicio;
private String horaTermino;

public HorarioDisponivel(String espaco, String data, String horaInicio, String horaTermino) {
this.espaco = espaco;
this.data = data;
this.horaInicio = horaInicio;
this.horaTermino = horaTermino;
}

public String getEspaco() {
return espaco;
}

public String getData() {
return data;
}

public String getHoraInicio() {
return horaInicio;
}

public String getHoraTermino() {
return horaTermino;
}
}

/**
* Método principal para iniciar a aplicação
*/
public static void main(String[] args) {
try {
// Define o tema FlatLaf personalizado com cores do Espaço Capital
UIManager.setLookAndFeel(new FlatLightLaf()); // Usando tema claro como base

// Personaliza cores do tema
UIManager.put("Button.arc", 10);
UIManager.put("Component.arc", 10);
UIManager.put("ProgressBar.arc", 10);
UIManager.put("TextComponent.arc", 10);

// Cores personalizadas para os componentes padrão do swing
UIManager.put("Component.focusColor", new Color(0, 153, 76));
UIManager.put("Button.default.startBackground", new Color(0, 102, 51));
UIManager.put("Button.default.endBackground", new Color(0, 102, 51));
UIManager.put("Button.default.foreground", Color.WHITE);
UIManager.put("TabbedPane.underlineColor", new Color(0, 102, 51));
UIManager.put("TabbedPane.hoverColor", new Color(0, 153, 76, 50));
UIManager.put("Table.selectionBackground", new Color(0, 153, 76, 100));
UIManager.put("TextField.selectionBackground", new Color(0, 153, 76, 100));
UIManager.put("List.selectionBackground", new Color(0, 153, 76, 100));

} catch (Exception e) {
e.printStackTrace();
}

// Inicia a aplicação
SwingUtilities.invokeLater(() -> new EspacoCapitalApp());
}
}