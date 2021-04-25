import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import io.swagger.client.model.Purchase;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import ChannelPoolUtil.*;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

@WebServlet(name = "HwServlet", value = "/HwServlet")
public class HwServlet extends HttpServlet {

    private KafkaProducer<String, String> kafkaProducer;
    public static final String TOPIC = "supermarket";

    @Override
    public void init() throws ServletException {
        super.init();
        Properties prop = new Properties();
        prop.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "54.152.226.111:9092");
        prop.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        prop.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        kafkaProducer = new KafkaProducer<>(prop);

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/plain");
        String urlPath = req.getRequestURI();
        // check we have a URL!
        if (urlPath == null || urlPath.isEmpty()) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            res.getWriter().write("missing parameters");
            return;
        }

        String[] urlParts = urlPath.split("/");
        // and now validate url path and return the response status code
        // (and maybe also some value if input is valid)

        if (!isGetUrlValid(urlParts)) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            res.getWriter().write("Invalid url for GET");
        } else {
            res.setStatus(HttpServletResponse.SC_OK);
            // do any sophisticated processing with urlParts which contains all the url params
            // TODO: process url params in `urlParts`

            res.getWriter().write("Test passed!");
        }
    }

    private boolean isGetUrlValid(String[] urlPath) {
        // TODO: validate the request url path according to the API spec
        // urlPath  = "/homework_war/purchase"
        if(urlPath.length != 3) {
            return false;
        } else if(!urlPath[2].equals("purchase")){
            return false;
        }
        return true;
    }

    private boolean isPostBodyValid(String[] body) {
        // urlPath  = "/homework_war/purchase/{storeID}/customer/{custID}/date/{date}"
        if(body.length != 8){
            return false;
        }else if(!body[2].equals("purchase")){
            return false;
        }else if(!body[3].matches("[0-9]+")){
            return false;
        }else if(!body[4].equals("customer")){
            return false;
        }else if(!body[5].matches("[0-9]+")){
            return false;
        }else if(!body[6].equals("date")){
            return false;
        }else if(!body[7].matches("[0-9]+") || !isDateValid(body[7])){
            return false;
        }
        return true;
    }

    private boolean isDateValid(String dateStr){
        DateTimeFormatter dtf = DateTimeFormatter.BASIC_ISO_DATE;
        try{
            LocalDate.parse(dateStr, dtf);
        }catch (DateTimeParseException e){
            return false;
        }
        return true;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/plain");
        String urlPath = req.getRequestURI();

        // check we have a URL!
        if (urlPath == null || urlPath.isEmpty()) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            res.getWriter().write("missing parameters");
            return;
        }

        String[] urlParts = urlPath.split("/");
        // and now validate url path and return the response status code
        // (and maybe also some value if input is valid)

        if (!isPostBodyValid(urlParts)) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            res.getWriter().write("Invalid url for POST");
        } else {
            res.setStatus(HttpServletResponse.SC_CREATED);
            res.getWriter().write("It works!");
            String message = processRequest(req, urlParts);
            ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC, message);
            kafkaProducer.send(record);
            System.out.println(" [x] Sent '" + message + "'");


        }

    }

    protected String processRequest(HttpServletRequest req, String[] urlParts) throws IOException {
        // read the request body
        BufferedReader br = req.getReader();
        StringBuilder sb = new StringBuilder();
        String line;
        while( (line = br.readLine()) != null){
            sb.append(line).append("\n");
        }

        String store_id = urlParts[3];
        String customer_id = urlParts[5];
        String date = urlParts[7];
        // USE UUID to generate purchase_id
        String purchase_id = UUID.randomUUID().toString();

        // encode message into json object to pass
        JSONObject obj = new JSONObject();
        obj.put("purchase_id", purchase_id);
        obj.put("store_id", store_id);
        obj.put("customer_id", customer_id);
        obj.put("date", date);
        obj.put("items", sb.toString());

        return obj.toJSONString();

    }
}
