package besucha.backend.model.emailer;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;

public class Emailer {
    private Authenticator auth;
    private Properties propMap;
    private InternetAddress iAddr;
    
    public Emailer(TrinityAuthenticator ta){
        this(ta, ta.getProperties(), ta.userNameAsInternetAddress());
    }

    public Emailer(Authenticator a, Properties p, String userName) throws AddressException {
        this(a, p, new InternetAddress(userName));
    }

    public Emailer(Authenticator a, Properties p, InternetAddress ia){
        this.auth = a;
        this.propMap = p;
        this.iAddr = ia;
    }

    public void sendEmail(EmailAddress emailAddress, String subject, String message){
        Session newSession = this.setUpSession();
        sendEmail(newSession, emailAddress, subject, message);
    }

    public void sendEmail(EmailAddress emailAddress, String subject, File message) throws IOException {
        Session newSession = this.setUpSession();
        String messageText = Files.readString(Path.of(message.toURI()));
        sendEmail(newSession, emailAddress, subject, messageText);
    }

    public void sendEmailToMailingList(List<EmailAddress> mailingList, String subject, String message){
        Session newSession = this.setUpSession();
        for (EmailAddress ea : mailingList){
            sendEmail(newSession, ea, subject, message);
        }
    }

    public void sendEmailToMailingList(List<EmailAddress> mailingList, String subject, File message) throws IOException {
        for (EmailAddress ea : mailingList){
            sendEmail(ea, subject, message);
        }
    }

    private void sendEmail(Session session, EmailAddress emailAddress, String subject, String message){
        Message emailMessage = new MimeMessage(session);

        try {
            emailMessage.setFrom(iAddr);
            emailMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailAddress.toString()));
            emailMessage.setSubject(subject);
            emailMessage.setText(message);
    
            Transport.send(emailMessage);
    
        } catch (AddressException e) {
            throw new RuntimeException(e.getMessage());
        } catch (MessagingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private Session setUpSession(){
        return Session.getInstance(propMap, auth);
    }
}
