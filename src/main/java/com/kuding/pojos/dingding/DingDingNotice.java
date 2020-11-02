package com.kuding.pojos.dingding;

import com.kuding.properties.enums.DingdingTextType;

public class DingDingNotice {

    private DingDingText text;
    private DingDingMarkdown markdown;
    private DingDingAt at;
    private String msgtype = "text";

    public DingDingNotice(DingDingText text, DingDingAt at) {
        this.text = text;
        this.at = at;
        this.msgtype = DingdingTextType.TEXT.getMsgType();
    }

    public DingDingNotice(DingDingMarkdown markdown, DingDingAt at) {
        this.markdown = markdown;
        this.at = at;
        this.msgtype = DingdingTextType.MARKDOWN.getMsgType();
    }

    public DingDingNotice(String title, String text, DingDingAt at) {
        this.markdown = new DingDingMarkdown(title, text);
        this.msgtype = DingdingTextType.MARKDOWN.getMsgType();
        this.at = at;
    }

    public DingDingNotice(String text, DingDingAt at) {
        this.text = new DingDingText(text);
        this.msgtype = DingdingTextType.TEXT.getMsgType();
        this.at = at;
    }

    /**
     * @return the text
     */
    public DingDingText getText() {
        return text;
    }

    /**
     * @param text the text to set
     */
    public void setText(DingDingText text) {
        this.text = text;
    }

    /**
     * @return the at
     */
    public DingDingAt getAt() {
        return at;
    }

    /**
     * @param at the at to set
     */
    public void setAt(DingDingAt at) {
        this.at = at;
    }

    /**
     * @return the msgtype
     */
    public String getMsgtype() {
        return msgtype;
    }

    /**
     * @param msgtype the msgtype to set
     */
    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public DingDingMarkdown getMarkdown() {
        return markdown;
    }

    public void setMarkdown(DingDingMarkdown markdown) {
        this.markdown = markdown;
    }

}
