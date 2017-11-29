package nl.hanze.distapps;

import java.util.HashMap;

public class Translater {
	private HashMap<String, HashMap<String, String>> translations = new HashMap<>();
	private String language = null;

	public Translater() {
		this.translations.put("nl", getDutchTranslations());
	}

	public boolean languageAvailable(String language) {
		return this.translations.containsKey(language);
	}

	public void setLanguage(String language) {
		this.language = language;
	}
	
	public String getTranslatedWord(String word){
		return this.translations.get(this.language).get(word.toLowerCase());
	}
	
	private HashMap<String, String> getDutchTranslations(){
		HashMap<String, String> translated = new HashMap<>();
		translated.put("hello", "hallo");
		translated.put("bye", "doei");
		translated.put("phone", "telefoon");
		translated.put("mouse", "muis");
		return translated;
	}
	
}
