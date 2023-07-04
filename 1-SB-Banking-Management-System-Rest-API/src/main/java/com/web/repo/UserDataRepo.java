package com.web.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.web.model.UserData;

@Repository
public interface UserDataRepo extends JpaRepository<UserData, Integer> {

}
