import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@WebServlet(name = "HwServlet", value = "/HwServlet")
public class HwServlet extends HttpServlet {

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
            // do any sophisticated processing with urlParts which contains all the url params
            // TODO: process url params in `urlParts`

            res.getWriter().write("It works!");
        }

        // parse the request body -- TODO: Figure out if I further need it.
        BufferedReader br = req.getReader();
        StringBuilder sb = new StringBuilder();
        String line;
        while( (line = br.readLine()) != null){
            sb.append(line);
        }
    }
}
