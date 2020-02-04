package concur_package;

class Vendor {
	Vendor(){}

    private String ID="";
	private String URI="";
	String VendorCode="";
	private String VendorName="";
	String AddressCode="";
	private String Address1="";
	private String Address2="";
	private String Address3="";
	private String City="";
	private String State="";

	private String PostalCode="";
	private String CountryCode="";
	private String Country="";
	private String Approved="";
	private String TaxID = "";
	private String CurrencyCode="";
	private String ContactEmail="";
	private String PaymentMethodType="";
	private String Custom1="";
	private String Custom2="";
	private String Custom3="";
	private String Custom4="";
	private String Custom5="";
    private String IsVisibleForContentExtraction="";
	private String AddressImportSyncID="";
	private String VendorGroup="";
	private String ContactPhoneNumber="";
	private String ContactFirstName="";


	String CONCUR_VENDOR_PIDM = "";
	String CONCUR_VENDOR_CWID = "";
	String CONCUR_VENDOR_NAME = "";
	String CONCUR_VENDOR_TAX_ID = "";
	String CONCUR_VENDOR_ADDR_CODE = "";
	String CONCUR_VENDOR_ADDR_LN1 = "";
	String CONCUR_VENDOR_ADDR_LN2 = "";
	String CONCUR_VENDOR_ADDR_LN3 = "";
	String CONCUR_VENDOR_CITY = "";
	String CONCUR_VENDOR_STATE = "";
	String CONCUR_VENDOR_ZIP = "";
	String CONCUR_VENDOR_NATN = "";
	String CONCUR_VENDOR_PHONE = "";
	String CONCUR_VENDOR_CONTACT = "";
	String CONCUR_VENDOR_CUSTOM1 = "";
	String CONCUR_VENDOR_CUSTOM2 = "";
	String CONCUR_VENDOR_CUSTOM3 = "";
	String CONCUR_VENDOR_CUSTOM4 = "";
	String CONCUR_VENDOR_CUSTOM5 = "";
	String CONCUR_VENDOR_CUSTOM6 = "";
	String CONCUR_VENDOR_EMAIL = "";
	String CONCUR_VENDOR_ACTIVE = "";
	String CONCUR_VENDOR_ACTIVITY_DATE  = "";

	static String getVendorHeader(){
		return "CONCUR_VENDOR_PIDM,CONCUR_VENDOR_CWID,CONCUR_VENDOR_NAME,CONCUR_VENDOR_TAX_ID,CONCUR_VENDOR_ADDR_CODE," +
				"CONCUR_VENDOR_ADDR_LN1,CONCUR_VENDOR_ADDR_LN2,CONCUR_VENDOR_ADDR_LN3,CONCUR_VENDOR_CITY,CONCUR_VENDOR_STATE," +
				"CONCUR_VENDOR_ZIP,CONCUR_VENDOR_NATN,CONCUR_VENDOR_PHONE,CONCUR_VENDOR_CONTACT,CONCUR_VENDOR_CUSTOM1," +
				"CONCUR_VENDOR_CUSTOM2,CONCUR_VENDOR_CUSTOM3,CONCUR_VENDOR_CUSTOM4,CONCUR_VENDOR_CUSTOM5,CONCUR_VENDOR_CUSTOM6," +
				"CONCUR_VENDOR_EMAIL,CONCUR_VENDOR_ACTIVE,CONCUR_VENDOR_ACTIVITY_DATE";
	}

	public String toString() {
		return "Vendor [\n\tID=" + ID + "\n\tURI=" + URI + "\n\tVendorCode=" + VendorCode + "\n\tVendorName=" + VendorName
				+ "\n\tAddressCode=" + AddressCode + "\n\tAddress1=" + Address1 + "\n\tAddress2=" + Address2 + "\n\tAddress3="
				+ Address3 + "\n\tCity=" + City + "\n\tState=" + State + "\n\tPostalCode=" + PostalCode + "\n\tCountryCode="
				+ CountryCode + "\n\tCountry=" + Country + "\n\tApproved=" + Approved + "\n\tTaxID=" + TaxID
				+ "\n\tCurrencyCode=" + CurrencyCode + "\n\tContactEmail=" + ContactEmail + "\n\tPaymentMethodType="
				+ PaymentMethodType + "\n\tCustom1=" + Custom1 + "\n\tCustom2=" + Custom2 + "\n\tCustom3=" + Custom3
				+ "\n\tContactPhoneNumber=" + ContactPhoneNumber
				+ "\n\tContactFirstName=" + ContactFirstName
				+ "\n\tCustom4=" + Custom4 + "\n\tCustom5=" + Custom5 + "\n\tIsVisibleForContentExtraction="
				+ IsVisibleForContentExtraction + "\n\tAddressImportSyncID=" + AddressImportSyncID + "\n\tVendorGroup="
				+ VendorGroup + "\n\t]";
	}
	public static String getHeader() {
		return "Vendor_ID,"+"URI,"+"VendorCode,"+"VendorName,"
				+"AddressCode,"+"Address1,"+"Address2,"+"Address3,"
				+"City,"+"State,"+"PostalCode,"+"CountryCode,"
				+"Country,"+"Approved,"+"TaxID,"
				+"CurrencyCode,"+"ContactEmail,"+"PaymentMethodType,"
				+"Custom1,"+"Custom2,"+"Custom3,"
				+"ContactPhoneNumber,"+"ContactFirstName,"+"Custom4,"+"Custom5,"+"IsVisibleForContentExtraction,"
				+"AddressImportSyncID,"+"VendorGroup";
	}

	public String toStringCSV_API() {
		return ID + "," + URI + "," + VendorCode + "," + VendorName
				+ "," + AddressCode + "," + Address1 + "," + Address2 + ","
				+ Address3 + "," + City + "," + State + "," + PostalCode + ","
				+ CountryCode + "," + Country + "," + Approved + "," + TaxID
				+ "," + CurrencyCode + "," + ContactEmail + ","
				+ PaymentMethodType + "," + Custom1 + "," + Custom2 + "," + Custom3
				+ "," + ContactPhoneNumber
				+ "," + ContactFirstName
				+ "," + Custom4 + "," + Custom5 + ","
				+ IsVisibleForContentExtraction + "," + AddressImportSyncID + ","
				+ VendorGroup;
	}
	public String toStringCSV_TrackingTable() {
		return CONCUR_VENDOR_PIDM +","+
				CONCUR_VENDOR_CWID +","+
				CONCUR_VENDOR_NAME +","+
				CONCUR_VENDOR_TAX_ID +","+
				CONCUR_VENDOR_ADDR_CODE +","+
				CONCUR_VENDOR_ADDR_LN1 +","+
				CONCUR_VENDOR_ADDR_LN2 +","+
				CONCUR_VENDOR_ADDR_LN3 +","+
				CONCUR_VENDOR_CITY +","+
				CONCUR_VENDOR_STATE +","+
				CONCUR_VENDOR_ZIP +","+
				CONCUR_VENDOR_NATN +","+
				CONCUR_VENDOR_PHONE +","+
				CONCUR_VENDOR_CONTACT +","+
				CONCUR_VENDOR_CUSTOM1 +","+
				CONCUR_VENDOR_CUSTOM2 +","+
				CONCUR_VENDOR_CUSTOM3 +","+
				CONCUR_VENDOR_CUSTOM4 +","+
				CONCUR_VENDOR_CUSTOM5 +","+
				CONCUR_VENDOR_CUSTOM6 +","+
				CONCUR_VENDOR_EMAIL +","+
				CONCUR_VENDOR_ACTIVE +","+
				CONCUR_VENDOR_ACTIVITY_DATE ;
	}


    public void setContactPhoneNumber(String contactPhoneNumber) {
		ContactPhoneNumber = contactPhoneNumber;
	}
	public void setContactFirstName(String contactFirstName) {
		ContactFirstName = contactFirstName;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public void setURI(String uRI) {
		URI = uRI;
	}
	public void setVendorCode(String vendorCode) {
		VendorCode = vendorCode;
	}
	public void setVendorName(String vendorName) {
		VendorName = vendorName;
	}
	public void setAddressCode(String addressCode) {
		AddressCode = addressCode;
	}
	public void setAddress1(String address1) {
		Address1 = address1;
	}
	public void setAddress2(String address2) {
		Address2 = address2;
	}
	public void setAddress3(String address3) {
		Address3 = address3;
	}
	public void setCity(String city) {
		City = city;
	}
	public void setState(String state) {
		State = state;
	}
	public void setPostalCode(String postalCode) {
		PostalCode = postalCode;
	}
	public void setCountryCode(String countryCode) {
		CountryCode = countryCode;
	}
	public void setCountry(String country) {
		Country = country;
	}
	public void setApproved(String approved) {
		Approved = approved;
	}
	public void setTaxID(String taxID) {
		TaxID = taxID;
	}
	public void setCurrencyCode(String currencyCode) {
		CurrencyCode = currencyCode;
	}
	public void setContactEmail(String contactEmail) {
		ContactEmail = contactEmail;
	}
	public void setPaymentMethodType(String paymentMethodType) {
		PaymentMethodType = paymentMethodType;
	}
	public void setCustom1(String custom1) {
		Custom1 = custom1;
	}
	public void setCustom2(String custom2) {
		Custom2 = custom2;
	}
	public void setCustom3(String custom3) {
		Custom3 = custom3;
	}
	public void setCustom4(String custom4) {
		Custom4 = custom4;
	}
	public void setCustom5(String custom5) {
		Custom5 = custom5;
	}
	public void setCustom6(String custom6) {
    }
	public void setIsVisibleForContentExtraction(String isVisibleForContentExtraction) {
		IsVisibleForContentExtraction = isVisibleForContentExtraction;
	}
	public void setAddressImportSyncID(String addressImportSyncID) {
		AddressImportSyncID = addressImportSyncID;
	}
	public void setVendorGroup(String vendorGroup) {
		VendorGroup = vendorGroup;
	}

}
