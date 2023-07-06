package tomocomd.camps.mdlais.workers.output;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class PlainTextOutput extends TextOutput
{
    public PlainTextOutput( Writer writer )
    {
        super(writer);
    }
    
    @Override
    public void writeHeader( List<String> items ) throws IOException 
    {
        int nitem = items.size();
        
        for (int i = 0; i < nitem - 1; i++) 
        {
            writer.write( items.get(i) + itemSep );
        }
        
        writer.write( items.get( nitem - 1 ) + lineSep );
    }
    
    @Override
    public void writeLine( List<String> items ) throws IOException 
    {
        int nitem = items.size();
        
        for (int i = 0; i < nitem - 1; i++) 
        {
            writer.write( items.get(i) + itemSep );
        }
        
        writer.write( items.get( nitem - 1 ) + lineSep );
    }
}
