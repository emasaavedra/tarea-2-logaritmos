package structures;

import java.util.Map;

public class Trie {
    private Nodo root;
    private long numeroNodos = 0;

    public  Trie(){
        root = new Nodo();
    }

    /** Función que inserta un String dado en el Trie.
     *
     * Inserta cada caracter en un nodo, si no existe, se crea.
     *
     * @param w String
     */
    public void insert(String w){
        Nodo n = this.root;
        for (int i = 0; i < w.length(); i++) {
            Character c = w.charAt(i);
            Nodo nodo = n.next.get(c);
            if (nodo == null) {
                nodo = new Nodo();
                nodo.parent = n;
                n.next.put(c, nodo);
                this.numeroNodos++;
            }
            n = nodo;
        }
        Nodo terminal = n.next.get('$');
        if (terminal == null) {
            terminal = new Nodo();
            terminal.parent = n;

            n.next.put('$', terminal);
            this.numeroNodos++;
        }

        terminal.str = w;
        terminal.best_terminal = terminal;
        terminal.best_priority = terminal.priority;

    }

    public Nodo descend(Nodo v, Character c){
        return v.next.get(c);
    }

    public Nodo autocomplete(Nodo v){
        return v.best_terminal;
    }

    public void update_priority(Nodo v){}

    /** Propaga las actualizaciones de priority del hijo al padre, hasta la raíz.
     *
     * @param v Nodo
     */
    static void propagate(Nodo v) {
        Nodo actual = v;
        while (actual.parent != null) {
            Nodo padre = actual.parent;

            if (actual.priority > padre.best_priority) {
                padre.best_priority = actual.priority;
                padre.best_terminal = actual.best_terminal != null ? actual.best_terminal : actual;
            } else {
                break;
            }
            actual = padre;
        }
    }

    public void printTrie() {
        System.out.println("=== Trie ===");
        printNodo(this.root, "");
    }

    private void printNodo(Nodo nodo, String prefix) {
        if (nodo == null) return;

        // Si este nodo representa el carácter '$', es un nodo terminal
        if (nodo.str != null) {
            System.out.println(prefix + "Terminal → " + nodo.str +
                    " | priority=" + nodo.priority +
                    " | best=" + (nodo.best_terminal != null ? nodo.best_terminal.str : "null"));
        }

        // Recorremos los hijos
        for (Map.Entry<Character, Nodo> entry : nodo.next.entrySet()) {
            Character c = entry.getKey();
            Nodo child = entry.getValue();

            if (child != null) {
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
