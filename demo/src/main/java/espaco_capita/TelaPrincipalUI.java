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
        this.listaDeEspacos = new java.util.ArrayList<>();
        this.listaDeAgendamentos = new java.util.ArrayList<>(); // Inicializar lista de agendamentos

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

            // Após o diálogo ser fechado (setVisible(true) é bloqueante para dialogs modais):
            Espaco espacoSalvo = dialogoNovoEspaco.getEspacoSalvo();

            if (espacoSalvo != null) {
                // Verificar se o nome não está vazio (o dialog já faz uma validação, mas podemos reforçar)
                // e se o espaco foi realmente modificado ou é novo (o getEspacoSalvo() atual não distingue cancelamento)
                // Para uma lógica mais robusta, FormularioEspacoDialog precisaria de um método
                // tipo 'foiSalvoComSucesso()' para distinguir entre salvar e cancelar/fechar.
                // O getEspacoSalvo() atual retorna o objeto mesmo se o usuário fechar o dialog sem salvar,
                // se ele foi preenchido anteriormente (no caso de edição, por exemplo).
                // A lógica exata de quando adicionar/atualizar pode ser refinada.

                if (espacoSalvo != null) { // espacoSalvo agora só retorna não-null se foi salvo com sucesso
                    // Checar se já existe um espaço com o mesmo ID (para caso de edição futura)
                    boolean jaExiste = false;
                    for(Espaco es : listaDeEspacos) {
                        if(es.getId().equals(espacoSalvo.getId())) {
                            jaExiste = true;
                            // Atualiza o existente - essa lógica será mais relevante no "Editar"
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
                } else {
                    System.out.println("Criação de novo espaço cancelada ou diálogo fechado sem salvar.");
                }
            } else { // Se o diálogo não foi salvo com sucesso (isSalvoComSucesso() == false)
                 System.out.println("Criação de novo espaço cancelada ou diálogo fechado sem salvar.");
            }
        });

        painelBotoesAcao.add(botaoNovoEspaco);
        painelCentralEspacos.add(painelBotoesAcao, BorderLayout.NORTH);

        // Tabela para listar espaços
        String[] colunasTabela = {"Nome", "Capacidade", "Descrição", "Ações"}; // Coluna "Ações" adicionada para botões
        this.modeloTabelaEspacos = new DefaultTableModel(colunasTabela, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Apenas a coluna "Ações" (onde estão os botões) deve ser "editável"
                // para ativar o TableCellEditor.
                return column == (colunasTabela.length - 1); // Se "Ações" for a última coluna
            }
            // Adicionar override para getClass para a coluna de ações, se necessário (para o renderer funcionar corretamente)
            // @Override
            // public Class<?> getColumnClass(int columnIndex) {
            //     if (columnIndex == (colunasTabela.length - 1)) {
            //         return AcoesTabelaPanel.class; // Ou Object.class se o painel for genérico
            //     }
            //     return super.getColumnClass(columnIndex);
            // }
        };
        JTable tabelaEspacos = new JTable(this.modeloTabelaEspacos);

        // Estilização da tabela (opcional, mas recomendado)
        tabelaEspacos.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabelaEspacos.setRowHeight(36); // Nova altura da linha (ex: 30 para o botão + 6 de padding vertical)
        tabelaEspacos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        tabelaEspacos.getTableHeader().setBackground(CINZA_CLARO); // Cor do cabeçalho
        tabelaEspacos.getTableHeader().setForeground(PRETO_SUAVE);
        tabelaEspacos.setFillsViewportHeight(true); // Para que a tabela preencha a altura do JScrollPane
        tabelaEspacos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Apenas uma linha pode ser selecionada

        // Configurar a coluna "Ações" para usar o renderer e editor customizados
        int indiceColunaAcoes = this.modeloTabelaEspacos.getColumnCount() - 1; // Assume que "Ações" é a última
        if (indiceColunaAcoes >= 0 && this.modeloTabelaEspacos.getColumnName(indiceColunaAcoes).equals("Ações")) {
            AcoesTabelaCellRendererEditor rendererEditor = new AcoesTabelaCellRendererEditor(tabelaEspacos);
            tabelaEspacos.getColumnModel().getColumn(indiceColunaAcoes).setCellRenderer(rendererEditor);
            tabelaEspacos.getColumnModel().getColumn(indiceColunaAcoes).setCellEditor(rendererEditor);

            // Definir largura preferida para a coluna de ações
            tabelaEspacos.getColumnModel().getColumn(indiceColunaAcoes).setPreferredWidth(85); // Nova largura preferida
            tabelaEspacos.getColumnModel().getColumn(indiceColunaAcoes).setMinWidth(80);    // Nova largura mínima
            tabelaEspacos.getColumnModel().getColumn(indiceColunaAcoes).setMaxWidth(100);   // Nova largura máxima


            // Adicionar ActionListeners específicos para os botões do editor
            rendererEditor.addActionListenerParaEditar(e -> {
                int linhaSelecionadaVisual = tabelaEspacos.getSelectedRow();
                if (linhaSelecionadaVisual != -1) {
                    int linhaModelo = tabelaEspacos.convertRowIndexToModel(linhaSelecionadaVisual);
                    editarEspaco(linhaModelo);
                } else {
                    // Se nenhuma linha estiver selecionada, mas o botão foi clicado (pode acontecer se a edição for iniciada de outra forma)
                    // Tenta obter a linha que está sendo editada.
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

        // Adicionar dados de exemplo (remover ou comentar depois)
        // modeloTabelaEspacos.addRow(new Object[]{"Sala de Reunião A", 10, "Equipada com projetor e quadro branco", "Editar/Excluir"});
        // modeloTabelaEspacos.addRow(new Object[]{"Sala de Coworking B", 25, "Ambiente compartilhado com mesas individuais", "Editar/Excluir"});
        // modeloTabelaEspacos.addRow(new Object[]{"Auditório", 100, "Grande espaço para eventos e palestras", "Editar/Excluir"});

        JScrollPane scrollPaneTabela = new JScrollPane(tabelaEspacos);
        painelCentralEspacos.add(scrollPaneTabela, BorderLayout.CENTER); // Adiciona a tabela ao painelCentralEspacos

        // Adicionar o painelCentralEspacos ao painel principal da aba Espaços
        painel.add(painelCentralEspacos, BorderLayout.CENTER);

        // Atualizar a tabela ao criar o painel pela primeira vez (se houver dados de exemplo)
        // Isso garante que os dados de exemplo sejam exibidos.
        // Se não houver dados de exemplo, a tabela estará vazia, o que é correto.
        atualizarTabelaEspacos();

        return painel;
    }

    private java.util.Date parseData(String dataStr) {
        try {
            // Usar o formato correto que o JFormattedTextField está configurado para produzir
            // ou o formato esperado do usuário.
            return new java.text.SimpleDateFormat("dd/MM/yyyy").parse(dataStr);
        } catch (java.text.ParseException e) {
            System.err.println("Erro ao parsear data: " + dataStr + " - " + e.getMessage());
            // Considerar notificar o usuário via JOptionPane aqui também ou retornar null e tratar no chamador.
            return null;
        }
    }

    private boolean isMesmaData(java.util.Date data1, java.util.Date data2) {
        if (data1 == null || data2 == null) {
            return false;
        }
        // Usar Calendar para comparar apenas ano, mês e dia.
        java.util.Calendar cal1 = java.util.Calendar.getInstance();
        cal1.setTime(data1);
        java.util.Calendar cal2 = java.util.Calendar.getInstance();
        cal2.setTime(data2);
        return cal1.get(java.util.Calendar.YEAR) == cal2.get(java.util.Calendar.YEAR) &&
               cal1.get(java.util.Calendar.MONTH) == cal2.get(java.util.Calendar.MONTH) &&
               cal1.get(java.util.Calendar.DAY_OF_MONTH) == cal2.get(java.util.Calendar.DAY_OF_MONTH);
    }

    private void atualizarTabelaEspacos() {
        // Limpar tabela existente
        if (this.modeloTabelaEspacos == null) {
            // Isso não deveria acontecer se criarPainelEspacos() foi chamado e inicializou o modelo
            System.err.println("modeloTabelaEspacos ainda não foi inicializado!");
            return;
        }
        this.modeloTabelaEspacos.setRowCount(0);

        // Preencher com dados da listaDeEspacos
        for (Espaco espaco : this.listaDeEspacos) {
            // A coluna "Ações" terá botões, por enquanto pode ser um placeholder ou null
            this.modeloTabelaEspacos.addRow(new Object[]{ // Nova Linha
                espaco.getNome(),
                espaco.getCapacidade(),
                espaco.getDescricao(),
                null // Ou um objeto placeholder se o editor/renderer precisar, mas null geralmente funciona.
            });
        }
    }

    private void editarEspaco(int linhaModelo) {
        if (linhaModelo >= 0 && linhaModelo < listaDeEspacos.size()) {
            Espaco espacoParaEditar = listaDeEspacos.get(linhaModelo);

            FormularioEspacoDialog dialogoEditarEspaco = new FormularioEspacoDialog(
                TelaPrincipalUI.this,
                "Editar Espaço: " + espacoParaEditar.getNome(),
                espacoParaEditar // Passa o objeto Espaco existente
            );
            dialogoEditarEspaco.setVisible(true);

            Espaco espacoEditado = dialogoEditarEspaco.getEspacoSalvo();
            if (dialogoEditarEspaco.isSalvoComSucesso() && espacoEditado != null) {
                // O objeto espacoParaEditar já foi modificado dentro do FormularioEspacoDialog
                // se o usuário salvou. A listaDeEspacos já contém a referência modificada.
                atualizarTabelaEspacos();
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
        // tituloAgendas.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0)); // Movido para painelSuperiorAgendas
        // painel.add(tituloAgendas, BorderLayout.NORTH); // Movido para painelSuperiorAgendas

        JPanel painelControlesAgendamento = new JPanel(new GridBagLayout());
        painelControlesAgendamento.setBackground(BRANCO);
        painelControlesAgendamento.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Novo Agendamento",
            javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
            javax.swing.border.TitledBorder.DEFAULT_POSITION,
            new Font("Segoe UI", Font.BOLD, 14), PRETO_SUAVE
        ));
        GridBagConstraints gbcControles = new GridBagConstraints();
        gbcControles.insets = new Insets(5, 8, 5, 8); // Padding
        gbcControles.anchor = GridBagConstraints.WEST;
        gbcControles.fill = GridBagConstraints.HORIZONTAL;

        // Seletor de Espaço (JComboBox)
        gbcControles.gridx = 0;
        gbcControles.gridy = 0;
        JLabel labelSeletorEspaco = new JLabel("Espaço:");
        labelSeletorEspaco.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        painelControlesAgendamento.add(labelSeletorEspaco, gbcControles);

        gbcControles.gridx = 1;
        gbcControles.gridwidth = 3; // Ocupar mais colunas
        this.comboBoxEspacosAgendamento = new JComboBox<>(); // Atribuir ao campo da classe
        // Popular o ComboBox com os espaços da listaDeEspacos
        if (this.listaDeEspacos != null) {
            for (Espaco esp : this.listaDeEspacos) {
                this.comboBoxEspacosAgendamento.addItem(esp);
            }
        }
        // Customizar o renderer do ComboBox para mostrar espaco.getNome()
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
        // Adicionar ActionListener para atualizar a tabela quando o espaço mudar
        this.comboBoxEspacosAgendamento.addActionListener(e -> {
            atualizarTabelaAgendamentos();
        });
        painelControlesAgendamento.add(this.comboBoxEspacosAgendamento, gbcControles);
        gbcControles.gridwidth = 1; // Reset gridwidth

        // Campo de Data (JFormattedTextField)
        gbcControles.gridx = 0;
        gbcControles.gridy = 1;
        JLabel labelData = new JLabel("Data (dd/mm/aaaa):");
        labelData.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        painelControlesAgendamento.add(labelData, gbcControles);

        gbcControles.gridx = 1;
        // JFormattedTextField campoData = null; // Linha antiga
        try {
            javax.swing.text.MaskFormatter mascaraData = new javax.swing.text.MaskFormatter("##/##/####");
            mascaraData.setPlaceholderCharacter('_');
            this.campoDataAgendamento = new JFormattedTextField(mascaraData); // Atribuir ao campo da classe
            this.campoDataAgendamento.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            this.campoDataAgendamento.setColumns(8); // Largura
        } catch (java.text.ParseException e) {
            e.printStackTrace();
            this.campoDataAgendamento = new JFormattedTextField(); // Fallback
        }
        painelControlesAgendamento.add(this.campoDataAgendamento, gbcControles);

        // Campo Hora Início (JSpinner)
        gbcControles.gridx = 0;
        gbcControles.gridy = 2;
        JLabel labelHoraInicio = new JLabel("Hora Início (HH:mm):");
        labelHoraInicio.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        painelControlesAgendamento.add(labelHoraInicio, gbcControles);

        gbcControles.gridx = 1;
        this.spinnerHoraInicioAgendamento = new JSpinner(new SpinnerDateModel()); // Atribuir ao campo da classe
        JSpinner.DateEditor editorHoraInicio = new JSpinner.DateEditor(this.spinnerHoraInicioAgendamento, "HH:mm");
        this.spinnerHoraInicioAgendamento.setEditor(editorHoraInicio);
        this.spinnerHoraInicioAgendamento.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        // Definir um valor inicial (ex: 08:00)
        java.util.Calendar calInicio = java.util.Calendar.getInstance();
        calInicio.set(java.util.Calendar.HOUR_OF_DAY, 8);
        calInicio.set(java.util.Calendar.MINUTE, 0);
        this.spinnerHoraInicioAgendamento.setValue(calInicio.getTime());
        painelControlesAgendamento.add(this.spinnerHoraInicioAgendamento, gbcControles);

        // Campo Hora Fim (JSpinner)
        gbcControles.gridx = 2; // Na mesma linha que Hora Início, mas coluna diferente
        gbcControles.anchor = GridBagConstraints.EAST; // Alinhar label à direita
        JLabel labelHoraFim = new JLabel("Hora Fim (HH:mm):");
        labelHoraFim.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        painelControlesAgendamento.add(labelHoraFim, gbcControles);
        gbcControles.anchor = GridBagConstraints.WEST; // Reset anchor

        gbcControles.gridx = 3;
        this.spinnerHoraFimAgendamento = new JSpinner(new SpinnerDateModel()); // Atribuir ao campo da classe
        JSpinner.DateEditor editorHoraFim = new JSpinner.DateEditor(this.spinnerHoraFimAgendamento, "HH:mm");
        this.spinnerHoraFimAgendamento.setEditor(editorHoraFim);
        this.spinnerHoraFimAgendamento.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        // Definir um valor inicial (ex: 09:00)
        java.util.Calendar calFim = java.util.Calendar.getInstance();
        calFim.set(java.util.Calendar.HOUR_OF_DAY, 9);
        calFim.set(java.util.Calendar.MINUTE, 0);
        this.spinnerHoraFimAgendamento.setValue(calFim.getTime());
        painelControlesAgendamento.add(this.spinnerHoraFimAgendamento, gbcControles);

        // Botão Adicionar Horário
        gbcControles.gridx = 0;
        gbcControles.gridy = 3;
        gbcControles.gridwidth = 4; // Ocupar todas as colunas
        gbcControles.anchor = GridBagConstraints.CENTER; // Centralizar botão
        gbcControles.fill = GridBagConstraints.NONE; // Não expandir o botão
        JButton botaoAdicionarHorario = new JButton("Adicionar Horário");
        botaoAdicionarHorario.setFont(new Font("Segoe UI", Font.BOLD, 14));
        botaoAdicionarHorario.setForeground(BRANCO);
        botaoAdicionarHorario.setBackground(VERDE_PRINCIPAL);
        botaoAdicionarHorario.setOpaque(true);
        botaoAdicionarHorario.setBorderPainted(false);
        botaoAdicionarHorario.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botaoAdicionarHorario.setMargin(new Insets(8, 15, 8, 15));
        // Adicionar ActionListener (será implementado em passo futuro)
        botaoAdicionarHorario.addActionListener(e -> {
            // 1. Coletar Dados
            Espaco espacoSelecionado = (Espaco) this.comboBoxEspacosAgendamento.getSelectedItem();
            String dataStr = this.campoDataAgendamento.getText();
            Date horaInicioSelecionada = (Date) this.spinnerHoraInicioAgendamento.getValue();
            Date horaFimSelecionada = (Date) this.spinnerHoraFimAgendamento.getValue();

            // 2. Validação de Campos
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

            // Normalizar horaInicio e horaFim para terem a mesma data (a data selecionada)
            // Isso é importante para comparações e para evitar problemas com o componente JSpinner
            // que pode manter sua própria data interna se não for resetado.
            Calendar cal = Calendar.getInstance();
            cal.setTime(dataSelecionada);

            Calendar calInicio = Calendar.getInstance();
            calInicio.setTime(horaInicioSelecionada);
            calInicio.set(Calendar.YEAR, cal.get(Calendar.YEAR));
            calInicio.set(Calendar.MONTH, cal.get(Calendar.MONTH));
            calInicio.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH));
            Date horaInicioFinal = calInicio.getTime();

            Calendar calFim = Calendar.getInstance();
            calFim.setTime(horaFimSelecionada);
            calFim.set(Calendar.YEAR, cal.get(Calendar.YEAR));
            calFim.set(Calendar.MONTH, cal.get(Calendar.MONTH));
            calFim.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH));
            Date horaFimFinal = calFim.getTime();

            if (horaFimFinal.before(horaInicioFinal) || horaFimFinal.equals(horaInicioFinal)) {
                JOptionPane.showMessageDialog(TelaPrincipalUI.this, "A hora de fim deve ser posterior à hora de início.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 3. Validação de Conflitos
            for (Agendamento ag : this.listaDeAgendamentos) {
                if (ag.getEspaco().getId().equals(espacoSelecionado.getId()) &&
                    isMesmaData(ag.getData(), dataSelecionada)) {
                    // Conflito se:
                    // (NovoInicio < AgExistenteFim) E (NovoFim > AgExistenteInicio)
                    if (horaInicioFinal.before(ag.getHoraFim()) && horaFimFinal.after(ag.getHoraInicio())) {
                        JOptionPane.showMessageDialog(TelaPrincipalUI.this,
                            "Conflito de horário! Já existe um agendamento para este espaço neste período.",
                            "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
            }

            // 4. Criar e Adicionar Agendamento
            Agendamento novoAgendamento = new Agendamento(espacoSelecionado, dataSelecionada, horaInicioFinal, horaFimFinal);
            this.listaDeAgendamentos.add(novoAgendamento);

            // 5. Atualizar Tabela (o método será criado/finalizado no próximo passo)
            atualizarTabelaAgendamentos();

            JOptionPane.showMessageDialog(TelaPrincipalUI.this, "Agendamento adicionado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            // Limpar campos (opcional)
            // this.campoDataAgendamento.setValue(null); // JFormattedTextField pode precisar de setText("")
            // Ou resetar para o dia atual ou próximo dia útil.
        });
        painelControlesAgendamento.add(botaoAdicionarHorario, gbcControles);

        // Adicionar o painel de controles ao painel principal da aba Agendas (no topo)
        JPanel painelSuperiorAgendas = new JPanel(new BorderLayout());
        painelSuperiorAgendas.setOpaque(false); // Transparente
        tituloAgendas.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0)); // Adiciona margem inferior ao título
        painelSuperiorAgendas.add(tituloAgendas, BorderLayout.NORTH);
        painelSuperiorAgendas.add(painelControlesAgendamento, BorderLayout.CENTER);

        painel.add(painelSuperiorAgendas, BorderLayout.NORTH);

        // Tabela para listar horários
        String[] colunasTabelaAgendamentos = {"Espaço", "Data", "Hora Início", "Hora Fim", "Ações"};
        this.modeloTabelaAgendamentos = new DefaultTableModel(colunasTabelaAgendamentos, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == (colunasTabelaAgendamentos.length - 1); // "Ações" é a última coluna
            }
        };
        JTable tabelaAgendamentos = new JTable(this.modeloTabelaAgendamentos);

        // Estilização da tabela (similar à tabela de espaços)
        tabelaAgendamentos.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabelaAgendamentos.setRowHeight(30);
        tabelaAgendamentos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        tabelaAgendamentos.getTableHeader().setBackground(CINZA_CLARO);
        tabelaAgendamentos.getTableHeader().setForeground(PRETO_SUAVE);
        tabelaAgendamentos.setFillsViewportHeight(true);
        tabelaAgendamentos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Configurar a coluna "Ações" para usar o renderer e editor customizados
        int indiceColunaAcoesAgend = this.modeloTabelaAgendamentos.getColumnCount() - 1; // "Ações" é a última
        if (indiceColunaAcoesAgend >= 0 && this.modeloTabelaAgendamentos.getColumnName(indiceColunaAcoesAgend).equals("Ações")) {

            AcoesTabelaCellRendererEditor rendererEditorAgend = new AcoesTabelaCellRendererEditor(tabelaAgendamentos);
            // Nota: AcoesTabelaPanel atualmente mostra Editar e Excluir.
            // Para esta tabela, apenas a ação de Excluir será funcional.

            tabelaAgendamentos.getColumnModel().getColumn(indiceColunaAcoesAgend).setCellRenderer(rendererEditorAgend);
            tabelaAgendamentos.getColumnModel().getColumn(indiceColunaAcoesAgend).setCellEditor(rendererEditorAgend);

            tabelaAgendamentos.getColumnModel().getColumn(indiceColunaAcoesAgend).setPreferredWidth(85);
            tabelaAgendamentos.getColumnModel().getColumn(indiceColunaAcoesAgend).setMinWidth(80);
            tabelaAgendamentos.getColumnModel().getColumn(indiceColunaAcoesAgend).setMaxWidth(100);

            // Adicionar ActionListener específico para o botão Excluir do editor
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

            // Listener informativo para o botão Editar (já que AcoesTabelaPanel o exibe)
            rendererEditorAgend.addActionListenerParaEditar(e -> {
                 JOptionPane.showMessageDialog(TelaPrincipalUI.this,
                                                    "A edição de agendamentos não está implementada nesta tabela.",
                                                    "Informação", JOptionPane.INFORMATION_MESSAGE);
            });
        }

        // Adicionar dados de exemplo (remover ou comentar depois)
        // modeloTabelaAgendamentos.addRow(new Object[]{"Sala Alpha", "25/12/2023", "10:00", "11:00", "Remover"});
        // modeloTabelaAgendamentos.addRow(new Object[]{"Sala Beta", "26/12/2023", "14:30", "15:30", "Remover"});

        JScrollPane scrollPaneTabelaAgendamentos = new JScrollPane(tabelaAgendamentos);
        painel.add(scrollPaneTabelaAgendamentos, BorderLayout.CENTER);

        return painel;
    }

    private void atualizarTabelaAgendamentos() {
        if (this.modeloTabelaAgendamentos == null) {
            System.err.println("modeloTabelaAgendamentos ainda não foi inicializado!");
            return;
        }
        this.modeloTabelaAgendamentos.setRowCount(0); // Limpa a tabela

        Espaco espacoFiltrar = null;
        // Verifica se o comboBoxEspacosAgendamento já foi inicializado e tem algum item selecionado
        if (this.comboBoxEspacosAgendamento != null && this.comboBoxEspacosAgendamento.getSelectedItem() instanceof Espaco) {
            espacoFiltrar = (Espaco) this.comboBoxEspacosAgendamento.getSelectedItem();
        }

        for (Agendamento ag : this.listaDeAgendamentos) {
            // Se um espaço foi selecionado para filtro, apenas mostra agendamentos desse espaço
            // Se nenhum espaço estiver selecionado (espacoFiltrar == null), mostra todos.
            // Ou, podemos ter um item "Todos os Espaços" no ComboBox.
            // Por agora, se nada específico for selecionado (ou o ComboBox estiver vazio/não pronto),
            // vamos considerar mostrar todos, ou podemos decidir não mostrar nada até que um espaço seja escolhido.
            // A lógica atual: se espacoFiltrar é null (ex: ComboBox vazio ou item não Espaco), mostra todos.
            // Se espacoFiltrar NÃO é null, filtra.

            boolean deveAdicionar = true; // Por padrão, adiciona
            if (espacoFiltrar != null) { // Se há um filtro ativo
                if (!ag.getEspaco().getId().equals(espacoFiltrar.getId())) {
                    deveAdicionar = false; // Não é do espaço filtrado
                }
            }
            // Se você quiser que NADA seja mostrado até que um espaço seja selecionado,
            // mude a inicialização de deveAdicionar para false e só sete para true se espacoFiltrar for null
            // OU se o agendamento corresponder ao espacoFiltrar.
            // Ex: boolean deveAdicionar = (espacoFiltrar == null) || (ag.getEspaco().getId().equals(espacoFiltrar.getId()));


            if (deveAdicionar) {
                this.modeloTabelaAgendamentos.addRow(new Object[]{
                    ag.getEspaco().getNome(),
                    ag.getDataFormatada(),
                    ag.getHoraInicioFormatada(),
                    ag.getHoraFimFormatada(),
                    null // Para a coluna de ações com botões
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
                atualizarTabelaAgendamentos(); // Método que já repopula a tabela
                System.out.println("Agendamento removido.");
            } else {
                System.out.println("Remoção de agendamento cancelada.");
            }
        } else {
            System.err.println("Índice de linha inválido para remoção de agendamento: " + linhaModelo);
        }
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
