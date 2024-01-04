package com.api.gestnotesapi.services;

import org.jvnet.hk2.annotations.Service;

import java.util.List;

public interface AnonymatService {
    public String anonymatGenerator(String codeUE, int session, int n);
}
