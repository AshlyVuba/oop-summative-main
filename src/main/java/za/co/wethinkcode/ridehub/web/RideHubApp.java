package za.co.wethinkcode.ridehub.web;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.staticfiles.Location;

public class RideHubApp {

    private final BikeRepository repository;

    public RideHubApp(BikeRepository repository) {
        this.repository = repository;
    }

    public Javalin create() {
        Javalin app = Javalin.create(config -> config.staticFiles.add("/public", Location.CLASSPATH));

        app.get("/api/bikes", this::listBikes);
        app.get("/api/bikes/{serialNo}", this::getBike);
        app.post("/api/rentals", this::createRental);

        return app;
    }

    // Worked example: GET /api/bikes -> 200 with a JSON array of every bike.
    private void listBikes(Context ctx) {
        ctx.json(repository.findAll());
    }

    // TODO (Q8.2a): GET /api/bikes/{serialNo}
    private void getBike(Context ctx) {
        ctx.status(501);
    }

    // TODO (Q8.2b): POST /api/rentals
    private void createRental(Context ctx) {
        ctx.status(501);
    }
}
