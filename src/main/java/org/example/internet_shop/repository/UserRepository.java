package org.example.internet_shop.repository;

import org.example.internet_shop.dao.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface  UserRepository  extends JpaRepository<MyUser, Integer> {

    MyUser findByUsername(String username);
}
