package com.web.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import com.web.model.Details;
import com.web.model.Transaction;
import com.web.model.Transfer;
import com.web.model.UserData;
import com.web.repo.DetailsRepo;
import com.web.repo.TransactionRepo;
import com.web.repo.UserDataRepo;

@Service
public class DetailsServiceImp implements DetailsService {
	@Autowired
	private DetailsRepo drepo;
	
	@Autowired
	private TransactionRepo trepo;
	
	@Autowired
	private UserDataRepo urepo;
	
	@Override
	public Details saveDetails(Details details){
		details.setFullname(details.getFirstname()+" "+details.getLastname());
		details.setStatus("active");
		
		if(details.getBranch().equalsIgnoreCase("Hyderabad")) {
			details.setIfsccode("HDFC0001");
		}
		else if(details.getBranch().equals("Bangalore")) {
			details.setIfsccode("HDFC0002");
		}
		else{
			details.setIfsccode("HDFC0003");
		}
		Details d=drepo.save(details);
		Transaction t=new Transaction();
		t.setAccountnumber(details.getAccountnumber());
		t.setAddress(details.getAddress());
		t.setCredit(details.getCurrentbalance());
		t.setDebit(0.0);
		t.setCurrentbalance(details.getCurrentbalance());
		t.setFullname(details.getFirstname()+details.getLastname());
		t.setDate(LocalDate.now());
		t.setTime(LocalTime.now());
	      trepo.save(t);
		UserData ud=new UserData();
		ud.setAccountnumber(details.getAccountnumber());
			urepo.save(ud);
			return d;
	}
	
	@Override
	public Integer getAccountnumber(String email) {
		Details details = drepo.findByEmail(email);
		return details != null ? details.getAccountnumber() : null;
	}
	
	@Override
	public String saveImage(Integer accountnumber, MultipartFile imageFile, MultipartFile file) throws IOException {
		Optional<UserData> optionalUserData = urepo.findById(accountnumber);
		if (optionalUserData.isPresent()) {
			UserData userData = optionalUserData.get();
			
			userData.setName(imageFile.getOriginalFilename());
			
			byte[] imageBytes = imageFile.getBytes();
			userData.setImage(imageBytes);

			byte[] fileBytes = file.getBytes();
			userData.setFile(fileBytes);
			urepo.save(userData);
			return "New Image and PDF Inserted Successfully";
		}
		return "Account number not found: " + accountnumber;
	}
	
	@Override
	public Details getOneDetail(Integer id) {
		Details details=drepo.findById(id).get();
		return details;
	}
	
	@Override
	public Transaction deposit(Transaction details) {
		Details oldDets=drepo.findById(details.getAccountnumber()).get();
		if(oldDets.getStatus().equals("active")){
		if(details.getAccountnumber()==oldDets.getAccountnumber()&& details.getFullname().equals(oldDets.getFullname())) {
		oldDets.setCurrentbalance(details.getCredit()+oldDets.getCurrentbalance());
		drepo.save(oldDets);
		Transaction t=new Transaction();
		t.setAccountnumber(details.getAccountnumber());
	    t.setAddress(oldDets.getAddress());
	    t.setCredit(details.getCredit());
		t.setDebit(0.0);
		t.setCurrentbalance(oldDets.getCurrentbalance());
		t.setFullname(oldDets.getFirstname()+oldDets.getLastname());
		t.setDate(LocalDate.now());
		t.setTime(LocalTime.now());
	      trepo.save(t);
		return t;
		}
		else {
			return null;
		}}
		else {
			return null;
			
		}
	}
	@Override
	public Transaction withdraw(Transaction details) {
		Details oldDets=drepo.findById(details.getAccountnumber()).get();
		if(oldDets.getStatus().equals("active")){
		if(details.getAccountnumber()==oldDets.getAccountnumber()&& details.getFullname().equals(oldDets.getFullname())) {
		if(oldDets.getCurrentbalance()>=details.getDebit()) {
		oldDets.setCurrentbalance(oldDets.getCurrentbalance()-details.getDebit());
		drepo.save(oldDets);
		Transaction t=new Transaction();
		t.setAccountnumber(details.getAccountnumber());
		t.setAddress(oldDets.getAddress());
		t.setDebit(details.getDebit());
		t.setCredit(0.0);
		t.setCurrentbalance(oldDets.getCurrentbalance());
		t.setFullname(oldDets.getFirstname()+oldDets.getLastname());
		t.setDate(LocalDate.now());
		t.setTime(LocalTime.now());
	      trepo.save(t);
	      return t;
		}
		
		else {
			
		return null;
		}
		}
		else {
			return null;
		}
		}
		else {
			return null;
			
		}
	}

	@Override
	public Transfer transfer(Transfer transfer) {
	    Details creditDetails = drepo.findById(transfer.getAccountnumber2()).orElse(null);
	    Details debitDetails = drepo.findById(transfer.getAccountnumber1()).orElse(null);

	    if (creditDetails != null && debitDetails != null && creditDetails.getStatus().equals("active") && debitDetails.getStatus().equals("active")) {
	        if (transfer.getAccountnumber2() == creditDetails.getAccountnumber() && transfer.getFullname().equals(creditDetails.getFullname()) && debitDetails.getAccountnumber() == transfer.getAccountnumber1()) {

	            if (debitDetails.getCurrentbalance() >= transfer.getCredit()) {
	                debitDetails.setCurrentbalance(debitDetails.getCurrentbalance() - transfer.getCredit());
	                drepo.save(debitDetails);

	                creditDetails.setCurrentbalance(transfer.getCredit() + creditDetails.getCurrentbalance());
	                drepo.save(creditDetails);

	                Transaction debitTransaction = new Transaction();
	                debitTransaction.setAccountnumber(transfer.getAccountnumber1());
	                debitTransaction.setAddress(debitDetails.getAddress());
	                debitTransaction.setDebit(transfer.getCredit());
	                debitTransaction.setCredit(0.0);
	                debitTransaction.setCurrentbalance(debitDetails.getCurrentbalance());
	                debitTransaction.setFullname(debitDetails.getFirstname() + " " + debitDetails.getLastname());
	                debitTransaction.setDate(LocalDate.now());
	                debitTransaction.setTime(LocalTime.now());
	                trepo.save(debitTransaction);

	                Transaction creditTransaction = new Transaction();
	                creditTransaction.setAccountnumber(transfer.getAccountnumber2());
	                creditTransaction.setAddress(creditDetails.getAddress());
	                creditTransaction.setCredit(transfer.getCredit());
	                creditTransaction.setDebit(0.0);
	                creditTransaction.setCurrentbalance(creditDetails.getCurrentbalance());
	                creditTransaction.setFullname(creditDetails.getFirstname() + " " + creditDetails.getLastname());
	                creditTransaction.setDate(LocalDate.now());
	                creditTransaction.setTime(LocalTime.now());
	                trepo.save(creditTransaction);

	                Transfer transferResult = new Transfer();
	                transferResult.setAccountnumber1(transfer.getAccountnumber1());
	                transferResult.setAccountnumber2(transfer.getAccountnumber2());
	                transferResult.setCredit(transfer.getCredit());
	                transferResult.setFullname(transfer.getFullname());
	                return transferResult;
	            } else {
	                return null;
	            }
	        } else {
	            return null;
	        }
	    } else {
	        return null;
	    }
	}


	
	@Override
	public void deleteDetails(Integer accountnumber) {
		drepo.deleteById(accountnumber);

	}


	@Override
	public List<Details> getAllDetails() {
		List<Details> list=drepo.findAll();
		return list;
	}
	
	@Override
    public List<Object[]> getAllDetailsWithTransactions() {
        return drepo.getAllDetailsWithTransactions();
    }
    
    @Override
    public List<Object[]> getAllDetailsWithUserData() {
        return drepo.getAllDetailsWithUserData();
    }
    
    @Override
    public List<Object[]> getAllDetailsWithTransactionAndUserData() {
     return drepo.getAllDetailsWithTransactionAndUserData();
    }
    
    @Override
	public List<Transaction> findTop1DayByOrderByDateDesc(PageRequest pageRequest, Integer accountnumber) {
    	return trepo.findTop1DayByOrderByDateDesc(PageRequest.of(0, 1),accountnumber);
	}
	

	@Override
	public List<Transaction> findTop7ByOrderByDateDesc(PageRequest pageRequest, Integer accountnumber) {
		 return trepo.findTop7ByOrderByDateDesc(PageRequest.of(0, 7),accountnumber);
	}
	
	@Override
	public List<Transaction> findTop10ByOrderByDateDesc(PageRequest pageRequest,Integer accountnumber) {
		 return trepo.findTop10ByOrderByDateDesc(PageRequest.of(0, 10),accountnumber);
	}

	@Override
	public List<Transaction> findTop1ByOrderByDateDesc(Integer accountnumber) {
		return trepo.findTop1ByOrderByDateDesc(accountnumber);
	}

	@Override
	public List<Transaction> findTop1MonthByOrderByDateDesc(PageRequest pageRequest, Integer accountnumber) {
		
		return trepo.findTop1MonthByOrderByDateDesc(PageRequest.of(0, 30), accountnumber);
	}
	
	@Override
	public List<Transaction> findTop1YearByOrderByDateDesc(PageRequest pageRequest, Integer accountnumber) {
		return trepo.findTop1MonthByOrderByDateDesc(PageRequest.of(0, 360), accountnumber);
	}


	@Override
	@Transactional
	 public List<Transaction> findByDateRangeByAccNumberDesc(Integer accountnumber, LocalDate fromdate, LocalDate todate) {
        return trepo.findByDateRangeByAccNumberDesc(accountnumber, fromdate, todate);
    }
	
	@Override
	public String validatePinAndGetBalance(Integer accountNumber, String pinNumber) {
		Optional<Details> optionalDetails = drepo.findById(accountNumber);
		if (optionalDetails.isPresent()) {
			Details details = optionalDetails.get();
			if (details.getPinNumber().equals(pinNumber)) {
				Double currentBalance = details.getCurrentbalance();
				return "Valid user from database\nCurrent balance: " + currentBalance;
			}
		}
		return "Invalid user";
	}
	@Override
	public String validatePin(Integer accountNumber, String pinNumber) {
		Optional<Details> optionalDetails = drepo.findById(accountNumber);
		if (optionalDetails.isPresent()) {
			Details details = optionalDetails.get();
			if (details.getPinNumber().equals(pinNumber)) {
				return "Valid user from database";
			}
		}
		return "Invalid user";
	}

	@Override
	public String updatePinNumber(Integer accountnumber, String pinNumber) {
		Optional<Details> optionalDetails= drepo.findById(accountnumber);
        if (optionalDetails.isPresent()) {
            Details details = optionalDetails.get();
		        details.setPinNumber(pinNumber);
		        drepo.save(details);
		    return "PinNumber updated successfully";
		 }
            return "Failed to update PinNumber";
	}

	@Override
	public String userLogin(Details d) {
		String message = null;
        String username = d.getUsername();
        String pass1 = d.getPassword();
        String pass2 =  drepo.userLogin(username);
        if(pass1.equals(pass2))
            return message="LOGIN SUCCESS...!";
        else
            return message="LOGIN Failed...!";
	}

	@Override
	public Integer getaccountnumber(String username) {
		Details details= drepo.findByUsername(username);
        return details != null ? details.getAccountnumber() : null;
	}

	@Override
	public String closeac(Details details) {
		Details old=drepo.findById(details.getAccountnumber()).get();
		old.setStatus("inactive");
		drepo.save(old);
		return "Account With Account Details : "+details.getAccountnumber()+" "+details.getFullname()+" Closed Successfully" ;
	}
	
	 @Override
	 public List<String> getHistory() {
	  List<String> history = new ArrayList<>();
	  
	  for (Transaction transaction : trepo.findAll()) {
	   if (transaction.getCredit() > 0 || transaction.getDebit() > 0) {
	    String line = String.format("%s %s %.2f on %s",
	     transaction.getTransactionType(),
	     transaction.getFullname(),
	     transaction.getAmount(),
	     transaction.getDate()
	    );
	    history.add(line);
	   }
	  }
	  
	  return history;
	 }
	

	@Override
	public List<String> getTransactionHistory(@PathVariable Integer accountnumber) {
	 List<String> transactionHistory = new ArrayList<>();
	 
	 List<Transaction> transactions = trepo.findByAccountnumber(accountnumber);
	 
	 for (Transaction transaction : transactions) {
	  if (transaction.getCredit() > 0) {
	   transactionHistory.add("Received " + transaction.getCredit() + " from " + transaction.getFullname() + " on " + transaction.getDate());
	  } else if (transaction.getDebit() > 0) {
	   transactionHistory.add("Paid " + transaction.getDebit() + " to " + transaction.getFullname() + " on " + transaction.getDate());
	  }
	 }
	 
	 return transactionHistory;
	}

	
	
	
}
