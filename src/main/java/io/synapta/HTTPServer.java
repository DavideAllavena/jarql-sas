package io.synapta;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import org.apache.clerezza.commons.rdf.Graph;
import org.apache.clerezza.commons.rdf.impl.utils.simple.SimpleGraph;
import org.apache.clerezza.rdf.core.serializedform.Serializer;
import org.apache.clerezza.rdf.jena.facade.JenaGraph;
import org.apache.clerezza.rdf.jena.storage.JenaGraphAdaptor;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.servlet.ServletOutputStream;
import java.io.*;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;


public class HTTPServer {

    public static void main(String[] args) {

        // Start embedded server at this port
        port(8080);

        // Main Page, welcome
        get("/", (request, response) -> "{\t\"data\": {\n" +
                "\t\t\"ssss\": \"XXXX\"\n" +
                "\t},\n" +
                "\t\"query\": \"construct {?s ?p ?o} where {?s ?p ?o }\"\n" +
                "}");

        // POST - Add an user
        post("/", (request, response) -> {

            JsonReader jsonReader = Json.createReader(new StringReader(request.body()));

            JsonObject jsonst = jsonReader.readObject();
            String jsonIn = jsonst.getJsonObject("data").toString();

            String query = jsonst.getJsonString("query").getString();

            jsonReader.close();
            String queryString = "construct {?s ?p ?o} where {?s ?p ?o }";
            Graph graph = execute(new ByteArrayInputStream(jsonIn.getBytes()), queryString);
            ServletOutputStream out = response.raw().getOutputStream();
            Serializer.getInstance().serialize( out , graph, "text/turtle");

            out.close();
            return 200;

        });

    }

    static public Graph execute(final InputStream in, final String queryString) {
        Graph JsonGraph = new SimpleGraph();
        JarqlParser.parse(in, JsonGraph);
        JenaGraph jg = new JenaGraph(JsonGraph);
        Model model = ModelFactory.createModelForGraph(jg);
        Query query = QueryFactory.create(queryString);
        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            Model results = qexec.execConstruct();
            return new JenaGraphAdaptor(results.getGraph());
        }

    };
}