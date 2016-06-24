package com.linsr.dumpling.note;

/**
 * description
 *
 * @author Linsr
 */
public class NoteManager {

    private static volatile NoteManager mInstance;

    private NoteManager() {
    }

    public static NoteManager getInstance() {
        if (mInstance == null) {
            synchronized (NoteManager.class) {
                if (mInstance == null) {
                    mInstance = new NoteManager();
                }
            }
        }
        return mInstance;
    }

}
