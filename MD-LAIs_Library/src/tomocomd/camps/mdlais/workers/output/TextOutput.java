package tomocomd.camps.mdlais.workers.output;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

public abstract class TextOutput implements ITextOutput 
{
    protected Writer writer;
    protected String itemSep = OutputFormats.OUTPUT_SPC;
    protected String lineSep = System.getProperty("line.separator");
    
    public TextOutput(Writer writer) 
    {
        this.writer = writer;
    }
    
    @Override
    public void setItemSeparator(String itemSep) 
    {
        this.itemSep = itemSep;
    }
    
    @Override
    public void writeBlock(List<List<String>> items) throws IOException 
    {
     for (List<String> item : items) 
     {
            writeLine(item);
        }
    }
}
