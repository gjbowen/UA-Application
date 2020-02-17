package public_package;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class JDBC_Connection {
	protected ResultSet rs;
	public Connection connection;
	private String environment;
	private String username;
	static public String password;
	String userFirstName;

	protected JDBC_Connection(String user, String pass,String env) {
		environment = env;
		username = user;
		password = pass;
		connection = null;
		rs = null;
	}

	private ArrayList<Integer> getMaxColumnSize(ArrayList<ArrayList<String>> all,Integer columnCount,ArrayList<String> header) {
		ArrayList<Integer> sizeArray = new ArrayList<Integer>();
		for (int i=0;i<columnCount;++i)
			//			sizeArray.add(i, 0); //uses max size of the columns
			sizeArray.add(i, header.get(i).length());  //adjusts to size of header text
		for(int i=0;i<all.size();++i)
			for(int j=0;j < all.get(i).size(); ++j) 
				if(all.get(i).get(j).length() > sizeArray.get(j))
					sizeArray.set(j, all.get(i).get(j).length());
		return sizeArray;
	}
	private int sumColumns(ArrayList<Integer> columnCount) {
		int sum=0;
		for(int i=0;i<columnCount.size();++i)
			sum+=columnCount.get(i)+5;
		return sum-2;
	}
	@SuppressWarnings("unused")
	private String rpad(String s, int n,char buffer) {
		String str = "";
		if(s.length()==n)
			return s;
		else if(s.length()>n)
			for(int i=0;i<n;++i) 
				str=str+s.charAt(i);
		else
			for(int i=0;i<n;++i)
				if(i<s.length())
					str=str+s.charAt(i);
				else
					str=str+buffer;

		return str;  
	}
	@SuppressWarnings("unused")
	private String lpad(String s, int n,char buffer) {
		String str = "";
		if(s.length()==n)
			return s;
		else if(s.length()>n)
			for(int i=0;i<n;++i)
				str=str+s.charAt(i);
		else 
			for(int i=0;i<n;++i)
				if(i<s.length())
					str=str+s.charAt(i);
				else
					str=buffer+str;
		return str;  
	}
	private ArrayList<String> getColumnNames(ResultSetMetaData rsmd) {
		ArrayList<String> header = new ArrayList<String>();
		try {
			for(int i=1;i<=rsmd.getColumnCount();++i)
				header.add(rsmd.getColumnName(i).replaceAll("TBRACCD_", ""));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return header;
	}
	public String prettyPrint(ResultSet rs) { 
		StringBuilder retVal = new StringBuilder();
		try {
			ResultSetMetaData rsmd = rs.getMetaData();

			ArrayList<ArrayList<String>> 	table = new ArrayList<ArrayList<String>>();
			ArrayList<String> 				header = getColumnNames(rsmd);
			ArrayList<String> 				tempRow;

			while (rs.next()) {
				tempRow = new ArrayList<String>();
				for(int i=1;i<=rsmd.getColumnCount();++i)
					if (rs.getString(i)==null)
						tempRow.add("");
					else
						tempRow.add(rs.getString(i));
				table.add(tempRow);
			}

			ArrayList<Integer> columnSizes = getMaxColumnSize(table,rsmd.getColumnCount(),header);

			//print header
			for(int i=0; i< columnSizes.size();++i) {
				for(int j=0;j<columnSizes.get(i);++j)
					if(header.get(i).length()>j) 
						retVal.append(header.get(i).charAt(j));
					else
						retVal.append(" ");
				retVal.append("  |  ");
			}
			retVal.append("\n");

			//print SPACING
			for(int i=0; i< sumColumns(columnSizes);++i)
				retVal.append("-");

			//print data
			retVal.append("\n");
			for(int i=0;i<table.size();++i) {
				for(int j=0;j<table.get(i).size();++j) {//current column
					for(int character=0;character < columnSizes.get(j); ++character)  //each TOKEN.
						if(character < table.get(i).get(j).length()) 
							retVal.append(table.get(i).get(j).charAt(character));
						else
							retVal.append(" ");
					retVal.append("  |  ");
				}
				retVal.append("\n");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return retVal.toString();
	}

	@SuppressWarnings("unused")
	public boolean isPIDM(String s) {
        return Integer.parseInt(s) > 0;
	}
	public boolean isCWID(String cwid) {
		if (cwid.length() != 8)
			return false;
		try {
			int cwidInt = Integer.parseInt(cwid);
            return cwidInt > 0;
		}
		catch (NumberFormatException nfe) {
			return false;
		}
	}

	private String getPIDMFromName(String name) {
		if(name==null || name.equals("TOO_MANY_RETURNED")|| name.equals("NONE_RETURNED"))
			return name;

		String first = "";
		String last = "";
		String middle = "";
		if (name.contains(",") && name.split(" ").length == 3) {// Last, First MI(Smith, John T)
			first = name.substring(name.indexOf(", ") + 2, name.lastIndexOf(" "));
			middle = name.substring(name.lastIndexOf(" ") + 1);
			last = name.substring(0, name.indexOf(","));
		}
		else if (name.contains(",") && name.split(" ").length == 2) {// Last, First (Smith, John)
			first = name.substring(name.indexOf(", ") + 2);
			middle = null;
			last = name.substring(0, name.indexOf(","));
		}
		else if (!name.contains(",") && name.split(" ").length == 3) {// First MI Last (John T Smith)
			first = name.substring(0, name.indexOf(" "));
			middle = name.substring(name.indexOf(" ") + 1, name.lastIndexOf(" "));
			last = name.substring(name.lastIndexOf(" ") + 1);
		}
		else if (!name.contains(",") && name.split(" ").length == 2) {// First Last (John Smith)
			first = name.substring(0, name.indexOf(" "));
			middle = null;
			last = name.substring(name.indexOf(" ") + 1);
		}

		first = first.toUpperCase();
		if (middle!=null)
			middle = middle.toUpperCase();
		last = last.toUpperCase();

		Statement stmt;
		String query;
		String pidm = "";
		try {
			stmt = connection.createStatement();
			if(middle == null) {
				query =   "select spriden_pidm "
						+ "from saturn.spriden "
						+ "where upper(spriden_last_name) = upper('" + last + "') "
						+ "and upper(spriden_first_name) like upper('" + first + "%') "
						+ "and spriden_change_ind is null";

			}
			else {
				query =  "select spriden_pidm "
						+ "from saturn.spriden "
						+ "where spriden_change_ind is null "
						+ "and upper(spriden_first_name) like '" + first + "%' "
						+ "and upper(spriden_mi) like '" + middle + "%' "
						+ "and upper(spriden_last_name) = '" + last + "'";
			}
			ResultSet rs = stmt.executeQuery(query);

			int count = 0;
			while (rs.next()) {
				++count;
				pidm = rs.getString(1);
			}
			if(count==0)
				return "NONE_RETURNED";
			else if(count==1)
				return pidm;
			else
				return "TOO_MANY_RETURNED";
		}
		catch (SQLException e2) {
			System.out.println("SQL error in getCWIDFromName - "+e2.getMessage());
			e2.printStackTrace();
		}
		return null;
	}
	public String getPIDMFromCWID(String cwid) {
		if(cwid==null || cwid.equals("TOO_MANY_RETURNED")|| cwid.equals("NONE_RETURNED"))
			return cwid;

		String pidm = null;
		Statement stmt;
		try {
			stmt = connection.createStatement();

			ResultSet rs = stmt.executeQuery(
					"select spriden_pidm "
							+ "from saturn.spriden "
							+ "where spriden_id = '" + cwid.trim() + "' "
							+ "and spriden_change_ind is null");


			int count = 0;
			while (rs.next()) {
				++count;
				pidm = rs.getString(1);
			}
			if(count==0)
				return "NONE_RETURNED";
			else if(count==1)
				return pidm;
			else
				return "TOO_MANY_RETURNED";
		}
		catch (SQLException e2) {
			System.out.println("SQL error in getPIDMFromCWID - "+e2.getMessage());
			e2.printStackTrace();
		}
		return null;
	}
	private String getPIDMFromMyBama(String myBama) {
		if(myBama==null || myBama.equals("TOO_MANY_RETURNED")|| myBama.equals("NONE_RETURNED"))
			return myBama;

		String pidm = null;
		Statement stmt;
		try {
			stmt = connection.createStatement();

			ResultSet rs = stmt.executeQuery(
					"select GOBTPAC_pidm from GENERAL.GOBTPAC " +
							"where upper(GOBTPAC_EXTERNAL_USER) = upper('"+myBama+"')");


			int count = 0;
			while (rs.next()) {
				++count;
				pidm = rs.getString(1);
			}
			if(count==0)
				return "NONE_RETURNED";
			else if(count==1)
				return pidm;
			else
				return "TOO_MANY_RETURNED";
		}
		catch (SQLException e2) {
			System.out.println("SQL error in getPIDMFromMyBama - "+e2.getMessage());
			e2.printStackTrace();
		}
		return null;
	}
	private String getPIDMFromEmail(String email) {
		if (email==null || email.equals("NONE_RETURNED") || email.equals("TOO_MANY_RETURNED"))
			return email;

		String pidm = null;
		Statement stmt;
		try {
			stmt = connection.createStatement();

			ResultSet rs = stmt.executeQuery(
					"select distinct GOREMAL_PIDM from goremal " +
							"where upper(goremal_email_address) = upper('"+email+"') " +
							"and GOREMAL_PREFERRED_IND = 'Y' " +
					"and GOREMAL_STATUS_IND = 'A'");


			int count = 0;
			while (rs.next()) {
				++count;
				pidm = rs.getString(1);
			}
			if(count==0)
				return "NONE_RETURNED";
			else if(count==1)
				return pidm;
			else
				return "TOO_MANY_RETURNED";
		}
		catch (SQLException e2) {
			System.out.println("SQL error in getPIDMFromEMail - " + e2.getMessage());
			e2.printStackTrace();
		}
		return null;
	}
	public String getCWIDFromPIDM(String pidm) {
		if (pidm==null || pidm.equals("NONE_RETURNED") || pidm.equals("TOO_MANY_RETURNED"))
			return pidm;

		String cwid = null;
		Statement stmt;
		try {
			stmt = connection.createStatement();

			ResultSet rs = stmt.executeQuery(
					"select spriden_id "
							+ "from saturn.spriden "
							+ "where spriden_pidm = " + pidm + " "
							+ "and spriden_change_ind is null");


			int count = 0;
			while (rs.next()) {
				++count;
				cwid = rs.getString(1);
			}
			if(!rs.isClosed())
				rs.close();
			if(count==0)
				return "NONE_RETURNED";
			else if(count==1)
				return cwid;
			else
				return "TOO_MANY_RETURNED";
		}
		catch (SQLException e2) {
			System.out.println("SQL error in getCWIDFromPIDM -" + e2.getMessage());
			e2.printStackTrace();
		}
		return null;
	}
	public String getNameFromPIDM(String pidm) {
		if (pidm==null || pidm.equals("NONE_RETURNED") || pidm.equals("TOO_MANY_RETURNED"))
			return pidm;

		String name = null;
		Statement stmt;
		try {
			stmt = connection.createStatement();

			ResultSet rs = stmt.executeQuery(
					"select spriden_first_name || ' ' || spriden_last_name "
							+ "from saturn.spriden "
							+ "where spriden_pidm = '" + pidm + "' "
							+ "and spriden_change_ind is null");


			int count = 0;
			while (rs.next()) {
				++count;
				name = rs.getString(1);
			}
			if(count==0)
				return "NONE_RETURNED";
			else if(count==1)
				return name;
			else
				return "TOO_MANY_RETURNED";
		}
		catch (SQLException e2) {
			System.out.println("SQL error in getNameFromPIDM - " + e2.getMessage());
			e2.printStackTrace();
		}
		return null;
	}
	private String getEmailFromPIDM(String pidm) {
		if(pidm==null || pidm.equals("TOO_MANY_RETURNED")|| pidm.equals("NONE_RETURNED"))
			return pidm;

		String email = null;
		Statement stmt;
		try {
			stmt = connection.createStatement();

			ResultSet rs = stmt.executeQuery(
					"select distinct GOREMAL_EMAIL_ADDRESS from goremal " +
							"where goremal_preferred_ind = 'Y' " +
							"and GOREMAL_STATUS_IND = 'A' " +
							"and goremal_pidm = " + pidm );

			int count = 0;
			while (rs.next()) {
				++count;
				email = rs.getString(1);
			}
			if(count==0)
				return "NONE_RETURNED";
			else if(count==1)
				return email;
			else
				return "TOO_MANY_RETURNED";
		}
		catch (SQLException e2) {
			System.out.println("SQL error in getEmailFromPIDM - " + e2.getMessage());
			e2.printStackTrace();
		}
		return null;
	}
	private String getMyBamaFromPIDM(String pidm) {
		if(pidm==null || pidm.equals("TOO_MANY_RETURNED")|| pidm.equals("NONE_RETURNED"))
			return pidm;

		String email = null;
		Statement stmt;
		try {
			stmt = connection.createStatement();

			ResultSet rs = stmt.executeQuery(
					"select GOBTPAC_EXTERNAL_USER from GENERAL.GOBTPAC " +
							"where gobtpac_pidm = " + pidm );

			int count = 0;
			while (rs.next()) {
				++count;
				email = rs.getString(1);
			}
			if(count==0)
				return "NONE_RETURNED";
			else if(count==1)
				return email;
			else
				return "TOO_MANY_RETURNED";
		}
		catch (SQLException e2) {
			System.out.println("SQL error in getMyBamaFromPIDM - " + e2.getMessage());
			e2.printStackTrace();
		}
		return null;
	}

	public String getUserFirstName(String username) {
		System.out.println("User: "+username);
		String name = null;
		Statement stmt;
		try {
			stmt = connection.createStatement();
			String firstName;
			ResultSet rs = stmt.executeQuery(
					"select spriden_first_name "
							+ "from saturn.spriden,general.gobtpac "
							+ "where spriden_pidm=gobtpac_pidm "
							+ "and gobtpac_external_user = '" + username + "' " 
							+ "and spriden_change_ind is null");

			int count = 0;
			while (rs.next()) {
				++count;
				name = rs.getString(1);
			}
			if (count==1)
				firstName=name;
			else
				firstName="Guest";
			System.out.println("Name found.. "+firstName);
			return firstName;
		}
		catch (SQLException e2) {
			System.out.println("Too many people were returned - " + name);
			e2.printStackTrace();
			return "Guest";
		}
	}

	public ArrayList<String> convert(String pidm,String cwid,String name,String email,String myBama) {
		if (!pidm.equals("")&&cwid.equals("")&&name.equals("")&&email.equals("")&&myBama.equals("")) {
			cwid = getCWIDFromPIDM(pidm);
			name = getNameFromPIDM(pidm);
			email = getEmailFromPIDM(pidm);
			myBama = getMyBamaFromPIDM(pidm);
		}
		else if(pidm.equals("")&&!cwid.equals("")&&name.equals("")&&email.equals("")&&myBama.equals("")) {
			pidm = getPIDMFromCWID(cwid);
			name = getNameFromPIDM(pidm);
			email = getEmailFromPIDM(pidm);
			myBama = getMyBamaFromPIDM(pidm);
		}
		else if(pidm.equals("")&&cwid.equals("")&&name.equals("")&&!email.equals("")&&myBama.equals("")) {
			pidm = getPIDMFromEmail(email);
			cwid = getCWIDFromPIDM(pidm);
			name = getNameFromPIDM(pidm);
			myBama = getMyBamaFromPIDM(pidm);
		}
		else if(pidm.equals("")&&cwid.equals("")&&name.equals("")&&email.equals("")&&!myBama.equals("")) {
			pidm = getPIDMFromMyBama(myBama);
			cwid = getCWIDFromPIDM(pidm);
			name = getNameFromPIDM(pidm);
			email = getEmailFromPIDM(pidm);
		}
		else if(pidm.equals("")&&cwid.equals("")&&!name.equals("")&&email.equals("")&&myBama.equals("")) {
			pidm = getPIDMFromName(name);
			cwid = getCWIDFromPIDM(pidm);
			email = getEmailFromPIDM(pidm);
			myBama = getMyBamaFromPIDM(pidm);
		}
		else  {
			pidm="";
			cwid = "";
			name = "";
			email = "";
			myBama = "";
		}

		ArrayList<String> values = new ArrayList<String>();
		values.add(pidm);
		values.add(cwid);
		values.add(name);
		values.add(email);
		values.add(myBama);
		return values;
	}

	
	boolean connected() {
		try {
            return connection != null && !connection.isClosed();
        }
		catch (SQLException e2) {
			return false;
		}
	}
	void jdbcConnect() {
		String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";
		try {
			Class.forName(JDBC_DRIVER);
		}
		catch (ClassNotFoundException e2) {
			System.out.println("Failed to register ODBC Driver");
		}
		System.out.println("Connecting JDBC to "+getDBHost(environment));
		try {
			connection = DriverManager.getConnection(getDBHost(environment), username, password);
			setUsersFirstName();
			System.out.println("JDBC Successful: " + getDBHost(environment));
		}
		catch (SQLException e3) {
			System.err.println("Failed connection to DB - "+e3.getMessage());
		}
	}
	private void setUsersFirstName() {

		String name = null;
		Statement stmt;
		try {
			stmt = connection.createStatement();

			rs = stmt.executeQuery(
					"select spriden_first_name "
							+ "from saturn.spriden,general.gobtpac "
							+ "where spriden_pidm=gobtpac_pidm "
							+ "and gobtpac_external_user = '" + username + "' " 
							+ "and spriden_change_ind is null");
			int count = 0;
			while (rs.next()) {
				++count;
				name = rs.getString(1);
			}
			if (count==1)
				userFirstName=name;
			else
				userFirstName="";
			new public_package.ASCIIArtGenerator("Welcome", public_package.ASCIIArtGenerator.ART_SIZE_SMALL, public_package.ASCIIArtGenerator.ASCIIArtFont.ART_FONT_MONO,"#");
			new public_package.ASCIIArtGenerator(userFirstName, public_package.ASCIIArtGenerator.ART_SIZE_SMALL, public_package.ASCIIArtGenerator.ASCIIArtFont.ART_FONT_MONO,"#");
		}
		catch (SQLException e2) {
			System.out.println("Too many people were returned - " + name);
			e2.printStackTrace();
		}
	}

	private String getDBHost(String mode) {
		if(mode.equals("SEVL"))
			return "jdbc:oracle:thin:@//bnrtdb-1.ua.edu:1521/SEVL.ua.edu";
		else if (mode.equals("TEST"))
			return "jdbc:oracle:thin:@//bnrtdb-1.ua.edu:1521/TEST.ua.edu";
		else
			return "jdbc:oracle:thin:@//bnrpdb-1.ua.edu:1521/PROD.ua.edu";
	}

}
