package besucha.backend.gui;

import besucha.backend.service.IOInterfacer;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
public class GuiManager {
    private static final String VERSION = "v0.3.2";

    private IOInterfacer ioi;

    private StartDialog sDialog;
    private ConfirmationDialog cDialog;
    private ProcessingDialog pDialog;
    private MessageDialog mDialog;
    private FinalDialog fDialog;
    private LoginDialog lDialog;
    private AuthenticationResultDialog aDialog;
    private ExtFileDialog eDialog;
    private SaveFileDialog sfDialog;

    private String catalogPath;
    private String preferencesPath;
    private String verificationResults, algorithmResults;
    private File resultFile;

    public GuiManager(@Lazy IOInterfacer ioi, @Lazy StartDialog sDialog, @Lazy ConfirmationDialog cDialog,
                      @Lazy ProcessingDialog pDialog, @Lazy MessageDialog mDialog, @Lazy FinalDialog fDialog,
                      @Lazy LoginDialog lDialog, @Lazy AuthenticationResultDialog aDialog,
                      @Lazy ExtFileDialog eDialog, @Lazy SaveFileDialog sfDialog) {

        this.ioi = ioi;
        this.sDialog = sDialog;
        this.cDialog = cDialog;
        this.pDialog = pDialog;
        this.mDialog = mDialog;
        this.fDialog = fDialog;
        this.lDialog = lDialog;
        this.aDialog = aDialog;
        this.eDialog = eDialog;
        this.sfDialog = sfDialog;
        this.init();
    }

    private void init(){
        this.catalogPath = "";
        this.preferencesPath = "";
        this.verificationResults = "";
        this.algorithmResults = "";
        //this.resultFile = new File(System.getProperty("user.dir"), "fake.txt");
        this.resultFile = null;
    }

    public String getTitle(){
        return String.format("BESUCHA %s", GuiManager.VERSION);
    }

    public void launchMainWindow(){
        this.washFrame(this.sDialog);
        this.sDialog.clearFiles();  // Due to unsolved bugs in AWT,
                                    // this is how we really initialize file labels.
    }

    public void sendFilesToValidate() {
        //this.closeConfirmationDialog();
        boolean isValid = this.sendFilesForValidation();
        if (isValid)
            this.runAlgorithm();
    }

    public void launchConfirmationDialog(){
        this.sDialog.setEnabled(false);
        this.washFrame(cDialog);
        this.cDialog.setModal(true);
    }

    public void closeConfirmationDialog(){
        this.closeOut(cDialog);
    }

    public void restoreStartDialogButtons(){
        this.sDialog.setEnabled(true);
        sDialog.showContinueButton();
    }

    public void closeOut(Window d){
        d.dispose();
    }

    public void closeOutAndQuit(){
        System.exit(0);
    }

    public void closeOutAndRestore(Window d){
        this.closeOut(d);
        this.launchMainWindow();
    }

    public boolean sendFilesForValidation()  {
        String errorMessage = ioi.validate(catalogPath, preferencesPath);
        if (errorMessage != null){
            errorMessage = "Error:\n" + errorMessage;
            mDialog.updateResults(new Exception(errorMessage));
            this.washFrame(mDialog);
            return false;
        }
        return true;
    }

    public void runAlgorithm(){
        try {
            returnResults(this.ioi.runAlgorithm());
        } catch (Exception e){
            returnResults(e);
        }
    }

    public void returnResults(String results){
        fDialog.updateResults(results);
        this.washFrame(fDialog);
        this.closeOut(cDialog);
    }

    public void returnResults(Exception e){
        mDialog.updateResults(e);
        this.washFrame(mDialog);
        this.closeOut(cDialog);
    }

    public void sendEmails(String username, String password)  {
        String errorMessage = ioi.sendEmails(username, password);
        if (errorMessage != null){
            errorMessage = "Error:\n" + errorMessage;
            this.aDialog.updateResults(errorMessage);
            this.washFrame(aDialog);
            this.restoreLoginDialog();
        } else {
            this.aDialog.updateResults("Emails Successfully Sent.");
            this.washFrame(aDialog);
            this.closeLoginDialog();
        }
    }

    public void setCatalogPath(String catalogPath) {
        this.catalogPath = catalogPath;
    }

    public void setPreferencesPath(String preferencesPath) {
        this.preferencesPath = preferencesPath;
    }

    public void launchLoginDialog(){
        this.washFrame(lDialog);
    }

    public void closeLoginDialog(){
        lDialog.dispose();
        //this.closeOut(lDialog);
    }

    public void restoreLoginDialog(){
        lDialog.enableEmailButton();
    }

    public File getFileFromDisk(){
        eDialog.setFilenameFilter(new ExcelFilenameFilter());
        eDialog.setVisible(true);
        File[] files = eDialog.getFiles();
        if (files.length > 0) return files[0];
        return null;
    }

    public void saveFileToDisk(File f){
        // NOTE: DESTROYS PASSED FILE!
        sfDialog.setFile(f.getPath());
        sfDialog.setVisible(true);
        File[] fileArr = sfDialog.getFiles();
        if (fileArr.length < 1) return;
        String pathStr = fileArr[0].getPath();
        File newFile = new File(pathStr);
        Path target = Paths.get(pathStr);
        Path source = Paths.get(f.getPath());
        try {
            if (!newFile.exists()) Files.copy(source, target);
            else Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e){
            System.out.println("DELAYED ERROR: " + e.toString());
        } // Since we're saving rather than loading files, this catch should be dead code
        //f.deleteOnExit();
    }

    public File getResultFile(){
        return this.resultFile;
    }

    public void setResultFile(File resultFile){
        this.resultFile = resultFile;
    }

    public Color getBackgroundColor(){
        return sDialog.getBackground();
    }

    private void washFrame(Window dlg){
        dlg.setLocationRelativeTo(null);
        dlg.setVisible(true);
        dlg.addNotify();
    }

    private class ExtWindowAdapter extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            closeOutAndQuit();
        }
    }
}
