package common.android;

public class KeySequence {
    private int[] seq = null;
    private int current = 0;

    public KeySequence(int[] seq) {
        this.seq = seq;
    }

    public boolean keyPressed(int key) {
        if (seq[current] == key) {
            current++;
        } else {
            current = 0;
        }
        boolean found = (current == seq.length);
        if (found) {
            current = 0;
        }
        return found;
    }
}
