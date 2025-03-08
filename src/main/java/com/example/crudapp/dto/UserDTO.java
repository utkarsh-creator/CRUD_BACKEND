package com.example.crudapp.dto;

import com.example.crudapp.model.Role;
import com.example.crudapp.model.User;
import lombok.Data;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String email;  // ✅ Added email
    private String firstName;  // ✅ Added first name
    private String lastName;   // ✅ Added last name
    private String address;  // ✅ Added address
    private String phone;    // ✅ Added phone number
    private Set<String> roles;

    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail(); // ✅ Assigning email
        this.firstName = user.getFirstName(); // ✅ Assigning first name
        this.lastName = user.getLastName(); // ✅ Assigning last name
        this.address = user.getAddress(); // ✅ Assigning address
        this.phone = user.getPhone(); // ✅ Assigning phone
        this.roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
    }
}
