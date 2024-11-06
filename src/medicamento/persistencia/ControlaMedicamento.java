package medicamento.persistencia;

import medicamento.negocio.Medicamento;

import java.util.ArrayList;
import java.util.Optional;

public class ControlaMedicamento {

    private final ArrayList<Medicamento> medicamentos = new ArrayList<>();

    public boolean addMedicamento(Medicamento medicamento) {
        if (medicamento != null) {
            medicamentos.add(medicamento);
            return true;
        } else {
            return false;
        }
    }

    public ArrayList<Medicamento> mostrarMedicamentos() {
        return medicamentos;
    }

    public void deletarMedicamento(int id) {
        medicamentos.removeIf(medicamento -> medicamento.getId() == id);
    }

    public Optional<Medicamento> recuperarMedicamento(int id) {
        return medicamentos.stream().filter(med -> med.getId() == id).findFirst();
    }

    @Override
    public String toString() {
        return "" + medicamentos;
    }
}
