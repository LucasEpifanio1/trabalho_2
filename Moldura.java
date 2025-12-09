public class Moldura{

    public int pagina;        // Número da página carregada 
    public int R;             // Bit de Referência 
    public int M;             // Bit de Modificação 
    public int ultimoAcesso;  // Momento do último acesso 

    public Moldura() {
        this.pagina = -1;     // -1 significa moldura vazia
        this.R = 0;
        this.M = 0;
        this.ultimoAcesso = 0;
    }

    // Método para copiar molduras 
    public Moldura copiar() {
        Moldura m = new Moldura();
        m.pagina = this.pagina;
        m.R = this.R;
        m.M = this.M;
        m.ultimoAcesso = this.ultimoAcesso;
        return m;
    }

    @Override
    public String toString() {
        return "[pag=" + pagina + ", R=" + R + ", M=" + M + ", ultimo=" + ultimoAcesso + "]";
    }
}
 

