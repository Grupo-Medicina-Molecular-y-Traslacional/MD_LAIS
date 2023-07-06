package tomocomd.camps.mdlais.local;

/**
 *
 * @author econtreras
 */
public class LocalFactory {

    static public ILocal getLocal(LocalType[] localTypes) {
        ILocal local = null;
        int n = localTypes.length;

        if (n == 1) {
            switch (localTypes[0]) {
                // 10 side-chain locals
                case total:

                    local = new Total();

                    break;

                case apolar:

                    local = new Apolar();

                    break;

                case polar_positively_charged:

                    local = new PolarPositivelyCharged();

                    break;

                case polar_negatively_charged:

                    local = new PolarNegativelyCharged();

                    break;

                case polar_uncharged:

                    local = new PolarUncharged();

                    break;

                case aromatic:

                    local = new Aromatic();

                    break;

                case aliphatic:

                    local = new Aliphatic();

                    break;

                case unfolding:

                    local = new Unfolding();

                    break;

                case alpha_helix_favoring:

                    local = new AlphaHelixFavoring();

                    break;

                case beta_sheet_favoring:

                    local = new BetaSheetFavoring();

                    break;

                case beta_turn_favoring:

                    local = new BetaTurnFavoring();

                    break;
            }

            return local != null ? local : new AminoAcid(localTypes[0]);
        } else 
        {
            if (isKmer(localTypes)) 
            {
                return new KMer(localTypes);
            }
            
            return new MultiLocal(localTypes);
        }
    }

    static boolean isKmer(LocalType[] loc) 
    {
        for (LocalType localType : loc) 
        {
            boolean isamino = LocalFactory.getLocal(new LocalType[]{localType}) instanceof AminoAcid;

            if (!isamino) 
            {
                return false;
            }
        }

        return true;
    }
}
