package concur_package;

public class User {
	String ID="";
	String URI="";
	String LoginID="";
	String EmployeeID="";
	String FirstName="";
	String LastName="";
	String MiddleName="";
	String PrimaryEmail="";
	String Active="";
	String CellPhoneNumber="";
	String OrganizationUnit="";
	@Override
	public String toString() {
		return "User [\n\tID=" + ID + "\n\tURI=" + URI + "\n\tLoginID=" + LoginID + "\n\tEmployeeID=" + EmployeeID
				+ "\n\tFirstName=" + FirstName + "\n\tLastName=" + LastName + "\n\tMiddleName=" + MiddleName
				+ "\n\tPrimaryEmail=" + PrimaryEmail + "\n\tActive=" + Active + "\n\tCellPhoneNumber=" + CellPhoneNumber
				+ "\n\tOrganizationUnit=" + OrganizationUnit + "\n\t]";
	}
	public void printString() {
		System.out.println(toString());
	}

	public String getID() {
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
	public String getLoginID() {
		return LoginID;
	}
	public void setLoginID(String loginID) {
		LoginID = loginID;
	}
	public String getEmployeeID() {
		return EmployeeID;
	}
	public void setEmployeeID(String employeeID) {
		EmployeeID = employeeID;
	}
	public String getFirstName() {
		return FirstName;
	}
	public void setFirstName(String firstName) {
		FirstName = firstName;
	}
	public String getLastName() {
		return LastName;
	}
	public void setLastName(String lastName) {
		LastName = lastName;
	}
	public String getMiddleName() {
		return MiddleName;
	}
	public void setMiddleName(String middleName) {
		MiddleName = middleName;
	}
	public String getPrimaryEmail() {
		return PrimaryEmail;
	}
	public void setPrimaryEmail(String primaryEmail) {
		PrimaryEmail = primaryEmail;
	}
	public String getActive() {
		return Active;
	}
	public void setActive(String active) {
		Active = active;
	}
	public String getCellPhoneNumber() {
		return CellPhoneNumber;
	}
	public void setCellPhoneNumber(String cellPhoneNumber) {
		CellPhoneNumber = cellPhoneNumber;
	}
	public String getOrganizationUnit() {
		return OrganizationUnit;
	}
	public void setOrganizationUnit(String organizationUnit) {
		OrganizationUnit = organizationUnit;
	}

}
