package com.web.model;

public class Transfer {
private int accountnumber1;
private String fullname;
private int accountnumber2;
private double credit;
public Transfer(int accountnumber1, String fullname, int accountnumber2, double credit) {
	super();
	this.accountnumber1 = accountnumber1;
	this.fullname = fullname;
	this.accountnumber2 = accountnumber2;
	this.credit = credit;
}
public int getAccountnumber1() {
	return accountnumber1;
}
public void setAccountnumber1(int accountnumber1) {
	this.accountnumber1 = accountnumber1;
}
public String getFullname() {
	return fullname;
}
public void setFullname(String fullname) {
	this.fullname = fullname;
}
public int getAccountnumber2() {
	return accountnumber2;
}
public void setAccountnumber2(int accountnumber2) {
	this.accountnumber2 = accountnumber2;
}
public double getCredit() {
	return credit;
}
public void setCredit(double credit) {
	this.credit = credit;
}
public Transfer() {
	super();
}
@Override
public String toString() {
	return "Transfer [accountnumber1=" + accountnumber1 + ", fullname=" + fullname + ", accountnumber2="
			+ accountnumber2 + ", credit=" + credit + "]";
}

}

