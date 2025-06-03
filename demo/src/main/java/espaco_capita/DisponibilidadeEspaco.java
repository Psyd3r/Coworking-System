package espaco_capita;

import java.text.SimpleDateFormat;
import java.util.Date; // Usaremos java.util.Date por enquanto

public class DisponibilidadeEspaco {
    private String id;
    private Espaco espaco; // Referência direta ao objeto Espaco
    private DiaDaSemana diaDaSemana;
    private Date horaInicioDisponivel; // Armazena apenas a hora, a data não é relevante aqui
    private Date horaFimDisponivel;   // Armazena apenas a hora

    private static final SimpleDateFormat FORMATO_HORA = new SimpleDateFormat("HH:mm");

    // Construtor para quando um novo registro de disponibilidade é criado pela UI
    public DisponibilidadeEspaco(Espaco espaco, DiaDaSemana diaDaSemana, Date horaInicio, Date horaFim) {
        this.id = java.util.UUID.randomUUID().toString();
        this.espaco = espaco;
        this.diaDaSemana = diaDaSemana;
        this.horaInicioDisponivel = horaInicio;
        this.horaFimDisponivel = horaFim;
    }

    // Construtor para quando carregamos do CSV (com ID)
    public DisponibilidadeEspaco(String id, Espaco espaco, DiaDaSemana diaDaSemana, Date horaInicio, Date horaFim) {
        this.id = id;
        this.espaco = espaco;
        this.diaDaSemana = diaDaSemana;
        this.horaInicioDisponivel = horaInicio;
        this.horaFimDisponivel = horaFim;
    }

    // Getters
    public String getId() {
        return id;
    }

    public Espaco getEspaco() {
        return espaco;
    }

    public DiaDaSemana getDiaDaSemana() {
        return diaDaSemana;
    }

    public Date getHoraInicioDisponivel() {
        return horaInicioDisponivel;
    }

    public String getHoraInicioFormatada() {
        return horaInicioDisponivel != null ? FORMATO_HORA.format(horaInicioDisponivel) : "";
    }

    public Date getHoraFimDisponivel() {
        return horaFimDisponivel;
    }

    public String getHoraFimFormatada() {
        return horaFimDisponivel != null ? FORMATO_HORA.format(horaFimDisponivel) : "";
    }

    // Setters (se necessário para edição direta, embora seja comum recriar o objeto)
    public void setEspaco(Espaco espaco) {
        this.espaco = espaco;
    }

    public void setDiaDaSemana(DiaDaSemana diaDaSemana) {
        this.diaDaSemana = diaDaSemana;
    }

    public void setHoraInicioDisponivel(Date horaInicioDisponivel) {
        this.horaInicioDisponivel = horaInicioDisponivel;
    }

    public void setHoraFimDisponivel(Date horaFimDisponivel) {
        this.horaFimDisponivel = horaFimDisponivel;
    }

    @Override
    public String toString() {
        return "DisponibilidadeEspaco{" +
               "id='" + id + '\'' +
               ", espaco=" + (espaco != null ? espaco.getNome() : "N/A") +
               ", diaDaSemana=" + diaDaSemana.getNomeFormatado() +
               ", horaInicio=" + getHoraInicioFormatada() +
               ", horaFim=" + getHoraFimFormatada() +
               '}';
    }
}
