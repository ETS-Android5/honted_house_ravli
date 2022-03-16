/*
 * Copyright (c) 2012-2016 Arne Schwabe
 * Distributed under the GNU GPL v2 with additional terms. For full terms see the file doc/LICENSE.txt
 */

package org.spongycastle.vpncas.io.pem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.spongycastle.vpncas.encoders.Base64V;

public class PemReaderV extends BufferedReader {
    private static final String BEGIN = "-----BEGIN ";
    private static final String END = "-----END ";

    public PemReaderV(Reader reader) {
        super(reader);
    }

    public PemObjectV readPemObject() throws IOException {
        String line = readLine();

        while (line != null && !line.startsWith(BEGIN)) {
            line = readLine();
        }

        if (line != null) {
            line = line.substring(BEGIN.length());
            int index = line.indexOf('-');
            String type = line.substring(0, index);

            if (index > 0) {
                return loadObject(type);
            }
        }

        return null;
    }

    private PemObjectV loadObject(String type) throws IOException {
        String line;
        String endMarker = END + type;
        StringBuilder buf = new StringBuilder();
        List headers = new ArrayList();

        while ((line = readLine()) != null) {
            if (line.indexOf(":") >= 0) {
                int index = line.indexOf(':');
                String hdr = line.substring(0, index);
                String value = line.substring(index + 1).trim();

                headers.add(new PemHeaderV(hdr, value));

                continue;
            }

            if (line.indexOf(endMarker) != -1) {
                break;
            }

            buf.append(line.trim());
        }

        if (line == null) {
            throw new IOException(endMarker + " not found");
        }

        return new PemObjectV(type, headers, Base64V.decode(buf.toString()));
    }

}
