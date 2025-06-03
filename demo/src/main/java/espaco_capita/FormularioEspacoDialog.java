package espaco_capita;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FormularioEspacoDialog extends JDialog {

    private JTextField campoNome;
    private JSpinner spinnerCapacidade; // Usar JSpinner para entrada numérica controlada
    private JTextArea areaDescricao;
    private JButton botaoSalvar;
    private JButton botaoCancelar;

    private Espaco espacoAtual; // Para armazenar o espaço em edição, null para novo
    private boolean salvoComSucesso = false; // Flag para indicar se o salvamento foi bem-sucedido

    // Cores (podem ser herdadas ou passadas, ou definidas aqui se necessário)
    private final Color VERDE_PRINCIPAL = Color.decode("#007a3e");
    private final Color BRANCO = Color.decode("#ffffff");
    private final Color CINZA_ESCURO = Color.decode("#3a3838");

    public FormularioEspacoDialog(Frame pai, String titulo, Espaco espacoParaEditar) {
        super(pai, titulo, true); // true para modal
        this.espacoAtual = espacoParaEditar;

        configurarLayoutDialog();
        inicializarCampos();
        configurarAcoesBotoes();

        if (espacoAtual != null) {
            preencherFormularioComEspacoExistente();
        }

        pack(); // Ajusta o tamanho do diálogo aos componentes
        setLocationRelativeTo(pai); // Centraliza em relação à janela pai
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    private void configurarLayoutDialog() {
        setLayout(new BorderLayout(10, 10)); // Layout principal com espaçamento
        getRootPane().setBorder(new EmptyBorder(15, 15, 15, 15)); // Padding interno no diálogo
        setBackground(BRANCO);
    }

    private void inicializarCampos() {
        JPanel painelFormulario = new JPanel(new GridBagLayout());
        painelFormulario.setBackground(BRANCO);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Espaçamento entre componentes
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Campo Nome
        JLabel labelNome = new JLabel("Nome do Espaço:");
        labelNome.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.2; // Peso para o label
        painelFormulario.add(labelNome, gbc);

        campoNome = new JTextField(25); // Largura do campo
        campoNome.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.8; // Peso para o campo
        painelFormulario.add(campoNome, gbc);

        // Campo Capacidade
        JLabel labelCapacidade = new JLabel("Capacidade:");
        labelCapacidade.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        painelFormulario.add(labelCapacidade, gbc);

        spinnerCapacidade = new JSpinner(new SpinnerNumberModel(1, 1, 500, 1)); // valor inicial, min, max, step
        spinnerCapacidade.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JSpinner.DefaultEditor editorCapacidade = (JSpinner.DefaultEditor) spinnerCapacidade.getEditor();
        editorCapacidade.getTextField().setColumns(5); // Largura do spinner
        gbc.gridx = 1;
        gbc.gridy = 1;
        painelFormulario.add(spinnerCapacidade, gbc);

        // Campo Descrição
        JLabel labelDescricao = new JLabel("Descrição:");
        labelDescricao.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.NORTHWEST; // Alinhar label no topo para JTextArea
        painelFormulario.add(labelDescricao, gbc);

        areaDescricao = new JTextArea(5, 25); // Linhas, Colunas
        areaDescricao.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        areaDescricao.setLineWrap(true);
        areaDescricao.setWrapStyleWord(true);
        JScrollPane scrollDescricao = new JScrollPane(areaDescricao);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.BOTH; // Para preencher altura e largura
        gbc.weighty = 1.0; // Permitir que a descrição cresça verticalmente
        painelFormulario.add(scrollDescricao, gbc);
        gbc.weighty = 0; // Resetar weighty
        gbc.fill = GridBagConstraints.HORIZONTAL; // Resetar fill

        // Painel de Botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        painelBotoes.setBackground(BRANCO);
        painelBotoes.setBorder(new EmptyBorder(10, 0, 0, 0)); // Margem acima dos botões

        botaoSalvar = new JButton("Salvar");
        estilizarBotaoDialog(botaoSalvar, VERDE_PRINCIPAL);

        botaoCancelar = new JButton("Cancelar");
        estilizarBotaoDialog(botaoCancelar, CINZA_ESCURO);

        painelBotoes.add(botaoSalvar);
        painelBotoes.add(botaoCancelar);

        add(painelFormulario, BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);
    }

    private void estilizarBotaoDialog(JButton botao, Color corFundo) {
        botao.setFont(new Font("Segoe UI", Font.BOLD, 14));
        botao.setForeground(BRANCO);
        botao.setBackground(corFundo);
        botao.setOpaque(true);
        botao.setBorderPainted(false);
        botao.setFocusPainted(false);
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botao.setMargin(new Insets(8, 15, 8, 15));

        // Efeito hover
        Color corOriginal = botao.getBackground();
        botao.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botao.setBackground(corOriginal.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                botao.setBackground(corOriginal);
            }
        });
    }

    private void preencherFormularioComEspacoExistente() {
        if (espacoAtual == null) return;
        campoNome.setText(espacoAtual.getNome());
        spinnerCapacidade.setValue(espacoAtual.getCapacidade());
        areaDescricao.setText(espacoAtual.getDescricao());
    }

    private void configurarAcoesBotoes() {
        botaoSalvar.addActionListener(e -> salvarEspaco());
        botaoCancelar.addActionListener(e -> dispose()); // Fecha o diálogo
    }

    private void salvarEspaco() {
        // Validação básica (pode ser expandida)
        String nome = campoNome.getText().trim();
        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(this, "O nome do espaço não pode estar vazio.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            campoNome.requestFocus();
            return;
        }
        int capacidade = (Integer) spinnerCapacidade.getValue();
        if (capacidade <= 0) {
            JOptionPane.showMessageDialog(this, "A capacidade deve ser maior que zero.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            spinnerCapacidade.requestFocus();
            return;
        }
        String descricao = areaDescricao.getText().trim();

        // Lógica para criar ou atualizar o objeto Espaco
        // Por enquanto, apenas imprimimos no console ou preparamos para retornar
        // A interação com a lista e atualização da tabela será feita na TelaPrincipalUI

        if (espacoAtual == null) { // Novo Espaço
            espacoAtual = new Espaco(nome, capacidade, descricao);
            System.out.println("Novo espaço criado (no dialog): " + espacoAtual.getNome());
        } else { // Editando Espaço
            espacoAtual.setNome(nome);
            espacoAtual.setCapacidade(capacidade);
            espacoAtual.setDescricao(descricao);
            System.out.println("Espaço atualizado (no dialog): " + espacoAtual.getNome());
        }

        // Sinalizar que o salvamento foi bem-sucedido (para a TelaPrincipalUI pegar o objeto)
        // Uma forma comum é ter um método público no dialog para pegar o 'espacoAtual'
        // e um flag indicando se foi salvo.
        // Por agora, vamos apenas fechar o dialog. A lógica de atualização da tabela
        // será chamada pela TelaPrincipalUI depois que o dialog for fechado.

        salvoComSucesso = true; // Define o flag de sucesso
        dispose(); // Fecha o diálogo após salvar
    }

    // Método para permitir que a TelaPrincipalUI obtenha o Espaco após o dialog ser fechado
    public Espaco getEspacoSalvo() {
        return this.salvoComSucesso ? this.espacoAtual : null;
    }

    // Getter para o flag de sucesso
    public boolean isSalvoComSucesso() {
        return salvoComSucesso;
    }

    // A definição da classe Espaco aninhada foi removida daqui,
    // pois agora existe o arquivo Espaco.java separado.
}
