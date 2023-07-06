package tomocomd.camps.mdlais.workers.output;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class ARFFTextOutput extends TextOutput
{
    public ARFFTextOutput(Writer writer) 
    {
        super(writer);
    }
    
    @Override
    public void writeHeader( List<String> items ) throws IOException 
    {
        writer.write("@RELATION descriptors" + lineSep + lineSep);
        writer.write("@ATTRIBUTE title STRING" + lineSep);
        for (int i = 1; i < items.size(); i++) 
        {
            writer.write("@ATTRIBUTE " + items.get(i) + " NUMERIC" + lineSep);
        }
        writer.write(lineSep + "@DATA" + lineSep);
    }
    
    @Override
    public void writeLine( List<String> items ) throws IOException 
    {
        for (int i = 0; i < items.size() - 1; i++)
        {
            writer.write( items.get(i) + "," );
        }
        writer.write( items.get( items.size() - 1 ) + lineSep );
    }
}