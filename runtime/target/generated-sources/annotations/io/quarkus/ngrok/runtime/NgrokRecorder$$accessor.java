package io.quarkus.ngrok.runtime;
public final class NgrokRecorder$$accessor {
    private NgrokRecorder$$accessor() {}
    @SuppressWarnings("unchecked")
    public static Object get_ngrokHttpClient(Object __instance) {
        return ((NgrokRecorder)__instance).ngrokHttpClient;
    }
    @SuppressWarnings("unchecked")
    public static void set_ngrokHttpClient(Object __instance, Object ngrokHttpClient) {
        ((NgrokRecorder)__instance).ngrokHttpClient = (NgrokHttpClient)ngrokHttpClient;
    }
    public static Object construct() {
        return new NgrokRecorder();
    }
}
