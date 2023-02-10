package backend;

public class Tables 
{
	int staffId;
	int tableNo;
	String assist;
	
	public Tables(int staffId,int tableNo, String assist) {
		
		this.staffId = staffId;
		this.tableNo = tableNo;
		this.assist = assist;
	}
	
	public int getStaffId() {
		return staffId;
	}
	public int getTableNo() {
		return tableNo;
	}
	public String getAssist() {
		return assist;
	}
}
