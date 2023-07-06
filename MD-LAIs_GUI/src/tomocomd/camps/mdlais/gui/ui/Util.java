/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tomocomd.camps.mdlais.gui.ui;

import java.awt.Desktop;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import tomocomd.camps.mdlais.weights.WeightValue;
import tomocomd.camps.mdlais.weights.WeightValueInterval;

/**
 *
 * @author Cesar
 */
public class Util 
{
    static public List<WeightValue> getCutoffValueList( String lagValue ) 
    {
        return getCutoffValueList( false, lagValue );
    }
    
    static public List<WeightValue> getCutoffValueList( boolean equalStric, String lagValue ) 
    {
        List<WeightValue> list = new ArrayList<>();
        
        getCutoffValueList( equalStric, lagValue, list );
        
        return list;
    }

    static private void getCutoffValueList( boolean equalStric, String lagValue, List<WeightValue> list ) 
    {
        if ( lagValue.isEmpty() )
        {
            //TODO poner que devuelva 1 o 0 para saber si se puedo poner el k value o no.
        } 
        else 
        {
            if ( lagValue.contains( ";" ) ) // If have more than one value
            {
                for ( String tag : lagValue.split( ";" ) ) 
                {
                    getCutoffValueList( equalStric, tag, list );
                }
            }
            else 
            {
                if ( lagValue.contains( "-" ) ) // If have an interval
                {
                    String[] tags = lagValue.split( "-" );

                    list.add(new WeightValueInterval( Float.parseFloat( tags[0] ), tags[0], Float.parseFloat( tags[1] ), tags[1] ) );
                } 
                else 
                {
                    list.add(new WeightValue( equalStric, Float.parseFloat( lagValue ), lagValue ) );
                }
            }
        }
    }
    
    static public ActionListener openFile( final Window owner, final String fileName )
    {
        return new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                File theory = new File( System.getProperty( "user.dir" ), "files" );
                theory = new File( theory, fileName );

                if ( Desktop.isDesktopSupported() ) 
                {
                    try
                    {
                        Desktop desktop = Desktop.getDesktop();
                        desktop.open( theory );
                    } 
                    catch ( Throwable ex ) 
                    {
                        JOptionPane.showMessageDialog( owner, "Error to open file " + theory.getName(), "ERROR", JOptionPane.ERROR_MESSAGE );
                    }
                }
            }
        };
    }
    
    static public ActionListener openDirectory( final Window owner, final File directory )
    {
        return new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                if ( Desktop.isDesktopSupported() ) 
                {
                    try 
                    {
                        Desktop desktop = Desktop.getDesktop();
                        desktop.open( directory );
                    } 
                    catch ( Throwable ex ) 
                    {
                        JOptionPane.showMessageDialog( owner, "Error to open file " + directory.getAbsolutePath(), "ERROR", JOptionPane.ERROR_MESSAGE );
                    }
                }
            }
        };
    }
    
    public static String capitalize(String str)
    {
        return str.substring(0, 1).toUpperCase() + str.substring(1);    
    }    
}
