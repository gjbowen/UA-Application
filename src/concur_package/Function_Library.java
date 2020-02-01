package concur_package;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.jcraft.jsch.Session;
import com.sshtools.sftp.SftpClient;

import java.text.ParseException;

class Function_Library {
	protected JDBC_Connection jdbc;
	protected SFTP_Connection sftp;
	protected SSH_Connection ssh;
	protected String environment;
	protected String userName;
	protected String firstName;
	protected String password;

	Function_Library(
			Connection conn_jdbc,
			SftpClient conn_sftp,
			Session conn_ssh,
			String user,
			String pass,
			String mode) {
		sftp= new SFTP_Connection(conn_sftp, user, pass, mode);
		ssh = new SSH_Connection(conn_ssh,mode, user, pass);
		jdbc= new JDBC_Connection(conn_jdbc,mode,user,pass);

		firstName = jdbc.getUserFirstName(user);
		environment = mode;
		userName = user;
		password = pass;
	}

	String startFontSize(int size){
		return "<font size=\""+size+"\">";
	}
	String endFontSize(){
		return "</font>";
	}

	protected String findAllDeleted() {
		System.out.println("Initiating Find All Deleted search...");
		// people.add("123");
		List<String> people = new ArrayList<String>();
		List<String> removedPeople = new ArrayList<String>();
		String line = "";
		int count = 0;

		File[] files = new File(System.getProperty("user.home") + "//Concur_Files//" + environment + "//Employee_Files").listFiles();
		// If this pathname does not denote a directory, then listFiles() returns null.
		File file = null;
		String[] parsedLine = null;
		StringBuilder retStr = new StringBuilder();
		for (int i = 0; i < files.length; i++) {
			file = files[i];
			try (BufferedReader br = new BufferedReader(new FileReader(file))) {
				while ((line = br.readLine()) != null) {
					if (line.equals("100,0,SSO,UPDATE,EN,N,N"))
						continue;
					parsedLine = line.split(",");
					if (parsedLine.length > 15) {
						if (people.contains(parsedLine[4])) {
							// it contains cwid
							// reactivated
							removedPeople.remove(parsedLine[4]);
							if (parsedLine[14].equals("N") && jdbc.cwidTracked(parsedLine[4])) {
								++count;
								removedPeople.add(parsedLine[4]);
								retStr.append("Deleted on " + getDateFromFilename(file.getName()) + " - "
										+ parsedLine[4] + "   " + parsedLine[3] + ", " + parsedLine[1] + " "
										+ parsedLine[2] + "\n");
							}
						}
						else
							// it does NOT contain CWID, add it.
							if (parsedLine[14].equals("Y"))
								people.add(parsedLine[4]);
					}
				}
			} catch (IOException e) {
				System.out.println("File not found");
			}
		}
		for (int i = 0; i < removedPeople.size(); ++i)
			if (jdbc.cwidTracked(removedPeople.get(i)))
				// reactivated
				System.out.println(removedPeople.get(i) + " - " + jdbc.getNameFromTrackingTable(removedPeople.get(i)));

		System.out.println("Done with Find All Deleted search.");
		if (count == 0)
			return "No Changed Deletions found.";
		return retStr.toString();
	}

	protected Set<String> parsePeople(String people) {
		people = people.trim();
		String[] parsedPeople = null;
		HashMap<String, Object> map = new HashMap<String, Object>();
		people = people.replace("\r\n",",");
		people = people.replace("\n",",");
		people = people.replace(" ",",");
		people = people.replace(" ",",");
		people = people.replace("\t",",");
		people = people.replace(";",",");

		people = people.replace(",,",",");
		people = people.replace(",,",",");

		parsedPeople = people.split(",");
		for (int i = 0; i < parsedPeople.length; ++i)
			if (!parsedPeople[i].trim().equals("") && !map.containsKey(parsedPeople[i].trim()))
				map.put(parsedPeople[i].trim(), null);

		Set<String> set = map.keySet();
		return set;
	}

	protected String findChangedLogin() {
		System.out.println("Initiating Changed Login search...");
		Map<String, String> map = new HashMap<String, String>();

		String line = "";
		int count = 0;

		File[] files = new File(System.getProperty("user.home") + "//Concur_Files//" + environment + "//Employee_Files").listFiles();
		// If this pathname does not denote a directory, then listFiles() returns null.
		File file = null;
		String[] parsedLine = null;
		StringBuilder retStr = new StringBuilder();
		for (int i = 0; i < files.length; i++) {
			file = files[i];
			try (BufferedReader br = new BufferedReader(new FileReader(file))) {
				while ((line = br.readLine()) != null) {
					parsedLine = line.split(",");

					if (map.containsKey(parsedLine[4])) {// it contains cwid
						if (!map.get(parsedLine[4]).equals(parsedLine[5])) {// there has been a change in ID..

							++count;
							retStr.append(getDateFromFilename(file.getName()) + " - " + parsedLine[4] + "   " // cwid
									+ parsedLine[3] + ", " + parsedLine[1] + " " + parsedLine[2] // name
									+ " -  Old: " + map.get(parsedLine[4]) // old ID
									+ " -  New: " + parsedLine[5] + "\n"); // new ID
							map.put(parsedLine[4], parsedLine[5]);
						}
					}
					else{// it does NOT contain cwid
						map.put(parsedLine[4], parsedLine[5]);

					}
				}
			} catch (IOException e) {
				System.out.println("File not found");
			}
		}
		System.out.println("Done with Changed Login search.");
		if (count == 0)
			return "No Changed Login IDs found.";
		return retStr.toString();
	}

	protected void findEmployeeCustom() { //never used
		String line = "";
		int count = 0;
		File[] files = new File(System.getProperty("user.home") + "//Concur_Files//" + environment + "//Employee_Files").listFiles();
		File file = null;
		String[] parsedLine = null;
		for (int i = 0; i < files.length; ++i) {
			file = files[i];
			//if(!file.getName().contains("2019"))
			//	continue;
			try {
				BufferedReader br = new BufferedReader(new FileReader(file));
				while ((line = br.readLine()) != null) {
					parsedLine = line.split(",");
					if (parsedLine.length < 10)
						continue;

					String orgn305 = parsedLine[17];
					String orgn350 = get305ORGN(parsedLine[4], file.getName().replace("305", "350"));

					if(!orgn305.trim().equals(orgn350.split(" ")[0].trim())) {
						if(orgn305.equals(""))
							orgn305="<null>";
						else
							orgn305="\""+orgn305 +"\"";
						System.out.println(file.getName() + "  "+parsedLine[4]+"  -   305: " +  orgn305 + "    350: " + "\"" + orgn350.split(" ")[0] + "\"");
					}

				}
				br.close();
			} catch (IOException e2){}
		}
		System.out.println("Done with Employee search" );
	}

	protected String findEmployee(String cwid,int col) {
		String line = "";
		int count = 0;
		File[] files = new File(System.getProperty("user.home") + "//Concur_Files//" + environment + "//Employee_Files").listFiles();
		File file = null;
		String[] parsedLine = null;
		StringBuilder retStr = new StringBuilder();
		cwid = cwid.trim();
		--col;
		for (int i = 0; i < files.length; ++i) {
			file = files[i];
			try {
				BufferedReader br = new BufferedReader(new FileReader(file));
				while ((line = br.readLine()) != null) {
					parsedLine = line.split(",");
					if (parsedLine.length < 10)
						continue;
					else if (!parsedLine[col].contains(cwid))
						continue;
					++count;

					String orgn305 = parsedLine[17];
					String orgn350 = get305ORGN(parsedLine[4], file.getName().replace("305", "350"));
					//System.out.println("Org is: "+orgn350+"\twith "+file.getName().replace("305", "350"));
					String foap = parsedLine[15]+"-"+parsedLine[16]+"-"+parsedLine[17]+"-"+parsedLine[18];
					foap=foap.replace("---","N/A");
					retStr.append(startFontSize(5)+createLink( getDateFromFilename(file.getName()),file.getPath()) + " - " + parsedLine[3] + ", "
							+ parsedLine[1] + " " + parsedLine[2] +
							"  - Email: " + parsedLine[5] +
							"  - Active: " + parsedLine[14] +
							"  - Orgn: "+ orgn350 +
							"  - FOAP: "+foap +
							"  - Approver: " + parsedLine[63] +
							"  - Approver's CWID: " + parsedLine[58] +endTab()+ endFontSize()+"<br>");
					if(!orgn305.trim().equals(orgn350.split(" ")[0].trim())) {
						if(orgn305.equals(""))
							orgn305="NULL";
						else
							orgn305="\""+orgn305 +"\"";
						retStr.append(startFontSize(3)+"ORGN MISMATCH FOUND   -   305: " +  orgn305 + "    350: " + "\"" + orgn350.split(" ")[0] + "\""+ endFontSize()+"<br>");
					}
				}
				br.close();
			} catch (IOException e2) {
				System.out.println("File not found");
			}
		}
		System.out.println("Done with Employee search for: " + cwid);
		if (count == 0)
			return "CWID " + cwid + " not found.";
		return retStr.toString();
	}
	String createLink(String tag,String dest){
		return "<a href=\""+dest+"\">"+tag+"</a>";
	}
	String startTab(){
		return "<span style=\"margin-left:4em\">";
	}
	String endTab(){
		return "</span>";
	}


	String toApproverRecord(String[] pLine){
		pLine[58]="";
		pLine[60]="";
		pLine[61]="";
		StringBuilder str = new StringBuilder();
		for(int i=0;i<pLine.length;++i) {
			str.append(pLine[i]);
			str.append(",");
		}
		str.deleteCharAt(str.length()-1);
		return str.toString();
	}
	protected String findBatch305(String cwid) {
		String line = "";
		int count = 0;
		Set<String> people = parsePeople(cwid);
		File[] files = new File(System.getProperty("user.home") + "//Concur_Files//" + environment + "//Employee_Files").listFiles();
		File file = null;
		String[] parsedLine = null;
		StringBuilder retStr = new StringBuilder();

		HashMap<String,String> approvers = new HashMap<String,String>();
		HashMap<String,String> nonApprovers = new HashMap<String,String>();

		for (int i = 0; i < files.length; ++i) {
			file = files[i];

			try {
				BufferedReader br = new BufferedReader(new FileReader(file));
				while ((line = br.readLine()) != null) {
					parsedLine = line.split(",");
					if (parsedLine.length <= 65 || !people.contains(parsedLine[4].trim())) {
						continue;
					}
					++count;

					if(parsedLine[63].equals("Y")){
						String apprRec = toApproverRecord(parsedLine);
						if(approvers.containsKey(parsedLine[4])) // they exist in the dictionary
							approvers.replace(parsedLine[4],apprRec); // update them
						else
							approvers.put(parsedLine[4],apprRec); // add them
					}

					if (nonApprovers.containsKey(parsedLine[4]))
						nonApprovers.replace(parsedLine[4], line);
					else
						nonApprovers.put(parsedLine[4], line);

					retStr.append(getDateFromFilename(file.getName()) + "  -  " + parsedLine[4] + "  -  "
							+ parsedLine[3] + ", " + parsedLine[1] + " " + parsedLine[2] + "  -  Email: "
							+ parsedLine[5] + "  -  Active: " + parsedLine[14] + "  -  Orgn: "
							+ get305ORGN(parsedLine[4], file.getName().replace("305", "350")) + "  -  Approver: "
							+ parsedLine[63] + "  -  Approver's CWID: " + parsedLine[58] + "\n");
				}
				br.close();

			} catch (IOException e2) {
				System.out.println("File not found");
			}
		}
		ArrayList<String> results = new ArrayList<String>();
		approvers.forEach((k, v) -> {
			results.add(v);
		});
		nonApprovers.forEach((k, v) -> {
			results.add(v);
		});
		if (count == 0)
			return "CWID " + cwid + " not found.";
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
		LocalDateTime now = LocalDateTime.now();

		writeListToFile("concur_employee_feed_305_"+dtf.format(now)+"_fix.txt","100,0,SSO,UPDATE,EN,N,N",results);
		System.out.println("Done with 305 search.");

		return retStr.toString();
	}

	protected String findBatch350(String cwid) {
		String line = "";
		int count = 0;
		Set<String> people = parsePeople(cwid);
		File[] files = new File(System.getProperty("user.home") + "//Concur_Files//" + environment + "//350_Files").listFiles();
		File file = null;
		String[] parsedLine = null;
		StringBuilder retStr = new StringBuilder();

		HashMap<String,String> found = new HashMap<String,String>();

		for (int i = 0; i < files.length; ++i) {
			file = files[i];

			try {
				BufferedReader br = new BufferedReader(new FileReader(file));
				while ((line = br.readLine()) != null) {
					parsedLine = line.split(",");


					if (parsedLine.length <= 10 || !people.contains(parsedLine[1].trim())) {
						continue;
					}
					++count;

					if (found.containsKey(parsedLine[1])) {
						found.replace(parsedLine[1], line);
					} else {
						found.put(parsedLine[1], line);
					}

					retStr.append(getDateFromFilename(file.getName()) + "  -  " + parsedLine[1] + "  -  "
							+ parsedLine[17]+ "  -  "
							+ parsedLine[19]+"\n");
				}
				br.close();

			} catch (IOException e2) {
				System.out.println("File not found");
			}
		}
		ArrayList<String> results = new ArrayList<String>();

		found.forEach((k, v) -> {
			results.add(v);
		});
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
		LocalDateTime now = LocalDateTime.now();

		writeListToFile("concur_employee_feed_350_"+dtf.format(now)+"_fix.txt","100,0,SSO,UPDATE,EN,N,N",results);

		if (count == 0)
			return "CWID " + cwid + " not found.";
		System.out.println("Done with 350 search.");

		return retStr.toString();
	}

	protected String get305ORGN(String cwid, String fileName) {
		String line = "";
		File[] files = new File(System.getProperty("user.home") + "//Concur_Files//" + environment + "//350_Files").listFiles();
		File file = null;
		String[] parsedLine = null;
		StringBuilder retStr = new StringBuilder();
		int count = 0;
		for (int i = 0; i < files.length; ++i)
			if (files[i].getName().equals(fileName)) {
				file = files[i];
				try {
					BufferedReader br = new BufferedReader(new FileReader(file));
					while ((line = br.readLine()) != null) {
						parsedLine = line.split(",");
						if (line.equals("100,0,SSO,UPDATE,EN,N,N") || !parsedLine[1].equals(cwid))
							continue;
						++count;
						retStr.append(parsedLine[19]);
					}
					br.close();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
			}
		if (count==0)
			return "N/A";
		return retStr.toString();
	}

	protected boolean validYear(String str) {
		int x;
		try {
			x = Integer.parseInt(str);
			if(x >= 1980 && x <= 2099) {
				return true;
			}
		} catch (NumberFormatException e2) {}
		return false;
	}

	protected String getDateFromFilename(String str) {
		String year = null;
		String month = null;
		String day = null;
		for (int i = 0; i < str.length() - 4; ++i) {
			if (validYear(str.substring(i, i + 4))) {
				year = str.substring(i, i + 4);
				month = str.substring(i + 4, i + 6);
				day = str.substring(i + 6, i + 8);
				return month + "/" + day + "/" + year;
			}
		}
		return "";
	}

	protected String getLatestPRAE() {
		String path = System.getProperty("user.home") + "//Concur_Files//" + environment + "//PRAE_Files";
		Integer max = 0;
		String maxFile = "";
		String fileName;
		File[] files = new File(path).listFiles();
		for (int i = 0; i < files.length; ++i) {
			fileName = files[i].getName();
			try {
				if (Integer.parseInt(fileName.substring(5, 15)) > max) {
					max = Integer.parseInt(fileName.substring(5, 15));
					maxFile = fileName;
				}
			} catch (NumberFormatException e) {
			}

		}
		return maxFile;
	}

	protected String getLatestSAE() {
		String path = System.getProperty("user.home") + "//Concur_Files//" + environment + "//SAE_Files";
		Integer max = 0;
		String maxFile = "";
		String fileName;
		File[] files = new File(path).listFiles();

		for (int i = 0; i < files.length; ++i) {
			fileName = files[i].getName();
			try {
				if (Integer.parseInt(fileName.substring(4, 14)) > max) {
					max = Integer.parseInt(fileName.substring(4, 14));
					maxFile = fileName;
				}
			} catch (NumberFormatException e) {
			}

		}
		return maxFile;
	}

	protected String getLatestSRE() {
		String path = System.getProperty("user.home") + "//Concur_Files//" + environment + "//SRE_Files";
		Integer max = 0;
		String maxFile = "";
		String fileName;
		File[] files = new File(path).listFiles();

		for (int i = 0; i < files.length; ++i) {
			fileName = files[i].getName();
			try {
				if (Integer.parseInt(fileName.substring(4, 14)) > max) {
					max = Integer.parseInt(fileName.substring(4, 14));
					maxFile = fileName;
				}
			} catch (NumberFormatException e) {
			}

		}
		return maxFile;
	}
	protected String getLatestActiveDept() {
		String path = System.getProperty("user.home") + "//Concur_Files//" + environment + "//Active_Departments";
		Integer max = 0;
		String maxFile = "";
		String fileName;
		File[] files = new File(path).listFiles();

		for (int i = 0; i < files.length; ++i) {
			fileName = files[i].getName();
			try {
				if (Integer.parseInt(fileName.substring(4, 14)) > max) {
					max = Integer.parseInt(fileName.substring(4, 14));
					maxFile = fileName;
				}
			} catch (NumberFormatException e) {
			}

		}
		return maxFile;
	}

	protected boolean isCWID(String cwid) {
		if ((cwid = cwid.trim()).length() != 8)
			return false;
		try {
			return Integer.parseInt(cwid) >= 0;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}

	protected boolean isPcard(ArrayList<String> parsedLine) {
		if (parsedLine.get(125).toUpperCase().equals("CBCP"))
			return !parsedLine.get(17).equals("") && Float.valueOf(parsedLine.get(17)) != 0;
		return false;
	}

	protected boolean isExpense(ArrayList<String> parsedLine) {
		if (parsedLine.get(125).toUpperCase().equals("CASH"))
			//if (!parsedLine.get(184).equals(""))
			return !parsedLine.get(17).equals("") && Float.valueOf(parsedLine.get(17)) != 0;
		return false;
	}

	protected ArrayList<String> findLatestVendorInfo(String cwid,String rt) {
		System.out.println("Searching for Vendor: "+cwid+"("+rt+")...");
		String line = "";
		File[] files = new File(System.getProperty("user.home") + "//Concur_Files//" + environment + "//VENDOR_FEED_Files").listFiles();
		File file = null;
		ArrayList<String> parsedLine = null;
		ArrayList<String> latestSent = null;
		Integer lineNumber;
		for (int i = files.length-1; i > 0; --i) {
			file = files[i];
			if (file.getName().endsWith(".txt") && file.getName().contains("concur_vendor_feed")) {
				try {
					BufferedReader br = new BufferedReader(new FileReader(file));
					lineNumber = 0;
					while ((line = br.readLine()) != null) {
						++lineNumber;
						parsedLine = customSplitSpecific(line, ',');
						// System.out.println(file.getName());
						if (parsedLine.size() < 40 || !parsedLine.get(1).equals(cwid) || !parsedLine.get(15).equals(rt))
							continue;
						return parsedLine;

					}
					br.close();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
			}
		}
		if (latestSent == null) {
			System.out.println("Vendor not found - " + cwid);
		}
		return latestSent;
	}

	/*
	 * given a file with cwid and rt address, find all instances of them in sent
	 * files and output the file.
	 */
	protected String retireVendorTerms(String fileName) {
		System.out.println("Initiating vendor termination..");
		File file = new File(fileName);
		String[] parsedLine = null;
		String line = "";
		String id = "";
		String rt = "";
		ArrayList<String> arrayOfLines = new ArrayList<String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			while ((line = br.readLine()) != null) {
				StringBuilder vendor=new StringBuilder();
				parsedLine = line.split(",");
				if (parsedLine.length <= 1) {
					continue;
				}
				id = parsedLine[0].trim();
				rt = parsedLine[1].trim();
				System.out.print(arrayOfLines.size()+") ");
				ArrayList<String> vend = findLatestVendorInfo(id,rt);
				//				vend.set(28, "Y");
				for(int i=0;i<vend.size();++i)
					if(i==vend.size()-1)
						vendor.append(vend.get(i));
					else
						vendor.append(vend.get(i)+",");
				arrayOfLines.add(vendor.toString());
			}
			br.close();
		} catch (IOException e2) {

			System.out.println("File not found "+e2.getMessage());
			return "ERROR:\n" + pwd()+"\\"+fileName+" not found.";
		}
		writeListToFile("Vendors_Terminated.txt", "100,0,US,CHECK", arrayOfLines);
		System.out.println("Terminated Vendors");
		return System.getProperty("user.home") + "\\Concur_Files\\" + environment + "\\Generated_Files\\Vendors_Terminated.txt";
	}

	protected String getPwd() {
		return System.getProperty("user.dir");
	}
	protected String pwd() {
		return getPwd();
	}
	protected boolean validItem(String object, ArrayList<ArrayList<String>> list, String item1, String item2,
								Date date) {
		for (int i = 0; i < list.size(); ++i) {
			if (list.get(i).get(0).equals(item1) && list.get(i).get(1).equals(item2)) {
				// FUND
				if (object.equals("FUND"))
					if (dateLessThanOrEqual(list.get(i).get(2), date)) // effective date
						if ((!dateLessThanOrEqual(list.get(i).get(3), date))) // end change date
							if (list.get(i).get(4).equals("") || !dateLessThanOrEqual(list.get(i).get(4), date)) // term
								// date
								return true;
				// ORGN
				if (object.equals("ORGN"))
					if (dateLessThanOrEqual(list.get(i).get(2), date)) // effective date
						if (list.get(i).get(3).equals("") || !dateLessThanOrEqual(list.get(i).get(3), date)) // end
							// change
							// date
							if (list.get(i).get(4).equals("") || !dateLessThanOrEqual(list.get(i).get(4), date)) // term
								// date
								return true;
				// PROG
				if (object.equals("PROG"))
					if (dateLessThanOrEqual(list.get(i).get(2), date)) // effective date
						if (list.get(i).get(3).equals("") || !dateLessThanOrEqual(list.get(i).get(3), date)) // end
							// change
							// date
							if (list.get(i).get(4).equals("") || !dateLessThanOrEqual(list.get(i).get(4), date)) // term
								// date
								return true;
				// ACTIVITY
				if (object.equals("ACTIVITY"))
					if (dateLessThanOrEqual(list.get(i).get(2), date)) // effective date
						if (list.get(i).get(3).equals("") || !dateLessThanOrEqual(list.get(i).get(3), date)) // end
							// change
							// date
							if (list.get(i).get(4).equals("") || !dateLessThanOrEqual(list.get(i).get(4), date)) // term
								// date
								return true;
				// ACCOUNT
				if (object.equals("ACCOUNT"))
					if (dateLessThanOrEqual(list.get(i).get(2), date)) // effective date
						if (!dateLessThanOrEqual(list.get(i).get(3), date)) // end change date
							if (list.get(i).get(4).equals("") || !dateLessThanOrEqual(list.get(i).get(4), date)) // term
								// date
								return true;
			}
		}
		return false;
	}

	protected boolean dateLessThanOrEqual(String tableDate, Date concurDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date1 = null;
		try {
			date1 = sdf.parse(tableDate);

			if (date1.before(concurDate))
				return true;
			else if (date1.after(concurDate))
				return false;
			else if (date1.equals(concurDate))
				return true;
			else {
				System.out.println("SHOULDN'T HAVE GOTTEN HERE.");
				return false;
			}
		} catch (ParseException e) {
			System.out.println("ERROR!! " + tableDate);
			System.out.println("ERROR!! " + "--" + sdf.format(concurDate));
			return false;
		}
	}

	protected void generateSAE(String columns, boolean validateItems,String transfer) {
		System.out.println("Initiating SAE rewrite...");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date1 = null;

		ArrayList<ArrayList<String>> allFunds = null;
		Set<String> invalidFunds = new HashSet<String>();

		ArrayList<ArrayList<String>> allOrgns = null;
		Set<String> invalidOrgns = new HashSet<String>();

		ArrayList<ArrayList<String>> allProgs = null;
		Set<String> invalidProgs = new HashSet<String>();

		ArrayList<ArrayList<String>> allActivity = null;
		Set<String> invalidActivity = new HashSet<String>();

		ArrayList<ArrayList<String>> allAccounts = null;
		Set<String> invalidAccounts = new HashSet<String>();

		if (validateItems) {
			System.out.println("---------------------------Getting FOAPALs---------------------------");
			allFunds = jdbc.getFunds();
			allOrgns = jdbc.getOrgns();
			allProgs = jdbc.getProgs();
			allActivity = jdbc.getActivity();
			allAccounts = jdbc.getAccounts();
			System.out.println("---------------------------------------------------------------------\n");
		}

		String line = "";
		String content = "";

		File[] files = new File(System.getProperty("user.home") + "//Concur_Files//" + environment + "//SAE_Files").listFiles();
		File file = null;
		ArrayList<String> parsedLine = null;
		String[] parsedIndexes = null;
		parsedIndexes = columns.split(",");
		float pcard = 0;
		float expense = 0;
		ArrayList<String> arrayOfLines = new ArrayList<String>();

		String coas = null;
		String fund = null;
		String orgn = null;
		String prog = null;
		String activity = null;
		String[] accountCodeParsed = null;
		String accountCode = null;
		String date = null;
		boolean validItem = true;
		int invalidCount = 0;
		int lineNumber = 0;
		String lineErrors = null;
		System.out.println("---------------Initiating File Read and Validations------------------");
		for (int i = 0; i < files.length; ++i) {
			file = files[i];
			if (file.getName().endsWith(".txt") && file.getName().startsWith("sae"))
				try {
					if(!file.getName().contains("2019"))
						continue;
					BufferedReader br = new BufferedReader(new FileReader(file));
					lineNumber = 0;

					while ((line = br.readLine()) != null) {
						++lineNumber;
						lineErrors = "";
						parsedLine = customSplitSpecific(line.replace(",", "").replace("\"", "").replace("|", ","),
								',');
						if (parsedLine.get(0).equals("EXTRACT") || parsedLine.size() < 2)
							continue;
						else if (parsedLine.size() != 399) {
							System.err.println("ERROR! Incorrect Length - Skipping line");
							System.err.println("File Name: " +
									file.getName() +
									" - Line: " + lineNumber +
									" - Length: " + parsedLine.size());
							continue;
						}
						validItem = true;

						if (isPcard(parsedLine)) {
							coas = parsedLine.get(191 - 1);
							fund = parsedLine.get(192 - 1);
							orgn = parsedLine.get(193 - 1);
							prog = parsedLine.get(194 - 1);
							activity = parsedLine.get(195 - 1);
							accountCode = parsedLine.get(63 - 1);
							accountCodeParsed = accountCode.split(" ");
							accountCode = accountCodeParsed[accountCodeParsed.length - 1];

							if (validateItems) {
								try {
									date = parsedLine.get(64 - 1);
									date1 = sdf.parse(date);
								} catch (ParseException e2) {
									System.out.println("Parse error - " + date);
								}
								if (!validItem("FUND", allFunds, coas, fund, date1)) {
									invalidFunds.add(fund);
									validItem = false;
									lineErrors = lineErrors + "FUND    - " + coas + "," + fund + "," + date + "\t";
								}

								if (!validItem("ORGN", allOrgns, coas, orgn, date1)) {
									invalidOrgns.add(orgn);
									validItem = false;
									lineErrors = lineErrors + "ORGN    - " + coas + "," + orgn + "," + date + "\t";
								}
								if (!activity.equals(""))
									if (!validItem("ACTIVITY", allActivity, coas, activity, date1)) {
										invalidActivity.add(activity);
										// validItem=false;
									}
								if (!validItem("PROG", allProgs, coas, prog, date1)) {
									invalidProgs.add(prog);
									validItem = false;
									lineErrors = lineErrors + "PROG    - " + coas + "," + prog + "," + date + "\t";
								}
								if (!validItem("ACCOUNT", allAccounts, coas, accountCode, date1)) {
									invalidAccounts.add(accountCode);
									validItem = false;
									lineErrors = lineErrors + "ACCOUNT - " + coas + "," + accountCode + "," + date
											+ "\t";
								}

								if (!validItem) {
									++invalidCount;
									System.out.println(parsedLine.get(125) + "\t" + parsedLine.get(17) + "\t"
											+ file.getName() + "\t" + lineNumber + "\t" + lineErrors);
								}
							}
							++pcard;
							columns = "5,18,20,63,71,191,192,193,194,195,6,7,8,27,33,41,42,43,324,325,31,63,71,250,314,334,3";//temp
						} else {
							++expense;
							continue;
							//columns = "20,31,63,70,250,313,334"; //temp
							// continue;
						}
						parsedIndexes = columns.split(","); // temp
						content = "";

						if (columns.equals("*"))
							content = line;
						else {
							for (int j = 0; j < parsedIndexes.length; ++j) {
								if (content.equals("")) {
									content = parsedLine.get(Integer.parseInt(parsedIndexes[j]) - 1);
								}
								else {
									content = content + "," + parsedLine.get(Integer.parseInt(parsedIndexes[j]) - 1);
								}
							}
						}
						if (validItem)
							arrayOfLines.add(content);
					}
					br.close();
				} catch (IOException e) {
					System.out.println("File not found");
				}
		}
		String header = "";
		if (columns.equals("*"))
			header = "ALL_COLUMNS";
		else
			for (int i = 0; i < parsedIndexes.length; ++i) {
				if (header.equals(""))
					header = "COL_" + parsedIndexes[i];
				else
					header = header + ",COL_" + parsedIndexes[i];
			}

		writeListToFile("SAE_GENERATED.txt", header, arrayOfLines);
		if(transfer.contains("SEVL")) {
			sftp.moveFile("SEVL","Generated_Files","SAE_GENERATED.txt","SAE_GENERATED.txt");
		}
		if(transfer.contains("TEST")) {
			sftp.moveFile("TEST","Generated_Files","SAE_GENERATED.txt","SAE_GENERATED.txt");
		}
		if(transfer.contains("PROD")) {
			sftp.moveFile("PROD","Generated_Files","SAE_GENERATED.txt","SAE_GENERATED.txt");
		}

		if(validateItems) {
			System.out.println("---------------------------------------------------------------------\n");
			System.out.println("----------------------------STATISTICS------------------------------");

			float x = pcard / (pcard + expense) * 100;
			System.out.println("Invalid PCARDS: " + invalidCount + "/" + pcard);
			System.out.println("PCARDS: " + pcard + "\t" + x + "%");
			float y = expense / (pcard + expense) * 100;
			System.out.println("EXPENSE: " + expense + "\t" + y + "%");
			System.out.println("TOTAL: " + (pcard + expense));
			System.out.println("Invalid Funds: " + invalidFunds.size());
			System.out.println("Invalid Orgns: " + invalidOrgns.size());
			System.out.println("Invalid Progs: " + invalidProgs.size());
			System.out.println("Invalid Accounts: " + invalidAccounts.size());
			System.out.println("Invalid Activity: " + invalidActivity.size());
		}
		System.out.println("--------------------------Program Complete---------------------------");
		//System.exit(0);
	}

	protected void rewritePRAE(String columns) {
		System.out.println("Initiating PRAE rewrite...");
		String line = "";
		String content = "";
		File[] files = new File(System.getProperty("user.home") + "//Concur_Files//" + environment + "//PRAE_Files").listFiles();
		File file = null;
		ArrayList<String> parsedLine = null;
		String[] parsedIndexes = null;
		parsedIndexes = columns.split(",");
		ArrayList<String> arrayOfLines = new ArrayList<String>();
		for (int i = 0; i < files.length; ++i) {
			file = files[i];
			if (file.getName().endsWith(".txt") && file.getName().startsWith("prae")) {
				try {
					BufferedReader br = new BufferedReader(new FileReader(file));
					while ((line = br.readLine()) != null) {
						parsedLine = customSplitSpecific(line.replace(",", "").replace("\"", "").replace("|", ","),
								',');
						if (parsedLine.get(0).equals("EXTRACT") || parsedLine.size() < 2)
							continue;

						content = "";
						if (parsedLine.size() != 255) {
							System.out.println(
									"ERROR!!!   File Name: " + file.getName() + "Length: " + parsedLine.size());
						}

						if (columns.equals("*")) {
							content = line;
						} else {
							for (int j = 0; j < parsedIndexes.length; ++j) {
								if (content.equals("")) {
									content = parsedLine.get(Integer.parseInt(parsedIndexes[j]) - 1);
								} else {
									content = content + "," + parsedLine.get(Integer.parseInt(parsedIndexes[j]) - 1);
								}
							}
						}
						arrayOfLines.add(content);
					}
					br.close();
				} catch (IOException e2) {
					System.out.println("File not found");
				}
			}
		}
		String header = "";
		if (columns.equals("*")) {
			header = "ALL_COLUMNS";
		} else {
			for (int i = 0; i < parsedIndexes.length; ++i) {
				if (header.equals("")) {
					header = "COL_" + parsedIndexes[i];
				} else {
					header = header + ",COL_" + parsedIndexes[i];
				}
			}
		}
		writeListToFile("PRAE_GENERATED.txt", header, arrayOfLines);
		System.out.println("DONE");
	}

	protected String searchPRAE(String str, int column, String contains_or_equals) {
		System.out.println("Initiating PRAE Report Key search...");
		String line = "";
		int count = 0;
		File[] files = new File(System.getProperty("user.home") + "//Concur_Files//" + environment + "//PRAE_Files").listFiles();
		File file = null;
		String[] parsedLine = null;
		StringBuilder retStr = new StringBuilder();
		retStr.append("Search column " + column + " to see if it " + contains_or_equals + " " + str + ":\n");
		--column;

		for (int i2 = 0; i2 < files.length; ++i2) {
			file = files[i2];
			if (file.getName().endsWith(".txt") && file.getName().startsWith("prae")) {
				try {
					BufferedReader br = new BufferedReader(new FileReader(file));
					int fileLine = 0;
					while ((line = br.readLine()) != null) {
						String transAmount;
						++fileLine;
						parsedLine = line.replace("|", "\",\"").split("\",\"");
						if (parsedLine[0].equals("EXTRACT") || parsedLine.length < 2) {
							continue;
						}
						if (parsedLine[61].length() < 6 || parsedLine[61].equals("")) {
							transAmount = "0.00";
						} else {
							String front = parsedLine[61].substring(0, parsedLine[61].indexOf("."));
							String back = parsedLine[61].substring(parsedLine[61].indexOf(".") + 1,
									parsedLine[61].indexOf(".") + 3);
							transAmount = front + "." + back;
						}
						if (contains_or_equals.equals("contains")
								&& parsedLine[column].toLowerCase().contains(str.toLowerCase())) {
							retStr.append("\nFile: " + file.getName() + "\n" + "Date: "
									+ getDateFromFilename(file.getName()) + "\n" + "Line: " + fileLine + "\n"
									+ "\tPerson: " + parsedLine[164] + "\n" + "\tReport Key: " + parsedLine[1] + "\n"
									+ "\tHold Code: " + parsedLine[18] + "\n" + "\tDispursement Code: " + parsedLine[19]
									+ "\n" + "\tTransaction Amount: " + transAmount + "\n" + "\tAccount Code: "
									+ parsedLine[134] + "\n");
							++count;
						}
						if (contains_or_equals.equals("startsWith") && parsedLine[column].startsWith(str)) {
							retStr.append("\nFile: " + file.getName() + "\n" + "Date: "
									+ getDateFromFilename(file.getName()) + "\n" + "Line: " + fileLine + "\n"
									+ "\tPerson: " + parsedLine[164] + "\n" + "\tReport Key: " + parsedLine[1] + "\n"
									+ "\tHold Code: " + parsedLine[18] + "\n" + "\tDispursement Code: " + parsedLine[19]
									+ "\n" + "\tTransaction Amount: " + transAmount + "\n" + "\tAccount Code: "
									+ parsedLine[134] + "\n");
							++count;
						}
						if (contains_or_equals.equals("doesNotEqual") && !parsedLine[column].equals(str)) {
							retStr.append("\nFile: " + file.getName() + "\n" + "Date: "
									+ getDateFromFilename(file.getName()) + "\n" + "Line: " + fileLine + "\n"
									+ "\tPerson: " + parsedLine[164] + "\n" + "\tReport Key: " + parsedLine[1] + "\n"
									+ "\tHold Code: " + parsedLine[18] + "\n" + "\tDispursement Code: " + parsedLine[19]
									+ "\n" + "\tTransaction Amount: " + transAmount + "\n" + "\tAccount Code: "
									+ parsedLine[134] + "\n");
							++count;
						}
						if (contains_or_equals.equals("endsWith") && parsedLine[column].endsWith(str)) {
							retStr.append("\nFile: " + file.getName() + "\n" + "Date: "
									+ getDateFromFilename(file.getName()) + "\n" + "Line: " + fileLine + "\n"
									+ "\tPerson: " + parsedLine[164] + "\n" + "\tReport Key: " + parsedLine[1] + "\n"
									+ "\tHold Code: " + parsedLine[18] + "\n" + "\tDispursement Code: " + parsedLine[19]
									+ "\n" + "\tTransaction Amount: " + transAmount + "\n" + "\tAccount Code: "
									+ parsedLine[134] + "\n");
							++count;
						}
						if (contains_or_equals.equals("equals") && parsedLine[column].equals(str)) {
							retStr.append("\nFile: " + file.getName() + "\n" + "Date: "
									+ getDateFromFilename(file.getName()) + "\n" + "Line: " + fileLine + "\n"
									+ "\tPerson: " + parsedLine[164] + "\n" + "\tReport Key: " + parsedLine[1] + "\n"
									+ "\tHold Code: " + parsedLine[18] + "\n" + "\tDispursement Code: " + parsedLine[19]
									+ "\n" + "\tTransaction Amount: " + transAmount + "\n" + "\tAccount Code: "
									+ parsedLine[134] + "\n");
							++count;
						}
					}
					br.close();
				} catch (IOException e2) {
					System.out.println("File not found");
				}
			}
		}
		System.out.println("Done with PRAE search.");
		System.out.println(column);
		if (count == 0)
			return "No results for: \nSearch column " + (column + 1) + " to see if it " + contains_or_equals + " "
					+ str;
		else
			retStr.append("\n\n\nTOTAL: " + count);

		return retStr.toString();
	}

	protected String buildSAE(ArrayList<String> parsedLine, String filePath, int line) {
		String transAmount;
		String message;

		if (parsedLine.get(17).length() < 6 || parsedLine.get(17).equals("")) {
			transAmount = "0.00";
		} else {
			String front = parsedLine.get(17).substring(0, parsedLine.get(17).indexOf("."));
			String back = parsedLine.get(17).substring(parsedLine.get(17).indexOf(".") + 1,
					parsedLine.get(17).indexOf(".") + 3);
			transAmount = front + "." + back;
		}
		String file = filePath.split("\\\\")[filePath.split("\\\\").length-1];
		message = "<br>File: " + createLink(file,filePath) + "<br>"
				+ "Date: " + getDateFromFilename(file) + "<br>"
				+ "Line: " + line + "<br>"
				+ "Person: " + parsedLine.get(4) + " - " + parsedLine.get(5) + ", " + parsedLine.get(6) + "<br>"
				+ "Report Key: " + parsedLine.get(19) + "<br>"
				+ "Description: " + parsedLine.get(26) + "<br>"
				+ parsedLine.get(41) + "<br>"
				+ parsedLine.get(42) + "<br>"
				+ parsedLine.get(62) + "<br>"
				+ parsedLine.get(68) + "<br>"
				+ parsedLine.get(70) + "<br>"
				+ parsedLine.get(145) + "<br>"
				+ "Transaction Amount: " + transAmount + "<br>"
				+ "Account Code: " + parsedLine.get(62) + "<br>";

		return message;
	}
	protected String buildSRE(ArrayList<String> parsedLine, String fileName, int line) {
		System.out.println("FOUND!!!!!!");
		if(parsedLine.get(0).equals("REQUEST DETAIL")){ //86 in length
			return "\nFile: " + fileName + "\n"
					+ "Batch Date: " + parsedLine.get(2) + "\n"
					+ "Line: " + line+ "\n"
					+ "\tPerson: " + parsedLine.get(4) + " - " + parsedLine.get(5) + ", " + parsedLine.get(6) + "\n"
					+ "\tFOAP: " + parsedLine.get(9) + "  " + parsedLine.get(10) + "  " + parsedLine.get(11) + "  " +  parsedLine.get(12) + "\n"
					;
		}


		return null;
	}

	protected String searchSAE(String str, int column, String searchMode, String module) {
		System.out.println("Search column " + column + " to see if it " + searchMode + " " + str );
		String line = "";
		int count = 0;
		File[] files = new File(System.getProperty("user.home") + "//Concur_Files//" + environment + "//SAE_Files").listFiles();
		File file = null;
		ArrayList<String> parsedLine = null;
		StringBuilder retStr = new StringBuilder();
		retStr.append("Search column " + column + " to see if it " + searchMode + " " + str + "\n");
		--column;
		if (searchMode.equals("contains")) {
			str = str.toLowerCase();
		}
		for (int i = 0; i < files.length; ++i) {
			file = files[i];
			if (file.getName().endsWith(".txt") && file.getName().startsWith("sae")) {
				int fileLine = 0;
				try {
					BufferedReader br = new BufferedReader(new FileReader(file));
					while ((line = br.readLine()) != null) {
						++fileLine;
						parsedLine = customSplitSpecific(line.replace(",", "").replace("\"", "").replace("|", ","),
								',');

						if (parsedLine.get(0).equals("EXTRACT") || parsedLine.size() < 63)
							continue;

						if (isPcard(parsedLine) && isExpense(parsedLine)) {
							System.out.println("\tERROR - Line evaluated to BOTH pCard and Expense");
							System.out.println(file.getName() + "\t" + fileLine);
							continue;
						}
						if (!isPcard(parsedLine) && !isExpense(parsedLine)) {
							System.out.println("\tERROR - Line evaluated to NEITHER pCard or Expense");
							System.out.println("\t"+file.getName() + "\t" + "Line: "+fileLine);
							continue;
						}

						// start the search
						if (searchMode.equals("contains") && parsedLine.get(column).toLowerCase().contains(str)) {
							retStr.append(buildSAE(parsedLine, file.getPath(), fileLine));
							++count;
						}
						else if (searchMode.equals("startsWith") && parsedLine.get(column).startsWith(str)) {
							retStr.append(buildSAE(parsedLine, file.getName(), fileLine));
							++count;
						}
						else if (searchMode.equals("endsWith") && parsedLine.get(column).endsWith(str)) {
							retStr.append(buildSAE(parsedLine, file.getName(), fileLine));
							++count;
						}
						else if (searchMode.equals("equals") && parsedLine.get(column).equals(str)) {
							retStr.append(buildSAE(parsedLine, file.getName(), fileLine));
							++count;
						}
						else if (searchMode.equals("doesNotEqual") && !parsedLine.get(column).equals(str)) {
							retStr.append(buildSAE(parsedLine, file.getName(), fileLine));
							++count;
						}
					}
					br.close();
				} catch (IOException e2) {
					e2.printStackTrace();
					System.out.println("File not found");
				}
			}
		}
		System.out.println("Done with SAE search.");
		if (count == 0) {
			return "No results for: \nSearch column " + ++column + " to see if it " + searchMode + " " + str;
		}
		return retStr.toString();
	}

	protected String searchSRE(String str, int column, String searchMode) {
		System.out.println("Search column " + column + " to see if it " + searchMode + " " + str );
		String line = "";
		int count = 0;
		File[] files = new File(System.getProperty("user.home") + "//Concur_Files//" + environment + "//SRE_Files").listFiles();
		File file = null;
		ArrayList<String> parsedLine = null;
		StringBuilder retStr = new StringBuilder();
		retStr.append("Search column " + column + " to see if it " + searchMode + " " + str + "\n");
		--column;
		if (searchMode.equals("contains")) {
			str = str.toLowerCase();
		}
		for (int i = 0; i < files.length; ++i) {
			file = files[i];
			if (file.getName().endsWith(".txt") && file.getName().startsWith("sre")) {
				int fileLine = 0;
				try {
					BufferedReader br = new BufferedReader(new FileReader(file));
					while ((line = br.readLine()) != null) {
						++fileLine;
						parsedLine = customSplitSpecific(line.replace(",", "").replace("\"", "").replace("|", ","), ',');
						if(parsedLine.size()!=86&&parsedLine.size()!=238) {
							System.out.println("ERROR. WRONG Length - " + parsedLine.size());
							continue;
						}
						// start the search
						if (searchMode.equals("contains") && parsedLine.get(column).toLowerCase().contains(str)) {
							retStr.append(buildSRE(parsedLine, file.getName(), fileLine));
							++count;
						} else if (searchMode.equals("startsWith") && parsedLine.get(column).startsWith(str)) {
							retStr.append(buildSRE(parsedLine, file.getName(), fileLine));
							++count;
						} else if (searchMode.equals("endsWith") && parsedLine.get(column).endsWith(str)) {
							retStr.append(buildSRE(parsedLine, file.getName(), fileLine));
							++count;
						} else if (searchMode.equals("equals") && parsedLine.get(column).equals(str)) {
							retStr.append(buildSRE(parsedLine, file.getName(), fileLine));
							++count;
						} else if (searchMode.equals("doesNotEqual") && !parsedLine.get(column).equals(str)) {
							retStr.append(buildSRE(parsedLine, file.getName(), fileLine));
							++count;
						}
					}
					br.close();
				} catch (IOException e2) {
					e2.printStackTrace();
					System.out.println("File not found");
				}
			}
		}
		System.out.println("Done with SRE search.");
		if (count == 0) {
			return "No results for: \nSearch column " + ++column + " to see if it " + searchMode + " " + str;
		}
		return retStr.toString();
	}

	protected String searchVendorTerm(String id) {
		String line = "";
		int count = 0;
		File[] files = new File(System.getProperty("user.home") + "//Concur_Files//" + environment + "//VENDOR_TERM_FEED_Files").listFiles();
		File file = null;
		String[] parsedLine = null;
		StringBuilder retStr = new StringBuilder();
		for (int i = 0; i < files.length; ++i) {
			file = files[i];
			if (file.getName().endsWith(".txt") && file.getName().contains("concur_vendor_term_feed")) {
				try {
					BufferedReader br = new BufferedReader(new FileReader(file));
					while ((line = br.readLine()) != null) {
						parsedLine = line.split(",");
						if (parsedLine.length < 5 || !parsedLine[1].equals(id) && parsedLine[1] != id)
							continue;
						retStr.append("File: " + file.getName() + "\tName: " + parsedLine[2].replaceAll("\"", "")
								+ "\tTax ID: " + parsedLine[3] + "\tEmail: " + parsedLine[52] + "\t" + parsedLine[15]
								+ "\n");
						++count;
					}
					br.close();
				} catch (IOException e2) {
					System.out.println("File not found");
				}
			}
		}
		System.out.println("Done with Vendor search.");
		if (count == 0) {
			return "Vendor " + id + " not found.";
		}
		return retStr.toString();
	}

	protected Float formatAmount(String amount){
		amount=amount.trim();
		if(amount.length()>2){
			amount=amount.substring(0,amount.length()-2)+"."+amount.substring(amount.length()-2,amount.length());
			return Float.valueOf(amount);
		}
		else if(amount== null||amount.equals("")) {
			return null;
		}
		else{
			amount=amount.substring(0,2);
			return Float.valueOf(amount);
		}
	}

	protected void validateFupload (String filename){
		String line = "";
		File file = new File(System.getProperty("user.home") + "//Concur_Files//misc//"+filename);
		double running_total = 0.0;

		System.out.println("File: "+file.getAbsolutePath());
		try {
			int count = 0;
			BufferedReader br = new BufferedReader(new FileReader(file));
			System.out.println("Successfully opened!\n");
			String rec_count ;
			String trans_tot;
			while ((line = br.readLine()) != null) {
				String ruleCode = line.substring(17,21);
				String recordType = line.substring(16,17);
				String amount = line.substring(29,41);
				String d_c_ind = line.substring(76,77);
				if(recordType.equals("2")) {
					++count;
					System.out.println("<" + ruleCode + ">    <" + d_c_ind + ">    <" + recordType + ">    <" + amount + ">    <" + formatAmount(amount) + ">");
				}
				else if(recordType.equals("3")){
					rec_count=line.substring(17,25).trim();
					trans_tot=line.substring(25,37).trim();
					System.out.println("<"+rec_count+">    <"+formatAmount(trans_tot)+">");
				}
				else
					System.out.println(line);

			}
			br.close();
			System.out.println("Double - "+running_total);
			System.out.println("Count - "+count);
		} catch (IOException e2) {
			System.out.println("File not found - "+file.getAbsolutePath());
		}
/*
line := rpad(v_system_id,8)         || --8 SYSTEM_ID*
		rpad(v_doc_code,8)          || --8 DOC_CODE
		'2'                         || --1 REC_TYPE (2 = detail record)*
		rpad( 'FT01', 4 )           || --4 RUCL_CODE*
		rpad( ' ', 8 )              || --8 DOC_REF_NUM  (cwid, Dana estimates)
		lpad(
			REPLACE(
				trim(TO_CHAR(abs(
					v_actual_amount), '9999999999999999999D99') )
				,'.' , '' ),
		12,' ')                     || --12 TRANS_AMT; right-jusitified; no decimal
		rpad('Adv-'||v_concur_sae_last_name||'-'||v_sre_row.concur_sre_cash_adv_key||v_str_counter, 35 )||--35 TRANS_DESC (col20 + col4 + col71)
		rpad( 'C', 1 )              || --1 DR_CR_IND; D or C
		rpad( ' ', 2 )              || --2 BANK_CODE
		rpad( 'A', 1 )              || --1 COAS_CODE
		rpad( ' ', 6 )              || --6 ACCI_CODE
		rpad( '10100', 6 )          || --6 FUND_CODE
		rpad( ' ', 6 )              || --6 ORGN_CODE
		rpad( '101136', 6 )         || --6 ACCT_CODE
		rpad( ' ', 6 )              || --6 PROG_CODE
		rpad( ' ', 6 )              || --6 ACTv_acct_code (leave blank)
		rpad( ' ', 6 )              || --6 LOCN_CODE ignored
		rpad( ' ', 8 )              || --8 ENCD_NUM
		rpad( ' ', 4 )              || --4 ENCD_ITEM_NUM
		rpad( ' ', 4 )              || --4 ENCD_SEQ_NUM
		rpad( ' ', 1 )              || --1 ENCD_ACTION_IND
		rpad( ' ', 8 )              || --8 PRJD_CODE
		rpad( ' ', 1 )                --1 ENCB_TYPE
		;
* */

		System.out.println("Done with fupload validation.");


	}

	protected String searchVendor(String id) {
		String line = "";
		int count = 0;
		File[] files = new File(System.getProperty("user.home") + "//Concur_Files//" + environment + "//VENDOR_FEED_Files").listFiles();
		File file = null;
		ArrayList<String> parsedLine = null;
		StringBuilder retStr = new StringBuilder();
		Integer lineNumber;
		for (int i2 = 0; i2 < files.length; ++i2) {
			file = files[i2];
			if (file.getName().endsWith(".txt") && file.getName().contains("concur_vendor_feed")) {
				try {
					BufferedReader br = new BufferedReader(new FileReader(file));
					lineNumber = 0;
					while ((line = br.readLine()) != null) {
						++lineNumber;
						parsedLine = customSplitSpecific(line, ',');
						// System.out.println(file.getName());
						if (parsedLine.size() < 40 || !parsedLine.get(1).equals(id))
							continue;

						retStr.append("File: " + file.getName() + "\tLENGTH: " + parsedLine.size() + "\tLINE: "
								+ lineNumber + "\tName: " + parsedLine.get(2).replaceAll("\"", "") + "\tDisp. Code: "
								+ parsedLine.get(39) + /*
						 * "\tEmail: " + parsedLine[52] +
						 */"\t" + parsedLine.get(15) + "\n");
						++count;
					}
					br.close();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
			}
		}
		System.out.println("Done with Vendor search.");
		if (count == 0) {
			return "Vendor not found - " + id;
		}
		return retStr.toString();
	}

	protected static ArrayList<String> customSplitSpecific(String s, char delim) {
		ArrayList<String> words = new ArrayList<String>();
		boolean notInsideComma = true;
		int start = 0;
		for (int i = 0; i < s.length() - 1; i++) {
			if (s.charAt(i) == delim && notInsideComma) {
				words.add(s.substring(start, i));
				start = i + 1;
			} else if (s.charAt(i) == '"')
				notInsideComma = !notInsideComma;
		}
		words.add(s.substring(start));
		return words;
	}

	protected String searchPaymentRequest(String key) {
		String line = "";
		int count = 0;
		File[] files = new File(System.getProperty("user.home") + "//Concur_Files//" + environment + "//PAYMENT_REQUEST").listFiles();
		File file = null;
		String[] parsedLine = null;
		StringBuilder retStr = new StringBuilder();
		for (int i2 = 0; i2 < files.length; ++i2) {
			file = files[i2];

			if (file.getName().endsWith(".txt") && file.getName().contains("concur_pymt_req")) {
				try {
					BufferedReader br = new BufferedReader(new FileReader(file));
					while ((line = br.readLine()) != null) {
						parsedLine = line.split(",");
						if (parsedLine.length < 6)
							continue;
						if (parsedLine[0].equals(key)) {

							retStr.append("File: " + file.getName() + "\tPayment Request Key: " + parsedLine[0]
									+ "\tName: " + parsedLine[1] + "\tInvoice Number: " + parsedLine[2] + "\tAmount: "
									+ parsedLine[3] + "\tStatus: " + parsedLine[4] + "\tDate: " + parsedLine[5] + "\n");
							++count;
						}
					}
					br.close();
				} catch (IOException e2) {
					System.out.println("File not found");
				}
			}
		}
		System.out.println("Done with Payment Request key search.");
		if (count == 0) {
			return "Request key " + key + " not found.";
		}
		return retStr.toString();
	}
	protected String searchPaymentReceipt(String key) {
		String line = "";
		int count = 0;
		File[] files = new File(System.getProperty("user.home") + "//Concur_Files//" + environment + "//PAYMENT_RECEIPT").listFiles();
		File file = null;
		String[] parsedLine = null;
		StringBuilder retStr = new StringBuilder();
		for (int i2 = 0; i2 < files.length; ++i2) {
			file = files[i2];
			if(!file.getName().contains("201911"))
				continue;
			if (file.getName().endsWith(".txt") && file.getName().contains("concur_pymt_receipt_")) {
				try {
					BufferedReader br = new BufferedReader(new FileReader(file));
					while ((line = br.readLine()) != null) {
						if(line.startsWith("100"))
							continue;
						parsedLine = line.split(",");
						if (parsedLine[6].contains(key)) {
							retStr.append("File: " + file.getName() +"\n\t" +
									"Key: "+parsedLine[6]+"\n\t" +
									"Check Number: "+parsedLine[5]+"\n\t" +
									"Amount: "+parsedLine[1]+"\n\t" +
									"\n");
							++count;
						}
					}
					br.close();
				} catch (IOException e2) {
					System.out.println("File not found");
				}
			}
		}
		System.out.println("Done with Payment Request key search.");
		if (count == 0) {
			return "Request key " + key + " not found.";
		}
		return retStr.toString();
	}

	protected String search710(String chart, String fund, String orgn, String cwid,String level) {
		System.out.println("Initiating 710 search..");
		String line = "";
		int count = 0;

		File[] files = new File(System.getProperty("user.home") + "//Concur_Files//" + environment + "//710_FEED_Files").listFiles();
		File file = null;
		String[] parsedLine = null;
		StringBuilder retStr = new StringBuilder();
		for (int i2 = 0; i2 < files.length; ++i2) {
			file = files[i2];
			if (file.getName().endsWith(".txt") && file.getName().contains("concur_employee_feed_710")) {
				try {
					//System.out.println(file.getName());
					BufferedReader br = new BufferedReader(new FileReader(file));
					while ((line = br.readLine()) != null) {
						parsedLine = line.split(",");
						if (parsedLine.length == 7 || !parsedLine[1].equals("EXP"))
							continue;


						if ((chart.equals("") || parsedLine[3].equals(chart))
								&& (fund.equals("") || parsedLine[4].equals(fund))
								&& (orgn.equals("") || parsedLine[5].equals(orgn))
								&& (cwid.equals("") || parsedLine[2].equals(cwid))
								&& (level.equals("") || parsedLine[parsedLine.length - 1].equals(level))
						) {
							{

								retStr.append("File: " + file.getName() + "\t\t" + "CWID: " + parsedLine[2] + "\t"
										+ "COAS: " + parsedLine[3] + "\t" + "Fund: " + parsedLine[4] + "\t" + "ORGN: "
										+ parsedLine[5] + "\t");
								if (parsedLine[0].equals("710")) {
									retStr.append("\tLEVEL: " + parsedLine[parsedLine.length - 1] + "\n");
									System.out.println(line);
								}
								else
									retStr.append("\tREMOVED\n");
								++count;

							}
						}
					/*

						 if(parsedLine[3].equals("A") && parsedLine[5].equals("217201") ){

							 retStr.append("File: " + file.getName() + "\t\t" + "CWID: " + parsedLine[2] + "\t"
									 + "COAS: " + parsedLine[3] + "\t" + "Fund: " + parsedLine[4] + "\t" + "ORGN: "
									 + parsedLine[5] + "\t");
						 }
						 */

					}
					br.close();
				} catch (IOException e2) {
					System.out.println("File not found");
				}
			}
		}
		System.out.println("Done with 710 search.");

		if (count == 0)
			return "NO RESULTS";
		return retStr.toString();
	}

	protected String writeListToFile(String fileName, String header, List<String> stuff) {
		try {
			String path = System.getProperty("user.home") + "//Concur_Files//" + environment + "//Generated_Files";
			BufferedWriter output = new BufferedWriter(new FileWriter(path+"//"+fileName, false));
			output.write(header + "\r\n");
			for (int i = 0; i < stuff.size(); ++i)
				output.write(stuff.get(i) + "\r\n");
			output.close();
			return "File created: "+ System.getProperty("user.home") + "\\Concur_Files\\" + environment + "\\Generated_Files\\"+fileName;
		} catch (IOException e2) {
			e2.printStackTrace();
			return null;
		}
	}

	protected String getContents(String fileName){
		String line;
		StringBuilder key = new StringBuilder();
		File file = new File(fileName);

		System.out.println("File: "+file.getAbsolutePath());
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			System.out.println("Successfully opened!\n");
			while ((line = br.readLine()) != null) {
				key.append(line);
			}
			br.close();
		} catch (IOException e2) {
			System.out.println("File not found - "+file.getAbsolutePath());
		}
		return key.toString();
	}

}