package tomocomd.camps.mdlais.workers;

import java.util.List;
import tomocomd.camps.mdlais.exceptions.ExceptionInfo;

public interface ISwingWorker 
{
    void go();
    
    int getLengthOfTask();
    
    int getCurrent();
    
    void stop();
    
    boolean isDone();
    
    boolean isCancelled();
    
    String getInputFormat();
    
    List<ExceptionInfo> getExceptionList();
}
