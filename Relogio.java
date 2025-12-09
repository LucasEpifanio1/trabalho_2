import java.util.List;

public class Relogio {

    public static int executar(List<AcessoPagina> acessos, int totalMolduras) {
        EstadoMemoria memoria = new EstadoMemoria(totalMolduras);
        int pageFaults = 0;
        int ponteiro = 0; // ponteiro do relógio

        for (AcessoPagina acesso : acessos) {
            int idx = memoria.buscarPagina(acesso.pagina);
            if (idx != -1) {
                // HIT
                memoria.marcarAcesso(memoria.molduras.get(idx), acesso);
                continue;
            }

            // MISS
            pageFaults++;

            // Verifica moldura vazia
            int vazia = memoria.buscarMolduraVazia();
            if (vazia != -1) {
                Moldura m = memoria.molduras.get(vazia);
                m.pagina = acesso.pagina;
                memoria.marcarAcesso(m, acesso);
                continue;
            }

            // Não há espaço livre -> usar algoritmo do relógio (segunda chance)
            while (true) {
                Moldura cur = memoria.molduras.get(ponteiro);
                if (cur.R == 0) {
                    // evict
                    cur.pagina = acesso.pagina;
                    cur.R = 1;
                    cur.M = acesso.escrita ? 1 : 0;
                    cur.ultimoAcesso = acesso.tempo;
                    // avança ponteiro para próximo (posição após o substituído)
                    ponteiro = memoria.avancarPonteiroClock(ponteiro);
                    break;
                } else {
                    // dá segunda chance: limpa R e avança
                    cur.R = 0;
                    ponteiro = memoria.avancarPonteiroClock(ponteiro);
                }
            }
        }

        return pageFaults;
    }
}
