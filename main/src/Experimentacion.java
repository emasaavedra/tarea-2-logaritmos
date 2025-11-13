import structures.*;

import java.io.*;
import java.util.*;

/**
 * Realiza los experimentos y permite debuggear el trie.
 */
public class Experimentacion {

    // Leer dataset desde archivo
    public static List<String> leerDataset(String path) throws IOException {
        List<String> words = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) words.add(line.toLowerCase());
            }
        }
        return words;
    }

    // Generar lista de potencias de dos
    public static List<Integer> potenciasDeDos(int N) {
        List<Integer> potencias = new ArrayList<>();
        int i = 1;
        while (i < N) {
            potencias.add(i);
            i *= 2;
        }
        if (potencias.get(potencias.size() - 1) != N)
            potencias.add(N);
        return potencias;
    }

    // Experimento de consumo de memoria
    public static void experimentoMemoria(List<String> words, String outputPath) throws IOException {
        Trie t = new Trie();
        int totalChars = 0;

        List<Integer> checkpoints = potenciasDeDos(words.size());

        try (PrintWriter pw = new PrintWriter(new FileWriter(outputPath))) {
            pw.println("palabras;nodos_normalizados");
            int nextCheckpoint = checkpoints.remove(0);

            for (int i = 0; i < words.size(); i++) {
                String w = words.get(i);
                t.insert(w);
                totalChars += w.length();

                if (i + 1 == nextCheckpoint) {
                    double normalizado = (double) t.getNumeroNodos() / totalChars;
                    pw.printf("%d;%.6f%n", i + 1, normalizado);
                    if (!checkpoints.isEmpty())
                        nextCheckpoint = checkpoints.remove(0);
                }
            }
        }

        System.out.println("✅ Archivo " + outputPath + " generado correctamente (memoria).");
    }

    // Experimento de tiempo
    public static void experimentoTiempo(List<String> words, String outputPath) throws IOException {
        Trie t = new Trie();
        List<Integer> checkpoints = potenciasDeDos(words.size());

        try (PrintWriter pw = new PrintWriter(new FileWriter(outputPath))) {
            pw.println("palabras;tiempo_normalizado_ms_por_char");

            int prev = 0;
            for (int cp : checkpoints) {
                long inicio = System.nanoTime();
                int charsGrupo = 0;

                for (int i = prev; i < cp; i++) {
                    String w = words.get(i);
                    charsGrupo += w.length();
                    t.insert(w);
                }

                long fin = System.nanoTime();
                double tiempoMs = (fin - inicio) / 1e6;
                double tiempoPorChar = tiempoMs / charsGrupo;
                pw.printf("%d;%.6f%n", cp, tiempoPorChar);

                prev = cp;
            }
        }

        System.out.println("✅ Archivo " + outputPath + " generado correctamente (tiempo).");
    }

    // 🔹 Imprimir el trie completo para depuración
    public static void printNodo(Nodo n, String path, PrintWriter pw) {
        // Mostrar la información del nodo actual
        pw.printf("Nodo: '%s'%n", path);
        if (n.str != null)
            pw.printf("  → terminal = \"%s\"%n", n.str);
        pw.printf("  priority = %d, best_priority = %d%n", n.priority, n.best_priority);

        if (n.best_terminal != null && n.best_terminal.str != null)
            pw.printf("  best_terminal = \"%s\"%n", n.best_terminal.str);

        // Mostrar qué hijos existen
        StringBuilder hijos = new StringBuilder();
        for (int i = 0; i < 27; i++) {
            Nodo child = n.next[i];
            if (child != null) {
                char c = Nodo.charOf(i);
                hijos.append(c).append(' ');
            }
        }
        pw.printf("  hijos: %s%n", hijos.length() > 0 ? hijos.toString().trim() : "(sin hijos)");
        pw.println();

// Llamada recursiva a los hijos no nulos
        for (int i = 0; i < 27; i++) {
            Nodo child = n.next[i];
            if (child != null) {
                char c = Nodo.charOf(i);
                printNodo(child, path + c, pw);
            }
        }

    }

    // Guardar el árbol a archivo para debug
    public static void dumpTrie(Trie t, String outputPath) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(outputPath))) {
            pw.println("=== DUMP DEL TRIE ===");
            printNodo(t.getRoot(), "", pw);
        }
        System.out.println("🪵 Trie guardado en " + outputPath);
    }

    // Programa principal
    public static void main(String[] args) throws Exception {
        String datasetPath = "datasets/words.txt";
        List<String> words = leerDataset(datasetPath);
        System.out.println("Cargando dataset con " + words.size() + " palabras...");

        // Ejecutar experimentos
        experimentoMemoria(words, "memoria.csv");
        experimentoTiempo(words, "tiempo.csv");

        Trie t = new Trie();
        for (String w : words) t.insert(w);
        dumpTrie(t, "trie_dump.txt");

        System.out.println("✅ Todos los experimentos finalizados correctamente.");
    }
}
