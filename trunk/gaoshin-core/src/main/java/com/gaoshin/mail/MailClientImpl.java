package com.gaoshin.mail;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gaoshin.business.ConfigurationService;

@Component
public class MailClientImpl implements MailClient {

    private String smtpHost = "smtp.1and1.com";
    private String pop3Host = "pop.1and1.com";
    private String inboxUserName = "*@25kmiles.com";
    private String inboxPassword = "starword";
    private String outboxUserName = "free@25kmiles.com";
    private String outboxPassword = "wordpass";
    private String defaultFrom = "free@25kmiles.com";
    private int port = 587;

    @Autowired
    public void setConfiguration(ConfigurationService confComponent) {
        smtpHost = confComponent.getString(MailConfig.SmtpHost.name(), smtpHost);
        pop3Host = confComponent.getString(MailConfig.Pop3Host.name(), pop3Host);
        inboxUserName = confComponent.getString(MailConfig.InboxUserName.name(), inboxUserName);
        inboxPassword = confComponent.getString(MailConfig.InboxPassword.name(), inboxPassword);
        outboxUserName = confComponent.getString(MailConfig.OutboxUserName.name(), outboxUserName);
        outboxPassword = confComponent.getString(MailConfig.OutboxPassword.name(), outboxPassword);
        defaultFrom = confComponent.getString(MailConfig.DefaultFrom.name(), defaultFrom);
        port = confComponent.getInt(MailConfig.Port.name(), port);
    }

    @SuppressWarnings("restriction")
    public boolean send(MailRecord mail) {
        boolean sent = false;
        try {
            String from = mail.getSender();
            if ((from == null) || (from.length() == 0)) {
                from = defaultFrom;
            }
            Properties properties = new Properties();
            properties.setProperty("mail.smtp.host", getSmtpHost());
            properties.setProperty("mail.smtp.submitter", inboxUserName);
            properties.setProperty("mail.smtp.auth", "true");
            properties.setProperty("mail.debug", "true");
            properties.setProperty("mail.smtp.port", port + "");
            Session session = Session.getInstance(properties, new Authenticator(outboxUserName, outboxPassword));

            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(from));

            for (String to : mail.getReceipts().split("[,; ]")) {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            }

            message.setSubject(mail.getSubject());

            if (mail.isHtml()) {
                message.setContent(mail.getContent(), "text/html");
            } else {
                message.setContent(mail.getContent(), "text/plain");
            }

            // Send message
            javax.mail.Transport.send(message);

            sent = true;
        } catch (Exception e) {
        }

        return sent;
    }

    private class Authenticator extends javax.mail.Authenticator {
        private final PasswordAuthentication authentication;

        public Authenticator(String userName, String password) {
            authentication = new javax.mail.PasswordAuthentication(userName, password);
        }

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return authentication;
        }
    }

    public void setDefaultFrom(String defaultFrom) {
        this.defaultFrom = defaultFrom;
    }

    public String getDefaultFrom() {
        return defaultFrom;
    }

    public String getUserName() {
        return inboxUserName;
    }

    public void setUserName(String userName) {
        this.inboxUserName = userName;
    }

    public String getPassword() {
        return inboxPassword;
    }

    public void setPassword(String password) {
        this.inboxPassword = password;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    private String getPlainText(Message msg) throws IOException, MessagingException {
        Object content = msg.getContent();
        if (content instanceof MimeMultipart) {
            return getPlainText((MimeMultipart) content);
        } else {
            return content.toString();
        }

    }

    private MailAttachmentList getImages(Message msg) throws IOException, MessagingException {
        Object content = msg.getContent();
        if (content instanceof MimeMultipart) {
            return getImages((MimeMultipart) content);
        } else {
            return null;
        }
    }

    private String getPlainText(MimeMultipart mmp) throws IOException, MessagingException {
        StringBuilder sb = new StringBuilder();
        getPlainText(sb, mmp);
        return sb.toString();
    }

    private MailAttachmentList getImages(MimeMultipart mmp) throws IOException, MessagingException {
        MailAttachmentList list = new MailAttachmentList();
        getImages(list, mmp);
        return list;
    }

    private void getPlainText(StringBuilder sb, MimeMultipart mmp) throws IOException, MessagingException {
        for (int i = 0; i < mmp.getCount(); i++) {
            BodyPart body = mmp.getBodyPart(i);
            String contentType = body.getContentType();
            if (contentType.indexOf("text/plain") != -1) {
                if (sb.length() > 0) {
                    sb.append("\n");
                }
                sb.append(body.getContent().toString());
            } else if (contentType.indexOf("multipart") != -1) {
                getPlainText(sb, (MimeMultipart) body.getContent());
            }
        }
    }

    private void getImages(MailAttachmentList list, MimeMultipart mmp) throws IOException, MessagingException {
        for (int i = 0; i < mmp.getCount(); i++) {
            BodyPart body = mmp.getBodyPart(i);
            String contentType = body.getContentType();
            if (contentType.startsWith("image/")) {
                MailAttachment attachment = new MailAttachment();
                attachment.setContentType(contentType);
                DataHandler dataHandler = body.getDataHandler();
                attachment.setImageData(dataHandler);
                attachment.setTitle(body.getFileName());
                list.getAttachment().add(attachment);
            } else if (contentType.indexOf("multipart") != -1) {
                getImages(list, (MimeMultipart) body.getContent());
            }
        }
    }

    private MailMessage buildMailMessage(Message msg) throws MessagingException, IOException {
        MailMessage mm = new MailMessage();
        InternetAddress addr = (InternetAddress) msg.getFrom()[0];
        mm.setFrom(addr.getAddress());
        mm.setUserName(addr.getPersonal());
        InternetAddress to = (InternetAddress) msg.getRecipients(RecipientType.TO)[0];
        mm.setToString(to.getAddress());
        mm.setSubject(msg.getSubject());
        mm.setContent(getPlainText(msg));
        mm.setAttachments(getImages(msg));
        return mm;
    }

    public ArrayList<MailMessage> read() {
        ArrayList<MailMessage> mailList = new ArrayList<MailMessage>();
        Properties properties = new Properties();
        properties.setProperty("mail.pop3.host", getPop3Host());
        Session session = Session.getInstance(properties, new Authenticator(inboxUserName, inboxPassword));
        try {
            Store store = session.getStore("pop3");
            store.connect();
            Folder folder = store.getFolder("INBOX");
            folder.open(Folder.READ_WRITE);
            Message[] messageList = folder.getMessages();
            for (Message msg : messageList) {
                MailMessage mm = buildMailMessage(msg);
                mailList.add(mm);
                msg.setFlag(Flag.DELETED, true);
            }
            folder.close(true);
            store.close();
        } catch (Exception e) {
        }
        return mailList;
    }

    public void setSmtpHost(String smtpHost) {
        this.smtpHost = smtpHost;
    }

    public String getSmtpHost() {
        return smtpHost;
    }

    public void setPop3Host(String pop3Host) {
        this.pop3Host = pop3Host;
    }

    public String getPop3Host() {
        return pop3Host;
    }

    public static void main(String[] args) throws IOException {
        MailClientImpl client = new MailClientImpl();

        MailRecord mr = new MailRecord();
        mr.setReceipts("zhangyongjiang@yahoo.com");
        mr.setSubject("test subject");
        mr.setContent("hello");
        client.send(mr);

        ArrayList<MailMessage> msgList = client.read();
        for (MailMessage msg : msgList) {
            System.out.println(msg.getUserName());
            System.out.println(msg.getFrom());
            System.out.println(msg.getSubject());
            System.out.println(msg.getContent());
            if (msg.getAttachments() != null) {
                for (MailAttachment att : msg.getAttachments().getAttachment()) {
                    System.out.println("write to file " + att.getTitle());
                    FileOutputStream fos = new FileOutputStream("/temp/" + att.getTitle());
                    InputStream is = att.getImageData().getInputStream();
                    while (true) {
                        byte[] buff = new byte[1024];
                        int len = is.read(buff);
                        if (len == -1) {
                            break;
                        }
                        fos.write(buff, 0, len);
                    }
                }
            }
        }
    }

}
