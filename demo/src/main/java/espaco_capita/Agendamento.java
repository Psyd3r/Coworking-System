package espaco_capita;

import java.text.SimpleDateFormat;
import java.util.Date; // Usaremos java.util.Date por enquanto, pode ser refatorado para java.time depois

public class Agendamento {
    private String id;
    private Espaco espaco; // Referência direta ao objeto Espaco
    private Date data;
    private Date horaInicio; // Usaremos Date para armazenar hora, formataremos na exibição
    private Date horaFim;

    // Formatter para exibição de data e hora
    private static final SimpleDateFormat FORMATO_DATA = new SimpleDateFormat("dd/MM/yyyy");
    private static final SimpleDateFormat FORMATO_HORA = new SimpleDateFormat("HH:mm");

    public Agendamento(Espaco espaco, Date data, Date horaInicio, Date horaFim) {
        this.id = java.util.UUID.randomUUID().toString(); // ID único
        this.espaco = espaco;
        this.data = data;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
    }

    // Getters
    public String getId() {
        return id;
    }

    public Espaco getEspaco() {
        return espaco;
    }

    public Date getData() {
        return data;
    }

    public String getDataFormatada() {
        return data != null ? FORMATO_DATA.format(data) : "";
    }

    public Date getHoraInicio() {
        return horaInicio;
    }

    public String getHoraInicioFormatada() {
        return horaInicio != null ? FORMATO_HORA.format(horaInicio) : "";
    }

    public Date getHoraFim() {
        return horaFim;
    }

    public String getHoraFimFormatada() {
        return horaFim != null ? FORMATO_HORA.format(horaFim) : "";
    }

    // Setters (podem ser usados para edição, se necessário no futuro)
    public void setEspaco(Espaco espaco) {
        this.espaco = espaco;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public void setHoraInicio(Date horaInicio) {
        this.horaInicio = horaInicio;
    }

    public void setHoraFim(Date horaFim) {
        this.horaFim = horaFim;
    }

    @Override
    public String toString() {
        return "Agendamento{" +
               "id='" + id + '\'' +
               ", espaco=" + (espaco != null ? espaco.getNome() : "N/A") +
               ", data=" + getDataFormatada() +
               ", horaInicio=" + getHoraInicioFormatada() +
               ", horaFim=" + getHoraFimFormatada() +
               '}';
    }
}
