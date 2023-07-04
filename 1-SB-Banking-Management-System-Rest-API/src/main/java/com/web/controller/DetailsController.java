package com.web.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.web.model.Details;
import com.web.model.Transaction;
import com.web.model.Transfer;
import com.web.service.DetailsService;

@RestController
@CrossOrigin("*")
public class DetailsController {

	
	@Autowired
	private DetailsService service;
	
	@PostMapping("/save")
	public String saveDets(@RequestBody Details details) {
		Details d1=service.saveDetails(details);
		String message=null;
		if(d1!=null) {
			message="New Bank Account Created";
		}
		else {
			message="Account Creation Failed";
		}
		return message;
	}
	
	@GetMapping("/details/{email}")
	public Integer getAccountnumber(@PathVariable String email) {
		return service.getAccountnumber(email);
	}
	
	@PatchMapping("/image/{accountnumber}")
	public String uploadImage(@PathVariable Integer accountnumber, @RequestParam("image") MultipartFile imageFile,
			@RequestParam("file") MultipartFile file) throws IOException {  	
		return service.saveImage(accountnumber, imageFile, file);
	}
	
	@PostMapping("/deposit")
	public String deposit(@RequestBody Transaction t) {
		Transaction t1=service.deposit(t);
		String message=null;
		if(t1!=null) {
			message=t1.getCredit()+"deposited in Account number:"+t1.getAccountnumber();
		}
		else {
			message="Deposit Failed please enter valid accuount number";
		}
		return message;
	}
	@PostMapping("/withdraw")
	public String withdraw(@RequestBody Transaction t) {
		Transaction t1=service.withdraw(t);
		String message=null;
		if(t1!=null) {
			message=t1.getDebit()+"withdrawed from Account number:"+t1.getAccountnumber();
		}
		else {
			message="Withdraw Failed Insuffient Funds or invalid Account number";
		}
		return message;
	}
	@PostMapping("/transfer/{accountnumber1}")
	public String transfer(@RequestBody Transfer transfer, @PathVariable("accountnumber1") int accountnumber1) {
	    transfer.setAccountnumber1(accountnumber1);
	    Transfer transferResult = service.transfer(transfer);
	    String message = null;
	    if (transferResult != null) {
	        message = transferResult.getCredit() + " transferred from Account number: " + transferResult.getAccountnumber1() + " to Account number: " + transferResult.getAccountnumber2();
	    } else {
	        message = "Transfer Failed: Insufficient funds or invalid account number";
	    }
	    return message;
	}

@DeleteMapping("/delete/{accountnumber}")
	  public void deleteCredit(@PathVariable Integer accountnumber) {
		  service.deleteDetails(accountnumber);
	  }
	
	@GetMapping("/getone/{id}")
	public Details getOneRecord(@PathVariable Integer id) {
		Details d=service.getOneDetail(id);
		return d;
	}
	
	@GetMapping("/getAll1")
	public List<Details> getAll1table() {
		List <Details> listDetails=service.getAllDetails();
		return listDetails;
	}
	
	 @GetMapping("/getAll2")
	    public List<Object[]> getAll2table() {
	        return service.getAllDetailsWithTransactions();
	 }
	 @GetMapping("/getAllDUD")
     public List<Object[]> getAllDetailsWithUserData() {
		 return service.getAllDetailsWithUserData();
	  }	 
	    
	@GetMapping("/getAll")
	public List<Object[]> getAll() {
	     return service.getAllDetailsWithTransactionAndUserData();
    }
	
//	Get Last 1Day Transaction With Account Number
	@GetMapping("/getlast1/{accountnumber}")
	public List<Transaction> getlast1(@PathVariable Integer accountnumber){
		List<Transaction> getlatest = service.findTop1DayByOrderByDateDesc(null, accountnumber);
		return getlatest;	
	}
//	Get Last 7 Transaction With Account Number
	@GetMapping("/getlast7/{accountnumber}")
	public List<Transaction> getlast7(@PathVariable Integer accountnumber){
		List<Transaction> getlatest = service.findTop7ByOrderByDateDesc(null, accountnumber);
		return getlatest;	
	}
	
//	Get Last 10 Transaction With Account Number
	@GetMapping("/getlast10/{accountnumber}")
	public List<Transaction> getlast10(@PathVariable Integer accountnumber){
		List<Transaction> getlateststatement = service.findTop10ByOrderByDateDesc(null,accountnumber);
		return getlateststatement;
		
	}
	
	
	
// Get All Transaction with AccountNumber
	@GetMapping("/getallstatement/{accountnumber}")
	public List<Transaction> getalltatement(@PathVariable Integer accountnumber){
		List<Transaction> getallstatement = service.findTop1ByOrderByDateDesc(accountnumber);
		return getallstatement;
	}
	
	@GetMapping("/get1Monthtranstions/{accountnumber}")
	public List<Transaction> get1MontTranstions(@PathVariable Integer accountnumber){
		List<Transaction> listmonth = service.findTop1MonthByOrderByDateDesc(null,accountnumber);
		return listmonth;
	}
	
	@GetMapping("/get1Yeartranstions/{accountnumber}")
	public List<Transaction> get1YearTranstions(@PathVariable Integer accountnumber){
		List<Transaction> listyear = service.findTop1YearByOrderByDateDesc(null,accountnumber);
		return listyear;
	}
	
	
	@GetMapping("/search/{accountnumber}")
    public List<Transaction> findByDateRangeByAccNumberDesc(@PathVariable Integer accountnumber,
            @RequestParam("fromDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam("toDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        return service.findByDateRangeByAccNumberDesc(accountnumber, fromDate, toDate);
	}
	 
	 	@PostMapping("/validatePinAndBalance/{accountNumber}")
	 	public String validatePinAndGetBalance(@PathVariable Integer accountNumber, @RequestBody Details details) {
			return service.validatePinAndGetBalance(accountNumber, details.getPinNumber());
		}
		
		@PostMapping("/validatePin/{accountNumber}")
		public String validatePin(@PathVariable Integer accountNumber, @RequestBody Details details) {
			return service.validatePin(accountNumber, details.getPinNumber());
		}
	 
	 @PatchMapping("/update/{acccountnumber}")
	    public String updatePinNumber(@PathVariable Integer acccountnumber, @RequestBody Map<String, Object> request) {
	        String pinNumber = (String) request.get("pinNumber");
	        return service.updatePinNumber(acccountnumber, pinNumber);
	    }
	 
	 @PostMapping("/userlogin")
	 public String userLogin(@RequestBody Details d) {
		 return service.userLogin(d);
	 }
	
	 @GetMapping("/user/{username}")
	 public Integer getAccountNumber(@PathVariable String username) {
		 return service.getaccountnumber(username);
	 }
	 @PostMapping("/closeac")
	 public String closeac(@RequestBody Details details)
	 {
		return service.closeac(details);
		 
	 }
	 
	 @GetMapping("/history")
	 public List<String> getHistory() {
	  return service.getHistory();
	 }
	 

@GetMapping("/history/{accountnumber}")
public List<String> getTransactionHistory(@PathVariable Integer accountnumber) {
	return service.getTransactionHistory(accountnumber);
}
}




