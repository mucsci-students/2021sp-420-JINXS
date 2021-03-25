package org.jinxs.umleditor;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class MementoTest {

    @Test
    public void initializeMeme() {
        Memento meme = new Memento();

        assertEquals("After initialization, the number of states in the memento should be 0", 0, meme.numStates());
    }

    @Test
    public void saveToMeme() {
        Memento meme = new Memento();
        meme.saveState("test");

        assertEquals("Meme should contain one state", 1, meme.numStates());
    }

    @Test
    public void loadFromMeme() {
        Memento meme = new Memento();
        meme.saveState("test");

        assertEquals("Meme should contain one state", 1, meme.numStates());

        String state = meme.loadState();

        assertEquals("Loaded state should be \"test\"", "test", state);
        assertEquals("Meme should be empty", 0, meme.numStates());
    }

    @Test
    public void saveMultipleToMeme() {
        Memento meme = new Memento();
        meme.saveState("test1");
        meme.saveState("test2");
        meme.saveState("test3");

        assertEquals("Meme should contain three states", 3, meme.numStates());
    }

    @Test
    public void loadMultipleFromMeme() {
        Memento meme = new Memento();
        meme.saveState("test1");
        meme.saveState("test2");
        meme.saveState("test3");

        assertEquals("Meme should contain three states", 3, meme.numStates());

        String state3 = meme.loadState();

        assertEquals("First loaded state should be \"test3\"", "test3", state3);

        String state2 = meme.loadState();

        assertEquals("First loaded state should be \"test2\"", "test2", state2);

        String state1 = meme.loadState();

        assertEquals("First loaded state should be \"test1\"", "test1", state1);
    }

    @Test
    public void maxOutMeme() {
        Memento meme = new Memento();
        meme.saveState("test1");
        meme.saveState("test2");
        meme.saveState("test3");
        meme.saveState("test4");
        meme.saveState("test5");
        meme.saveState("test6");
        meme.saveState("test7");
        meme.saveState("test8");
        meme.saveState("test9");
        meme.saveState("test10");

        assertEquals("Meme should contain ten states", 10, meme.numStates());

        meme.saveState("test11");

        assertEquals("Meme should still contain ten states", 10, meme.numStates());
    }

    @Test
    public void removeOldestStateFromMeme() {
        Memento meme = new Memento();
        meme.saveState("test1");
        meme.saveState("test2");
        meme.saveState("test3");
        meme.saveState("test4");
        meme.saveState("test5");
        meme.saveState("test6");
        meme.saveState("test7");
        meme.saveState("test8");
        meme.saveState("test9");
        meme.saveState("test10");

        assertEquals("Meme should contain ten states", 10, meme.numStates());

        meme.saveState("test11");

        String newOldestState = "";
        for (int s = 0; s < 10; ++s) {
            newOldestState = meme.loadState();
        }

        assertEquals("Oldest state should now be \"test2\"", "test2", newOldestState);
    }
}
