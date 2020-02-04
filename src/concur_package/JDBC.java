package concur_package;
import java.sql.Connection;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


class JDBC extends public_package.JDBC_Connection{
	JDBC(Connection conn, String user, String pass, String env) {
		super(user, pass,env);
		connection=conn;
	}

	public String getNameFromTrackingTable(String cwid) {
		String name = "";
		Statement stmt;
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery("select concur_last_name||', '||concur_first_name||' '||concur_mi from ua_fimsmgr.concur_emp_feed where concur_cwid = '" + cwid + "' ");
			int count = 0;
			while (rs.next()) {
				++count;
				name = rs.getString(1);
			}
			if(count==1)
				return name;
		}
		catch (SQLException e2) {
			System.out.println("Too many people were returned - " + cwid);
			e2.printStackTrace();
		}
		System.out.println("Error getting CWID.");
		return null;
	}

	public String getInformationTrackingTable(String cwid){
		cwid=cwid.trim();
		//STEP 4: Execute a query
		System.out.println("Creating statement...");

		Statement stmt;
		ResultSetMetaData rsmd;
		String message="",result,row;
		System.out.println("Executing query...");
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery("select CONCUR_PIDM as \"PIDM\","
					+ "CONCUR_CWID as \"CWID\","
					+ "CONCUR_FIRST_NAME as \"First Name\","
					+ "CONCUR_MI as \"Middle Name\","
					+ "CONCUR_LAST_NAME as \"Last Name\","
					+ "CONCUR_LOGIN_ID as \"Login ID\","
					+ "CONCUR_EMAIL as \"Email\","
					+ "CONCUR_ACTIVE as \"Active\","
					+ "CONCUR_RT_CODE as \"RT-CODE\","
					+ "CONCUR_EXP_REP_APPR_ID as \"EXP Rep Appr ID\","
					+ "CONCUR_TRAVEL_APPR_ID as \"Travel Appr ID\","
					+ "CONCUR_INVOICE_APPR_ID as \"Invoice Appr ID\","
					+ "CONCUR_EXPENSE_USER as \"Expense User\","
					+ "CONCUR_APPROVER as \"Approver\","
					+ "CONCUR_INVOICE_USER as \"Invoice User\","
					+ "CONCUR_INVOICE_APPR as \"Invoice Appr\","
					+ "CONCUR_TRAVEL_REQUEST_USER as \"Travel Request User\","
					+ "CONCUR_TRAVEL_REQUEST_MGR as \"Travel Request Mgr\","
					+ "CONCUR_TRAVEL_WIZARD_USER as \"Travel Wiz User\","
					+ "CONCUR_TRAVEL_CLASS as \"Travel Class\","
					+ "CONCUR_APPR_POSN_DESC as \"Appr Posn Desc\","
					+ "CONCUR_coas_CODE as \"Chart Code\","
					+ "CONCUR_fund_CODE as \"Fund Code\","
					+ "CONCUR_ORGN_CODE as \"Orgn Code\","
					+ "CONCUR_prog_CODE as \"Prog Code\","
					+ "CONCUR_VERSION as \"Version\","
					+ "CONCUR_ACTIVITY_DATE as \"Activity Date\""
					+ "from ua_fimsmgr.concur_emp_feed "
					+ "where concur_cwid = '"+cwid+"'");
			rsmd = rs.getMetaData();
			System.out.println("Extracting data...");
			//STEP 5: Extract data from result set
			while(rs.next()){
				//Retrieve by column name
				row="";
				for(int i =1;i < rsmd.getColumnCount()+1;++i){
					result=rs.getString(i);
					if(result==null)
						result="";
					if(row.equals(""))
						row = rsmd.getColumnName(i)+": "+result;
					else
						row = row+"\n"+rsmd.getColumnName(i)+": "+result;

				}
				message=message+row+"\n";
			}
			if(stmt != null) {stmt.close();	}
		} catch (SQLException e) {
			System.out.println("Error Executing Query");
			e.printStackTrace();
		}

		System.out.println("Finished executing query.");
		return message;

	}
	public ArrayList<Vendor> getActiveVendors() {
		ArrayList<Vendor> all = new ArrayList<Vendor>();
		Statement stmt;
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery(
					"select CONCUR_VENDOR_PIDM,CONCUR_VENDOR_CWID,CONCUR_VENDOR_NAME,CONCUR_VENDOR_TAX_ID, "+
							"CONCUR_VENDOR_ADDR_CODE,CONCUR_VENDOR_ADDR_LN1,CONCUR_VENDOR_ADDR_LN2,CONCUR_VENDOR_ADDR_LN3, "+
							"CONCUR_VENDOR_CITY,CONCUR_VENDOR_STATE,CONCUR_VENDOR_ZIP,CONCUR_VENDOR_NATN,CONCUR_VENDOR_PHONE, "+
							"CONCUR_VENDOR_CONTACT,CONCUR_VENDOR_CUSTOM1,CONCUR_VENDOR_CUSTOM2,CONCUR_VENDOR_CUSTOM3,CONCUR_VENDOR_CUSTOM4, "+
							"CONCUR_VENDOR_CUSTOM5,CONCUR_VENDOR_CUSTOM6,CONCUR_VENDOR_EMAIL, "+
							"CONCUR_VENDOR_ACTIVE,CONCUR_VENDOR_ACTIVITY_DATE "+
							"from ua_fimsmgr.concur_vendor_feed " +
							"where concur_vendor_active = 'ACTIVE' " +
							"order by concur_vendor_cwid");

			while (rs.next()) {
				Vendor vendor = new Vendor();

				vendor.CONCUR_VENDOR_PIDM = rs.getString(1);
				vendor.CONCUR_VENDOR_CWID = rs.getString(2);
				vendor.CONCUR_VENDOR_NAME = rs.getString(3);
				vendor.CONCUR_VENDOR_TAX_ID = rs.getString(4);
				vendor.CONCUR_VENDOR_ADDR_CODE = rs.getString(5);
				vendor.CONCUR_VENDOR_ADDR_LN1 = rs.getString(6);
				vendor.CONCUR_VENDOR_ADDR_LN2 = rs.getString(7);
				vendor.CONCUR_VENDOR_ADDR_LN3 = rs.getString(8);
				vendor.CONCUR_VENDOR_CITY = rs.getString(9);
				vendor.CONCUR_VENDOR_STATE = rs.getString(10);
				vendor.CONCUR_VENDOR_ZIP = rs.getString(11);
				vendor.CONCUR_VENDOR_NATN = rs.getString(12);
				vendor.CONCUR_VENDOR_PHONE = rs.getString(13);
				vendor.CONCUR_VENDOR_CONTACT = rs.getString(14);
				vendor.CONCUR_VENDOR_CUSTOM1 = rs.getString(15);
				vendor.CONCUR_VENDOR_CUSTOM2 = rs.getString(16);
				vendor.CONCUR_VENDOR_CUSTOM3 = rs.getString(17);
				vendor.CONCUR_VENDOR_CUSTOM4 = rs.getString(18);
				vendor.CONCUR_VENDOR_CUSTOM5 = rs.getString(19);
				vendor.CONCUR_VENDOR_CUSTOM6 = rs.getString(20);
				vendor.CONCUR_VENDOR_EMAIL = rs.getString(21);
				vendor.CONCUR_VENDOR_ACTIVE = rs.getString(22);
				vendor.CONCUR_VENDOR_ACTIVITY_DATE = rs.getString(23);

				all.add(vendor);
			}
			System.out.println("ACTIVE VENDORS IN TRACKING: " + all.size());
		}
		catch (SQLException e2) {
			e2.printStackTrace();
			return null;
		}
		return all;
	}

	public boolean cwidTracked(String cwid) {
		Statement stmt;
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery("select 'Y' from ua_fimsmgr.concur_emp_feed where concur_cwid='" + cwid + "'");
			return rs.next();
		}
		catch (SQLException e2) {
			System.out.println("Error Executing Query");
			e2.printStackTrace();
			return false;
		}
	}

	public ArrayList<User> getActiveUsers() {
		ArrayList<User> all = new ArrayList<User>();
		Statement stmt;
		try {
			stmt = connection.createStatement();

			rs = stmt.executeQuery(
					"select CONCUR_PIDM,CONCUR_CWID,CONCUR_FIRST_NAME,CONCUR_MI,CONCUR_LAST_NAME,CONCUR_LOGIN_ID,CONCUR_EMAIL,CONCUR_ACTIVE," +
							"CONCUR_RT_CODE,CONCUR_EXP_REP_APPR_ID,CONCUR_TRAVEL_APPR_ID,CONCUR_INVOICE_APPR_ID,CONCUR_EXPENSE_USER," +
							"CONCUR_APPROVER,CONCUR_INVOICE_USER,CONCUR_INVOICE_APPR,CONCUR_TRAVEL_REQUEST_USER,CONCUR_TRAVEL_REQUEST_MGR," +
							"CONCUR_TRAVEL_WIZARD_USER,CONCUR_TRAVEL_CLASS,CONCUR_APPR_POSN_DESC,CONCUR_coas_CODE,CONCUR_fund_CODE," +
							"CONCUR_ORGN_CODE,CONCUR_prog_CODE,CONCUR_VERSION,CONCUR_ACTIVITY_DATE " +
							"from ua_fimsmgr.concur_emp_feed " +
							"where concur_active = 'Y'");

			while (rs.next()) {
				User person = new User();

				person.CONCUR_PIDM = rs.getString(1);
				person.CONCUR_CWID = rs.getString(2);
				person.CONCUR_FIRST_NAME = rs.getString(3);
				person.CONCUR_MI = rs.getString(4);
				person.CONCUR_LAST_NAME = rs.getString(5);
				person.CONCUR_LOGIN_ID = rs.getString(6);
				person.CONCUR_EMAIL = rs.getString(7);
				person.CONCUR_ACTIVE = rs.getString(8);
				person.CONCUR_RT_CODE = rs.getString(9);
				person.CONCUR_EXP_REP_APPR_ID = rs.getString(10);
				person.CONCUR_TRAVEL_APPR_ID = rs.getString(11);
				person.CONCUR_INVOICE_APPR_ID = rs.getString(12);
				person.CONCUR_EXPENSE_USER = rs.getString(13);
				person.CONCUR_APPROVER = rs.getString(14);
				person.CONCUR_INVOICE_USER = rs.getString(15);
				person.CONCUR_INVOICE_APPR = rs.getString(16);
				person.CONCUR_TRAVEL_REQUEST_USER = rs.getString(17);
				person.CONCUR_TRAVEL_REQUEST_MGR = rs.getString(18);
				person.CONCUR_TRAVEL_WIZARD_USER = rs.getString(19);
				person.CONCUR_TRAVEL_CLASS = rs.getString(20);
				person.CONCUR_APPR_POSN_DESC = rs.getString(21);
				person.CONCUR_COAS_CODE = rs.getString(22);
				person.CONCUR_FUND_CODE = rs.getString(23);
				person.CONCUR_ORGN_CODE = rs.getString(24);
				person.CONCUR_PROG_CODE = rs.getString(25);
				person.CONCUR_VERSION = rs.getString(26);
				person.CONCUR_ACTIVITY_DATE = rs.getString(27);

				all.add(person);
			}
			System.out.println("ACTIVE USERS SIZE: " + all.size());
		}

		catch (SQLException e2) {
			System.out.println("ERROR - "+e2.getMessage() );
			e2.printStackTrace();
			return null;
		}
		return all;
	}

	public String getVendorInfo(String cwid) {
		System.out.println("here..");
		Statement stmt;
		try {
			stmt = connection.createStatement();

			rs = stmt.executeQuery(
					"select * from ua_fimsmgr.concur_vendor_feed "
							+ "where concur_vendor_cwid = '"+cwid.trim()+"' "
							+ "and concur_vendor_active = 'ACTIVE' ");

			ResultSetMetaData rsmd = rs.getMetaData();
			//rs.next();
			ArrayList<String> contents = new ArrayList<String>();
			StringBuilder str = new StringBuilder();
			while (rs.next()) {
				for(int i=1;i<=rsmd.getColumnCount();++i){
					str.append(rsmd.getColumnName(i).replaceAll("CONCUR_VENDOR_", "")+": "+rs.getString(i)+"\n");
				}
				str.append("\n");
			}
			System.out.println(contents.toString());
			return str.toString();
		}

		catch (SQLException e2) {
			System.out.println("ERROR - "+e2.getMessage() );
			e2.printStackTrace();
		}
		return null;
	}

	public ArrayList<ArrayList<String>> getFunds() {
		ArrayList<ArrayList<String>> all = new ArrayList<ArrayList<String>>();
		ArrayList<String> x;

		Statement stmt;
		try {
			stmt = connection.createStatement();

			rs = stmt.executeQuery(
					"select " +
							"ftvfund_coas_code,ftvfund_fund_code,trunc(ftvfund_eff_date),trunc(ftvfund_nchg_date),trunc(ftvfund_term_date) " +
							"from fimsmgr.ftvfund " +
							"where ftvfund_status_ind = 'A' " +
							"and ftvfund_data_entry_ind = 'Y' ");

			while (rs.next()) {
				//	name = rs.getString(1);
				x = new ArrayList<String>();

				x.add( rs.getString(1));
				x.add( rs.getString(2));
				x.add( rs.getString(3));
				x.add( rs.getString(4));
				if (rs.getString(5)==null)
					x.add("");
				else
					x.add(rs.getString(5));
				all.add(x);
			}

			for(int i = 0; i<all.size(); ++i) {
				//System.out.println(allFunds.get(i).toString());
			}
			System.out.println("FUND SIZE: " + all.size());
		}

		catch (SQLException e2) {
			System.out.println("Too many were returned" );
			e2.printStackTrace();
			return null;
		}
		return all;
	}
	public ArrayList<ArrayList<String>> getOrgns() {
		ArrayList<ArrayList<String>> all = new ArrayList<ArrayList<String>>();
		ArrayList<String> x;

		Statement stmt;
		try {
			stmt = connection.createStatement();

			rs = stmt.executeQuery(
					"select ftvorgn_coas_code,ftvorgn_orgn_code,trunc(ftvorgn_eff_date),trunc(ftvorgn_nchg_date),trunc(ftvorgn_term_date) " +
							"from fimsmgr.ftvorgn " +
							"where ftvorgn_status_ind = 'A' " +
							"and ftvorgn_data_entry_ind = 'Y' ");

			while (rs.next()) {
				//	name = rs.getString(1);
				x = new ArrayList<String>();

				x.add( rs.getString(1));
				x.add( rs.getString(2));
				x.add( rs.getString(3));
				x.add( rs.getString(4));
				if (rs.getString(5)==null)
					x.add("");
				else
					x.add(rs.getString(5));
				all.add(x);
			}

			for(int i = 0; i<all.size(); ++i) {
				//System.out.println(all.get(i).toString());
			}
			System.out.println("ORGNs SIZE: " + all.size());
		}

		catch (SQLException e2) {
			System.out.println("Too many were returned" );
			e2.printStackTrace();
			return null;
		}
		return all;
	}
	public ArrayList<ArrayList<String>> getProgs() {
		ArrayList<ArrayList<String>> all = new ArrayList<ArrayList<String>>();
		ArrayList<String> x;

		Statement stmt;
		try {
			stmt = connection.createStatement();

			rs = stmt.executeQuery(
					"select ftvprog_coas_code,ftvprog_prog_code,trunc(ftvprog_eff_date),trunc(ftvprog_nchg_date),trunc(ftvprog_term_date) " +
							"from fimsmgr.ftvprog " +
							"where ftvprog_status_ind = 'A' " +
							"and ftvprog_data_entry_ind = 'Y'");

			while (rs.next()) {
				x = new ArrayList<String>();
				x.add( rs.getString(1));
				x.add( rs.getString(2));
				x.add( rs.getString(3));
				x.add( rs.getString(4));
				if (rs.getString(5)==null)
					x.add("");
				else
					x.add(rs.getString(5));


				all.add(x);
			}

			for(int i = 0; i<all.size(); ++i) {
				//System.out.println(allFunds.get(i).toString());
			}
			System.out.println("PROGs SIZE: " + all.size());
		}

		catch (SQLException e2) {
			e2.printStackTrace();
			return null;
		}
		return all;
	}
	public ArrayList<ArrayList<String>> getActivity() {
		ArrayList<ArrayList<String>> all = new ArrayList<ArrayList<String>>();
		ArrayList<String> x;

		Statement stmt;
		try {
			stmt = connection.createStatement();

			rs = stmt.executeQuery(
					"select ftvactv_coas_code,ftvactv_actv_code,trunc(ftvactv_eff_date),trunc(ftvactv_nchg_date),trunc(ftvactv_term_date) " +
							"from fimsmgr.ftvactv " +
							"where ftvactv_status_ind = 'A' ");

			while (rs.next()) {
				x = new ArrayList<String>();
				x.add( rs.getString(1));
				x.add( rs.getString(2));
				x.add( rs.getString(3));
				x.add( rs.getString(4));
				if (rs.getString(5)==null)
					x.add("");
				else
					x.add(rs.getString(5));
				all.add(x);
			}

			for(int i = 0; i<all.size(); ++i) {
				//System.out.println(allFunds.get(i).toString());
			}
			System.out.println("ACTIVITYs SIZE: " + all.size());
		}

		catch (SQLException e2) {
			System.out.println("Too many were returned" );
			e2.printStackTrace();
			return null;
		}
		return all;
	}
	public ArrayList<ArrayList<String>> getAccounts() {
		ArrayList<ArrayList<String>> all = new ArrayList<ArrayList<String>>();
		ArrayList<String> x;

		Statement stmt;
		try {
			stmt = connection.createStatement();

			rs = stmt.executeQuery(
					"select ftvacct_coas_code,ftvacct_acct_code,trunc(ftvacct_eff_date),trunc(ftvacct_nchg_date),trunc(ftvacct_term_date) " +
							"from fimsmgr.ftvacct " +
							"where  ftvacct_data_entry_ind = 'Y' " +
							"and ftvacct_status_ind = 'A' ");

			while (rs.next()) {
				x = new ArrayList<String>();
				x.add( rs.getString(1));
				x.add( rs.getString(2));
				x.add( rs.getString(3));
				x.add( rs.getString(4));
				if (rs.getString(5)==null)
					x.add("");
				else
					x.add(rs.getString(5));
				all.add(x);
			}
			System.out.println("ACCOUNTs SIZE: " + all.size());
		}

		catch (SQLException e2) {
			System.out.println("Too many were returned" );
			e2.printStackTrace();
			return null;
		}
		return all;
	}

}
