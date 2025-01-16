package io.v4guard.shield.api;

public class v4GuardShieldProvider {

    private static ShieldAPI shieldApi;

    private v4GuardShieldProvider() {
        throw new UnsupportedOperationException("An utility class cannot be instantiated!");
    }

    public static ShieldAPI get() {
        ShieldAPI shieldApi = v4GuardShieldProvider.shieldApi;
        if (shieldApi == null) {
            throw new NotInitializedException();
        }
        return shieldApi;
    }

    public static void register(ShieldAPI shieldApi) {
        v4GuardShieldProvider.shieldApi = shieldApi;
    }

    public static void unregister() {
        v4GuardShieldProvider.shieldApi = null;
    }


    public static class NotInitializedException extends IllegalStateException{
        private static final String message = """
                    The v4Guard account-shield API isn't initialized yet!
                    This is most likely because the v4Guard account shield isn't loaded or you're trying to use it before the plugin is enabled.
                    Please make sure to enable the v4Guard account shield plugin before using the API.
                """;

        public NotInitializedException() {
            super(message);
        }
    }
}
