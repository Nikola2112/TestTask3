import com.goit.Equation;
import org.junit.Test;
import static org.junit.Assert.*;

public class EquationTest {

    @Test
    public void testIsValid() {
        // Валидное уравнение
        Equation validEquation = new Equation("2*x+5=17");
        assertTrue(validEquation.isValid());

        // Невалидное уравнение (два знака равенства)
        Equation invalidEquation1 = new Equation("x+2=3=5");
        assertFalse(invalidEquation1.isValid());

        // Невалидное уравнение (пустая строка)
        Equation emptyEquation = new Equation("");
        assertFalse(emptyEquation.isValid());

        // Невалидное уравнение (отсутствие операторов)
        Equation noOperatorsEquation = new Equation("x");
        assertFalse(noOperatorsEquation.isValid());

        // Невалидное уравнение (два знака равенства, но без пробелов)
        Equation invalidEquation2 = new Equation("x=3=5");
        assertFalse(invalidEquation2.isValid());
    }

    @Test
    public void testHasRoot() {
        // Уравнение с корнем
        Equation equationWithRoot = new Equation("x-2=0");
        assertTrue(equationWithRoot.hasRoot(2));

        // Уравнение без корня
        Equation equationWithoutRoot = new Equation("x+2=0");
        assertFalse(equationWithoutRoot.hasRoot(2));
    }

    @Test
    public void testEvaluateExpression() {
        Equation equation = new Equation("2*x+5=17");
        double result = equation.evaluateExpression(6);
        assertEquals(11, result, 1e-10);
    }

    @Test
    public void testCheckAndSaveRootValidEquation() {
        Equation validEquation = new Equation("2*x+5=17");
        assertTrue(validEquation.checkAndSaveRoot(2));
    }



    @Test
    public void testIsRoot() {
        Equation invalidEquation = new Equation("x+2=0");
        assertFalse(invalidEquation.isRoot(2));

        Equation validEquation = new Equation("x-2=0");
        assertTrue(validEquation.isRoot(2.0));
    }

}

