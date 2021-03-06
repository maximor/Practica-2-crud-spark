package controladores;

import entidades.Estudiante;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import static spark.Spark.get;
import static spark.Spark.post;

public class EstudianteController {

    public EstudianteController() {
        get("/consultar-estudiantes", (req, res) -> {

            HashMap<String, String> consultas = new HashMap<>();
            try {
                consultas.put("consulta", "active");
                if (req.session().attribute("listaEstudiante") != null) {
                    consultas.put("estudiantes", req.session().attribute("listaEstudiante"));
                } else {
                    consultas.put("estudiantes", null);
                }

            }catch (Exception e){
                System.out.println(e.getMessage());
            }
            return new ModelAndView(consultas, "plantillas/consultar.vtl");
        }, new VelocityTemplateEngine());

        get("/crear-estudiante", (req, res) ->{
            HashMap<String, String> crear = new HashMap<>();
            crear.put("crear", "active");
            return new ModelAndView(crear, "plantillas/crear-estudiante.vtl");
        }, new VelocityTemplateEngine());

        get("/crear-estudiante/:matricula", (req, res) ->{
            HashMap<String, Estudiante> crear = new HashMap<>();
            int matricula = Integer.valueOf(req.params(":matricula"));
            int listaPosition = -1;
            List<Estudiante> listaEstudiante = null;
            if (req.session().attribute("listaEstudiante") != null) {
                listaEstudiante = req.session().attribute("listaEstudiante");
                for(int i = 0; i < listaEstudiante.size(); i++){
                    if(listaEstudiante.get(i).getMatricula() == matricula){
                        crear.put("estudiante", listaEstudiante.get(i));
                        req.session().attribute("listaPosition", i);
                        i = listaEstudiante.size();
                    }
                }
            }
            return new ModelAndView(crear, "plantillas/crear-estudiante.vtl");
        }, new VelocityTemplateEngine());

        post("/crear-estudiante", (req, res) -> {
            List<Estudiante> estudiantes = new ArrayList<>();

            if(req.session().attribute("listaEstudiante") != null){
                estudiantes = req.session().attribute("listaEstudiante");
            }

            if(req.queryParams("crear") != null || req.queryParams("actualizar") != null || req.queryParams("borrar") != null) {
                String crear =req.queryParams("crear");
                if (crear != null && crear.equals("crear")) {
                    try {
                        if (req.session().attribute("listaEstudiante") != null) {
                            estudiantes = req.session().attribute("listaEstudiante");
                        }

                        estudiantes.add(new Estudiante(
                                Integer.valueOf(req.queryParams("matricula")),
                                req.queryParams("nombre"),
                                req.queryParams("apellido"),
                                req.queryParams("telefono")));
                        req.session().attribute("listaEstudiante", estudiantes);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
                System.out.println(" hola" + req.queryParams());
                String actualizar = req.queryParams("actualizar");
                if (actualizar != null && actualizar.equals("actualizar")) {
                    System.out.println("Indice: " + req.session().attribute("listaPosition"));
                    estudiantes.set(req.session().attribute("listaPosition"), new Estudiante(
                            Integer.valueOf(req.queryParams("matricula")),
                            req.queryParams("nombre"),
                            req.queryParams("apellido"),
                            req.queryParams("telefono")
                    ));

                    req.session().attribute("listaEstudiante", estudiantes);
                }

                String valor = req.queryParams("borrar");
                if (valor != null && valor.equals("borrar")) {
                    estudiantes.remove(estudiantes.get(req.session().attribute("listaPosition")));
                    req.session().attribute("listaEstudiante", estudiantes);
                }
            }


            return new ModelAndView(new HashMap<>(), "plantillas/crear-estudiante.vtl");
        }, new VelocityTemplateEngine());
    }
}
