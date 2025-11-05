package structures;

public class TrieFrecuencia extends Trie {

    public TrieFrecuencia() {
        super();
    }

    @Override
    public void update_priority(Nodo v){
        v.priority++;
        propagate(v);
    }
}
