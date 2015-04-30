package fr.sii.domain.email;

import javax.validation.constraints.NotNull;

/**
 * Created by tmaugin on 09/04/2015.
 */
public class Email {
    @NotNull
    public String from;
    @NotNull
    public String to;
    @NotNull
    public String subject;
    @NotNull
    public String template;
    public Object data;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Email{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", subject='" + subject + '\'' +
                ", template='" + template + '\'' +
                ", data=" + data +
                '}';
    }
}
