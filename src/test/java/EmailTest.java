import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fr.sii.repository.email.Templating;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by tmaugin on 09/04/2015.
 */
public class EmailTest {


    @Test
    public void test1_emailTemplate() throws Exception {
        //Use random content to avoid potential residual lingering problems
        Templating t = new Templating("test.html");

        JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
        ObjectNode node = nodeFactory.objectNode();
        node.put("var1", "test1");
        node.put("var2", "test2");
        t.setData(node);

        assertEquals("\"test1\" \"test2\"", t.getTemplate());
    }
}
