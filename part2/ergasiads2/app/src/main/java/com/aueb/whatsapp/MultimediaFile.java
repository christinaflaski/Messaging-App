package com.aueb.whatsapp;

public class MultimediaFile {
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
