package com.pesonal.adsdk.vpn;

import java.util.Locale;

public class Converter {
    public static String humanReadableByteCountOld(long j, boolean z) {
        int i = z ? 1000 : 1024;
        if (j < ((long) i)) {
            return j + " B";
        }
        double d = (double) j;
        double d2 = (double) i;
        int log = (int) (Math.log(d) / Math.log(d2));
        StringBuilder sb = new StringBuilder();
        sb.append((z ? "kMGTPE" : "KMGTPE").charAt(log - 1));
        sb.append(z ? "" : "i");
        String sb2 = sb.toString();
        Locale locale = Locale.ENGLISH;
        double pow = Math.pow(d2, (double) log);
        Double.isNaN(d);
        return String.format(locale, "%.1f %sB", Double.valueOf(d / pow), sb2);
    }

    public static String megabyteCount(long j) {
        Locale locale = Locale.getDefault();
        double d = (double) j;
        Double.isNaN(d);
        return String.valueOf(String.format(locale, "%.0f", Double.valueOf((d / 1024.0d) / 1024.0d)));
    }
}
