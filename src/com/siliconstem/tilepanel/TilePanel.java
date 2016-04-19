
package com.siliconstem.tilepanel;

import asciipanel.AsciiPanel;
import java.awt.Color;

import java.awt.image.BufferedImage;
import java.awt.image.LookupOp;
import java.awt.image.ShortLookupTable;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.StreamTokenizer;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.FileNotFoundException;
import java.util.concurrent.TimeUnit;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

/**
 *
 * @author Brian McGuire
 */
public class TilePanel extends AsciiPanel implements KeyListener {
    protected int startX; //the tiles may not start in the top left corner
    protected int startY;
    protected int paddingX;//if there is padding between the tiles
    protected int paddingY;
    protected int glyphTilesX;
    protected int glyphTilesY;
    protected String tileFilename;
    protected String glyphNamesFilename;
    protected String[] glyphNames;
    
    public TilePanel(int tilesX, int tilexY)
    {
        this(tilesX,tilexY,"WastelandTiles.png","WastelandTiles.txt",14,4,4,4,16,10,32,32);
    }
    public TilePanel(int tilesX, int tilesY,String tileFile,String glyphNamesFile,int sX, int sY, int pX, int pY, int gTilesX, int gTilesY, int tileWidth, int tileHeight){
        super();
        glyphTilesX=gTilesX;//dimensions of the glyph image in tiles
        glyphTilesY=gTilesY;
        tileFilename=tileFile;//filw with the glyph image
        glyphNamesFilename=glyphNamesFile;
        startX=sX;//where the tiles start in the image
        startY=sY;
        paddingX=pX;//padding between tiles in the glyph image
        paddingY=pY;
        widthInCharacters=tilesX;//how large the view screen will be
        heightInCharacters=tilesY;
        charWidth=tileWidth; //how large each tile is
        charHeight=tileHeight;
        //change glyphs from default 256 to actual number
        numGlyphs=widthInCharacters*heightInCharacters;
        glyphs=new BufferedImage[numGlyphs];
        glyphNames=new String[numGlyphs];
        
    }
    public int getWidthPixels(){
        return widthInCharacters*charWidth;
    }
    public int getHeightPixels(){
        return heightInCharacters*charHeight;
    }
    public int getWidthNumTiles(){
        return widthInCharacters;
    }
    public int getHeightNumTiles(){
        return heightInCharacters;
    }
    public void load(){
        loadGlyphs();
        loadGlyphNames();
    }
            
    
    protected void loadGlyphNames(){
        BufferedReader br=null;
        try {
            String line="";
            String separator=",";
            StringBuilder completeFile=new StringBuilder();
            File file = new File(TilePanel.class.getResource(glyphNamesFilename).getFile());
            br = new BufferedReader(new FileReader(file)); 
            while ((line = br.readLine()) != null) {
                completeFile.append(line+separator);
            }
            String[] tileNames = completeFile.toString().split(separator);


	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	} finally {
		if (br != null) {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

    } 
     public void loadGlyphs() {
        try {
            glyphSprite = ImageIO.read(TilePanel.class.getResource(tileFilename));
        } catch (IOException e) {
            System.err.println("loadGlyphs(): " + e.getMessage());
        }

        for (int i = 0; i < this.numGlyphs; i++) {
            int sx = startX+(i % this.glyphTilesX) * (charWidth+paddingX) + this.glyphTilesY;
            int sy = startY+(i / this.glyphTilesX) * (charHeight+paddingY) + this.glyphTilesY;

            System.out.println("sx:"+sx+" sy:"+sy);
            glyphs[i] = new BufferedImage(charWidth, charHeight, BufferedImage.TYPE_INT_ARGB);
            glyphs[i].getGraphics().drawImage(glyphSprite, 0, 0, charWidth, charHeight, sx, sy, sx + charWidth, sy + charHeight, null);
        }
    }
     
     protected LookupOp setColors(Color bgColor, Color fgColor) {
        short[] a = new short[this.numGlyphs];
        short[] r = new short[this.numGlyphs];
        short[] g = new short[this.numGlyphs];
        short[] b = new short[this.numGlyphs];

        byte bgr = (byte) (bgColor.getRed());
        byte bgg = (byte) (bgColor.getGreen());
        byte bgb = (byte) (bgColor.getBlue());

        byte fgr = (byte) (fgColor.getRed());
        byte fgg = (byte) (fgColor.getGreen());
        byte fgb = (byte) (fgColor.getBlue());

        for (int i = 0; i < this.numGlyphs; i++) {
            if (i == 0) {
                a[i] = (byte) 255;
                r[i] = bgr;
                g[i] = bgg;
                b[i] = bgb;
            } else {
                a[i] = (byte) 255;
                r[i] = fgr;
                g[i] = fgg;
                b[i] = fgb;
            }
        }

        short[][] table = {r, g, b, a};
        return new LookupOp(new ShortLookupTable(0, table), null);
    }
     
    public TilePanel write(int glyphNum,int x, int y){
        if (glyphNum < 0 || glyphNum >= glyphs.length)
            throw new IllegalArgumentException("glyphNum " + glyphNum + " must be within range [0," + glyphs.length + "]." );

        if (x < 0 || x >= this.getWidthNumTiles())
            throw new IllegalArgumentException("x " + x + " must be within range [0," + this.getWidthNumTiles() + ")" );

        if (y < 0 || y >= this.getHeightNumTiles())
            throw new IllegalArgumentException("y " + y + " must be within range [0," + this.getHeightNumTiles() + ")" );

        this.chars[x][y]=glyphNum;
        return this;
     
    }
    public TilePanel clear(int glyphNum){
        if (glyphNum < 0 || glyphNum >= glyphs.length)
            throw new IllegalArgumentException("glyphNum " + glyphNum + " must be within range [0," + glyphs.length + "]." );
        for(int x=0;x<this.widthInCharacters;x++){
            for(int y=0;y<this.heightInCharacters;y++){
                this.chars[x][y]=glyphNum;
            }
        }
        return this;
    }
    
  public void keyPressed(KeyEvent e) {
        System.out.println("keyPressed:"+e.getKeyChar());
       

    }

    public void keyReleased(KeyEvent e) {
        System.out.println("keyReleased:"+e.getKeyChar());
    }


    public void keyTyped(KeyEvent e) {
        System.out.println("keyTyped:"+e.getKeyChar());
    }
   
}
