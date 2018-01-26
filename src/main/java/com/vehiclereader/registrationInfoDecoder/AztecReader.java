package com.vehiclereader.registrationInfoDecoder;

import java.nio.charset.Charset;
import java.util.ArrayList;

public class AztecReader {
    public String read(String data){

        int padPos = data.lastIndexOf('=');
        if (padPos > 0) {
            data = data.substring(0, padPos + 1);
        } else {
            padPos = data.lastIndexOf('/');
            data = data.substring(0, padPos) + "=";
        }

        byte[] encodedBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(data);
        BitsReader bitsReader = new BitsReader(encodedBytes, 4);
        ArrayList<Byte> output = new ArrayList<Byte>();
        int off_copy = 0;

        while(true)
        {
            if (bitsReader.isEndOfData()) {
                break;
            }

            while(bitsReader.readBit() == 1)
            {
                int readByte = bitsReader.readByte();
                output.add((byte)readByte);
            }
            int off = 1;
            int lng = 0;
            while(true)
            {
                if (bitsReader.isEndOfData()) {
                    break;
                }

                off *= 2;
                off += bitsReader.readBit();
                if(bitsReader.readBit() == 1)
                {
                    break;
                }
                off--;
                off *= 2;
                off += bitsReader.readBit();
            }
            if(off == 2)
            {
                lng = bitsReader.readBit();
                off = off_copy;
            }
            else
            {
                off -= 3;
                off = off << 8;
                int b = bitsReader.readByte();
                off += b;
                if(bitsReader.isEndOfData())
                {
                    return getDecodedText(output);
                }
                lng = (off ^ 1) & 1;
            }
            if(lng == 0)
            {
                if(bitsReader.readBit() == 1)
                {
                    lng = 3 + bitsReader.readBit();
                }
                else
                {
                    ++lng;
                    do
                    {
                        lng *= 2;
                        lng += bitsReader.readBit();
                    }
                    while(bitsReader.readBit() == 0);
                    lng += 3;
                }
            }
            else
            {
                lng = 1 + bitsReader.readBit();
            }

            if (off < 0){
                off = (off & 0xFF);
            }

            off_copy = off;
            if((off >>> 1) > 0x500){
                ++lng;
            }

            long offset = off >>> 1;
            long startPos = output.size() - 1 - offset;
            long length = lng;

            while (length-- >= 0) {
                long pos = startPos++;
                byte exByte = output.get((int)pos);
                output.add(exByte);
            }
        }
        return getDecodedText(output);
    }

    private String getDecodedText(ArrayList<Byte> bytes){
        byte[] b = new byte[bytes.size()];
        for (int i = 0; i < b.length; i++) {
            b[i] = (byte) bytes.get(i).byteValue();
        }
        return new String(b, Charset.forName("UTF-16LE"));
    }
}
