package com.gaoshin.business;

public class EmailGatewayLookup {
    public static String getEmail(PhoneInfo pi) {
        if (pi == null)
            return null;

        if ("us".equalsIgnoreCase(pi.getNetworkCountryIso())) {
            if ("verizon wireless".equalsIgnoreCase(pi.getNetworkOperatorName())
                    || ("verizon".equalsIgnoreCase(pi.getSimOperatorName()))) {
                return pi.getLine1Number() + "@vtext.com";
            }

            if ("t-mobile".equalsIgnoreCase(pi.getNetworkOperatorName())
                    || ("t-mobile".equalsIgnoreCase(pi.getSimOperatorName()))) {
                return pi.getLine1Number() + "@tmomail.net";
            }
        }

        return null;
    }
}
