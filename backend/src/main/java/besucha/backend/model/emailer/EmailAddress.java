package besucha.backend.model.emailer;

import java.util.StringTokenizer;


public class EmailAddress {
    private String prefix, suffix;

    public EmailAddress(String s){
        StringTokenizer st = new StringTokenizer(s, "@");
        this.prefix = st.nextToken();
        this.suffix = st.nextToken();
    }

    @Override
    public String toString(){
        return this.prefix + "@" + this.suffix;
    }

    @Override 
    public boolean equals(Object obj){
        if (this == obj) return true;
        if (!(this instanceof EmailAddress)) return false;
        if (this.toString().equals((String)obj)) return true;
        return false;
    }
}
