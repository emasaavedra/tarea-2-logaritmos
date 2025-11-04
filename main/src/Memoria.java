import structures.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Memoria {
    public static void main(String[] args) throws IOException {
        /**
        Trie t = new Trie();
        t.insert("apple");
        t.insert("app");
        t.insert("ape");
        t.insert("bat");

        t.printTrie();
         */
        List<String> words = Datos.leerDataset("datasets/words.txt");

        Trie t = new Trie();
        List<Integer> pasos = new ArrayList<>();
        List<Double> nodosNormalizados = new ArrayList<>();

        int totalChars = 0;

        for (int i = 0; i < words.size(); i++) {
            String w = words.get(i);
            t.insert(w);
            totalChars += w.length();

            if (i >= 19) { // desde la palabra 20
                double normalizado = (double) t.getNumeroNodos() / totalChars;
                pasos.add(i + 1);
                nodosNormalizados.add(normalizado);
            }
        }

        // Imprime resultados en CSV
        System.out.println("palabras,nodos_normalizados");
        for (int i = 0; i < pasos.size(); i++) {
            System.out.println(pasos.get(i) + "," + nodosNormalizados.get(i));
        }
    }
}