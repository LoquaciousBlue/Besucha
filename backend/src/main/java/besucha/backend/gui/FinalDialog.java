package besucha.backend.gui;

import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

@Component
public class FinalDialog extends Dialog {
    private Label congratsMessage;
    private TextArea resultsMessage;
    private Button confirmationButton;
    private Button emailButton;
    private GuiManager gm;

    public FinalDialog(GuiManager gm){
        super(new Frame());
        this.gm = gm;
        this.initialize();
    }

    public void updateResults(String resultsStr){
        this.removeAll();
        super.setLayout(this.generateLayout(resultsStr));
        this.resize();
        this.validate();
    }

    // NB: Approx 23p per char
    private void initialize(){
        this.setTitle(gm.getTitle());
        this.addWindowListener(this.new ExtWindowAdapter());
        this.resize();
        super.setLayout(this.generateLayout());
    }

    private void resize(){
        DimensionManager dm = new DimensionManager();
        dm.setSquareDimensions(this, 0.7);
        this.setFont(new Font("Cambria", Font.PLAIN, (int)dm.getY(0.02)));
    }

    private GridBagLayout generateLayout(){
        return this.generateLayout("");
    }

    private GridBagLayout generateLayout(String resultsStr){
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        DimensionManager dm = new DimensionManager();

        String congratsStr = "Congratulations. The schedules were successfully created and saved:\nStatistics below";
        congratsMessage = new WrappingLabel(congratsStr, (int)dm.getY(0.7)/23);
        resultsMessage = new TextArea(resultsStr, 1,1, TextArea.SCROLLBARS_NONE);
        confirmationButton = new Button("Save");
        emailButton = new Button("Email");

        congratsMessage.setAlignment(Label.CENTER);
        confirmationButton.addActionListener(new SaveActionListener());
        emailButton.addActionListener(new EmailActionListener());


        gbc.fill = GridBagConstraints.BOTH;
        int padding = (int)(dm.getY(0.01));
        gbc.insets = new Insets(padding,padding,padding,padding);

        gbc.gridwidth = 3;
        gbc.weightx = 0.1;
        gbc.weighty = 0.1;
        gbc.anchor = GridBagConstraints.NORTH;
        gbl.setConstraints(congratsMessage, gbc);
        this.add(congratsMessage);

        gbc.gridy = 2;
        gbc.gridheight = 4;
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;
        gbc.anchor = GridBagConstraints.CENTER;
        gbl.setConstraints(resultsMessage, gbc);
        this.add(resultsMessage);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.1;
        gbc.weighty = 0.1;
        gbc.anchor = GridBagConstraints.SOUTHWEST;
        gbl.setConstraints(confirmationButton, gbc);
        this.add(confirmationButton);

        gbc.gridx = 2;
        gbc.anchor = GridBagConstraints.SOUTHEAST;
        gbl.setConstraints(emailButton, gbc);
        this.add(emailButton);

        return gbl;
    }

    private class ConfirmationActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent arg0) {
            //FinalDialog.this.dispose();
            gm.closeOutAndQuit();
        }
    }

    private class EmailActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent arg0) {
            gm.launchLoginDialog();
        }
    }

    private class ExtWindowAdapter extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            gm.closeOutAndQuit();
        }
    }

    private class SaveActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent arg0){
            confirmationButton.setEnabled(false);
            gm.saveFileToDisk(gm.getResultFile());
            confirmationButton.setEnabled(true);
        }
    }
}
