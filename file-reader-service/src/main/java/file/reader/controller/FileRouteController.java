package file.reader.controller;

import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/file")
public class FileRouteController {

    @Autowired
    private ProducerTemplate producerTemplate;
    private static final Logger logger = LoggerFactory.getLogger(FileRouteController.class);

    @GetMapping("/trigger")
    public ResponseEntity<Object> triggerRoute(@RequestParam String route) {
        try {
            producerTemplate.sendBody("direct:" + route, null);
            String successMessage = "Route" + route + "' triggered successfully.";
            return ResponseEntity.ok(successMessage);
        } catch (Exception exception) {
            String errorMessage = "Failed to trigger route '" + route + "': " + exception.getMessage();
            logger.error(exception.getLocalizedMessage());
            return ResponseEntity.internalServerError().body(errorMessage);
        }
    }
}