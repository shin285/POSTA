package kr.co.shineware.nlp.posta.en.test;

import kr.co.shineware.nlp.posta.en.wiki.TitleNormalizer;

public class TitleNormalizerTest {

	public static void main(String[] args) {
		TitleNormalizer tn = new TitleNormalizer();
		tn.setDictionary("corpus_build/dic.word");
		tn.setWikiTitleFilename("D:\\0_Data\\2014 영어 위키\\enwiki-latest-all-titles-in-ns0");
		tn.normalizing();
	}

}
