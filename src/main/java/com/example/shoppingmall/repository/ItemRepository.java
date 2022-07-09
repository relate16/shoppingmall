package com.example.shoppingmall.repository;

import com.example.shoppingmall.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemCustomRepository {
    Optional<Item> findByName(String itemName);
}
