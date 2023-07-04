package com.web.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import com.web.model.Details;
import com.web.model.Transaction;
import com.web.model.Transfer;
public interface DetailsService {
	public Details saveDetails(Details dets);
	public Integer getAccountnumber(String email);
	String saveImage(Integer accountnumber, MultipartFile imageFile, MultipartFile file) throws IOException;
	
	public void deleteDetails(Integer accountnumber);
	public Details getOneDetail(Integer id);
	public List<Details> getAllDetails();
	public List<Object[]> getAllDetailsWithTransactions();
	 public List<Object[]> getAllDetailsWithUserData();
	public List<Object[]> getAllDetailsWithTransactionAndUserData();
	public Transaction deposit(Transaction t);
	public Transaction withdraw(Transaction t);
	public Transfer transfer(Transfer t);
	public String userLogin(Details d);
	public Integer getaccountnumber(String username);
	public List<String> getHistory();
	public List<String> getTransactionHistory(@PathVariable Integer accountnumber);
	
	List<Transaction> findTop1DayByOrderByDateDesc(PageRequest pageRequest,Integer accountnumber);
	List<Transaction> findTop7ByOrderByDateDesc(PageRequest pageRequest, Integer accountnumber);
	List<Transaction> findTop10ByOrderByDateDesc(PageRequest pageRequest,Integer accountnumber);
	List<Transaction> findTop1ByOrderByDateDesc(Integer accountnumber);
	List<Transaction> findTop1MonthByOrderByDateDesc(PageRequest pageRequest,Integer accountnumber);
	List<Transaction> findTop1YearByOrderByDateDesc(PageRequest pageRequest,Integer accountnumber);
	public String validatePinAndGetBalance(Integer accountnumber, String pinNumber); 
	public String validatePin(Integer accountnumber, String pinNumber);
	public String updatePinNumber(Integer accountnumber, String pinNumber);
	public List<Transaction> findByDateRangeByAccNumberDesc(Integer accountnumber, LocalDate fromdate, LocalDate todate);
	public String closeac(Details details);
	
}
