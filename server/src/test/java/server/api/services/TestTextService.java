package server.api.services;

import server.services.TextService;

public final class TestTextService  extends TextService {

    @Override
    public String randomAlphanumericalString(int n) {
        return "a".repeat(n);
    }

}
