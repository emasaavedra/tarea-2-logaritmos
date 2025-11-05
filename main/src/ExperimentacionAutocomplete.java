package experimentacion;

import structures.*;
import java.io.*;
import java.util.*;

public class ExperimentacionAutocomplete {

    public static void experimentoAutocomplete(String datasetPath, String nombreDataset, String variante) throws IOException {

        // Crear carpeta de salida según la variante
        String dirName = "autocomplete_" + variante + "_datasets";
        new File(dirName).mkdirs();

        String outFile = dirName + "/" + nombreDataset + ".csv";
        PrintWriter pw = new PrintWriter(new FileWriter(outFile));
        pw.println("palabras_insertadas,caracteres_insertados,nodos");

        // Leer dataset completo a memoria (solo una vez)
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


        int N = palabras.size();
        System.out.println("Insertando hasta " + N + " palabras desde " + nombreDataset + " usando variante " + variante);

        // Bucle en potencias de 2
        for (int exp = 0; exp <= 22; exp++) {
            int n = (int) Math.pow(2, exp);
            if (n > N) break;

            // Crear nuevo trie por iteración (variante según parámetro)
            Trie trie = variante.equals("frecuencia") ? new TrieFrecuencia() : new TrieReciente();

            long totalChars = 0;
            for (int i = 0; i < n; i++) {
                String w = palabras.get(i);
                trie.insert(w);
                totalChars += w.length();
            }

            double normalizado = (double) trie.getNumeroNodos() / totalChars;
            pw.printf(Locale.US, "%d,%d,%.6f%n", n, totalChars, normalizado);
            System.out.printf(Locale.US, "  %d palabras → %.6f nodos/char%n", n, normalizado);

            trie = null; // liberar memoria
            System.gc();
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

        System.out.println("✅ Experimentos completados sin desbordar memoria.");
    }
}
