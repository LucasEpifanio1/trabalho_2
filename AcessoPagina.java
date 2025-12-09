// AcessoPagina.java
public class AcessoPagina {
    public int pagina;      // Número da página acessada
    public int tempo;       // Momento t do acesso
    public boolean escrita; // true = W, false = R

    public AcessoPagina(int pagina, int tempo, char operacao) {
        this.pagina = pagina;
        this.tempo = tempo;
        this.escrita = (operacao == 'W');
    }

    @Override
    public String toString() {
        return "Página: " + pagina +
               ", tempo: " + tempo +
               ", operação: " + (escrita ? "W" : "R");
    }
}

