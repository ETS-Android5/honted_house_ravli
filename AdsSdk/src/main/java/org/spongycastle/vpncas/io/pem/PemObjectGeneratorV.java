/*
 * Copyright (c) 2012-2016 Arne Schwabe
 * Distributed under the GNU GPL v2 with additional terms. For full terms see the file doc/LICENSE.txt
 */

package org.spongycastle.vpncas.io.pem;

public interface PemObjectGeneratorV
{
    PemObjectV generate()
        throws PemGenerationExceptionV;
}