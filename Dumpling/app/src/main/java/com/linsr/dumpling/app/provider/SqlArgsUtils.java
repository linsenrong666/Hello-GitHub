package com.linsr.dumpling.app.provider;

import java.util.Arrays;

/**
 * Created by Piyao on 06/12/2015.
 */
public class SqlArgsUtils {

    /**
     * Format the selection & selectionArgs into a single where clause
     *
     * @param selection     - the selections
     * @param selectionArgs - the args for the selection
     * @return - a formatted where clause (exclude the where keyword)
     * @throws IllegalArgumentException - the passed in selection & selectionArgs doesn't follow the rule described below
     *                                  <p/>
     *                                  NOTE: selection must follow the format like: "A = ? OP B = ? OP C = ?" and all args must be set in selectionArgs
     */
    public static synchronized String getFormattedSelection(String selection, String[] selectionArgs) throws IllegalArgumentException {

        if (selection == null || selectionArgs == null) {
            return null;
        }
        // Only support = format style like: "A = ? OP B = ? OP C = ?" currently
        // all args must be set in selectionArgs

        String[] selections = selection.split("(=)|(<>)");
        if ( selections.length > 0
                && selections.length == selectionArgs.length + 1) {
            StringBuilder formattedSelection = new StringBuilder();
            formattedSelection.append(selection);
            for (int idx = 0; idx < selectionArgs.length; idx++) {
                int idxOfQ = formattedSelection.indexOf("?");
                if (idxOfQ > -1) {
                    formattedSelection.replace(idxOfQ, idxOfQ + 1, "'" + selectionArgs[idx] + "'");
                }
            }
            String formattedSelectionString = formattedSelection.toString();
            return formattedSelectionString;
        } else {
            throw new IllegalArgumentException("Not supported format, selection " + selection + ", selectionArgs: " + Arrays.toString(selectionArgs));
        }
    }
}
