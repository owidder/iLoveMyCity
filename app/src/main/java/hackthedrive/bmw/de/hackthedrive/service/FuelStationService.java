package hackthedrive.bmw.de.hackthedrive.service;

import android.location.Location;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathFactory;

import hackthedrive.bmw.de.hackthedrive.R;
import hackthedrive.bmw.de.hackthedrive.util.LocationUtil;
import hackthedrive.bmw.de.hackthedrive.util.LogUtil;
import hackthedrive.bmw.de.hackthedrive.util.RestClient;

/**
 * Created by dst on 11.01.2015.
 */
public class FuelStationService {
    private static final LogUtil logger = LogUtil.getLogger(FuelStationService.class);
    public static final String USER = "b207195d0b1684270db5aeae7970408c5179ce9f5a4dc1366937247";
    public static final String PASS = "167fb3e18980d8622f6a19fbbda3e01d";
    public static final String WS_URL = "https://webservices.chargepoint.com/webservices/chargepoint/services/4.1";
    public static final String XML_INPUT = "  <soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:web=\"http://litwinconsulting.com/webservices/\">\n" +
            "	<soap:Header>\n" +
            "		<wsse:Security xmlns:wsse='http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd' soap:mustUnderstand='1'>\n" +
            "			<wsse:UsernameToken xmlns:wsu='http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd' wsu:Id='UsernameToken-261'>\n" +
            "				<wsse:Username>%s</wsse:Username>\n" +
            "				<wsse:Password Type='http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText'>%s</wsse:Password>\n" +
            "			</wsse:UsernameToken>\n" +
            "		</wsse:Security>\n" +
            "   </soap:Header>\n" +
            "   <soap:Body>\n" +
            "		<ns2:getPublicStations xmlns:ns2='http://www.example.org/coulombservices/'>\n" +
            "			<searchQuery>\n" +
            "				<Proximity>%s</Proximity>\n" +
            "				<proximityUnit>M</proximityUnit>\n" +
            "				<Geo>\n" +
            "					<Lat>%s</Lat>\n" +
            "					<Long>%s</Long>\n" +
            "				</Geo>\n" +
            "			</searchQuery>\n" +
            "		</ns2:getPublicStations>\n" +
            "   </soap:Body>\n" +
            "  </soap:Envelope>\n";


    public List<Location> getChargingStations(Location center){
        List<Location> chargingStations = new ArrayList<>();
        try {
            String xml = getData(center, 10);
            logger.d("Charing stations: %s", xml);

            DocumentBuilderFactory builderFactory =
                    DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = null;
            try {
                builder = builderFactory.newDocumentBuilder();
                Document xmlDocument = builder.parse(new ByteArrayInputStream(xml.getBytes()));
                XPath xPath =  XPathFactory.newInstance().newXPath();
                String expression = "/stationData";
                // String email = xPath.compile(expression).evaluate(xmlDocument);
                // Node node = (Node) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODE);
                NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET);
                for(int i=0; i<nodeList.getLength(); i++){
                    Node node = nodeList.item(i);
                    NodeList stationData = node.getChildNodes();
                    Double lat = null;
                    Double lon = null;
                    for(int j=0; j<stationData.getLength(); j++) {
                        Node nodeInside = stationData.item(i);
                        if(nodeInside.getNodeName().equals("Lat")){
                            lat = Double.valueOf(nodeInside.getNodeValue());
                        } else if(nodeInside.getNodeName().equals("Long")){
                            lon = Double.valueOf(nodeInside.getNodeValue());
                        }
                    }
                    if(lat != null && lon != null){
                        chargingStations.add(LocationUtil.createLocation(lat, lon));
                    }
                }
            } catch (ParserConfigurationException |SAXException | XPathException e){
                // TODO
            }

        } catch (IOException e) {
            logger.w(e,"Charing stations could not be fetched. %s", e.getMessage());
        }
        return chargingStations;
    }

    public static String getData(Location center, long proximity) throws MalformedURLException, IOException {

        //Code to make a webservice HTTP request
        String responseString = "";
        String outputString = "";

        //String wsURL = "http://www.deeptraining.com/webservices/weather.asmx";
        URL url = new URL(WS_URL);
        URLConnection connection = url.openConnection();
        HttpURLConnection httpConn = (HttpURLConnection)connection;
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        String message = String.format(XML_INPUT, USER, PASS, proximity, center.getLatitude(), center.getLongitude());
        byte[] buffer = new byte[message.length()];
        buffer = message.getBytes();
        bout.write(buffer);
        byte[] b = bout.toByteArray();

        //String SOAPAction = "http://litwinconsulting.com/webservices/GetWeather";
        String SOAPAction = "urn:provider/interface/chargepointservices/getPublicStations";

        // Set the appropriate HTTP parameters.
        httpConn.setRequestProperty("Content-Length",
                String.valueOf(b.length));
        httpConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
        httpConn.setRequestProperty("SOAPAction", SOAPAction);
        httpConn.setRequestMethod("POST");
        httpConn.setDoOutput(true);
        httpConn.setDoInput(true);
        OutputStream out = httpConn.getOutputStream();
        //Write the content of the request to the outputstream of the HTTP Connection.
        out.write(b);
        out.close();
        //Ready with sending the request.

        //Read the response.
        InputStreamReader isr =
                new InputStreamReader(httpConn.getInputStream());
        BufferedReader in = new BufferedReader(isr);

        //Write the SOAP message response to a String.
        while ((responseString = in.readLine()) != null) {
            outputString = outputString + responseString;
        }
        return outputString;
    }
}
