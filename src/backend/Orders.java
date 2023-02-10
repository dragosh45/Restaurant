package backend;

import java.sql.Timestamp;

public class Orders 
{
	int orderNo;
	int tableNo;
	int staffId;
	String status;
	Timestamp time;
	Boolean paid;
	
	//public Orders(int orderNo, int tableNo, int staffId, String status, Timestamp time, boolean paid ) {	}

	public Orders() {
		
	}
	public Orders(int orderNo, int tableNo, int staffId, String status, Timestamp time, boolean paid) {
		this.orderNo = orderNo;
		this.tableNo = tableNo;
		this.staffId = staffId;
		this.status = status;
		this.time = time;
		this.paid = paid;
	}

	public int getOrderNo() {
		return orderNo;
	}
	public int getTableNo() {
		return tableNo;
	}
	public int getStaffId() {
		return staffId;
	}
	public String getStatus() {
		return status;
	}
	public Timestamp getTime() {
		return time;
	}
	public Boolean paymentConfim() {
		return paid;
	}
}


