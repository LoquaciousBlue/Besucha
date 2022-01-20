package besucha.backend.gui;

import org.springframework.stereotype.Component;
import java.awt.*;

@Component
public class SaveFileDialog extends FileDialog {
    private GuiManager gm;

    public SaveFileDialog(GuiManager gm){
        super(new Frame());
        this.gm = gm;
        this.initialize();
    }

    private void initialize(){
        this.setMode(FileDialog.SAVE);
    }
}
