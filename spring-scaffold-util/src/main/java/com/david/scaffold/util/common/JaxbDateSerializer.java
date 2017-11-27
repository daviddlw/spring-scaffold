package com.david.scaffold.util.common;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import com.david.scaffold.util.constants.Constants;

public class JaxbDateSerializer extends XmlAdapter<String, Date> {
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat(Constants.YYYYMMDDTHHMMSS);

	@Override
	public Date unmarshal(String str) throws Exception {
		return sdf.parse(str);
	}

	@Override
	public String marshal(Date date) throws Exception {
		return sdf.format(date);
	}

}
