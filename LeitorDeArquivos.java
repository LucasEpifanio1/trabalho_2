import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LeitorDeArquivos {

    // Classe auxiliar para devolver todos os dados lidos
    public static class DadosEntrada {
        public int totalPaginas;      // p
        public int totalMolduras;     // m
        public int cicloResetR;       // c
        public List<AcessoPagina> acessos; // lista com todos os acessos
    }

    public static DadosEntrada lerArquivo(String caminho) throws Exception {
        DadosEntrada dados = new DadosEntrada();
        dados.acessos = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(caminho))) {

            // Lê as três primeiras linhas obrigatórias (p, m, c)
            String l1 = br.readLine();
            String l2 = br.readLine();
            String l3 = br.readLine();

            if (l1 == null || l2 == null || l3 == null) {
                throw new Exception("Arquivo de entrada incompleto (faltam p, m ou c) em: " + caminho);
            }

            try {
                dados.totalPaginas  = Integer.parseInt(l1.trim());
            } catch (NumberFormatException e) {
                throw new Exception("Erro ao parsear totalPaginas (p) na primeira linha: '" + l1 + "'", e);
            }
            try {
                dados.totalMolduras = Integer.parseInt(l2.trim());
            } catch (NumberFormatException e) {
                throw new Exception("Erro ao parsear totalMolduras (m) na segunda linha: '" + l2 + "'", e);
            }
            try {
                dados.cicloResetR   = Integer.parseInt(l3.trim());
            } catch (NumberFormatException e) {
                throw new Exception("Erro ao parsear cicloResetR (c) na terceira linha: '" + l3 + "'", e);
            }

            // Lê os acessos (restantes)
            String linha;
            int linhaNumero = 4; // já lemos 1-3
            while ((linha = br.readLine()) != null) {
                linhaNumero++;
                if (linha.trim().isEmpty()) {
                    // ignora linhas em branco
                    continue;
                }

                // Divide por qualquer quantidade de espaços/tabs
                String[] partes = linha.trim().split("\\s+");
                if (partes.length < 3) {
                    // linha inválida — apenas pular e avisar no console
                    System.out.println("Aviso: linha " + linhaNumero + " inválida (menos de 3 tokens), ignorando: '" + linha + "'");
                    continue;
                }

                try {
                    int pagina = Integer.parseInt(partes[0]);
                    int tempo  = Integer.parseInt(partes[1]);
                    char op    = partes[2].charAt(0);
                    if (op != 'R' && op != 'W') {
                        System.out.println("Aviso: operação desconhecida na linha " + linhaNumero + ": '" + partsSafe(partes) + "'. Esperado R ou W. Ignorando linha.");
                        continue;
                    }
                    dados.acessos.add(new AcessoPagina(pagina, tempo, op));
                } catch (NumberFormatException nfe) {
                    System.out.println("Aviso: número inválido na linha " + linhaNumero + ", ignorando: '" + linha + "'");
                    continue;
                }
            }

        } catch (IOException ioe) {
            throw new Exception("Erro ao abrir/ler o arquivo: " + caminho + " -> " + ioe.getMessage(), ioe);
        }

        // Log de depuração (pode remover depois)
        System.out.println("Leitura concluída: " + caminho);
        System.out.println("  p (totalPaginas)  = " + dados.totalPaginas);
        System.out.println("  m (totalMolduras) = " + dados.totalMolduras);
        System.out.println("  c (cicloResetR)   = " + dados.cicloResetR);
        System.out.println("  acessos lidos     = " + dados.acessos.size());

        return dados;
    }

    // helper para exibir partes em aviso (evita erro se menor que 3)
    private static String partsSafe(String[] parts) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parts.length && i < 5; i++) {
            if (i > 0) sb.append(" ");
            sb.append(parts[i]);
        }
        return sb.toString();
    }
}
