import structures.*;
import java.util.*;

public class Tiempo {
    public static void main(String[] args) throws Exception {
        List<String> words = Datos.leerDataset("datasets/words.txt");

        int N = words.size();
        int M = 16;
        int grupo = N / M;

        Trie t = new Trie();

        System.out.println("grupo,tiempo_normalizado(ms/char)");

        int index = 0;
        for (int g = 0; g < M; g++) {
            long inicio = System.nanoTime();
            int charsGrupo = 0;

            for (int i = 0; i < grupo && index < N; i++, index++) {
                String w = words.get(index);
                charsGrupo += w.length();
                t.insert(w);
            }

            long fin = System.nanoTime();
            double tiempoMs = (fin - inicio) / 1e6; // nanosegundos → milisegundos
            double tiempoPorChar = tiempoMs / charsGrupo;

            System.out.printf("%d,%.6f%n", g + 1, tiempoPorChar);
        }
    }
}
