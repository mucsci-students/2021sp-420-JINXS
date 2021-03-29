package org.jinxs.umleditor;

import java.util.ArrayList;

public class Memento {
    
    // Index 0 is oldest state, index 9 is newest state
    public ArrayList<String> states;

    public Memento() {
        states = new ArrayList<String>(10);
    }

    public void saveState(String state) {
        if (states.size() == 10) {
            states.remove(0);
        }
        states.add(state);
    }

    public String loadState() {
        if (!states.isEmpty()) {
            return states.remove(states.size() - 1);
        }
        return null;
    }

    public void clear() {
        states.clear();
    }

    public int numStates() {
        return states.size();
    }
}
