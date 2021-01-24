package com.meli.data;

import com.meli.model.Satelite;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class SateliteLiveData {

    public Set<Satelite> satelites = new HashSet<>();
}
