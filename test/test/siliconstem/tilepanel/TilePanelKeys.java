
package test.siliconstem.tilepanel;

import org.junit.Test;
import com.siliconstem.tilepanel.TilePanel;
import com.siliconstem.tilepanel.Utilities;
import asciipanel.AsciiPanel;



import javax.swing.JFrame;

public class TilePanelKeys {
    @Test
    public void moveToon(){
  
        TilePanel tp=new TilePanel(20,20,"WastelandTiles.png","WastelandTiles.txt",14,4,4,4,16,10,32,32);
        tp.load();
        tp.clear(0);
        
        JFrame window=new JFrame();
        window.add(tp);
        window.setVisible(true);
        window.setSize(tp.getWidthPixels(),tp.getHeightPixels());
        
        window.addKeyListener(tp);
        Utilities.pause(30);
    }
}
