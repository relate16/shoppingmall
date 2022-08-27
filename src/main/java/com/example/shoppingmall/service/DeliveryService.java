package com.example.shoppingmall.service;

import com.example.shoppingmall.entity.Address;
import com.example.shoppingmall.entity.Delivery;
import com.example.shoppingmall.entity.Order;
import com.example.shoppingmall.exception.NotFoundException;
import com.example.shoppingmall.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;

    @Transactional
    public void changeDelivery(Long deliveryId, Address address) {
        Optional<Delivery> deliveryOpt = deliveryRepository.findById(deliveryId);
        if (!deliveryOpt.isEmpty()) {
            Delivery findDelivery = deliveryOpt.get();
            findDelivery.changeAddress(address);
        } else {
            throw new NotFoundException("해당 delivery가 없습니다.");
        }
    }

    public Delivery findDeliveryByOrder(Order order) {
        Optional<Delivery> deliveryOpt = deliveryRepository.findById(order.getDelivery().getId());
        Delivery delivery = deliveryOpt.orElse(new Delivery(new Address("", "")));
        return delivery;
    }

}
