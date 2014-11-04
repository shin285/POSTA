package kr.co.shineware.nlp.posta.en.test;

import kr.co.shineware.nlp.posta.en.wiki.TitleNormalizer;

public class TitleNormalizerTest {

	public static void main(String[] args) {
		TitleNormalizer tn = new TitleNormalizer();
		tn.setDictionary("corpus_build/dic.word");
		tn.setWikiTitleFilename("C:\\Users\\jsshin\\Desktop\\enwiki-latest-all-titles-in-ns0\\enwiki-latest-all-titles-in-ns0");
		tn.normalizing();
	}

}
