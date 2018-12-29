package com.zxd.shopping.mapper;

import com.zxd.shopping.bean.User;

public interface UserMapper {

    public void insert(User user);

    public void update(User user);

    public void delete(int id);

    public User find(int id);

}