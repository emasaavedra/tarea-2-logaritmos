
import structures.*;
import java.io.*;
import java.util.*;

public class ExperimentacionAutocomplete {

    public static void experimentoAutocomplete(String datasetPath, String nombreDataset, String variante) throws IOException {

        String dirName = "autocomplete_" + variante + "_datasets";
        new File(dirName).mkdirs();

        String outFile = dirName + "/" + nombreDataset + ".csv";
        PrintWriter pw = new PrintWriter(new FileWriter(outFile));
        pw.println("palabras_procesadas,caracteres_totales,porcentaje_caracteres_escritos,tiempo_total_ms,tiempo_por_palabra_ms,tiempo_por_caracter_ms");

        List<String> palabras = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(datasetPath))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                for (String palabra : linea.trim().split("\\s+")) {
                    if (!palabra.isEmpty()) palabras.add(palabra);
                }
            }
        }
        System.out.println("Total de palabras en " + nombreDataset + ": " + palabras.size());

        int L = palabras.size();
        System.out.println("Procesando hasta " + L + " palabras con variante " + variante);

        Trie trie = variante.equals("frecuencia") ? new TrieFrecuencia() : new TrieReciente();
        for (String w : palabras) {
            trie.insert(w);
        }

        List<Integer> checkpoints = new ArrayList<>();
        for (int i = 0; i <= 21; i++) {
            int val = (int) Math.pow(2, i);
            if (val <= L) checkpoints.add(val);
        }
        if (checkpoints.isEmpty() || checkpoints.get(checkpoints.size()-1) != L) {
            if (L > checkpoints.get(checkpoints.size()-1))
                checkpoints.add(L);
            else if (checkpoints.isEmpty())
                checkpoints.add(L);
        }

        long totalChars = 0L;
        long escritosUsuario = 0L;
        long inicioTotal = System.nanoTime();

        int nextCpIdx = 0;
        int nextCheckpoint = checkpoints.get(nextCpIdx);

        for (int i = 0; i < L; i++) {
            String w = palabras.get(i);
            totalChars += w.length();

            Nodo actual = trie.getRoot();
            int llamadasDescend = 0;
            Nodo terminalEncontrado = null;

            for (int j = 0; j < w.length(); j++) {
                char c = w.charAt(j);
                llamadasDescend++;
                actual = trie.descend(actual, c);

                if (actual == null) {
                    escritosUsuario += w.length();
                    break;
                }

                Nodo mejorTerminal = trie.autocomplete(actual);
                if (mejorTerminal != null && mejorTerminal.str != null && mejorTerminal.str.equals(w)) {
                    escritosUsuario += llamadasDescend;
                    terminalEncontrado = mejorTerminal;
                    break;
                }

                if (j == w.length() - 1) {
                    int endIdx = Nodo.indexOf('$');
                    Nodo terminal = null;
                    if (actual != null && endIdx >= 0) terminal = actual.next[endIdx];
                    if (terminal != null && terminal.str != null && terminal.str.equals(w)) {
                        escritosUsuario += w.length();
                        terminalEncontrado = terminal;
                    } else {
                        // no existe la palabra
                        escritosUsuario += w.length();
                    }
                }
            }

            if (terminalEncontrado != null) {
                trie.update_priority(terminalEncontrado);
            }


            if (i + 1 == nextCheckpoint) {
                long ahora = System.nanoTime();
                double elapsedMs = (ahora - inicioTotal) / 1e6;
                double porcentajeEscritas = 100.0 * ((double) escritosUsuario / (double) totalChars);

                double tiempoPorPalabra = elapsedMs / (i + 1);
                double tiempoPorCaracter = elapsedMs / (double) totalChars;

                pw.printf(Locale.US, "%d,%d,%.6f,%.3f,%.6f,%.9f%n",
                        i + 1, totalChars, porcentajeEscritas, elapsedMs, tiempoPorPalabra, tiempoPorCaracter);

                System.out.printf(Locale.US, "  %d palabras → %.4f%% caracteres escritos (t=%.3f ms)%n",
                        i + 1, porcentajeEscritas, elapsedMs);


                nextCpIdx++;
                if (nextCpIdx < checkpoints.size()) nextCheckpoint = checkpoints.get(nextCpIdx);
                else nextCheckpoint = Integer.MAX_VALUE;
            }
        }

        pw.close();
        System.out.println("Resultados guardados en: " + outFile);
    }

    public static void main(String[] args) throws IOException {
        String basePath = "datasets/";
        String[] archivos = {"wikipedia.txt", "random.txt", "random_with_distribution.txt"};

        for (String nombre : archivos) {
            String datasetPath = basePath + nombre;

            experimentoAutocomplete(datasetPath, nombre, "frecuencia");
            experimentoAutocomplete(datasetPath, nombre, "reciente");
        }

        System.out.println("✅ Experimentos de autocompletado completados.");
    }
}
