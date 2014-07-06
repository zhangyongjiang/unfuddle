package common.geo;

import java.util.HashMap;
import java.util.Map;

public class PostalAbbreviation {
    public static final Map<String, String> long2Short = new HashMap<String, String>() {
        {
            put("ALLEE", "ALY");
            put("ALLEY", "ALY");
            put("ALLY", "ALY");
            put("ANEX", "ANX");
            put("ANNEX", "ANX");
            put("ANNX", "ANX");
            put("APARTMENT", "APT");
            put("ARCADE", "ARC");
            put("AV", "AVE");
            put("AVEN", "AVE");
            put("AVENU", "AVE");
            put("AVENUE", "AVE");
            put("AVN", "AVE");
            put("AVNUE", "AVE");
            put("BASEMENT", "BSMT");
            put("BAYOO", "BYU");
            put("BAYOU", "BYU");
            put("BEACH", "BCH");
            put("BEND", "BND");
            put("BLUF", "BLF");
            put("BLUFF", "BLF");
            put("BLUFFS", "BLFS");
            put("BOT", "BTM");
            put("BOTTM", "BTM");
            put("BOTTOM", "BTM");
            put("BOUL", "BLVD");
            put("BOULEVARD", "BLVD");
            put("BOULV", "BLVD");
            put("BRANCH", "BR");
            put("BRDGE", "BRG");
            put("BRIDGE", "BRG");
            put("BRNCH", "BR");
            put("BROOK", "BRK");
            put("BROOKS", "BRKS");
            put("BUILDING", "BLDG");
            put("BURG", "BG");
            put("BURGS", "BGS");
            put("BYPA", "BYP");
            put("BYPAS", "BYP");
            put("BYPASS", "BYP");
            put("BYPS", "BYP");
            put("CAMP", "CP");
            put("CANYN", "CYN");
            put("CANYON", "CYN");
            put("CAPE", "CPE");
            put("CAUSEWAY", "CSWY");
            put("CAUSWAY", "CSWY");
            put("CEN", "CTR");
            put("CENT", "CTR");
            put("CENTER", "CTR");
            put("CENTERS", "CTRS");
            put("CENTR", "CTR");
            put("CENTRE", "CTR");
            put("CIRC", "CIR");
            put("CIRCL", "CIR");
            put("CIRCLE", "CIR");
            put("CIRCLES", "CIRS");
            put("CK", "CRK");
            put("CLIFF", "CLF");
            put("CLIFFS", "CLFS");
            put("CLUB", "CLB");
            put("CMP", "CP");
            put("CNTER", "CTR");
            put("CNTR", "CTR");
            put("CNYN", "CYN");
            put("COMMON", "CMN");
            put("CORNER", "COR");
            put("CORNERS", "CORS");
            put("COURSE", "CRSE");
            put("COURT", "CT");
            put("COURTS", "CTS");
            put("COVE", "CV");
            put("COVES", "CVS");
            put("CR", "CRK");
            put("CRCL", "CIR");
            put("CRCLE", "CIR");
            put("CRECENT", "CRES");
            put("CREEK", "CRK");
            put("CRESCENT", "CRES");
            put("CRESENT", "CRES");
            put("CREST", "CRST");
            put("CROSSING", "XING");
            put("CROSSROAD", "XRD");
            put("CRSCNT", "CRES");
            put("CRSENT", "CRES");
            put("CRSNT", "CRES");
            put("CRSSING", "XING");
            put("CRSSNG", "XING");
            put("CRT", "CT");
            put("CURVE", "CURV");
            put("DALE", "DL");
            put("DAM", "DM");
            put("DEPARTMENT", "DEPT");
            put("DIV", "DV");
            put("DIVIDE", "DV");
            put("DRIV", "DR");
            put("DRIVE", "DR");
            put("DRIVES", "DRS");
            put("DRV", "DR");
            put("DVD", "DV");
            put("ESTATE", "EST");
            put("ESTATES", "ESTS");
            put("EXP", "EXPY");
            put("EXPR", "EXPY");
            put("EXPRESS", "EXPY");
            put("EXPRESSWAY", "EXPY");
            put("EXPW", "EXPY");
            put("EXTENSION", "EXT");
            put("EXTENSIONS", "EXTS");
            put("EXTN", "EXT");
            put("EXTNSN", "EXT");
            put("FALLS", "FLS");
            put("FERRY", "FRY");
            put("FIELD", "FLD");
            put("FIELDS", "FLDS");
            put("FLAT", "FLT");
            put("FLATS", "FLTS");
            put("FLOOR", "FL");
            put("FORD", "FRD");
            put("FORDS", "FRDS");
            put("FOREST", "FRST");
            put("FORESTS", "FRST");
            put("FORG", "FRG");
            put("FORGE", "FRG");
            put("FORGES", "FRGS");
            put("FORK", "FRK");
            put("FORKS", "FRKS");
            put("FORT", "FT");
            put("FREEWAY", "FWY");
            put("FREEWY", "FWY");
            put("FRONT", "FRNT");
            put("FRRY", "FRY");
            put("FRT", "FT");
            put("FRWAY", "FWY");
            put("FRWY", "FWY");
            put("GARDEN", "GDN");
            put("GARDENS", "GDNS");
            put("GARDN", "GDN");
            put("GATEWAY", "GTWY");
            put("GATEWY", "GTWY");
            put("GATWAY", "GTWY");
            put("GLEN", "GLN");
            put("GLENS", "GLNS");
            put("GRDEN", "GDN");
            put("GRDN", "GDN");
            put("GRDNS", "GDNS");
            put("GREEN", "GRN");
            put("GREENS", "GRNS");
            put("GROV", "GRV");
            put("GROVE", "GRV");
            put("GROVES", "GRVS");
            put("GTWAY", "GTWY");
            put("HANGAR", "HNGR");
            put("HARB", "HBR");
            put("HARBOR", "HBR");
            put("HARBORS", "HBRS");
            put("HARBR", "HBR");
            put("HAVEN", "HVN");
            put("HAVN", "HVN");
            put("HEIGHT", "HTS");
            put("HEIGHTS", "HTS");
            put("HGTS", "HTS");
            put("HIGHWAY", "HWY");
            put("HIGHWY", "HWY");
            put("HILL", "HL");
            put("HILLS", "HLS");
            put("HIWAY", "HWY");
            put("HIWY", "HWY");
            put("HLLW", "HOLW");
            put("HOLLOW", "HOLW");
            put("HOLLOWS", "HOLW");
            put("HOLWS", "HOLW");
            put("HRBOR", "HBR");
            put("HT", "HTS");
            put("HWAY", "HWY");
            put("INLET", "INLT");
            put("ISLAND", "IS");
            put("ISLANDS", "ISS");
            put("ISLES", "ISLE");
            put("ISLND", "IS");
            put("ISLNDS", "ISS");
            put("JCTION", "JCT");
            put("JCTN", "JCT");
            put("JCTNS", "JCTS");
            put("JUNCTION", "JCT");
            put("JUNCTIONS", "JCTS");
            put("JUNCTN", "JCT");
            put("JUNCTON", "JCT");
            put("KEY", "KY");
            put("KEYS", "KYS");
            put("KNOL", "KNL");
            put("KNOLL", "KNL");
            put("KNOLLS", "KNLS");
            put("LA", "LN");
            put("LAKE", "LK");
            put("LAKES", "LKS");
            put("LANDING", "LNDG");
            put("LANE", "LN");
            put("LANES", "LN");
            put("LDGE", "LDG");
            put("LIGHT", "LGT");
            put("LIGHTS", "LGTS");
            put("LNDNG", "LNDG");
            put("LOAF", "LF");
            put("LOBBY", "LBBY");
            put("LOCK", "LCK");
            put("LOCKS", "LCKS");
            put("LODG", "LDG");
            put("LODGE", "LDG");
            put("LOOPS", "LOOP");
            put("LOWER", "LOWR");
            put("MANOR", "MNR");
            put("MANORS", "MNRS");
            put("MEADOW", "MDW");
            put("MEADOWS", "MDWS");
            put("MEDOWS", "MDWS");
            put("MILL", "ML");
            put("MILLS", "MLS");
            put("MISSION", "MSN");
            put("MISSN", "MSN");
            put("MNT", "MT");
            put("MNTAIN", "MTN");
            put("MNTN", "MTN");
            put("MNTNS", "MTNS");
            put("MOTORWAY", "MTWY");
            put("MOUNT", "MT");
            put("MOUNTAIN", "MTN");
            put("MOUNTAINS", "MTNS");
            put("MOUNTIN", "MTN");
            put("MSSN", "MSN");
            put("MTIN", "MTN");
            put("NECK", "NCK");
            put("OFFICE", "OFC");
            put("ORCHARD", "ORCH");
            put("ORCHRD", "ORCH");
            put("OVERPASS", "OPAS");
            put("OVL", "OVAL");
            put("PARKS", "PARK");
            put("PARKWAY", "PKWY");
            put("PARKWAYS", "PKWY");
            put("PARKWY", "PKWY");
            put("PASSAGE", "PSGE");
            put("PATHS", "PATH");
            put("PENTHOUSE", "PH");
            put("PIKES", "PIKE");
            put("PINE", "PNE");
            put("PINES", "PNES");
            put("PK", "PARK");
            put("PKWAY", "PKWY");
            put("PKWYS", "PKWY");
            put("PKY", "PKWY");
            put("PLACE", "PL");
            put("PLAIN", "PLN");
            put("PLAINES", "PLNS");
            put("PLAINS", "PLNS");
            put("PLAZA", "PLZ");
            put("PLZA", "PLZ");
            put("POINT", "PT");
            put("POINTS", "PTS");
            put("PORT", "PRT");
            put("PORTS", "PRTS");
            put("PRAIRIE", "PR");
            put("PRARIE", "PR");
            put("PRK", "PARK");
            put("PRR", "PR");
            put("RAD", "RADL");
            put("RADIAL", "RADL");
            put("RADIEL", "RADL");
            put("RANCH", "RNCH");
            put("RANCHES", "RNCH");
            put("RAPID", "RPD");
            put("RAPIDS", "RPDS");
            put("RDGE", "RDG");
            put("REST", "RST");
            put("RIDGE", "RDG");
            put("RIDGES", "RDGS");
            put("RIVER", "RIV");
            put("RIVR", "RIV");
            put("RNCHS", "RNCH");
            put("ROAD", "RD");
            put("ROADS", "RDS");
            put("ROOM", "R");
            put("ROOM", "RM");
            put("ROUTE", "RT");
            put("ROUTE", "RTE");
            put("RVR", "RIV");
            put("SHOAL", "SHL");
            put("SHOALS", "SHLS");
            put("SHOAR", "SHR");
            put("SHOARS", "SHRS");
            put("SHORE", "SHR");
            put("SHORES", "SHRS");
            put("SKYWAY", "SKWY");
            put("SPACE", "SPC");
            put("SPNG", "SPG");
            put("SPNGS", "SPGS");
            put("SPRING", "SPG");
            put("SPRINGS", "SPGS");
            put("SPRNG", "SPG");
            put("SPRNGS", "SPGS");
            put("SPURS", "SPUR");
            put("SQR", "SQ");
            put("SQRE", "SQ");
            put("SQRS", "SQS");
            put("SQU", "SQ");
            put("SQUARE", "SQ");
            put("SQUARES", "SQS");
            put("STATION", "STA");
            put("STATN", "STA");
            put("STN", "STA");
            put("STR", "ST");
            put("STRAV", "STRA");
            put("STRAVE", "STRA");
            put("STRAVEN", "STRA");
            put("STRAVENUE", "STRA");
            put("STRAVN", "STRA");
            put("STREAM", "STRM");
            put("STREET", "ST");
            put("STREETS", "STS");
            put("STREME", "STRM");
            put("STRT", "ST");
            put("STRVN", "STRA");
            put("STRVNUE", "STRA");
            put("SUITE", "STE");
            put("SUMIT", "SMT");
            put("SUMITT", "SMT");
            put("SUMMIT", "SMT");
            put("TERR", "TER");
            put("TERRACE", "TER");
            put("THROUGHWAY", "TRWY");
            put("TPK", "TPKE");
            put("TR", "TRL");
            put("TRACE", "TRCE");
            put("TRACES", "TRCE");
            put("TRACK", "TRAK");
            put("TRACKS", "TRAK");
            put("TRAFFICWAY", "TRFY");
            put("TRAIL", "TRL");
            put("TRAILER", "TRLR");
            put("TRAILS", "TRL");
            put("TRK", "TRAK");
            put("TRKS", "TRAK");
            put("TRLS", "TRL");
            put("TRNPK", "TPKE");
            put("TRPK", "TPKE");
            put("TUNEL", "TUNL");
            put("TUNLS", "TUNL");
            put("TUNNEL", "TUNL");
            put("TUNNELS", "TUNL");
            put("TUNNL", "TUNL");
            put("TURNPIKE", "TPKE");
            put("TURNPK", "TPKE");
            put("UNDERPASS", "UPAS");
            put("UNION", "UN");
            put("UNIONS", "UNS");
            put("UPPER", "UPPR");
            put("VALLEY", "VLY");
            put("VALLEYS", "VLYS");
            put("VALLY", "VLY");
            put("VDCT", "VIA");
            put("VIADCT", "VIA");
            put("VIADUCT", "VIA");
            put("VIEW", "VW");
            put("VIEWS", "VWS");
            put("VILL", "VLG");
            put("VILLAG", "VLG");
            put("VILLAGE", "VLG");
            put("VILLAGES", "VLGS");
            put("VILLE", "VL");
            put("VILLG", "VLG");
            put("VILLIAGE", "VLG");
            put("VIST", "VIS");
            put("VISTA", "VIS");
            put("VLLY", "VLY");
            put("VST", "VIS");
            put("VSTA", "VIS");
            put("WALKS", "WALK");
            put("WELL", "WL");
            put("WELLS", "WLS");
            put("WY", "WAY");
            
            put("NORTH", "N");
            put("EAST", "E");
            put("SOUTH", "S");
            put("WEST", "W");
            put("NORTHEAST", "NE");
            put("SOUTHEAST", "SE");
            put("NORTHWEST", "NW");
            put("SOUTHWEST", "SW");
        }
    };

    public static final Map<String, String> short2Long = new HashMap<String, String>() {
        {
            for (Map.Entry<String, String> entry : long2Short.entrySet()) {
                put(entry.getValue(), entry.getKey());
            }
        }
    };
}