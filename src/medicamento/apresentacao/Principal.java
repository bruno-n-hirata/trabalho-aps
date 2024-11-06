package medicamento.apresentacao;

import medicamento.negocio.Medicamento;
import medicamento.persistencia.ControlaMedicamento;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class Principal extends JFrame {

    private JPanel mainPanel;
    private JLabel tituloLabel;
    private JLabel medicamentoLabel;
    private JTextField medicamentoTField;
    private JLabel principioAtivoLabel;
    private JTextField principioAtivoTField;
    private JLabel dataFabricacaoLabel;
    private JFormattedTextField dataFabricacaoTField;
    private JLabel dataValidadeLabel;
    private JFormattedTextField dataValidadeTField;
    private JButton salvarButton;
    private JButton limparButton;
    private JButton listarMedicamentos;
    private JTextField classeTerapeuticaTField;
    private JLabel classeTerapeuticaLabel;
    private JLabel dosagemLabel;
    private JTextField dosagemTField;

    private ControlaMedicamento controlaMedicamento = new ControlaMedicamento();

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private List<JTextField> campos = List.of(medicamentoTField, principioAtivoTField, dosagemTField, classeTerapeuticaTField);
    private List<JFormattedTextField> camposFormatados = List.of(dataFabricacaoTField, dataValidadeTField);

    public Principal() {
        setContentPane(mainPanel);
        setTitle("Cadastro de Medicamento");
        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        try {
            MaskFormatter dataMascara = new MaskFormatter("##/##/####");
            dataMascara.setPlaceholderCharacter('_');
            dataFabricacaoTField.setFormatterFactory(new DefaultFormatterFactory(dataMascara));
            dataValidadeTField.setFormatterFactory(new DefaultFormatterFactory(dataMascara));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        salvarButton.addActionListener(_ -> {
            if (validarCampos()) {
                Medicamento medicamento = new Medicamento();

                int id = Medicamento.getProximoIdMedicamento();
                medicamento.setId(id);
                medicamento.setNome(medicamentoTField.getText());
                medicamento.setPrincipioAtivo(principioAtivoTField.getText());
                medicamento.setDosagem(dosagemTField.getText());
                medicamento.setClasseTerapeutica(classeTerapeuticaTField.getText());

                try {
                    medicamento.setDataFabricacao(LocalDate.parse(dataFabricacaoTField.getText(), formatter));
                    medicamento.setDataValidade(LocalDate.parse(dataValidadeTField.getText(), formatter));
                } catch (DateTimeParseException e) {
                    JOptionPane.showMessageDialog(this, "Data inválida dos campos!");
                    return;
                }

                if (controlaMedicamento.addMedicamento(medicamento)) {
                    JOptionPane.showMessageDialog(null, "Medicamento cadastrado com sucesso!");
                    limparCampos();
                } else {
                    JOptionPane.showMessageDialog(null, "Erro ao cadastrar medicamento!");
                    limparCampos();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Preenchimento obrigatório dos campos!");
            }
        });

        limparButton.addActionListener(_ -> limparCampos());

        listarMedicamentos.addActionListener(_ -> {
            dlMostrarMedicamentos dlg = new dlMostrarMedicamentos(controlaMedicamento);
            dlg.setVisible(true);
        });
    }

    public void limparCampos() {
        campos.forEach(campo -> campo.setText(""));
        camposFormatados.forEach(campo -> campo.setValue(null));
        medicamentoTField.requestFocus();
    }

    public boolean validarCampos() {
        return campos.stream().noneMatch(campo -> campo.getText().trim().isEmpty()) && camposFormatados.stream().noneMatch(campo -> campo.getValue() == null);
    }
}
