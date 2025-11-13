import java.io.*;
import java.util.*;

public class Datos {

    // Dado el path de un archivo
    // Se genera una lista de Strings, siguiendo la estructura del archivo
    // Se transcribe en minúsculas
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
}
