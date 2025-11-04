package structures;

public class TrieFrecuencia extends Trie {

    public TrieFrecuencia() {
        new TrieFrecuencia();
    }

    public void update_priority(Nodo v){
        v.priority++;
        propagate(v);
    }
}
