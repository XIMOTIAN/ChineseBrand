package com.darren.chinesebrand.base.http;

import java.util.HashMap;
import java.util.Map;

/**
 * description:
 * author: Darren on 2017/10/11 15:41
 * email: 240336124@qq.com
 * version: 1.0
 */
public class GoHttpConfig {
    private Map<String, Object> publicParams;
    private IHttpEngine httpEngine;
    private Converter.Factory factory;

    private GoHttpConfig(Builder builder) {
        this.publicParams = builder.publicParams;
        this.httpEngine = builder.httpEngine;
        this.factory = builder.factory;
    }

    public IHttpEngine getHttpEngine() {
        return httpEngine;
    }

    public Map<String, Object> getPublicParams() {
        return publicParams;
    }

    public Converter.Factory getFactory() {
        return factory;
    }

    public static class Builder {
        Map<String, Object> publicParams;
        IHttpEngine httpEngine;
        Converter.Factory factory;

        public Builder addConverterFactory(Converter.Factory factory) {
            this.factory = factory;
            return this;
        }

        public Builder() {
            publicParams = new HashMap<>();
        }

        public Builder publicParams(Map<String, Object> publicParams) {
            this.publicParams = publicParams;
            return this;
        }

        public Builder engine(IHttpEngine httpEngine) {
            this.httpEngine = httpEngine;
            return this;
        }

        public GoHttpConfig build() {
            return new GoHttpConfig(this);
        }
    }
}
