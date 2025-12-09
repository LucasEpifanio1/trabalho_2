import java.util.List;

public class Otimo {

    public static int executar(List<AcessoPagina> acessos, int totalMolduras) {

        EstadoMemoria memoria = new EstadoMemoria(totalMolduras);

        int pageFaults = 0;

        for (int i = 0; i < acessos.size(); i++) {
            AcessoPagina acesso = acessos.get(i);

            // Verificar se página já está na memória
            int indice = memoria.buscarPagina(acesso.pagina);
            if (indice != -1) {
                memoria.marcarAcesso(memoria.molduras.get(indice), acesso);
                continue;
            }

            pageFaults++;

            // Moldura vazia?
            int vazia = memoria.buscarMolduraVazia();
            if (vazia != -1) {
                Moldura m = memoria.molduras.get(vazia);
                m.pagina = acesso.pagina;
                memoria.marcarAcesso(m, acesso);
                continue;
            }

            // Sr nao tem espaço, selecionar página para substituir
            int indiceSubstituir = selecionarPagina(memoria, acessos, i);

            Moldura alvo = memoria.molduras.get(indiceSubstituir);
            alvo.pagina = acesso.pagina;
            memoria.marcarAcesso(alvo, acesso);
        }

        return pageFaults;
    }

   
    private static int selecionarPagina(EstadoMemoria memoria, List<AcessoPagina> acessos, int posicaoAtual) {

        int indiceEscolhido = -1;
        int maiorDistancia = -1;

        for (int i = 0; i < memoria.molduras.size(); i++) {

            int pagina = memoria.molduras.get(i).pagina;
            int distanciaFutura = Integer.MAX_VALUE; // assume que nunca mais será usada

            // Busca futura
            for (int j = posicaoAtual + 1; j < acessos.size(); j++) {
                if (acessos.get(j).pagina == pagina) {
                    distanciaFutura = j;
                    break;
                }
            }

            // A página que demora mais para voltar substituição ideal
            if (distanciaFutura > maiorDistancia) {
                maiorDistancia = distanciaFutura;
                indiceEscolhido = i;
            }
        }

        return indiceEscolhido;
    }
}
