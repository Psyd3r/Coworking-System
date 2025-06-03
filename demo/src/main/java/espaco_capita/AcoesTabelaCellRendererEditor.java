package espaco_capita;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AcoesTabelaCellRendererEditor extends AbstractCellEditor implements TableCellRenderer, TableCellEditor {
    private AcoesTabelaPanel painelAcoesRender;
    private AcoesTabelaPanel painelAcoesEditor;
    private JTable tabela; // Para obter a linha durante a edição
    private Object editorValue; // Para armazenar o valor da célula durante a edição

    public AcoesTabelaCellRendererEditor(JTable tabela) {
        this.tabela = tabela;
        this.painelAcoesRender = new AcoesTabelaPanel();
        this.painelAcoesEditor = new AcoesTabelaPanel();

        // Adiciona listeners aos botões do painel de edição
        // Estes listeners vão disparar o evento de parada da edição
        // A ação real de editar/excluir será tratada na TelaPrincipalUI
        ActionListener actionListenerEditor = e -> {
            // Para o editor, precisamos parar a edição da célula e passar a ação para a tabela/modelo
            fireEditingStopped();
        };

        painelAcoesEditor.botaoEditar.addActionListener(actionListenerEditor);
        painelAcoesEditor.botaoExcluir.addActionListener(actionListenerEditor);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        // Define a cor de fundo com base na seleção da linha
        if (isSelected) {
            painelAcoesRender.setBackground(table.getSelectionBackground());
        } else {
            painelAcoesRender.setBackground(table.getBackground());
        }
        return painelAcoesRender;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        // O 'value' aqui não é usado diretamente, pois o painel é sempre o mesmo.
        // Mas guardamos para o getCellEditorValue.
        this.editorValue = value;

        if (isSelected) {
            painelAcoesEditor.setBackground(table.getSelectionBackground());
        } else {
            painelAcoesEditor.setBackground(table.getBackground());
        }
        return painelAcoesEditor;
    }

    @Override
    public Object getCellEditorValue() {
        // Este valor não é realmente usado para modificar os dados da célula diretamente,
        // pois as ações são tratadas externamente. Retornar o valor original ou um placeholder.
        return editorValue;
    }

    // Método para adicionar listeners específicos que serão chamados pela TelaPrincipalUI
    // quando o botão Editar do editor for clicado.
    public void addActionListenerParaEditar(ActionListener listener) {
        // Removemos o listener genérico e adicionamos o específico
        for(ActionListener al : painelAcoesEditor.botaoEditar.getActionListeners()) {
            painelAcoesEditor.botaoEditar.removeActionListener(al);
        }
        painelAcoesEditor.botaoEditar.addActionListener(e -> {
            fireEditingStopped(); // Primeiro para a edição
            listener.actionPerformed(e); // Depois executa a ação específica
        });
    }

    public void addActionListenerParaExcluir(ActionListener listener) {
        for(ActionListener al : painelAcoesEditor.botaoExcluir.getActionListeners()) {
            painelAcoesEditor.botaoExcluir.removeActionListener(al);
        }
        painelAcoesEditor.botaoExcluir.addActionListener(e -> {
            fireEditingStopped();
            listener.actionPerformed(e);
        });
    }
}
