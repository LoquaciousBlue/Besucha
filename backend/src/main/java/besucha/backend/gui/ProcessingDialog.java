package besucha.backend.gui;

import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

@Component
public class ProcessingDialog extends Dialog {
    private GuiManager gm;
    private Label waitingMessage;

    public ProcessingDialog(GuiManager gm){
        super(new Frame());
        this.gm = gm;
        this.initialize();
    }

    private void initialize(){
        this.addWindowListener(new ExtWindowAdapter());

        super.setLayout(this.generateLayout());
        //this.resize();
        this.presize();
    }

    public void resize(){
        DimensionManager dm = new DimensionManager();
        dm.setDimensions(this, 0.3, 0.2);
        this.setFont(new Font("Cambria", Font.PLAIN, (int)dm.getY(0.02)));
    }

    public void presize(){
        DimensionManager dm = new DimensionManager();
        this.setSize(1,1);
        this.setFont(new Font("Cambria", Font.PLAIN, (int)dm.getY(0.02)));
    }

    private GridBagLayout generateLayout(){
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        DimensionManager dm = new DimensionManager();
        waitingMessage = new WrappingLabel
            ("Please wait while the schedules are processed\nThis may take some time.");

        int padding = (int)(dm.getY(0.01));
        gbc.insets = new Insets(padding,padding,padding,padding);
        gbl.setConstraints(waitingMessage, gbc);
        this.add(waitingMessage);

        return gbl;
    }

    private class ExtWindowAdapter extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            gm.closeOutAndQuit();
        }
    }
}
