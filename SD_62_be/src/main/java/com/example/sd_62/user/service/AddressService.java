package com.example.sd_62.user.service;

import com.example.sd_62.user.entity.Address;

import java.util.List;

public interface AddressService {

    // Thêm address mới
    Address addAddress(Integer userId, Address address);

    // Cập nhật address
    Address updateAddress(Integer id, Address address);

    // Xóa address
    void deleteAddress(Integer id);

    // Lấy address by id
    Address getAddressById(Integer id);

    // Lấy tất cả addresses của user
    List<Address> getUserAddresses(Integer userId);

    // Lấy address mặc định của user
    Address getDefaultAddress(Integer userId);

    // Đặt address làm mặc định
    Address setDefaultAddress(Integer addressId);

    // Kiểm tra user có address mặc định chưa
    boolean hasDefaultAddress(Integer userId);

    // Đếm số addresses của user
    long countUserAddresses(Integer userId);

    // Kiểm tra address có thuộc về user không
    boolean belongsToUser(Integer addressId, Integer userId);
}