import java.nio.file.*;
import java.util.List;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        List<String> arquivos = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            arquivos.add(String.format("TESTE-%02d.txt", i));
        }

        for (String arquivo : arquivos) {
            Path inPath = Paths.get(arquivo);
            if (!Files.exists(inPath)) {
                System.out.println("Arquivo não encontrado (pulando): " + arquivo);
                continue;
            }

            try {
                LeitorDeArquivos.DadosEntrada dados = LeitorDeArquivos.lerArquivo(arquivo);

                int opt = Otimo.executar(dados.acessos, dados.totalMolduras);
                int nru = NRU.executar(dados.acessos, dados.totalMolduras, dados.cicloResetR);
                int relo = Relogio.executar(dados.acessos, dados.totalMolduras);
                int ws = WSClock.executar(dados.acessos, dados.totalMolduras, dados.cicloResetR); // tau = c

                List<String> outLines = List.of(
                    String.valueOf(opt),
                    String.valueOf(nru),
                    String.valueOf(relo),
                    String.valueOf(ws)
                );

                String outName = arquivo.replace(".txt", "-RESULTADO.txt");
                Files.write(Paths.get(outName), outLines);
                System.out.println("Processado: " + arquivo + " -> " + outName);

            } catch (Exception e) {
                System.out.println("Erro processando " + arquivo + ": " + e.getMessage());
                e.printStackTrace();
            }
        }

        System.out.println("Concluído.");
    }
}
