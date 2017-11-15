package com.example.MoneyMinder.models.data;

import com.example.MoneyMinder.models.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface AccountDao extends CrudRepository<Account, Integer> {
}
