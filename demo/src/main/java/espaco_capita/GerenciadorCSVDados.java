package espaco_capita;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner; // Importação adicionada
import java.util.Calendar; // Importação adicionada

public class GerenciadorCSVDados {

    private static final String SEPARADOR_CSV = ",";
    private static final SimpleDateFormat FORMATO_DATA_CSV = new SimpleDateFormat("dd/MM/yyyy");
    private static final SimpleDateFormat FORMATO_HORA_CSV = new SimpleDateFormat("HH:mm");

    // --- Métodos para Espaços ---

    public static List<Espaco> carregarEspacosDoCSV(String caminhoArquivo) {
        List<Espaco> espacos = new ArrayList<>();
        File arquivoCSV = new File(caminhoArquivo);

        if (!arquivoCSV.exists()) {
            System.out.println("Arquivo de espaços não encontrado, iniciando com lista vazia: " + caminhoArquivo);
            return espacos; // Retorna lista vazia se o arquivo não existe
        }

        try (BufferedReader br = new BufferedReader(new FileReader(arquivoCSV))) {
            String linha;
            br.readLine(); // Pular linha do cabeçalho

            while ((linha = br.readLine()) != null) {
                if (linha.trim().isEmpty()) continue; // Pular linhas vazias
                String[] dados = linha.split(SEPARADOR_CSV, -1); // -1 para incluir campos vazios no final

                if (dados.length >= 4) {
                    try {
                        String id = dados[0];
                        String nome = dados[1];
                        int capacidade = Integer.parseInt(dados[2]);
                        String descricao = dados[3];
                        espacos.add(new Espaco(id, nome, capacidade, descricao));
                    } catch (NumberFormatException e) {
                        System.err.println("Erro ao parsear capacidade em espacos.csv: " + dados[2] + " na linha: " + linha);
                    }
                } else {
                    System.err.println("Linha mal formatada em espacos.csv: " + linha);
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo de espaços CSV: " + e.getMessage());
            // Considerar lançar uma exceção customizada ou notificar o usuário
        }
        return espacos;
    }

    public static void salvarEspacosNoCSV(String caminhoArquivo, List<Espaco> espacos) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(caminhoArquivo))) {
            // Escrever cabeçalho
            bw.write("id,nome,capacidade,descricao");
            bw.newLine();

            for (Espaco espaco : espacos) {
                StringJoiner sj = new StringJoiner(SEPARADOR_CSV);
                sj.add(espaco.getId());
                sj.add(tratarStringParaCSV(espaco.getNome()));
                sj.add(String.valueOf(espaco.getCapacidade()));
                sj.add(tratarStringParaCSV(espaco.getDescricao()));
                bw.write(sj.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar o arquivo de espaços CSV: " + e.getMessage());
            // Considerar lançar uma exceção customizada ou notificar o usuário
        }
    }

    // --- Métodos para Agendamentos ---

    public static List<Agendamento> carregarAgendamentosDoCSV(String caminhoArquivo, List<Espaco> todosOsEspacos) {
        List<Agendamento> agendamentos = new ArrayList<>();
        File arquivoCSV = new File(caminhoArquivo);

        if (!arquivoCSV.exists()) {
            System.out.println("Arquivo de agendamentos não encontrado, iniciando com lista vazia: " + caminhoArquivo);
            return agendamentos;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(arquivoCSV))) {
            String linha;
            br.readLine(); // Pular linha do cabeçalho

            while ((linha = br.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;
                String[] dados = linha.split(SEPARADOR_CSV, -1);

                if (dados.length >= 5) {
                    try {
                        String idAgendamento = dados[0]; // Este ID não está sendo usado no construtor atual de Agendamento
                        String idEspaco = dados[1];
                        Date data = FORMATO_DATA_CSV.parse(dados[2]);
                        Date horaInicio = FORMATO_HORA_CSV.parse(dados[3]); // Parse direto como HH:mm
                        Date horaFim = FORMATO_HORA_CSV.parse(dados[4]);   // Parse direto como HH:mm

                        Espaco espacoAssociado = null;
                        for (Espaco esp : todosOsEspacos) {
                            if (esp.getId().equals(idEspaco)) {
                                espacoAssociado = esp;
                                break;
                            }
                        }

                        if (espacoAssociado != null) {
                            // Para consistência, ajustar a data das horas para a data do agendamento
                            Calendar calData = Calendar.getInstance();
                            calData.setTime(data);

                            Calendar calInicio = Calendar.getInstance();
                            calInicio.setTime(horaInicio);
                            calInicio.set(Calendar.YEAR, calData.get(Calendar.YEAR));
                            calInicio.set(Calendar.MONTH, calData.get(Calendar.MONTH));
                            calInicio.set(Calendar.DAY_OF_MONTH, calData.get(Calendar.DAY_OF_MONTH));

                            Calendar calFim = Calendar.getInstance();
                            calFim.setTime(horaFim);
                            calFim.set(Calendar.YEAR, calData.get(Calendar.YEAR));
                            calFim.set(Calendar.MONTH, calData.get(Calendar.MONTH));
                            calFim.set(Calendar.DAY_OF_MONTH, calData.get(Calendar.DAY_OF_MONTH));

                            // Utiliza o novo construtor para preservar o ID do CSV
                            Agendamento ag = new Agendamento(idAgendamento, espacoAssociado, calData.getTime(), calInicio.getTime(), calFim.getTime());
                            agendamentos.add(ag);

                        } else {
                            System.err.println("Espaço com ID " + idEspaco + " não encontrado para o agendamento na linha: " + linha);
                        }
                    } catch (ParseException e) {
                        System.err.println("Erro ao parsear data/hora em agendamentos.csv: " + e.getMessage() + " na linha: " + linha);
                    }
                } else {
                     System.err.println("Linha mal formatada em agendamentos.csv: " + linha);
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo de agendamentos CSV: " + e.getMessage());
        }
        return agendamentos;
    }

    public static void salvarAgendamentosNoCSV(String caminhoArquivo, List<Agendamento> agendamentos) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(caminhoArquivo))) {
            bw.write("id,id_espaco,data,hora_inicio,hora_fim");
            bw.newLine();

            for (Agendamento ag : agendamentos) {
                StringJoiner sj = new StringJoiner(SEPARADOR_CSV);
                sj.add(ag.getId());
                sj.add(ag.getEspaco().getId());
                sj.add(FORMATO_DATA_CSV.format(ag.getData()));
                sj.add(FORMATO_HORA_CSV.format(ag.getHoraInicio()));
                sj.add(FORMATO_HORA_CSV.format(ag.getHoraFim()));
                bw.write(sj.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar o arquivo de agendamentos CSV: " + e.getMessage());
        }
    }

    // Método auxiliar para tratar strings que podem conter o separador CSV ou aspas.
    private static String tratarStringParaCSV(String valor) {
        if (valor == null) {
            return "";
        }
        // Se o valor contém o separador, aspas ou quebras de linha, envolve com aspas duplas.
        // Aspas duplas dentro do valor são duplicadas.
        if (valor.contains(SEPARADOR_CSV) || valor.contains("\"") || valor.contains("\n") || valor.contains("\r")) {
            return "\"" + valor.replace("\"", "\"\"") + "\"";
        }
        return valor;
    }
}
