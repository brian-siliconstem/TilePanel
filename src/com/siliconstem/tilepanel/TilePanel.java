
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
import java.net.URL;


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
    protected String tileFilename="WastelandTiles.png";
    protected String glyphNamesFilename="WastelandTiles.txt";
    protected String[] glyphNames;
    
    protected boolean debug=false;
    
    public TilePanel(int tilesX, int tilexY)
    {
        this(tilesX,tilexY,null,null,24,14,4,4,16,10,32,32);
    }
    public TilePanel(int tilesX, int tilexY,BufferedImage sprite,String[] spriteNames,int gTilesX,int gTilesY,int tileWidth,int tileHeight)
    {
        this(tilesX,tilexY,sprite,spriteNames,0,0,0,0,gTilesX,gTilesY,tileWidth,tileHeight);
    }
    public TilePanel(int tilesX, int tilesY,BufferedImage sprite, String[] spriteLabels, int sX, int sY, int pX, int pY, int gTilesX, int gTilesY, int tileWidth, int tileHeight){
        super();
        BufferedImage loadedSprite=sprite;
        String[] loadedSpriteLabels=spriteLabels;
        if (sprite==null){
            URL spriteURL= TilePanel.class.getResource(tileFilename);
            loadedSprite=Utilities.loadBufferedImage(spriteURL);
        }
        if (spriteLabels==null){
            URL spriteNamesURL= TilePanel.class.getResource(glyphNamesFilename);
            loadedSpriteLabels=Utilities.loadCSVToArray(spriteNamesURL);
        }
        glyphSprite=loadedSprite;//save the sprite, we will cut it up into pieces later
        glyphTilesX=gTilesX;//dimensions of the glyph image in tiles
        glyphTilesY=gTilesY;
        //tileFilename=tileFile;//filw with the glyph image
        //glyphNamesFilename=glyphNamesFile;
        startX=sX;//where the tiles start in the image
        startY=sY;
        paddingX=pX;//padding between tiles in the glyph image
        paddingY=pY;
        widthInCharacters=tilesX;//how large the view screen will be
        heightInCharacters=tilesY;
        charWidth=tileWidth; //how large each tile is
        charHeight=tileHeight;
        //change glyphs from default 256 to actual number
        numGlyphs=glyphTilesX*glyphTilesY;
        glyphs=new BufferedImage[numGlyphs];
        glyphNames=loadedSpriteLabels;
        
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
    public void loadGlyphNames(){
        URL spriteNamesURL= TilePanel.class.getResource(glyphNamesFilename);
        glyphNames=Utilities.loadCSVToArray(spriteNamesURL);
    }
    public void loadGlyphs(){
        BufferedImage sprite=null;
        try {
            sprite = ImageIO.read(TilePanel.class.getResource(tileFilename));
        } catch (IOException e) {
            System.err.println("loadGlyphs(): " + e.getMessage());
        }
        if(sprite != null) loadGlyphs(sprite);
        else System.err.println("loadGlyphs(): sprite is null!");
    }
     public void loadGlyphs(BufferedImage sprite) {
        for (int i = 0; i < this.numGlyphs; i++) {
            int sx = startX+(i % this.glyphTilesX) * (charWidth+paddingX) ;
            int sy = startY+(i / this.glyphTilesX) * (charHeight+paddingY) ;

            System.out.println("sx:"+sx+" sy:"+sy);
            glyphs[i] = new BufferedImage(charWidth, charHeight, BufferedImage.TYPE_INT_ARGB);
            glyphs[i].getGraphics().drawImage(glyphSprite, 0, 0, charWidth, charHeight, sx, sy, sx + charWidth, sy + charHeight, null);
        }
    }
     
    /* protected LookupOp setColors(Color bgColor, Color fgColor) {
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
     */
     public int getTileByName(String tileName){
         //loop through the names to find a match
         int nameIndex=0;
         while(nameIndex<glyphNames.length&&!glyphNames[nameIndex].equalsIgnoreCase(tileName)){
             nameIndex++;
         }
         if (nameIndex>glyphNames.length) nameIndex=-1;//return -1 if we cound;t find it, this should cause an error elsewhere
         //return the index of the matching glyph
         return nameIndex;
     }
     public int getGlyph(int x, int y){
         return chars[x][y];
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
