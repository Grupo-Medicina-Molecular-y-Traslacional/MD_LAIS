package tomocomd.camps.mdlais.local;

/**
 *
 * @author econtreras
 */
/*
apolar (RAP)
polar positively charged (RPC)
polar negatively charged (RNC) 
polar uncharged (RPU) 
aromatic (ARO) 
aliphatic (ALG)
commonly NOT found in proteins as part of α-helices or β-sheets (UFG)
α-helices favoring amino acids (FAH) 
β-sheets favoring amino acids (FBS) 
β-turns favoring amino acids (AFT) 
es decir 10 locales de grupo y 20 de tipo de AA, 30 locales total. 
Alanine
Arginine
Asparagine
Aspartate
Cysteine
Glutamate
Glutamine
Glycine
Histidine
Isoleucine
Leucine
Lysine
Methionine
Phenylalanine
Proline
Serine
Threonine
Tryptophan
Tyrosine
Valine
*/
public enum LocalType 
{
    total,apolar,polar_positively_charged,polar_negatively_charged,polar_uncharged,aromatic,aliphatic,unfolding,
    alpha_helix_favoring,beta_sheet_favoring,beta_turn_favoring,alanine,arginine,asparagine,aspartate,
    cysteine,glutamate,glutamine,glycine,histidine,isoleucine,leucine,lysine,methionine,phenylalanine,
    proline,serine,threonine,tryptophan,tyrosine,valine
}
