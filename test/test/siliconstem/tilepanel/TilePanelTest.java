
package test.siliconstem.tilepanel;

import org.junit.Test;
import com.siliconstem.tilepanel.TilePanel;
import com.siliconstem.tilepanel.Utilities;
import asciipanel.AsciiPanel;

import javax.swing.JFrame;

public class TilePanelTest {
    
    @Test
    public void TestTilePanelConstructor(){
        TilePanel tp=new TilePanel(20,20,"WastelandTiles.png","WastelandTiles.txt",24,14,4,4,16,10,32,32);
        tp.load();
    }
    
    @Test
    public void TestTilePanelWindow(){
        TilePanel tp=new TilePanel(20,20,"WastelandTiles.png","WastelandTiles.txt",24,14,4,4,16,10,32,32);
        tp.load();
        
        JFrame window=new JFrame();
        window.add(tp);
        window.setVisible(true);
        window.setSize(tp.getWidthPixels(),tp.getHeightPixels());
        Utilities.pause(1);
    }
    
    @Test
    public void TestTilePanelWindowWrite(){
        TilePanel tp=new TilePanel(20,20,"WastelandTiles.png","WastelandTiles.txt",14,4,4,4,16,10,32,32);
        tp.load();
        
        JFrame window=new JFrame();
        window.add(tp);
        window.setVisible(true);
        window.setSize(tp.getWidthPixels(),tp.getHeightPixels());
        
        tp.write(1,0,0);
        tp.write(1,1,0);
        tp.write(1,0,1);
        tp.write(1,1,1);
        tp.write(1,2,2);
        Utilities.pause(30);
    }
}
