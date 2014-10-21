package kr.co.shineware.nlp.posta.test;

import kr.co.shineware.nlp.posta.corpus.builder.CorpusBuilder;

public class CorpusBuildTest {

	public static void main(String[] args) {
		String buildPath = "D:\\NLP\\shineware\\posta\\Converter_version1.0.1\\data";
		CorpusBuilder corpusBuilder = new CorpusBuilder();
		corpusBuilder.buildPath(buildPath);
	}

}
