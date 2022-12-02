package ca.sheridancollege.tadiwala.assignment3_pizaan_tadiwala.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import ca.sheridancollege.tadiwala.assignment3_pizaan_tadiwala.bean.User;

@Repository
public class SecurityRepository {
    @Autowired
    private NamedParameterJdbcTemplate jdbc;

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public User findUserAccount(String userName) {
        MapSqlParameterSource p = new MapSqlParameterSource();
        p.addValue("name", userName);
        String query = "SELECT * FROM sec_user WHERE userName=:name";
        ArrayList<User> users = (ArrayList<User>) jdbc.query(query, p,
                new BeanPropertyRowMapper<>(User.class));
        return (users.size() > 0) ? users.get(0) : null;
    }

    public List<String> getRolesById(long userId) {
        MapSqlParameterSource p = new MapSqlParameterSource();
        p.addValue("id", userId);
        String query = "SELECT roleName FROM user_role r JOIN sec_user u ON r.userId = u.userId WHERE u.userId=:id";
        ArrayList<String> roles = new ArrayList<>();
        List<Map<String, Object>> rows = jdbc.queryForList(query, p);

        for (Map<String, Object> row : rows) {
            roles.add((String) row.get("roleName"));
        }
        return roles;
    }

    public void addUser(User user) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        MapSqlParameterSource p = new MapSqlParameterSource();
        p.addValue("name", user.getUserName());
        p.addValue("pass", encoder.encode(user.getEncrPassword()));
        p.addValue("id", getUserId(user.getUserName()));
        String query = "INSERT INTO sec_user (userName, encrPassword) VALUES (:name, :pass)";
        jdbc.update(query, p);
    }

    public int getUserId(String userName) {
        MapSqlParameterSource p = new MapSqlParameterSource();
        p.addValue("name", userName);
        String query = "SELECT userId FROM sec_user WHERE userName=:name";
        ArrayList<User> users = (ArrayList<User>) jdbc.query(query, p,
                new BeanPropertyRowMapper<>(User.class));
        if (users.size() > 0) {
            return users.get(0).getUserId();
        }
        return 0;
    }

    public void addRole(int userId) {
        MapSqlParameterSource p = new MapSqlParameterSource();
        p.addValue("id", userId);
        String query = "INSERT INTO user_role (userId, roleName) VALUES (:id, 'ROLE_GUEST')";
        jdbc.update(query, p);
    }
}
