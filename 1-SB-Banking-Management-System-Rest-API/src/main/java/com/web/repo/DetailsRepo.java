package com.web.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.web.model.Details;

@Repository
public interface DetailsRepo extends JpaRepository<Details, Integer> {
	  
	    Details findByEmail(String email);
	    @Query("SELECT d, t FROM Details d INNER JOIN Transaction t ON d.accountnumber = t.accountnumber")
	    List<Object[]> getAllDetailsWithTransactions();
	    @Query("SELECT d, t, u FROM Details d "
	    		   + "INNER JOIN Transaction t ON d.accountnumber = t.accountnumber "
	    		   + "INNER JOIN UserData u ON d.accountnumber = u.accountnumber")
	    List<Object[]> getAllDetailsWithTransactionAndUserData(); 
	    
	    @Query("select d.password from Details d where d.username = :u")
	    public String userLogin(@Param("u") String username);
	    
	    @Query("SELECT d, ud FROM Details d INNER JOIN UserData ud ON d.accountnumber = ud.accountnumber")
	    List<Object[]> getAllDetailsWithUserData();
	    
	    Details findByUsername(String username);
}
