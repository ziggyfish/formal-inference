package antlr;

import org.antlr.v4.runtime.IntStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Recognizer;

public class RecognitionException extends org.antlr.v4.runtime.RecognitionException {
    public RecognitionException(Recognizer<?, ?> recognizer, IntStream input, ParserRuleContext ctx) {
        super(recognizer, input, ctx);
    }

    public RecognitionException(String message, Recognizer<?, ?> recognizer, IntStream input, ParserRuleContext ctx) {
        super(message, recognizer, input, ctx);
    }
}
