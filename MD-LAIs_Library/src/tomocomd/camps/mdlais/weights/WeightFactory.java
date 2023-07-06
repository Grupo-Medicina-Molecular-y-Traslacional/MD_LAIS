/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tomocomd.camps.mdlais.weights;

/**
 *
 * @author Cesar
 */
public class WeightFactory {

    static public Weight getCutoff(WeightConfiguration configuration) 
    {
        if (configuration == null||configuration.getCutoffTypes().isEmpty()) 
        {
            return null;
        }

        Weight cutoff = null;

        for (int i = configuration.getCutoffTypes().size() - 1; i >= 0; i--) 
        {
            WeightType type = configuration.getCutoffTypes().get(i);

            switch (type) {
             
                case S_SHAPED:
                    cutoff = new S(type, configuration.getTruncationValues());
                    break;
                case Z_SHAPED:
                    cutoff = new Z(type,configuration.getTruncationValues());
                    break;
                case PI_SHAPED:
                    cutoff = new PI(type, configuration.getTruncationValues());
                    break;
                case ASCENDING_GAUSSIAN:
                    cutoff = new AscendingGaussian(type, configuration.getTruncationValues());
                    break;
                case SHIFTING1:
                    cutoff = new Shifting1(type, configuration.getTruncationValues());
                    break;
                case SWITCHING:
                    cutoff = new Switching(type, configuration.getTruncationValues());
                    break;
            }
        }
        
        ((FuzzyWeight)cutoff).setReferenceType(configuration.getTruncationReferenceType());
        
        return cutoff;
    }
}
