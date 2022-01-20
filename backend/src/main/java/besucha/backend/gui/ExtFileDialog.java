package besucha.backend.gui;

import org.springframework.stereotype.Component;

import java.awt.*;

@Component
public class ExtFileDialog extends FileDialog {
    public ExtFileDialog(){
        super(new Frame());
    }
}
