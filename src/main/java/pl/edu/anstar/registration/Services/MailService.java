package pl.edu.anstar.registration.Services;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;
/**
 * klassa używana do wysyłania maili
 *
 * wysyła receptę na podany adres
 * zawiera email i haslo do konta firmy
 * */
public class MailService {
    static String username ="szamannoeply@gmail.com";
    static String password = "xawjquzgiqzmlabd";
    /**
     * funkcja wysylająca maila
     *
     * @param contentField wiadomość dodana do maila
     * @param titleField    tytuł wiadamości
     * @param recipientsField email na który wysyłamy wiadomość
     * */
    public static void sendEmail(String recipientsField, String titleField, String contentField) throws IOException, EmailException {
        try{
        Email email = new SimpleEmail();
        email.setHostName("smtp.googlemail.com");
        email.setSmtpPort(465);
        email.setAuthenticator(new DefaultAuthenticator(username, password));
        email.setSSLOnConnect(true);
        email.setFrom(username);
        email.setSubject(titleField);
        email.setMsg(contentField);
        email.addTo(recipientsField);
        email.send();

        } catch (EmailException e) {
            System.out.println(e.getMessage());
        }
    }
}
