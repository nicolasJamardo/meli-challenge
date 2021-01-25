package com.meli.data;

import com.meli.model.Satellite;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class SatelliteLiveData {

    public Set<Satellite> satellites = new HashSet<>();
}
