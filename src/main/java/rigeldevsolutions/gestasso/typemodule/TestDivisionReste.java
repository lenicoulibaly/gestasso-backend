package rigeldevsolutions.gestasso.typemodule;

import java.math.BigDecimal;

public class TestDivisionReste
{
    public static void main(String[] args) {
        BigDecimal dividend = new BigDecimal(10.75);
        BigDecimal divisor = new BigDecimal(2.5);
        BigDecimal remain = dividend.remainder(divisor);
        BigDecimal quotient = dividend.divideToIntegralValue(divisor);
        System.out.println("remain = " + remain);
        System.out.println("quotient = " + quotient);
    }
}
