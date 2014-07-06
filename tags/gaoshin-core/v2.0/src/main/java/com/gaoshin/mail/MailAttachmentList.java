package com.gaoshin.mail;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MailAttachmentList {
    private List<MailAttachment> attachment = new ArrayList<MailAttachment>();

    public void setAttachment(List<MailAttachment> attachment) {
        this.attachment = attachment;
    }

    public List<MailAttachment> getAttachment() {
        return attachment;
    }
}
