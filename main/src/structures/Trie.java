package structures;

public class Trie {
    private Nodo root;
    private long numeroNodos = 0;

    public Trie() {
        root = new Nodo();
    }

    /** Inserta un String dado en el Trie.
     *  Inserta cada caracter en un nodo, si no existe, se crea.
     */
    public void insert(String w) {
        Nodo n = this.root;

        for (int i = 0; i < w.length(); i++) {
            char c = w.charAt(i);
            int idx = Nodo.indexOf(c);

            if (idx == -1) continue;

            Nodo nodo = n.next[idx];
            if (nodo == null) {
                nodo = new Nodo();
                nodo.parent = n;
                n.next[idx] = nodo;
                this.numeroNodos++;
            }
            n = nodo;
        }

        // Nodo terminal (carácter especial $)
        int endIdx = Nodo.indexOf('$');
        Nodo terminal = n.next[endIdx];
        if (terminal == null) {
            terminal = new Nodo();
            terminal.parent = n;
            n.next[endIdx] = terminal;
            this.numeroNodos++;
        }

        terminal.str = w; // Guarda la palabra completa en el nodo terminal
    }

    /** Retorna el hijo al descender por carácter c. */
    public Nodo descend(Nodo v, Character c) {
        return v.next[Nodo.indexOf(c)];
    }

    /** Retorna el mejor nodo terminal del subárbol. */
    public Nodo autocomplete(Nodo v) {
        return v.best_terminal;
    }

    /** Actualiza la prioridad del nodo terminal (implementado en subclases). */
    public void update_priority(Nodo v) {}

    /** Propaga las actualizaciones de priority hacia la raíz. */
    static void propagate(Nodo v) {
        Nodo actual = v;
        while (actual.parent != null) {
            Nodo padre = actual.parent;

            if (actual.priority > padre.best_priority) {
                padre.best_priority = actual.priority;
                padre.best_terminal =
                        (actual.best_terminal != null) ? actual.best_terminal : actual;
            } else {
                break;
            }
            actual = padre;
        }
    }

    /** Imprime el contenido del Trie (para debugging). */
    public void printTrie() {
        System.out.println("=== Trie ===");
        printNodo(this.root, "");
    }

    private void printNodo(Nodo nodo, String prefix) {
        if (nodo == null) return;

        if (nodo.str != null) {
            System.out.println(prefix + "Terminal → " + nodo.str +
                    " | priority=" + nodo.priority +
                    " | best=" + (nodo.best_terminal != null ? nodo.best_terminal.str : "null"));
        }

        for (int i = 0; i < 27; i++) {
            Nodo child = nodo.next[i];
            if (child != null) {
                char c = Nodo.charOf(i);
                System.out.println(prefix + "─ " + c);
                printNodo(child, prefix + "  ");
            }
        }
    }

    public long getNumeroNodos() {
        return numeroNodos;
    }

    public Nodo getRoot() {
        return this.root;
    }
}
