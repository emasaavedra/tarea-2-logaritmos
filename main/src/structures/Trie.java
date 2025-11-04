package structures;

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

                this.numeroNodos++;
            }
            n.next.put(c, nodo);
            n = nodo;
        }
        Nodo ultimoNodo = n.next.get("$");
        if (ultimoNodo == null) {ultimoNodo = new Nodo();}
        n.next.put('$', ultimoNodo);

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
                padre.best_terminal = actual;
            } else {
                break;
            }
            actual = padre;
        }
    }
}
