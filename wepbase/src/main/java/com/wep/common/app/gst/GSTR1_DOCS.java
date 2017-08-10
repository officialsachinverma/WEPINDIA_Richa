package com.wep.common.app.gst;

/**
 * Created by RichaA on 6/28/2017.
 */

public class GSTR1_DOCS  {
    private int num;
    private String from;
    private String to;
    private int totnum;
    private int cancel;
    private int net_issue;

    public GSTR1_DOCS(int num, String from, String to, int totnum, int cancel, int net_issue) {
        this.num = num;
        this.from = from;
        this.to = to;
        this.totnum = totnum;
        this.cancel = cancel;
        this.net_issue = net_issue;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public int getTotnum() {
        return totnum;
    }

    public void setTotnum(int totnum) {
        this.totnum = totnum;
    }

    public int getCancel() {
        return cancel;
    }

    public void setCancel(int cancel) {
        this.cancel = cancel;
    }

    public int getNet_issue() {
        return net_issue;
    }

    public void setNet_issue(int net_issue) {
        this.net_issue = net_issue;
    }
}
