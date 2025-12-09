import java.io.BufferedReader;
import java.io.FileReader;
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

        BufferedReader br = new BufferedReader(new FileReader(caminho));

        DadosEntrada dados = new DadosEntrada();
        dados.acessos = new ArrayList<>();

        // Lê as três primeiras linhas
        dados.totalPaginas  = Integer.parseInt(br.readLine().trim());
        dados.totalMolduras = Integer.parseInt(br.readLine().trim());
        dados.cicloResetR   = Integer.parseInt(br.readLine().trim());

        // Lê os acessos
        String linha;
        while ((linha = br.readLine()) != null) {
            linha = linha.trim();
            if (linha.isEmpty()) continue;

            String[] partes = linha.split(" ");
            int pagina = Integer.parseInt(partes[0]);
            int tempo  = Integer.parseInt(partes[1]);
            char op    = partes[2].charAt(0);

            dados.acessos.add(new AcessoPagina(pagina, tempo, op));
        }

        br.close();
        return dados;
    }
}