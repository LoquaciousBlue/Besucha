package besucha.backend.gui;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * Class for making labels wrap their text
 * @author Logan Drescher
 * @author Manfred Leonhardt
 * @author Dave Mulligan
 */
public class WrappingLabel extends Label {
    
    private Vector<String> innerText;

    /**
     * Creates an empty Label
     */
    public WrappingLabel() {
        this("");
    }

    /**
     * Creates a Label using the given text
     * @param text the text to place on the label
     */
    public WrappingLabel(String text) {
        this(text, 30);
    }

    /**
     * Creates a Label with the provided text 
     * @param text the text to place on the label
     * @param maxLength the max length of a line?
     */
    public WrappingLabel(String text, int maxLength) {
        if (text != null) {
            StringTokenizer st = new StringTokenizer(text, "\n");
            innerText = new Vector<>(st.countTokens());  // we know we'll need at least this many lines
            while (st.hasMoreTokens()) {
                // make sure we don't exceed maximum length
                String nextLine = st.nextToken();
                StringTokenizer st2  = new StringTokenizer(nextLine, " ", true);
                StringBuffer buf = new StringBuffer();

                if (nextLine.equals("")) {
                    innerText.addElement("\n");
                    continue;
                }
 
                while (st2.hasMoreTokens()) {
                    String nextWord = st2.nextToken();
 
                    if (buf.length() > 0 && buf.length() + nextWord.length() > maxLength) {
                        // when we would exceed max length, set the buffer into the array
                        // & start a new buffer
                        innerText.addElement(buf.toString());
                        buf.setLength(0);
                    }
                    buf.append(nextWord);
                }
                // catch the last part of a string
                innerText.addElement(buf.toString());
            }
        }
    }

    /**
     * Returns the font used by the label
     * @return the font used; never returns null.
     */  
    @Override
    public Font getFont() {
        Font f = super.getFont();
        return f == null ? new Font("dialog", Font.PLAIN, 12) : f;
    }

    //  
    // Make sure that the height and width are getting  
    // sent using the multi lines we know about.  
    //  
    public Dimension getPreferredSize() {
        FontMetrics fm = getFontMetrics(getFont());
        int maxWidth = 0;

        for (int i = 0; i < innerText.size(); ++i) {
            int mw = fm.stringWidth((String)innerText.elementAt(i));
            if (mw > maxWidth) 
                maxWidth = mw;
        }
        return new Dimension(maxWidth, fm.getHeight() * innerText.size());
    }

    /**
     * Paints the canvas
     * @param g the graphics 
     */
    public void paint(Graphics g) {   
        // Start at ascent with offset in y; Start at 0 for x.    
        super.paint(g);
        g.setColor(getForeground());
        FontMetrics fm = g.getFontMetrics(getFont());
        int yLoc = fm.getAscent(), xLoc = 0;
        Dimension dim = getSize();
        for (int i = 0; i < innerText.size(); ++i) {
            switch (getAlignment()){
                case Label.CENTER:
                    xLoc = (dim.width - fm.stringWidth((String)innerText.elementAt(i))) / 2;
                    break;
                case Label.RIGHT:
                    xLoc = dim.width - fm.stringWidth((String)innerText.elementAt(i));
                    break;
                default:
                    xLoc = 0;
            }

            g.drawString((String)innerText.elementAt(i), xLoc, yLoc);
            yLoc += fm.getHeight();

            /*
            if (getAlignment() == Label.CENTER) {
                xLoc = (dim.width - fm.stringWidth((String)innerText.elementAt(i))) / 2;
            } else if (getAlignment() == Label.RIGHT) {
                xLoc = dim.width - fm.stringWidth((String)innerText.elementAt(i));
            } else {
                xLoc = 0;
            }
            */   
        }
    }

    public static void main(String[] args) {
        Frame f = new Frame("WrapLabel Test");
        f.setLayout(new FlowLayout(FlowLayout.LEFT));
        WrappingLabel wl = new WrappingLabel("Left Aligned\nWrapping\nLabel Component", 10);
        wl.setBackground(Color.red);
        f.add(wl);
        wl = new WrappingLabel("Center Aligned\nWrapping\nLabel Component");
        wl.setAlignment(Label.CENTER);
        wl.setBackground(Color.green);
        f.add(wl);
        wl = new WrappingLabel("Right Aligned\nWrapping\nLabel Component");
        wl.setAlignment(Label.RIGHT);
        wl.setBackground(Color.yellow);
        f.add(wl);
        wl = new WrappingLabel("Single Line Label");
        wl.setBackground(Color.cyan);
        f.add(wl);
        f.pack();
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });
        f.setVisible(true);
    }
}
