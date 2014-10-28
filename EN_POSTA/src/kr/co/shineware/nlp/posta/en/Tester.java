package kr.co.shineware.nlp.posta.en;

import kr.co.shineware.nlp.posta.en.core.EnPosta;

public class Tester {

	public static void main(String[] args) {
		EnPosta posta = new EnPosta();
//		posta.buildCorpus("D:\\NLP\\shineware\\posta\\Converter_version1.0.1\\data");
//		posta.saveCorpus("corpus_build");
//		posta.buildModel("corpus_build");
//		posta.saveModel("model_build");
		
		posta.load("model_build");
		posta.appendUserDic("dic.user");
		posta.buildFailLink();
		posta.analyze("New York City! GOOD!     City! GOOD! City! GOOD!");
	}
}
