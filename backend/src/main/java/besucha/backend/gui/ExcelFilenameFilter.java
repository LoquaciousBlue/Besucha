package besucha.backend.gui;

import java.io.File;
import java.io.FilenameFilter;

public class ExcelFilenameFilter implements FilenameFilter {

    @Override
    public boolean accept(File dir, String name) {
        return this.accept(name);
    }

    public boolean accept(String name){
        return name.matches(".+\\.xlsx");
    }

}