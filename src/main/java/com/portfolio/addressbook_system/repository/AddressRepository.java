package com.portfolio.addressbook_system.repository;

import com.portfolio.addressbook_system.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long>
{
    List<Address> findByNameContaining(String keyword);
}
