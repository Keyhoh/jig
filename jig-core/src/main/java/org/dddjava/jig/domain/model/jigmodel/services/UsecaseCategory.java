package org.dddjava.jig.domain.model.jigmodel.services;

public enum UsecaseCategory {
    ハンドラ,
    その他;

    public static UsecaseCategory resolver(ServiceAngle serviceAngle) {
        return serviceAngle.usingFromController() ? ハンドラ : その他;
    }

    public boolean handler() {
        return this == ハンドラ;
    }
}