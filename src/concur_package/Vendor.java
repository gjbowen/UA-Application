package concur_package;

public class Vendor {
	String ID="";
	String URI="";
	String VendorCode="";
	String VendorName="";
	String AddressCode="";
	String Address1="";
	String Address2="";
	String Address3="";
	String City="";
	String State="";

	String PostalCode="";
	String CountryCode="";
	String Country="";
	String Approved="";
	String TaxID = "";
	String CurrencyCode="";
	String ContactEmail="";
	String PaymentMethodType="";
	String Custom1="";
	String Custom2="";
	String Custom3="";
	String Custom4="";
	String Custom5="";
	String Custom6="";
	String IsVisibleForContentExtraction="";
	String AddressImportSyncID="";
	String VendorGroup="";
	String ContactPhoneNumber="";
	String ContactFirstName="";
	
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
	public String getCwidRt() {
		return VendorCode + "," + AddressCode;
	}
	public void printString() {
		System.out.println(toString());
	}
	
	public String getContactPhoneNumber() {
		return ContactPhoneNumber;
	}

	public void setContactPhoneNumber(String contactPhoneNumber) {
		ContactPhoneNumber = contactPhoneNumber;
	}

	public String getContactFirstName() {
		return ContactFirstName;
	}

	public void setContactFirstName(String contactFirstName) {
		ContactFirstName = contactFirstName;
	}	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getURI() {
		return URI;
	}
	public void setURI(String uRI) {
		URI = uRI;
	}
	public String getVendorCode() {
		return VendorCode;
	}
	public void setVendorCode(String vendorCode) {
		VendorCode = vendorCode;
	}
	public String getVendorName() {
		return VendorName;
	}
	public void setVendorName(String vendorName) {
		VendorName = vendorName;
	}
	public String getAddressCode() {
		return AddressCode;
	}
	public void setAddressCode(String addressCode) {
		AddressCode = addressCode;
	}
	public String getAddress1() {
		return Address1;
	}
	public void setAddress1(String address1) {
		Address1 = address1;
	}
	public String getAddress2() {
		return Address2;
	}
	public void setAddress2(String address2) {
		Address2 = address2;
	}
	public String getAddress3() {
		return Address3;
	}
	public void setAddress3(String address3) {
		Address3 = address3;
	}
	public String getCity() {
		return City;
	}
	public void setCity(String city) {
		City = city;
	}
	public String getState() {
		return State;
	}
	public void setState(String state) {
		State = state;
	}
	public String getPostalCode() {
		return PostalCode;
	}
	public void setPostalCode(String postalCode) {
		PostalCode = postalCode;
	}
	public String getCountryCode() {
		return CountryCode;
	}
	public void setCountryCode(String countryCode) {
		CountryCode = countryCode;
	}
	public String getCountry() {
		return Country;
	}
	public void setCountry(String country) {
		Country = country;
	}
	public String getApproved() {
		return Approved;
	}
	public void setApproved(String approved) {
		Approved = approved;
	}
	public String getTaxID() {
		return TaxID;
	}
	public void setTaxID(String taxID) {
		TaxID = taxID;
	}
	public String getCurrencyCode() {
		return CurrencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		CurrencyCode = currencyCode;
	}
	public String getContactEmail() {
		return ContactEmail;
	}
	public void setContactEmail(String contactEmail) {
		ContactEmail = contactEmail;
	}
	public String getPaymentMethodType() {
		return PaymentMethodType;
	}
	public void setPaymentMethodType(String paymentMethodType) {
		PaymentMethodType = paymentMethodType;
	}
	public String getCustom1() {
		return Custom1;
	}
	public void setCustom1(String custom1) {
		Custom1 = custom1;
	}
	public String getCustom2() {
		return Custom2;
	}
	public void setCustom2(String custom2) {
		Custom2 = custom2;
	}
	public String getCustom3() {
		return Custom3;
	}
	public void setCustom3(String custom3) {
		Custom3 = custom3;
	}
	public String getCustom4() {
		return Custom4;
	}
	public void setCustom4(String custom4) {
		Custom4 = custom4;
	}
	public String getCustom5() {
		return Custom5;
	}
	public void setCustom5(String custom5) {
		Custom5 = custom5;
	}
	public String getCustom6() {
		return Custom6;
	}
	public void setCustom6(String custom6) {
		Custom6=custom6;
	}
	
	public String getIsVisibleForContentExtraction() {
		return IsVisibleForContentExtraction;
	}
	public void setIsVisibleForContentExtraction(String isVisibleForContentExtraction) {
		IsVisibleForContentExtraction = isVisibleForContentExtraction;
	}
	public String getAddressImportSyncID() {
		return AddressImportSyncID;
	}
	public void setAddressImportSyncID(String addressImportSyncID) {
		AddressImportSyncID = addressImportSyncID;
	}
	public String getVendorGroup() {
		return VendorGroup;
	}
	public void setVendorGroup(String vendorGroup) {
		VendorGroup = vendorGroup;
	}

}
