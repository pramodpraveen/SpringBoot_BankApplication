package com.web.model;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
@Entity
@NamedQuery(name="Transaction.findTop1ByOrderByDateDesc", query = "select t from Transaction t where t.accountnumber =:accountnumber order by t.date desc")
@NamedQuery(name="Transaction.findTop10ByOrderByDateDesc", query = "select t from Transaction t where t.accountnumber =:accountnumber order by t.date desc")
@NamedQuery(name="Transaction.findTop7ByOrderByDateDesc", query = "select t from Transaction t where t.accountnumber =:accountnumber order by t.date desc")
@NamedQuery(name="Transaction.findTop1MonthByOrderByDateDesc", query = "select t from Transaction t where t.accountnumber =:accountnumber order by t.date desc")
@NamedQuery(name="Transaction.findTop1DayByOrderByDateDesc", query = "select t from Transaction t where t.accountnumber =:accountnumber order by t.date desc")
@NamedQuery(name="Transaction.findTop1YearByOrderByDateDesc", query = "select t from Transaction t where t.accountnumber =:accountnumber order by t.date desc")
public class Transaction {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(length = 20)
	private Integer accountnumber;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(length = 20)
	private Integer transaction_number;
	@Column(name = "date")
    private LocalDate date;
    @Column(name = "time")
    private LocalTime time;
	@Column(length = 20)
	private String fullname;
	@Column(length = 20)
	private Double currentbalance;
	@Column(length = 20)
	private Double debit;
	@Column(length = 20)
	private Double credit;
	@Column(length = 20)
	private String address;
	public Transaction() {}
	public Integer getAccountnumber() {
		return accountnumber;
	}
	public void setAccountnumber(Integer accountnumber) {
		this.accountnumber = accountnumber;
	}
	public Integer getTransaction_number() {
		return transaction_number;
	}
	public void setTransaction_number(Integer transaction_number) {
		this.transaction_number = transaction_number;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public LocalTime getTime() {
		return time;
	}
	public void setTime(LocalTime time) {
		this.time = time;
	}
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	public Double getCurrentbalance() {
		return currentbalance;
	}
	public void setCurrentbalance(Double currentbalance) {
		this.currentbalance = currentbalance;
	}
	public Double getDebit() {
		return debit;
	}
	public void setDebit(Double debit) {
		this.debit = debit;
	}
	public Double getCredit() {
		return credit;
	}
	public void setCredit(Double credit) {
		this.credit = credit;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

	 public String getTransactionType() {
		  if (credit > 0) {
		   return "received from";
		  } else if (debit > 0) {
		   return "paid to";
		  } else {
		   return "";
		  }
		 }
		 
		 public Double getAmount() {
		  if (credit > 0) {
		   return credit;
		  } else {
		   return debit * -1;
		  }
		}
		 
	
}