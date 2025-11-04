package structures;

public class TrieReciente extends Trie{
    int timestamp = 0;
    public TrieReciente(){
        super();
    }

    @Override
    public void update_priority(Nodo v){
        this.timestamp++;
        v.priority = this.timestamp;
        propagate(v);
    }
}
