package com.gaoshin.mail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

public class MailParser {
    public static MailRecord parse(String mail) {
        MailRecord mr = new MailRecord();
        BufferedReader br = new BufferedReader(new StringReader(mail));
        StringBuilder sb = new StringBuilder();
        boolean bodyFound = false;
        while (true) {
            String line = null;
            try {
                line = br.readLine();
            } catch (IOException e) {
            }
            if (line == null) {
                break;
            }
            if (bodyFound) {
                sb.append(line).append("\n");
                continue;
            }

            if (line.toLowerCase().startsWith("from:")) {
                String from = line.substring(5).trim();
                if (from.length() > 0) {
                    mr.setReceipts(trim(from));
                }
            } else if (line.toLowerCase().startsWith("to:")) {
                String to = line.substring(3).trim();
                mr.setReceipts(to);
            } else if (line.toLowerCase().startsWith("subject:")) {
                String subject = line.substring(8).trim();
                if (subject.length() > 0) {
                    mr.setSubject(trim(subject));
                }
            } else if (line.toLowerCase().startsWith("cc:")) {
                String cc = line.substring(3).trim();
                mr.setCc(cc);
            } else if (line.toLowerCase().startsWith("body:")) {
                bodyFound = true;
                sb.append(line.substring(5)).append("\n");
            }
        }
        mr.setContent(trim(sb.toString()));
        return mr;
    }

    public static String trim(String s) {
        if (s == null) {
            return s;
        }
        return s.trim();
    }
}
