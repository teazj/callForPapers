import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fr.sii.repository.email.Templating;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

/**
 * Created by tmaugin on 09/04/2015.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EmailTest {


    @Test
    public void test1_emailTemplate() throws Exception {
        Templating t = new Templating("test.html",true);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("var1", "test1");
        map.put("var2", "test2");
        t.setData(map);
        assertEquals("test1 test2", t.getTemplate());
    }

    @Test
    public void test2_emailTemplateConfirmed() throws Exception {
        Templating t = new Templating("confirmed.html",true);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("name", "Thomas");
        map.put("talk", "Google App Engine pour les nuls");
        t.setData(map);
        assertEquals("<html>\r\n" +
                "<head>\r\n" +
                "  <meta charset=\"UTF-8\">\r\n" +
                "</head>\r\n" +
                "<body>\r\n" +
                "  <h2>Bonjour Thomas,</h2>\r\n" +
                "  <p>Votre talk Google App Engine pour les nuls a bien été enregistré. Nous reviendrons vers vous dès que nous aurons réalisés la sélection des talks.\r\n" +
                "  </p>\r\n" +
                "</body>\r\n" +
                "</html>", t.getTemplate());
    }

    @Test
    public void test3_emailTemplateNotSelectionned() throws Exception {
        Templating t = new Templating("notSelectionned.html", true);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("name", "Thomas");
        map.put("talk", "Google App Engine pour les nuls");
        map.put("community", "GDG Nantes");
        map.put("event", "DevFest 2015");

        t.setData(map);
        assertEquals("<html>\r\n" +
                "<head>\r\n" +
                "  <meta charset=\"UTF-8\">\r\n" +
                "</head>\r\n" +
                "<body>\r\n" +
                "  <h2>Désolé Thomas !</h2>\r\n" +
                "  <p>\r\n" +
                "    Votre talk Google App Engine pour les nuls  n'a été retenu pour le DevFest 2015. Nous avons en effet reçu beaucoup de propositions et nous avons du faire des choix.\r\n" +
                "  </p>\r\n" +
                "  <p>\r\n" +
                "\tCependant votre proposition a retenu notre attention et nous ne manquerons pas de vous recontacter pour venir présenter votre sujet au cours de l'année dans le cadre du GDG Nantes.\r\n" +
                "  </p>\r\n" +
                "  <p>\r\n" +
                "\tMerci encore pour votre proposition.\r\n" +
                "  </p>\r\n" +
                "</body>\r\n" +
                "</html>", t.getTemplate());
    }

    @Test
    public void test4_emailTemplatePending() throws Exception {
        Templating t = new Templating("pending.html", true);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("name", "Thomas");
        map.put("talk", "Google App Engine pour les nuls");
        map.put("event", "DevFest 2015");
        map.put("date", "13/11/1992");
        t.setData(map);
        assertEquals("<html>\r\n" +
                "<head>\r\n" +
                "  <meta charset=\"UTF-8\">\r\n" +
                "</head>\r\n" +
                "<body>\r\n" +
                "  <h2>Bonjour Thomas,</h2>\r\n" +
                "  <p>\r\n" +
                "    Votre talk Google App Engine pour les nuls est en liste d'attente pour le programme du DevFest 2015, afin de\r\n" +
                "    gérer les éventuels désistements. Nous sommes en effet dans l'attente des réponses des  \r\n" +
                "    premières sélections. \r\n" +
                "  </p>\r\n" +
                "  <p>Cependant pouvez-vous nous confirmer votre éventuelle venue, en tant que speaker, le  \r\n" +
                "    13/11/1992 dans le cas où votre talk serait confirmé ?\r\n" +
                "  </p>\r\n" +
                "  <p>Merci de nous répondre le plus rapidement possible.</p> \r\n" +
                "</body>\r\n" +
                "</html>", t.getTemplate());
    }

    @Test
    public void test5_emailTemplateSelectionned() throws Exception {
        Templating t = new Templating("selectionned.html", true);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("name", "Thomas");
        map.put("talk", "Google App Engine pour les nuls");
        map.put("event", "DevFest 2015");
        map.put("date", "13/11/1992");
        map.put("releaseDate", "12/11/1992");

        t.setData(map);
        assertEquals("<html>\r\n" +
                "<head>\r\n" +
                "  <meta charset=\"UTF-8\">\r\n" +
                "</head>\r\n" +
                "<body>\r\n" +
                "  <h2>Félicitations Thomas !</h2>\r\n" +
                "  <p>Votre talk Google App Engine pour les nuls a été retenu pour le DevFest 2015 !</p>\r\n" +
                "  <p>Nous aurions besoin d'avoir un retour de votre part sur les aspects suivants :</p>\r\n" +
                "  <ul>\r\n" +
                "    <li>Pouvez-vous confirmer votre venue, en tant que speaker, le 13/11/1992 au DevFest 2015 ?</li>\r\n" +
                "    <li>Avez-vous des questions particulières sur l'organisation et votre venue ?</li>\r\n" +
                "  </ul>\r\n" +
                "  <p>Merci de nous répondre le plus rapidement possible. Nous annoncerons le programme le 12/11/1992.</p>\r\n" +
                "</body>\r\n" +
                "</html>", t.getTemplate());
    }
}
