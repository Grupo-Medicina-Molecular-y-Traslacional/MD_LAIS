package tomocomd.camps.mdlais.pdbfilter;

/**
 *
 * @author econtreras
 */
public class ExperimentInformation {

    private String seqID, fileName, technique, resolution;

    public ExperimentInformation(String seqID, String fileName, String technique, String resolution) 
    {
        this.seqID = seqID;

        this.fileName = fileName;

        this.technique = technique;

        this.resolution = resolution;
    }

    public String getSeqID() {
        return seqID;
    }

    public void setSeqID(String seqID) {
        this.seqID = seqID;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getTechnique() {
        return technique;
    }

    public void setTechnique(String technique) {
        this.technique = technique;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }
}
