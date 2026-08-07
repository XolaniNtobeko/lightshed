package za.co.wethinkcode.lightshed;

import com.sun.source.tree.BreakTree;
import io.javalin.Javalin;
import za.co.wethinkcode.lightshed.service.TownRepository;

import java.util.List;

public class PlaceNameServer {

    private final Javalin app;
    private final TownRepository repository;

    public PlaceNameServer(TownRepository repository){
        this.repository = repository;
        this.app = Javalin.create();

        setupRoutes();
    }

    public void setupRoutes(){
        // GET /api/towns (all or filtered)
        app.get("/api/towns", ctx -> {
            String name = ctx.queryParam("name");
            if(name != null && !name.isBlank()){
                ctx.json(repository.findByName(name));
            }
            else{
                ctx.json(repository.getAllTowns());
            }
        });
        // GET /api/towns/{name} (single town lookup)
        app.get("/api/towns/{name}", ctx -> {
            String townName = ctx.pathParam("name");
            List matchings = repository.findByName(townName);

            if(matchings.isEmpty()){
                ctx.status(404);
            }
            else{
                ctx.json(matchings.get(0));
            }
        });
    }

    public Javalin getApp(){
        return this.app;
    }
    public void start(int port){
        this.app.start(port);
    }
}
