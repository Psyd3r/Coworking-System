package espaco_capita;

public class Espaco {
    private String nome;
    private int capacidade;
    private String descricao;
    private String id; // Opcional: Adicionar um ID único para facilitar a edição/exclusão

    // Construtor principal
    public Espaco(String nome, int capacidade, String descricao) {
        this.nome = nome;
        this.capacidade = capacidade;
        this.descricao = descricao;
        // Gerar um ID simples (pode ser melhorado com UUID ou outra estratégia)
        this.id = java.util.UUID.randomUUID().toString();
    }

    // Construtor que inclui ID (útil se recuperando de uma persistência que já tem ID)
    public Espaco(String id, String nome, int capacidade, String descricao) {
        this.id = id;
        this.nome = nome;
        this.capacidade = capacidade;
        this.descricao = descricao;
    }


    // Getters
    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public String getDescricao() {
        return descricao;
    }

    // Setters
    // Não é comum ter um setId a menos que haja um bom motivo, pois o ID deve ser imutável ou gerenciado pela persistência.
    // public void setId(String id) { this.id = id; }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCapacidade(int capacidade) {
        this.capacidade = capacidade;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return "Espaco{" +
               "id='" + id + '\'' +
               ", nome='" + nome + '\'' +
               ", capacidade=" + capacidade +
               ", descricao='" + descricao + '\'' +
               '}';
    }
}
