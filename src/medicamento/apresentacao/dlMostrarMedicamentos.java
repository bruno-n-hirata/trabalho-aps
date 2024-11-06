package medicamento.apresentacao;

import medicamento.negocio.Medicamento;
import medicamento.persistencia.ControlaMedicamento;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.text.MaskFormatter;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class dlMostrarMedicamentos extends JDialog {
    private JPanel contentPane;
    private JTable tabelaMedicamentos;
    private JScrollPane scrollPane;
    private JButton cancelarButton;
    private JButton alterarButton;
    private JButton deletarButton;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public dlMostrarMedicamentos(ControlaMedicamento cm) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(cancelarButton);
        setTitle("Lista de Medicamentos");

        String[] colunas = { "ID", "Nome", "Data Fabricação", "Data Validade", "Principio Ativo", "Dosagem", "Classe Terapêutica" };
        DefaultTableModel modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0;
            }
        };

        for (Medicamento medicamento : cm.mostrarMedicamentos()) {
            Object[] dadosMedicamento = {
                    medicamento.getId(),
                    medicamento.getNome(),
                    medicamento.getDataFabricacao().format(formatter),
                    medicamento.getDataValidade().format(formatter),
                    medicamento.getPrincipioAtivo(),
                    medicamento.getDosagem(),
                    medicamento.getClasseTerapeutica()
            };
            modeloTabela.addRow(dadosMedicamento);
        }

        tabelaMedicamentos.setModel(modeloTabela);

        try {
            MaskFormatter dataMascara = new MaskFormatter("##/##/####");
            dataMascara.setPlaceholderCharacter('_');

            JFormattedTextField campoMascara = new JFormattedTextField(dataMascara);

            TableColumn dataFabricacaoColuna = tabelaMedicamentos.getColumnModel().getColumn(2);
            dataFabricacaoColuna.setCellEditor(new DefaultCellEditor(campoMascara));
            TableColumn dataValidadeColuna = tabelaMedicamentos.getColumnModel().getColumn(3);
            dataValidadeColuna.setCellEditor(new DefaultCellEditor(campoMascara));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        pack();
        setLocationRelativeTo(null);

        alterarButton.addActionListener(_ -> {
            int linhaSelecionada = tabelaMedicamentos.getSelectedRow();

            if (linhaSelecionada != -1) {
                if (validarCampos(linhaSelecionada)) {
                    int id = (Integer) tabelaMedicamentos.getValueAt(linhaSelecionada, 0);
                    String nome = tabelaMedicamentos.getValueAt(linhaSelecionada, 1).toString().trim();

                    LocalDate dataFabricacao;
                    LocalDate dataValidade;
                    try {
                        dataFabricacao = LocalDate.parse(tabelaMedicamentos.getValueAt(linhaSelecionada, 2).toString(), formatter);
                        dataValidade = LocalDate.parse(tabelaMedicamentos.getValueAt(linhaSelecionada, 3).toString(), formatter);
                    } catch (DateTimeParseException e) {
                        JOptionPane.showMessageDialog(this, "Data inválida dos campos!");
                        return;
                    }

                    String principioAtivo = tabelaMedicamentos.getValueAt(linhaSelecionada, 4).toString().trim();
                    String dosagem = tabelaMedicamentos.getValueAt(linhaSelecionada, 5).toString().trim();
                    String classeTerapeutica = tabelaMedicamentos.getValueAt(linhaSelecionada, 6).toString().trim();

                    cm.recuperarMedicamento(id).ifPresent(medicamento -> {

                        Map<String, String> alteracoes = new LinkedHashMap<>();

                        if (!nome.equals(medicamento.getNome())) {
                            alteracoes.put("Nome", "De: " + medicamento.getNome() + " - Para: " + nome);
                        }

                        if (!dataFabricacao.equals(medicamento.getDataFabricacao())) {
                            alteracoes.put("Data Fabricação", "De: " + medicamento.getDataFabricacao().format(formatter) + " - Para: " + dataFabricacao.format(formatter));
                        }

                        if (!dataValidade.equals(medicamento.getDataValidade())) {
                            alteracoes.put("Data Validade", "De: " + medicamento.getDataValidade().format(formatter) + " - Para: " + dataValidade.format(formatter));
                        }

                        if (!principioAtivo.equals(medicamento.getPrincipioAtivo())) {
                            alteracoes.put("Principio Ativo", "De: " + medicamento.getPrincipioAtivo() + " - Para: " + principioAtivo);
                        }

                        if (!dosagem.equals(medicamento.getDosagem())) {
                            alteracoes.put("Dosagem", "De: " + medicamento.getDosagem() + " - Para: " + dosagem);
                        }

                        if (!classeTerapeutica.equals(medicamento.getClasseTerapeutica())) {
                            alteracoes.put("Classe Terapêutica", "De: " + medicamento.getClasseTerapeutica() + " - Para: " + classeTerapeutica);
                        }

                        if (!alteracoes.isEmpty()) {
                            StringBuilder mensagem = new StringBuilder("Alterações realizadas:\n");
                            for (String campo : alteracoes.keySet()) {
                                mensagem.append(campo).append(": ").append(alteracoes.get(campo)).append("\n");
                            }
                            mensagem.append("\nTem certeza que deseja alterar o medicamento do ID ").append(id).append("?");

                            int confirmar = JOptionPane.showConfirmDialog(this, mensagem.toString(), "Alterar medicamento", JOptionPane.YES_NO_OPTION);

                            if (confirmar == JOptionPane.YES_OPTION) {
                                medicamento.setNome(nome);
                                medicamento.setDataFabricacao(dataFabricacao);
                                medicamento.setDataValidade(dataValidade);
                                medicamento.setPrincipioAtivo(principioAtivo);
                                medicamento.setDosagem(dosagem);
                                medicamento.setClasseTerapeutica(classeTerapeutica);

                                JOptionPane.showMessageDialog(this, "Alterações aplicadas para o medicamento do ID " + id + ".", "Alterar medicamento", JOptionPane.INFORMATION_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(this, "Nenhuma alteração foi feita para o medicamento do ID " + id + ".");
                        }
                    });
                } else {
                    JOptionPane.showMessageDialog(this, "Preenchimento obrigatório dos campos!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecione uma linha para alterar.", "Alterar medicamento", JOptionPane.WARNING_MESSAGE);
            }
        });

        deletarButton.addActionListener(_ -> {
            int linhaSelecionada = tabelaMedicamentos.getSelectedRow();
            if (linhaSelecionada != -1) {
                int id = (Integer) tabelaMedicamentos.getValueAt(linhaSelecionada, 0);

                int confirmar = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir o medicamento do ID " + id + "?",
                        "Deletar medicamento", JOptionPane.YES_NO_OPTION);

                if (confirmar == JOptionPane.YES_OPTION) {
                    cm.deletarMedicamento(id);
                    modeloTabela.removeRow(linhaSelecionada);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecione uma linha para excluir.", "Deletar medicamento", JOptionPane.WARNING_MESSAGE);
            }
        });

        cancelarButton.addActionListener(_ -> dispose());
    }

    public boolean validarCampos(int linhaSelecionada) {
        return IntStream.range(0, 6).noneMatch(campo -> tabelaMedicamentos.getValueAt(linhaSelecionada, campo).toString().trim().isEmpty());
    }
}
