package besucha.backend.gui;

import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

@Component
public class LoginDialog extends Dialog {
    private TextField usernameField, passwordField;
    private Button submitButton;
    private Label instructions;
    private GuiManager gm;

    public LoginDialog(GuiManager gm){
        super(new Frame());
        this.gm = gm;
        this.initialize();
    }

    public void enableEmailButton(){
        this.submitButton.setEnabled(true);
    }

    private void initialize(){
        this.setTitle(gm.getTitle());
        this.addWindowListener(new ExtWindowAdapter());

        this.resize();
        this.setLayout(this.generateLayout());
    }

    private void disableEmailButton(){
        this.submitButton.setEnabled(false);
    }

    private GridBagLayout generateLayout(){
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();

        instructions = new WrappingLabel("Please enter Trinity username then password");
        usernameField = new TextField();
        passwordField = new TextField();
        submitButton = new Button("Submit");

        instructions.setAlignment(Label.CENTER);

        usernameField.setText("username");

        passwordField.setText("password");
        passwordField.setEchoChar('*');

        submitButton.addActionListener(new EmailButtonActionListener());

        //gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridy = 1;
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;
        gbl.setConstraints(instructions, gbc);
        this.add(instructions);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 2;
        gbc.weightx = 0.1;
        gbc.weighty = 0.1;
        gbl.setConstraints(usernameField, gbc);
        this.add(usernameField);

        gbc.gridy = 3;
        gbl.setConstraints(passwordField, gbc);
        this.add(passwordField);

        gbc.fill = GridBagConstraints.NONE;
        gbc.gridy = 4;
        gbl.setConstraints(submitButton, gbc);
        this.add(submitButton);

        return gbl;
    }

    private void resize(){
        DimensionManager dm = new DimensionManager();
        dm.setSquareDimensions(this, 0.3);
        this.setFont(new Font("Cambria", Font.PLAIN, (int)dm.getY(0.02)));
    }

    private class ExtWindowAdapter extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            dispose(); // Maybe improper and should make GuiManager method
        }
    }

    private class EmailButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent arg0) {
            disableEmailButton();
            gm.sendEmails(usernameField.getText(), passwordField.getText());
            this.clearPasswordField();
        }

        private void clearPasswordField(){
            String tempText = "-";
            for (int i = 0; i < passwordField.getText().length(); i++){
                tempText += (char)0; 
            }
            passwordField.setText(tempText); //replace existing chars + 1 w/ null garbage
            passwordField.setText(""); // "clear" the box
            // originally entered password should now be effectively purged from memory?
        }
    }
}
