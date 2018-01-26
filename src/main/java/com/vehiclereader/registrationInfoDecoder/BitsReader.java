package com.vehiclereader.registrationInfoDecoder;

public class BitsReader {
    private byte[] data;
    private int dataPosition;
    private int currentByte;
    private int bitPosition;
    private boolean endOfData;

    public BitsReader(byte[] data, int startIdx){
        this.data = data;
        dataPosition = startIdx;
        bitPosition = 0;
        endOfData = false;
    }

    public boolean isEndOfData(){
        return endOfData;
    }

    public int readBit() {
        if (endOfData)
            return -1;
        if (bitPosition == 0) {
            if (dataPosition >= data.length) {
                endOfData = true;
                return -1;
            }
            currentByte = data[dataPosition++] & (0xff);
            bitPosition = 8;
        }
        bitPosition--;
        return (currentByte >>> bitPosition) & 1;
    }

    public int readByte() {
        if (endOfData)
            return -1;
        if (dataPosition >= data.length) {
            endOfData = true;
            return -1;
        }
        return data[dataPosition++] & (0xff);
    }
}
