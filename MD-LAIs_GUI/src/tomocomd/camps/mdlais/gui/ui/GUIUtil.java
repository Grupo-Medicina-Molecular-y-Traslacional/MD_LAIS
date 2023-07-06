package tomocomd.camps.mdlais.gui.ui;

import java.awt.Component;
import java.awt.Container;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author econtreras
 */
public class GUIUtil {

    private static Component[] getComponents(Component container) {
        ArrayList<Component> list = null;

        try {
            list = new ArrayList<Component>(Arrays.asList(
                    ((Container) container).getComponents()));
            for (int index = 0; index < list.size(); index++) {
                for (Component currentComponent : getComponents(list.get(index))) {
                    list.add(currentComponent);
                }
            }
        } catch (ClassCastException e) {
            list = new ArrayList<Component>();
        }

        return list.toArray(new Component[list.size()]);
    }

    public static void disableAllComponents(Container container) {
        for (Component component : getComponents(container)) {
            component.setEnabled(false);
        }
    }

    public static void enableAllComponents(Container container) {
        for (Component component : getComponents(container)) {
            component.setEnabled(true);
        }
    }

    public static FileFilter getCustomFileFilter(final String[] extensions, final String description) 
    {
        FileFilter fileFilter = new FileFilter() {

            @Override
            public boolean accept(File f) 
            {
                if (!f.isDirectory()) 
                {
                    for (String ext : extensions) 
                    {
                        if (f.getName().endsWith(ext)) 
                        {
                            return true;
                        }
                    }
                    return false;
                } else 
                {
                    return true;
                }
            }

            @Override
            public String getDescription() 
            {
                return description;
            }
        };

        return fileFilter;
    }
    
    static int showDialog(String content,String dialogType)
    {
        return 0;
    }
    
    static String getTaskTitle(TaskType taskType)
    {
        if(taskType==TaskType.GATHER_FILES_INFORMATION)
        {
            return "Gathering files infomation";
        }
        else if (taskType==TaskType.REMOVE_DUPLICATED_ID)
        {
            return "Detecting duplicated by identifier";
        }
        
        else if (taskType==TaskType.REMOVE_DUPLICATED_SEQUENCE)
        {
            return "Detecting duplicated by sequence";
        }
        
        return null;                
    }
}
