package besucha.backend.model.emailer;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.Properties;
import java.util.Scanner;

/**
 * Child of Authenticator designed for Trinity Authentication
 */
public class TrinityAuthenticator extends Authenticator implements AutoCloseable {
    private PasswordAuthentication auth;
    private final Properties propMap;
    private Scanner reader;

    /**
     * Prompts for both UserName and Password
     */
    public TrinityAuthenticator(){
        this(null, null);
    }

    /**
     * Prompts for only password
     * @param userName
     */
    public TrinityAuthenticator(String userName){
        this(userName, null);
    }

    /**
     * Creates a new Authenticator using 
     * @param userName
     * @param password
     */
    public TrinityAuthenticator(String userName, String password){
        super();
        reader = new Scanner(System.in);
        String usr;
        if (userName == null){
            usr = this.pollForUserName();
        } else {
            usr = userName;
        }

        if (password == null){
            this.auth = new PasswordAuthentication(usr, this.pollForPassword());
        } else {
            this.auth = new PasswordAuthentication(usr, password);
        }
        this.propMap = this.constructPropMap();
    }

    /**
     * Returns the contained PasswordAuthentication object
     * @return this.auth
     */
    protected PasswordAuthentication getPasswordAuthentication() {
        return this.auth;
    }

    /**
     * Prompts user to provide a user name
     * @return the username
     */
    protected final String pollForUserName(){
        //String str;
        //Scanner s = new Scanner (System.in);

        System.out.println("Enter Trinity user name + \"@trincoll.edu\" :"); 
        return reader.nextLine();
    }

    /**
     * Prompts the user for their password
     * @return the password
     */
    protected final String pollForPassword(){
        //String str;
        //Scanner s = new Scanner (System.in);

        System.out.println("Enter password:"); 
        return reader.nextLine();
    }

    /**
     * Creates the prop map needed to auth as a Trinity member
     * @return
     */
    protected final Properties constructPropMap(){
        Properties props = new Properties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.office365.com");
        props.put("mail.smtp.port", "587");
        return props;
    }

    /**
     * Getter for the property field of the Authenticator
     * @return the properties of the object.
     */
    public Properties getProperties(){
        return this.propMap;
    }

    /**
     * Returns the username provided as an InternetAddress
     * @return the user name
     */
    public InternetAddress userNameAsInternetAddress(){
        InternetAddress ia;
        try {
            ia = new InternetAddress(this.auth.getUserName());
        } catch (AddressException e) {
            throw new RuntimeException("Unable to create InternetAddress from username: " + this.auth.getUserName() + "\n" + e);
        }
        return ia;
    }

    @Override
    public void close() throws Exception {
        if (this.reader != null){
            this.reader.close();
        }
    }
}
