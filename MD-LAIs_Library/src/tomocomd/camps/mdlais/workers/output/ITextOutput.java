package tomocomd.camps.mdlais.workers.output;

import java.io.IOException;
import java.util.List;

public interface ITextOutput 
{
    public void setItemSeparator(String itemSep);

    public void writeHeader( List<String> items ) throws IOException;
    
    public void writeLine( List<String> items ) throws IOException;
    
    public void writeBlock( List<List<String>> items ) throws IOException;
}
