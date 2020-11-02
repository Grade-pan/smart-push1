package com.kuding.properties;

public class EmailExceptionNoticeProperty {

    /**
     * 发件人，默认是通过springboot javamail配置的stmp的用户名
     */
    private String from;

    /**
     * 收件人
     */
    private String[] to;

    /**
     * 抄送
     */
    private String[] cc;

    /**
     * 密抄送
     */
    private String[] bcc;

    /**
     * @return the from
     */
    public String getFrom() {
        return from;
    }

    /**
     * @param from the from to set
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * @return the to
     */
    public String[] getTo() {
        return to;
    }

    /**
     * @param to the to to set
     */
    public void setTo(String[] to) {
        this.to = to;
    }

    /**
     * @return the cc
     */
    public String[] getCc() {
        return cc;
    }

    /**
     * @param cc the cc to set
     */
    public void setCc(String[] cc) {
        this.cc = cc;
    }

    /**
     * @return the bcc
     */
    public String[] getBcc() {
        return bcc;
    }

    /**
     * @param bcc the bcc to set
     */
    public void setBcc(String[] bcc) {
        this.bcc = bcc;
    }

}
