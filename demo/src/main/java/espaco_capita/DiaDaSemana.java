package espaco_capita;

public enum DiaDaSemana {
    SEGUNDA("Segunda-feira"),
    TERCA("Terça-feira"),
    QUARTA("Quarta-feira"),
    QUINTA("Quinta-feira"),
    SEXTA("Sexta-feira"),
    SABADO("Sábado"),
    DOMINGO("Domingo");

    private final String nomeFormatado;

    DiaDaSemana(String nomeFormatado) {
        this.nomeFormatado = nomeFormatado;
    }

    public String getNomeFormatado() {
        return nomeFormatado;
    }

    @Override
    public String toString() {
        return nomeFormatado; // Para exibição em JComboBox ou JTable
    }

    // Opcional: método para obter o enum a partir de um nome (útil ao ler do CSV)
    public static DiaDaSemana fromString(String text) {
        for (DiaDaSemana dia : DiaDaSemana.values()) {
            if (dia.name().equalsIgnoreCase(text) || dia.nomeFormatado.equalsIgnoreCase(text)) {
                return dia;
            }
        }
        // Retornar null ou lançar exceção se não encontrar
        // Para simplicidade, vamos assumir que o CSV guardará o .name() (SEGUNDA, TERCA, etc.)
        try {
            return valueOf(text.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
