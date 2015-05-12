package fr.sii.repository.email;

/**
 * Created by tmaugin on 08/04/2015.
 */

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Templating
{
    private VelocityEngine ve;
    private HashMap<String, String> data;
    private String templatePath;

    public Templating(String templatePath) throws Exception {
        Properties props = new Properties();
        props.setProperty("resource.loader", "class");
        props.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

        this.ve = new VelocityEngine(props);
        this.templatePath = templatePath;
    }

    public Templating(String templatePath, boolean isTesting) throws Exception {
        Properties props = new Properties();

        props.setProperty("resource.loader", "file");
        props.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.FileResourceLoader");
        props.setProperty("file.resource.loader.path","src/main/webapp/WEB-INF/classes");

        this.ve = new VelocityEngine(props);
        this.templatePath = templatePath;
    }


    public String getTemplate() throws Exception {

        this.ve.init();
        VelocityContext context = new VelocityContext();
        for (  Map.Entry<String,String> item : this.data.entrySet()) {
            context.put(item.getKey(),item.getValue());
        }
        Template t = ve.getTemplate(this.templatePath,"UTF-8");
        StringWriter writer = new StringWriter();
        t.merge(context, writer);
        return writer.toString();
    }
    public HashMap<String, String> getData() {
        return data;
    }

    public void setData(HashMap<String, String> data) {
        this.data = data;
    }

    public String getTemplatePath() {
        return templatePath;
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }
}