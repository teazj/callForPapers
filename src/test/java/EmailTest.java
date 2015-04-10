import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.icegreen.greenmail.junit.GreenMailRule;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetupTest;
import fr.sii.email.Templating;
import org.junit.Rule;
import org.junit.Test;


import static org.junit.Assert.assertEquals;

/**
 * Created by tmaugin on 09/04/2015.
 */
public class EmailTest {

    @Rule
    public final GreenMailRule greenMail = new GreenMailRule(ServerSetupTest.ALL);

    @Test
    public void testSomething() throws Exception {
        if (greenMail != null){
            greenMail.stop();
        }
        GreenMail greenMail = new GreenMail(ServerSetupTest.ALL);
        greenMail.start();

        //Use random content to avoid potential residual lingering problems
        final String subject = GreenMailUtil.random();
        final String body = GreenMailUtil.random();
        Templating t = new Templating("./src/main/resources/mail/template/" +  "test.html");

        JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
        ObjectNode node = nodeFactory.objectNode();
        node.put("var1", "test1");
        node.put("var2", "test2");
        t.setData(node);

        GreenMailUtil.sendTextEmailTest("to@localhost.com", "from@localhost.com", "subject", t.getTemplate()); // --- Place your sending code here
        assertEquals("\"test1\" \"test2\"", GreenMailUtil.getBody(greenMail.getReceivedMessages()[0]));
        assertEquals("subject", greenMail.getReceivedMessages()[0].getSubject());
        assertEquals(1, greenMail.getReceivedMessages().length);

        // --- Place your retrieve code here
        greenMail.stop();
    }
}
