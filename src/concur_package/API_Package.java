package concur_package;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.swing.JOptionPane;

import org.json.JSONException;
import org.json.JSONObject;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.w3c.dom.Node;

class API_Package {

	private static String access_token;
	private static String tokenParameters;
	private static List<Vendor> vendors = new ArrayList<Vendor>();
	private static List<User> people = new ArrayList<User>();
	private String url;
	Function_Library functions;
	String serverResponse;
	private int vendorCount = 0;
	private int userCount = 0;

	API_Package(Function_Library conn){
		functions = conn;
		if(functions.environment.equals("PROD"))
			url="https://www.concursolutions.com";
		else
			url="https://implementation.concursolutions.com";

		setCredentials();
		setAccessToken();
	}

	void reinit() {
		System.out.println("API has been reinitialized..");
		vendors = new ArrayList<Vendor>();
		people = new ArrayList<User>();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private boolean vendorInBanner(ArrayList<Vendor> banner, Vendor vendor){
		for(int i=0;i<banner.size();++i)
			if(banner.get(i).CONCUR_VENDOR_CWID.equals(vendor.VendorCode))
				if(banner.get(i).CONCUR_VENDOR_ADDR_CODE.equals(vendor.AddressCode))
					return true;
		return false;
	}
	String compareVendorsNotInBanner(ArrayList<Vendor> bannerVendors) {
		List<String> differences = new ArrayList<String>();
		System.out.println("Initiating comparison..");
		for(int i=0;i<vendors.size();++i)
			if(!vendorInBanner(bannerVendors,vendors.get(i)))
				differences.add(vendors.get(i).toStringCSV_API());
		System.out.println(differences.size() + " vendors not active in Banner that are in Concur.");
		return functions.writeListToFile("vendors_remove_from_concur.csv", Vendor.getHeader(), differences);
	}
	///////////////////////////////////////////////////////////////////////////
	private boolean vendorInConcur(Vendor v){
		for(int i=0;i<vendors.size();++i)
			if(vendors.get(i).VendorCode.equals(v.CONCUR_VENDOR_CWID))
				if(vendors.get(i).AddressCode.equals(v.CONCUR_VENDOR_ADDR_CODE))
					return true;
		return false;
	}
	String compareVendorsNotInConcur(ArrayList<Vendor> bannerVendors) {
		List<String> differences = new ArrayList<String>();
		System.out.println("Initiating comparison..");
		for(int i=0;i<bannerVendors.size();++i)
			if(!vendorInConcur(bannerVendors.get(i)))
				differences.add(bannerVendors.get(i).toStringCSV_API());
		System.out.println(differences.size() + " vendors active in Banner that are not in Concur.");
		return functions.writeListToFile("vendors_add_to_concur.csv", Vendor.getVendorHeader(), differences);
	}
	///////////////////////////////////////////////////////////////////////////
	String writeApiVendors() {
		return functions.writeListToFile("vendors_in_api.csv", Vendor.getHeader(), vendorList2RegList(vendors));
	}
	String writeTrackingTableVendors(ArrayList<Vendor> ttVendors) {
		return functions.writeListToFile("vendors_in_tracking_table.csv", Vendor.getVendorHeader(), vendorList2RegList(ttVendors));
	}
	private ArrayList<String> vendorList2RegList(ArrayList<Vendor> p) { //tt
		ArrayList<String> newList = new ArrayList<String>();
		for(int i=0;i<p.size();++i) {
			newList.add(p.get(i).toStringCSV_TrackingTable());
		}
		return newList;
	}
	private ArrayList<String> vendorList2RegList(List<Vendor> p) { //API
		ArrayList<String> newList = new ArrayList<String>();
		for(int i=0;i<p.size();++i) {
			newList.add(p.get(i).toStringCSV_API());
		}
		return newList;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private boolean userInBanner(ArrayList<User> banner, User person){
		for(int i=0;i<banner.size();++i)
			if(banner.get(i).CONCUR_CWID.equals(person.EmployeeID)) {
				return true;
			}
		return false;
	}
	String compareUsersNotInBanner(ArrayList<User> bannerUsers) {
		List<String> differences = new ArrayList<String>();
		System.out.println("Initiating comparison..");
		for(int i=0;i<people.size();++i) {
			if(!userInBanner(bannerUsers,people.get(i))) {
				differences.add(people.get(i).toStringCSV_API());
			}
		}
		System.out.println(differences.size() + " users not active in Banner that are in Concur.");
		return functions.writeListToFile("users_remove_from_concur.csv", User.getHeader(), differences);
	}
	/////////////////////////////////////////////////////////////////////
	private boolean userInConcur(User p){
		for(int i=0;i<people.size();++i)
			if(people.get(i).EmployeeID.equals(p.CONCUR_CWID))
				return true;
		return false;
	}
	String compareUsersNotInConcur(ArrayList<User> bannerUsers) {
		List<String> differences = new ArrayList<String>();
		System.out.println("Initiating comparison..");
		for(int i=0;i<bannerUsers.size();++i) {
			if(!userInConcur(bannerUsers.get(i))) {
				differences.add(bannerUsers.get(i).toStringCSV_Table());
			}
		}
		System.out.println(differences.size() + " users active in Banner that are not in Concur.");
		return functions.writeListToFile("users_add_to_concur.csv",User.getEmpHeader(), differences);
	}

	String writeApiUsers() {
		return functions.writeListToFile("users_in_api.csv", User.getHeader(), userList2RegList(people));
	}
	String writeTrackingTableUsers(ArrayList<User> ttPeople) {
		return functions.writeListToFile("users_in_tracking_table.csv", User.getEmpHeader(), userList2RegList(ttPeople));
	}
	private ArrayList<String> userList2RegList(ArrayList<User> p) { //tt
		ArrayList<String> newList = new ArrayList<String>();
		for(int i=0;i<p.size();++i) {
			newList.add(p.get(i).toStringCSV_Table());
		}
		return newList;
	}
	private ArrayList<String> userList2RegList(List<User> p) { //API
		ArrayList<String> newList = new ArrayList<String>();
		for(int i=0;i<p.size();++i) {
			newList.add(p.get(i).toStringCSV_API());
		}
		return newList;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	void openLink(String link) {
		try {
			Desktop.getDesktop().browse(new URI(link));
		} catch (IOException | URISyntaxException   e) {
			e.printStackTrace();
		}
	}
	String usersToString(){
		StringBuilder retStr = new StringBuilder();
		for(int i=0;i<people.size();++i) {
			retStr.append(people.get(i).toString()+"\n");
		}
		return retStr.toString();
	}
	String vendorsToString() {
		StringBuilder retStr = new StringBuilder();
		for(int i=0;i<vendors.size();++i) {
			retStr.append(vendors.get(i).toString()+"\n");
		}
		return retStr.toString();
	}
	private void setCredentials() {
		if(functions.environment.equals("PROD"))
			tokenParameters =
					functions.getContents("S:\\EDAS\\Aux Serv\\Banner Finance\\Concur Project\\" +
							"CONCUR PROD\\Concur API\\documentation\\prod.dat");
		else
			tokenParameters =
					functions.getContents("S:\\EDAS\\Aux Serv\\Banner Finance\\Concur Project\\" +
							"CONCUR PROD\\Concur API\\documentation\\test.dat");
	}
	private String jsonRead(String jsonString){
		try {
			JSONObject jsonObject = new JSONObject(jsonString);
			return jsonObject.getString("access_token");
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	void sendGetRequest(String urlSuffix){
		URL obj;
		try {
			obj = new URL(url+urlSuffix);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestProperty("Authorization", "Bearer "+access_token);
			// optional default is GET
			con.setRequestMethod("GET");
			//add request header
			con.setRequestProperty("User-Agent", "Mozilla/5.0");
			int responseCode = con.getResponseCode();

			if(responseCode==200) {
				BufferedReader in = new BufferedReader(
						new InputStreamReader(con.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				//print result
				if(urlSuffix.contains("invoice/vendors"))
					xmlParse(response.toString(),"vendor");
				else
					xmlParse(response.toString(),"user");
			}else if(responseCode==500) {
				System.out.println("RESPONSE CODE 500 - TRYING AGAIN..");
				sendGetRequest(urlSuffix);
			}
			else{
				System.out.println("Response Code - "+responseCode);
				JOptionPane.showMessageDialog(null,"Response Code - "+responseCode);
				System.out.println("Exiting program..");
				System.exit(1);
			}
		} catch (MalformedURLException | ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private String clearPrefix(String site) {
		if(site.startsWith("https://implementation.concursolutions.com"))
			return site.replaceAll("https://implementation.concursolutions.com","");
		else
			return site.replace("https://www.concursolutions.com", "");
	}
	void sendDeleteRequest(String urlSuffix){
		if(functions.environment.equals("PROD")) {
			JOptionPane.showMessageDialog(null,"OVERRIDING TO TEST.");
		}
		String url ="https://implementation.concursolutions.com";
		System.out.println("URL: "+url);
		System.out.println("urlSuffix: "+urlSuffix);

		URL obj;
		try {

			obj = new URL(url+urlSuffix);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestProperty("Authorization", "Bearer "+access_token);
			con.setRequestMethod("DELETE");
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded" );
			con.setRequestProperty("User-Agent", "Mozilla/5.0");
			int responseCode = con.getResponseCode();


			if(responseCode==200) {
				BufferedReader in = new BufferedReader(
						new InputStreamReader(con.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				//print result
				if(urlSuffix.contains("/api/v3.0/invoice/vendors"))
					xmlParse(response.toString(),"vendor");
				else
					xmlParse(response.toString(),"user");
			}
			else if (responseCode==400){
				serverResponse="400 - Bad request. Record likely does not exist, or has already been processed.";
			}
			else {
				responseCode(responseCode);
				System.out.println("Exiting program..");
				System.exit(0);
			}
		} catch (MalformedURLException | ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void responseCode(int code) {
		if(code==200)
			System.out.println("200 - GOOD!!");
		else if(code==503)
			System.out.println("503 - Service Unavailable");
		else if(code==400)
			System.out.println("400 - Bad Request");
	}
	private void setAccessToken() {
		try {
			String url ;
			if(functions.environment.equals("PROD"))
				url= "https://us.api.concursolutions.com/oauth2/v0/token";
			else
				url = "https://us-impl.api.concursolutions.com/oauth2/v0/token";
			URL obj = new URL(url);
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

			//add request header
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());


			wr.writeBytes(tokenParameters);
			wr.flush();
			wr.close();

			int responseCode = con.getResponseCode();
			System.out.println("Sending 'POST' request to URL: " + url);
			System.out.println("Post parameters: " + tokenParameters);
			responseCode(responseCode);
			if(responseCode==200) {
				BufferedReader in = new BufferedReader(
						new InputStreamReader(con.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				access_token = jsonRead(response.toString());
			}
			else {
				System.out.println("Error getting access token (code "+responseCode+"). Exiting program..");
				System.exit(1);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	private Document loadXMLFromString(String xml){
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder;
			builder = factory.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(xml));
			return builder.parse(is);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		return null;

	}
	private void xmlParse(String xml, String module) {
		try {
			Document doc =  loadXMLFromString(xml);
			if (doc.hasChildNodes()) {
				if(module.equals("vendor"))
					printNodeVendors(doc.getChildNodes());
				else {
					printNodeUsers(doc.getChildNodes());
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}
	private void printNodeVendors(NodeList nodeList) {
		Node headNode = nodeList.item(0);
		for (int i = 0; i < headNode.getChildNodes().getLength(); i++) {
			Node tempNode = headNode.getChildNodes().item(i);

			if(tempNode.getNodeName().equals("NextPage")) {
				++vendorCount;
				System.out.println(vendorCount+")\t"+tempNode.getTextContent());
				if(!tempNode.getTextContent().equals(""))
					sendGetRequest(clearPrefix(tempNode.getTextContent()));
			}
			else if(tempNode.getNodeName().equals("TotalCount")) {
			}
			else if(tempNode.getNodeName().equals("RequestRunSummary")) {
				serverResponse=tempNode.getTextContent();
			}
			else if(tempNode.getNodeName().equals("Vendor")) {
				Vendor vendor = new Vendor();
				for(int j=0;j<tempNode.getChildNodes().getLength();j++) {
					Node attribute = tempNode.getChildNodes().item(j);

					if(attribute.getNodeName().equals("ID"))
						vendor.setID(attribute.getTextContent());
					else if(attribute.getNodeName().equals("URI"))
						vendor.setURI(attribute.getTextContent());
					else if(attribute.getNodeName().equals("VendorCode"))
						vendor.setVendorCode(attribute.getTextContent());
					else if(attribute.getNodeName().equals("VendorName"))
						vendor.setVendorName(attribute.getTextContent());
					else if(attribute.getNodeName().equals("AddressCode"))
						vendor.setAddressCode(attribute.getTextContent());
					else if(attribute.getNodeName().equals("Address1"))
						vendor.setAddress1(attribute.getTextContent());
					else if(attribute.getNodeName().equals("Address2"))
						vendor.setAddress2(attribute.getTextContent());
					else if(attribute.getNodeName().equals("Address3"))
						vendor.setAddress3(attribute.getTextContent());
					else if(attribute.getNodeName().equals("City"))
						vendor.setCity(attribute.getTextContent());
					else if(attribute.getNodeName().equals("State"))
						vendor.setState(attribute.getTextContent());
					else if(attribute.getNodeName().equals("PostalCode"))
						vendor.setPostalCode(attribute.getTextContent());
					else if(attribute.getNodeName().equals("CountryCode"))
						vendor.setCountryCode(attribute.getTextContent());
					else if(attribute.getNodeName().equals("Country"))
						vendor.setCountry(attribute.getTextContent());
					else if(attribute.getNodeName().equals("Approved"))
						vendor.setApproved(attribute.getTextContent());
					else if(attribute.getNodeName().equals("TaxID"))
						vendor.setTaxID(attribute.getTextContent());
					else if(attribute.getNodeName().equals("CurrencyCode"))
						vendor.setCurrencyCode(attribute.getTextContent());
					else if(attribute.getNodeName().equals("ContactEmail"))
						vendor.setContactEmail(attribute.getTextContent());
					else if(attribute.getNodeName().equals("PaymentMethodType"))
						vendor.setPaymentMethodType(attribute.getTextContent());
					else if(attribute.getNodeName().equals("ContactPhoneNumber"))
						vendor.setContactPhoneNumber(attribute.getTextContent());
					else if(attribute.getNodeName().equals("ContactFirstName"))
						vendor.setContactFirstName(attribute.getTextContent());
					else if(attribute.getNodeName().equals("Custom1"))
						vendor.setCustom1(attribute.getTextContent());
					else if(attribute.getNodeName().equals("Custom2"))
						vendor.setCustom2(attribute.getTextContent());
					else if(attribute.getNodeName().equals("Custom3"))
						vendor.setCustom3(attribute.getTextContent());
					else if(attribute.getNodeName().equals("Custom4"))
						vendor.setCustom4(attribute.getTextContent());
					else if(attribute.getNodeName().equals("Custom5"))
						vendor.setCustom5(attribute.getTextContent());
					else if(attribute.getNodeName().equals("Custom6"))
						vendor.setCustom6(attribute.getTextContent());
					else if(attribute.getNodeName().equals("IsVisibleForContentExtraction"))
						vendor.setIsVisibleForContentExtraction(attribute.getTextContent());
					else if(attribute.getNodeName().equals("AddressImportSyncID"))
						vendor.setAddressImportSyncID(attribute.getTextContent());
					else if(attribute.getNodeName().equals("VendorGroup"))
						vendor.setVendorGroup(attribute.getTextContent());
					else
						System.out.println("Unknown type - "+attribute.getNodeName());
				}
				vendors.add(vendor);
			}
		}
	}
	private void printNodeUsers(NodeList nodeList) {
		Node headNode = nodeList.item(0);
		for (int i = 0; i < headNode.getChildNodes().getLength(); ++i) {
			Node tempNode = headNode.getChildNodes().item(i);
			if(tempNode.getNodeName().equals("NextPage")) {
				++userCount;
				System.out.println(userCount+")\t"+tempNode.getTextContent());
				if (!tempNode.getTextContent().equals(""))
					sendGetRequest(clearPrefix(tempNode.getTextContent()));

			}
			if(tempNode.getNodeName().equals("Items"))
				for(int j=0;j<tempNode.getChildNodes().getLength();++j) {
					Node userNode = tempNode.getChildNodes().item(j);
					User person = new User();
					for(int k=0;k<userNode.getChildNodes().getLength();++k) {
						Node attributeNode = userNode.getChildNodes().item(k);
						if(attributeNode.getNodeName().equals("ID"))
							person.setID(attributeNode.getTextContent());
						else if(attributeNode.getNodeName().equals("LoginID"))
							person.setLoginID(attributeNode.getTextContent());
						else if(attributeNode.getNodeName().equals("EmployeeID"))
							person.setEmployeeID(attributeNode.getTextContent());
						else if(attributeNode.getNodeName().equals("FirstName"))
							person.setFirstName(attributeNode.getTextContent());
						else if(attributeNode.getNodeName().equals("LastName"))
							person.setLastName(attributeNode.getTextContent());
						else if(attributeNode.getNodeName().equals("MiddleName"))
							person.setMiddleName(attributeNode.getTextContent());
						else if(attributeNode.getNodeName().equals("PrimaryEmail"))
							person.setPrimaryEmail(attributeNode.getTextContent());
						else if(attributeNode.getNodeName().equals("Active"))
							person.setActive(attributeNode.getTextContent());
						else if(attributeNode.getNodeName().equals("CellPhoneNumber"))
							person.setCellPhoneNumber(attributeNode.getTextContent());
						else if(attributeNode.getNodeName().equals("OrganizationUnit"))
							person.setOrganizationUnit(attributeNode.getTextContent());
					}
					people.add(person);
				}
		}
	}
}
