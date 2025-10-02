package vn.iotstar.service;

import vn.iotstar.entity.User;
import java.util.List;

public interface UserService {
    User login(String username, String password);
    User save(User user);
    List<User> findAll();
}
