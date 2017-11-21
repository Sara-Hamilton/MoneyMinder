package com.example.MoneyMinder.models.data;

import com.example.MoneyMinder.models.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface TransactionDao extends CrudRepository<Transaction, Integer>{

    List<Transaction> findByUserId (int user_id);


}
