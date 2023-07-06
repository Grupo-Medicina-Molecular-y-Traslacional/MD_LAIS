package tomocomd.camps.mdlais.descriptors;

/**
 *
 * @author Cesar
 * @author econtreras
 */
import java.util.ArrayList;
import java.util.List;
import tomocomd.camps.mdlais.local.LocalType;
import tomocomd.camps.mdlais.weights.WeightConfiguration;
import tomocomd.camps.mdlais.properties.AminoAcidProperty;

public class MolecularDescriptorFactory {

    static public List<MolecularDescriptor> getAlgebraicDescriptors(MolecularDescriptorReader adr) {
        return getAlgebraicDescriptors(adr.isTotal(), adr.getAminoaAcidProperties(), adr.getLocals());
    }

    static public List<MolecularDescriptor> getAlgebraicDescriptorsFromReader(MolecularDescriptorReader adr) {
        return getAlgebraicDescriptors(adr.getClassicInvariants(), adr.getNoClassicInvariants(), adr.getAminoaAcidProperties(), adr.getLocals(), adr.getCutConfig());
    }

    static public List<MolecularDescriptor> getAlgebraicDescriptors(boolean isTotal,
            List<AminoAcidProperty> aminoAcidProperties,
            List<LocalType[]> locals) {

        List<MolecularDescriptor> instances = new ArrayList<>();

        for (AminoAcidProperty property : aminoAcidProperties) 
        {
            if (isTotal) 
            {
                instances.add(MolecularDescriptorFactory.newDescriptor(property, new LocalType[]{LocalType.total}));
            }

            for (LocalType[] clocal : locals) 
            {
               instances.add(MolecularDescriptorFactory.newDescriptor(property, clocal));
            }
        }

        return instances;
    }

    static public List<MolecularDescriptor> getAlgebraicDescriptors(
            List<String> classicInvs, List<String> noclassicInvs, List<AminoAcidProperty> aminoAcidProperties,
            List<LocalType[]> locals, WeightConfiguration cutConfig) {

        WeightConfiguration aux = cutConfig != null && cutConfig.isKa() ? new WeightConfiguration() : null;

        if (aux != null) {
            aux.setKa(true);
        }

        if (cutConfig != null) {
            cutConfig.setKa(false);
        }

        List<MolecularDescriptor> instances = new ArrayList<>();

        for (String classicInv : classicInvs) {
            if (!classicInv.equals("sic") && !classicInv.equals("sicn")
                    && !classicInv.equals("tic") && !classicInv.equals("ticn")) {
                for (String noClassicInv : noclassicInvs) {

                    for (AminoAcidProperty property: aminoAcidProperties) {

                        for (LocalType[] clocal : locals) 
                        {
                            if (aux != null) {
                                MolecularDescriptor adKA = newDescriptor(classicInv, noClassicInv, property, clocal);
                                adKA.setCutoff(aux);

                                instances.add(adKA);
                            }

                            MolecularDescriptor adCutoff = newDescriptor(classicInv, noClassicInv, property, clocal);

                            adCutoff.setCutoff(cutConfig);

                            instances.add(adCutoff);
                        }

                    }
                }
            } else {

                for (AminoAcidProperty comb : aminoAcidProperties) {

                    for (LocalType[] clocal : locals) {

                        if (aux != null) {
                            MolecularDescriptor adKA = newDescriptor(classicInv, "", comb, clocal);
                            adKA.setCutoff(aux);

                            instances.add(adKA);
                        }

                        MolecularDescriptor ad = newDescriptor(classicInv, "", comb, clocal);
                        ad.setCutoff(cutConfig);
                        instances.add(ad);
                    }

                }

            }
        }

        return instances;
    }

    static private MolecularDescriptor newDescriptor(String property, LocalType[] clocal) 
    {
        AminoAcidProperty prop = MolecularDescriptorHeading.propertyType.get(property);

        return new MolecularDescriptor(prop, clocal);
    }

    static private MolecularDescriptor newDescriptor(AminoAcidProperty property, LocalType[] clocal) 
    {
        return new MolecularDescriptor(property, clocal);
    }

    static public MolecularDescriptor newDescriptor(String classicInv, String noClassicInv, String comb, LocalType[] clocal, WeightConfiguration config) {
        MolecularDescriptor algDes = newDescriptor(comb, clocal);

        if (algDes != null) {
            algDes.setClassicInv(classicInv);

            algDes.setNoClassicInv(noClassicInv);

            if (config != null) {
                algDes.setCutoff(config);
            }
        }

        return algDes;
    }

    static public MolecularDescriptor newDescriptor(String classicInv, String noClassicInv, AminoAcidProperty property, LocalType[] clocal) {

        MolecularDescriptor algDes = newDescriptor(property, clocal);

        if (algDes != null) {
            algDes.setClassicInv(classicInv);
            algDes.setNoClassicInv(noClassicInv);
        }

        return algDes;
    }
}
