package structures;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Nodo {
    Nodo parent;

    // Estructura que mapea caracteres del abecedario a Hijos
    Map<Character, Nodo> next = new HashMap<Character, Nodo>();

    long priority;

    String str;

    Nodo best_terminal;

    long best_priority;

    public Nodo() {
        this.parent = null;

        for (char c = 'a'; c <= 'z'; c++) {
            next.put(c, null);
        }
        next.put('$', null);
        this.priority = 0;
        this.str = null;
        this.best_terminal = null;
        this.best_priority = -1;
    }
}

