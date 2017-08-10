package com.wep.common.app.gst;

import java.util.ArrayList;

/**
 * Created by RichaA on 6/28/2017.
 */

public class GSTR1_DOCS_Data {
    private int doc_num;
    private ArrayList<GSTR1_DOCS> docs;

    public GSTR1_DOCS_Data(int doc_num, ArrayList<GSTR1_DOCS> docs) {
        this.doc_num = doc_num;
        this.docs = docs;
    }

    public int getDoc_num() {
        return doc_num;
    }

    public void setDoc_num(int doc_num) {
        this.doc_num = doc_num;
    }

    public ArrayList<GSTR1_DOCS> getDocs() {
        return docs;
    }

    public void setDocs(ArrayList<GSTR1_DOCS> docs) {
        this.docs = docs;
    }
}
