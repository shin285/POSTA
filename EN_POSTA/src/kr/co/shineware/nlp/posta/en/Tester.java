package kr.co.shineware.nlp.posta.en;

public class Tester {

	public static void main(String[] args) {
		EnPosta enposta = new EnPosta("..\\POSTA\\model_build");
		enposta.analyze("This is the war");
	}

}
