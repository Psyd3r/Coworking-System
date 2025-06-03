package espaco_capita;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;
import java.util.Calendar;
import javax.swing.JOptionPane;

public class GerenciadorCSVDados {

    private static final String SEPARADOR_CSV = ",";
    private static final SimpleDateFormat FORMATO_DATA_CSV = new SimpleDateFormat("dd/MM/yyyy");
    private static final SimpleDateFormat FORMATO_HORA_CSV = new SimpleDateFormat("HH:mm");

    // --- Métodos para Espaços ---

    public static List<Espaco> carregarEspacosDoCSV(String caminhoArquivo) {
        List<Espaco> espacos = new ArrayList<>();
        File arquivoCSV = new File(caminhoArquivo);

        if (!arquivoCSV.exists()) {
            System.out.println("Arquivo de espaços não encontrado, tentando criar: " + caminhoArquivo);
            try {
                File diretorioPai = arquivoCSV.getParentFile();
                if (diretorioPai != null && !diretorioPai.exists()) {
                    diretorioPai.mkdirs();
                }

                if (arquivoCSV.createNewFile()) {
                    try (BufferedWriter bw = new BufferedWriter(new FileWriter(arquivoCSV))) {
                        bw.write("id,nome,capacidade,descricao");
                        bw.newLine();
                        System.out.println("Arquivo espacos.csv criado com sucesso com cabeçalho.");
                    }
                    return espacos;
                } else {
                    System.err.println("Não foi possível criar o arquivo espacos.csv.");
                    return espacos;
                }
            } catch (IOException e) {
                System.err.println("Erro ao tentar criar o arquivo espacos.csv: " + e.getMessage());
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erro ao criar arquivo de espaços: " + e.getMessage(), "Erro de Arquivo", JOptionPane.ERROR_MESSAGE);
                return espacos;
            }
        }

        try (BufferedReader br = new BufferedReader(new FileReader(arquivoCSV))) {
            String linha;
            String cabecalho = br.readLine();
            if (cabecalho == null || !cabecalho.equals("id,nome,capacidade,descricao")) {
                 System.err.println("Arquivo espacos.csv está vazio ou com cabeçalho inválido. Tratando como lista vazia.");
                 return espacos;
            }

            while ((linha = br.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;
                String[] dados = linha.split(SEPARADOR_CSV, -1);

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
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                                          "Ocorreu um erro ao tentar carregar os dados dos espaços.\n" +
                                          "Verifique o console para mais detalhes.\n" +
                                          "Detalhes: " + e.getMessage(),
                                          "Erro de Leitura de Dados",
                                          JOptionPane.ERROR_MESSAGE);
        }
        return espacos;
    }

    public static void salvarEspacosNoCSV(String caminhoArquivo, List<Espaco> espacos) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(caminhoArquivo))) {
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
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                                          "Ocorreu um erro ao tentar salvar os dados dos espaços.\n" +
                                          "Verifique as permissões do arquivo ou o espaço em disco.\n" +
                                          "Detalhes: " + e.getMessage(),
                                          "Erro de Persistência",
                                          JOptionPane.ERROR_MESSAGE);
        }
    }

    // --- Métodos para Agendamentos ---

    public static List<Agendamento> carregarAgendamentosDoCSV(String caminhoArquivo, List<Espaco> todosOsEspacos) {
        List<Agendamento> agendamentos = new ArrayList<>();
        File arquivoCSV = new File(caminhoArquivo);

        if (!arquivoCSV.exists()) {
            System.out.println("Arquivo de agendamentos não encontrado, tentando criar: " + caminhoArquivo);
            try {
                File diretorioPai = arquivoCSV.getParentFile();
                if (diretorioPai != null && !diretorioPai.exists()) {
                    diretorioPai.mkdirs();
                }

                if (arquivoCSV.createNewFile()) {
                    try (BufferedWriter bw = new BufferedWriter(new FileWriter(arquivoCSV))) {
                        bw.write("id,id_espaco,data,hora_inicio,hora_fim");
                        bw.newLine();
                        System.out.println("Arquivo agendamentos.csv criado com sucesso com cabeçalho.");
                    }
                    return agendamentos;
                } else {
                    System.err.println("Não foi possível criar o arquivo agendamentos.csv.");
                    return agendamentos;
                }
            } catch (IOException e) {
                System.err.println("Erro ao tentar criar o arquivo agendamentos.csv: " + e.getMessage());
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erro ao criar arquivo de agendamentos: " + e.getMessage(), "Erro de Arquivo", JOptionPane.ERROR_MESSAGE);
                return agendamentos;
            }
        }

        try (BufferedReader br = new BufferedReader(new FileReader(arquivoCSV))) {
            String linha;
            String cabecalho = br.readLine();
            if (cabecalho == null || !cabecalho.equals("id,id_espaco,data,hora_inicio,hora_fim")) {
                 System.err.println("Arquivo agendamentos.csv está vazio ou com cabeçalho inválido. Tratando como lista vazia.");
                 return agendamentos;
            }

            while ((linha = br.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;
                String[] dados = linha.split(SEPARADOR_CSV, -1);

                if (dados.length >= 5) {
                    try {
                        String idAgendamento = dados[0];
                        String idEspaco = dados[1];
                        Date data = FORMATO_DATA_CSV.parse(dados[2]);
                        Date horaInicio = FORMATO_HORA_CSV.parse(dados[3]);
                        Date horaFim = FORMATO_HORA_CSV.parse(dados[4]);

                        Espaco espacoAssociado = null;
                        for (Espaco esp : todosOsEspacos) {
                            if (esp.getId().equals(idEspaco)) {
                                espacoAssociado = esp;
                                break;
                            }
                        }

                        if (espacoAssociado != null) {
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
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                                          "Ocorreu um erro ao tentar carregar os dados dos agendamentos.\n" +
                                          "Verifique o console para mais detalhes.\n" +
                                          "Detalhes: " + e.getMessage(),
                                          "Erro de Leitura de Dados",
                                          JOptionPane.ERROR_MESSAGE);
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
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                                          "Ocorreu um erro ao tentar salvar os dados dos agendamentos.\n" +
                                          "Verifique as permissões do arquivo ou o espaço em disco.\n" +
                                          "Detalhes: " + e.getMessage(),
                                          "Erro de Persistência",
                                          JOptionPane.ERROR_MESSAGE);
        }
    }

    // --- Métodos para DisponibilidadeEspaco ---

    public static List<DisponibilidadeEspaco> carregarDisponibilidadesDoCSV(String caminhoArquivo, List<Espaco> todosOsEspacos) {
        List<DisponibilidadeEspaco> disponibilidades = new ArrayList<>();
        File arquivoCSV = new File(caminhoArquivo);

        if (!arquivoCSV.exists()) {
            System.out.println("Arquivo de disponibilidades não encontrado, tentando criar: " + caminhoArquivo);
            try {
                File diretorioPai = arquivoCSV.getParentFile();
                if (diretorioPai != null && !diretorioPai.exists()) {
                    diretorioPai.mkdirs();
                }
                if (arquivoCSV.createNewFile()) {
                    try (BufferedWriter bw = new BufferedWriter(new FileWriter(arquivoCSV))) {
                        bw.write("id_disponibilidade,id_espaco,dia_semana,hora_inicio,hora_fim"); // Cabeçalho
                        bw.newLine();
                        System.out.println("Arquivo disponibilidades.csv criado com sucesso com cabeçalho.");
                    }
                } else {
                    System.err.println("Não foi possível criar o arquivo disponibilidades.csv.");
                }
            } catch (IOException e) {
                System.err.println("Erro ao tentar criar o arquivo disponibilidades.csv: " + e.getMessage());
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erro ao criar arquivo de disponibilidades: " + e.getMessage(), "Erro de Arquivo", JOptionPane.ERROR_MESSAGE);
            }
            return disponibilidades; // Retorna lista vazia se o arquivo foi recém-criado ou falhou ao criar
        }

        try (BufferedReader br = new BufferedReader(new FileReader(arquivoCSV))) {
            String linha;
            String cabecalho = br.readLine(); // Pular linha do cabeçalho
             if (cabecalho == null || !cabecalho.equals("id_disponibilidade,id_espaco,dia_semana,hora_inicio,hora_fim")) {
                 System.err.println("Arquivo disponibilidades.csv está vazio ou com cabeçalho inválido. Tratando como lista vazia.");
                 return disponibilidades;
            }


            while ((linha = br.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;
                String[] dados = linha.split(SEPARADOR_CSV, -1);

                if (dados.length >= 5) {
                    try {
                        String idDisponibilidade = dados[0];
                        String idEspaco = dados[1];
                        DiaDaSemana dia = DiaDaSemana.fromString(dados[2]); // Usar DiaDaSemana.fromString()
                        Date horaInicio = FORMATO_HORA_CSV.parse(dados[3]);
                        Date horaFim = FORMATO_HORA_CSV.parse(dados[4]);

                        Espaco espacoAssociado = null;
                        for (Espaco esp : todosOsEspacos) {
                            if (esp.getId().equals(idEspaco)) {
                                espacoAssociado = esp;
                                break;
                            }
                        }

                        if (espacoAssociado != null && dia != null) {
                            DisponibilidadeEspaco disp = new DisponibilidadeEspaco(idDisponibilidade, espacoAssociado, dia, horaInicio, horaFim);
                            disponibilidades.add(disp);
                        } else {
                            if (espacoAssociado == null) System.err.println("Espaço com ID " + idEspaco + " não encontrado para disponibilidade na linha: " + linha);
                            if (dia == null) System.err.println("Dia da semana inválido '" + dados[2] + "' na linha: " + linha);
                        }
                    } catch (ParseException e) {
                        System.err.println("Erro ao parsear hora em disponibilidades.csv: " + e.getMessage() + " na linha: " + linha);
                    }
                } else {
                    System.err.println("Linha mal formatada em disponibilidades.csv: " + linha);
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo de disponibilidades CSV: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao carregar disponibilidades: " + e.getMessage(), "Erro de Leitura", JOptionPane.ERROR_MESSAGE);
        }
        return disponibilidades;
    }

    public static void salvarDisponibilidadesNoCSV(String caminhoArquivo, List<DisponibilidadeEspaco> disponibilidades) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(caminhoArquivo))) {
            bw.write("id_disponibilidade,id_espaco,dia_semana,hora_inicio,hora_fim");
            bw.newLine();

            for (DisponibilidadeEspaco disp : disponibilidades) {
                StringJoiner sj = new StringJoiner(SEPARADOR_CSV);
                sj.add(disp.getId());
                sj.add(disp.getEspaco().getId());
                sj.add(disp.getDiaDaSemana().name()); // Salvar o nome do enum (ex: SEGUNDA)
                sj.add(FORMATO_HORA_CSV.format(disp.getHoraInicioDisponivel()));
                sj.add(FORMATO_HORA_CSV.format(disp.getHoraFimDisponivel()));
                bw.write(sj.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar o arquivo de disponibilidades CSV: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao salvar disponibilidades: " + e.getMessage(), "Erro de Escrita", JOptionPane.ERROR_MESSAGE);
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
