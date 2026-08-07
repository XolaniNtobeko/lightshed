package za.co.wethinkcode.lightshed;

import io.javalin.Javalin;
import za.co.wethinkcode.lightshed.service.TownRepository;

public class PlaceNameServer {

    private Javalin app;
    private TownRepository repository;

    public PlaceNameServer(TownRepository repository){
        this.repository = repository;
        this.app = Javalin.create();
    }

    public Javalin getApp(){
        return this.app;
    }
    public void start(int port){
        this.app.start(port);
    }
}
