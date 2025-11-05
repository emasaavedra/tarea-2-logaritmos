package structures;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Nodo {
    Nodo parent;

    // Estructura que mapea caracteres del abecedario a Hijos
    public Nodo[] next;

    public long priority;

    public String str;

    public Nodo best_terminal;

    public long best_priority;

    public Nodo() {
        this.parent = null;

        this.next = new Nodo[27];
        this.priority = 0;
        this.str = null;
        this.best_terminal = null;
        this.best_priority = -1;
    }

    public static int indexOf(char c) {
        if (c == '$') return 26;

        // Normalizamos: minúscula y verificamos que sea una letra válida
        if (c >= 'A' && c <= 'Z') c = Character.toLowerCase(c);

        if (c >= 'a' && c <= 'z') {
            return c - 'a';
        }

        // Si no pertenece al alfabeto, devolvemos -1 (carácter inválido)
        return -1;
    }


    public static char charOf(int i) {
        return i == 26 ? '$' : (char) ('a' + i);
    }

}

