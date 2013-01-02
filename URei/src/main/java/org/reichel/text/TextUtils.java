package org.reichel.text;

import java.text.Normalizer;

public class TextUtils {

	public String removeSpecialCharacters(String text){
		return Normalizer.normalize(text, java.text.Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]","");
	}
}
