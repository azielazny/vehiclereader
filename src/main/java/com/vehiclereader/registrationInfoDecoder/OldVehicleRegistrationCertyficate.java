package com.vehiclereader.registrationInfoDecoder;

public class OldVehicleRegistrationCertyficate {
    protected static void completRegistrationInfo(RegistrationInfo info, String[] array) {
        info.SeriaDr = array[1];
        info.OrganWydajacyNazwa = array[3];
        info.OrganWydajacyUlica = array[4];
        info.OrganWydajacyMiasto = array[5];
        info.NrRejestracyjny = array[7];
        info.MarkaPojazdu = array[8];
        info.TypPojazdu = array[9];
        info.WariantPojazdu = array[10];
        info.WersjaPojazdu = array[11];
        info.ModelPojazdu = array[12];
        info.VinNrNadwozia = array[13];
        info.DataWydaniaAktualnegoDr = array[14];
        info.NazwaPosiadaczaDr = array[16];
        info.ImionaPosiadaczaDr = array[17];
        info.NazwiskoPosiadaczaDr = array[18];
        info.PeselRegonPosiadaczaDr = array[20];
        info.KodPocztowyPosiadaczaDr = array[21];
        info.MiastoPosiadaczaDr = array[22];
        info.PowiatPosiadaczaDr = array[23];
        info.UlicaPosiadaczaDr = array[24];
        info.NrDomuPosiadaczaDr = array[25];
        info.NrLokaluPosiadaczaDr = array[26];
        info.NazwaWlascicielaPojazdu = array[27];
        info.ImionaWlascicielaPojazdu = array[28];
        info.NazwiskoWlascicielaPojazdu = array[29];
        info.PeselRegonWlascicielaPojazdu = array[31];
        info.KodPocztowyWlascicielaPojazdu = array[32];
        info.MiastoWlascicielaPojazdu = array[33];
        info.PowiatWlascicielaPojazdu = array[34];
        info.UlicaWlascicielaPojazdu = array[35];
        info.NrDomuWlascicielaPojazdu = array[36];
        info.NrLokaluWlascicielaPojazdu = array[37];
        info.MaksymalnaMasaCalkowita = array[38];
        info.DopuszczalnaMasaCalkowitaPojazdu = array[39];
        info.DopuszczalnaMasaCalkowitaZespoluPojazdow = array[40];
        info.MasaWlasna = array[41];
        info.KategoriaPojazdu = array[42];
        info.NrSwiadectwaHomologacjiTypuPojazdu = array[43];
        info.LiczbaOsi = array[44];
        info.MaksymalnaMasaCalkowitaPrzyczepyZHamulcem = array[45];
        info.MaksymalnaMasaCalkowitaPrzyczepyBezHamulca = "-";
        info.StosunekMocyDoCiezaru = "-";
        info.Pojemnosc = "-";
        info.MocSilnika ="-";
        info.RodzajPaliwa = "-";
        info.DataPierwszejRejestracji = "-";
        info.MiejscaSiedzace = "-";
        info.MiejscaStojace = "-";
        info.RodzajPojazdu = "-";
        info.Przeznaczenie = "-";
        info.RokProdukcji = "-";
        info.DopuszczalnaLadownosc = "-";
        info.NajwiekszyDopuszczalnyNaciskOsi = "-";
        info.NrKartyPojazdu = "-";
        info.KodITS = "-";
    }
}