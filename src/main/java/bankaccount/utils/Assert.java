package bankaccount.utils;

public class Assert {
    public static void notNull(String paramName, Object param) {
        org.springframework.util.Assert.notNull(
                param,
                String.format("'%s' must not be null", paramName)
        );
    }
}
