package com.fundamentos.springboot.fundamentos.dto;

import java.time.LocalDate;

public class UserDto {
    private Long id;
    private String name;
    private LocalDate birthday;

    public UserDto(Long id, String name, LocalDate birthday) {
        this.id = id;
        this.name = name;
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", birthday='" + birthday + '\'' +
                '}';
    }
}
