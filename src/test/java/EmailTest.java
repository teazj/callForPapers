import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import fr.sii.service.email.Templating;

/**
 * Created by tmaugin on 09/04/2015.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EmailTest {

    @Test
    public void test1_emailTemplate() throws Exception {
        Templating t = new Templating("mails/test.html", true);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("var1", "test1");
        map.put("var2", "test2");
        t.setData(map);
        assertEquals(false, t.getTemplate().contains("$"));
    }

    @Test
    public void test2_emailTemplateConfirmed() throws Exception {
        Templating t = new Templating("mails/confirmed.html", true);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("name", "Thomas");
        map.put("talk", "Google App Engine pour les nuls");
        map.put("hostname", "http://yourappid.appspot.com/");
        map.put("id", "123");
        t.setData(map);
        assertEquals(false, t.getTemplate().contains("$"));
    }

    @Test
    public void test3_emailTemplateNotSelectionned() throws Exception {
        Templating t = new Templating("mails/notSelectionned.html", true);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("name", "Thomas");
        map.put("talk", "Google App Engine pour les nuls");
        map.put("community", "GDG Nantes");
        map.put("event", "DevFest 2015");
        map.put("hostname", "http://yourappid.appspot.com/");

        t.setData(map);
        assertEquals(false, t.getTemplate().contains("$"));
    }

    @Test
    public void test4_emailTemplatePending() throws Exception {
        Templating t = new Templating("mails/pending.html", true);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("name", "Thomas");
        map.put("talk", "Google App Engine pour les nuls");
        map.put("event", "DevFest 2015");
        map.put("date", "13/11/1992");
        map.put("hostname", "http://yourappid.appspot.com/");
        t.setData(map);
        assertEquals(false, t.getTemplate().contains("$"));
    }

    @Test
    public void test5_emailTemplateSelectionned() throws Exception {
        Templating t = new Templating("mails/selectionned.html", true);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("name", "Thomas");
        map.put("talk", "Google App Engine pour les nuls");
        map.put("event", "DevFest 2015");
        map.put("date", "13/11/1992");
        map.put("releaseDate", "12/11/1992");
        map.put("hostname", "http://yourappid.appspot.com/");

        t.setData(map);
        assertEquals(false, t.getTemplate().contains("$"));
    }

    @Test
    public void test5_emailTemplateVerify() throws Exception {
        Templating t = new Templating("mails/verify.html", true);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("link", "http://google.fr");
        map.put("hostname", "http://yourappid.appspot.com/");

        t.setData(map);
        assertEquals(false, t.getTemplate().contains("$"));
    }

    @Test
    public void test6_emailTemplateVerify() throws Exception {
        Templating t = new Templating("mails/newMessage.html", true);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("name", "Thomas");
        map.put("talk", "Google App Engine pour les nuls");
        map.put("hostname", "http://yourappid.appspot.com/");
        map.put("id", "123");
        t.setData(map);
        assertEquals(false, t.getTemplate().contains("$"));
    }

    @Test
    public void test7_emailTemplateVerify() throws Exception {
        Templating t = new Templating("mails/newMessageAdmin.html", true);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("name", "Thomas");
        map.put("talk", "Google App Engine pour les nuls");
        map.put("hostname", "http://yourappid.appspot.com/");
        map.put("id", "123");
        t.setData(map);
        assertEquals(false, t.getTemplate().contains("$"));
    }
}
