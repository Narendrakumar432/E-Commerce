package com.ecommerce_backend.service;

import com.ecommerce_backend.dto.request.AddressRequest;
import com.ecommerce_backend.entity.Address;
import java.util.List;

public interface AddressService {
    Address addAddress(String email, AddressRequest request);
    List<Address> getMyAddresses(String email);
    Address updateAddress(String email, Long addressId, AddressRequest request);
    void deleteAddress(String email, Long addressId);
}