package za.co.wethinkcode.lightshed.controller;

import io.javalin.http.Context;
import za.co.wethinkcode.lightshed.model.Town;
import za.co.wethinkcode.lightshed.service.TownRepository;

import java.util.List;

public class TownController {
    private final TownRepository repository;

    public TownController(TownRepository repository){
        this.repository = repository;
    }

    public void getAll(Context ctx){
        List<Town> towns = repository.getAllTowns();
        ctx.json(towns);
    }
}
