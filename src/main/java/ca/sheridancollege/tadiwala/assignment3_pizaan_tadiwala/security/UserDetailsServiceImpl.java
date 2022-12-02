package ca.sheridancollege.tadiwala.assignment3_pizaan_tadiwala.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ca.sheridancollege.tadiwala.assignment3_pizaan_tadiwala.database.SecurityRepository;



@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private SecurityRepository repo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        ca.sheridancollege.tadiwala.assignment3_pizaan_tadiwala.bean.User user = repo.findUserAccount(username);

        if (user == null) {
            System.out.println("User Not Found!");
            throw new UsernameNotFoundException("User Not Found");
        }

        List<String> roles = repo.getRolesById(user.getUserId());

        ArrayList<SimpleGrantedAuthority> grantList = new ArrayList<SimpleGrantedAuthority>();

        for (String role : roles) {
            grantList.add(new SimpleGrantedAuthority(role));
        }

        User springUser = new User(user.getUserName(), user.getEncrPassword(), grantList);

        return (UserDetails) springUser;
    }

}