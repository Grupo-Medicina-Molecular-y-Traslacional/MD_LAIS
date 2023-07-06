package tomocomd.camps.mdlais.pdbfilter;

/**
 *
 * @author econtreras
 */
public class AtomFilterFactory 
{
    public static IPDBAtomFilter getAtomFilter(AtomFilterType filterType) 
    {
        switch (filterType) 
        {
            case CA:

                return new CAAtomFilter();

            case CB:

                return new CBAtomFilter();

            case AB:

                return new CarbonylAtomFilter();

            case AVERAGE:

                return new AverageAtomsFilter();

            case AVR:

                return new AverageRAtomsFilter();

            default:
                return null;
        }
    }
}
