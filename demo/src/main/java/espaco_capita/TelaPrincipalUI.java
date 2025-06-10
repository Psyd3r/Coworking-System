package espaco_capita;

import com.formdev.flatlaf.FlatLightLaf; // Look and Feel

import javax.swing.*;
import javax.swing.Box; // Added for sidebar layout
import javax.swing.BoxLayout; // Added for sidebar layout
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter; // Para campo de data em Agendas

import java.awt.*;
import java.awt.Component; // Added for setAlignmentX
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File; // Para loadIcon
import java.text.ParseException; // Para MaskFormatter e SimpleDateFormat
import java.text.SimpleDateFormat; // Para formatar datas/horas
import java.util.ArrayList;
import java.util.Calendar; // Para manipulação de datas/horas
import java.util.Date;    // Para datas/horas
import java.util.List;    // Para listas

public class TelaPrincipalUI extends JFrame {

    // Constantes de Cores (copiadas de LoginUI ou definidas aqui)
    private final Color VERDE_PRINCIPAL = Color.decode("#007a3e");
    private final Color CINZA_ESCURO = Color.decode("#3a3838");
    private final Color CINZA_CLARO = Color.decode("#d3d3d3");
    private final Color BRANCO = Color.decode("#ffffff");
    private final Color PRETO_SUAVE = Color.decode("#1a1a1a");

    // Constantes para nomes de arquivos CSV
    private static final String ARQUIVO_ESPACOS_CSV = "demo/espacos.csv";
    private static final String ARQUIVO_AGENDAMENTOS_CSV = "demo/agendamentos.csv";
    private static final String ARQUIVO_DISPONIBILIDADES_CSV = "demo/disponibilidades.csv";

    // Listas de Dados em Memória
    private List<Espaco> listaDeEspacos;
    private List<Agendamento> listaDeAgendamentos;
    private List<DisponibilidadeEspaco> listaDisponibilidades;

    // Modelos de Tabela
    private DefaultTableModel modeloTabelaEspacos;
    private DefaultTableModel modeloTabelaAgendamentos;
    private DefaultTableModel modeloTabelaDisponibilidades;

    // Componentes da UI referenciáveis entre métodos
    private JPanel painelConteudo; // Principal painel central com CardLayout

    // --- Componentes da Aba Agendas ---
    private JComboBox<Espaco> comboBoxEspacosAgendamento; // Seletor de espaços na aba Agendas
    private JFormattedTextField campoDataAgendamento;
    private JSpinner spinnerHoraInicioAgendamento;
    private JSpinner spinnerHoraFimAgendamento;

    // --- Componentes da Aba Horários Disponíveis ---
    private JComboBox<Espaco> comboBoxEspacosDisponibilidade; // Seletor de espaços na aba Disponibilidade
    private List<JCheckBox> checkBoxesDiasSemana;
    private JSpinner spinnerHoraInicioDisp;
    private JSpinner spinnerHoraFimDisp;

    // Ícones para abas da Sidebar
    private ImageIcon iconeEspacos;
    private ImageIcon iconeAgendas;
    private ImageIcon iconeHorariosDisponiveis; // Placeholder para relogio-e-calendario.PNG
    private ImageIcon iconeSair;

    // Construtor Principal
    public TelaPrincipalUI() {
        setTitle("Espaço Capital - Sistema de Agendamento Principal");
        setSize(1280, 720); // Tamanho um pouco maior
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        // Inicializar listas
        this.listaDeEspacos = new ArrayList<>();
        this.listaDeAgendamentos = new ArrayList<>();
        this.listaDisponibilidades = new ArrayList<>();
        this.checkBoxesDiasSemana = new ArrayList<>(); // Para aba Disponibilidade

        carregarDadosIniciais();       // Carrega dados dos CSVs (será implementado)
        inicializarComponentesVisuais(); // Configura a UI (será implementado)

        setVisible(true);
    }

    // Método para carregar todos os dados iniciais (dos CSVs)
    private void carregarDadosIniciais() {
        System.out.println("Iniciando carregamento de dados dos arquivos CSV...");

        // Carregar Espaços
        this.listaDeEspacos = GerenciadorCSVDados.carregarEspacosDoCSV(ARQUIVO_ESPACOS_CSV);
        System.out.println(this.listaDeEspacos.size() + " espaços carregados de " + ARQUIVO_ESPACOS_CSV);

        // Carregar Agendamentos (precisa da listaDeEspacos para associar)
        this.listaDeAgendamentos = GerenciadorCSVDados.carregarAgendamentosDoCSV(ARQUIVO_AGENDAMENTOS_CSV, this.listaDeEspacos);
        System.out.println(this.listaDeAgendamentos.size() + " agendamentos carregados de " + ARQUIVO_AGENDAMENTOS_CSV);

        // Carregar Disponibilidades (precisa da listaDeEspacos para associar)
        this.listaDisponibilidades = GerenciadorCSVDados.carregarDisponibilidadesDoCSV(ARQUIVO_DISPONIBILIDADES_CSV, this.listaDeEspacos);
        System.out.println(this.listaDisponibilidades.size() + " registros de disponibilidade carregados de " + ARQUIVO_DISPONIBILIDADES_CSV);

        System.out.println("Carregamento de dados iniciais concluído.");
    }

    // Método para inicializar e configurar todos os componentes visuais
    private void inicializarComponentesVisuais() {
        // Painel Principal com BorderLayout
        JPanel painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.setBackground(BRANCO); // Fundo geral, se visível
        setContentPane(painelPrincipal);

        // === Sidebar (Painel Lateral Esquerdo) ===
        JPanel painelLateral = new JPanel();
        painelLateral.setLayout(new BoxLayout(painelLateral, BoxLayout.Y_AXIS));
        painelLateral.setBackground(VERDE_PRINCIPAL);
        painelLateral.setPreferredSize(new Dimension(260, 0)); // Largura da sidebar
        painelLateral.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0)); // Padding vertical

        // Carregar Ícones para a Sidebar (já devem estar como campos da classe)
        this.iconeEspacos = loadIcon("user.png"); // Usar "espaco.PNG" quando disponível e confirmado
        this.iconeAgendas = loadIcon("calendar-day.png");
        this.iconeHorariosDisponiveis = loadIcon("relogio-e-calendario.PNG"); // Usará se existir
        this.iconeSair = loadIcon("leave.png");

        // Botões da Sidebar
        JButton abaEspacosSidebar = new JButton("Espaços");
        configurarBotaoSidebar(abaEspacosSidebar, this.iconeEspacos);

        JButton abaAgendasSidebar = new JButton("Agendas");
        configurarBotaoSidebar(abaAgendasSidebar, this.iconeAgendas);

        JButton abaHorariosDisponiveis = new JButton("Disponibilidade");
        configurarBotaoSidebar(abaHorariosDisponiveis, this.iconeHorariosDisponiveis);

        JButton botaoSair = new JButton("Sair");
        configurarBotaoSidebar(botaoSair, this.iconeSair);
        botaoSair.addActionListener(e -> { // Ação de Sair já implementada aqui
            dispose();
            SwingUtilities.invokeLater(() -> new LoginUI().setVisible(true));
        });

        // Adicionar componentes à sidebar
        // Adicionar título "Menu" ou similar, se desejado (ex: JLabel)
        JLabel labelMenuTitle = new JLabel("Menu Principal");
        labelMenuTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        labelMenuTitle.setForeground(BRANCO);
        labelMenuTitle.setAlignmentX(Component.CENTER_ALIGNMENT); // Centralizar se usar BoxLayout na direção X
        labelMenuTitle.setBorder(new EmptyBorder(0,0,15,0)); // Margem inferior
        // Para alinhar o label ao centro ou esquerda no BoxLayout Y_AXIS, pode precisar de um painel extra
        // ou ajustar alinhamento X dos componentes.
        // Por simplicidade, vamos alinhar os botões à esquerda e o título pode ficar assim.

        // painelLateral.add(labelMenuTitle); // Opcional: Título para a Sidebar

        Dimension maxButtonSize = new Dimension(Integer.MAX_VALUE, abaEspacosSidebar.getPreferredSize().height + 10);

        abaEspacosSidebar.setAlignmentX(Component.LEFT_ALIGNMENT);
        abaEspacosSidebar.setMaximumSize(maxButtonSize);

        abaAgendasSidebar.setAlignmentX(Component.LEFT_ALIGNMENT);
        abaAgendasSidebar.setMaximumSize(maxButtonSize);

        abaHorariosDisponiveis.setAlignmentX(Component.LEFT_ALIGNMENT);
        abaHorariosDisponiveis.setMaximumSize(maxButtonSize);

        botaoSair.setAlignmentX(Component.LEFT_ALIGNMENT);
        botaoSair.setMaximumSize(maxButtonSize);

        painelLateral.add(Box.createVerticalStrut(10));
        painelLateral.add(abaEspacosSidebar);
        painelLateral.add(Box.createVerticalStrut(10));
        painelLateral.add(abaAgendasSidebar);
        painelLateral.add(Box.createVerticalStrut(10));
        painelLateral.add(abaHorariosDisponiveis);

        painelLateral.add(Box.createVerticalGlue()); // Empurra 'Sair' para baixo
        painelLateral.add(botaoSair);
        painelLateral.add(Box.createVerticalStrut(10)); // Espaço abaixo do botão Sair

        painelPrincipal.add(painelLateral, BorderLayout.WEST);

        // === Painel de Conteúdo Central (CardLayout) ===
        this.painelConteudo = new JPanel(new CardLayout());
        this.painelConteudo.setBackground(BRANCO); // Fundo para a área de conteúdo

        // Painel Default/Inicial
        JPanel painelDefault = new JPanel(new BorderLayout());
        painelDefault.setBackground(BRANCO);
        JLabel labelDefault = new JLabel("Bem-vindo ao Sistema de Agendamento! Selecione uma opção no menu.", SwingConstants.CENTER);
        labelDefault.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        labelDefault.setForeground(CINZA_ESCURO);
        painelDefault.add(labelDefault, BorderLayout.CENTER);
        this.painelConteudo.add(painelDefault, "painelDefault"); // Adiciona com um nome

        painelPrincipal.add(this.painelConteudo, BorderLayout.CENTER);

        // ActionListeners para abas (serão completados quando os métodos criarPainel... forem refeitos)
        // Exemplo para abaEspacosSidebar (a lógica completa será refeita depois):
        abaEspacosSidebar.addActionListener(e -> {
            // ((CardLayout) this.painelConteudo.getLayout()).show(this.painelConteudo, "painelEspacos");
            System.out.println("Botão Espaços clicado - painel será carregado no próximo passo.");
        });
        abaAgendasSidebar.addActionListener(e -> {
            System.out.println("Botão Agendas clicado - painel será carregado no próximo passo.");
        });
        abaHorariosDisponiveis.addActionListener(e -> {
            final String NOME_PAINEL = "painelHorariosDisponiveis"; // Usar uma constante
            // Verifica se o painel já existe no CardLayout
            boolean painelJaExiste = false;
            Component painelExistente = null;
            for (Component comp : painelConteudo.getComponents()) {
                if (comp.getName() != null && comp.getName().equals(NOME_PAINEL)) {
                    painelJaExiste = true;
                    painelExistente = comp; // Guarda referência se existir
                    break;
                }
            }

            if (!painelJaExiste) {
                System.out.println("Criando novo painelHorariosDisponiveis...");
                JPanel novoPainel = criarPainelHorariosDisponiveis();
                novoPainel.setName(NOME_PAINEL);
                this.painelConteudo.add(novoPainel, NOME_PAINEL);
                ((CardLayout) this.painelConteudo.getLayout()).show(this.painelConteudo, NOME_PAINEL);
            } else {
                System.out.println("Painel painelHorariosDisponiveis já existe. Apenas mostrando.");
                // Se o painel já existe, talvez seja necessário chamar um método para atualizar seus dados,
                // como o ComboBox de espaços, caso a lista de espaços tenha mudado desde a última vez.
                // Por exemplo: if (painelExistente instanceof JPanel) { /* lógica para atualizar dados */ }
                // E também chamar atualizarTabelaDisponibilidades() para recarregar a tabela com o espaço selecionado.
                if (this.comboBoxEspacosDisponibilidade != null && this.comboBoxEspacosDisponibilidade.getItemCount() > 0) {
                     if(this.comboBoxEspacosDisponibilidade.getSelectedIndex() == -1) { // se nada estiver selecionado
                        this.comboBoxEspacosDisponibilidade.setSelectedIndex(0); // seleciona o primeiro
                     } else {
                        // Força a atualização da tabela com base no item já selecionado
                        atualizarTabelaDisponibilidades();
                     }
                } else {
                     // Se o combobox estiver vazio (ex: nenhum espaço cadastrado), limpa a tabela.
                     if(this.modeloTabelaDisponibilidades != null) this.modeloTabelaDisponibilidades.setRowCount(0);
                }
                ((CardLayout) this.painelConteudo.getLayout()).show(this.painelConteudo, NOME_PAINEL);
            }
        });

        // Mostrar painel default inicialmente
        ((CardLayout) this.painelConteudo.getLayout()).show(this.painelConteudo, "painelDefault");
    }

    // Método para configurar o estilo dos botões da Sidebar
    private void configurarBotaoSidebar(JButton botao, ImageIcon icone) {
        botao.setFont(new Font("Segoe UI", Font.BOLD, 16));
        botao.setForeground(BRANCO);
        botao.setBackground(VERDE_PRINCIPAL);
        botao.setOpaque(false); // Controla o fundo no hover
        botao.setFocusPainted(false);
        botao.setBorderPainted(false);
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botao.setHorizontalAlignment(SwingConstants.LEFT);
        botao.setIconTextGap(15);
        botao.setMargin(new Insets(10, 15, 10, 15)); // Padding interno

        if (icone != null) {
            botao.setIcon(new ImageIcon(icone.getImage().getScaledInstance(22, 22, Image.SCALE_SMOOTH)));
        } else {
            // Adiciona um placeholder ou um ícone padrão se o ícone específico não for carregado
            // Ex: botao.setIcon(loadIcon("default_icon.png"));
            // Por enquanto, deixamos sem ícone se for null
        }

        botao.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botao.setOpaque(true);
                botao.setBackground(VERDE_PRINCIPAL.brighter().brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                botao.setOpaque(false);
                botao.setBackground(VERDE_PRINCIPAL);
            }
        });
    }

    private JPanel criarPainelHorariosDisponiveis() {
        System.out.println(">>> Iniciando criarPainelHorariosDisponiveis()...");
        JPanel painelPrincipalAba = new JPanel(new BorderLayout(10, 10));
        painelPrincipalAba.setBackground(BRANCO);
        painelPrincipalAba.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel tituloHorarios = new JLabel("Gerenciar Horários Disponíveis dos Espaços", SwingConstants.CENTER);
        tituloHorarios.setFont(new Font("Segoe UI", Font.BOLD, 22));
        tituloHorarios.setForeground(PRETO_SUAVE);
        tituloHorarios.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        // painelPrincipalAba.add(tituloHorarios, BorderLayout.NORTH); // Título será adicionado em painelSuperior

        // Painel de Controles para adicionar/editar disponibilidade
        JPanel painelControles = new JPanel(new GridBagLayout());
        painelControles.setBackground(BRANCO);
        painelControles.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Definir Nova Disponibilidade",
            javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
            javax.swing.border.TitledBorder.DEFAULT_POSITION,
            new Font("Segoe UI", Font.BOLD, 14), PRETO_SUAVE
        ));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Seletor de Espaço
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 0.1; // Label com menos peso
        painelControles.add(new JLabel("Espaço:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        gbc.weightx = 0.9; // ComboBox com mais peso
        this.comboBoxEspacosDisponibilidade = new JComboBox<>(); // Campo da classe
        // Logs para depuração da população do ComboBox (serão adicionados/verificados no Passo 5 do plano)
        System.out.println("Verificando listaDeEspacos para ComboBox de Disponibilidade (em criarPainelHorariosDisponiveis)...");
        if (this.listaDeEspacos == null) {
            System.out.println("!!! listaDeEspacos é NULA ao criar comboBoxEspacosDisponibilidade.");
        } else {
            System.out.println("Nº de espaços em listaDeEspacos: " + this.listaDeEspacos.size());
            if (this.listaDeEspacos.isEmpty()) {
                System.out.println("--- listaDeEspacos está VAZIA ao criar comboBoxEspacosDisponibilidade.");
            }
            for (Espaco esp : this.listaDeEspacos) {
                if (esp != null) this.comboBoxEspacosDisponibilidade.addItem(esp);
            }
        }
        System.out.println("Nº de itens no comboBoxEspacosDisponibilidade: " + this.comboBoxEspacosDisponibilidade.getItemCount());

        this.comboBoxEspacosDisponibilidade.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Espaco) setText(((Espaco) value).getNome());
                return this;
            }
        });
        this.comboBoxEspacosDisponibilidade.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        this.comboBoxEspacosDisponibilidade.addActionListener(e -> atualizarTabelaDisponibilidades()); // Chama método a ser implementado
        painelControles.add(this.comboBoxEspacosDisponibilidade, gbc);
        gbc.gridwidth = 1; gbc.weightx = 0; // Reset

        // Dias da Semana (Checkboxes)
        gbc.gridx = 0; gbc.gridy = 1;
        painelControles.add(new JLabel("Dias da Semana:"), gbc);
        JPanel painelDiasSemana = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 0)); // Diminuído espaçamento
        painelDiasSemana.setBackground(BRANCO);
        this.checkBoxesDiasSemana.clear();
        for (DiaDaSemana dia : DiaDaSemana.values()) {
            JCheckBox checkBox = new JCheckBox(dia.getNomeFormatado().substring(0, 3));
            checkBox.setFont(new Font("Segoe UI", Font.PLAIN, 11)); // Fonte menor
            checkBox.setBackground(BRANCO);
            checkBox.setName(dia.name());
            this.checkBoxesDiasSemana.add(checkBox);
            painelDiasSemana.add(checkBox);
        }
        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 3;
        painelControles.add(painelDiasSemana, gbc);
        gbc.gridwidth = 1;

        // Hora Início Disponível
        gbc.gridx = 0; gbc.gridy = 2;
        painelControles.add(new JLabel("Das:"), gbc);
        gbc.gridx = 1;
        this.spinnerHoraInicioDisp = new JSpinner(new SpinnerDateModel()); // Campo da classe
        JSpinner.DateEditor editorInicioDisp = new JSpinner.DateEditor(this.spinnerHoraInicioDisp, "HH:mm");
        this.spinnerHoraInicioDisp.setEditor(editorInicioDisp);
        this.spinnerHoraInicioDisp.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        ((JSpinner.DefaultEditor)this.spinnerHoraInicioDisp.getEditor()).getTextField().setColumns(4);
        painelControles.add(this.spinnerHoraInicioDisp, gbc);

        // Hora Fim Disponível
        gbc.gridx = 2;
        painelControles.add(new JLabel("Até:"), gbc);
        gbc.gridx = 3;
        this.spinnerHoraFimDisp = new JSpinner(new SpinnerDateModel()); // Campo da classe
        JSpinner.DateEditor editorFimDisp = new JSpinner.DateEditor(this.spinnerHoraFimDisp, "HH:mm");
        this.spinnerHoraFimDisp.setEditor(editorFimDisp);
        this.spinnerHoraFimDisp.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        ((JSpinner.DefaultEditor)this.spinnerHoraFimDisp.getEditor()).getTextField().setColumns(4);
        painelControles.add(this.spinnerHoraFimDisp, gbc);

        // Botão Salvar Disponibilidade
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.EAST; gbc.fill = GridBagConstraints.NONE; // Alinhado à direita
        gbc.insets = new Insets(10, 8, 5, 8); // Mais margem superior
        JButton botaoSalvarDisponibilidade = new JButton("Salvar Disponibilidade");
        botaoSalvarDisponibilidade.setFont(new Font("Segoe UI", Font.BOLD, 14));
        botaoSalvarDisponibilidade.setForeground(BRANCO);
        botaoSalvarDisponibilidade.setBackground(VERDE_PRINCIPAL);
        botaoSalvarDisponibilidade.setOpaque(true);
        botaoSalvarDisponibilidade.setBorderPainted(false);
        botaoSalvarDisponibilidade.addActionListener(e -> {
            System.out.println(">>> Botão Salvar Disponibilidade clicado.");
            Espaco espacoSelecionado = (Espaco) this.comboBoxEspacosDisponibilidade.getSelectedItem();
            if (espacoSelecionado == null) {
                JOptionPane.showMessageDialog(this, "Por favor, selecione um espaço para definir a disponibilidade.", "Validação - Espaço", JOptionPane.WARNING_MESSAGE);
                System.out.println("Nenhum espaço selecionado.");
                return;
            }
            System.out.println("Espaço selecionado para salvar disponibilidade: " + espacoSelecionado.getNome());

            Date horaInicioInput = (Date) this.spinnerHoraInicioDisp.getValue();
            Date horaFimInput = (Date) this.spinnerHoraFimDisp.getValue();

            // Normalizar as horas para uma data base (01/01/1970) para consistência no armazenamento e comparação
            Calendar calHelper = Calendar.getInstance();

            calHelper.setTime(horaInicioInput);
            calHelper.set(1970, Calendar.JANUARY, 1); // Ano, Mês (Janeiro=0), Dia
            Date horaInicioNormalizada = calHelper.getTime();

            calHelper.setTime(horaFimInput);
            calHelper.set(1970, Calendar.JANUARY, 1);
            Date horaFimNormalizada = calHelper.getTime();

            System.out.println("Hora Início Normalizada: " + new SimpleDateFormat("HH:mm").format(horaInicioNormalizada) +
                               ", Hora Fim Normalizada: " + new SimpleDateFormat("HH:mm").format(horaFimNormalizada));


            if (horaFimNormalizada.before(horaInicioNormalizada) || horaFimNormalizada.equals(horaInicioNormalizada)) {
                JOptionPane.showMessageDialog(this, "A hora de fim da disponibilidade deve ser estritamente posterior à hora de início.", "Validação - Horário", JOptionPane.WARNING_MESSAGE);
                System.out.println("Validação de horário falhou: Fim <= Início.");
                return;
            }

            boolean algumDiaSelecionado = false;
            int disponibilidadesProcessadas = 0;
            for (JCheckBox checkBoxDia : this.checkBoxesDiasSemana) {
                if (checkBoxDia.isSelected()) {
                    algumDiaSelecionado = true;
                    DiaDaSemana dia = DiaDaSemana.fromString(checkBoxDia.getName()); // Recupera o enum pelo nome do componente

                    if (dia != null) {
                        System.out.println("Processando disponibilidade para o dia: " + dia.getNomeFormatado());
                        // Lógica "última configuração ganha": remove qualquer disponibilidade existente para este espaço e dia.
                        boolean removidoExistente = this.listaDisponibilidades.removeIf(disp ->
                            disp.getEspaco().getId().equals(espacoSelecionado.getId()) &&
                            disp.getDiaDaSemana() == dia);
                        if (removidoExistente) {
                            System.out.println("Disponibilidade existente removida para " + espacoSelecionado.getNome() + " na " + dia.getNomeFormatado());
                        }

                        DisponibilidadeEspaco novaDisp = new DisponibilidadeEspaco(
                            espacoSelecionado,
                            dia,
                            horaInicioNormalizada,
                            horaFimNormalizada
                        );
                        this.listaDisponibilidades.add(novaDisp);
                        disponibilidadesProcessadas++;
                        System.out.println("Nova disponibilidade adicionada: " + novaDisp.toString());
                    } else {
                        System.err.println("Erro: DiaDaSemana.fromString retornou null para o checkbox: " + checkBoxDia.getName());
                    }
                }
            }

            if (!algumDiaSelecionado) {
                JOptionPane.showMessageDialog(this, "Por favor, selecione pelo menos um dia da semana para definir a disponibilidade.", "Validação - Dias", JOptionPane.WARNING_MESSAGE);
                System.out.println("Nenhum dia da semana selecionado.");
                return;
            }

            if (disponibilidadesProcessadas > 0) {
                System.out.println(disponibilidadesProcessadas + " registros de disponibilidade foram adicionados/atualizados na lista em memória.");
                GerenciadorCSVDados.salvarDisponibilidadesNoCSV(ARQUIVO_DISPONIBILIDADES_CSV, this.listaDisponibilidades);
                atualizarTabelaDisponibilidades();
                JOptionPane.showMessageDialog(this, "Disponibilidade(s) salva(s) com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                System.out.println("Disponibilidades salvas no CSV e tabela atualizada.");

                // Limpar checkboxes dos dias após salvar
                for (JCheckBox checkBoxDia : this.checkBoxesDiasSemana) {
                    checkBoxDia.setSelected(false);
                }
                System.out.println("Checkboxes de dias da semana limpos.");
            } else {
                // Isso pode acontecer se os dias selecionados resultarem em dia==null, o que não deveria.
                System.out.println("Nenhuma disponibilidade foi processada para salvamento (verifique logs de erro de DiaDaSemana).");
            }
            System.out.println("<<< ActionListener Salvar Disponibilidade concluído.");
        });
        painelControles.add(botaoSalvarDisponibilidade, gbc);
        gbc.anchor = GridBagConstraints.WEST; // Reset

        // Painel para a Tabela de Disponibilidades
        JPanel painelTabela = new JPanel(new BorderLayout());
        painelTabela.setBackground(BRANCO);
        painelTabela.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Disponibilidades Registradas para o Espaço Selecionado",
             javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
             javax.swing.border.TitledBorder.DEFAULT_POSITION,
            new Font("Segoe UI", Font.BOLD, 14), PRETO_SUAVE
        ));

        String[] colunasTabelaDisp = {"Dia da Semana", "Hora Início", "Hora Fim", "Ações"};
        this.modeloTabelaDisponibilidades = new DefaultTableModel(colunasTabelaDisp, 0) { // Campo da classe
            @Override
            public boolean isCellEditable(int row, int column) { return column == (colunasTabelaDisp.length - 1); }
        };
        JTable tabelaDisponibilidades = new JTable(this.modeloTabelaDisponibilidades);
        tabelaDisponibilidades.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabelaDisponibilidades.setRowHeight(30); // Aumentar altura da linha para botões de ação
        tabelaDisponibilidades.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        tabelaDisponibilidades.getTableHeader().setBackground(CINZA_CLARO);
        tabelaDisponibilidades.getTableHeader().setForeground(PRETO_SUAVE);
        // Ações da tabela (Renderer/Editor) serão adicionadas no Passo 2.
        // Configurar a coluna "Ações" para a tabela de disponibilidades
        int indiceColunaAcoesDisp = this.modeloTabelaDisponibilidades.getColumnCount() - 1;
        if (indiceColunaAcoesDisp >= 0 && this.modeloTabelaDisponibilidades.getColumnName(indiceColunaAcoesDisp).equals("Ações")) {

            AcoesTabelaCellRendererEditor rendererEditorDisp = new AcoesTabelaCellRendererEditor(tabelaDisponibilidades);
            // Por enquanto, só o botão Excluir terá funcionalidade. O Editar mostrará uma mensagem.
            // Se AcoesTabelaPanel for refatorado para aceitar quais botões mostrar, isso pode ser ajustado.

            tabelaDisponibilidades.getColumnModel().getColumn(indiceColunaAcoesDisp).setCellRenderer(rendererEditorDisp);
            tabelaDisponibilidades.getColumnModel().getColumn(indiceColunaAcoesDisp).setCellEditor(rendererEditorDisp);

            tabelaDisponibilidades.getColumnModel().getColumn(indiceColunaAcoesDisp).setPreferredWidth(85);
            tabelaDisponibilidades.getColumnModel().getColumn(indiceColunaAcoesDisp).setMinWidth(80);
            tabelaDisponibilidades.getColumnModel().getColumn(indiceColunaAcoesDisp).setMaxWidth(100);

            // ActionListener para o botão Excluir (a ser implementado no Passo 4)
            rendererEditorDisp.addActionListenerParaExcluir(e -> {
                int linhaSelecionadaVisual = tabelaDisponibilidades.getSelectedRow();
                int linhaModelo = -1;
                if (linhaSelecionadaVisual != -1) {
                    linhaModelo = tabelaDisponibilidades.convertRowIndexToModel(linhaSelecionadaVisual);
                } else {
                    linhaModelo = tabelaDisponibilidades.getEditingRow();
                }

                if (linhaModelo != -1) {
                    removerDisponibilidade(linhaModelo);

                } else {
                    JOptionPane.showMessageDialog(TelaPrincipalUI.this,
                                                "Por favor, selecione um registro para remover.",
                                                "Aviso", JOptionPane.WARNING_MESSAGE);
                }
            });

            rendererEditorDisp.addActionListenerParaEditar(e -> {
                JOptionPane.showMessageDialog(TelaPrincipalUI.this,
                                            "Para editar uma disponibilidade, defina-a novamente usando os campos acima e clique em 'Salvar Disponibilidade'.",
                                            "Informação", JOptionPane.INFORMATION_MESSAGE);
            });
        }
        JScrollPane scrollPaneTabelaDisp = new JScrollPane(tabelaDisponibilidades);
        painelTabela.add(scrollPaneTabelaDisp, BorderLayout.CENTER);

        // Painel Superior (Título + Controles)
        JPanel painelSuperiorAba = new JPanel(new BorderLayout(0,15)); // Espaçamento entre título e controles
        painelSuperiorAba.setOpaque(false);
        painelSuperiorAba.add(tituloHorarios, BorderLayout.NORTH);
        painelSuperiorAba.add(painelControles, BorderLayout.CENTER);

        painelPrincipalAba.add(painelSuperiorAba, BorderLayout.NORTH);
        painelPrincipalAba.add(painelTabela, BorderLayout.CENTER);

        // Chamada inicial para popular a tabela (será implementada no Passo 2)
        // atualizarTabelaDisponibilidades();
        System.out.println("<<< criarPainelHorariosDisponiveis() concluído.");
        return painelPrincipalAba;
    }

    private void atualizarTabelaDisponibilidades() {
        System.out.println(">>> Iniciando atualizarTabelaDisponibilidades()...");
        if (this.modeloTabelaDisponibilidades == null) {
            System.out.println("modeloTabelaDisponibilidades é nulo. Abortando atualização.");
            return;
        }
        this.modeloTabelaDisponibilidades.setRowCount(0); // Limpa a tabela

        Espaco espacoSelecionado = null;
        if (this.comboBoxEspacosDisponibilidade != null &&
            this.comboBoxEspacosDisponibilidade.getSelectedItem() instanceof Espaco) {
            espacoSelecionado = (Espaco) this.comboBoxEspacosDisponibilidade.getSelectedItem();
            System.out.println("Espaço selecionado para filtro de disponibilidade: " + (espacoSelecionado != null ? espacoSelecionado.getNome() : "Nenhum"));
        } else {
            System.out.println("Nenhum espaço selecionado no ComboBox de Disponibilidade ou ComboBox não inicializado. Tabela ficará vazia.");
            return; // Se nenhum espaço estiver selecionado, não há o que mostrar.
        }

        if (this.listaDisponibilidades == null) {
            System.out.println("listaDisponibilidades é nula. Tabela ficará vazia.");
            return;
        }

        int disponibilidadesAdicionadas = 0;
        for (DisponibilidadeEspaco disp : this.listaDisponibilidades) {
            if (disp != null && disp.getEspaco() != null && espacoSelecionado != null && disp.getEspaco().getId().equals(espacoSelecionado.getId())) {
                this.modeloTabelaDisponibilidades.addRow(new Object[]{
                    disp.getDiaDaSemana() != null ? disp.getDiaDaSemana().getNomeFormatado() : "N/A",
                    disp.getHoraInicioFormatada(),
                    disp.getHoraFimFormatada(),
                    null // Para a coluna de ações (botões)
                });
                disponibilidadesAdicionadas++;
            }
        }
        System.out.println(disponibilidadesAdicionadas + " registros de disponibilidade adicionados à tabela para o espaço: " + espacoSelecionado.getNome());
        System.out.println("<<< atualizarTabelaDisponibilidades() concluído.");
    }

    private void removerDisponibilidade(int linhaModeloTabelaFiltrada) {
        System.out.println(">>> Iniciando removerDisponibilidade para linha da tabela (filtrada): " + linhaModeloTabelaFiltrada);

        Espaco espacoSelecionadoFiltro = null;
        if (this.comboBoxEspacosDisponibilidade != null &&
            this.comboBoxEspacosDisponibilidade.getSelectedItem() instanceof Espaco) {
            espacoSelecionadoFiltro = (Espaco) this.comboBoxEspacosDisponibilidade.getSelectedItem();
        }

        if (espacoSelecionadoFiltro == null) {
            JOptionPane.showMessageDialog(this,
                "Não foi possível identificar o espaço selecionado para remover a disponibilidade.",
                "Erro Interno", JOptionPane.ERROR_MESSAGE);
            System.err.println("Erro em removerDisponibilidade: espacoSelecionadoFiltro é nulo.");
            return;
        }
        System.out.println("Removendo disponibilidade do espaço: " + espacoSelecionadoFiltro.getNome());

        // A linhaModeloTabelaFiltrada é o índice da *visão filtrada* da tabela.
        // Precisamos encontrar o objeto DisponibilidadeEspaco correspondente na lista global.
        // Primeiro, criamos uma sub-lista temporária das disponibilidades que estão atualmente na tabela.
        List<DisponibilidadeEspaco> disponibilidadesExibidas = new ArrayList<>();
        if (this.listaDisponibilidades != null) {
            for (DisponibilidadeEspaco disp : this.listaDisponibilidades) {
                if (disp.getEspaco().getId().equals(espacoSelecionadoFiltro.getId())) {
                    disponibilidadesExibidas.add(disp);
                }
            }
        }

        if (linhaModeloTabelaFiltrada >= 0 && linhaModeloTabelaFiltrada < disponibilidadesExibidas.size()) {
            DisponibilidadeEspaco dispParaRemoverDaListaOriginal = disponibilidadesExibidas.get(linhaModeloTabelaFiltrada);
            System.out.println("Disponibilidade para remover (identificada pela tabela): " + dispParaRemoverDaListaOriginal.toString());

            int confirmacao = JOptionPane.showConfirmDialog(
                this,
                "Tem certeza que deseja remover a disponibilidade para " +
                dispParaRemoverDaListaOriginal.getEspaco().getNome() + " na " +
                dispParaRemoverDaListaOriginal.getDiaDaSemana().getNomeFormatado() + " de " +
                dispParaRemoverDaListaOriginal.getHoraInicioFormatada() + " às " + dispParaRemoverDaListaOriginal.getHoraFimFormatada() + "?",
                "Confirmar Remoção de Disponibilidade",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );

            if (confirmacao == JOptionPane.YES_OPTION) {
                // Remover da lista GLOBAL 'listaDisponibilidades' usando o ID do objeto.
                boolean removidoComSucesso = this.listaDisponibilidades.removeIf(
                    d -> d.getId().equals(dispParaRemoverDaListaOriginal.getId())
                );

                if (removidoComSucesso) {
                    System.out.println("Disponibilidade com ID " + dispParaRemoverDaListaOriginal.getId() + " removida da listaDisponibilidades.");
                    GerenciadorCSVDados.salvarDisponibilidadesNoCSV(ARQUIVO_DISPONIBILIDADES_CSV, this.listaDisponibilidades);
                    atualizarTabelaDisponibilidades(); // Atualiza a UI da tabela
                    JOptionPane.showMessageDialog(this, "Disponibilidade removida com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    System.out.println("Disponibilidades salvas no CSV e tabela atualizada após remoção.");
                } else {
                    System.err.println("Falha ao remover disponibilidade da listaDisponibilidades (ID: " + dispParaRemoverDaListaOriginal.getId() + " não encontrado na lista global, o que é inesperado).");
                     JOptionPane.showMessageDialog(this, "Erro: A disponibilidade não pôde ser encontrada na lista principal para remoção.", "Erro Interno", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                System.out.println("Remoção de disponibilidade cancelada pelo usuário.");
            }
        } else {
            System.err.println("Índice de linha inválido (" + linhaModeloTabelaFiltrada + ") para a lista de disponibilidades exibidas (tamanho: " + disponibilidadesExibidas.size() + ").");
            JOptionPane.showMessageDialog(this, "Erro: Índice da linha selecionada é inválido.", "Erro Interno", JOptionPane.ERROR_MESSAGE);
        }
        System.out.println("<<< removerDisponibilidade() concluído.");
    }

    // Método para carregar ícones (versão flexível)
    private ImageIcon loadIcon(String path) {
        try {
            File fileFromPath = new File(path);
            if (fileFromPath.isAbsolute() && fileFromPath.exists()) {
                return new ImageIcon(path);
            }
            String basePathIcons = "demo/src/main/resources/icons/";
            File iconFile = new File(basePathIcons + path);
            if (iconFile.exists()) {
                return new ImageIcon(iconFile.getAbsolutePath());
            } else {
                String basePathResources = "demo/src/main/resources/";
                File resourceFile = new File(basePathResources + path);
                if (resourceFile.exists()) {
                    return new ImageIcon(resourceFile.getAbsolutePath());
                } else {
                    System.err.println("Ícone não encontrado em /icons/ ou /resources/: " + path);
                    return null;
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar ícone: " + path + " - " + e.getMessage());
            // e.printStackTrace(); // Descomentar para debug detalhado do erro do ícone
            return null;
        }
    }

    // Main para testes isolados da TelaPrincipalUI
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
            UIManager.put("Button.arc", 10); // Arredondamento global de botões
            UIManager.put("Component.arc", 10); // Arredondamento para outros componentes
            UIManager.put("ProgressBar.arc", 10);
            UIManager.put("TextComponent.arc", 6); // Leve arredondamento para campos de texto
            UIManager.put("Table.selectionBackground", new Color(180, 215, 255)); // Cor de seleção mais suave para tabelas
            UIManager.put("Table.selectionForeground", Color.BLACK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new TelaPrincipalUI().setVisible(true));
    }
}
