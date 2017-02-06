package fr.argus.socle.util;

import static com.diffplug.common.base.Errors.rethrow;
import static com.google.common.base.Strings.emptyToNull;
import static fr.argus.socle.util.Constant.FORMAT_YYYYMMDD_HH_MM_SS;
import static java.util.Optional.ofNullable;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;
 
public class DateAdapter extends XmlAdapter<String, Date>{
 
    private SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT_YYYYMMDD_HH_MM_SS);
 
    @Override
    public String marshal(Date date) throws Exception {
        return ofNullable(date).map(d->dateFormat.format(d)).orElse("");
    }
 
    @Override
    public Date unmarshal(String date) throws Exception {
        return ofNullable(emptyToNull(date)).map(rethrow().wrapFunction(d->dateFormat.parse(d))).orElse(null);
    }
}