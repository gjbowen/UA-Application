package ar_package;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;

class JDBC extends public_package.JDBC_Connection {
	JDBC(Connection conn, String user, String pass, String env) {
		super(user, pass, env);
		connection = conn;
	}

	String getAccountInformation(String cwid) {
		String message;
		String pidm = cwid;
		if (isCWID(cwid))
			pidm = getPIDMFromCWID(cwid);
		message = "Name: " + getNameFromPIDM(pidm) + "\n" +
				"CWID: " + cwid + "\n" +
				"PIDM: " + pidm + "\n" +
				"Account Balanace: " + getBalance(pidm) + "\n" +
				"Account Transactions: " + getBalance(pidm) + "\n" +
				"Holds: " + getHolds(pidm) + "\n" +
				"Active Attributes: " + getAttributes(pidm);

		return message;
	}

	private String getHolds(String pidm) { //NEEDS FIXED
		if (pidm == null)
			return null;
		else if (pidm.equals("NONE_RETURNED"))
			return "NONE_RETURNED";
		else if (pidm.equals("TOO_MANY_RETURNED"))
			return "TOO_MANY_RETURNED";

		String attributes = "";
		Statement stmt;
		try {
			stmt = connection.createStatement();

			ResultSet rs = stmt.executeQuery(
					"select a.sgrsatt_atts_code "
							+ "from saturn.sgrsatt a "
							+ "where  a.sgrsatt_pidm = " + pidm + " "
							+ "and a.sgrsatt_term_code_eff = (select max(b.sgrsatt_term_code_eff) "
							+ "from saturn.sgrsatt b "
							+ "where a.sgrsatt_pidm = b.sgrsatt_pidm) ");


			int count = 0;
			while (rs.next()) {
				++count;
				attributes = attributes + rs.getString(1) + " ";
			}
			if (count == 0) {
				return "NONE_RETURNED";
			}
			return attributes.trim();
		} catch (SQLException e2) {
			System.out.println("Too many people were returned - " + pidm);
			e2.printStackTrace();
		}
		return null;
	}

	private String getAttributes(String pidm) {
		if (pidm == null)
			return null;
		else if (pidm.equals("NONE_RETURNED"))
			return "NONE_RETURNED";
		else if (pidm.equals("TOO_MANY_RETURNED"))
			return "TOO_MANY_RETURNED";

		String attributes = "";
		Statement stmt;
		try {
			stmt = connection.createStatement();

			ResultSet rs = stmt.executeQuery(
					"select a.sgrsatt_atts_code "
							+ "from saturn.sgrsatt a "
							+ "where  a.sgrsatt_pidm = " + pidm + " "
							+ "and a.sgrsatt_term_code_eff = (select max(b.sgrsatt_term_code_eff) "
							+ "from saturn.sgrsatt b "
							+ "where a.sgrsatt_pidm = b.sgrsatt_pidm) ");


			int count = 0;
			while (rs.next()) {
				++count;
				attributes = attributes + rs.getString(1) + " ";
			}
			if (count == 0) {
				return "NONE_RETURNED";
			}
			return attributes.trim();
		} catch (SQLException e2) {
			System.out.println("Too many people were returned - " + pidm);
			e2.printStackTrace();
		}
		return null;
	}

	String getTransactions(String pidm) {
		if (pidm == null)
			return null;
		else if (pidm.equals("NONE_RETURNED"))
			return "NONE_RETURNED";
		else if (pidm.equals("TOO_MANY_RETURNED"))
			return "TOO_MANY_RETURNED";
		else if (isCWID(pidm))
			pidm = getPIDMFromCWID(pidm);

		Statement stmt;
		try {
			stmt = connection.createStatement();

			ResultSet rs = stmt.executeQuery(
					"select tbraccd_pidm,"
							+ "tbraccd_term_code,"
							+ "tbraccd_tran_number,"
							+ "tbraccd_detail_code,"
							+ "tbraccd_user,"
							+ "tbraccd_entry_date,"
							+ "tbraccd_amount,"
							+ "tbraccd_balance,"
							+ "to_char(tbraccd_effective_date,'MM/DD/YYYY') tbraccd_effective_date,"
							+ "to_char(tbraccd_bill_date,'MM/DD/YYYY') tbraccd_bill_date,"
							+ "to_char(tbraccd_due_date,'MM/DD/YYYY') tbraccd_due_date,"
							+ "tbraccd_desc "
							+ "from taismgr.tbraccd "
							+ "where tbraccd_pidm  = " + pidm.trim() + " "
							+ "order by tbraccd_tran_number desc");

			return prettyPrint(rs);
		} catch (SQLException e2) {
			System.out.println("SQL error in getTransactions - " + pidm);
			e2.printStackTrace();
		}
		return null;
	}

	String getDetailCodes(String code) {
		Statement stmt;
		try {
			stmt = connection.createStatement();

			ResultSet rs = stmt.executeQuery(
					"select TBBDETC_DETAIL_CODE DETAIL_CODE," +
							"TBBDETC_DESC description," +
							"TBBDETC_TYPE_IND	type_ind," +
							"TBBDETC_PRIORITY priority	," +
							"TBBDETC_LIKE_TERM_IND like_term_ind," +
							"TBBDETC_DCAT_CODE	dcat_code," +
							"TBBDETC_AMOUNT	amount," +
							"TBBDETC_TERM_CODE TERM_CODE," +
							"TBBDETC_REFUND_IND REFUND_IND," +
							"TBBDETC_ACTIVITY_DATE ACTIVITY_DATE," +
							"TBBDETC_PAYT_CODE PAYT_CODE," +
							"TBBDETC_GL_NOS_ENTERABLE GL_NOS_ENTERABLE," +
							"TBBDETC_TAXT_CODE TAXT_CODE," +
							"TBBDETC_TBDC_IND TBDC_IND," +
							"TBBDETC_DETAIL_CODE_IND DETAIL_CODE_IND," +
							"TBBDETC_DETC_ACTIVE_IND DETC_ACTIVE_IND," +
							"TBBDETC_DIRD_IND DIRD_IND," +
							"TBBDETC_TIV_IND TIV_IND," +
							"TBBDETC_INST_CHG_IND INST_CHG_IND," +
							"TBBDETC_LIKE_AIDY_IND LIKE_AIDY_IND " +
							"from tbbdetc "
							+ "where TBBDETC_DETAIL_CODE like upper('" + code + "') "
							+ "order by 1 asc");
			return prettyPrint(rs);
		} catch (SQLException e2) {
			System.out.println("Too many people were returned - " + code);
			e2.printStackTrace();
		}
		return null;
	}

	String getFootballDetailCodes(String cwid, String year) {
		Statement stmt;
		String pidm;
		try {
			stmt = connection.createStatement();
			if (isCWID(cwid))
				pidm = getPIDMFromCWID(cwid);
			else
				pidm = cwid;
			ResultSet rs = stmt.executeQuery(
					"select decode(TBRACCD_DETAIL_CODE,'OSFT','A-B', " +
							"                                     'OSFA','A', " +
							"                                     'OSFB','B', " +
							"                                     'OSF3','C', " +
							"                                     'OSF4','D', " +
							"                                     'OSF5','E') as \"PACKAGE\", " +
							" TBRACCD_DETAIL_CODE as \"DETAIL_CODE\", " +
							" tbraccd_amount as \"TRANSACTION_AMOUNT\", " +
							"(select tbbdetc_desc from taismgr.tbbdetc " +
							"  where tbbdetc_detail_code = tbraccd_detail_code) as \"CODE_DESCRIPTION\"," +
							" tbraccd_trans_date as \"TRANSACTION_DATE\"" +
							"from saturn.spriden,taismgr.tbraccd " +
							"where spriden_pidm = tbraccd_pidm " +
							"and spriden_pidm = " + pidm + " " +
							"and spriden_change_ind is null " +
							"and tbraccd_term_code = '" + year + "40' " +
							"and tbraccd_detail_code in ('OSFA','OSFB','OSF3','OSF4','OSF5','OSFT','OSFH')");


			return prettyPrint(rs);
		} catch (SQLSyntaxErrorException e1) {
			return "INVALID CWID/PIDM - " + cwid;
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		return null;
	}

	String getFootballHistory(String cwid, String year) {
		Statement stmt;
		String pidm;
		try {
			stmt = connection.createStatement();
			if (isCWID(cwid))
				pidm = getPIDMFromCWID(cwid);
			else
				pidm = cwid;
			ResultSet rs = stmt.executeQuery(
					"select " +
							"TBSFBT_RULE_YEAR      		AS \"YEAR           \"," +
							"TBSFBT_PIDM           		AS \"PIDM           \"," +
							"TBSFBT_OPT_IN_DATE    		AS \"OPT_IN_DATE    \"," +
							"TBSFBT_DATE_ORDERED   		AS \"DATE_ORDERED   \"," +
							"TBSFBT_ATTEMPT_DATE   		AS \"ATTEMPT_DATE   \"," +
							"TBSFBT_REG_NUMBER     		AS \"REG_NUMBER     \"," +
							"TBSFBT_REMOVED        		AS \"REMOVED        \"," +
							"TBSFBT_GROUPID        		AS \"GROUP_ID       \"," +
							"TBSFBT_USERID         		AS \"USER_ID        \"," +
							"TBSFBT_SOURCE         		AS \"SOURCE         \"," +
							"TBSFBT_PLAN_PURCHASED 		AS \"PLAN_PURCHASED \"," +
							//							"TBSFBT_ACTION       		AS \"ACTION      	\"," + 
							"TBSFBT_ACTIVITY_DATE		AS \"ACTIVITY_DATE  \" "
							+ "from ua_taismgr.ua_tbsfbt_history "
							+ "where tbsfbt_action = 'AFTER UPDATE' "
							+ "and tbsfbt_pidm = " + pidm + " "
							+ "and tbsfbt_rule_year = '" + year + "'");


			return prettyPrint(rs);
		} catch (SQLSyntaxErrorException e1) {
			return "INVALID CWID/PIDM - " + cwid;
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		return null;
	}

	String getFootballInfo(String cwid, String year) {
		Statement stmt;
		String pidm;
		try {
			stmt = connection.createStatement();
			if (isCWID(cwid))
				pidm = getPIDMFromCWID(cwid);
			else
				pidm = cwid;
			ResultSet rs = stmt.executeQuery(
					"select " +
							"TBSFBT_RULE_YEAR      		AS \"YEAR           \"," +
							"TBSFBT_PIDM           		AS \"PIDM           \"," +
							"TBSFBT_OPT_IN_DATE    		AS \"OPT_IN_DATE    \"," +
							"TBSFBT_DATE_ORDERED   		AS \"DATE_ORDERED   \"," +
							"TBSFBT_ATTEMPT_DATE   		AS \"ATTEMPT_DATE   \"," +
							"TBSFBT_REG_NUMBER     		AS \"REG_NUMBER     \"," +
							"TBSFBT_REMOVED        		AS \"REMOVED        \"," +
							"TBSFBT_GROUPID        		AS \"GROUP_ID       \"," +
							"TBSFBT_USERID         		AS \"USER_ID        \"," +
							"TBSFBT_SOURCE         		AS \"SOURCE         \"," +
							"TBSFBT_ELIGIBLE_HOURS 		AS \"UA_HOURS       \"," +
							"TBSFBT_STU_LEVEL      		AS \"STU_LEVEL      \"," +
							"TBSFBT_STU_CLASS      		AS \"STU_CLASS      \"," +
							"TBSFBT_PLAN_PURCHASED 		AS \"PLAN_PURCHASED \"," +
							"TBSFBT_LOAD_DATE       	AS \"LOAD_DATE      \"," +
							"TBSFBT_ALL_ELIGIBLE_HOURS 	AS \"ALL_HOURS      \" "
							+ "from ua_taismgr.ua_tbsfbt "
							+ "where tbsfbt_pidm = " + pidm + " "
							+ "and tbsfbt_rule_year = '" + year + "'");


			ResultSetMetaData rsmd = rs.getMetaData();
			System.out.println("Extracting data...");
			//STEP 5: Extract data from result set
			String message = "";
			while (rs.next()) {
				//Retrieve by column name
				String row = "";
				for (int i = 1; i < rsmd.getColumnCount() + 1; ++i) {
					String result = rs.getString(i);
					if (result == null)
						result = "";

					if (row.equals("")) {
						if (rsmd.getColumnName(i).trim().equals("GROUP_ID")) {
							String group = getFootballGroupDesc(result, year);
							row = rsmd.getColumnName(i) + " " + group + ": " + result + " - " + group;
						} else {
							//System.out.println(rsmd.getColumnName(i));
							row = rsmd.getColumnName(i) + ": " + result;
						}
					} else {
						if (rsmd.getColumnName(i).trim().equals("GROUP_ID")) {
							String group = getFootballGroupDesc(result, year);
							row = row + "\n" + rsmd.getColumnName(i) + ": " + result + " - " + group;
						} else {
//							System.out.println(rsmd.getColumnName(i));
							row = row + "\n" + rsmd.getColumnName(i) + ": " + result;
						}
					}
				}
				message = message + row + "\n";

			}
			stmt.close();
			return message;
		} catch (SQLSyntaxErrorException e1) {
			return "INVALID CWID/PIDM - " + cwid;
		} catch (SQLException e2) {
			e2.printStackTrace();
		}

		return null;
	}

	private String getFootballGroupDesc(String group, String year) {
		Statement stmt;
		String message = "";
		try {
			stmt = connection.createStatement();

			ResultSet rs = stmt.executeQuery(
					"select " +
							"tvsfbt_groupdesc "
							+ "from ua_taismgr.ua_tvsfbt "
							+ "where tvsfbt_groupid = '" + group + "' "
							+ "and tvsfbt_year = '" + year + "'");
			//STEP 5: Extract data from result set
			while (rs.next()) {
				message = rs.getString(1);
			}
			return message;
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		return null;
	}

	String getFootballYear() {
		Statement stmt;
		String message = "";
		try {
			stmt = connection.createStatement();

			ResultSet rs = stmt.executeQuery(
					"select tvsfbt_cur_rule_year " +
							"from ua_taismgr.ua_tvsfbt_cur_rule_year");
			//STEP 5: Extract data from result set
			while (rs.next()) {
				message = rs.getString(1);
			}
			return message;
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		return null;
	}

	String getFootballGroupReason(String cwid, String year) {
		Statement stmt;
		String pidm;
		try {
			stmt = connection.createStatement();
			if (isCWID(cwid))
				pidm = super.getPIDMFromCWID(cwid);
			else
				pidm = cwid;
			ResultSet rs = stmt.executeQuery(
					"select " +
							"TBSFBT_RULE_YEAR      		AS \"YEAR           \"," +
							"TBSFBT_PIDM           		AS \"PIDM           \"," +
							"TBSFBT_OPT_IN_DATE    		AS \"OPT_IN_DATE    \"," +
							"TBSFBT_DATE_ORDERED   		AS \"DATE_ORDERED   \"," +
							"TBSFBT_ATTEMPT_DATE   		AS \"ATTEMPT_DATE   \"," +
							"TBSFBT_REG_NUMBER     		AS \"REG_NUMBER     \"," +
							"TBSFBT_REMOVED        		AS \"REMOVED        \"," +
							"TBSFBT_GROUPID        		AS \"GROUP_ID       \"," +
							"TBSFBT_USERID         		AS \"USER_ID        \"," +
							"TBSFBT_SOURCE         		AS \"SOURCE         \"," +
							"TBSFBT_PLAN_PURCHASED 		AS \"PLAN_PURCHASED \""
							+ "from ua_taismgr.ua_tbsfbt "
							+ "where tbsfbt_pidm = " + pidm + " "
							+ "and tbsfbt_rule_year = '" + year + "'");

			ResultSetMetaData rsmd = rs.getMetaData();
			System.out.println("Extracting data...");
			String group = null, plan_purchased = null, removed = null, reg_number = null, date_ordered = null, opt_in_date = null;
			while (rs.next()) {
				//Retrieve by column name
				for (int i = 1; i < rsmd.getColumnCount() + 1; ++i) {
					String result = rs.getString(i);
					if (result == null)
						result = "";

					if (rsmd.getColumnName(i).trim().equals("GROUP_ID"))
						group = result;
					else if (rsmd.getColumnName(i).trim().equals("REG_NUMBER"))
						reg_number = result;
					else if (rsmd.getColumnName(i).trim().equals("PLAN_PURCHASED"))
						plan_purchased = result;
					else if (rsmd.getColumnName(i).trim().equals("REMOVED"))
						removed = result;
					else if (rsmd.getColumnName(i).trim().equals("DATE_ORDERED"))
						date_ordered = result;
					else if (rsmd.getColumnName(i).trim().equals("OPT_IN_DATE"))
						opt_in_date = result;
				}
			}
			stmt.close();
			return getReason(group, plan_purchased, removed, reg_number, date_ordered, opt_in_date);
		} catch (SQLSyntaxErrorException e1) {
			return "INVALID CWID/PIDM - " + cwid;
		} catch (SQLException e2) {
			e2.printStackTrace();
		}

		return null;
	}

	private String getReason(String group, String plan_purchased, String removed, String reg_number, String date_ordered, String opt_in_date) {
		if (group.equals("GRP1"))
			return "Student never opted in.";
		else if (!removed.equals("N"))
			return "Student was removed manually from group " + group + ".";

		else if (group.equals("GRP2"))
			return "Student opted in on " + opt_in_date + " and will receive the full package.";

		else if (group.equals("GRP5") && reg_number.equals(""))
			return "Student opted in on " + opt_in_date + " but never selected a package. Package " + plan_purchased + " was randomly assigned.";
		else if (group.equals("GRP5") && !reg_number.equals(""))
			return "Student opted in and selected package " + plan_purchased + " on " + date_ordered + ".";

		else if (group.equals("GRP7") && reg_number.equals(""))
			return "Freshman hasn't ordered anything.";
		else if (group.equals("GRP7") && !reg_number.equals(""))
			return "Freshman ordered package " + plan_purchased + ".";

		else if (group.equals("GRP8") && reg_number.equals(""))
			return "Non-Freshman(transfer) hasn't ordered anything.";
		else if (group.equals("GRP8") && !reg_number.equals(""))
			return "Non-Freshman(transfer) ordered package " + plan_purchased + ".";

		else if (group.equals("GRP13") && !reg_number.equals(""))
			return "Freshman/transfer ordered package C and " + plan_purchased + ".";
		else if (group.equals("GRP13") && reg_number.equals(""))
			return "Freshman/transfer ordered package C and can purchase an additional package.";

		else if (group.equals("GRP14") && !reg_number.equals(""))
			return "Freshman/transfer ordered package D and " + plan_purchased + ".";
		else if (group.equals("GRP14") && reg_number.equals(""))
			return "Freshman/transfer ordered package D and can purchase an additional package.";

		else if (group.equals("GRP15") && !reg_number.equals(""))
			return "Freshman/transfer ordered package E and " + plan_purchased + ".";
		else if (group.equals("GRP15") && reg_number.equals(""))
			return "Freshman/transfer ordered package E and can purchase an additional package.";
		else
			return "UNKNOWN/UNDEFINED REASON\n\n" +
					"  group: " + group + "\n" +
					"  plan_purchased: " + plan_purchased + "\n" +
					"  removed: " + removed + "\n" +
					"  reg_number: " + reg_number + "\n" +
					"  date_ordered: " + date_ordered + "\n" +
					"  opt_in_date: " + opt_in_date;
	}

	String getFootballGroups(String year) {
		Statement stmt;
		try {
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(
					"select * from ua_taismgr.ua_TVSFBT " +
							"where TVSFBT_YEAR = '" + year + "'");

			return prettyPrint(rs);
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		return null;
	}

	String getFootballGames(String year) {
		Statement stmt;
		try {
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(
					"select   TVSFBT_PKG_TYPE as \"TYPE\", " +
							"  TVSFBT_PKG_GAME1 as \"Game 1\", " +
							"  TVSFBT_PKG_GAME1_DATE as \"Game 1 Date\", " +
							"  TVSFBT_PKG_GAME2 as \"Game 2\", " +
							"  TVSFBT_PKG_GAME2_DATE as \"Game 2 Date\", " +
							"  TVSFBT_PKG_GAME3 as \"Game 3\", " +
							"  TVSFBT_PKG_GAME3_DATE as \"Game 3 Date\", " +
							"  TVSFBT_PKG_GAME4 as \"Game 4\", " +
							"  TVSFBT_PKG_GAME4_DATE as \"Game 4 Date\" from ua_taismgr.ua_tvsfbt_pkg " +
							"where tvsfbt_pkg_year = '" + year + "'");

			return prettyPrint(rs);
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		return null;
	}

	String getCategoryCodes(String code) {
		Statement stmt;
		try {
			stmt = connection.createStatement();

			ResultSet rs = stmt.executeQuery("select ttvdcat_code CODE, " +
					"ttvdcat_desc DESCRIPTION, " +
					"ttvdcat_activity_date ACTIVITY_DATE, " +
					"ttvdcat_sysreq_ind SYSREQ_IND " +
					"from taismgr.ttvdcat " +
					"where ttvdcat_code like upper('" + code + "') " +
					"order by ttvdcat_code");

			return prettyPrint(rs);
		} catch (SQLException e2) {
			System.out.println("Too many people were returned - " + code);
			e2.printStackTrace();
		}
		return null;
	}

	private String getBalance(String pidm) {
		if (pidm == null)
			return null;
		else if (pidm.equals("NONE_RETURNED"))
			return "NONE_RETURNED";
		else if (pidm.equals("TOO_MANY_RETURNED"))
			return "TOO_MANY_RETURNED";
		else
			pidm = pidm.trim();

		String balance = null;
		Statement stmt;
		try {
			stmt = connection.createStatement();

			ResultSet rs = stmt.executeQuery(
					"select to_char(sum(tbraccd_balance),'$999,900.00') "
							+ "from taismgr.tbraccd "
							+ "where  tbraccd_pidm = " + pidm);


			int count = 0;
			while (rs.next()) {
				++count;
				balance = rs.getString(1);
			}
			if (count == 0)
				return "NONE_RETURNED";
			else if (count == 1)
				return balance;
			else
				return "TOO_MANY_RETURNED";
		} catch (SQLException e2) {
			System.out.println("Too many people were returned - " + pidm);
			e2.printStackTrace();
		}
		return null;
	}

	boolean isRegistered(String cwid, String term) {
		Statement stmt;
		String message = "";
		try {
			stmt = connection.createStatement();

			ResultSet rs = stmt.executeQuery("select 'y' " +
					"from saturn.spriden, saturn.sfrstcr, saturn.stvrsts " +
					"where spriden_pidm = sfrstcr_pidm " +
					"and sfrstcr_pidm = "+getPIDMFromCWID(cwid)+" " +
					"and sfrstcr_rsts_code = stvrsts_code " +
					"and spriden_change_ind is null " +
					"and stvrsts_incl_sect_enrl = 'Y' " +
					"and stvrsts_withdraw_ind <> 'Y' " +
					"and sfrstcr_term_code = '"+term+"'");
			if (rs.next())
				return true;
			return false;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	boolean isConfirmed(String cwid, String term) {
		Statement stmt;
		String message = "";
		try {
			stmt = connection.createStatement();

			ResultSet rs = stmt.executeQuery("select 'Y' " +
					"from saturn.sfbetrm " +
					"where sfbetrm_ar_ind = 'Y' " +
					"and sfbetrm_pidm ="+getPIDMFromCWID(cwid)+" " +
					"and sfbetrm_term_code = '"+term+"'");
			if (rs.next())
				return true;
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	String getCampus(String cwid, String term) {
		Statement stmt;
		String message = "";
		try {
			stmt = connection.createStatement();

			ResultSet rs = stmt.executeQuery(
					"select ua_saturn.f_get_student_campus ("+getPIDMFromCWID(cwid)+",'"+term+"') from dual");
			if (rs.next())
				return rs.getString(1);
			return "MA";
		} catch (SQLException e) {
			e.printStackTrace();
			return "UNKNOWN";
		}
	}
	boolean isEMBA(String cwid, String term) {
		Statement stmt;
		String message = "";
		try {
			stmt = connection.createStatement();

			ResultSet rs = stmt.executeQuery("select distinct 'Y' " +
					"from saturn.sfrstcr, saturn.stvrsts " +
					"where sfrstcr_rsts_code = stvrsts_code " +
					"and stvrsts_incl_sect_enrl = 'Y' " +
					"and stvrsts_withdraw_ind <> 'Y' " +
					"and sfrstcr_camp_code in ('EH1','EM','EM1','EM4','EMH') " +
					"and sfrstcr_term_code = '"+term+"' " +
					"and sfrstcr_pidm = "+getPIDMFromCWID(cwid));
			if (rs.next())
				return true;
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	boolean isAHE(String cwid, String term) {
		Statement stmt;
		String message = "";
		try {
			stmt = connection.createStatement();

			ResultSet rs = stmt.executeQuery("select sum(tbraccd_amount) " +
					"from taismgr.tbraccd " +
					"where tbraccd_detail_code in ('TED2', 'TEED') " +
					"and tbraccd_term_code = '"+term+"' " +
					"and tbraccd_pidm = "+getPIDMFromCWID(cwid));
			if (rs.next())
				return true;
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}


}
