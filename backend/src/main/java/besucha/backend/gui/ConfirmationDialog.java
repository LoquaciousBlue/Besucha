package besucha.backend.gui;

import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

@Component
public class ConfirmationDialog extends Dialog {

    private GuiManager gm;
    private Label messageLabel;
    private Button yesButton, cancelButton;

    public ConfirmationDialog(GuiManager gm){
        super((Frame)null);
        this.gm = gm;
        this.initialize();
    }

    public void setMessage(String message){
        messageLabel.setText(message);
    }

    private void initialize(){
        this.setTitle(gm.getTitle());
        this.addWindowListener(new ExtWindowAdapter());

        this.resize();
        this.setLayout(this.generateLayout());
    }

    private void resize(){
        DimensionManager dm = new DimensionManager();
        dm.setDimensions(this, 0.3, 0.2);
        this.setFont(new Font("Cambria", Font.PLAIN, (int)dm.getY(0.02)));
    }

    private GridBagLayout generateLayout(){
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        DimensionManager dm = new DimensionManager();

        // init buttons and labels
        messageLabel = new WrappingLabel("Are you sure you want to start enrollment? The processing of the provided data will take a few minutes.");
        messageLabel.setAlignment(Label.CENTER);
        yesButton = new Button("Start");
        cancelButton = new Button("Cancel");

        // Add Listeners
        yesButton.addActionListener(new ConfirmationActionListener());
        cancelButton.addActionListener(new RejectionActionListener());

        gbc.fill = GridBagConstraints.BOTH;
        int padding = (int)(dm.getY(0.01));
        gbc.insets = new Insets(padding,padding,padding,padding);

        gbc.gridwidth = 3;
        gbc.gridheight = 4;
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;
        gbc.anchor = GridBagConstraints.NORTH;
        gbl.setConstraints(messageLabel, gbc);
        this.add(messageLabel);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.1;
        gbc.weighty = 0.1;
        gbc.anchor = GridBagConstraints.SOUTHWEST;
        gbl.setConstraints(yesButton, gbc);
        this.add(yesButton);

        gbc.gridx = 2;
        gbc.anchor = GridBagConstraints.SOUTHEAST;
        gbl.setConstraints(cancelButton, gbc);
        this.add(cancelButton);

        return gbl;
    }

    private class ExtWindowAdapter extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            gm.restoreStartDialogButtons();
            gm.closeConfirmationDialog();
        }
    }

    private class ConfirmationActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent arg0) {
            ConfirmationDialog.this.messageLabel.setForeground(Color.GRAY);
            ConfirmationDialog.this.cancelButton.setEnabled(false);
            ConfirmationDialog.this.yesButton.setEnabled(false);
            gm.sendFilesToValidate();
        }
    }

    private class RejectionActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent arg0) {
            gm.restoreStartDialogButtons();
            gm.closeConfirmationDialog();
        }
    }
}
