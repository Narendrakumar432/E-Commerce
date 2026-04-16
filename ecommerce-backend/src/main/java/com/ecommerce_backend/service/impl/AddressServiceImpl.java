package com.ecommerce_backend.service.impl;

import com.ecommerce_backend.dto.request.AddressRequest;
import com.ecommerce_backend.entity.Address;
import com.ecommerce_backend.entity.User;
import com.ecommerce_backend.exception.ResourceNotFoundException;
import com.ecommerce_backend.repository.AddressRepository;
import com.ecommerce_backend.repository.UserRepository;
import com.ecommerce_backend.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Address addAddress(String email, AddressRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Address address = new Address();
        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setPincode(request.getPincode());
        address.setLandmark(request.getLandmark());
        address.setDefault(request.isDefault());
        address.setUser(user);

        return addressRepository.save(address);
    }

    @Override
    public List<Address> getMyAddresses(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return addressRepository.findByUserId(user.getId());
    }

    @Override
    public Address updateAddress(String email, Long addressId, AddressRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        // Security check
        if (!address.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized!");
        }

        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setPincode(request.getPincode());
        address.setLandmark(request.getLandmark());
        address.setDefault(request.isDefault());

        return addressRepository.save(address);
    }

    @Override
    public void deleteAddress(String email, Long addressId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        if (!address.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized!");
        }

        addressRepository.delete(address);
    }
}