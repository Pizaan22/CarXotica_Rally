package ca.sheridancollege.tadiwala.assignment3_pizaan_tadiwala.bean;

import java.io.Serializable;
import java.text.DecimalFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ticket implements Serializable{
    private int id;
    private String personName;
    private String gender;
    private int tickets;
    private String viewZone;
    private int age;
    private double price;

    DecimalFormat df = new DecimalFormat("#.00");

    public void calcPrice(String viewZone, int tickets) {
        if (viewZone.equals("A ($89.99)")) {
            this.price = Double.parseDouble(df.format(tickets * 89.99));
        } else if (viewZone.equals("B ($49.99)")) {
            this.price = Double.parseDouble(df.format(tickets * 49.99));
        } else if (viewZone.equals("C ($29.99)")) {
            this.price = Double.parseDouble(df.format(tickets * 29.99));
        } else {
            this.price = 0;
        }
    }

    private String[] zones = { "A ($89.99)", "B ($49.99)", "C ($29.99)" };
    private String[] genders = { "Male", "Female", "Other" };
}
