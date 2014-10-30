package kr.co.shineware.nlp.posta.en;

import java.util.List;

import kr.co.shineware.nlp.posta.en.core.EnPosta;
import kr.co.shineware.util.common.file.FileUtil;

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
		List<String> lines = FileUtil.load2List("test.in");
		for (String line : lines) {
			if(line.trim().length() == 0)continue;
			posta.analyze(line);
		}
//		posta.analyze("THIS IS WONDERFUL!");
	}
}
