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
    System.out.println(">>> Iniciando carregarDisponibilidadesDoCSV de: " + caminhoArquivo);
    List<DisponibilidadeEspaco> disponibilidades = new ArrayList<>();
    File arquivoCSV = new File(caminhoArquivo);

    System.out.println("Caminho absoluto do arquivo para carregar (disponibilidades): " + arquivoCSV.getAbsolutePath());

    if (!arquivoCSV.exists()) {
        System.out.println("Arquivo de disponibilidades não encontrado, tentando criar: " + caminhoArquivo);
        try {
            File diretorioPai = arquivoCSV.getParentFile();
            if (diretorioPai != null && !diretorioPai.exists()) {
                diretorioPai.mkdirs();
            }
            if (arquivoCSV.createNewFile()) {
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(arquivoCSV))) {
                    bw.write("id_disponibilidade,id_espaco,dia_semana,hora_inicio,hora_fim");
                    bw.newLine();
                    System.out.println("Arquivo disponibilidades.csv criado com sucesso com cabeçalho.");
                }
            } else {
                System.err.println("Não foi possível criar o arquivo disponibilidades.csv (createNewFile retornou false).");
            }
        } catch (IOException e) {
            System.err.println("Erro CRÍTICO ao tentar criar o arquivo disponibilidades.csv: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro CRÍTICO ao criar arquivo de disponibilidades: " + e.getMessage(), "Erro de Criação", JOptionPane.ERROR_MESSAGE);
        }
        System.out.println("Retornando lista de disponibilidades vazia após tentativa de criação.");
        return disponibilidades;
    }

    System.out.println("Arquivo de disponibilidades encontrado. Tentando ler: " + caminhoArquivo);
    int contadorLinhasLidas = 0;
    int contadorDisponibilidadesCriadas = 0;

    try (BufferedReader br = new BufferedReader(new FileReader(arquivoCSV))) {
        String linha;
        String cabecalho = br.readLine();
        contadorLinhasLidas++;
        System.out.println("Cabeçalho lido (disponibilidades): " + cabecalho);

        if (cabecalho == null || !cabecalho.equals("id_disponibilidade,id_espaco,dia_semana,hora_inicio,hora_fim")) {
             System.err.println("Arquivo disponibilidades.csv está vazio ou com cabeçalho inválido. Tratando como lista vazia.");
             return disponibilidades;
        }

        while ((linha = br.readLine()) != null) {
            contadorLinhasLidas++;
            System.out.println("Lendo linha " + contadorLinhasLidas + " (disponibilidades): " + linha);
            if (linha.trim().isEmpty()) {
                System.out.println("Linha " + contadorLinhasLidas + " (disponibilidades) está vazia, pulando.");
                continue;
            }
            String[] dados = linha.split(SEPARADOR_CSV, -1);

            if (dados.length >= 5) {
                try {
                    String idDisponibilidade = dados[0].trim();
                    String idEspaco = dados[1].trim();
                    String diaStr = dados[2].trim();
                    String horaInicioStr = dados[3].trim();
                    String horaFimStr = dados[4].trim();

                    System.out.println(String.format("Dados parseados da linha %d (disponibilidades): ID_Disp=%s, ID_Espaco=%s, Dia=%s, HrInicio=%s, HrFim=%s",
                                                     contadorLinhasLidas, idDisponibilidade, idEspaco, diaStr, horaInicioStr, horaFimStr));

                    DiaDaSemana dia = DiaDaSemana.fromString(diaStr);
                    Date horaInicio = FORMATO_HORA_CSV.parse(horaInicioStr);
                    Date horaFim = FORMATO_HORA_CSV.parse(horaFimStr);

                    Espaco espacoAssociado = null;
                    if (todosOsEspacos == null) {
                        System.err.println("Lista 'todosOsEspacos' é nula em carregarDisponibilidades. Não é possível associar espaços.");
                        continue;
                    }
                    for (Espaco esp : todosOsEspacos) {
                        if (esp.getId().equals(idEspaco)) {
                            espacoAssociado = esp;
                            break;
                        }
                    }

                    if (espacoAssociado != null && dia != null) {
                        DisponibilidadeEspaco disp = new DisponibilidadeEspaco(idDisponibilidade, espacoAssociado, dia, horaInicio, horaFim);
                        disponibilidades.add(disp);
                        contadorDisponibilidadesCriadas++;
                        System.out.println("Disponibilidade criada e adicionada: " + disp.toString());
                    } else {
                        if (espacoAssociado == null) System.err.println("Espaço com ID '" + idEspaco + "' não encontrado na lista 'todosOsEspacos' para disponibilidade na linha: " + linha);
                        if (dia == null) System.err.println("Dia da semana inválido '" + diaStr + "' na linha: " + linha + ". Use o nome do enum (ex: SEGUNDA).");
                    }
                } catch (ParseException e) {
                    System.err.println("Erro ao parsear hora em disponibilidades.csv: " + e.getMessage() + " na linha: " + linha);
                } catch (Exception e) {
                    System.err.println("Erro inesperado ao processar linha de disponibilidade: " + linha + " - " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                 System.err.println("Linha mal formatada em disponibilidades.csv (partes < 5): " + linha);
            }
        }
    } catch (IOException e) {
        System.err.println("Erro CRÍTICO ao ler o arquivo de disponibilidades CSV: " + e.getMessage());
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Erro CRÍTICO ao carregar disponibilidades: " + e.getMessage(), "Erro de Leitura", JOptionPane.ERROR_MESSAGE);
    }
    System.out.println(">>> carregarDisponibilidadesDoCSV concluído. Total de linhas lidas: " + contadorLinhasLidas +
                       ". Disponibilidades criadas: " + contadorDisponibilidadesCriadas);
    return disponibilidades;
}

public static void salvarDisponibilidadesNoCSV(String caminhoArquivo, List<DisponibilidadeEspaco> disponibilidades) {
    System.out.println(">>> Iniciando salvarDisponibilidadesNoCSV para: " + caminhoArquivo);
    if (disponibilidades == null) {
        System.out.println("Lista de disponibilidades é nula. Nada a salvar.");
        return;
    }
    System.out.println("Número de registros de disponibilidade para salvar: " + disponibilidades.size());

    File arquivo = new File(caminhoArquivo);
    System.out.println("Caminho absoluto do arquivo para salvar: " + arquivo.getAbsolutePath());

    try (BufferedWriter bw = new BufferedWriter(new FileWriter(arquivo))) { // FileWriter por padrão sobrescreve o arquivo
        bw.write("id_disponibilidade,id_espaco,dia_semana,hora_inicio,hora_fim");
        bw.newLine();
        System.out.println("Cabeçalho escrito no CSV (disponibilidades).");

        if (disponibilidades.isEmpty()) {
            System.out.println("Lista de disponibilidades está vazia. Apenas o cabeçalho foi salvo.");
        }

        for (DisponibilidadeEspaco disp : disponibilidades) {
            if (disp == null || disp.getEspaco() == null || disp.getDiaDaSemana() == null || disp.getHoraInicioDisponivel() == null || disp.getHoraFimDisponivel() == null) {
                System.err.println("Registro de disponibilidade inválido encontrado (null ou campos internos null) ao salvar: " + disp);
                continue;
            }

            StringJoiner sj = new StringJoiner(SEPARADOR_CSV);
            // Garantir que mesmo um ID nulo no objeto seja tratado (embora não devesse acontecer com UUID)
            sj.add(disp.getId() != null ? disp.getId() : "ID_NULO_AO_SALVAR_" + System.currentTimeMillis());
            sj.add(disp.getEspaco().getId());
            sj.add(disp.getDiaDaSemana().name());
            sj.add(FORMATO_HORA_CSV.format(disp.getHoraInicioDisponivel()));
            sj.add(FORMATO_HORA_CSV.format(disp.getHoraFimDisponivel()));

            String linhaParaEscrever = sj.toString();
            System.out.println("Salvando linha no CSV (disponibilidades): " + linhaParaEscrever);
            bw.write(linhaParaEscrever);
            bw.newLine();
        }
        bw.flush();
        System.out.println(">>> salvarDisponibilidadesNoCSV concluído com sucesso para: " + caminhoArquivo);

    } catch (IOException e) {
        System.err.println("Erro CRÍTICO ao salvar o arquivo de disponibilidades CSV: " + e.getMessage());
        e.printStackTrace();
        JOptionPane.showMessageDialog(null,
                                      "Erro CRÍTICO ao salvar dados de disponibilidade: " + e.getMessage() +
                                      "\nVerifique o console para mais detalhes.",
                                      "Erro de Escrita no Arquivo",
                                      JOptionPane.ERROR_MESSAGE);
    } catch (NullPointerException npe) {
        System.err.println("Erro de NullPointerException em salvarDisponibilidadesNoCSV: " + npe.getMessage());
        npe.printStackTrace();
         JOptionPane.showMessageDialog(null,
                                      "Erro interno (NullPointerException) ao preparar dados de disponibilidade para salvar." +
                                      "\nVerifique o console para mais detalhes.",
                                      "Erro Interno",
                                      JOptionPane.ERROR_MESSAGE);
    } catch (Exception ex) {
        System.err.println("Erro inesperado em salvarDisponibilidadesNoCSV: " + ex.getMessage());
        ex.printStackTrace();
         JOptionPane.showMessageDialog(null,
                                      "Erro inesperado ao salvar dados de disponibilidade." +
                                      "\nVerifique o console para mais detalhes.",
                                      "Erro Inesperado",
                                      JOptionPane.ERROR_MESSAGE);
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
