package medicamento.negocio;

import java.time.LocalDate;

public class Medicamento {

    private int id;
    private String nome;
    private String principioAtivo;
    private String dosagem;
    private String classeTerapeutica;
    private LocalDate dataFabricacao;
    private LocalDate dataValidade;

    public static int proximoIdMedicamento = 1;

    public Medicamento(String nome, String principioAtivo, String dosagem, String classeTerapeutica, LocalDate dataFabricacao, LocalDate dataValidade) {
        this.nome = nome;
        this.principioAtivo = principioAtivo;
        this.dosagem = dosagem;
        this.classeTerapeutica = classeTerapeutica;
        this.dataFabricacao = dataFabricacao;
        this.dataValidade = dataValidade;
    }

    public Medicamento() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPrincipioAtivo() {
        return principioAtivo;
    }

    public void setPrincipioAtivo(String principioAtivo) {
        this.principioAtivo = principioAtivo;
    }

    public String getDosagem() {
        return dosagem;
    }

    public void setDosagem(String dosagem) {
        this.dosagem = dosagem;
    }

    public String getClasseTerapeutica() {
        return classeTerapeutica;
    }

    public void setClasseTerapeutica(String classeTerapeutica) {
        this.classeTerapeutica = classeTerapeutica;
    }

    public LocalDate getDataFabricacao() {
        return dataFabricacao;
    }

    public void setDataFabricacao(LocalDate dataFabricacao) {
        this.dataFabricacao = dataFabricacao;
    }

    public LocalDate getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(LocalDate dataValidade) {
        this.dataValidade = dataValidade;
    }

    public static int getProximoIdMedicamento() {
        return proximoIdMedicamento++;
    }

    @Override
    public String toString() {
        return "Medicamento{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", principioAtivo='" + principioAtivo + '\'' +
                ", dosagem='" + dosagem + '\'' +
                ", classeTerapeutica='" + classeTerapeutica + '\'' +
                ", dataFabricacao=" + dataFabricacao +
                ", dataValidade=" + dataValidade +
                '}';
    }
}
