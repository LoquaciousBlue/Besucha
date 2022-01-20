package besucha.backend.gui;

import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

@Component
public class StartDialog extends Dialog {
    private boolean hasCatalog, hasPreferences;
    private Button continueButton, catalogButton, preferenceButton;
    private Label welcomeLabel, catalogLabel, preferenceLabel;

    private GuiManager gm;

    public StartDialog(GuiManager gm){
        super(new Frame());
        this.gm = gm;
        this.initialize();
    }

    public void getCatalogFileFromDisk(){
        File f = this.getFileFromDisk();
        if (f != null){
            gm.setCatalogPath(f.getPath());
            this.hasCatalog = true;
            this.catalogLabel.setText(" ");
            this.catalogLabel.setText(this.processName(f.getName()));
        } else {
            this.hasCatalog = false;
            this.catalogLabel.setText(" ");
            this.catalogLabel.setText("no file selected");
        }
        showContinueButton(); // Only works if both files are uploaded
    }

    public void getPreferenceFileFromDisk(){
        File f = this.getFileFromDisk();
        if (f != null){
            gm.setPreferencesPath(f.getPath());
            this.hasPreferences = true;
            this.preferenceLabel.setText(" ");
            this.preferenceLabel.setText(this.processName(f.getName()));
        } else {
            this.hasPreferences = false;
            this.preferenceLabel.setText(" ");
            this.preferenceLabel.setText("no file selected");
        }
        showContinueButton(); // Only works if both files are uploaded
    }

    public void clearCatalogFile(){
        this.hasCatalog = false;
        this.catalogLabel.setText(" ");
        this.catalogLabel.setText("no file selected");

        gm.setCatalogPath(null);
        showContinueButton(); // Will remove the button if it is visible
    }

    public void clearPreferenceFile(){
        this.hasPreferences = false;
        this.preferenceLabel.setText(" ");
        this.preferenceLabel.setText("no file selected");

        gm.setPreferencesPath(null);
        showContinueButton(); // Will remove the button if it is visible
    }

    private void initialize(){
        this.setTitle(gm.getTitle());
        this.hasCatalog = false;
        this.hasPreferences = false;
        this.addWindowListener(new ExtWindowAdapter());
        this.setLayout(this.generateLayout());
        this.resize();
    }

    private void resize(){
        DimensionManager dm = new DimensionManager();
        dm.setSquareDimensions(this, 0.45);
        this.resizeFont();
    }

    private File getFileFromDisk(){
        return gm.getFileFromDisk();
    }

    public void showContinueButton(){

        if (this.hasCatalog && this.hasPreferences){
            //catalogLabel.setEnabled(true);
            //preferenceButton.setEnabled(true);
            continueButton.setEnabled(true);
        } else if (continueButton != null){
            continueButton.setEnabled(false);
        }
    }

    public void clearFiles(){
        this.clearCatalogFile();
        this.clearPreferenceFile();
    }

    private void resizeFont(){
        DimensionManager dm = new DimensionManager();
        this.setFont(new Font("Cambria", Font.PLAIN, (int)dm.getY(0.02)));
    }

    private String processName(String filename){
        if (filename.length() < 15) return filename;
        else return filename.substring(0,13) + "...";
    }

    private GridBagLayout generateLayout(){
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        DimensionManager dm = new DimensionManager();

        // init buttons and labels
        welcomeLabel = new WrappingLabel("Welcome to Besucha");
        welcomeLabel.setAlignment(Label.CENTER);
        catalogLabel = new WrappingLabel(" ");
        catalogLabel.setAlignment(Label.CENTER);
        preferenceLabel = new WrappingLabel(" ");
        preferenceLabel.setAlignment(Label.CENTER);
        catalogButton = new Button("Select Catalog");
        preferenceButton = new Button("Select Preferences");
        continueButton = new Button("Continue");
        continueButton.setVisible(true);
        continueButton.setEnabled(false);

        // Add Listeners
        catalogButton.addActionListener(new CatalogFileActionListener());
        preferenceButton.addActionListener(new PreferenceFileActionListener());
        continueButton.addActionListener(new ContinueActionListener());

        // Style and add
        gbc.fill = GridBagConstraints.BOTH;
        int padding = (int)(dm.getY(0.01));
        gbc.insets = new Insets(padding,padding,padding,padding);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weighty = 1.0;
        gbc.gridheight = 2;
        gbc.gridy = 1;
        gbc.gridx = 1;
        gbl.setConstraints(welcomeLabel, gbc);
        super.add(welcomeLabel);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0,0,0,0);
        gbc.weighty = 0.0;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.2;
        gbc.gridy = 3;
        gbl.setConstraints(catalogLabel, gbc);
        super.add(catalogLabel);

        gbc.gridx = 2;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        //gbc.gridwidth = 1;
        gbl.setConstraints(preferenceLabel, gbc);
        super.add(preferenceLabel);

        gbc.insets = new Insets(padding,padding,padding,padding);
        gbc.weighty = 0.0;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;
        gbc.gridy = 4;
        gbc.gridx = 1;
        gbl.setConstraints(catalogButton, gbc);
        super.add(catalogButton);

        gbc.gridx = 2;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbl.setConstraints(preferenceButton, gbc);
        super.add(preferenceButton);

        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.gridy = 5;
        gbc.gridx = 1;
        gbc.weightx = 0.0;
        gbl.setConstraints(continueButton, gbc);
        super.add(continueButton);

        return gbl;
    }

    private class CatalogFileActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent arg0) {
            try {
                getCatalogFileFromDisk();
            } catch (NullPointerException e){
                clearCatalogFile();
            }
        }
    }

    private class PreferenceFileActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent arg0) {
            try {
                getPreferenceFileFromDisk();
            } catch (NullPointerException e){
                clearPreferenceFile();
            }
        }
    }

    private class ContinueActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent arg0) {
            continueButton.setEnabled(false);
            gm.launchConfirmationDialog();
        }
    }

    private class ExtWindowAdapter extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            gm.closeOutAndQuit();
        }
    }
}
