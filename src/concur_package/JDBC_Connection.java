package concur_package;
import java.sql.Connection;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


class JDBC_Connection extends public_package.JDBC_Connection{
	JDBC_Connection(Connection conn,String env, String user, String pass) {
		super(env, user, pass);
		connection=conn;
	}
	void reintialize(Connection conn) {
		connection = conn;
	}
	public String getOrgn(String cwid) {
		String orgn = "";
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery("select concur_orgn_code from ua_fimsmgr.concur_emp_feed where concur_cwid = '" + cwid + "' ");
			int count = 0;
			while (rs.next()) {
				++count;
				orgn = rs.getString(1);
			}
			if(count==1)
				return orgn;
			else 
				return null;
		}
		catch (SQLException e2) {
			System.out.println("Too many people were returned - " + cwid);
			e2.printStackTrace();
		}
		System.out.println("Error getting CWID.");
		return null;
	}
	public String getNameFromTrackingTable(String cwid) {
		String name = "";
		Statement stmt = null;
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

		Statement stmt = null;
		ResultSetMetaData rsmd = null;
		String message="",result="",row="";
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
					if(row=="")
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
	public String generatePaid(String check_date) {
		System.out.println("Creating statement...");
		Statement stmt = null;
		String message = "100,LF,KEY";
		String result = "";
		System.out.println("Executing query...");
		try {
			stmt = connection.createStatement();
			String query = "select to_number(substr(fabinvh_vend_inv_code,3,11)) as \"Request Key\",spriden_first_name|| ' ' || spriden_last_name as \"Vendor Name\", fabinvh_code as \"Invoice Number\", fabchka_gross_amt as \"Invoice Amount\",fabinvh_open_paid_ind as \"Payment Status\", to_char(fabchka_check_date,'yyyymmdd') as \"Payment Status Date\", fabchka_check_num as \"Check Number\" from fimsmgr.fabinvh, saturn.spriden, fimsmgr.fabchka, fimsmgr.farinvc where fabinvh_vend_pidm = spriden_pidm and fabinvh_code = fabchka_inv_code and fabinvh_code = farinvc_invh_code and spriden_change_ind is null and fabinvh_vend_inv_code like 'CI%' and farinvc_comm_code = 'APCNCR'";
			if (!check_date.equals(""))
				query = query + " and to_char(fabchka_check_date,'yyyymmdd') = " + check_date;
			rs = stmt.executeQuery(query);
			System.out.println("Extracting data...");
			String payment_type = "";
			while (rs.next()) {
				payment_type = rs.getString(5).equals("P") || rs.getString(5) == "P" ? "Paid" : rs.getString(5);
				result = rs.getString(1) + "," + rs.getString(2) + "," + rs.getString(3) + "," + rs.getString(4) + "," + payment_type + "," + rs.getString(6) + "," + rs.getString(7) + "," + "," + "," + "," + "," + "," + "," + "," + "," + "," + "," + "," + "," + "," + "," + "," + "," + "," + "," + "," + "," + "," + "," + "," + "," + "," + "," + "," + "," + "," + "," + "," + "," + "," + ",";
				message = message + "\n" + result;
			}
			if (stmt != null)
				stmt.close();
		}
		catch (SQLException e2) {
			System.out.println("Error Executing Query");
			e2.printStackTrace();
		}
		System.out.println("Finished executing query.");
		return message;
	}

	public String generateReceipted(String check_date) {
		System.out.println("Creating statement...");
		Statement stmt = null;
		String message = "100,LF,KEY";
		String result = "";
		System.out.println("Executing query...");
		try {
			stmt = connection.createStatement();
			String query = "select to_number(substr(fabinvh_vend_inv_code,3,11)) as \"Request Key\",spriden_first_name|| ' ' || spriden_last_name as \"Vendor Name\", fabinvh_code as \"Invoice Number\", fabchka_gross_amt as \"Invoice Amount\",fabinvh_open_paid_ind as \"Payment Status\", to_char(fabchka_check_date,'yyyymmdd') as \"Payment Status Date\", fabchka_check_num as \"Check Number\" from fimsmgr.fabinvh, saturn.spriden, fimsmgr.fabchka, fimsmgr.farinvc where fabinvh_vend_pidm = spriden_pidm and fabinvh_code = fabchka_inv_code and fabinvh_code = farinvc_invh_code and spriden_change_ind is null and fabinvh_vend_inv_code like 'CE%' and farinvc_comm_code = 'APCNCR'";
			if (!check_date.equals(""))
				query = query + " and to_char(fabchka_check_date,'yyyymmdd') = " + check_date;
			rs = stmt.executeQuery(query);
			System.out.println("Extracting data...");
			String payment_type = "";
			while (rs.next()) {
				payment_type = rs.getString(7).startsWith("!") ? "E" : "C";
				result = "600," + rs.getString(4) + "," + rs.getString(6) + "," + "," + payment_type + "," + rs.getString(7) + "," + rs.getString(1) + "," + "," + "," + "," + "," + ",";
				message = message + "\n" + result;
			}
			if (stmt != null)
				stmt.close();
		}
		catch (SQLException e2) {
			System.out.println("Error Executing Query");
			e2.printStackTrace();
		}
		System.out.println("Finished executing query.");
		return message;
	}

	public ArrayList<ArrayList<String>> getFunds() {
		ArrayList<ArrayList<String>> all = new ArrayList<ArrayList<String>>();
		ArrayList<String> x = new ArrayList<String>();

		Statement stmt = null;
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


	public boolean cwidExistsInTrackingTable(String cwid) {
		Statement stmt = null;
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

	public ArrayList<String> getActiveUsers() {
		ArrayList<String> all = new ArrayList<String>();

		Statement stmt = null;
		try {
			stmt = connection.createStatement();

			rs = stmt.executeQuery(
					"select concur_cwid " + 
							"from ua_fimsmgr.concur_emp_feed " + 
					"where concur_active = 'Y'");

			while (rs.next()) {
				all.add( rs.getString(1));
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
	public ArrayList<String> getVendors() {
		ArrayList<String> all = new ArrayList<String>();
		Statement stmt = null;
		try {
			stmt = connection.createStatement();

			rs = stmt.executeQuery(
					"select concur_vendor_cwid||','||concur_vendor_addr_code " + 
							"from ua_fimsmgr.concur_vendor_feed " + 
							"where concur_vendor_active = 'ACTIVE' " + 
					"order by concur_vendor_cwid");

			while (rs.next())
				all.add( rs.getString(1));

			System.out.println("ACTIVE VENDORS IN TRACKING: " + all.size());
		}

		catch (SQLException e2) {
			System.out.println("Too many were returned" );
			e2.printStackTrace();
			return null;
		}
		return all;
	}

	public String getVendorInfo(String cwid) {
		System.out.println("here..");
		Statement stmt = null;
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
	protected String makeVendorString(ArrayList<String> contents) {
		StringBuilder retStr = new StringBuilder();
		retStr.append("?");
		retStr.append("VendorCode="+contents.get(0));
		return retStr.toString();
	}
	public ArrayList<ArrayList<String>> getOrgns() {
		ArrayList<ArrayList<String>> all = new ArrayList<ArrayList<String>>();
		ArrayList<String> x = new ArrayList<String>();

		Statement stmt = null;
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
		ArrayList<String> x = new ArrayList<String>();

		Statement stmt = null;
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
		ArrayList<String> x = new ArrayList<String>();

		Statement stmt = null;
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
		ArrayList<String> x = new ArrayList<String>();

		Statement stmt = null;
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

	public String[] getFOPFromTrackingTable(String cwid) {
		String[] fop= {null,null,null,null};
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
			rs =  stmt.executeQuery("select concur_coas_code,concur_fund_code,concur_orgn_code,concur_prog_code "
					+ "from ua_fimsmgr.concur_emp_feed "
					+ "where concur_cwid='" + cwid + "' ");
			int count = 0;
			while (rs.next()) {
				++count;
				fop[0] = rs.getString(1);
				fop[1] = rs.getString(2);
				fop[2] = rs.getString(3);
				fop[3] = rs.getString(4);
			}
			if(count==1)
				return fop;
		}
		catch (SQLException e2) {
			System.out.println("Too many people were returned - " + cwid);
			e2.printStackTrace();
		}
		return null;
	}

	public String getCWIDFromTrackingTable(String str) {
		String cwid = null,first = "",last = "",middle = "";

		str = str.trim();
		if (str.contains(",") && str.split(" ").length == 3) {
			first = str.substring(str.indexOf(", ") + 2, str.lastIndexOf(" "));
			middle = str.substring(str.lastIndexOf(" ") + 1);
			last = str.substring(0, str.indexOf(","));
		} else if (str.contains(",") && str.split(" ").length == 2) {
			first = str.substring(str.indexOf(", ") + 2);
			middle = null;
			last = str.substring(0, str.indexOf(","));
		} else if (!str.contains(",") && str.split(" ").length == 3) {
			first = str.substring(0, str.indexOf(" "));
			middle = str.substring(str.indexOf(" ") + 1, str.lastIndexOf(" "));
			last = str.substring(str.lastIndexOf(" ") + 1);
		} else if (!str.contains(",") && str.split(" ").length == 2) {
			first = str.substring(0, str.indexOf(" "));
			middle = null;
			last = str.substring(str.indexOf(" ") + 1);
		}
		first = first.substring(0, 1).toUpperCase() + first.substring(1);
		last = last.substring(0, 1).toUpperCase() + last.substring(1);
		if (middle != null) {
			middle = middle.substring(0, 1).toUpperCase() + middle.substring(1);
		}
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
			rs = middle == null ? stmt.executeQuery("select concur_cwid from ua_fimsmgr.concur_emp_feed where concur_last_name = '" + last + "' " + "and concur_first_name like '" + first + "%'") : stmt.executeQuery("select concur_cwid from ua_fimsmgr.concur_emp_feed where concur_last_name='" + last + "' " + "and concur_first_name like '" + first + "%'" + "and concur_mi like '" + middle + "%'");
			int count = 0;
			while (rs.next()) {
				++count;
				cwid = rs.getString(1);
			}
			if(count==1)
				return cwid;
		}
		catch (SQLException e2) {
			e2.printStackTrace();
		}
		return null;
	}
	public String cwidIsActiveMessage(String cwid) {
		Statement stmt = null;
		String message = "";
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery("select 'Y' from ua_fimsmgr.concur_emp_feed where concur_cwid='" + cwid + "' and concur_active='Y'");
			if (rs.next())
				if (onPayroll(cwid)) 
					message = "CWID " + cwid + " is in the tracking table as active and is in ua_payroll.actdemo.";
				else
					message = "CWID " + cwid + " is in the tracking table as active BUT NOT in the ua_payroll.actdemo table.";
			else
				if (onPayroll(cwid))
					message = "CWID " + cwid + " is not the tracking table BUT is as active is ua_payroll.actdemo.";
				else
					message = "CWID " + cwid + " is not in the tracking table as active OR in the ua_payroll.actdemo table.";
		}
		catch (SQLException e2) {
			System.out.println("Error Executing Query");
			e2.printStackTrace();
		}
		System.out.println("Finished executing query.");
		return message;
	}

	public boolean onPayroll(String cwid) {
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery("select 'Y' from ua_payroll.actdemo where actdemo_id = '" + cwid + "'");
            return rs.next();
        }
		catch (SQLException e2) {
			System.out.println("Error Executing Query");
			e2.printStackTrace();
			return false;
		}
	}

	public boolean cwidIsActive(String cwid) {
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery("select 'Y' from ua_fimsmgr.concur_emp_feed where concur_cwid='" + cwid + "' " + "and concur_active='Y'");
            return rs.next();
        }
		catch (SQLException e2) {
			System.out.println("Error Executing Query");
			e2.printStackTrace();
			return false;
		}
	}


}
