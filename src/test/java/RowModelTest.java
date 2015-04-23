import com.github.javafaker.Faker;
import fr.sii.domain.spreadsheet.Row;
import org.junit.Test;

import java.util.Date;
import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * Created by tmaugin on 08/04/2015.
 */
public class RowModelTest {
    Row row = new Row();

    @Test
    public void newRowModel() {
        Faker faker = new Faker();

        String bio = faker.lorem().paragraph();
        String company = faker.name().lastName();
        String coSpeaker = faker.name().fullName() + "," + faker.name().fullName();
        String description = faker.lorem().paragraph();
        Integer difficulty = new Random().nextInt((3 - 1) + 1) + 1;
        String email = faker.internet().emailAddress();
        boolean financial = true;
        String firstName = faker.name().firstName();
        boolean hotel = true;
        String hotelDate = "13/11/1992";
        String name = faker.name().lastName();
        String phone = faker.phoneNumber().phoneNumber();
        String references = faker.lorem().paragraph();
        String sessionName = faker.lorem().paragraph(1);
        String social = faker.internet().url() + ", " + faker.internet().url();
        String track= faker.name().lastName();
        boolean travel = false;
        Date added = new Date();

        row.setBio(bio);
        row.setCompany(company);
        row.setCoSpeaker(coSpeaker);
        row.setDescription(description);
        row.setDifficulty(difficulty);
        row.setEmail(email);
        row.setFinancial(financial);
        row.setFirstname(firstName);
        row.setHotel(hotel);
        row.setHotelDate(hotelDate);
        row.setName(name);
        row.setPhone(phone);
        row.setReferences(references);
        row.setSessionName(sessionName);
        row.setSocial(social);
        row.setTrack(track);
        row.setTravel(travel);
        row.setAdded(added);

        assertEquals(bio, row.getBio());
        assertEquals(company, row.getCompany());
        assertEquals(coSpeaker, row.getCoSpeaker());
        assertEquals(description, row.getDescription());
        assertEquals(difficulty, Integer.valueOf(row.getDifficulty()));
        assertEquals(email, row.getEmail());
        assertEquals(financial, row.getFinancial());
        assertEquals(firstName, row.getFirstname());
        assertEquals(hotel, row.getHotel());
        assertEquals(hotelDate, row.getHotelDate());
        assertEquals(name, row.getName());
        assertEquals(phone, row.getPhone());
        assertEquals(references, row.getReferences());
        assertEquals(sessionName, row.getSessionName());
        assertEquals(social, row.getSocial());
        assertEquals(track, row.getTrack());
        assertEquals(travel, row.getTravel());
        assertEquals(added, row.getAdded());
    }
}
