package common.util;

import java.io.FileInputStream;

import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONObject;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class JsonUtilTest {
    private DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

    @Test
    public void testXml2Json() throws Exception {
        // String addr =
        // "http://ecs.amazonaws.com/onca/xml?AWSAccessKeyId=1T1HRK7KA46TMYXP9GR2&ItemId=B0039JBXVY%2C0153467916&Operation=ItemLookup&ResponseGroup=Small%2CBrowseNodes%2CImages%2CItemAttributes%2CItemIds%2CLarge%2CMedium%2CAccessories%2CEditorialReview%2COfferFull%2COffers%2COfferSummary%2CTracks%2CVariationImages%2CVariationSummary%2CReviews%2CSalesRank%2CSimilarities&Service=AWSECommerceService&TagsPerPage=10&Timestamp=2011-01-18T16%3A44%3A31Z&Version=2010-09-01&Signature=Fy7wRfvpV4R4%2B83Fl1KqaNxK1SQAOov%2F9hmyeuTOFR0%3D";
        // URL url = new URL(addr);
        // HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        // InputSource inputSource = new InputSource(conn.getInputStream());

        InputSource inputSource = new InputSource(new FileInputStream("src/test/resources/item.xml"));

        Document document = factory.newDocumentBuilder().parse(inputSource);
        JSONObject json = JsonUtil.toJson(document.getDocumentElement());
        System.out.println(json.toString());
    }
}
