import java.util.ArrayList;
import java.util.List;

public class EstadoMemoria {

    public List<Moldura> molduras;

    // cria todas as molduras vazias
    public EstadoMemoria(int quantidadeMolduras) {
        molduras = new ArrayList<>();
        for (int i = 0; i < quantidadeMolduras; i++) {
            molduras.add(new Moldura());
        }
    }

    
    // Procura se uma página já está carregada na memória
    public int buscarPagina(int pagina) {
        for (int i = 0; i < molduras.size(); i++) {
            if (molduras.get(i).pagina == pagina) {
                return i; // achou
            }
        }
        return -1; // não achou
    }

    // Verifica se existe moldura vazia (pagina == -1)
    public int buscarMolduraVazia() {
        for (int i = 0; i < molduras.size(); i++) {
            if (molduras.get(i).pagina == -1) {
                return i;
            }
        }
        return -1; // nenhuma moldura vazia
    }

    // Zera o bit R de todas as molduras (usado no NRU)
    public void resetarBitsR() {
        for (Moldura m : molduras) {
            m.R = 0;
        }
    }

    // Clonar a memória (útil para rodar mais de um algoritmo)
    public EstadoMemoria copiar() {
        EstadoMemoria copia = new EstadoMemoria(0);
        copia.molduras = new ArrayList<>();
        for (Moldura m : molduras) {
            copia.molduras.add(m.copiar());
        }
        return copia;
    }

    // Atualiza bits da moldura após um acesso
    public void marcarAcesso(Moldura moldura, AcessoPagina acesso) {
        moldura.R = 1;
        if (acesso.escrita) {
            moldura.M = 1;
        }
        moldura.ultimoAcesso = acesso.tempo;
    }

    // Clock e WSClock

    // Clock: avança ponteiro circularmente
    public int avancarPonteiroClock(int ponteiro) {
        return (ponteiro + 1) % molduras.size();
    }

    // WSClock: verifica se página está "velha"
    public boolean paginaAntigaWSClock(Moldura m, int tempoAtual, int tau) {
        return (tempoAtual - m.ultimoAcesso) > tau;
    }

    // ---------------------------------------------------------------------
    // Para debug (opcional)
    // ---------------------------------------------------------------------
    @Override
    public String toString() {
        return molduras.toString();
    }
}
