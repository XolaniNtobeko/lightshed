package za.co.wethinkcode.lightshed.controller;

import io.javalin.http.Context;
import za.co.wethinkcode.lightshed.service.TownRepository;

public class TownController {
    private final TownRepository repository;

    public TownController(TownRepository repository){
        this.repository = repository;
    }

    public void getAll(Context ctx){
        ctx.status(501);
    }
}
