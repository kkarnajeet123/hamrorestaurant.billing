package com.hamrorestaurant.billing.repository;

import com.hamrorestaurant.billing.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<MenuItem, Long> {
}
