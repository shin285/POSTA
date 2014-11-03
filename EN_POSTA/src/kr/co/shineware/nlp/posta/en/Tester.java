package kr.co.shineware.nlp.posta.en;

import java.io.BufferedReader;
import java.io.FileReader;

import kr.co.shineware.nlp.posta.en.core.EnPosta;

public class Tester {

	public static void main(String[] args) throws Exception {
		EnPosta posta = new EnPosta();
//		posta.buildCorpus("D:\\NLP\\shineware\\posta\\Converter_version1.0.1\\data");
//		posta.saveCorpus("corpus_build");
//		posta.buildModel("corpus_build");
//		posta.saveModel("model_build");
		
		posta.load("model_build");
		posta.appendUserDic("dic.user");
		posta.buildFailLink();
		BufferedReader br = new BufferedReader(new FileReader("test.in"));
		long begin,end,elapsed=0;
		String line = null;
		while((line = br.readLine()) != null){
			if(line.trim().length() == 0)continue;
			begin = System.currentTimeMillis();
			posta.analyze(line);
			end = System.currentTimeMillis();
			elapsed += (end-begin);
		}
		System.out.println(elapsed/1000.0+" sec");
		System.out.println(posta.totalTokens/1000.0+"k tokens");
		br.close();
	}
}
