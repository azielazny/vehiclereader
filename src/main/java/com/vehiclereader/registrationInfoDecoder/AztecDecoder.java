package com.vehiclereader.registrationInfoDecoder;

import java.util.regex.Pattern;

public class AztecDecoder {
    public static RegistrationInfo Decode(String data) throws AztecDecodingException {

        try {
            String rawData = new AztecReader().read(data);

            RegistrationInfo info = new RegistrationInfo();
            String[] array = rawData.split(Pattern.quote("|"));
            if ((array[0].length() > 4)) {
                OldVehicleRegistrationCertyficate.completRegistrationInfo(info, array);
            } else {
                NewVehicleRegistrationCertyficate.completRegistrationInfo(info, array);
            }
            return info;

        } catch (Exception ex) {
            System.out.println(ex);
            throw new AztecDecodingException();
        }
    }


}
