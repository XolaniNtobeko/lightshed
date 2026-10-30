package za.co.wethinkcode.lightshed.town;

import io.javalin.http.Context;

import java.util.List;

public class TownController {
    private final TownRepository repository;

    public TownController(TownRepository repository){
        this.repository = repository;
    }

    public void getAll(Context ctx){
        String nameQuery = ctx.queryParam("name");
        List<Town> towns = repository.findByName(nameQuery);
        ctx.json(towns);
    }
}
