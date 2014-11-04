package kr.co.shineware.nlp.posta.en.wiki;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

import kr.co.shineware.nlp.posta.corpus.model.Dictionary;

public class TitleNormalizer {
	private String wikiTitleFilename;
	private Dictionary dic;

	public String getWikiTitleFilename() {
		return wikiTitleFilename;
	}

	public void setWikiTitleFilename(String wikiTitleFilename) {
		this.wikiTitleFilename = wikiTitleFilename;
	}

	public void setDictionary(String filename){
		dic = new Dictionary(filename);
	}
	public void normalizing(){
		try {
			int count = 0;
			BufferedWriter bw = new BufferedWriter(new FileWriter("result.txt"));
			BufferedReader br = new BufferedReader(new FileReader(wikiTitleFilename));
			String line = null;
			while((line = br.readLine()) != null){
				line = this.preprocessing(line);
				if(line.length() < 2)continue;
				if(dic.getDictionary().containsKey(line)){
					continue;
				}
				if(line.replaceAll("[a-zA-Z ]+", "").length() != 0){
					continue;
				}

				bw.write(line);
				bw.newLine();
				count++;
				System.out.println(count);

			}
			bw.close();
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String preprocessing(String line) {
		line = line.replaceAll("_", " ");
		line = line.replaceAll("\\(.+\\)", "");
		line = line.replaceAll("[ ]+", " ");
		return line.trim();
	}
}
