import controladores.EstudianteController;
import entidades.Estudiante;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static spark.Spark.*;

public class app {

    List<Estudiante> estudianteList;
    public static void main(String[] args) {
        staticFileLocation("/publico");
        port(80);

        before((req, res) -> {
            req.session(true);
        });

        get("/", (req, res) -> {
            HashMap<String, String> home = new HashMap<>();
            home.put("home", "active");
            return new ModelAndView(home, "plantillas/index.vtl");
        }, new VelocityTemplateEngine());

        EstudianteController ec = new EstudianteController();
    }
}
