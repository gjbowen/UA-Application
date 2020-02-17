package concur_package;

class User {
	private String ID="";//api ref
	private final String URI="";
	private String LoginID="";//same as email
	String EmployeeID="";//cwid
	private String FirstName="";
	private String LastName="";
	private String MiddleName="";
	private String PrimaryEmail="";
	private String Active="";
	private String CellPhoneNumber="";
	private String OrganizationUnit="";

	public void setID(String ID) {
		this.ID = ID;
	}

    public void setLoginID(String loginID) {
		LoginID = loginID;
	}

	public void setEmployeeID(String employeeID) {
		EmployeeID = employeeID;
	}

	public void setFirstName(String firstName) {
		FirstName = firstName;
	}

	public void setLastName(String lastName) {
		LastName = lastName;
	}

	public void setMiddleName(String middleName) {
		MiddleName = middleName;
	}

	public void setPrimaryEmail(String primaryEmail) {
		PrimaryEmail = primaryEmail;
	}

	public void setActive(String active) {
		Active = active;
	}

	public void setCellPhoneNumber(String cellPhoneNumber) {
		CellPhoneNumber = cellPhoneNumber;
	}

	public void setOrganizationUnit(String organizationUnit) {
		OrganizationUnit = organizationUnit;
	}

	String CONCUR_PIDM = "";
	String CONCUR_CWID = "";
	String CONCUR_FIRST_NAME = "";
	String CONCUR_MI = "";
	String CONCUR_LAST_NAME = "";
	String CONCUR_LOGIN_ID = "";
	String CONCUR_EMAIL = "";
	String CONCUR_ACTIVE = "";
	String CONCUR_RT_CODE = "";
	String CONCUR_EXP_REP_APPR_ID = "";
	String CONCUR_TRAVEL_APPR_ID = "";
	String CONCUR_INVOICE_APPR_ID = "";
	String CONCUR_EXPENSE_USER = "";
	String CONCUR_APPROVER = "";
	String CONCUR_INVOICE_USER = "";
	String CONCUR_INVOICE_APPR = "";
	String CONCUR_TRAVEL_REQUEST_USER = "";
	String CONCUR_TRAVEL_REQUEST_MGR = "";
	String CONCUR_TRAVEL_WIZARD_USER = "";
	String CONCUR_TRAVEL_CLASS = "";
	String CONCUR_APPR_POSN_DESC = "";
	String CONCUR_COAS_CODE = "";
	String CONCUR_FUND_CODE = "";
	String CONCUR_ORGN_CODE = "";
	String CONCUR_PROG_CODE = "";
	String CONCUR_VERSION = "";
	String CONCUR_ACTIVITY_DATE = "";

    static String getHeader() {
		return "User_ID," + "URI,"  + "LoginID,"  + "EmployeeID,"
				+ "FirstName,"  + "LastName,"  + "MiddleName,"
				+ "PrimaryEmail,"  + "Active,"  + "CellPhoneNumber,"
				+ "OrganizationUnit," ;
	}

	String toStringCSV_API(){
		return ID+"," + URI + "," + LoginID + "," + EmployeeID
				+ "," + FirstName + "," + LastName + "," + MiddleName
				+ "," + PrimaryEmail + "," + Active + "," + CellPhoneNumber
				+ "," + OrganizationUnit ;
	}
	@Override public String toString() {
		return "User [\n\tID=" + ID + "\n\tURI=" + URI + "\n\tLoginID=" + LoginID + "\n\tEmployeeID=" + EmployeeID
				+ "\n\tFirstName=" + FirstName + "\n\tLastName=" + LastName + "\n\tMiddleName=" + MiddleName
				+ "\n\tPrimaryEmail=" + PrimaryEmail + "\n\tActive=" + Active + "\n\tCellPhoneNumber=" + CellPhoneNumber
				+ "\n\tOrganizationUnit=" + OrganizationUnit + "\n\t]";
	}
	String toStringCSV_Table(){
		return CONCUR_PIDM+","+CONCUR_CWID +","+CONCUR_FIRST_NAME+","+CONCUR_MI+","+CONCUR_LAST_NAME+","+CONCUR_LOGIN_ID+","+CONCUR_EMAIL+","+CONCUR_ACTIVE +","+
				CONCUR_RT_CODE+","+CONCUR_EXP_REP_APPR_ID+","+CONCUR_TRAVEL_APPR_ID+","+CONCUR_INVOICE_APPR_ID+","+CONCUR_EXPENSE_USER+"," +
				CONCUR_APPROVER+","+CONCUR_INVOICE_USER+","+CONCUR_INVOICE_APPR+","+CONCUR_TRAVEL_REQUEST_USER+","+CONCUR_TRAVEL_REQUEST_MGR+"," +
				CONCUR_TRAVEL_WIZARD_USER+","+CONCUR_TRAVEL_CLASS+","+CONCUR_APPR_POSN_DESC+","+CONCUR_COAS_CODE+","+CONCUR_FUND_CODE+"," +
				CONCUR_ORGN_CODE+","+CONCUR_PROG_CODE+","+CONCUR_VERSION+","+CONCUR_ACTIVITY_DATE;
	}
	static String getEmpHeader(){
		return "CONCUR_PIDM,"+"CONCUR_CWID," +
				"CONCUR_FIRST_NAME,"+"CONCUR_MI,"+"CONCUR_LAST_NAME,"+"CONCUR_LOGIN_ID,"+"CONCUR_EMAIL,"+"CONCUR_ACTIVE," +
				"CONCUR_RT_CODE,"+"CONCUR_EXP_REP_APPR_ID,"+"CONCUR_TRAVEL_APPR_ID,"+"CONCUR_INVOICE_APPR_ID,"+"CONCUR_EXPENSE_USER," +
				"CONCUR_APPROVER,"+"CONCUR_INVOICE_USER,"+"CONCUR_INVOICE_APPR,"+"CONCUR_TRAVEL_REQUEST_USER,"+"CONCUR_TRAVEL_REQUEST_MGR," +
				"CONCUR_TRAVEL_WIZARD_USER,"+"CONCUR_TRAVEL_CLASS,"+"CONCUR_APPR_POSN_DESC,"+"CONCUR_coas_CODE,"+"CONCUR_fund_CODE," +
				"CONCUR_ORGN_CODE,"+"CONCUR_prog_CODE,"+"CONCUR_VERSION,"+"CONCUR_ACTIVITY_DATE";
	}


}
