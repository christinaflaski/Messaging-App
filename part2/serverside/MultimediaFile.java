import java.io.Serializable;

public class MultimediaFile implements Serializable {
    private String name;
    private byte[] chunk;

    public MultimediaFile(String name, byte[] chunk) {
        this.name = name;
        this.chunk = chunk;
    }
    public String getMFileName() {
        return name;
    }

    public byte[] getChunk(){ return chunk; }
}
