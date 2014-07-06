package common.api.email;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class MailMessage {
    private List<String> to = new ArrayList<String>();
    private String from;
    private String userName;
    private String subject;
    private String content;
    private boolean isHtml = true;

    public MailMessage() {
    }

    public MailMessage(String to, String subject, String content) {
        addTo(to);
        setSubject(subject);
        setContent(content);
    }

    public MailMessage(String to, String subject, String content, boolean approved) {
        addTo(to);
        setSubject(subject);
        setContent(content);
    }

    @XmlElement
    public String getToString() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < to.size(); i++) {
            String s = to.get(i);
            if (i != 0) {
                sb.append(",");
            }
            sb.append(s);
        }
        return sb.toString();
    }

    public void setToString(String s) {
        if (s == null)
            return;
        for (String str : s.split("[ ,;\t\r\n]+")) {
            if (str == null || str.length() == 0)
                continue;
            to.add(str);
        }
    }

    public List<String> getTo() {
        return to;
    }

    public void setTo(List<String> to) {
        this.to = to;
    }

    public void addTo(String to) {
        if (this.to == null) {
            this.to = new ArrayList<String>();
        }
        this.to.add(to);
    }

    @XmlElement
    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    @XmlElement
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @XmlElement
    public String getContent() {
        return content;
    }

    @XmlElement
    public void setContent(String content) {
        this.content = content;
    }

    public void setHtml(boolean isHtml) {
        this.isHtml = isHtml;
    }

    @XmlElement
    public boolean isHtml() {
        return isHtml;
    }

    public void setUserName(String name) {
        this.userName = name;
    }

    @XmlElement
    public String getUserName() {
        return userName;
    }
}
