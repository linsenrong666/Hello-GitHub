package com.linsr.dumpling.app.provider;

import java.util.HashMap;

/**
 * description
 *
 * @author Linsr
 */
public class ColumnMap extends HashMap<String, String> {

    public ColumnMap() {
        put(BaseColumn._ID,BaseColumn._ID);
        put(BaseColumn._LAST_UPDATE_TIME,BaseColumn._LAST_UPDATE_TIME);
        put(BaseColumn._COUNT,BaseColumn._COUNT);
    }

}
