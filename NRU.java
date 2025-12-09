import java.util.List;

public class NRU {

    public static int executar(List<AcessoPagina> acessos, int totalMolduras, int cicloResetR) {

        EstadoMemoria memoria = new EstadoMemoria(totalMolduras);
        int pageFaults = 0;

        int ultimoReset = 0; // último tempo em que R foi zerado

        for (AcessoPagina acesso : acessos) {

            // Zerar bits R se passaram "c" ciclos
            if (acesso.tempo - ultimoReset >= cicloResetR) {
                memoria.resetarBitsR();
                ultimoReset = acesso.tempo;
            }

            // Verificar se página esta na memoria
            int indice = memoria.buscarPagina(acesso.pagina);

            if (indice != -1) {
                // HIT, atualizar R e M
                memoria.marcarAcesso(memoria.molduras.get(indice), acesso);
                continue;
            }

            // Page fault
            pageFaults++;

            //  Moldura vazia?
            int vazia = memoria.buscarMolduraVazia();
            if (vazia != -1) {
                Moldura m = memoria.molduras.get(vazia);
                m.pagina = acesso.pagina;
                memoria.marcarAcesso(m, acesso);
                continue;
            }

            //  NRU: procurar classe mais baixa disponível
            int indiceSubstituir = selecionarPagina(memoria);

            // Substituição
            Moldura alvo = memoria.molduras.get(indiceSubstituir);
            alvo.pagina = acesso.pagina;
            alvo.R = 1;
            alvo.M = acesso.escrita ? 1 : 0;
            alvo.ultimoAcesso = acesso.tempo;
        }

        return pageFaults;
    }

   

    private static int selecionarPagina(EstadoMemoria memoria) {

        // cada classe guarda os índices das molduras
        List<Integer>[] classes = new List[4];
        for (int i = 0; i < 4; i++) {
            classes[i] = new java.util.ArrayList<>();
        }

        // classificar molduras
        for (int i = 0; i < memoria.molduras.size(); i++) {
            Moldura m = memoria.molduras.get(i);

            int classe = (m.R * 2) + m.M;  // exatamente tabela NRU
            classes[classe].add(i);
        }

        // escolher a menor classe não vazia
        for (int c = 0; c < 4; c++) {
            if (!classes[c].isEmpty()) {
                return classes[c].get(0); // pega a primeira dessa classe
            }
        }

        return 0;
    }
}
