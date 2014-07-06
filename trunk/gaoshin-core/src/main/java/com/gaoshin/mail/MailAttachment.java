package com.gaoshin.mail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.activation.DataHandler;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MailAttachment {

    @XmlElement
    private String contentType;

    @XmlElement
    private String title;

    @XmlMimeType("application/octet-stream")
    private DataHandler imageData;

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentType() {
        return contentType;
    }

    public void setImageData(DataHandler imageData) {
        this.imageData = imageData;
    }

    public DataHandler getImageData() {
        return imageData;
    }

    public byte[] getByteArrayData() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            InputStream is = getImageData().getInputStream();
            while (true) {
                byte[] buff = new byte[1024];
                int len = is.read(buff);
                if (len == -1) {
                    break;
                }
                os.write(buff, 0, len);
            }
        } catch (IOException e) {
        }
        return os.toByteArray();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

}
