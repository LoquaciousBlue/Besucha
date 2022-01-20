package besucha.backend.gui;

import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

@Component
public class MessageDialog extends Dialog {
    private TextArea messageArea;
    private Button okayButton;

    private GuiManager gm;

    public MessageDialog(GuiManager gm){
        super(new Frame());
        this.gm = gm;
        this.initialize();
    }

    public void updateResults(Exception e){
        this.removeAll();
        super.setLayout(this.generateLayout(e.getMessage()));
        this.resize();
        this.validate();
    }

    private void initialize(){
        this.setTitle(gm.getTitle());
        this.addWindowListener(new ExtWindowAdapter());
        super.setLayout(generateLayout());
        this.resize();
    }

    private void resize(){
        DimensionManager dm = new DimensionManager();
        dm.setDimensions(this, 0.3, 0.4);
        this.setFont(new Font("Cambria", Font.PLAIN, (int)dm.getY(0.02)));
    }

    private GridBagLayout generateLayout(){
        return this.generateLayout("");
    }

    private GridBagLayout generateLayout(String labelText){
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        DimensionManager dm = new DimensionManager();
        
        // init buttons and labels
        messageArea = new TextArea(labelText, 1,1, TextArea.SCROLLBARS_NONE);
        messageArea.setEditable(false);
        okayButton = new Button("Ok");

        // Add Listeners
        okayButton.addActionListener(new OkayActionListener());

        // Apply bi-axial fill
        gbc.fill = GridBagConstraints.BOTH;

        // Message
        gbc.gridx = 3;
        gbc.gridy = 1;
        int padding = (int)(dm.getY(0.01));
        gbc.insets = new Insets(padding,padding,padding,padding);
        gbc.weightx = 1.0;
        gbc.weighty = 1.0; 
        
        gbc.anchor = GridBagConstraints.NORTH;

        gbl.setConstraints(messageArea, gbc);
        super.add(messageArea);

        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.gridy = 4;
        gbc.weighty = 0.1;
        gbc.weightx = 0.1;
        gbc.anchor = GridBagConstraints.SOUTH;
        gbl.setConstraints(okayButton, gbc);
        super.add(okayButton);

        return gbl;
    }

    private class OkayActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent arg0) {
            gm.closeOutAndQuit();
        }
    }

    private class ExtWindowAdapter extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            gm.closeOutAndQuit();
        }
    }

}
