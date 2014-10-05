package dyl.anjon.es.traintrack.api;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.text.format.Time;

import dyl.anjon.es.traintrack.models.Station;
import dyl.anjon.es.traintrack.models.Operator;
import dyl.anjon.es.traintrack.utils.Utils;

public class Service {

	public static final String TABLE_NAME = "services";
	private String serviceId;
	private String serviceType;
	private Time generatedAt;
	private Operator operator;
	private String operatorCode;
	private String platform;
	private Station station;
	private String crs;
	private boolean isCancelled;
	private String scheduledTimeArrival;
	private String actualTimeArrival;
	private String estimatedTimeArrival;
	private String scheduledTimeDeparture;
	private String actualTimeDeparture;
	private String estimatedTimeDeparture;
	private String disruptionReason;
	private String overdueMessage;
	private ArrayList<CallingPoint> previousCallingPoints;
	private String previousCallingPointsServiceType;
	private ArrayList<CallingPoint> subsequentCallingPoints;
	private String subsequentCallingPointsServiceType;

	private Service(String serviceId, String xml) {

		this.serviceId = serviceId;
		this.previousCallingPoints = new ArrayList<CallingPoint>();
		this.subsequentCallingPoints = new ArrayList<CallingPoint>();

		Document doc = Utils.parseXml(xml);

		NodeList results = doc.getElementsByTagName("GetServiceDetailsResult");
		if (results.getLength() == 0) {
			return;
		}

		Node result = results.item(0);
		for (int i = 0; i < result.getChildNodes().getLength(); i++) {
			Node node = result.getChildNodes().item(i);
			if (node.getNodeName().equalsIgnoreCase("serviceType")) {
				setServiceType(node.getTextContent());
			} else if (node.getNodeName().equalsIgnoreCase("generatedAt")) {
				Time t = new Time();
				t.parse3339(node.getTextContent());
				setGeneratedAt(t);
				setStation(Station.getByCrs(getCrs()));
			} else if (node.getNodeName().equalsIgnoreCase("crs")) {
				setCrs(node.getTextContent());
				setStation(Station.getByCrs(getCrs()));
			} else if (node.getNodeName().equalsIgnoreCase("operatorCode")) {
				setOperatorCode(node.getTextContent());
				Operator operator = Operator.getByCode(getOperatorCode());
				if (operator != null) {
					setOperator(operator);
				} else {
					Utils.log("NO OPERATOR FOUND IN DB!");
				}
			} else if (node.getNodeName().equalsIgnoreCase("isCancelled")) {
				setCancelled(Boolean.valueOf(node.getTextContent()));
			} else if (node.getNodeName().equalsIgnoreCase("disruptionReason")) {
				setDisruptionReason(node.getTextContent());
			} else if (node.getNodeName().equalsIgnoreCase("overdueMessage")) {
				setOverdueMessage(node.getTextContent());
			} else if (node.getNodeName().equalsIgnoreCase("sta")) {
				setScheduledTimeArrival(node.getTextContent());
			} else if (node.getNodeName().equalsIgnoreCase("eta")) {
				setEstimatedTimeArrival(node.getTextContent());
			} else if (node.getNodeName().equalsIgnoreCase("ata")) {
				setActualTimeArrival(node.getTextContent());
			} else if (node.getNodeName().equalsIgnoreCase("std")) {
				setScheduledTimeDeparture(node.getTextContent());
			} else if (node.getNodeName().equalsIgnoreCase("etd")) {
				setEstimatedTimeDeparture(node.getTextContent());
			} else if (node.getNodeName().equalsIgnoreCase("atd")) {
				setActualTimeDeparture(node.getTextContent());
			} else if (node.getNodeName().equalsIgnoreCase("platform")) {
				setPlatform(node.getTextContent());
			}
		}

		NodeList callingPointsList = doc
				.getElementsByTagName("callingPointList");
		if (callingPointsList.getLength() > 0) {
			Node previousCallingPointsListNode = callingPointsList.item(0);

			Element previousCallingPointsListElement = (Element) previousCallingPointsListNode;
			this.previousCallingPointsServiceType = previousCallingPointsListElement
					.getAttribute("serviceType");

			NodeList callingPoints = previousCallingPointsListNode
					.getChildNodes();
			for (int i = 0; i < callingPoints.getLength(); i++) {
				Element cp = (Element) callingPoints.item(i);
				CallingPoint callingPoint = new CallingPoint(cp);
				this.previousCallingPoints.add(callingPoint);
			}
		}
		if (callingPointsList.getLength() > 1) {
			Node subsequentCallingPointsListNode = callingPointsList.item(1);
			Element subsequentCallingPointsListElement = (Element) subsequentCallingPointsListNode;
			this.subsequentCallingPointsServiceType = subsequentCallingPointsListElement
					.getAttribute("serviceType");

			NodeList callingPoints = subsequentCallingPointsListElement
					.getChildNodes();
			for (int i = 0; i < callingPoints.getLength(); i++) {
				Element cp = (Element) callingPoints.item(i);
				CallingPoint callingPoint = new CallingPoint(cp);
				this.subsequentCallingPoints.add(callingPoint);
			}
		}
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public Time getGeneratedAt() {
		return generatedAt;
	}

	public String getGeneratedAtString() {
		return "Last updated at " + generatedAt.format("%H:%m");
	}

	public void setGeneratedAt(Time generatedAt) {
		this.generatedAt = generatedAt;
	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public String getOperatorCode() {
		return operatorCode;
	}

	public void setOperatorCode(String operatorCode) {
		this.operatorCode = operatorCode;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public Station getStation() {
		return station;
	}

	public void setStation(Station station) {
		this.station = station;
	}

	public String getCrs() {
		return crs;
	}

	public void setCrs(String crs) {
		this.crs = crs;
	}

	public void setCancelled(Boolean isCancelled) {
		this.isCancelled = isCancelled;
	}

	public boolean isCancelled() {
		return isCancelled;
	}

	public String getScheduledTimeArrival() {
		return scheduledTimeArrival;
	}

	public void setScheduledTimeArrival(String scheduledTimeArrival) {
		this.scheduledTimeArrival = scheduledTimeArrival;
	}

	public String getActualTimeArrival() {
		return actualTimeArrival;
	}

	public void setActualTimeArrival(String actualTimeArrival) {
		this.actualTimeArrival = actualTimeArrival;
	}

	public String getEstimatedTimeArrival() {
		return estimatedTimeArrival;
	}

	public void setEstimatedTimeArrival(String estimatedTimeArrival) {
		this.estimatedTimeArrival = estimatedTimeArrival;
	}

	public String getScheduledTimeDeparture() {
		return scheduledTimeDeparture;
	}

	public void setScheduledTimeDeparture(String scheduledTimeDeparture) {
		this.scheduledTimeDeparture = scheduledTimeDeparture;
	}

	public String getActualTimeDeparture() {
		return actualTimeDeparture;
	}

	public void setActualTimeDeparture(String actualTimeDeparture) {
		this.actualTimeDeparture = actualTimeDeparture;
	}

	public String getEstimatedTimeDeparture() {
		return estimatedTimeDeparture;
	}

	public void setEstimatedTimeDeparture(String estimatedTimeDeparture) {
		this.estimatedTimeDeparture = estimatedTimeDeparture;
	}

	public String getDisruptionReason() {
		return disruptionReason;
	}

	public void setDisruptionReason(String disruptionReason) {
		this.disruptionReason = disruptionReason;
	}

	public String getOverdueMessage() {
		return overdueMessage;
	}

	public void setOverdueMessage(String overdueMessage) {
		this.overdueMessage = overdueMessage;
	}

	public ArrayList<CallingPoint> getPreviousCallingPoints() {
		return previousCallingPoints;
	}

	public void setPreviousCallingPoints(
			ArrayList<CallingPoint> previousCallingPoints) {
		this.previousCallingPoints = previousCallingPoints;
	}

	public String getPreviousCallingPointsServiceType() {
		return previousCallingPointsServiceType;
	}

	public void setPreviousCallingPointsServiceType(
			String previousCallingPointsServiceType) {
		this.previousCallingPointsServiceType = previousCallingPointsServiceType;
	}

	public ArrayList<CallingPoint> getSubsequentCallingPoints() {
		return subsequentCallingPoints;
	}

	public void setSubsequentCallingPoints(
			ArrayList<CallingPoint> subsequentCallingPoints) {
		this.subsequentCallingPoints = subsequentCallingPoints;
	}

	public String getSubsequentCallingPointsServiceType() {
		return subsequentCallingPointsServiceType;
	}

	public void setSubsequentCallingPointsServiceType(
			String subsequentCallingPointsServiceType) {
		this.subsequentCallingPointsServiceType = subsequentCallingPointsServiceType;
	}

	public static Service getByServiceId(String serviceId) {

		String body = "<soapenv:Body><typ:GetServiceDetailsRequest><typ:serviceID>"
				+ serviceId
				+ "</typ:serviceID></typ:GetServiceDetailsRequest></soapenv:Body>";

		String xml = Utils.SOAP_START + Utils.SOAP_HEADER + body
				+ Utils.SOAP_END;
		String xmlResponse = "";
		if (Utils.DEBUG_MODE) {
			xmlResponse = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><soap:Body><GetServiceDetailsResponse xmlns=\"http://thalesgroup.com/RTTI/2012-01-13/ldb/types\"><GetServiceDetailsResult><generatedAt>2014-10-05T22:37:25.2473081+01:00</generatedAt><serviceType>train</serviceType><locationName>Leeds</locationName><crs>LDS</crs><operator>First TransPennine Express</operator><operatorCode>TP</operatorCode><disruptionReason>This train has been delayed by a problem at a level crossing</disruptionReason><sta>22:08</sta><ata>On time</ata><std>22:14</std><etd>22:39</etd><previousCallingPoints><callingPointList serviceType=\"train\" serviceChangeRequired=\"false\"><callingPoint><locationName>Liverpool Lime Street</locationName><crs>LIV</crs><st>20:22</st><at>On time</at></callingPoint><callingPoint><locationName>Liverpool South Parkway</locationName><crs>LPY</crs><st>20:32</st><at>20:34</at></callingPoint><callingPoint><locationName>Warrington Central</locationName><crs>WAC</crs><st>20:45</st><at>20:47</at></callingPoint><callingPoint><locationName>Birchwood</locationName><crs>BWD</crs><st>20:50</st><at>No report</at></callingPoint><callingPoint><locationName>Manchester Oxford Road</locationName><crs>MCO</crs><st>21:07</st><at>On time</at></callingPoint><callingPoint><locationName>Manchester Piccadilly</locationName><crs>MAN</crs><st>21:11</st><at>On time</at></callingPoint><callingPoint><locationName>Stalybridge</locationName><crs>SYB</crs><st>21:25</st><at>On time</at></callingPoint><callingPoint><locationName>Huddersfield</locationName><crs>HUD</crs><st>21:46</st><at>On time</at></callingPoint><callingPoint><locationName>Dewsbury</locationName><crs>DEW</crs><st>21:55</st><at>On time</at></callingPoint></callingPointList></previousCallingPoints><subsequentCallingPoints><callingPointList serviceType=\"train\" serviceChangeRequired=\"false\"><callingPoint><locationName>Selby</locationName><crs>SBY</crs><st>22:44</st><et>23:08</et></callingPoint><callingPoint><locationName>Brough</locationName><crs>BUH</crs><st>23:03</st><et>23:26</et></callingPoint><callingPoint><locationName>Hull</locationName><crs>HUL</crs><st>23:20</st><et>23:39</et></callingPoint></callingPointList></subsequentCallingPoints></GetServiceDetailsResult></GetServiceDetailsResponse></soap:Body></soap:Envelope>";
		} else {
			xmlResponse = Utils.httpPost(Utils.API_URL, xml);
		}

		return new Service(serviceId, xmlResponse);
	}

	public String toString() {
		return this.previousCallingPoints.toString();
	}

}
