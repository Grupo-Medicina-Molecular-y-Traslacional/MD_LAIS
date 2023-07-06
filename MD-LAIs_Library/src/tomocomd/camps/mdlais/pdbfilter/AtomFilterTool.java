/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tomocomd.camps.mdlais.pdbfilter;

/**
 *
 * @author Dragon
 */
public class AtomFilterTool {

    static public String getRepresentationSymbol(AtomFilterType type) {
        switch (type) {
            case CA:
                return "CA";
            case CB:
                return "CB";
            case AVERAGE:
                return "AVG";
            case AB:
                return "AB";
            default:
                break;
        }
        return null;
    }
}
