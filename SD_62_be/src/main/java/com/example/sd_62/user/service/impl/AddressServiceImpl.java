package com.example.sd_62.user.service.impl;

import com.example.sd_62.user.entity.Address;
import com.example.sd_62.user.entity.User;
import com.example.sd_62.user.repository.AddressRepository;
import com.example.sd_62.user.repository.UserRepository;
import com.example.sd_62.user.service.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    @Override
    public Address addAddress(Integer userId, Address address) {
        log.info("Adding new address for user id: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với ID: " + userId));

        address.setUser(user);

        // Nếu là address đầu tiên, tự động set làm mặc định
        if (address.getIsDefault() == null) {
            long addressCount = countUserAddresses(userId);
            address.setIsDefault(addressCount == 0);
        }

        // Nếu set là default, cập nhật các address khác
        if (Boolean.TRUE.equals(address.getIsDefault())) {
            addressRepository.unsetAllDefaultAddresses(userId);
        }

        Address savedAddress = addressRepository.save(address);
        log.info("Added address successfully for user: {}", user.getEmail());

        return savedAddress;
    }

    @Override
    public Address updateAddress(Integer id, Address addressDetails) {
        log.info("Updating address with id: {}", id);

        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy địa chỉ với ID: " + id));

        // Cập nhật các trường
        if (addressDetails.getCustName() != null) {
            address.setCustName(addressDetails.getCustName());
        }

        if (addressDetails.getCustPhone() != null) {
            address.setCustPhone(addressDetails.getCustPhone());
        }

        if (addressDetails.getProvince() != null) {
            address.setProvince(addressDetails.getProvince());
        }

        if (addressDetails.getDistrict() != null) {
            address.setDistrict(addressDetails.getDistrict());
        }

        if (addressDetails.getWard() != null) {
            address.setWard(addressDetails.getWard());
        }

        if (addressDetails.getStreet() != null) {
            address.setStreet(addressDetails.getStreet());
        }

        if (addressDetails.getType() != null) {
            address.setType(addressDetails.getType());
        }

        // Xử lý đặt làm mặc định
        if (addressDetails.getIsDefault() != null && addressDetails.getIsDefault()) {
            addressRepository.unsetAllDefaultAddresses(address.getUser().getId());
            address.setIsDefault(true);
        }

        Address updatedAddress = addressRepository.save(address);
        log.info("Updated address successfully: {}", updatedAddress.getId());

        return updatedAddress;
    }

    @Override
    public void deleteAddress(Integer id) {
        log.info("Deleting address with id: {}", id);

        Address address = getAddressById(id);

        // Nếu là address mặc định, cần set address khác làm mặc định
        if (Boolean.TRUE.equals(address.getIsDefault())) {
            // Tìm address khác của user
            List<Address> otherAddresses = addressRepository.findByUserId(address.getUser().getId());
            otherAddresses.removeIf(a -> a.getId().equals(id));

            if (!otherAddresses.isEmpty()) {
                Address newDefault = otherAddresses.get(0);
                newDefault.setIsDefault(true);
                addressRepository.save(newDefault);
            }
        }

        addressRepository.deleteById(id);
        log.info("Deleted address successfully: {}", id);
    }

    @Override
    public Address getAddressById(Integer id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy địa chỉ với ID: " + id));
    }

    @Override
    public List<Address> getUserAddresses(Integer userId) {
        return addressRepository.findByUserId(userId);
    }

    @Override
    public Address getDefaultAddress(Integer userId) {
        return addressRepository.findByUserIdAndIsDefaultTrue(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy địa chỉ mặc định cho user ID: " + userId));
    }

    @Override
    public Address setDefaultAddress(Integer addressId) {
        Address address = getAddressById(addressId);
        Integer userId = address.getUser().getId();

        // Hủy tất cả addresses mặc định
        addressRepository.unsetAllDefaultAddresses(userId);

        // Set address này làm mặc định
        address.setIsDefault(true);

        Address updatedAddress = addressRepository.save(address);
        log.info("Set address as default: {}", addressId);

        return updatedAddress;
    }

    @Override
    public boolean hasDefaultAddress(Integer userId) {
        return addressRepository.existsByUserIdAndIsDefaultTrue(userId);
    }

    @Override
    public long countUserAddresses(Integer userId) {
        return addressRepository.countByUserId(userId);
    }

    @Override
    public boolean belongsToUser(Integer addressId, Integer userId) {
        try {
            Address address = getAddressById(addressId);
            return address.getUser().getId().equals(userId);
        } catch (Exception e) {
            return false;
        }
    }
}