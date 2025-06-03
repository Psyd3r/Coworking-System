package espaco_capita;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import java.awt.*;
import java.io.File; // Importação para o método loadIcon
// import java.net.URL; // Importação para futura refatoração do loadIcon com getResource
import javax.swing.BoxLayout; // Importação para BoxLayout
import javax.swing.Box; // Importação para Box
import java.awt.Component; // Importação para Component.LEFT_ALIGNMENT
import java.awt.CardLayout; // Importação para CardLayout
import javax.swing.JTable; // Importação para JTable
import javax.swing.JScrollPane; // Importação para JScrollPane
import javax.swing.table.DefaultTableModel; // Importação para DefaultTableModel
import javax.swing.ListSelectionModel; // Importação para ListSelectionModel
import java.util.List; // Importação para List
import java.util.ArrayList; // Importação para ArrayList
import javax.swing.JOptionPane; // Importação para JOptionPane
import javax.swing.JComboBox; // Importação para JComboBox
import javax.swing.JFormattedTextField; // Importação para JFormattedTextField
import javax.swing.text.MaskFormatter; // Importação para MaskFormatter
import javax.swing.JSpinner; // Importação para JSpinner
import javax.swing.SpinnerDateModel; // Importação para SpinnerDateModel
import java.util.Date; // Importação para Date (usado em SpinnerDateModel)
import java.util.Calendar; // Importação para Calendar
import javax.swing.DefaultListCellRenderer; // Importação para DefaultListCellRenderer
import javax.swing.JList; // Importação para JList
// GridBagLayout e GridBagConstraints já estão importados de java.awt.*

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
    private JPanel painelConteudo; // Adicionado como campo da classe
    private DefaultTableModel modeloTabelaEspacos; // Adicionado para a tabela de espaços
    private DefaultTableModel modeloTabelaAgendamentos; // Adicionado para a tabela de agendamentos
    private java.util.List<Espaco> listaDeEspacos; // Lista para armazenar os espaços
    private java.util.List<Agendamento> listaDeAgendamentos; // Lista para armazenar os agendamentos

    // Constantes para nomes de arquivos CSV
    private static final String ARQUIVO_ESPACOS_CSV = "demo/espacos.csv";
    private static final String ARQUIVO_AGENDAMENTOS_CSV = "demo/agendamentos.csv";

    // Campos para os controles da aba Agendamentos
    private JComboBox<Espaco> comboBoxEspacosAgendamento;
    private JFormattedTextField campoDataAgendamento;
    private JSpinner spinnerHoraInicioAgendamento;
    private JSpinner spinnerHoraFimAgendamento;

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

        // Inicializar a lista de espaços
        this.listaDeEspacos = new java.util.ArrayList<>(); // Já existia
        this.listaDeAgendamentos = new java.util.ArrayList<>(); // Já existia

        // Carregar dados dos CSVs
        this.listaDeEspacos = GerenciadorCSVDados.carregarEspacosDoCSV(ARQUIVO_ESPACOS_CSV);
        // Para carregar agendamentos, precisamos da lista de espaços já carregada para associar os IDs.
        this.listaDeAgendamentos = GerenciadorCSVDados.carregarAgendamentosDoCSV(ARQUIVO_AGENDAMENTOS_CSV, this.listaDeEspacos);

        // Dados de exemplo (para teste) - Remova ou comente em produção
        // this.listaDeEspacos.add(new Espaco("Sala Alpha", 10, "Projetor, AC"));
        // this.listaDeEspacos.add(new Espaco("Sala Beta", 5, "Quadro branco"));


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

        abaEspacosSidebar.addActionListener(e -> {
            // Verifica se o painel já existe no CardLayout
            boolean painelJaExiste = false;
            for (Component comp : painelConteudo.getComponents()) {
                if (comp.getName() != null && comp.getName().equals("painelEspacos")) {
                    painelJaExiste = true;
                    break;
                }
            }

            if (!painelJaExiste) {
                JPanel novoPainelEspacos = criarPainelEspacos();
                novoPainelEspacos.setName("painelEspacos"); // Define um nome para o painel
                this.painelConteudo.add(novoPainelEspacos, "painelEspacos");
            }
            ((CardLayout) this.painelConteudo.getLayout()).show(this.painelConteudo, "painelEspacos");
        });

        abaAgendasSidebar.addActionListener(e -> {
            boolean painelJaExiste = false;
            for (Component comp : painelConteudo.getComponents()) {
                if (comp.getName() != null && comp.getName().equals("painelAgendas")) {
                    painelJaExiste = true;
                    break;
                }
            }

            if (!painelJaExiste) {
                JPanel novoPainelAgendas = criarPainelAgendas();
                novoPainelAgendas.setName("painelAgendas");
                this.painelConteudo.add(novoPainelAgendas, "painelAgendas");
            }
            ((CardLayout) this.painelConteudo.getLayout()).show(this.painelConteudo, "painelAgendas");
        });

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
            // Aumentar a altura do logo e garantir SCALE_SMOOTH para qualidade
            int logoAltura = 55; // Nova altura aumentada (navbar tem 60px)
            int logoLargura = -1;  // Manter -1 para que a largura seja calculada proporcionalmente à nova altura

            // Usar Image.SCALE_SMOOTH para melhor qualidade de redimensionamento
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
        this.painelConteudo = new JPanel(new CardLayout()); // MUDAR PARA CARDLAYOUT
        this.painelConteudo.setBackground(BRANCO); // Cor de fundo padrão
        this.painelConteudo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding

        // Adicionar um painel inicial/default ao painelConteudo
        JPanel painelDefault = new JPanel(new BorderLayout());
        painelDefault.setBackground(BRANCO);
        JLabel labelDefault = new JLabel("Bem-vindo! Selecione uma opção na barra lateral.", SwingConstants.CENTER);
        labelDefault.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        labelDefault.setForeground(CINZA_ESCURO);
        painelDefault.add(labelDefault, BorderLayout.CENTER);
        this.painelConteudo.add(painelDefault, "painelDefault"); // Adiciona com um nome

        // ((CardLayout) this.painelConteudo.getLayout()).show(this.painelConteudo, "painelDefault"); // Mostra o default
        painelPrincipal.add(this.painelConteudo, BorderLayout.CENTER);


        // Mais componentes serão adicionados aqui nos próximos passos
    }

    private JPanel criarPainelEspacos() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(BRANCO); // Cor de fundo para o painel de espaços
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel tituloEspacos = new JLabel("Gerenciamento de Espaços", SwingConstants.CENTER);
        tituloEspacos.setFont(new Font("Segoe UI", Font.BOLD, 22));
        tituloEspacos.setForeground(PRETO_SUAVE);
        tituloEspacos.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0)); // Margem inferior
        painel.add(tituloEspacos, BorderLayout.NORTH);

        // Painel para agrupar botões de ação e a tabela
        JPanel painelCentralEspacos = new JPanel(new BorderLayout(0, 10)); // Espaçamento vertical de 10px
        painelCentralEspacos.setOpaque(false); // Transparente para herdar cor do painel principal da aba

        // Painel para o botão "Novo Espaço"
        JPanel painelBotoesAcao = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0)); // Alinhado à esquerda
        painelBotoesAcao.setOpaque(false);

        JButton botaoNovoEspaco = new JButton("Novo Espaço");
        botaoNovoEspaco.setFont(new Font("Segoe UI", Font.BOLD, 14));
        botaoNovoEspaco.setForeground(BRANCO);
        botaoNovoEspaco.setBackground(VERDE_PRINCIPAL); // Usando a cor verde principal
        botaoNovoEspaco.setOpaque(true); // Precisa ser opaco para a cor de fundo aparecer
        botaoNovoEspaco.setBorderPainted(false); // Para um look mais moderno
        botaoNovoEspaco.setFocusPainted(false);
        botaoNovoEspaco.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botaoNovoEspaco.setMargin(new Insets(8, 15, 8, 15)); // Padding interno

        // Efeito hover para o botão Novo Espaço
        botaoNovoEspaco.addMouseListener(new java.awt.event.MouseAdapter() {
            Color originalColor = botaoNovoEspaco.getBackground();
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botaoNovoEspaco.setBackground(originalColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                botaoNovoEspaco.setBackground(originalColor);
            }
        });

        botaoNovoEspaco.addActionListener(e -> {
            FormularioEspacoDialog dialogoNovoEspaco = new FormularioEspacoDialog(
                TelaPrincipalUI.this, // Passa a instância da JFrame principal como pai
                "Adicionar Novo Espaço",
                null // Passa null para espacoParaEditar, indicando que é um novo espaço
            );
            dialogoNovoEspaco.setVisible(true);

            Espaco espacoSalvo = dialogoNovoEspaco.getEspacoSalvo();

            if (espacoSalvo != null) {
                boolean jaExiste = false;
                for(Espaco es : listaDeEspacos) {
                    if(es.getId().equals(espacoSalvo.getId())) {
                        jaExiste = true;
                        es.setNome(espacoSalvo.getNome());
                        es.setCapacidade(espacoSalvo.getCapacidade());
                        es.setDescricao(espacoSalvo.getDescricao());
                        break;
                    }
                }

                if (!jaExiste) {
                    this.listaDeEspacos.add(espacoSalvo);
                }

                atualizarTabelaEspacos();
                System.out.println("Espaço salvo/atualizado: " + espacoSalvo.getNome());
                GerenciadorCSVDados.salvarEspacosNoCSV(ARQUIVO_ESPACOS_CSV, this.listaDeEspacos);
                atualizarComboBoxEspacosAgendamento();
            } else {
                 System.out.println("Criação de novo espaço cancelada ou diálogo fechado sem salvar.");
            }
        });

        painelBotoesAcao.add(botaoNovoEspaco);
        painelCentralEspacos.add(painelBotoesAcao, BorderLayout.NORTH);

        String[] colunasTabela = {"Nome", "Capacidade", "Descrição", "Ações"};
        this.modeloTabelaEspacos = new DefaultTableModel(colunasTabela, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == (colunasTabela.length - 1);
            }
        };
        JTable tabelaEspacos = new JTable(this.modeloTabelaEspacos);

        tabelaEspacos.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabelaEspacos.setRowHeight(36);
        tabelaEspacos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        tabelaEspacos.getTableHeader().setBackground(CINZA_CLARO);
        tabelaEspacos.getTableHeader().setForeground(PRETO_SUAVE);
        tabelaEspacos.setFillsViewportHeight(true);
        tabelaEspacos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        int indiceColunaAcoes = this.modeloTabelaEspacos.getColumnCount() - 1;
        if (indiceColunaAcoes >= 0 && this.modeloTabelaEspacos.getColumnName(indiceColunaAcoes).equals("Ações")) {
            AcoesTabelaCellRendererEditor rendererEditor = new AcoesTabelaCellRendererEditor(tabelaEspacos);
            tabelaEspacos.getColumnModel().getColumn(indiceColunaAcoes).setCellRenderer(rendererEditor);
            tabelaEspacos.getColumnModel().getColumn(indiceColunaAcoes).setCellEditor(rendererEditor);

            tabelaEspacos.getColumnModel().getColumn(indiceColunaAcoes).setPreferredWidth(85);
            tabelaEspacos.getColumnModel().getColumn(indiceColunaAcoes).setMinWidth(80);
            tabelaEspacos.getColumnModel().getColumn(indiceColunaAcoes).setMaxWidth(100);

            rendererEditor.addActionListenerParaEditar(e -> {
                int linhaSelecionadaVisual = tabelaEspacos.getSelectedRow();
                if (linhaSelecionadaVisual != -1) {
                    int linhaModelo = tabelaEspacos.convertRowIndexToModel(linhaSelecionadaVisual);
                    editarEspaco(linhaModelo);
                } else {
                    int linhaEditando = tabelaEspacos.getEditingRow();
                    if (linhaEditando != -1) {
                         int linhaModelo = tabelaEspacos.convertRowIndexToModel(linhaEditando);
                         editarEspaco(linhaModelo);
                    } else {
                        JOptionPane.showMessageDialog(TelaPrincipalUI.this,
                                                    "Por favor, selecione um espaço para editar.",
                                                    "Aviso", JOptionPane.WARNING_MESSAGE);
                    }
                }
            });

            rendererEditor.addActionListenerParaExcluir(e -> {
                int linhaSelecionadaVisual = tabelaEspacos.getSelectedRow();
                if (linhaSelecionadaVisual != -1) {
                    int linhaModelo = tabelaEspacos.convertRowIndexToModel(linhaSelecionadaVisual);
                    excluirEspaco(linhaModelo);
                } else {
                    int linhaEditando = tabelaEspacos.getEditingRow();
                     if (linhaEditando != -1) {
                         int linhaModelo = tabelaEspacos.convertRowIndexToModel(linhaEditando);
                         excluirEspaco(linhaModelo);
                    } else {
                        JOptionPane.showMessageDialog(TelaPrincipalUI.this,
                                                    "Por favor, selecione um espaço para excluir.",
                                                    "Aviso", JOptionPane.WARNING_MESSAGE);
                    }
                }
            });
        }

        JScrollPane scrollPaneTabela = new JScrollPane(tabelaEspacos);
        painelCentralEspacos.add(scrollPaneTabela, BorderLayout.CENTER);
        painel.add(painelCentralEspacos, BorderLayout.CENTER);
        atualizarTabelaEspacos();
        return painel;
    }

    private java.util.Date parseData(String dataStr) {
        try {
            return new java.text.SimpleDateFormat("dd/MM/yyyy").parse(dataStr);
        } catch (java.text.ParseException e) {
            System.err.println("Erro ao parsear data: " + dataStr + " - " + e.getMessage());
            return null;
        }
    }

    private boolean isMesmaData(java.util.Date data1, java.util.Date data2) {
        if (data1 == null || data2 == null) {
            return false;
        }
        java.util.Calendar cal1 = java.util.Calendar.getInstance();
        cal1.setTime(data1);
        java.util.Calendar cal2 = java.util.Calendar.getInstance();
        cal2.setTime(data2);
        return cal1.get(java.util.Calendar.YEAR) == cal2.get(java.util.Calendar.YEAR) &&
               cal1.get(java.util.Calendar.MONTH) == cal2.get(java.util.Calendar.MONTH) &&
               cal1.get(java.util.Calendar.DAY_OF_MONTH) == cal2.get(java.util.Calendar.DAY_OF_MONTH);
    }

    private void atualizarTabelaEspacos() {
        if (this.modeloTabelaEspacos == null) {
            System.err.println("modeloTabelaEspacos ainda não foi inicializado!");
            return;
        }
        this.modeloTabelaEspacos.setRowCount(0);
        for (Espaco espaco : this.listaDeEspacos) {
            this.modeloTabelaEspacos.addRow(new Object[]{
                espaco.getNome(),
                espaco.getCapacidade(),
                espaco.getDescricao(),
                null
            });
        }
    }

    private void editarEspaco(int linhaModelo) {
        if (linhaModelo >= 0 && linhaModelo < listaDeEspacos.size()) {
            Espaco espacoParaEditar = listaDeEspacos.get(linhaModelo);

            FormularioEspacoDialog dialogoEditarEspaco = new FormularioEspacoDialog(
                TelaPrincipalUI.this,
                "Editar Espaço: " + espacoParaEditar.getNome(),
                espacoParaEditar
            );
            dialogoEditarEspaco.setVisible(true);

            Espaco espacoEditado = dialogoEditarEspaco.getEspacoSalvo();
            if (dialogoEditarEspaco.isSalvoComSucesso() && espacoEditado != null) {
                atualizarTabelaEspacos();
                GerenciadorCSVDados.salvarEspacosNoCSV(ARQUIVO_ESPACOS_CSV, this.listaDeEspacos);
                atualizarComboBoxEspacosAgendamento();
                System.out.println("Espaço editado: " + espacoEditado.getNome());
            } else {
                System.out.println("Edição de espaço cancelada.");
            }
        } else {
            System.err.println("Índice de linha inválido para edição: " + linhaModelo);
        }
    }

    private void excluirEspaco(int linhaModelo) {
        if (linhaModelo >= 0 && linhaModelo < listaDeEspacos.size()) {
            Espaco espacoParaExcluir = listaDeEspacos.get(linhaModelo);
            int confirmacao = JOptionPane.showConfirmDialog(
                TelaPrincipalUI.this,
                "Tem certeza que deseja excluir o espaço: " + espacoParaExcluir.getNome() + "?",
                "Confirmar Exclusão",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );

            if (confirmacao == JOptionPane.YES_OPTION) {
                listaDeEspacos.remove(linhaModelo);
                atualizarTabelaEspacos();
                GerenciadorCSVDados.salvarEspacosNoCSV(ARQUIVO_ESPACOS_CSV, this.listaDeEspacos);
                atualizarComboBoxEspacosAgendamento();
                System.out.println("Espaço excluído: " + espacoParaExcluir.getNome());
            } else {
                System.out.println("Exclusão de espaço cancelada.");
            }
        } else {
            System.err.println("Índice de linha inválido para exclusão: " + linhaModelo);
        }
    }

    private JPanel criarPainelAgendas() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(BRANCO);
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel tituloAgendas = new JLabel("Gerenciamento de Agendas", SwingConstants.CENTER);
        tituloAgendas.setFont(new Font("Segoe UI", Font.BOLD, 22));
        tituloAgendas.setForeground(PRETO_SUAVE);

        JPanel painelControlesAgendamento = new JPanel(new GridBagLayout());
        painelControlesAgendamento.setBackground(BRANCO);
        painelControlesAgendamento.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Novo Agendamento",
            javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
            javax.swing.border.TitledBorder.DEFAULT_POSITION,
            new Font("Segoe UI", Font.BOLD, 14), PRETO_SUAVE
        ));
        GridBagConstraints gbcControles = new GridBagConstraints();
        gbcControles.insets = new Insets(5, 8, 5, 8);
        gbcControles.anchor = GridBagConstraints.WEST;
        gbcControles.fill = GridBagConstraints.HORIZONTAL;

        gbcControles.gridx = 0;
        gbcControles.gridy = 0;
        JLabel labelSeletorEspaco = new JLabel("Espaço:");
        labelSeletorEspaco.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        painelControlesAgendamento.add(labelSeletorEspaco, gbcControles);

        gbcControles.gridx = 1;
        gbcControles.gridwidth = 3;
        this.comboBoxEspacosAgendamento = new JComboBox<>();
        if (this.listaDeEspacos != null) {
            for (Espaco esp : this.listaDeEspacos) {
                this.comboBoxEspacosAgendamento.addItem(esp);
            }
        }
        this.comboBoxEspacosAgendamento.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Espaco) {
                    setText(((Espaco) value).getNome());
                }
                return this;
            }
        });
        this.comboBoxEspacosAgendamento.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        this.comboBoxEspacosAgendamento.addActionListener(e -> {
            atualizarTabelaAgendamentos();
        });
        painelControlesAgendamento.add(this.comboBoxEspacosAgendamento, gbcControles);
        gbcControles.gridwidth = 1;

        gbcControles.gridx = 0;
        gbcControles.gridy = 1;
        JLabel labelData = new JLabel("Data (dd/mm/aaaa):");
        labelData.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        painelControlesAgendamento.add(labelData, gbcControles);

        gbcControles.gridx = 1;
        try {
            javax.swing.text.MaskFormatter mascaraData = new javax.swing.text.MaskFormatter("##/##/####");
            mascaraData.setPlaceholderCharacter('_');
            this.campoDataAgendamento = new JFormattedTextField(mascaraData);
            this.campoDataAgendamento.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            this.campoDataAgendamento.setColumns(8);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
            this.campoDataAgendamento = new JFormattedTextField();
        }
        painelControlesAgendamento.add(this.campoDataAgendamento, gbcControles);

        gbcControles.gridx = 0;
        gbcControles.gridy = 2;
        JLabel labelHoraInicio = new JLabel("Hora Início (HH:mm):");
        labelHoraInicio.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        painelControlesAgendamento.add(labelHoraInicio, gbcControles);

        gbcControles.gridx = 1;
        this.spinnerHoraInicioAgendamento = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editorHoraInicio = new JSpinner.DateEditor(this.spinnerHoraInicioAgendamento, "HH:mm");
        this.spinnerHoraInicioAgendamento.setEditor(editorHoraInicio);
        this.spinnerHoraInicioAgendamento.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        java.util.Calendar calInicio = java.util.Calendar.getInstance();
        calInicio.set(java.util.Calendar.HOUR_OF_DAY, 8);
        calInicio.set(java.util.Calendar.MINUTE, 0);
        this.spinnerHoraInicioAgendamento.setValue(calInicio.getTime());
        painelControlesAgendamento.add(this.spinnerHoraInicioAgendamento, gbcControles);

        gbcControles.gridx = 2;
        gbcControles.anchor = GridBagConstraints.EAST;
        JLabel labelHoraFim = new JLabel("Hora Fim (HH:mm):");
        labelHoraFim.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        painelControlesAgendamento.add(labelHoraFim, gbcControles);
        gbcControles.anchor = GridBagConstraints.WEST;

        gbcControles.gridx = 3;
        this.spinnerHoraFimAgendamento = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editorHoraFim = new JSpinner.DateEditor(this.spinnerHoraFimAgendamento, "HH:mm");
        this.spinnerHoraFimAgendamento.setEditor(editorHoraFim);
        this.spinnerHoraFimAgendamento.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        java.util.Calendar calFim = java.util.Calendar.getInstance();
        calFim.set(java.util.Calendar.HOUR_OF_DAY, 9);
        calFim.set(java.util.Calendar.MINUTE, 0);
        this.spinnerHoraFimAgendamento.setValue(calFim.getTime());
        painelControlesAgendamento.add(this.spinnerHoraFimAgendamento, gbcControles);

        gbcControles.gridx = 0;
        gbcControles.gridy = 3;
        gbcControles.gridwidth = 4;
        gbcControles.anchor = GridBagConstraints.CENTER;
        gbcControles.fill = GridBagConstraints.NONE;
        JButton botaoAdicionarHorario = new JButton("Adicionar Horário");
        botaoAdicionarHorario.setFont(new Font("Segoe UI", Font.BOLD, 14));
        botaoAdicionarHorario.setForeground(BRANCO);
        botaoAdicionarHorario.setBackground(VERDE_PRINCIPAL);
        botaoAdicionarHorario.setOpaque(true);
        botaoAdicionarHorario.setBorderPainted(false);
        botaoAdicionarHorario.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botaoAdicionarHorario.setMargin(new Insets(8, 15, 8, 15));
        botaoAdicionarHorario.addActionListener(e -> {
            Espaco espacoSelecionado = (Espaco) this.comboBoxEspacosAgendamento.getSelectedItem();
            String dataStr = this.campoDataAgendamento.getText();
            Date horaInicioSelecionada = (Date) this.spinnerHoraInicioAgendamento.getValue();
            Date horaFimSelecionada = (Date) this.spinnerHoraFimAgendamento.getValue();

            if (espacoSelecionado == null) {
                JOptionPane.showMessageDialog(TelaPrincipalUI.this, "Por favor, selecione um espaço.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (dataStr.trim().replace("_", "").isEmpty() || dataStr.trim().length() != 10) {
                JOptionPane.showMessageDialog(TelaPrincipalUI.this, "Por favor, insira uma data válida (dd/mm/aaaa).", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Date dataSelecionada = parseData(dataStr);
            if (dataSelecionada == null) {
                JOptionPane.showMessageDialog(TelaPrincipalUI.this, "Formato de data inválido. Use dd/mm/aaaa.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }

            java.util.Calendar calendarioDataBase = java.util.Calendar.getInstance();
            calendarioDataBase.setTime(dataSelecionada);

            java.util.Calendar calendarioAgendInicio = java.util.Calendar.getInstance();
            calendarioAgendInicio.setTime(horaInicioSelecionada);
            calendarioAgendInicio.set(java.util.Calendar.YEAR, calendarioDataBase.get(java.util.Calendar.YEAR));
            calendarioAgendInicio.set(java.util.Calendar.MONTH, calendarioDataBase.get(java.util.Calendar.MONTH));
            calendarioAgendInicio.set(java.util.Calendar.DAY_OF_MONTH, calendarioDataBase.get(java.util.Calendar.DAY_OF_MONTH));
            Date horaInicioFinal = calendarioAgendInicio.getTime();

            java.util.Calendar calendarioAgendFim = java.util.Calendar.getInstance();
            calendarioAgendFim.setTime(horaFimSelecionada);
            calendarioAgendFim.set(java.util.Calendar.YEAR, calendarioDataBase.get(java.util.Calendar.YEAR));
            calendarioAgendFim.set(java.util.Calendar.MONTH, calendarioDataBase.get(java.util.Calendar.MONTH));
            calendarioAgendFim.set(java.util.Calendar.DAY_OF_MONTH, calendarioDataBase.get(java.util.Calendar.DAY_OF_MONTH));
            Date horaFimFinal = calendarioAgendFim.getTime();

            if (horaFimFinal.before(horaInicioFinal) || horaFimFinal.equals(horaInicioFinal)) {
                JOptionPane.showMessageDialog(TelaPrincipalUI.this, "A hora de fim deve ser posterior à hora de início.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }

            for (Agendamento ag : this.listaDeAgendamentos) {
                if (ag.getEspaco().getId().equals(espacoSelecionado.getId()) &&
                    isMesmaData(ag.getData(), dataSelecionada)) {
                    if (horaInicioFinal.before(ag.getHoraFim()) && horaFimFinal.after(ag.getHoraInicio())) {
                        JOptionPane.showMessageDialog(TelaPrincipalUI.this,
                            "Conflito de horário! Já existe um agendamento para este espaço neste período.",
                            "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
            }

            Agendamento novoAgendamento = new Agendamento(espacoSelecionado, dataSelecionada, horaInicioFinal, horaFimFinal);
            this.listaDeAgendamentos.add(novoAgendamento);
            atualizarTabelaAgendamentos();
            JOptionPane.showMessageDialog(TelaPrincipalUI.this, "Agendamento adicionado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            GerenciadorCSVDados.salvarAgendamentosNoCSV(ARQUIVO_AGENDAMENTOS_CSV, this.listaDeAgendamentos);
        });
        painelControlesAgendamento.add(botaoAdicionarHorario, gbcControles);

        JPanel painelSuperiorAgendas = new JPanel(new BorderLayout());
        painelSuperiorAgendas.setOpaque(false);
        tituloAgendas.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        painelSuperiorAgendas.add(tituloAgendas, BorderLayout.NORTH);
        painelSuperiorAgendas.add(painelControlesAgendamento, BorderLayout.CENTER);

        painel.add(painelSuperiorAgendas, BorderLayout.NORTH);

        String[] colunasTabelaAgendamentos = {"Espaço", "Data", "Hora Início", "Hora Fim", "Ações"};
        this.modeloTabelaAgendamentos = new DefaultTableModel(colunasTabelaAgendamentos, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == (colunasTabelaAgendamentos.length - 1);
            }
        };
        JTable tabelaAgendamentos = new JTable(this.modeloTabelaAgendamentos);

        tabelaAgendamentos.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabelaAgendamentos.setRowHeight(30);
        tabelaAgendamentos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        tabelaAgendamentos.getTableHeader().setBackground(CINZA_CLARO);
        tabelaAgendamentos.getTableHeader().setForeground(PRETO_SUAVE);
        tabelaAgendamentos.setFillsViewportHeight(true);
        tabelaAgendamentos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        int indiceColunaAcoesAgend = this.modeloTabelaAgendamentos.getColumnCount() - 1;
        if (indiceColunaAcoesAgend >= 0 && this.modeloTabelaAgendamentos.getColumnName(indiceColunaAcoesAgend).equals("Ações")) {

            AcoesTabelaCellRendererEditor rendererEditorAgend = new AcoesTabelaCellRendererEditor(tabelaAgendamentos);
            tabelaAgendamentos.getColumnModel().getColumn(indiceColunaAcoesAgend).setCellRenderer(rendererEditorAgend);
            tabelaAgendamentos.getColumnModel().getColumn(indiceColunaAcoesAgend).setCellEditor(rendererEditorAgend);

            tabelaAgendamentos.getColumnModel().getColumn(indiceColunaAcoesAgend).setPreferredWidth(85);
            tabelaAgendamentos.getColumnModel().getColumn(indiceColunaAcoesAgend).setMinWidth(80);
            tabelaAgendamentos.getColumnModel().getColumn(indiceColunaAcoesAgend).setMaxWidth(100);

            rendererEditorAgend.addActionListenerParaExcluir(e -> {
                int linhaSelecionadaVisual = tabelaAgendamentos.getSelectedRow();
                if (linhaSelecionadaVisual != -1) {
                    int linhaModelo = tabelaAgendamentos.convertRowIndexToModel(linhaSelecionadaVisual);
                    removerAgendamento(linhaModelo);
                } else {
                    int linhaEditando = tabelaAgendamentos.getEditingRow();
                    if (linhaEditando != -1) {
                        int linhaModelo = tabelaAgendamentos.convertRowIndexToModel(linhaEditando);
                        removerAgendamento(linhaModelo);
                    } else {
                        JOptionPane.showMessageDialog(TelaPrincipalUI.this,
                                                    "Por favor, selecione um agendamento para remover.",
                                                    "Aviso", JOptionPane.WARNING_MESSAGE);
                    }
                }
            });

            rendererEditorAgend.addActionListenerParaEditar(e -> {
                 JOptionPane.showMessageDialog(TelaPrincipalUI.this,
                                                    "A edição de agendamentos não está implementada nesta tabela.",
                                                    "Informação", JOptionPane.INFORMATION_MESSAGE);
            });
        }

        JScrollPane scrollPaneTabelaAgendamentos = new JScrollPane(tabelaAgendamentos);
        painel.add(scrollPaneTabelaAgendamentos, BorderLayout.CENTER);

        return painel;
    }

    private void atualizarTabelaAgendamentos() {
        if (this.modeloTabelaAgendamentos == null) {
            System.err.println("modeloTabelaAgendamentos ainda não foi inicializado!");
            return;
        }
        this.modeloTabelaAgendamentos.setRowCount(0);

        Espaco espacoFiltrar = null;
        if (this.comboBoxEspacosAgendamento != null && this.comboBoxEspacosAgendamento.getSelectedItem() instanceof Espaco) {
            espacoFiltrar = (Espaco) this.comboBoxEspacosAgendamento.getSelectedItem();
        }

        for (Agendamento ag : this.listaDeAgendamentos) {
            boolean deveAdicionar = true;
            if (espacoFiltrar != null) {
                if (!ag.getEspaco().getId().equals(espacoFiltrar.getId())) {
                    deveAdicionar = false;
                }
            }
            if (deveAdicionar) {
                this.modeloTabelaAgendamentos.addRow(new Object[]{
                    ag.getEspaco().getNome(),
                    ag.getDataFormatada(),
                    ag.getHoraInicioFormatada(),
                    ag.getHoraFimFormatada(),
                    null
                });
            }
        }
    }

    private void removerAgendamento(int linhaModelo) {
        if (linhaModelo >= 0 && linhaModelo < listaDeAgendamentos.size()) {
            Agendamento agendamentoParaRemover = listaDeAgendamentos.get(linhaModelo);
            int confirmacao = JOptionPane.showConfirmDialog(
                TelaPrincipalUI.this,
                "Tem certeza que deseja remover o agendamento para o espaço: " +
                agendamentoParaRemover.getEspaco().getNome() + " em " +
                agendamentoParaRemover.getDataFormatada() + " de " +
                agendamentoParaRemover.getHoraInicioFormatada() + " às " +
                agendamentoParaRemover.getHoraFimFormatada() + "?",
                "Confirmar Remoção de Horário",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );

            if (confirmacao == JOptionPane.YES_OPTION) {
                listaDeAgendamentos.remove(linhaModelo);
                atualizarTabelaAgendamentos();
                GerenciadorCSVDados.salvarAgendamentosNoCSV(ARQUIVO_AGENDAMENTOS_CSV, this.listaDeAgendamentos);
                System.out.println("Agendamento removido.");
            } else {
                System.out.println("Remoção de agendamento cancelada.");
            }
        } else {
            System.err.println("Índice de linha inválido para remoção de agendamento: " + linhaModelo);
        }
    }

    private void configurarBotaoSidebar(JButton botao, ImageIcon icone) {
        botao.setFont(new Font("Segoe UI", Font.BOLD, 16));
        botao.setForeground(BRANCO);
        botao.setBackground(VERDE_PRINCIPAL);
        botao.setOpaque(false);
        botao.setFocusPainted(false);
        botao.setBorderPainted(false);
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botao.setHorizontalAlignment(SwingConstants.LEFT);
        botao.setIconTextGap(15);
        botao.setMargin(new Insets(10, 15, 10, 15));

        if (icone != null) {
            botao.setIcon(new ImageIcon(icone.getImage().getScaledInstance(22, 22, Image.SCALE_SMOOTH)));
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

    private void atualizarComboBoxEspacosAgendamento() {
        if (this.comboBoxEspacosAgendamento == null) {
            // ComboBox ainda não foi inicializado (painel de Agendas talvez não aberto)
            return;
        }

        // Salvar o item selecionado anteriormente, se houver e for válido
        Object itemSelecionadoAnteriormente = this.comboBoxEspacosAgendamento.getSelectedItem();

        // Limpar itens existentes
        this.comboBoxEspacosAgendamento.removeAllItems();

        // Repopular com a lista atualizada de espaços
        if (this.listaDeEspacos != null) {
            for (Espaco esp : this.listaDeEspacos) {
                this.comboBoxEspacosAgendamento.addItem(esp);
            }
        }

        // Tentar restaurar a seleção anterior, se ainda existir na lista
        if (itemSelecionadoAnteriormente instanceof Espaco) {
            Espaco espacoAnterior = (Espaco) itemSelecionadoAnteriormente;
            for (int i = 0; i < this.comboBoxEspacosAgendamento.getItemCount(); i++) {
                Espaco itemAtual = this.comboBoxEspacosAgendamento.getItemAt(i);
                if (itemAtual != null && itemAtual.getId().equals(espacoAnterior.getId())) {
                    this.comboBoxEspacosAgendamento.setSelectedIndex(i);
                    break;
                }
            }
        } else if (this.comboBoxEspacosAgendamento.getItemCount() > 0) {
            // Se não havia seleção válida ou o item anterior sumiu, seleciona o primeiro (se houver)
            this.comboBoxEspacosAgendamento.setSelectedIndex(0);
        }

        // Atualizar a tabela de agendamentos, pois a seleção do combobox pode ter mudado
        // ou os espaços disponíveis para filtro mudaram.
        atualizarTabelaAgendamentos();
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

[end of demo/src/main/java/espaco_capita/TelaPrincipalUI.java]
