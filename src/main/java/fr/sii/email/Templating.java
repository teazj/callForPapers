package fr.sii.email;

/**
 * Created by tmaugin on 08/04/2015.
 */

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.StringWriter;

public class Templating
{
    private VelocityEngine ve;
    private Object data;
    private String templatePath;

    public Templating(String templatePath){
        this.ve = new VelocityEngine();
        this.templatePath = templatePath;
    }
    public String getTemplate() throws Exception {
        this.ve.init();
        VelocityContext context = new VelocityContext();
        context.put("data",this.data);
        Template t = ve.getTemplate(this.templatePath,"UTF-8");
        StringWriter writer = new StringWriter();
        t.merge(context, writer);
        return writer.toString();
    }
    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getTemplatePath() {
        return templatePath;
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }
}