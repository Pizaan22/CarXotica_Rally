package ca.sheridancollege.tadiwala.assignment3_pizaan_tadiwala.bean;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class User implements Serializable{
    private int userId;
    private String userName;
    private String encrPassword;
    private String roleName;
}
