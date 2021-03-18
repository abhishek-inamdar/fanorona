import java.util.Map;

/**
 * Evaluation interface
 * This is a functional interface whose
 * functional method is evaluate(Map<Position, Integer>).
 *
 * @author Abhishek Inamdar
 */
@FunctionalInterface
public interface Evaluation {
    int evaluate(Map<Position, Integer> valueMap, int remainingDepth);
}
