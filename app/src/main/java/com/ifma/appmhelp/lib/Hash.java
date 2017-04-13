package com.ifma.appmhelp.lib;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by leo on 4/12/17.
 */

public class Hash {
    private static final SecureRandom random = new SecureRandom();

    public static String getHash() {
        return new BigInteger(130, random).toString(32);
    }

}
