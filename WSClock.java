import java.util.List;

public class WSClock {

    /**
     * executar: tau é o limiar de idade (usei c como default)
     */
    public static int executar(List<AcessoPagina> acessos, int totalMolduras, int tau) {
        EstadoMemoria memoria = new EstadoMemoria(totalMolduras);
        int pageFaults = 0;
        int hand = 0;

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

            // WSClock scan
            Integer candidate = null;
            int scanned = 0;
            int frames = memoria.molduras.size();

            while (scanned < frames) {
                Moldura cur = memoria.molduras.get(hand);

                if (cur.R == 1) {
                    // recentemente usado: dar segunda chance
                    cur.R = 0;
                    // não atualizo ultimoAcesso aqui (a decisão do seu enunciado pode variar),
                    // mas podemos manter último acesso como está.
                } else {
                    // R == 0
                    boolean antiga = memoria.paginaAntigaWSClock(cur, acesso.tempo, tau);
                    if (antiga && cur.M == 0) {
                        // ótimo candidato: página velha e não modificada
                        candidate = hand;
                        break;
                    } else if (antiga && cur.M == 1) {
                        // página velha mas modificada: "escreve de volta" (simulação)
                        // limpamos M e atualizamos últimoAcesso para simular escrita
                        cur.M = 0;
                        cur.ultimoAcesso = acesso.tempo;
                    } else {
                        // nem velha nem candidata: guardamos como fallback
                        if (candidate == null) candidate = hand;
                    }
                }

                hand = memoria.avancarPonteiroClock(hand);
                scanned++;
            }

            int victim;
            if (candidate != null) {
                victim = candidate;
            } else {
                // fallback (teoricamente não acontece): escolhe a posição atual do hand
                victim = hand;
            }

            // Substitui
            Moldura alvo = memoria.molduras.get(victim);
            alvo.pagina = acesso.pagina;
            alvo.R = 1;
            alvo.M = acesso.escrita ? 1 : 0;
            alvo.ultimoAcesso = acesso.tempo;

            // ponteiro avança para posição seguinte à substituição
            hand = memoria.avancarPonteiroClock(victim);
        }

        return pageFaults;
    }
}
