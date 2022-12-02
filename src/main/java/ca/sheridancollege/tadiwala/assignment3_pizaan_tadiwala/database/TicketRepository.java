package ca.sheridancollege.tadiwala.assignment3_pizaan_tadiwala.database;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import ca.sheridancollege.tadiwala.assignment3_pizaan_tadiwala.bean.Ticket;

@Repository
public class TicketRepository implements Serializable {
    @Autowired
    private NamedParameterJdbcTemplate jdbc;

    public ArrayList<Ticket> getTickets() {
        MapSqlParameterSource param = new MapSqlParameterSource();
        String query = "SELECT * FROM tickets";
        ArrayList<Ticket> tickets = (ArrayList<Ticket>) jdbc.query(query, param,
                new BeanPropertyRowMapper<>(Ticket.class));

        return tickets;
    }

    public ArrayList<Ticket> getTicketsByName(String userName) {
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("userName", userName);
        String query = "SELECT * FROM tickets WHERE personName=:userName";
        ArrayList<Ticket> tickets = (ArrayList<Ticket>) jdbc.query(query, param,
                new BeanPropertyRowMapper<>(Ticket.class));

        return tickets;
    }

    public Ticket getTicketById(int id) {
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("id", id);
        String query = "SELECT * FROM tickets WHERE id=:id";
        ArrayList<Ticket> tickets = (ArrayList<Ticket>) jdbc.query(query, param,
                new BeanPropertyRowMapper<>(Ticket.class));

        return tickets.size() > 0 ? tickets.get(0) : null;
    }

    public void addTicket(Ticket person) {
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("name", person.getPersonName());
        param.addValue("gender", person.getGender());
        param.addValue("age", person.getAge());
        param.addValue("tickets", person.getTickets());
        param.addValue("zone", person.getViewZone());

        person.calcPrice(person.getViewZone(), person.getTickets());
        param.addValue("price", person.getPrice());
        String query = "INSERT INTO tickets(personName, gender, age, tickets, viewZone, price) VALUES(:name, :gender, :age, :tickets, :zone, :price)";
        jdbc.update(query, param);
    }

    public void deleteTicketById(int id) {
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("id", id);
        String query = "DELETE FROM tickets WHERE id=:id";
        jdbc.update(query, param);
    }

    public void editTicket(Ticket person) {
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("id", person.getId());
        param.addValue("name", person.getPersonName());
        param.addValue("gender", person.getGender());
        param.addValue("age", person.getAge());
        param.addValue("tickets", person.getTickets());
        param.addValue("zone", person.getViewZone());

        person.calcPrice(person.getViewZone(), person.getTickets());
        param.addValue("price", person.getPrice());
        String query = "UPDATE tickets SET personName=:name, gender=:gender, age=:age, tickets=:tickets, viewZone=:zone, price=:price WHERE id=:id";
        jdbc.update(query, param);
    }

    public int mNum() {
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("male", "Male");
        String query = "SELECT * FROM tickets WHERE gender=:male";
        ArrayList<Ticket> tickets = (ArrayList<Ticket>) jdbc.query(query, param,
                new BeanPropertyRowMapper<>(Ticket.class));

        int mtickets = tickets.size();

        return mtickets;
    }

    public int fNum() {
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("female", "Female");

        String query = "SELECT * FROM tickets WHERE gender=:female";
        ArrayList<Ticket> tickets = (ArrayList<Ticket>) jdbc.query(query, param,
                new BeanPropertyRowMapper<>(Ticket.class));
        int ftickets = tickets.size();

        return ftickets;
    }

    public int oNum() {
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("other", "Other");
        String query = "SELECT * FROM tickets WHERE gender=:other";

        ArrayList<Ticket> tickets = (ArrayList<Ticket>) jdbc.query(query, param,
                new BeanPropertyRowMapper<>(Ticket.class));
        int otickets = tickets.size();

        return otickets;
    }

    public LinkedHashMap<String, Integer> agePlot() {
        LinkedHashMap<String, Integer> ageMap = new LinkedHashMap<>();
        MapSqlParameterSource param = new MapSqlParameterSource();
        String query = "SELECT * FROM tickets";

        for (Map<String, Object> row : jdbc.queryForList(query, param)) {
            ageMap.put((String) row.get("personName"), (Integer) row.get("age"));
        }
        return ageMap;
    }

    public int totalSales() {
        MapSqlParameterSource param = new MapSqlParameterSource();
        String query = "SELECT tickets FROM tickets";
        ArrayList<Ticket> tickets = (ArrayList<Ticket>) jdbc.query(query, param,
                new BeanPropertyRowMapper<>(Ticket.class));

        int counter = 0;
        for (Ticket t : tickets) {
            counter += t.getTickets();
        }
        return counter;
    }

    public double avgAge() {
        MapSqlParameterSource param = new MapSqlParameterSource();
        String query = "SELECT age FROM tickets";
        ArrayList<Ticket> tickets = (ArrayList<Ticket>) jdbc.query(query, param,
                new BeanPropertyRowMapper<>(Ticket.class));

        double counter = 0;
        for (Ticket t : tickets) {
            counter += t.getAge();
        }
        return counter;
    }

    

}
