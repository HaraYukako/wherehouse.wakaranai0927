package log;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Log4jのログ出力サンプルクラス。
 */
public class LogTest {

	public static void main(String[] args) {
		Sample s = new Sample();
		s.testLog();
	}
}

class Sample {
	Logger log = LogManager.getLogger(Sample.class);

	void testLog(){
		String a = "テスト";
		Exception error = new Exception();

		log.trace(a); // 出力なし
		log.debug(a); //出力なし
		log.info(a); //2016/07/21 22:50:24.975 [main] INFO   テスト
		log.warn(a, error); //2016/07/21 22:50:24.976 [main] WARN   テスト
		log.error(a, error); //2016/07/21 22:50:24.976 [main] ERROR  テスト
		log.fatal(a, error); //2016/07/21 22:50:24.976 [main] FATAL  テスト
	}
}
